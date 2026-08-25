/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package boundary;

import control.HousekeepingMaintenance;
import control.HousekeepingMaintenance.TaskView;
import java.util.Scanner;

/**
 * Boundary class for the Housekeeping and Task Log module.
 *
 * @author [Chang Kai Zhe]
 */
public class HousekeepingUI {

    private static final String LINE =
            "======================================================================";

    private static final String SHORT_LINE =
            "----------------------------------------------------------------------";

    private final Scanner scanner;
    private final HousekeepingMaintenance control;


    public HousekeepingUI() {
        this(new Scanner(System.in));
    }

    public HousekeepingUI(Scanner scanner) {
        if (scanner == null) {
            throw new IllegalArgumentException("Scanner cannot be null.");
        }

        this.scanner = scanner;
        control = new HousekeepingMaintenance();
    }

    /**
     * Displays and controls the main Housekeeping menu.
     */
    public void runMenu() {
        int choice;

        do {
            printMenu();
            choice = readIntInRange(
                    "Enter choice: ", 0, 7);

            System.out.println();

            switch (choice) {
                case 1:
                    addTaskUI();
                    break;

                case 2:
                    updateStatusUI();
                    break;

                case 3:
                    rollbackUI();
                    break;

                case 4:
                    searchUI();
                    break;

                case 5:
                    listAllUI();
                    break;

                case 6:
                    statusReportUI();
                    break;

                case 7:
                    overdueReportUI();
                    break;

                case 0:
                    System.out.println(
                            "Returning to Resort Management System...");
                    break;

                default:
                    // readIntInRange() prevents this case.
                    break;
            }

        } while (choice != 0);
    }

    /**
     * Displays the main Housekeeping menu.
     */
    private void printMenu() {
        System.out.println();
        System.out.println(LINE);
        System.out.println("              HOUSEKEEPING & TASK LOG");
        System.out.println(LINE);
        System.out.println("  1. Add New Task");
        System.out.println("  2. Update Room Status");
        System.out.println("  3. Rollback Last Status Change");
        System.out.println("  4. Search Task by Room Number");
        System.out.println("  5. List All Tasks");
        System.out.println("  6. Status Summary Report");
        System.out.println("  7. Overdue Cleaning Report");
        System.out.println("  0. Return to Main Menu");
        System.out.println(LINE);
    }

    /**
     * Displays a heading for one operation.
     *
     * @param title operation title
     */
    private void printSectionTitle(String title) {
        System.out.println(LINE);
        System.out.println("  " + title);
        System.out.println(LINE);
    }

