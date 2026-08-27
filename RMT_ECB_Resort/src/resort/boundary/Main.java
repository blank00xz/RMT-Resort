/**
 * Author: Karlson Tan Zhi Ming
 * Class: Main
 * Description: Boundary class for the Resort Booking System.
 */

package resort.boundary;

import resort.control.ResortSystem;
import resort.entity.Guest;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner =
            new Scanner(System.in);

    public static void main(String[] args) {

        ResortSystem resort =
                new ResortSystem();

        int choice;

        do {

                        clearScreen();
            displayMenu();

            choice =
                    readInteger(
                            "Enter your choice: "
                    );

                        switch (choice) {

                                case 1:
                                        standardBooking(resort);
                                        break;

                                case 2:
                                        walkInRegistration(resort);
                                        break;

                                case 3:
                                        walkInQueueAndProcess(resort);
                                        break;

                                case 4:
                                        resort.displayRooms();
                                        break;

                                case 5:
                                        resort.displayBookings();
                                        break;

                                case 6:
                                        cancelBooking(resort);
                                        break;

                                case 7:
                                        resort.displayBillings();
                                        break;

                                case 8:
                                        System.out.println();
                                        System.out.println("Thank you for using the");
                                        System.out.println("Resort Booking System.");
                                        break;

                                default:
                                        System.out.println();
                                        System.out.println("Invalid choice.");
                        }

                        if (choice != 8) {
                                pressEnterToContinue();
                        }

                } while (choice != 8);

        scanner.close();
    }

        public static void clearScreen() {

        System.out.print(
                "\033[H\033[2J"
        );

        for (int i = 0; i < 5; i++) {

            System.out.println();
        }

        System.out.flush();
    }

        private static void pressEnterToContinue() {

                System.out.println();
                System.out.print("Press Enter to continue...");
                scanner.nextLine();
        }

    // =====================================================
    // MAIN MENU
    // =====================================================

    private static void displayMenu() {

        System.out.println();

        System.out.println(
                "========================================"
        );

        System.out.println(
                "         RESORT BOOKING SYSTEM"
        );

        System.out.println(
                "========================================"
        );

        System.out.println(
                "1. Standard Booking"
        );

        System.out.println(
                "2. Walk-in Registration"
        );

        System.out.println(
                "3. Walk-in Queue & Process"
        );

        System.out.println(
                "4. View All Rooms"
        );

        System.out.println(
                "5. View All Bookings"
        );

        System.out.println(
                "6. Cancel Booking"
        );

        System.out.println(
                "7. View All Billings"
        );

        System.out.println(
                "8. Exit"
        );

        System.out.println(
                "========================================"
        );
    }

    // =====================================================
    // STANDARD BOOKING
    // =====================================================

    private static void standardBooking(
            ResortSystem resort) {

        System.out.println();

        System.out.println(
                "========== STANDARD BOOKING =========="
        );

        // Guest name
        String name;

        while (true) {

            System.out.print(
                    "Enter guest name: "
            );

            name =
                    scanner.nextLine().trim();

            if (name.isEmpty()) {

                System.out.println(
                        "Guest name cannot be empty."
                );

            } else if (!name.matches(
                    "[a-zA-Z ]+")) {

                System.out.println(
                        "Guest name can only contain letters."
                );

            } else {

                break;
            }
        }

        // Phone number
        String phone;

        while (true) {

            System.out.print(
                    "Enter phone number: "
            );

            phone =
                    scanner.nextLine().trim();

            if (!phone.matches(
                    "01[0-9]{8,9}")) {

                System.out.println(
                        "Invalid Malaysian phone number."
                );

                System.out.println(
                        "Example: 0123456789"
                );

            } else if (
                    resort.phoneExists(phone)) {

                System.out.println(
                        "Phone number already exists."
                );

            } else {

                break;
            }
        }

        // Number of guests
        int numberOfGuests;

        while (true) {

            numberOfGuests =
                    readInteger(
                            "Enter number of guests: "
                    );

            if (numberOfGuests < 1) {

                System.out.println(
                        "Number of guests must be at least 1."
                );

            } else {

                break;
            }
        }

        // Room type
        String roomType;

        while (true) {

            System.out.print(
                    "Enter room type "
                    + "(Standard/Deluxe/Family): "
            );

            roomType =
                    scanner.nextLine().trim();

            if (!resort.isValidRoomType(
                    roomType)) {

                System.out.println(
                        "Invalid room type."
                );

                continue;
            }

                        if (resort.hasAvailableRoom(roomType)) {
                                break;
                        }

                        System.out.println(
                                        "No available " + roomType + " room."
                        );

                        if (!askToChangeRoomType()) {
                                return;
                        }
        }

        // Check-in
        LocalDate checkIn;

        while (true) {

            System.out.print(
                    "Enter check-in date "
                    + "(YYYY-MM-DD): "
            );

            String input =
                    scanner.nextLine().trim();

            try {

                checkIn =
                        LocalDate.parse(input);

                if (checkIn.isBefore(
                        LocalDate.now())) {

                    System.out.println(
                            "Check-in date cannot be before today."
                    );

                    continue;
                }

                break;

            } catch (
                    DateTimeParseException e) {

                System.out.println(
                        "Invalid date format."
                );
            }
        }

        // Check-out
        LocalDate checkOut;

        while (true) {

            System.out.print(
                    "Enter check-out date "
                    + "(YYYY-MM-DD): "
            );

            String input =
                    scanner.nextLine().trim();

            try {

                checkOut =
                        LocalDate.parse(input);

                if (!checkOut.isAfter(
                        checkIn)) {

                    System.out.println(
                            "Check-out date must be after check-in date."
                    );

                    continue;
                }

                break;

            } catch (
                    DateTimeParseException e) {

                System.out.println(
                        "Invalid date format."
                );
            }
        }

        // Temporary confirmation number
        Guest guest =
                new Guest(
                        resort.generateConfirmationNumber(),
                        name,
                        phone,
                        numberOfGuests
                );

        guest.setRegisterMethod(
                "STANDARD BOOKING"
        );

        resort.createStandardBooking(
                guest,
                roomType,
                checkIn,
                checkOut
        );
    }

    // =====================================================
    // WALK-IN REGISTRATION
    // =====================================================

    private static void walkInRegistration(
            ResortSystem resort) {

        System.out.println();

        System.out.println(
                "======= WALK-IN REGISTRATION ======="
        );

        // Guest name
        String name;

        while (true) {

            System.out.print(
                    "Enter guest name: "
            );

            name =
                    scanner.nextLine().trim();

            if (name.isEmpty()) {

                System.out.println(
                        "Guest name cannot be empty."
                );

            } else if (!name.matches(
                    "[a-zA-Z ]+")) {

                System.out.println(
                        "Guest name can only contain letters."
                );

            } else {

                break;
            }
        }

        // Phone
        String phone;

        while (true) {

            System.out.print(
                    "Enter phone number: "
            );

            phone =
                    scanner.nextLine().trim();

            if (!phone.matches(
                    "01[0-9]{8,9}")) {

                System.out.println(
                        "Invalid Malaysian phone number."
                );

            } else if (
                    resort.phoneExists(phone)) {

                System.out.println(
                        "Phone number already exists."
                );

            } else {

                break;
            }
        }

        // Number of guests
        int numberOfGuests;

        while (true) {

            numberOfGuests =
                    readInteger(
                            "Enter number of guests: "
                    );

            if (numberOfGuests < 1) {

                System.out.println(
                        "Number of guests must be at least 1."
                );

            } else {

                break;
            }
        }

        String confirmationNumber =
                resort.generateConfirmationNumber();

        Guest guest =
                new Guest(
                        confirmationNumber,
                        name,
                        phone,
                        numberOfGuests
                );

        guest.setRegisterMethod(
                "WALK-IN"
        );

                System.out.println();

                if (!resort.displayAvailableRooms()) {

                        System.out.println(
                                        "Guest will remain in the walk-in queue."
                        );

                        resort.addToWalkInQueue(guest);
                        return;
                }

                System.out.println();
                System.out.println("1. Yes, check in now");
                System.out.println("2. No, do not check in");
                System.out.println("3. Stay in walk-in queue");

                int registrationChoice;

                do {

                        registrationChoice = readInteger(
                                        "Choose an option: "
                        );

                        if (registrationChoice < 1
                                        || registrationChoice > 3) {

                                System.out.println(
                                                "Please choose 1, 2, or 3."
                                );
                        }

                } while (registrationChoice < 1
                                || registrationChoice > 3);

                if (registrationChoice == 2) {

                        System.out.println(
                                        "Walk-in registration cancelled."
                        );

                        return;
                }

                if (registrationChoice == 3) {

                        resort.addToWalkInQueue(guest);
                        return;
                }

                String roomType;

                while (true) {

                        System.out.print(
                                        "Enter room type (Standard/Deluxe/Family): "
                        );

                        roomType = scanner.nextLine().trim();

                        if (!resort.isValidRoomType(roomType)) {

                                System.out.println(
                                                "Invalid room type."
                                );

                        } else if (!resort.hasAvailableRoom(roomType)) {

                                System.out.println(
                                                "No available room for that type."
                                );

                                if (!askToChangeRoomType()) {

                                        resort.addToWalkInQueue(guest);
                                        return;
                                }

                        } else {

                                break;
                        }
                }

                LocalDate checkIn = readCheckInDate();
                LocalDate checkOut = readCheckOutDate(checkIn);

                resort.completeWalkInBooking(
                                guest,
                                roomType,
                                checkIn,
                                checkOut
                );
    }

        private static LocalDate readCheckInDate() {

                while (true) {

                        System.out.print(
                                        "Enter check-in date (YYYY-MM-DD): "
                        );

                        try {

                                LocalDate checkIn = LocalDate.parse(
                                                scanner.nextLine().trim()
                                );

                                if (checkIn.isBefore(LocalDate.now())) {

                                        System.out.println(
                                                        "Check-in date cannot be before today."
                                        );

                                        continue;
                                }

                                return checkIn;

                        } catch (DateTimeParseException e) {

                                System.out.println(
                                                "Invalid date format."
                                );
                        }
                }
        }

        private static LocalDate readCheckOutDate(
                        LocalDate checkIn) {

                while (true) {

                        System.out.print(
                                        "Enter check-out date (YYYY-MM-DD): "
                        );

                        try {

                                LocalDate checkOut = LocalDate.parse(
                                                scanner.nextLine().trim()
                                );

                                if (!checkOut.isAfter(checkIn)) {

                                        System.out.println(
                                                        "Check-out date must be after check-in date."
                                        );

                                        continue;
                                }

                                return checkOut;

                        } catch (DateTimeParseException e) {

                                System.out.println(
                                                "Invalid date format."
                                );
                        }
                }
        }

    private static void walkInQueueAndProcess(
            ResortSystem resort) {

        System.out.println();
        resort.displayWalkInQueue();

        if (resort.getNextWalkInGuest() == null) {
            return;
        }

        System.out.println();
        System.out.println("1. Customer arrived");
        System.out.println("2. Mark as no-show");
        System.out.println("3. Keep in queue");

        int choice = readInteger(
                "Enter processing choice: "
        );

        if (choice == 1) {

            if (!resort.displayAvailableRooms()) {
                return;
            }

            String roomType;

            while (true) {

                System.out.print(
                        "Choose room type (Standard/Deluxe/Family): "
                );

                roomType = scanner.nextLine().trim();

                if (resort.isValidRoomType(roomType)
                        && resort.hasAvailableRoom(roomType)) {
                    break;
                }

                System.out.println(
                        "No available room for that type. Please choose again."
                );

                                if (resort.isValidRoomType(roomType)
                                                && !askToChangeRoomType()) {
                                        return;
                                }
            }

            resort.processNextWalkIn(choice, roomType);
            return;
        }

        resort.processNextWalkIn(choice);
    }

        private static boolean askToChangeRoomType() {

                System.out.println("1. Change room type");
                System.out.println("2. Do not change");

                int choice;

                do {

                        choice = readInteger(
                                        "Choose an option: "
                        );

                        if (choice < 1 || choice > 2) {
                                System.out.println("Please choose 1 or 2.");
                        }

                } while (choice < 1 || choice > 2);

                return choice == 1;
        }

    // =====================================================
    // CANCEL BOOKING
    // =====================================================

    private static void cancelBooking(
            ResortSystem resort) {

        System.out.println();

                System.out.println(
                        "========== CANCEL BOOKING =========="
        );

        System.out.print(
                "Enter confirmation number: "
        );

        String confirmationNumber =
                scanner.nextLine().trim();

        resort.cancelBooking(
                confirmationNumber
        );
    }

    // =====================================================
    // READ INTEGER
    // =====================================================

    private static int readInteger(
            String message) {

        while (true) {

            System.out.print(message);

            String input =
                    scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(
                        "Please enter a valid number."
                );
            }
        }
    }
}