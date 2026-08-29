/**
 * Author: Karlson Tan Zhi Ming
 * Class: ResortSystem
 * Description: Control class for the Resort Booking System.
 *              The system uses Queue ADT to manage rooms,
 *              guests, bookings, walk-in guests and
 *              cancelled bookings.
 */

package control;

import entity.Booking;
import entity.Billing;
import entity.Guest;
import entity.Room;
import adt.walkin.QueueImplementation;

import java.time.LocalDate;

public class ResortSystem {

    // ==================================================
    // COLLECTION ADT - QUEUE ONLY
    // ==================================================

    /*
     * Queue ADT - stores all rooms
     */
    private final QueueImplementation<Room> rooms =
            new QueueImplementation<>();

    /*
     * Queue ADT - stores all registered guests
     */
    private final QueueImplementation<Guest> guests =
            new QueueImplementation<>();

    /*
     * Queue ADT - stores walk-in guests
     */
    private final QueueImplementation<Guest> walkInQueue =
            new QueueImplementation<>();

    /*
     * Queue ADT - stores all bookings
     */
    private final QueueImplementation<Booking> bookings =
            new QueueImplementation<>();

    /*
     * Queue ADT - stores cancelled bookings
     */
    private final QueueImplementation<Booking> cancelledBookings =
            new QueueImplementation<>();

    /*
     * Queue ADT - stores all billing records
     */
    private final QueueImplementation<Billing> billings =
            new QueueImplementation<>();

    /*
     * Queue ADT - stores confirmation numbers
     */
    private final QueueImplementation<String>
            confirmationNumbers =
            new QueueImplementation<>();


    // ==================================================
    // CONSTRUCTOR
    // ==================================================

    public ResortSystem() {

        initializeSampleRooms();
    }


    // ==================================================
    // SAMPLE ROOMS
    // ==================================================

    public void initializeSampleRooms() {

        rooms.enqueue(
                new Room(
                        101,
                        "Standard",
                        180.00
                )
        );

        rooms.enqueue(
                new Room(
                        102,
                        "Standard",
                        180.00
                )
        );

        rooms.enqueue(
                new Room(
                        103,
                        "Standard",
                        180.00
                )
        );

        rooms.enqueue(
                new Room(
                        201,
                        "Deluxe",
                        280.00
                )
        );

        rooms.enqueue(
                new Room(
                        202,
                        "Deluxe",
                        280.00
                )
        );

        rooms.enqueue(
                new Room(
                        301,
                        "Family",
                        380.00
                )
        );

        rooms.enqueue(
                new Room(
                        302,
                        "Family",
                        380.00
                )
        );
    }


    // ==================================================
    // ROOM FUNCTIONS
    // ==================================================

    public void displayRooms() {

        System.out.println(
                "\n--- ROOM LIST ---"
        );

        if (rooms.isEmpty()) {

            System.out.println(
                    "No rooms available."
            );

            return;
        }

        QueueImplementation<Room> temp =
                new QueueImplementation<>();

        while (!rooms.isEmpty()) {

            Room room =
                    rooms.dequeue();

            System.out.println(room);

            temp.enqueue(room);
        }

        while (!temp.isEmpty()) {

            rooms.enqueue(
                    temp.dequeue()
            );
        }
    }


    // ==================================================
    // DISPLAY AVAILABLE ROOMS
    // ==================================================

    public boolean displayAvailableRooms() {

        System.out.println(
                "\n--- AVAILABLE ROOMS ---"
        );

        if (rooms.isEmpty()) {

            System.out.println(
                    "No rooms available."
            );

            return false;
        }

        QueueImplementation<Room> temp =
                new QueueImplementation<>();

        boolean found = false;

        while (!rooms.isEmpty()) {

            Room room =
                    rooms.dequeue();

            if (room.isAvailable()) {

                System.out.println(
                        "Room "
                                + room.getRoomNumber()
                                + " | "
                                + room.getRoomType()
                                + " | RM "
                                + String.format(
                                        "%.2f",
                                        room.getPricePerNight()
                                )
                                + "/night"
                );

                found = true;
            }

            temp.enqueue(room);
        }

        while (!temp.isEmpty()) {

            rooms.enqueue(
                    temp.dequeue()
            );
        }

        if (!found) {

            System.out.println(
                    "No rooms are currently available."
            );
        }

        return found;
    }


    // ==================================================
    // VALID ROOM TYPE
    // ==================================================

