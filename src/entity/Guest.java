package entity;
//entity = no input statements
//entity need const, getter, setter, tostring, equals, compareto
public class Guest implements Comparable<Guest> {

    private String confirmationNumber;
    private String name;
    private String phone;
    private int roomNumber;
    private double bill;
    private String status;

    public Guest(String confirmationNumber, String name, String phone, int roomNumber, double bill, String status) 
    {

        this.confirmationNumber = confirmationNumber;
        this.name = name;
        this.phone = phone;
        this.roomNumber = roomNumber;
        this.bill = bill;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setConfirmationNumber(String confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setBill(double bill) {
        this.bill = bill;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Guest{" +
                "confirmationNumber='" + confirmationNumber + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", roomNumber=" + roomNumber +
                ", bill=" + bill +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) { //determine 8num equal/not

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Guest)) {
            return false;
        }

        Guest other = (Guest) obj;

        return confirmationNumber.equals(other.confirmationNumber);
    }

    @Override
    public int compareTo(Guest other) { //for bst search node ordering
        return this.confirmationNumber.compareTo(other.confirmationNumber); 
    }


}