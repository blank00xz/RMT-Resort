package entity;

import java.util.Objects;

/**
 * Represents a resort room that can be assigned to a guest.
 *
 * @author Your Name
 */
public class VIPRoom {

    private String roomNumber;
    private String roomType;   // e.g. DELUXE, SUITE, VILLA
    private boolean available;

    public VIPRoom() {
        this.available = true;
    }

    public VIPRoom(String roomNumber, String roomType, boolean available) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.available = available;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        VIPRoom other = (VIPRoom) obj;
        if (roomNumber == null || other.roomNumber == null) {
            return false;
        }
        return roomNumber.equalsIgnoreCase(other.roomNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomNumber == null ? null : roomNumber.toLowerCase());
    }

    @Override
    public String toString() {
        String status = available ? "AVAILABLE" : "OCCUPIED";
        return String.format("Room: %-6s | Type: %-8s | Status: %s", roomNumber, roomType, status);
    }
}
