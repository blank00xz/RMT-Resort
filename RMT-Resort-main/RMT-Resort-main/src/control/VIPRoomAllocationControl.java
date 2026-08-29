package control;

import adt.viproom.LinkedMaxHeapPriorityQueue;
import adt.viproom.PriorityQueueADT;
import entity.VIPRoom;
import entity.RoomAllocationRecord;
import entity.VIPRoomRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements the business logic for VIP & Loyalty Tier priority room
 * allocation. Orchestrates the non-linear priority queue ADT together with
 * the resort's room inventory.
 *
 * NOTE: allocationHistory / cancelledRequests / roomList currently use
 * ArrayList as a placeholder so this module runs standalone. Swap these for
 * your team's shared Linear ADT (list/queue) implementation once it is
 * merged in, so every module manages data consistently.
 *
 * @author Your Name
 */
public class VIPRoomAllocationControl {

    private final PriorityQueueADT<VIPRoomRequest> waitingQueue;
    private final List<VIPRoom> roomList;
    private final List<RoomAllocationRecord> allocationHistory;
    private final List<VIPRoomRequest> cancelledRequests;

    private int nextAllocationNumber;
    private int nextConfirmationNumber;

    public VIPRoomAllocationControl() {
        waitingQueue = new LinkedMaxHeapPriorityQueue<>();
        roomList = new ArrayList<>();
        allocationHistory = new ArrayList<>();
        cancelledRequests = new ArrayList<>();
        nextAllocationNumber = 1;
        nextConfirmationNumber = 1;
    }

    /**
     * Validates and submits a new VIP room request into the priority queue.
     */
    public boolean submitRequest(VIPRoomRequest request) {
        if (request == null
                || request.getConfirmationNumber() == null
                || !request.getConfirmationNumber().matches("\\d{8}")
                || request.getGuest() == null
                || request.getGuest().getGuestId() == null
                || request.getGuest().getGuestId().isBlank()
                || request.getGuest().getName() == null
                || request.getGuest().getName().isBlank()
                || request.getGuest().getTier() == null
                || request.getRequestedRoomType() == null
                || request.getRequestedRoomType().isBlank()
                || request.getRequestTime() == null) {
            return false;
        }

        // Confirmation numbers must stay unique across every request state
        if (waitingQueue.contains(request)
                || searchAllocationByConfirmation(request.getConfirmationNumber()) != null
                || searchCancelledRequestByConfirmation(request.getConfirmationNumber()) != null) {
            return false;
        }

        request.setStatus(VIPRoomRequest.WAITING);

        return waitingQueue.enqueue(request);
    }

    /** Returns the highest-priority waiting request without removing it. */
    public VIPRoomRequest viewNextRequest() {
        return waitingQueue.peek();
    }

    public int getWaitingRequestCount() {
        return waitingQueue.getNumberOfEntries();
    }

    public int getAllocationCount() {
        return allocationHistory.size();
    }

    public int getCancelledRequestCount() {
        return cancelledRequests.size();
    }

    public int getRoomCount() {
        return roomList.size();
    }

    public VIPRoom[] getAllRooms() {
        return roomList.toArray(new VIPRoom[0]);
    }

    public RoomAllocationRecord[] getAllocationHistory() {
        return allocationHistory.toArray(new RoomAllocationRecord[0]);
    }

    public VIPRoomRequest[] getCancelledRequests() {
        return cancelledRequests.toArray(new VIPRoomRequest[0]);
    }

    /**
     * Returns every waiting request ordered from highest to lowest priority,
     * without disturbing the original priority queue.
     */
    public VIPRoomRequest[] getWaitingRequestsInPriorityOrder() {
        Object[] entries = waitingQueue.toArray();

        // Copy into a temporary heap purely to read off priority order
        PriorityQueueADT<VIPRoomRequest> queueCopy = new LinkedMaxHeapPriorityQueue<>();
        for (Object entry : entries) {
            queueCopy.enqueue((VIPRoomRequest) entry);
        }

        VIPRoomRequest[] ordered = new VIPRoomRequest[entries.length];
        for (int i = 0; i < ordered.length; i++) {
            ordered[i] = queueCopy.dequeue();
        }

        return ordered;
    }

    public VIPRoomRequest searchWaitingRequestByConfirmation(String confirmationNumber) {
        if (confirmationNumber == null || confirmationNumber.isBlank()) {
            return null;
        }

        Object[] entries = waitingQueue.toArray();
        for (Object entry : entries) {
            VIPRoomRequest request = (VIPRoomRequest) entry;
            if (request.getConfirmationNumber().equalsIgnoreCase(confirmationNumber)) {
                return request;
            }
        }
        return null;
    }

