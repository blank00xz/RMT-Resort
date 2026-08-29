package control;

import adt.housekeeping.QueueInterface;
import dao.HousekeepingDAO;
import entity.RoomTaskLog;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Control class for the Housekeeping and Task Log module.
 *
 * @author Chang Kai Zhe
 */
public class HousekeepingMaintenance {

    private static final DateTimeFormatter REPORT_TIME_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    /*
     * Main Housekeeping collection.
     * The team collection ADT is Queue.
     */
    private final QueueInterface<RoomTaskLog> taskLog;

    /*
     * Array is used only as a simple history storage mechanism
     * for rollback. It is not a Collection ADT.
     */
    private final RoomTaskLog[] undoHistory;

    private int undoHistoryCount;

    private static final int MAX_UNDO_HISTORY = 100;

    /**
     * Immutable output view returned to Boundary.
     */
    public static final class TaskView {

        private final String roomNumber;
        private final String status;
        private final String staffName;
        private final String lastUpdated;
        private final long elapsedMinutes;

        private TaskView(RoomTaskLog task, LocalDateTime now) {
            roomNumber = task.getRoomNumber();
            status = task.getStatus();
            staffName = task.getStaffName();

            lastUpdated =
                    task.getTimestamp().format(REPORT_TIME_FORMAT);

            elapsedMinutes =
                    Math.max(
                            0,
                            Duration.between(
                                    task.getTimestamp(),
                                    now).toMinutes());
        }

        public String getRoomNumber() {
            return roomNumber;
        }

        public String getStatus() {
            return status;
        }

        public String getStaffName() {
            return staffName;
        }

        public String getLastUpdated() {
            return lastUpdated;
        }

        public long getElapsedMinutes() {
            return elapsedMinutes;
        }
    }

    /**
     * Constructor.
     */
    public HousekeepingMaintenance() {

        taskLog =
                HousekeepingDAO.getSampleTaskLogs();

        undoHistory =
                new RoomTaskLog[MAX_UNDO_HISTORY];

        undoHistoryCount = 0;
    }

    /**
     * Adds a new housekeeping task.
     *
     * @param roomNumber room number
     * @param status initial status
     * @param staffName staff name
     * @return true if successfully added
     */
    public boolean addTask(
            String roomNumber,
            String status,
            String staffName) {

        if (roomNumber == null
                || !roomNumber.trim().matches("\\d{3}")
                || staffName == null
                || staffName.trim().isEmpty()
                || !staffName.trim().matches("[A-Za-z ]+")
                || !RoomTaskLog.isValidStatus(status)) {

            return false;
        }

        String cleanedRoomNumber =
                roomNumber.trim();

        if (searchByRoomNumber(
                cleanedRoomNumber) != null) {

            return false;
        }

        RoomTaskLog newTask =
                new RoomTaskLog(
                        cleanedRoomNumber,
                        status,
                        staffName.trim());

        return taskLog.enqueue(newTask);
    }

    /**
     * Linear search.
     *
     * @param roomNumber room number
     * @return 1-based position, or -1 if not found
     */
    public int findPosition(String roomNumber) {

        if (roomNumber == null
                || roomNumber.trim().isEmpty()) {

            return -1;
        }

        String target =
                roomNumber.trim();

        Object[] tasks =
                taskLog.toArray();

        for (int index = 0;
                index < tasks.length;
                index++) {

            RoomTaskLog task =
                    (RoomTaskLog) tasks[index];

            if (task != null
                    && task.getRoomNumber()
                            .equalsIgnoreCase(target)) {

                return index + 1;
            }
        }

        return -1;
    }

    /**
     * Searches a task by room number.
     *
     * @param roomNumber room number
     * @return matching task or null
     */
    public RoomTaskLog searchByRoomNumber(
            String roomNumber) {

        if (roomNumber == null) {
            return null;
        }

        String target =
                roomNumber.trim();

        Object[] tasks =
                taskLog.toArray();

        for (Object object : tasks) {

            RoomTaskLog task =
                    (RoomTaskLog) object;

            if (task != null
                    && task.getRoomNumber()
                            .equalsIgnoreCase(target)) {

                return task;
            }
        }

        return null;
    }