    public boolean isValidRoomType(
            String roomType) {

        return roomType != null
                && (
                roomType.equalsIgnoreCase(
                        "Standard"
                )
                || roomType.equalsIgnoreCase(
                        "Deluxe"
                )
                || roomType.equalsIgnoreCase(
                        "Family"
                )
        );
    }


    // ==================================================
    // CHECK ROOM AVAILABILITY
    // ==================================================

    public boolean hasAvailableRoom(
            String roomType) {

        return findAvailableRoom(roomType)
                != null;
    }


    // ==================================================
    // FIND AVAILABLE ROOM
    // ==================================================

    private Room findAvailableRoom(
            String roomType) {

        if (roomType == null) {

            return null;
        }

        QueueImplementation<Room> temp =
                new QueueImplementation<>();

        Room foundRoom = null;

        while (!rooms.isEmpty()) {

            Room room =
                    rooms.dequeue();

            if (foundRoom == null
                    && room.isAvailable()
                    && room.getRoomType()
                    .equalsIgnoreCase(roomType)) {

                foundRoom = room;
            }

            temp.enqueue(room);
        }

        while (!temp.isEmpty()) {

            rooms.enqueue(
                    temp.dequeue()
            );
        }

        return foundRoom;
    }


    // ==================================================
    // FIND ROOM BY NUMBER
    // ==================================================

    public Room findRoom(
            int roomNumber) {

        QueueImplementation<Room> temp =
                new QueueImplementation<>();

        Room foundRoom = null;

        while (!rooms.isEmpty()) {

            Room room =
                    rooms.dequeue();

            if (room.getRoomNumber()
                    == roomNumber) {

                foundRoom = room;
            }

            temp.enqueue(room);
        }

        while (!temp.isEmpty()) {

            rooms.enqueue(
                    temp.dequeue()
            );
        }

        return foundRoom;
    }


    // ==================================================
    // CONFIRMATION NUMBER
    // ==================================================

    public String generateConfirmationNumber() {

        String confirmationNumber;

        do {

            int number =
                    10000000
                    + (int) (
                    Math.random()
                    * 90000000
            );

            confirmationNumber =
                    String.valueOf(number);

        } while (
                confirmationNumberExists(
                        confirmationNumber
                )
        );

        confirmationNumbers.enqueue(
                confirmationNumber
        );

        return confirmationNumber;
    }


    // ==================================================
    // CHECK CONFIRMATION NUMBER
    // ==================================================

    private boolean confirmationNumberExists(
            String confirmationNumber) {

        QueueImplementation<String> temp =
                new QueueImplementation<>();

        boolean found = false;

        while (!confirmationNumbers.isEmpty()) {

            String number =
                    confirmationNumbers.dequeue();

            if (number.equals(
                    confirmationNumber)) {

                found = true;
            }

            temp.enqueue(number);
        }

        while (!temp.isEmpty()) {

            confirmationNumbers.enqueue(
                    temp.dequeue()
            );
        }

        return found;
    }


    // ==================================================
    // WALK-IN QUEUE
    // ==================================================

    public void addToWalkInQueue(
            Guest guest) {

        if (guest == null) {

            System.out.println(
                    "Invalid guest."
            );

            return;
        }

        walkInQueue.enqueue(guest);

        System.out.println(
                "Guest added to Walk-in Queue."
        );

        System.out.println(
                "Queue position: "
                        + walkInQueue.size()
        );
    }


    // ==================================================
    // DISPLAY WALK-IN QUEUE
    // ==================================================

    public void displayWalkInQueue() {

        System.out.println(
                "\n--- WALK-IN QUEUE ---"
        );

        if (walkInQueue.isEmpty()) {

            System.out.println(
                    "Queue is empty."
            );

            return;
        }

        QueueImplementation<Guest> temp =
                new QueueImplementation<>();

        int position = 1;

        while (!walkInQueue.isEmpty()) {

            Guest guest =
                    walkInQueue.dequeue();

            System.out.println(
                    position++
                            + ". "
                            + guest.getGuestName()
                            + " | Phone: "
                            + guest.getPhoneNumber()
            );

            temp.enqueue(guest);
        }

        while (!temp.isEmpty()) {

            walkInQueue.enqueue(
                    temp.dequeue()
            );
        }
    }


    // ==================================================
    // GET NEXT WALK-IN
    // ==================================================

    public Guest getNextWalkInGuest() {

        if (walkInQueue.isEmpty()) {

            System.out.println(
                    "Walk-in queue is empty."
            );

            return null;
        }

        return walkInQueue.peek();
    }


