// @author : Ong Wei Chen 
//frontdesk = storing guests
//bst = storing objects T to be compared 
package control;

import entity.FrontDeskGuest;
import adt.frontdesk.BST;
import adt.frontdesk.BSTInterface;
import java.util.Iterator;
import adt.frontdesk.QueueInterface;
import adt.frontdesk.ArrayQueue;

public class FrontDesk {
    //frontdesk only need to know bstinterface can do add,search,isempty and stored objects are guest
    private BSTInterface<FrontDeskGuest> guestTree;

    //constructor
    public FrontDesk() {

        guestTree = new BST<FrontDeskGuest>(); //search by 8num
        loadGuests();

    }

    private void loadGuests() {

        FrontDeskGuest guest1 = new FrontDeskGuest(
                "12345678",
                "Bryan",
                "0123456789",
                305,
                750.00,
                "Checked In",
                "Cash"

        );

        FrontDeskGuest guest2 = new FrontDeskGuest(
                "10000000",
                "Alice",
                "0134567890",
                201,
                500.00,
                "Checked In",
                "Credit Card"
        );

        FrontDeskGuest guest3 = new FrontDeskGuest(
                "15000000",
                "John",
                "0145678901",
                402,
                900.00,
                "Checked Out",
                "Debit Card"
        );

        FrontDeskGuest guest4 = new FrontDeskGuest(
                "11000000",
                "Sarah",
                "0156789012",
                102,
                350.00,
                "Checked In",
                "Online Payment"
        );

        FrontDeskGuest guest5 = new FrontDeskGuest(
                "13000000",
                "Daniel",
                "0167890123",
                308,
                1200.00,
                "Checked In",
                "Debit Card"
        );

        FrontDeskGuest guest6 = new FrontDeskGuest(
                "17000000",
                "Emily",
                "0178901234",
                501,
                650.00,
                "Checked Out",
                "Online Payment"
        );

        FrontDeskGuest guest7 = new FrontDeskGuest(
                "10500000",
                "Michael",
                "0189012345",
                205,
                450.00,
                "Checked Out",
                "Credit Card"
        );

        FrontDeskGuest guest8 = new FrontDeskGuest(
                "14000000",
                "Jessica",
                "0190123456",
                410,
                1500.00,
                "Checked In",
                "Cash"
        );

        FrontDeskGuest guest9 = new FrontDeskGuest(
                "12500000",
                "Kevin",
                "0111234567",
                307,
                800.00,
                "Checked In",
                "Online Payment"
        );

        FrontDeskGuest guest10 = new FrontDeskGuest(
                "16000000",
                "Rachel",
                "0122345678",
                305,
                550.00,
                "Checked Out",
                "Credit Card"
        );


        guestTree.add(guest1);
        guestTree.add(guest2);
        guestTree.add(guest3);
        guestTree.add(guest4);
        guestTree.add(guest5);
        guestTree.add(guest6);
        guestTree.add(guest7);
        guestTree.add(guest8);
        guestTree.add(guest9);
        guestTree.add(guest10);
    }

    public void searchGuest(String confirmationNumber) {

        FrontDeskGuest searchKey = new FrontDeskGuest(
                confirmationNumber,
                "",
                "",
                0,
                0.0,
                "",
                ""
        );

        FrontDeskGuest guest = guestTree.getEntry(searchKey);

        if (guest != null) {

            System.out.println("\nGuest found!");
            System.out.println("Name: " + guest.getName());
            System.out.println("Confirmation Number: " + guest.getConfirmationNumber());
            System.out.println("Phone: " + guest.getPhone());
            System.out.println("Room Number: " + guest.getRoomNumber());

        } else {

            System.out.println("\nGuest not found.");
        }
    }

    public void searchBill(String confirmationNumber) {

        FrontDeskGuest searchKey = new FrontDeskGuest(
                confirmationNumber,
                "",
                "",
                0,
                0.0,
                "",
                ""
        );

        FrontDeskGuest guest = guestTree.getEntry(searchKey);

        if (guest != null) {

            System.out.println("\nBill Information");
            System.out.println("----------------");
            System.out.println("Guest: " + guest.getName());
            System.out.println("Room Number: " + guest.getRoomNumber());
            System.out.println("Total Bill: RM " + guest.getBill());
            System.out.println("Status: " + guest.getStatus());
            System.out.println("Payment Method: " + guest.getPayMethod());

        } else {

            System.out.println("\nGuest not found.");
        }
    }

    public void displayAllGuests() {

        Iterator<FrontDeskGuest> iterator = guestTree.getInOrderIterator();

        System.out.println("\n============ All Guests ============");

        while (iterator.hasNext()) {

            FrontDeskGuest guest = iterator.next();

            System.out.println(guest);
        }
    }

    //bridge between bst and report
    public QueueInterface<FrontDeskGuest> getGuestQueue() { 

        QueueInterface<FrontDeskGuest> guestQueue = new ArrayQueue<>();

        Iterator<FrontDeskGuest> iterator = guestTree.getInOrderIterator();

        //transfers every guest into queue for report
        while (iterator.hasNext()) {

            FrontDeskGuest guest = iterator.next();
            guestQueue.enqueue(guest);
        }

        return guestQueue;
    }