    /**
     * Updates the status of a housekeeping task.
     *
     * The old task is saved before updating so that
     * rollback can restore the previous state.
     *
     * @param roomNumber room number
     * @param newStatus new housekeeping status
     * @return true if updated successfully
     */
    public boolean updateStatus(
        String roomNumber,
        String newStatus) {

    if (!RoomTaskLog.isValidStatus(newStatus)) {
        return false;
    }

    RoomTaskLog current =
            searchByRoomNumber(roomNumber);

    if (current == null) {
        return false;
    }

    String currentStatus =
            current.getStatus();

    // Do not update to the same status
    if (currentStatus.equals(newStatus)) {
        return false;
    }

    /*
     * Housekeeping status must follow the normal sequence:
     *
     * Dirty
     *    ↓
     * Cleaning In Progress
     *    ↓
     * Inspected
     *    ↓
     * Ready for Check-In
     */
    if (!isNextStatus(currentStatus, newStatus)) {
        return false;
    }

    /*
     * Save the old state before updating.
     */
    if (!saveUndoSnapshot(
            new RoomTaskLog(current))) {

        return false;
    }

    /*
     * Create a new version of the task.
     */
    RoomTaskLog updated =
            new RoomTaskLog(current);

    updated.setStatus(newStatus);

    /*
     * Replace the task while preserving
     * the Queue order.
     */
    return replaceTask(
            roomNumber,
            updated);
}

    /**
     * Replaces one task in the Queue while preserving
     * the original Queue order.
     *
     * @param roomNumber room number
     * @param replacement replacement task
     * @return true if replacement succeeds
     */
    private boolean replaceTask(
            String roomNumber,
            RoomTaskLog replacement) {

        int size =
                taskLog.getNumberOfEntries();

        boolean found = false;

        for (int i = 0; i < size; i++) {

            RoomTaskLog current =
                    taskLog.dequeue();

            if (current == null) {
                continue;
            }

            if (!found
                    && current.getRoomNumber()
                            .equalsIgnoreCase(roomNumber)) {

                taskLog.enqueue(replacement);
                found = true;

            } else {

                taskLog.enqueue(current);
            }
        }


        return found;
    }

    /**
     * Saves a previous task state for rollback.
     *
     * @param snapshot previous task state
     * @return true if saved
     */
    private boolean saveUndoSnapshot(
            RoomTaskLog snapshot) {

        if (snapshot == null) {
            return false;
        }

        if (undoHistoryCount >= MAX_UNDO_HISTORY) {

            /*
             * Shift older history entries left.
             */
            for (int i = 1;
                    i < undoHistoryCount;
                    i++) {

                undoHistory[i - 1] =
                        undoHistory[i];
            }

            undoHistoryCount--;
        }

        undoHistory[undoHistoryCount] =
                snapshot;

        undoHistoryCount++;

        return true;
    }

    /**
     * Rolls back the most recent status change.
     *
     * @return true if rollback succeeds
     */
    public boolean rollbackLastChange() {

        if (undoHistoryCount == 0) {
            return false;
        }

        /*
         * Get the most recent snapshot.
         */
        RoomTaskLog previous =
                undoHistory[undoHistoryCount - 1];

        if (previous == null) {
            return false;
        }

        /*
         * Check whether the task still exists.
         */
        RoomTaskLog current =
                searchByRoomNumber(
                        previous.getRoomNumber());

        if (current == null) {
            return false;
        }

        /*
         * Restore previous version.
         */
        boolean restored =
                replaceTask(
                        previous.getRoomNumber(),
                        new RoomTaskLog(previous));

        if (!restored) {
            return false;
        }

        undoHistory[
                undoHistoryCount - 1] = null;

        undoHistoryCount--;

        return true;
    }

