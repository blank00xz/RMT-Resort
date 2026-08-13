package control;

import entity.Guest;
import adt.BST;
import adt.BSTInterface;

public class FrontDesk {
    //frontdesk only need to know bstinterface can do add,search,isempty
    private BSTInterface guestTree;

    //constructor
    public FrontDesk() {

        guestTree = new BST();
        loadGuests();

    }

    private void loadGuests(){
    Guest guest1 = new Guest(
            "12345678",
            "Bryan Ong",
            "0123456789",
            305,
            750.00
    );

    Guest guest2 = new Guest(
            "10000000",
            "Alice Tan",
            "0134567890",
            201,
            500.00
    );

    Guest guest3 = new Guest(
            "15000000",
            "John Lim",
            "0145678901",
            402,
            900.00
    );


    guestTree.add(guest1);
    guestTree.add(guest2);
    guestTree.add(guest3);

    }

    public void searchGuest(String confirmationNumber){
        Guest guest = guestTree.search(confirmationNumber);

        if (guest != null){
        System.out.println("\nGuest found!");
        System.out.println("Name: " + guest.getName());
        System.out.println("Confirmation Number: " + guest.getConfirmationNumber());
        System.out.println("Phone: " + guest.getPhone());
        System.out.println("Room Number: " + guest.getRoomNumber());

        } else {

        System.out.println("\nGuest not found.");
        }
    }

    public void searchBill(String confirmationNumber){
        Guest guest = guestTree.search(confirmationNumber);

        if (guest != null){
        System.out.println("\nBill Information");
        System.out.println("----------------");
        System.out.println("Guest: " + guest.getName());
        System.out.println("Room Number: " + guest.getRoomNumber());
        System.out.println("Total Bill: RM " + guest.getBill());

        } else {

        System.out.println("\nGuest not found.");
        }
    }


}