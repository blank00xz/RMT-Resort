package entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a completed VIP room allocation: which request was matched to
 * which room, and when.
 *
 * @author Your Name
 */
public class RoomAllocationRecord {

    private String allocationId;
    private VIPRoomRequest request;
    private VIPRoom allocatedRoom;
    private LocalDateTime allocationTime;

    public RoomAllocationRecord() {
    }

    public RoomAllocationRecord(String allocationId, VIPRoomRequest request, VIPRoom allocatedRoom,
            LocalDateTime allocationTime) {
        this.allocationId = allocationId;
        this.request = request;
        this.allocatedRoom = allocatedRoom;
        this.allocationTime = allocationTime;
    }

    public String getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(String allocationId) {
        this.allocationId = allocationId;
    }

    public VIPRoomRequest getRequest() {
        return request;
    }

    public void setRequest(VIPRoomRequest request) {
        this.request = request;
    }

    public VIPRoom getAllocatedRoom() {
        return allocatedRoom;
    }

    public void setAllocatedRoom(VIPRoom allocatedRoom) {
        this.allocatedRoom = allocatedRoom;
    }

    public LocalDateTime getAllocationTime() {
        return allocationTime;
    }

    public void setAllocationTime(LocalDateTime allocationTime) {
        this.allocationTime = allocationTime;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String time = allocationTime == null ? "N/A" : allocationTime.format(formatter);

        return String.format("Allocation: %-6s | %s | Room: %-6s | Allocated at: %s",
                allocationId,
                request == null ? "N/A" : request.toString(),
                allocatedRoom == null ? "N/A" : allocatedRoom.getRoomNumber(),
                time);
    }
}
