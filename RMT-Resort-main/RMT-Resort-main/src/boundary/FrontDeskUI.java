// @author : Ong Wei Chen 
package boundary;

import java.util.Scanner;
import control.FrontDesk;

public class FrontDeskUI {
    
    private Scanner scanner;
    private FrontDesk frontDesk;

    public FrontDeskUI() {

        scanner = new Scanner(System.in);
        frontDesk = new FrontDesk();
    }

    //menu start
    public void start() {

        while (true) {

            System.out.println("");
            System.out.println("=================================");
            System.out.println("       Front-Desk Service");
            System.out.println("=================================");
            System.out.println("1. Search Single Guest Information");
            System.out.println("2. Search Single Bill Information");
            System.out.println("3. Display All Guests");
            System.out.println("4. Generate Billing Report");
            System.out.println("5. Generate Occupancy Report");
            System.out.println("6. Return to Main Menu");
            System.out.println("=================================");
            System.out.println("");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {

                searchGuest();

            } else if (choice == 2) {

                searchBill();

            } else if (choice == 3) {

                frontDesk.displayAllGuests();

            } else if (choice == 4) {

                generateBillingReport();
                
            } else if (choice == 5) {

                generateOccupancyReport();

            } else if (choice == 6) {

                System.out.println("-> Returning to Main Menu...");
                break;

            } else {

                System.out.println("Invalid choice.");

            }
        }
    }

    //search guest
    private void searchGuest(){

        System.out.print("\n-> Enter 8-digit confirmation number: ");

        String confirmationNumber = scanner.nextLine();

        if (confirmationNumber.length() !=8) {
            System.out.println("Invalid confirmation number.");
            return;
        }

        frontDesk.searchGuest(confirmationNumber);
    }

    //search bill
    private void searchBill() {

        System.out.print("\n-> Enter 8-digit confirmation number: ");

        String confirmationNumber = scanner.nextLine();

        if (confirmationNumber.length() != 8) {

            System.out.println("Invalid confirmation number.");

            return;
        }

        frontDesk.searchBill(confirmationNumber);
    }

    private void generateBillingReport() {

        System.out.println("\n=== Guest Billing Report ===");
        System.out.println("1. Checked In");
        System.out.println("2. Checked Out");
        System.out.println("3. All Guests");
        System.out.print("-> Select status: ");

        int statusChoice = scanner.nextInt();
        scanner.nextLine();

        String status;

        if (statusChoice == 1) {

            status = "Checked In";

        } else if (statusChoice == 2) {

            status = "Checked Out";

        } else if (statusChoice == 3) {

            status = "All";

        } else {

            System.out.println("Invalid choice.");
            return;
        }

        System.out.print("-> Enter minimum bill amount: RM ");

        double minimumBill = scanner.nextDouble();
        scanner.nextLine();

        frontDesk.generateBillingReport(status, minimumBill); //user input gets passed to control class

    }

    private void generateOccupancyReport() {

    System.out.println("\n=== Guest Occupancy Report ===");
    System.out.println("1. Checked In");
    System.out.println("2. Checked Out");
    System.out.println("3. All Guests");
    System.out.print("-> Select status: ");

    int statusChoice = scanner.nextInt();
    scanner.nextLine();

    String status;

    if (statusChoice == 1) {

        status = "Checked In";

    } else if (statusChoice == 2) {

        status = "Checked Out";

    } else if (statusChoice == 3) {

        status = "All";

    } else {

        System.out.println("Invalid choice.");
        return;
    }

    System.out.print("-> Enter minimum room number: ");
    int minimumRoom = scanner.nextInt();

    System.out.print("-> Enter maximum room number: ");
    int maximumRoom = scanner.nextInt();
    scanner.nextLine();

    frontDesk.generateOccupancyReport(status, minimumRoom, maximumRoom); //send input values to control class

    }



}
