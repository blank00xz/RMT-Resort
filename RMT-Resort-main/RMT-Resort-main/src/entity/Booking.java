package entity;

public class Booking {

    private final String confirmationNumber;
    private final Guest guest;
    private final Room room;

    private String status;

    public Booking(
            String confirmationNumber,
            Guest guest,
            Room room) {

        this.confirmationNumber = confirmationNumber;
        this.guest = guest;
        this.room = room;

        this.status = "CONFIRMED";
    }

    // ==============================
    // Getter
    // ==============================

    public String getConfirmationNumber() {
        return confirmationNumber;
    }

    public Guest getGuest() {
        return guest;
    }

    public Room getRoom() {
        return room;
    }

    public String getStatus() {
        return status;
    }

    // ==============================
    // Setter
    // ==============================

    public void setStatus(String status) {
        this.status = status;
    }

    // ==============================
    // toString
    // ==============================

    @Override
    public String toString() {

        return "\n================================"
                + "\nConfirmation Number : "
                + confirmationNumber
                + "\nGuest Name          : "
                + guest.getGuestName()
                + "\nPhone Number        : "
                + guest.getPhoneNumber()
                + "\nRegister Method     : "
                + guest.getRegisterMethod()
                + "\nRoom Number         : "
                + room.getRoomNumber()
                + "\nRoom Type           : "
                + room.getRoomType()
                + "\nRoom Status         : "
                + room.getRoomStatus()
                + "\nCheck-in Date       : "
                + guest.getCheckInDate()
                + "\nCheck-out Date      : "
                + guest.getCheckOutDate()
                + "\nBooking Status      : "
                + status
                + "\n================================";
    }
}