    public VIPRoomRequest searchCancelledRequestByConfirmation(String confirmationNumber) {
        if (confirmationNumber == null || confirmationNumber.isBlank()) {
            return null;
        }
        for (VIPRoomRequest request : cancelledRequests) {
            if (request.getConfirmationNumber().equalsIgnoreCase(confirmationNumber)) {
                return request;
            }
        }
        return null;
    }

    public RoomAllocationRecord searchAllocationByConfirmation(String confirmationNumber) {
        if (confirmationNumber == null || confirmationNumber.isBlank()) {
            return null;
        }
        for (RoomAllocationRecord record : allocationHistory) {
            if (record.getRequest().getConfirmationNumber().equalsIgnoreCase(confirmationNumber)) {
                return record;
            }
        }
        return null;
    }

    /**
     * Cancels and removes a waiting request from the priority queue by
     * rebuilding the queue without it.
     */
    public VIPRoomRequest cancelWaitingRequest(String confirmationNumber) {
        VIPRoomRequest requestToCancel = searchWaitingRequestByConfirmation(confirmationNumber);

        if (requestToCancel == null) {
            return null;
        }

        Object[] entries = waitingQueue.toArray();
        waitingQueue.clear();

        for (Object entry : entries) {
            VIPRoomRequest request = (VIPRoomRequest) entry;
            if (!request.equals(requestToCancel)) {
                waitingQueue.enqueue(request);
            }
        }

        requestToCancel.setStatus(VIPRoomRequest.CANCELLED);
        cancelledRequests.add(requestToCancel);

        return requestToCancel;
    }

    /**
     * Allocates a room to the highest-priority waiting request that has a
     * compatible available room. Lower-priority requests are never chosen
     * ahead of a higher-priority one, even if the higher-priority guest's
     * room type is temporarily unavailable (that request is simply
     * skipped and returned to the queue).
     */
    public RoomAllocationRecord allocateNextRequest() {
        if (waitingQueue.isEmpty()) {
            return null;
        }

        PriorityQueueADT<VIPRoomRequest> skipped = new LinkedMaxHeapPriorityQueue<>();

        VIPRoomRequest selectedRequest = null;
        VIPRoom availableRoom = null;

        while (!waitingQueue.isEmpty()) {
            VIPRoomRequest currentRequest = waitingQueue.dequeue();

            VIPRoom matchingRoom = searchAvailableRoomByType(currentRequest.getRequestedRoomType());

            if (matchingRoom != null) {
                selectedRequest = currentRequest;
                availableRoom = matchingRoom;
                break;
            }

            skipped.enqueue(currentRequest);
        }

        while (!skipped.isEmpty()) {
            waitingQueue.enqueue(skipped.dequeue());
        }

        if (selectedRequest == null) {
            return null;
        }

        availableRoom.setAvailable(false);
        selectedRequest.setStatus(VIPRoomRequest.ALLOCATED);

        RoomAllocationRecord record = new RoomAllocationRecord(
                generateAllocationId(), selectedRequest, availableRoom, LocalDateTime.now());

        allocationHistory.add(record);

        return record;
    }

    private String generateAllocationId() {
        String id = String.format("A%04d", nextAllocationNumber);
        nextAllocationNumber++;
        return id;
    }

    public String generateConfirmationNumber() {
        String confirmationNumber = String.format("%08d", nextConfirmationNumber);
        nextConfirmationNumber++;
        return confirmationNumber;
    }

    public boolean addRoom(VIPRoom room) {
        if (room == null
                || room.getRoomNumber() == null || room.getRoomNumber().isBlank()
                || room.getRoomType() == null || room.getRoomType().isBlank()
                || searchRoomByNumber(room.getRoomNumber()) != null) {
            return false;
        }
        return roomList.add(room);
    }

    public VIPRoom searchRoomByNumber(String roomNumber) {
        if (roomNumber == null) {
            return null;
        }
        for (VIPRoom room : roomList) {
            if (room.getRoomNumber().equalsIgnoreCase(roomNumber)) {
                return room;
            }
        }
        return null;
    }

    public boolean updateRoomAvailability(String roomNumber, boolean available) {
        VIPRoom room = searchRoomByNumber(roomNumber);
        if (room == null) {
            return false;
        }
        room.setAvailable(available);
        return true;
    }

    public VIPRoom searchAvailableRoomByType(String requestedRoomType) {
        if (requestedRoomType == null || requestedRoomType.isBlank()) {
            return null;
        }
        for (VIPRoom room : roomList) {
            if (room.isAvailable() && room.getRoomType().equalsIgnoreCase(requestedRoomType)) {
                return room;
            }
        }
        return null;
    }
}
