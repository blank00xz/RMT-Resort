/**
 * Author: Karlson Tan Zhi Ming
 * Class: Room
 * Description: Stores room information for the Resort Booking System.
 */

package entity;

public class Room {

    private final int roomNumber;
    private final String roomType;
    private final double pricePerNight;

    private String roomStatus;

    public Room(
            int roomNumber,
            String roomType,
            double pricePerNight) {

        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;

        this.roomStatus = "READY FOR CHECK-IN";
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public String getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(String roomStatus) {
        this.roomStatus = roomStatus;
    }

    public boolean isAvailable() {

        return roomStatus.equals(
                "READY FOR CHECK-IN"
        );
    }

    @Override
    public String toString() {

        return "Room " + roomNumber
                + " | Type: " + roomType
                + " | RM "
                + String.format("%.2f", pricePerNight)
                + "/night"
                + " | Status: "
                + roomStatus;
    }
}