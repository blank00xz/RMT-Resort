import boundary.WalkinMain;
import boundary.VIPRoomAllocationUI;
import boundary.HousekeepingUI;
import boundary.FrontDeskUI;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        int choice;

        do {

            displayMainMenu();

            choice = readInteger("Enter your choice: ");

            switch (choice) {

                case 1:
                    System.out.println("\nOpening Walk-in Module...");
                    WalkinMain.runMenu();
                    break;

                case 2:
                    System.out.println("\nOpening VIP Room Module...");
                    VIPRoomAllocationUI vipUI = new VIPRoomAllocationUI();
                    vipUI.runMenu();
                    break;

                case 3:
                    System.out.println("\nOpening Housekeeping Module...");
                    HousekeepingUI housekeepingUI = new HousekeepingUI();
                    housekeepingUI.runMenu();
                    break;

                case 4:
                    System.out.println("\nOpening Front Desk Module...");
                    FrontDeskUI frontDeskUI = new FrontDeskUI();
                    frontDeskUI.start();
                    break;

                case 5:
                    System.out.println("\nThank you for using the Resort Management System.");
                    break;

                default:
                    System.out.println("\nInvalid choice.");

            }

        } while (choice != 5);

        scanner.close();
    }

    private static void displayMainMenu() {

        System.out.println();
        System.out.println("========================================");
        System.out.println("       RMT RESORT MANAGEMENT SYSTEM");
        System.out.println("========================================");
        System.out.println("1. Walk-in");
        System.out.println("2. VIP Room");
        System.out.println("3. Housekeeping");
        System.out.println("4. Front Desk");
        System.out.println("5. Exit");
        System.out.println("========================================");
    }

    private static int readInteger(String message) {

        while (true) {

            System.out.print(message);

            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid number.");
            }
        }
    }

}