    /**
     * Returns number of available rollback records.
     *
     * @return undo history count
     */
    public int getUndoHistoryCount() {
        return undoHistoryCount;
    }

    /**
     * Converts status menu choice to status string.
     *
     * @param choice menu choice
     * @return status
     */
    public String mapStatusChoice(int choice) {

        switch (choice) {

            case 1:
                return RoomTaskLog.STATUS_DIRTY;

            case 2:
                return RoomTaskLog.STATUS_CLEANING;

            case 3:
                return RoomTaskLog.STATUS_INSPECTED;

            case 4:
                return RoomTaskLog.STATUS_READY;

            default:
                return null;
        }
    }

    /**
     * Searches and returns immutable task view.
     *
     * @param roomNumber room number
     * @return task view
     */
    public TaskView searchTaskViewByRoomNumber(
            String roomNumber) {

        RoomTaskLog task =
                searchByRoomNumber(roomNumber);

        if (task == null) {
            return null;
        }

        return toTaskView(
                task,
                LocalDateTime.now());
    }

    /**
     * Returns all task views.
     *
     * @return task views
     */
    public TaskView[] getAllTaskViews() {

        return toTaskViews(
                getAllTasks(),
                LocalDateTime.now());
    }

    /**
     * Generates filtered status report.
     *
     * @param filterStatus status filter
     * @return report
     */
    public TaskView[] generateStatusReportViews(
            String filterStatus) {

        return toTaskViews(
                generateStatusReport(filterStatus),
                LocalDateTime.now());
    }

    /**
     * Generates overdue report.
     *
     * @param thresholdMinutes overdue threshold
     * @return report
     */
    public TaskView[] generateOverdueReportViews(
            int thresholdMinutes) {

        return toTaskViews(
                generateOverdueReport(thresholdMinutes),
                LocalDateTime.now());
    }

    /**
     * Generates status counts.
     *
     * @return counts
     */
    public int[] generateStatusCounts() {

        int[] counts =
                new int[4];

        Object[] tasks =
                taskLog.toArray();

        for (Object object : tasks) {

            RoomTaskLog task =
                    (RoomTaskLog) object;

            if (RoomTaskLog.STATUS_DIRTY
                    .equals(task.getStatus())) {

                counts[0]++;

            } else if (RoomTaskLog.STATUS_CLEANING
                    .equals(task.getStatus())) {

                counts[1]++;

            } else if (RoomTaskLog.STATUS_INSPECTED
                    .equals(task.getStatus())) {

                counts[2]++;

            } else if (RoomTaskLog.STATUS_READY
                    .equals(task.getStatus())) {

                counts[3]++;
            }
        }

        return counts;
    }

    /**
     * Converts task array to TaskView array.
     */
    private TaskView[] toTaskViews(
            RoomTaskLog[] tasks,
            LocalDateTime now) {

        TaskView[] views =
                new TaskView[tasks.length];

        for (int i = 0;
                i < tasks.length;
                i++) {

            views[i] =
                    toTaskView(
                            tasks[i],
                            now);
        }

        return views;
    }

    /**
     * Converts one task into immutable view.
     */
    private TaskView toTaskView(
            RoomTaskLog task,
            LocalDateTime now) {

        return new TaskView(
                task,
                now);
    }

    // =========================================================
    // REPORT 1
    // Status Summary Report
    // =========================================================

    public RoomTaskLog[] generateStatusReport(
            String filterStatus) {

        if (filterStatus != null
                && !filterStatus.isEmpty()
                && !RoomTaskLog.isValidStatus(
                        filterStatus)) {

            return new RoomTaskLog[0];
        }

        Object[] rawArray =
                taskLog.toArray();

        RoomTaskLog[] filtered =
                new RoomTaskLog[rawArray.length];

        int count = 0;

        for (Object object : rawArray) {

            RoomTaskLog task =
                    (RoomTaskLog) object;

            if (filterStatus == null
                    || filterStatus.isEmpty()
                    || task.getStatus()
                            .equalsIgnoreCase(
                                    filterStatus)) {

                filtered[count] =
                        task;

                count++;
            }
        }

        RoomTaskLog[] result =
                new RoomTaskLog[count];

        for (int i = 0;
                i < count;
                i++) {

            result[i] =
                    filtered[i];
        }

        /*
         * Manual insertion sort.
         */
        sortByRoomNumber(result);

        return result;
    }