    /**
     * Reads an integer and repeats until valid.
     *
     * @param prompt input prompt
     * @return valid integer
     */
    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);

            } catch (NumberFormatException exception) {
                System.out.println(
                        "[ERROR] Please enter a whole number.");
            }
        }
    }

    /**
     * Reads an integer within a specified range.
     *
     * @param prompt input prompt
     * @param minimum minimum accepted value
     * @param maximum maximum accepted value
     * @return valid integer in the specified range
     */
    private int readIntInRange(
            String prompt,
            int minimum,
            int maximum) {

        while (true) {
            int value = readInt(prompt);

            if (value >= minimum
                    && value <= maximum) {
                return value;
            }

            System.out.printf(
                    "[ERROR] Please enter a number from %d to %d.%n",
                    minimum,
                    maximum);
        }
    }

    /**
     * Reads and validates a three-digit room number.
     *
     * @param prompt input prompt
     * @return valid room number
     */
    private String readRoomNumber(String prompt) {
        while (true) {
            System.out.print(prompt);
            String roomNumber =
                    scanner.nextLine().trim();

            if (roomNumber.matches("\\d{3}")) {
                return roomNumber;
            }

            System.out.println(
                    "[ERROR] Room number must contain exactly 3 digits.");
        }
    }

    /**
     * Reads and validates a staff name.
     *
     * @param prompt input prompt
     * @return valid staff name
     */
    private String readStaffName(String prompt) {
        while (true) {
            System.out.print(prompt);
            String staffName =
                    scanner.nextLine().trim();

            if (staffName.isEmpty()) {
                System.out.println(
                        "[ERROR] Staff name cannot be empty.");

            } else if (!staffName.matches("[A-Za-z ]+")) {
                System.out.println(
                        "[ERROR] Staff name can only contain letters and spaces.");

            } else {
                return staffName;
            }
        }
    }

    /**
     * Displays the available housekeeping statuses.
     */
    private void printStatusMenu() {
        System.out.println();
        System.out.println("  1. Dirty");
        System.out.println("  2. Cleaning In Progress");
        System.out.println("  3. Inspected");
        System.out.println("  4. Ready for Check-In");
    }

    /**
     * Reads a valid housekeeping status.
     *
     * @param prompt input prompt
     * @return selected housekeeping status
     */
    private String readStatus(String prompt) {
        printStatusMenu();

        int choice =
                readIntInRange(prompt, 1, 4);

        return mapStatus(choice);
    }

    /**
     * Adds a new housekeeping task.
     */
    private void addTaskUI() {
        printSectionTitle("ADD NEW HOUSEKEEPING TASK");

        String roomNumber =
                readRoomNumber("Room number: ");

        String staffName =
                readStaffName("Staff name : ");

        String status =
                readStatus("Initial status: ");

        System.out.println();
        System.out.println("Please confirm the new task:");
        System.out.println("  Room   : " + roomNumber);
        System.out.println("  Staff  : " + staffName);
        System.out.println("  Status : " + status);

        if (!readConfirmation("Add this task? (Y/N): ")) {
            System.out.println(
                    "[INFO] Add operation cancelled.");
            return;
        }

        boolean success =
                control.addTask(
                        roomNumber,
                        status,
                        staffName);

        if (success) {
            System.out.println(
                    "[SUCCESS] Task added successfully.");
        } else {
            System.out.println(
                    "[FAILED] Unable to add task. "
                    + "The room may already have an active task.");
        }
    }

    /**
     * Updates the status of an existing task.
     */
    private void updateStatusUI() {
        printSectionTitle("UPDATE ROOM STATUS");

        String roomNumber =
                readRoomNumber("Room number: ");

        TaskView existingTask =
                control.searchTaskViewByRoomNumber(roomNumber);

        if (existingTask == null) {
            System.out.println(
                    "[FAILED] No task found for room "
                    + roomNumber + ".");
            return;
        }

        System.out.println();
        System.out.println("Current task:");
        printSingleTask(existingTask);

        String newStatus =
                readStatus("New status: ");

        if (existingTask.getStatus()
                .equals(newStatus)) {

            System.out.println(
                    "[INFO] The room is already in that status.");
            return;
        }

        System.out.println();
        System.out.println(
                "Status change: "
                + existingTask.getStatus()
                + " -> "
                + newStatus);

        if (!readConfirmation(
                "Confirm status update? (Y/N): ")) {

            System.out.println(
                    "[INFO] Update operation cancelled.");
            return;
        }

        boolean success =
                control.updateStatus(
                        roomNumber,
                        newStatus);

        if (success) {
            System.out.println(
                    "[SUCCESS] Room status updated successfully.");

            System.out.println(
                    "[INFO] Available rollback records: "
                    + control.getUndoHistoryCount());

        } else {
            System.out.println(
                    "[FAILED] Unable to update room status.");
        }
    }

    /**
     * Rolls back the most recent status change.
     */
    private void rollbackUI() {
        printSectionTitle("ROLLBACK LAST STATUS CHANGE");

        int undoCount =
                control.getUndoHistoryCount();

        if (undoCount == 0) {
            System.out.println(
                    "[INFO] No status changes are available to rollback.");
            return;
        }

        System.out.println(
                "Available rollback records: "
                + undoCount);

        if (!readConfirmation(
                "Rollback the latest status change? (Y/N): ")) {

            System.out.println(
                    "[INFO] Rollback cancelled.");
            return;
        }

        boolean success =
                control.rollbackLastChange();

        if (success) {
            System.out.println(
                    "[SUCCESS] Latest status change rolled back.");

            System.out.println(
                    "[INFO] Remaining rollback records: "
                    + control.getUndoHistoryCount());

        } else {
            System.out.println(
                    "[FAILED] Unable to rollback the latest change.");
        }
    }

    /**
     * Searches for a task using its room number.
     */
    private void searchUI() {
        printSectionTitle("SEARCH TASK");

        String roomNumber =
                readRoomNumber("Room number: ");

        TaskView result =
                control.searchTaskViewByRoomNumber(roomNumber);

        if (result == null) {
            System.out.println(
                    "[INFO] No task found for room "
                    + roomNumber + ".");
            return;
        }

        System.out.println();
        System.out.println("Task found:");
        printSingleTask(result);
    }

    /**
     * Displays all housekeeping tasks.
     */
    private void listAllUI() {
        TaskView[] allTasks =
                control.getAllTaskViews();

        printReport(
                "ALL HOUSEKEEPING TASKS",
                allTasks);
    }

    /**
     * Generates a status-filtered report.
     */
    private void statusReportUI() {
        printSectionTitle("STATUS SUMMARY REPORT");

        System.out.println("  0. All Statuses");
        printStatusMenu();

        int choice =
                readIntInRange(
                        "Status filter: ", 0, 4);

        String filter =
                choice == 0
                        ? null
                        : mapStatus(choice);

        TaskView[] report =
                control.generateStatusReportViews(filter);

        String title =
                filter == null
                        ? "STATUS SUMMARY - ALL STATUSES"
                        : "STATUS SUMMARY - " + filter.toUpperCase();

        printStatusSummary();
        printReport(title, report);
    }

    /**
     * Generates an overdue cleaning report.
     */
    private void overdueReportUI() {
        printSectionTitle("OVERDUE CLEANING REPORT");

        int threshold;

        do {
            threshold =
                    readInt(
                            "Overdue threshold in minutes: ");

            if (threshold <= 0) {
                System.out.println(
                        "[ERROR] Threshold must be greater than zero.");
            }

        } while (threshold <= 0);

        TaskView[] report =
                control.generateOverdueReportViews(
                        threshold);

        printOverdueReport(
                "OVERDUE TASKS - "
                + threshold
                + " MINUTES OR MORE",
                report);
    }

    /**
     * Displays a formatted report.
     *
     * @param title report title
     * @param data report records
     */
    private void printReport(
            String title,
            TaskView[] data) {

        System.out.println();
        System.out.println(LINE);
        System.out.println("  " + title);
        System.out.println(LINE);

        if (data == null || data.length == 0) {
            System.out.println(
                    "  No records found.");
            System.out.println(LINE);
            return;
        }

        System.out.printf(
                "%-8s | %-22s | %-15s | %-19s%n",
                "Room",
                "Status",
                "Staff",
                "Last Updated");

        System.out.println(SHORT_LINE);

        for (TaskView task : data) {
            printTaskRow(task);
        }

        System.out.println(SHORT_LINE);

        System.out.printf(
                "Total records: %d%n",
                data.length);

        System.out.println(LINE);
    }

    /**
     * Displays one task with a table heading.
     *
     * @param task task to display
     */
    private void printSingleTask(
            TaskView task) {

        System.out.printf(
                "%-8s | %-22s | %-15s | %-19s%n",
                "Room",
                "Status",
                "Staff",
                "Last Updated");

        System.out.println(SHORT_LINE);
        printTaskRow(task);
    }

    private void printTaskRow(TaskView task) {
        System.out.printf(
                "%-8s | %-22s | %-15s | %-19s%n",
                task.getRoomNumber(),
                task.getStatus(),
                task.getStaffName(),
                task.getLastUpdated());
    }

    private void printStatusSummary() {
        int[] counts = control.generateStatusCounts();
        int total = counts[0] + counts[1] + counts[2] + counts[3];

        System.out.println();
        System.out.println("STATUS COUNTS");
        System.out.println(SHORT_LINE);
        System.out.printf("%-24s : %d%n", "Dirty", counts[0]);
        System.out.printf("%-24s : %d%n", "Cleaning In Progress", counts[1]);
        System.out.printf("%-24s : %d%n", "Inspected", counts[2]);
        System.out.printf("%-24s : %d%n", "Ready for Check-In", counts[3]);
        System.out.printf("%-24s : %d%n", "Total", total);
    }

    private void printOverdueReport(
            String title,
            TaskView[] data) {

        System.out.println();
        System.out.println(LINE);
        System.out.println("  " + title);
        System.out.println(LINE);

        if (data == null || data.length == 0) {
            System.out.println("  No overdue tasks found.");
            System.out.println(LINE);
            return;
        }

        System.out.printf(
                "%-8s | %-22s | %-15s | %-19s | %-10s%n",
                "Room", "Status", "Staff", "Last Updated", "Elapsed");
        System.out.println(
                "-------------------------------------------------------------------------------------");

        for (TaskView task : data) {
            System.out.printf(
                    "%-8s | %-22s | %-15s | %-19s | %d min%n",
                    task.getRoomNumber(),
                    task.getStatus(),
                    task.getStaffName(),
                    task.getLastUpdated(),
                    task.getElapsedMinutes());
        }

        System.out.println(
                "-------------------------------------------------------------------------------------");
        System.out.println("Total overdue tasks: " + data.length);
        System.out.println(LINE);
    }

    /**
     * Reads a yes/no confirmation.
     *
     * @param prompt confirmation prompt
     * @return true for yes and false for no
     */
    private boolean readConfirmation(
            String prompt) {

        while (true) {
            System.out.print(prompt);

            String input =
                    scanner.nextLine()
                            .trim()
                            .toUpperCase();

            if (input.equals("Y")
                    || input.equals("YES")) {
                return true;
            }

            if (input.equals("N")
                    || input.equals("NO")) {
                return false;
            }

            System.out.println(
                    "[ERROR] Please enter Y or N.");
        }
    }

    /**
     * Maps a menu choice to a housekeeping status.
     *
     * @param choice status-menu choice
     * @return housekeeping status, or null for invalid choice
     */
    private String mapStatus(int choice) {
        return control.mapStatusChoice(choice);
    }

    /**
     * Runs only the Housekeeping module for testing.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        HousekeepingUI ui =
                new HousekeepingUI();

        ui.runMenu();
    }
}