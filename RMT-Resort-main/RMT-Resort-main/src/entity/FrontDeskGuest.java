// @author : Ong Wei Chen 
package entity;
//entity = no input statements
//entity need const, getter, setter, tostring, equals, compareto
public class FrontDeskGuest implements Comparable<FrontDeskGuest> {

    private String confirmationNumber;
    private String name;
    private String phone;
    private int roomNumber;
    private double bill;
    private String status;
    private String payMethod;

    public FrontDeskGuest(String confirmationNumber, String name, String phone, int roomNumber, double bill, String status, String payMethod) 
    {

        this.confirmationNumber = confirmationNumber;
        this.name = name;
        this.phone = phone;
        this.roomNumber = roomNumber;
        this.bill = bill;
        this.status = status;
        this.payMethod = payMethod;
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

    public String getPayMethod() {
        return payMethod;
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

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
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
                ", payMethod='" + payMethod + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) { //determine 8num equal/not

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof FrontDeskGuest)) {
            return false;
        }

        FrontDeskGuest other = (FrontDeskGuest) obj;

        return confirmationNumber.equals(other.confirmationNumber);
    }

    @Override
    public int compareTo(FrontDeskGuest other) { //for bst search node ordering
        return this.confirmationNumber.compareTo(other.confirmationNumber); 
    }


}