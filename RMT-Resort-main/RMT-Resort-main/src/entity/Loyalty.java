package entity;

/**
 * Represents the loyalty membership tiers used to prioritise VIP room
 * allocation. A higher {@code minPointsRequired} means a higher tier and a
 * higher allocation priority.
 *
 * @author Your Name
 */
public enum Loyalty {
    SILVER(0),
    GOLD(1000),
    PLATINUM(5000),
    DIAMOND(15000),
    ELITE(30000);

    private final int minPointsRequired;

    Loyalty(int minPointsRequired) {
        this.minPointsRequired = minPointsRequired;
    }

    public int getMinPointsRequired() {
        return minPointsRequired;
    }
}
