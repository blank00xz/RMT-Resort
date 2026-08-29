package boundary;

import control.VIPRoomAllocationControl;
import control.VIPRoomAllocationReportControl;
import entity.Loyalty;
import entity.VIPRoom;
import entity.RoomAllocationRecord;
import entity.VIPGuest;
import entity.VIPRoomRequest;
import java.time.LocalDateTime;
import java.util.Scanner;

/**
 * Console boundary class for the VIP & Loyalty Tier priority room allocation
 * module.
 *
 * @author Goh Wei Hong
 */
public class VIPRoomAllocationUI {

    private final Scanner scanner = new Scanner(System.in);
    private final VIPRoomAllocationControl allocationControl;
    private final VIPRoomAllocationReportControl reportControl;

    public VIPRoomAllocationUI() {
        this.allocationControl = new VIPRoomAllocationControl();
        this.reportControl = new VIPRoomAllocationReportControl();
    }

    public VIPRoomAllocationUI(VIPRoomAllocationControl allocationControl) {
        if (allocationControl == null) {
            throw new IllegalArgumentException("Allocation control cannot be null.");
        }
        this.allocationControl = allocationControl;
        this.reportControl = new VIPRoomAllocationReportControl();
    }

    private void printMenu() {
        MessageUI.printHeader("VIP PRIORITY ROOM ALLOCATION");
        System.out.println("1. Submit VIP room request");
        System.out.println("2. View waiting requests (priority order)");
        System.out.println("3. Allocate highest-priority request");
        System.out.println("4. Cancel waiting request");
        System.out.println("5. Search by confirmation number");
        System.out.println("6. Add room");
        System.out.println("7. Update room availability");
        System.out.println("8. View all rooms");
        System.out.println("9. Allocation history report");
        System.out.println("0. Return to main menu");
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException ex) {
                MessageUI.printError("Please enter a whole number.");
            }
        }
    }

    private String readNonBlank(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isBlank()) {
                return value;
            }
            MessageUI.printError("This field cannot be blank.");
        }
    }

    private Loyalty readTier() {
        while (true) {
            System.out.println("\nSelect loyalty tier:");
            System.out.println("1. SILVER   2. GOLD   3. PLATINUM   4. DIAMOND   5. ELITE");
            int choice = readInt("Tier: ");
            switch (choice) {
                case 1: return Loyalty.SILVER;
                case 2: return Loyalty.GOLD;
                case 3: return Loyalty.PLATINUM;
                case 4: return Loyalty.DIAMOND;
                case 5: return Loyalty.ELITE;
                default: MessageUI.printError("Please select 1 to 5.");
            }
        }
    }

    private String readRoomType() {
        while (true) {
            System.out.println("\nSelect room type:");
            System.out.println("1. DELUXE   2. SUITE   3. VILLA");
            int choice = readInt("Room type: ");
            switch (choice) {
                case 1: return "DELUXE";
                case 2: return "SUITE";
                case 3: return "VILLA";
                default: MessageUI.printError("Please select 1 to 3.");
            }
        }
    }

    private void submitRequestUI() {
        MessageUI.printHeader("SUBMIT VIP ROOM REQUEST");

        String guestId = readNonBlank("Guest / member ID (e.g. M001): ");
        String name = readNonBlank("Guest name: ");
        String contact = readNonBlank("Contact number: ");
        Loyalty tier = readTier();
        String roomType = readRoomType();

        String confirmationNumber = allocationControl.generateConfirmationNumber();
        System.out.println("Generated confirmation number: " + confirmationNumber);

        VIPGuest guest = new VIPGuest(guestId, name, contact, tier, "WAITING");
        VIPRoomRequest request = new VIPRoomRequest(confirmationNumber, guest, roomType, LocalDateTime.now());

        boolean submitted = allocationControl.submitRequest(request);

        if (submitted) {
            MessageUI.printSuccess("Request submitted successfully.");
            System.out.println("Current waiting requests: " + allocationControl.getWaitingRequestCount());
        } else {
            MessageUI.printError("Submission failed. Check the input.");
        }
    }

    private void viewWaitingRequestsUI() {
        VIPRoomRequest[] requests = allocationControl.getWaitingRequestsInPriorityOrder();

        MessageUI.printHeader("WAITING REQUESTS (PRIORITY ORDER)");

        if (requests.length == 0) {
            System.out.println("No requests are waiting.");
            return;
        }

        for (int i = 0; i < requests.length; i++) {
            System.out.println((i + 1) + ". " + requests[i]);
        }
        System.out.println("Total waiting: " + requests.length);
    }

    private void allocateNextRequestUI() {
        if (allocationControl.getWaitingRequestCount() == 0) {
            System.out.println("No requests are waiting for allocation.");
            return;
        }

        RoomAllocationRecord record = allocationControl.allocateNextRequest();

        if (record == null) {
            MessageUI.printError("No available room matches any waiting request.");
            return;
        }

        MessageUI.printSuccess("Room allocated successfully:");
        System.out.println(record);
    }

    private void cancelRequestUI() {
        String confirmationNumber = readNonBlank("Confirmation number to cancel: ");

        VIPRoomRequest cancelled = allocationControl.cancelWaitingRequest(confirmationNumber);

        if (cancelled == null) {
            MessageUI.printError("No matching waiting request was found.");
            return;
        }

        MessageUI.printSuccess("Request cancelled:");
        System.out.println(cancelled);
    }

    private void searchRequestUI() {
        String confirmationNumber = readNonBlank("Confirmation number to search: ");

        VIPRoomRequest waiting = allocationControl.searchWaitingRequestByConfirmation(confirmationNumber);
        if (waiting != null) {
            System.out.println("Found in WAITING queue:");
            System.out.println(waiting);
            return;
        }

        RoomAllocationRecord allocated = allocationControl.searchAllocationByConfirmation(confirmationNumber);
        if (allocated != null) {
            System.out.println("Found in ALLOCATION history:");
            System.out.println(allocated);
            return;
        }

        VIPRoomRequest cancelled = allocationControl.searchCancelledRequestByConfirmation(confirmationNumber);
        if (cancelled != null) {
            System.out.println("Found in CANCELLED records:");
            System.out.println(cancelled);
            return;
        }

        MessageUI.printError("No request found with that confirmation number.");
    }

    private void addRoomUI() {
        String roomNumber = readNonBlank("Room number: ");
        String roomType = readRoomType();

        boolean added = allocationControl.addRoom(new VIPRoom(roomNumber, roomType, true));

        if (added) {
            MessageUI.printSuccess("Room added.");
        } else {
            MessageUI.printError("Room number already exists or input invalid.");
        }
    }

    private void updateRoomAvailabilityUI() {
        viewAllRoomsUI();

        if (allocationControl.getRoomCount() == 0) {
            return;
        }

        String roomNumber = readNonBlank("\nRoom number to update: ");
        System.out.println("1. AVAILABLE   2. OCCUPIED");
        int choice = readInt("New status: ");

        if (choice != 1 && choice != 2) {
            MessageUI.printError("Invalid status selection.");
            return;
        }

        boolean updated = allocationControl.updateRoomAvailability(roomNumber, choice == 1);

        if (updated) {
            MessageUI.printSuccess("Room availability updated.");
        } else {
            MessageUI.printError("Room number was not found.");
        }
    }

    private void viewAllRoomsUI() {
        VIPRoom[] rooms = allocationControl.getAllRooms();

        MessageUI.printHeader("CURRENT ROOMS");

        if (rooms.length == 0) {
            System.out.println("No rooms are registered.");
            return;
        }

        for (VIPRoom room : rooms) {
            System.out.println(room);
        }
    }

    private void allocationReportUI() {
        System.out.println("\nTier filter: 0=ALL 1=SILVER 2=GOLD 3=PLATINUM 4=DIAMOND 5=ELITE");
        int choice = readInt("Filter: ");

        Loyalty tierFilter;
        switch (choice) {
            case 0: tierFilter = null; break;
            case 1: tierFilter = Loyalty.SILVER; break;
            case 2: tierFilter = Loyalty.GOLD; break;
            case 3: tierFilter = Loyalty.PLATINUM; break;
            case 4: tierFilter = Loyalty.DIAMOND; break;
            case 5: tierFilter = Loyalty.ELITE; break;
            default:
                MessageUI.printError("Invalid filter.");
                return;
        }

        RoomAllocationRecord[] records = reportControl.generateAllocationReport(
                allocationControl.getAllocationHistory(), tierFilter);

        MessageUI.printHeader("VIP ALLOCATION HISTORY REPORT");
        System.out.println("Tier: " + (tierFilter == null ? "ALL" : tierFilter));

        if (records.length == 0) {
            System.out.println("No allocation records match the filter.");
            return;
        }

        for (RoomAllocationRecord record : records) {
            System.out.println(record);
        }
        System.out.println("Total matching allocations: " + records.length);
    }

    public void runMenu() {
        int choice;
        do {
            printMenu();
            choice = readInt("Enter choice: ");

            switch (choice) {
                case 1: submitRequestUI(); break;
                case 2: viewWaitingRequestsUI(); break;
                case 3: allocateNextRequestUI(); break;
                case 4: cancelRequestUI(); break;
                case 5: searchRequestUI(); break;
                case 6: addRoomUI(); break;
                case 7: updateRoomAvailabilityUI(); break;
                case 8: viewAllRoomsUI(); break;
                case 9: allocationReportUI(); break;
                case 0: System.out.println("Returning to the main menu..."); break;
                default: MessageUI.printError("Please select a valid menu option.");
            }
        } while (choice != 0);
    }

    /** Allows this module to be tested independently. */
    public static void main(String[] args) {
        VIPRoomAllocationUI ui = new VIPRoomAllocationUI();
        ui.runMenu();
    }
}