    // =========================================================
    // REPORT 2
    // Overdue Cleaning Report
    // =========================================================

    public RoomTaskLog[] generateOverdueReport(
            int thresholdMinutes) {

        if (thresholdMinutes <= 0) {
            return new RoomTaskLog[0];
        }

        Object[] rawArray =
                taskLog.toArray();

        RoomTaskLog[] filtered =
                new RoomTaskLog[rawArray.length];

        int count = 0;

        LocalDateTime now =
                LocalDateTime.now();

        for (Object object : rawArray) {

            RoomTaskLog task =
                    (RoomTaskLog) object;

            if (!task.getStatus()
                    .equalsIgnoreCase(
                            RoomTaskLog.STATUS_READY)) {

                long minutesElapsed =
                        Duration.between(
                                task.getTimestamp(),
                                now).toMinutes();

                if (minutesElapsed
                        >= thresholdMinutes) {

                    filtered[count] =
                            task;

                    count++;
                }
            }
        }

        RoomTaskLog[] result =
                new RoomTaskLog[count];

        for (int i = 0;
                i < count;
                i++) {

            result[i] =
                    filtered[i];
        }

        /*
         * Oldest task first.
         */
        sortByTimestamp(result);

        return result;
    }

    /**
     * Insertion sort by room number.
     */
    private void sortByRoomNumber(
            RoomTaskLog[] array) {

        for (int i = 1;
                i < array.length;
                i++) {

            RoomTaskLog key =
                    array[i];

            int j = i - 1;

            while (j >= 0
                    && array[j]
                            .getRoomNumber()
                            .compareTo(
                                    key.getRoomNumber()) > 0) {

                array[j + 1] =
                        array[j];

                j--;
            }

            array[j + 1] =
                    key;
        }
    }

    /**
     * Insertion sort by timestamp.
     */
    private void sortByTimestamp(
            RoomTaskLog[] array) {

        for (int i = 1;
                i < array.length;
                i++) {

            RoomTaskLog key =
                    array[i];

            int j = i - 1;

            while (j >= 0
                    && array[j]
                            .getTimestamp()
                            .isAfter(
                                    key.getTimestamp())) {

                array[j + 1] =
                        array[j];

                j--;
            }

            array[j + 1] =
                    key;
        }
    }

    /**
     * Returns all tasks as an array.
     *
     * @return all housekeeping tasks
     */
    public RoomTaskLog[] getAllTasks() {

        Object[] rawArray =
                taskLog.toArray();

        RoomTaskLog[] result =
                new RoomTaskLog[rawArray.length];

        for (int i = 0;
                i < rawArray.length;
                i++) {

            result[i] =
                    (RoomTaskLog) rawArray[i];
        }

        return result;
    }


private boolean isNextStatus(
        String currentStatus,
        String newStatus) {

    if (RoomTaskLog.STATUS_DIRTY.equals(currentStatus)) {
        return RoomTaskLog.STATUS_CLEANING.equals(newStatus);
    }

    if (RoomTaskLog.STATUS_CLEANING.equals(currentStatus)) {
        return RoomTaskLog.STATUS_INSPECTED.equals(newStatus);
    }

    if (RoomTaskLog.STATUS_INSPECTED.equals(currentStatus)) {
        return RoomTaskLog.STATUS_READY.equals(newStatus);
    }

    if (RoomTaskLog.STATUS_READY.equals(currentStatus)) {
        return RoomTaskLog.STATUS_DIRTY.equals(newStatus);
    }

    return false;
}

}