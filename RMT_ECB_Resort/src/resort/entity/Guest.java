/**
 * Author: Karlson Tan Zhi Ming
 * Class: Guest
 * Description: Stores guest information for the Resort Booking System.
 */

package resort.entity;

public class Guest {

    private final String confirmationNumber;
    private final String guestName;
    private final String phoneNumber;
    private final int numberOfGuests;
    private String registerMethod;
    private String checkInDate;
    private String checkOutDate;

    public Guest(
            String confirmationNumber,
            String guestName,
            String phoneNumber) {

        this(
            confirmationNumber,
            guestName,
            phoneNumber,
            1
        );
    }

    public Guest(
            String confirmationNumber,
            String guestName,
            String phoneNumber,
            int numberOfGuests) {

        this.confirmationNumber = confirmationNumber;
        this.guestName = guestName;
        this.phoneNumber = phoneNumber;
        this.numberOfGuests = numberOfGuests;
        this.registerMethod = "N/A";
        this.checkInDate = "N/A";
        this.checkOutDate = "N/A";
    }

    public String getConfirmationNumber() {
        return confirmationNumber;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public String getRegisterMethod() {
        return registerMethod;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setRegisterMethod(String registerMethod) {
        this.registerMethod = registerMethod;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    @Override
    public String toString() {

        return "\nConfirmation Number : " + confirmationNumber
                + "\nGuest Name          : " + guestName
                + "\nPhone Number        : " + phoneNumber
                + "\nRegister Method     : " + registerMethod
                + "\nCheck-in Date       : " + checkInDate
                + "\nCheck-out Date      : " + checkOutDate;
    }
}