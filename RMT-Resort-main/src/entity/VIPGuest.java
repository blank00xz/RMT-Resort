package entity;

import java.util.Objects;

/**
 * Represents a VIP / loyalty-member guest who may request a priority room
 * allocation.
 *
 * @author Goh Wei Hong
 */
public class VIPGuest {

    private String guestId;        // e.g. "M1001"
    private String name;
    private String contactNumber;
    private Loyalty tier;
    private String status;         // e.g. "WAITING", "CHECKED_IN"

    public VIPGuest() {
    }

    public VIPGuest(String guestId, String name, String contactNumber, Loyalty tier, String status) {
        this.guestId = guestId;
        this.name = name;
        this.contactNumber = contactNumber;
        this.tier = tier;
        this.status = status;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Loyalty getTier() {
        return tier;
    }

    public void setTier(Loyalty tier) {
        this.tier = tier;
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
        VIPGuest other = (VIPGuest) obj;
        if (guestId == null || other.guestId == null) {
            return false;
        }
        return guestId.equalsIgnoreCase(other.guestId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guestId == null ? null : guestId.toLowerCase());
    }

    @Override
    public String toString() {
        return String.format("ID: %-8s | Name: %-15s | Tier: %-8s | Status: %-10s | Contact: %s",
                guestId, name, tier, status, contactNumber);
    }
}
