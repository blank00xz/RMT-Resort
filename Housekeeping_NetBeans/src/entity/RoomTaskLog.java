/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt
 * to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java
 * to edit this template
 */
package entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Entity class representing a single housekeeping task log entry.
 *
 * @author [Chang Kai Zhe]
 */
public class RoomTaskLog {

    public static final String STATUS_DIRTY =
            "Dirty";

    public static final String STATUS_CLEANING =
            "Cleaning In Progress";

    public static final String STATUS_INSPECTED =
            "Inspected";

    public static final String STATUS_READY =
            "Ready for Check-In";

    private String roomNumber;
    private String status;
    private String staffName;
    private LocalDateTime timestamp;

    /**
     * Creates a new housekeeping task log.
     *
     * @param roomNumber room number containing exactly three digits
     * @param status housekeeping status
     * @param staffName name of the assigned staff member
     */
    public RoomTaskLog(
            String roomNumber,
            String status,
            String staffName) {

        this(roomNumber, status, staffName, LocalDateTime.now());
    }

    /**
     * Creates a task log with a supplied timestamp for prototype sample data.
     *
     * @param roomNumber room number containing exactly three digits
     * @param status housekeeping status
     * @param staffName name of the assigned staff member
     * @param timestamp date and time of the latest status change
     */
    public RoomTaskLog(
            String roomNumber,
            String status,
            String staffName,
            LocalDateTime timestamp) {

        validateRoomNumber(roomNumber);
        validateStatus(status);
        validateStaffName(staffName);

        if (timestamp == null) {
            throw new IllegalArgumentException(
                    "Timestamp cannot be null.");
        }

        this.roomNumber =
                roomNumber.trim();

        this.status =
                status;

        this.staffName =
                staffName.trim();

        this.timestamp = timestamp;
    }

    /**
     * Creates a snapshot of an existing task before a status update.
     * This copy constructor is used by the Housekeeping Undo Module.
     *
     * @param other task log to copy
     */
    public RoomTaskLog(RoomTaskLog other) {
        if (other == null) {
            throw new IllegalArgumentException(
                    "Task log to copy cannot be null.");
        }

        this.roomNumber =
                other.roomNumber;

        this.status =
                other.status;

        this.staffName =
                other.staffName;

        this.timestamp =
                other.timestamp;
    }

    /**
     * Checks whether a housekeeping status is valid.
     *
     * @param status status to validate
     * @return true when the status is one of the supported statuses
     */
    public static boolean isValidStatus(
            String status) {

        return STATUS_DIRTY.equals(status)
                || STATUS_CLEANING.equals(status)
                || STATUS_INSPECTED.equals(status)
                || STATUS_READY.equals(status);
    }

    /**
     * Validates a room number.
     *
     * @param roomNumber room number to validate
     */
    private static void validateRoomNumber(
            String roomNumber) {

        if (roomNumber == null
                || !roomNumber.trim().matches("\\d{3}")) {

            throw new IllegalArgumentException(
                    "Room number must contain exactly 3 digits.");
        }
    }

    /**
     * Validates a housekeeping status.
     *
     * @param status status to validate
     */
    private static void validateStatus(
            String status) {

        if (!isValidStatus(status)) {
            throw new IllegalArgumentException(
                    "Invalid housekeeping status.");
        }
    }

    /**
     * Validates a staff name.
     *
     * @param staffName staff name to validate
     */
    private static void validateStaffName(
            String staffName) {

        if (staffName == null
                || staffName.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Staff name cannot be empty.");
        }

        if (!staffName.trim().matches(
                "[A-Za-z ]+")) {

            throw new IllegalArgumentException(
                    "Staff name can only contain letters and spaces.");
        }
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(
            String roomNumber) {

        validateRoomNumber(roomNumber);

        this.roomNumber =
                roomNumber.trim();
    }

    public String getStatus() {
        return status;
    }

    /**
     * Updates the housekeeping status and refreshes its timestamp.
     *
     * @param status new housekeeping status
     */
    public void setStatus(String status) {
        validateStatus(status);

        this.status =
                status;

        this.timestamp =
                LocalDateTime.now();
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(
            String staffName) {

        validateStaffName(staffName);

        this.staffName =
                staffName.trim();
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof RoomTaskLog)) {
            return false;
        }

        RoomTaskLog other = (RoomTaskLog) object;
        return roomNumber.equalsIgnoreCase(other.roomNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomNumber.toLowerCase());
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "dd-MM-yyyy HH:mm:ss");

        return String.format(
                "%-8s | %-22s | %-15s | %s",
                roomNumber,
                status,
                staffName,
                timestamp.format(formatter));
    }
}