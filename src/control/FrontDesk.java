//frontdesk = storing guests
//bst = storing objects T to be compared 
package control;

import entity.Guest;
import adt.BST;
import adt.BSTInterface;
import java.util.Iterator;
import adt.QueueInterface;
import adt.ArrayQueue;

public class FrontDesk {
    //frontdesk only need to know bstinterface can do add,search,isempty and stored objects are guest
    private BSTInterface<Guest> guestTree;

    //constructor
    public FrontDesk() {

        guestTree = new BST<Guest>(); //search by 8num
        loadGuests();

    }

    private void loadGuests() {

        Guest guest1 = new Guest(
                "12345678",
                "Bryan Ong",
                "0123456789",
                305,
                750.00,
                "Checked In"
        );

        Guest guest2 = new Guest(
                "10000000",
                "Alice Tan",
                "0134567890",
                201,
                500.00,
                "Checked In"
        );

        Guest guest3 = new Guest(
                "15000000",
                "John Lim",
                "0145678901",
                402,
                900.00,
                "Checked Out"
        );

        Guest guest4 = new Guest(
                "11000000",
                "Sarah Lee",
                "0156789012",
                102,
                350.00,
                "Checked In"
        );

        Guest guest5 = new Guest(
                "13000000",
                "Daniel Wong",
                "0167890123",
                308,
                1200.00,
                "Checked In"
        );

        Guest guest6 = new Guest(
                "17000000",
                "Emily Tan",
                "0178901234",
                501,
                650.00,
                "Checked Out"
        );

        Guest guest7 = new Guest(
                "10500000",
                "Michael Lee",
                "0189012345",
                205,
                450.00,
                "Checked Out"
        );

        Guest guest8 = new Guest(
                "14000000",
                "Jessica Lim",
                "0190123456",
                410,
                1500.00,
                "Checked In"
        );

        Guest guest9 = new Guest(
                "12500000",
                "Kevin Tan",
                "0111234567",
                307,
                800.00,
                "Checked In"
        );

        Guest guest10 = new Guest(
                "16000000",
                "Rachel Wong",
                "0122345678",
                305,
                550.00,
                "Checked Out"
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

        Guest searchKey = new Guest(
                confirmationNumber,
                "",
                "",
                0,
                0.0,
                ""
        );

        Guest guest = guestTree.getEntry(searchKey);

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

        Guest searchKey = new Guest(
                confirmationNumber,
                "",
                "",
                0,
                0.0,
                ""
        );

        Guest guest = guestTree.getEntry(searchKey);

        if (guest != null) {

            System.out.println("\nBill Information");
            System.out.println("----------------");
            System.out.println("Guest: " + guest.getName());
            System.out.println("Room Number: " + guest.getRoomNumber());
            System.out.println("Total Bill: RM " + guest.getBill());
            System.out.println("Status: " + guest.getStatus());

        } else {

            System.out.println("\nGuest not found.");
        }
    }

    public void displayAllGuests() {

        Iterator<Guest> iterator = guestTree.getInOrderIterator();

        System.out.println("\n=== All Guests ===");

        while (iterator.hasNext()) {

            Guest guest = iterator.next();

            System.out.println(guest);
        }
    }

    //bridge between bst and report
    public QueueInterface<Guest> getGuestQueue() { 

        QueueInterface<Guest> guestQueue = new ArrayQueue<>();

        Iterator<Guest> iterator = guestTree.getInOrderIterator();

        //transfers every guest into queue for report
        while (iterator.hasNext()) {

            Guest guest = iterator.next();
            guestQueue.enqueue(guest);
        }

        return guestQueue;
    }

    public void generateBillingReport(String status, double minimumBill) {

        QueueInterface<Guest> guestQueue = getGuestQueue();
        QueueInterface<Guest> filteredQueue = new ArrayQueue<>();

        // Filter guests
        while (!guestQueue.isEmpty()) {

            Guest guest = guestQueue.dequeue();

            boolean statusMatches =
                    status.equals("All") ||
                    guest.getStatus().equals(status);

            boolean billMatches =
                    guest.getBill() >= minimumBill;

            if (statusMatches && billMatches) {
                filteredQueue.enqueue(guest);
            }
        }

        // Count filtered guests
        int count = 0;

        QueueInterface<Guest> tempQueue = new ArrayQueue<>();

        while (!filteredQueue.isEmpty()) {

            Guest guest = filteredQueue.dequeue();

            tempQueue.enqueue(guest);
            count++;
        }

        // No matching guests
        if (count == 0) {

            System.out.println("\n=== Guest Billing Report ===");
            System.out.println("No guests match the selected criteria.");
            return;
        }

        // Transfer queue into array
        Guest[] guests = new Guest[count];

        for (int i = 0; i < count; i++) {

            guests[i] = tempQueue.dequeue();
        }

        // Sort by bill: highest to lowest
        sortByBillDescending(guests);

        // Generate report
        double totalBill = 0;

        System.out.println("\n=== Guest Billing Report ===");
        System.out.println("-----------------------------------------------");
        System.out.println("Status: " + status);
        System.out.println("Minimum Bill: RM " + minimumBill);
        System.out.println("Sort: Bill (Highest to Lowest)");
        System.out.println("-----------------------------------------------");

        System.out.printf("%-18s %-8s %-12s %-15s%n",
                "Guest", "Room", "Bill", "Status");

        System.out.println("-----------------------------------------------");

        for (Guest guest : guests) {

            System.out.printf("%-18s %-8d RM%-10.2f %-15s%n",
                    guest.getName(),
                    guest.getRoomNumber(),
                    guest.getBill(),
                    guest.getStatus());

            totalBill += guest.getBill();
        }

        double averageBill = totalBill / count;

        System.out.println("-----------------------------------------------");
        System.out.println("Total Guests: " + count);
        System.out.printf("Total Billing: RM %.2f%n", totalBill);
        System.out.printf("Average Bill: RM %.2f%n", averageBill);
    }

    public void generateOccupancyReport(String status, int minimumRoom, int maximumRoom) {

        QueueInterface<Guest> guestQueue = getGuestQueue();
        QueueInterface<Guest> filteredQueue = new ArrayQueue<>();

        // Filter guests
        while (!guestQueue.isEmpty()) {

            Guest guest = guestQueue.dequeue();

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

        // Transfer filtered guests into an array
        int count = 0;
        QueueInterface<Guest> tempQueue = new ArrayQueue<>();

        while (!filteredQueue.isEmpty()) {

            Guest guest = filteredQueue.dequeue();

            tempQueue.enqueue(guest);
            count++;
        }

        // No matching guests
        if (count == 0) {

            System.out.println("\n=== Guest Occupancy Report ===");
            System.out.println("No guests match the selected criteria.");
            return;
        }

        Guest[] guests = new Guest[count];

        for (int i = 0; i < count; i++) {

            guests[i] = tempQueue.dequeue();
        }

        // Sort by room number: lowest to highest
        sortByRoomAscending(guests);

        // Generate report
        System.out.println("\n=== Guest Occupancy Report ===");
        System.out.println("-----------------------------------------------");
        System.out.println("Status: " + status);
        System.out.println("Room Range: " + minimumRoom + " - " + maximumRoom);
        System.out.println("Sort: Room Number (Lowest to Highest)");
        System.out.println("-----------------------------------------------");

        System.out.printf("%-18s %-10s %-15s%n",
                "Guest", "Room", "Status");

        System.out.println("-----------------------------------------------");

        for (Guest guest : guests) {

            System.out.printf("%-18s %-10d %-15s%n",
                    guest.getName(),
                    guest.getRoomNumber(),
                    guest.getStatus());
        }

        System.out.println("-----------------------------------------------");
        System.out.println("Total Guests: " + count);
    }

    private void sortByBillDescending(Guest[] guests) { //selection sort with the filtered queue changed into guest array

        for (int i = 0; i < guests.length - 1; i++) {

            int largestIndex = i;

            for (int j = i + 1; j < guests.length; j++) {

                if (guests[j].getBill() > guests[largestIndex].getBill()) {
                    largestIndex = j;
                }
            }

            Guest temp = guests[i];
            guests[i] = guests[largestIndex];
            guests[largestIndex] = temp;
        }
    }

    private void sortByRoomAscending(Guest[] guests) {

        for (int i = 0; i < guests.length - 1; i++) {

            int smallestIndex = i;

            for (int j = i + 1; j < guests.length; j++) {

                if (guests[j].getRoomNumber() < guests[smallestIndex].getRoomNumber()) { //lowest room num to highest num
                    smallestIndex = j;
                }
            }

            Guest temp = guests[i];
            guests[i] = guests[smallestIndex];
            guests[smallestIndex] = temp;
        }
    }




}