    // ==================================================
    // PROCESS NEXT WALK-IN
    // ==================================================

    public void processNextWalkIn(
            int choice) {

        processNextWalkIn(
                choice,
                "Standard"
        );
    }

    public void processNextWalkIn(
            int choice,
            String roomType) {

        if (walkInQueue.isEmpty()) {

            System.out.println(
                    "Walk-in queue is empty."
            );

            return;
        }

        Guest guest =
                walkInQueue.peek();

        System.out.println(
                "\n--- NEXT WALK-IN GUEST ---"
        );

        System.out.println(guest);


        switch (choice) {

            // ------------------------------------------
            // 1. Customer arrived
            // ------------------------------------------

            case 1:

                Room room =
                        findAvailableRoom(
                                roomType
                        );

                if (room == null) {

                    System.out.println(
                            "No "
                                    + roomType
                                    + " room is available."
                    );

                    System.out.println(
                            "Guest remains in queue."
                    );

                    return;
                }

                guest =
                        walkInQueue.dequeue();

                guest.setRegisterMethod(
                        "WALK-IN"
                );

                guest.setCheckInDate(
                        LocalDate.now().toString()
                );

                guest.setCheckOutDate(
                        LocalDate.now()
                                .plusDays(1)
                                .toString()
                );

                addGuestIfNotExists(
                        guest
                );

                Booking booking =
                        createBooking(
                                guest,
                                room
                        );

                System.out.println(
                        "\nGuest has arrived."
                );

                System.out.println(
                        "Walk-in registration completed."
                );

                System.out.println(booking);

                break;


            // ------------------------------------------
            // 2. No-show
            // ------------------------------------------

            case 2:

                guest =
                        walkInQueue.dequeue();

                System.out.println(
                        "\nGuest "
                                + guest.getGuestName()
                                + " has been marked "
                                + "as NO-SHOW."
                );

                System.out.println(
                        "Guest removed from queue."
                );

                break;


            // ------------------------------------------
            // 3. Keep in queue
            // ------------------------------------------

            case 3:

                System.out.println(
                        "Guest remains in queue."
                );

                break;


            default:

                System.out.println(
                        "Invalid choice. "
                                + "Please select "
                                + "1, 2, or 3."
                );
        }
    }


    // ==================================================
    // COMPLETE WALK-IN BOOKING
    // ==================================================

    public void completeWalkInBooking(
            Guest guest,
            String roomType,
            LocalDate checkIn,
            LocalDate checkOut) {

        if (guest == null) {

            System.out.println(
                    "Invalid guest."
            );

            return;
        }

        if (!isValidRoomType(roomType)) {

            System.out.println(
                    "Invalid room type."
            );

            return;
        }

        if (!isValidDateRange(
                checkIn,
                checkOut
        )) {

            return;
        }

        Room room =
                findAvailableRoom(
                        roomType
                );

        if (room == null) {

            System.out.println(
                    "\nSorry, the "
                            + roomType
                            + " room is no longer "
                            + "available."
            );

            return;
        }

        guest.setRegisterMethod(
                "WALK-IN"
        );

        guest.setCheckInDate(
                checkIn.toString()
        );

        guest.setCheckOutDate(
                checkOut.toString()
        );

        addGuestIfNotExists(
                guest
        );

        Booking booking =
                createBooking(
                        guest,
                        room
                );

        System.out.println(
                "\nWalk-in registration successful."
        );

        System.out.println(booking);
        displayBilling(booking);
    }


    // ==================================================
    // STANDARD BOOKING
    // ==================================================

