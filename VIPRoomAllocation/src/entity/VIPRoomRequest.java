package entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Represents a VIP guest's request for a priority room allocation.
 *
 * Priority (used by the non-linear priority queue ADT) is decided by:
 * 1. Higher loyalty tier wins.
 * 2. If tiers are equal, the earlier request wins.
 * 3. If still tied, the smaller confirmation number wins (deterministic
 *    tie-break so the heap ordering is stable).
 *
 * @author Your Name
 */
public class VIPRoomRequest implements Comparable<VIPRoomRequest> {

    public static final String WAITING = "WAITING";
    public static final String ALLOCATED = "ALLOCATED";
    public static final String CANCELLED = "CANCELLED";

    private String confirmationNumber;
    private VIPGuest guest;
    private String requestedRoomType;
    private LocalDateTime requestTime;
    private String status;

    public VIPRoomRequest() {
        this.requestTime = LocalDateTime.now();
        this.status = WAITING;
    }

    public VIPRoomRequest(String confirmationNumber, VIPGuest guest, String requestedRoomType,
            LocalDateTime requestTime) {
        this.confirmationNumber = confirmationNumber;
        this.guest = guest;
        this.requestedRoomType = requestedRoomType;
        this.requestTime = requestTime;
        this.status = WAITING;
    }

    public String getConfirmationNumber() {
        return confirmationNumber;
    }

    public void setConfirmationNumber(String confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }

    public VIPGuest getGuest() {
        return guest;
    }

    public void setGuest(VIPGuest guest) {
        this.guest = guest;
    }

    public String getRequestedRoomType() {
        return requestedRoomType;
    }

    public void setRequestedRoomType(String requestedRoomType) {
        this.requestedRoomType = requestedRoomType;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        VIPRoomRequest other = (VIPRoomRequest) obj;
        if (confirmationNumber == null || other.confirmationNumber == null) {
            return false;
        }
        return confirmationNumber.equalsIgnoreCase(other.confirmationNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(confirmationNumber == null ? null : confirmationNumber.toLowerCase());
    }

    @Override
    public int compareTo(VIPRoomRequest other) {
        // Rule 1: higher loyalty tier has higher priority
        int thisPoints = guest.getTier().getMinPointsRequired();
        int otherPoints = other.getGuest().getTier().getMinPointsRequired();
        if (thisPoints != otherPoints) {
            return Integer.compare(thisPoints, otherPoints);
        }

        // Rule 2: earlier request time has higher priority
        int timeComparison = other.getRequestTime().compareTo(this.requestTime);
        if (timeComparison != 0) {
            return timeComparison;
        }

        // Rule 3: smaller confirmation number has higher priority (tie-break)
        return other.getConfirmationNumber().compareToIgnoreCase(this.confirmationNumber);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String guestId = guest == null ? "N/A" : guest.getGuestId();
        String guestName = guest == null ? "N/A" : guest.getName();
        String tier = guest == null || guest.getTier() == null ? "N/A" : guest.getTier().toString();
        String formattedTime = requestTime == null ? "N/A" : requestTime.format(formatter);

        return String.format(
                "Confirmation: %-8s | Guest: %-6s | Name: %-15s | Tier: %-8s | Room Type: %-8s | Status: %-9s | Requested: %s",
                confirmationNumber, guestId, guestName, tier, requestedRoomType, status, formattedTime);
    }
}
