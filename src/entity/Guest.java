package entity;
//entity = no input statements
public class Guest {

    private String confirmationNumber;
    private String name;
    private String phone;
    private int roomNumber;
    private double bill;

    public Guest(String confirmationNumber, String name, String phone, int roomNumber, double bill) 
    {

        this.confirmationNumber = confirmationNumber;
        this.name = name;
        this.phone = phone;
        this.roomNumber = roomNumber;
        this.bill = bill;
    }


    public String getConfirmationNumber() {
        return confirmationNumber;
    }


    public String getName() {
        return name;
    }


    public String getPhone() {
        return phone;
    }


    public int getRoomNumber() {
        return roomNumber;
    }


    public double getBill() {
        return bill;
    }


    public void displayGuest() {

        System.out.println("\n");
        System.out.println("Guest Information");
        System.out.println("----------------------");
        System.out.println("Confirmation Number: " + confirmationNumber);
        System.out.println("Name: " + name);
        System.out.println("Phone: " + phone);
        System.out.println("Room Number: " + roomNumber);
        System.out.println("Bill: RM " + bill);
    }
}