    public void generateBillingReport(String status, double minimumBill) {

        QueueInterface<FrontDeskGuest> guestQueue = getGuestQueue();
        QueueInterface<FrontDeskGuest> filteredQueue = new ArrayQueue<>();

        // filter guests
        while (!guestQueue.isEmpty()) {

            FrontDeskGuest guest = guestQueue.dequeue();

            boolean statusMatches =
                    status.equals("All") ||
                    guest.getStatus().equals(status);

            boolean billMatches =
                    guest.getBill() >= minimumBill;

            if (statusMatches && billMatches) {
                filteredQueue.enqueue(guest);
            }
        }

        // count filtered guests
        int count = 0;

        QueueInterface<FrontDeskGuest> tempQueue = new ArrayQueue<>();

        while (!filteredQueue.isEmpty()) {

            FrontDeskGuest guest = filteredQueue.dequeue();

            tempQueue.enqueue(guest);
            count++;
        }

        // no matching guests
        if (count == 0) {

            System.out.println("\n=== Guest Billing Report ===");
            System.out.println("No guests match the selected criteria.");
            return;
        }

        // transfer queue into array
        FrontDeskGuest[] guests = new FrontDeskGuest[count];

        for (int i = 0; i < count; i++) {

            guests[i] = tempQueue.dequeue();
        }

        // sort by bill: highest to lowest
        sortByBillDescending(guests);

        // generate report
        double totalBill = 0;

        System.out.println("\n=== Guest Billing Report ===");
        System.out.println("----------------------------------------------------------------------------------");
        System.out.println("Status: " + status);
        System.out.println("Minimum Bill: RM " + minimumBill);
        System.out.println("Sort: Bill (Highest to Lowest)");
        System.out.println("----------------------------------------------------------------------------------");
        System.out.printf("%-18s %-8s %-12s %-15s %-15s%n",
                "Guest", "Room", "Bill", "Status", "Payment Method");

        System.out.println("----------------------------------------------------------------------------------");

        for (FrontDeskGuest guest : guests) {

            System.out.printf("%-18s %-8d RM%-10.2f %-15s %-15s%n",
                    guest.getName(),
                    guest.getRoomNumber(),
                    guest.getBill(),
                    guest.getStatus(),
                    guest.getPayMethod());

            totalBill += guest.getBill();
        }

        double averageBill = totalBill / count;

        System.out.println("----------------------------------------------------------------------------------");
        System.out.println("Total Guests: " + count);
        System.out.printf("Total Billing: RM %.2f%n", totalBill);
        System.out.printf("Average Bill: RM %.2f%n", averageBill);
    }

    public void generateOccupancyReport(String status, int minimumRoom, int maximumRoom) {

        QueueInterface<FrontDeskGuest> guestQueue = getGuestQueue();
        QueueInterface<FrontDeskGuest> filteredQueue = new ArrayQueue<>();

        // Filter guests
        while (!guestQueue.isEmpty()) {

            FrontDeskGuest guest = guestQueue.dequeue();

            boolean statusMatches =
                    status.equals("All") ||
                    guest.getStatus().equals(status);

            boolean roomMatches =
                    guest.getRoomNumber() >= minimumRoom &&
                    guest.getRoomNumber() <= maximumRoom;

            if (statusMatches && roomMatches) {
                filteredQueue.enqueue(guest);
            }
        }

        // transfer filtered guests into an array
        int count = 0;
        QueueInterface<FrontDeskGuest> tempQueue = new ArrayQueue<>();

        while (!filteredQueue.isEmpty()) {

            FrontDeskGuest guest = filteredQueue.dequeue();

            tempQueue.enqueue(guest);
            count++;
        }

        // no matching guests
        if (count == 0) {

            System.out.println("\n=== Guest Occupancy Report ===");
            System.out.println("No guests match the selected criteria.");
            return;
        }

        FrontDeskGuest[] guests = new FrontDeskGuest[count];

        for (int i = 0; i < count; i++) {

            guests[i] = tempQueue.dequeue();
        }

        // sort by room number: lowest to highest
        sortByRoomAscending(guests);

        // generate report
        System.out.println("\n=== Guest Occupancy Report ===");
        System.out.println("-----------------------------------------------");
        System.out.println("Status: " + status);
        System.out.println("Room Range: " + minimumRoom + " - " + maximumRoom);
        System.out.println("Sort: Room Number (Lowest to Highest)");
        System.out.println("-----------------------------------------------");

        System.out.printf("%-18s %-10s %-15s%n",
                "Guest", "Room", "Status");

        System.out.println("-----------------------------------------------");

        for (FrontDeskGuest guest : guests) {

            System.out.printf("%-18s %-10d %-15s%n",
                    guest.getName(),
                    guest.getRoomNumber(),
                    guest.getStatus());
        }

        System.out.println("-----------------------------------------------");
        System.out.println("Total Guests: " + count);
    }

    private void sortByBillDescending(FrontDeskGuest[] guests) { //selection sort with the filtered queue changed into guest array

        for (int i = 0; i < guests.length - 1; i++) {

            int largestIndex = i;

            for (int j = i + 1; j < guests.length; j++) {

                if (guests[j].getBill() > guests[largestIndex].getBill()) {
                    largestIndex = j;
                }
            }

            FrontDeskGuest temp = guests[i];
            guests[i] = guests[largestIndex];
            guests[largestIndex] = temp;
        }
    }

    private void sortByRoomAscending(FrontDeskGuest[] guests) {

        for (int i = 0; i < guests.length - 1; i++) {

            int smallestIndex = i;

            for (int j = i + 1; j < guests.length; j++) {

                if (guests[j].getRoomNumber() < guests[smallestIndex].getRoomNumber()) { //lowest room num to highest num
                    smallestIndex = j;
                }
            }

            FrontDeskGuest temp = guests[i];
            guests[i] = guests[smallestIndex];
            guests[smallestIndex] = temp;
        }
    }




}