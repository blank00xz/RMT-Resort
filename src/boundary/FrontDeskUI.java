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

            System.out.println("\n=== Front Desk ===");
            System.out.println("1. Search Guest");
            System.out.println("2. Search Bill");
            System.out.println("3. Exit");
            System.out.print("\n-> Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {

                searchGuest();

            } else if (choice == 2) {

                searchBill();

            } else if (choice == 3) {

                System.out.println("-> Goodbye.");
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
}