    public void createStandardBooking(
            Guest guest,
            String roomType,
            LocalDate checkIn,
            LocalDate checkOut) {

        if (guest == null) {

            System.out.println(
                    "Invalid guest."
            );

            return;
        }


        // ------------------------------------------
        // Validate room type
        // ------------------------------------------

        if (!isValidRoomType(roomType)) {

            System.out.println(
                    "\nInvalid room type."
            );

            return;
        }


        // ------------------------------------------
        // Validate dates
        // ------------------------------------------

        if (!isValidDateRange(
                checkIn,
                checkOut
        )) {

            return;
        }


        // ------------------------------------------
        // Duplicate phone number
        // ------------------------------------------

        if (phoneExists(
                guest.getPhoneNumber()
        )) {

            System.out.println(
                    "\nThis phone number is "
                            + "already registered."
            );

            return;
        }


        // ------------------------------------------
        // Find room
        // ------------------------------------------

        Room room =
                findAvailableRoom(
                        roomType
                );


        if (room == null) {

            System.out.println(
                    "\nAll "
                            + roomType
                            + " rooms are currently FULL."
            );

            System.out.println(
                    "Please choose another room type."
            );

            return;
        }


        // ------------------------------------------
        // Set Guest data
        // ------------------------------------------

        guest.setRegisterMethod(
                "BOOKING"
        );

        guest.setCheckInDate(
                checkIn.toString()
        );

        guest.setCheckOutDate(
                checkOut.toString()
        );


        addGuestIfNotExists(
                guest
        );


        // ------------------------------------------
        // Create booking
        // ------------------------------------------

        Booking booking =
                createBooking(
                        guest,
                        room
                );


        System.out.println(
                "\nStandard booking successful."
        );

        System.out.println(booking);
        displayBilling(booking);
    }

    private void displayBilling(
            Booking booking) {

        long nights = java.time.temporal.ChronoUnit.DAYS.between(
                LocalDate.parse(booking.getGuest().getCheckInDate()),
                LocalDate.parse(booking.getGuest().getCheckOutDate())
        );

        double amount =
                nights * booking.getRoom().getPricePerNight();

        Billing billing = new Billing(
                amount,
                LocalDate.now().toString(),
                "PENDING"
        );

                billings.enqueue(billing);

        System.out.println(billing);
    }

        public void displayBillings() {

                System.out.println(
                                "\n--- ALL BILLINGS ---"
                );

                if (billings.isEmpty()) {

                        System.out.println(
                                        "No billing records available."
                        );

                        return;
                }

                QueueImplementation<Billing> temp =
                                new QueueImplementation<>();

                while (!billings.isEmpty()) {

                        Billing billing = billings.dequeue();

                        System.out.println(billing);

                        temp.enqueue(billing);
                }

                while (!temp.isEmpty()) {

                        billings.enqueue(temp.dequeue());
                }
        }


    // ==================================================
    // DATE VALIDATION
    // ==================================================

    private boolean isValidDateRange(
            LocalDate checkIn,
            LocalDate checkOut) {

        if (checkIn == null
                || checkOut == null) {

            System.out.println(
                    "Date cannot be empty."
            );

            return false;
        }

        if (checkIn.isBefore(
                LocalDate.now())) {

            System.out.println(
                    "\nCheck-in date cannot be "
                            + "before today."
            );

            return false;
        }

        if (!checkOut.isAfter(
                checkIn)) {

            System.out.println(
                    "\nCheck-out date must be "
                            + "after check-in date."
            );

            return false;
        }

        return true;
    }


    // ==================================================
    // ADD GUEST
    // ==================================================

    private void addGuestIfNotExists(
            Guest guest) {

        if (guestExists(
                guest.getPhoneNumber()
        )) {

            return;
        }

        guests.enqueue(guest);
    }


    // ==================================================
    // CHECK GUEST EXISTS
    // ==================================================

    private boolean guestExists(
            String phoneNumber) {

        QueueImplementation<Guest> temp =
                new QueueImplementation<>();

        boolean found = false;

        while (!guests.isEmpty()) {

            Guest guest =
                    guests.dequeue();

            if (guest.getPhoneNumber()
                    .equals(phoneNumber)) {

                found = true;
            }

            temp.enqueue(guest);
        }

        while (!temp.isEmpty()) {

            guests.enqueue(
                    temp.dequeue()
            );
        }

        return found;
    }


    // ==================================================
    // CREATE BOOKING
    // ==================================================

    private Booking createBooking(
            Guest guest,
            Room room) {

        String confirmationNumber =
                guest.getConfirmationNumber();

        room.setRoomStatus(
                "OCCUPIED"
        );

        Booking booking =
                new Booking(
                        confirmationNumber,
                        guest,
                        room
                );

        bookings.enqueue(
                booking
        );

        return booking;
    }


    // ==================================================
    // SEARCH BOOKING
    // ==================================================

    public Booking searchBooking(
            String confirmationNumber) {

        QueueImplementation<Booking> temp =
                new QueueImplementation<>();

        Booking foundBooking = null;

        while (!bookings.isEmpty()) {

            Booking booking =
                    bookings.dequeue();

            if (booking
                    .getConfirmationNumber()
                    .equalsIgnoreCase(
                            confirmationNumber
                    )) {

                foundBooking = booking;
            }

            temp.enqueue(booking);
        }

        while (!temp.isEmpty()) {

            bookings.enqueue(
                    temp.dequeue()
            );
        }

        return foundBooking;
    }


    // ==================================================
    // CANCEL BOOKING
    // ==================================================

    public void cancelBooking(
            String confirmationNumber) {

        Booking booking =
                searchBooking(
                        confirmationNumber
                );


        if (booking == null) {

            System.out.println(
                    "Booking not found."
            );

            return;
        }


        if (booking.getStatus()
                .equalsIgnoreCase(
                        "CANCELLED"
                )) {

            System.out.println(
                    "Booking is already cancelled."
            );

            return;
        }


        booking.setStatus(
                "CANCELLED"
        );


        booking.getRoom()
                .setRoomStatus(
                        "DIRTY"
                );


        // Queue
        cancelledBookings.enqueue(
                booking
        );


        System.out.println(
                "Booking "
                        + confirmationNumber
                        + " has been cancelled."
        );

        System.out.println(
                "Room "
                        + booking.getRoom()
                        .getRoomNumber()
                        + " status changed to DIRTY."
        );
    }


    // ==================================================
    // UNDO LAST CANCELLATION
    // ==================================================

    public void undoLastCancellation() {

        if (cancelledBookings.isEmpty()) {

            System.out.println(
                    "No cancellation available "
                            + "to undo."
            );

            return;
        }


        /*
         * Since only Queue ADT is used,
         * cancelled bookings are processed
         * using FIFO order.
         */

        Booking booking =
                cancelledBookings.dequeue();


        if (!booking.getStatus()
                .equalsIgnoreCase(
                        "CANCELLED"
                )) {

            System.out.println(
                    "Booking is not cancelled."
            );

            return;
        }


        /*
         * In this system, undoing a cancellation
         * changes the booking back to CONFIRMED.
         * The room becomes OCCUPIED again.
         */

        booking.setStatus(
                "CONFIRMED"
        );


        booking.getRoom()
                .setRoomStatus(
                        "OCCUPIED"
                );


        System.out.println(
                "Cancellation undone for "
                        + booking
                        .getConfirmationNumber()
        );
    }


    // ==================================================
    // DISPLAY BOOKINGS
    // ==================================================

    public void displayBookings() {

        System.out.println(
                "\n--- ALL BOOKINGS ---"
        );


        if (bookings.isEmpty()) {

            System.out.println(
                    "No bookings available."
            );

            return;
        }


        QueueImplementation<Booking> temp =
                new QueueImplementation<>();


        while (!bookings.isEmpty()) {

            Booking booking =
                    bookings.dequeue();

            System.out.println(
                    booking
            );

            temp.enqueue(booking);
        }


        while (!temp.isEmpty()) {

            bookings.enqueue(
                    temp.dequeue()
            );
        }
    }


    // ==================================================
    // PHONE VALIDATION
    // ==================================================

    public boolean phoneExists(
            String phoneNumber) {

        QueueImplementation<Guest> temp =
                new QueueImplementation<>();

        boolean found = false;


        while (!guests.isEmpty()) {

            Guest guest =
                    guests.dequeue();

            if (guest.getPhoneNumber()
                    .equals(phoneNumber)) {

                found = true;
            }

            temp.enqueue(guest);
        }


        while (!temp.isEmpty()) {

            guests.enqueue(
                    temp.dequeue()
            );
        }


        return found;
    }


    // ==================================================
    // GUEST LIST
    // ==================================================

    public void displayGuests() {

        System.out.println(
                "\n--- GUEST LIST ---"
        );


        if (guests.isEmpty()) {

            System.out.println(
                    "No guests registered."
            );

            return;
        }


        QueueImplementation<Guest> temp =
                new QueueImplementation<>();


        while (!guests.isEmpty()) {

            Guest guest =
                    guests.dequeue();

            System.out.println(
                    guest
            );

            System.out.println(
                    "--------------------------------"
            );

            temp.enqueue(guest);
        }


        while (!temp.isEmpty()) {

            guests.enqueue(
                    temp.dequeue()
            );
        }
    }


    // ==================================================
    // QUEUE SIZE
    // ==================================================

    public int getWalkInQueueSize() {

        return walkInQueue.size();
    }


    // ==================================================
    // CHECK QUEUE EMPTY
    // ==================================================

    public boolean isWalkInQueueEmpty() {

        return walkInQueue.isEmpty();
    }
}