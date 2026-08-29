package control;

import entity.Loyalty;
import entity.RoomAllocationRecord;

/**
 * Handles filtering and sorting logic for VIP room-allocation reports.
 *
 * @author Your Name
 */
public class VIPRoomAllocationReportControl {

    /**
     * Filters completed allocation records by loyalty tier (null = all
     * tiers) and sorts them so the highest tier / most recent allocation
     * appears first. Uses a manual insertion sort (no java.util.Collections)
     * to stay consistent with DSA-assignment expectations.
     */
    public RoomAllocationRecord[] generateAllocationReport(
            RoomAllocationRecord[] sourceRecords, Loyalty tierFilter) {

        if (sourceRecords == null) {
            return new RoomAllocationRecord[0];
        }

        int matchingCount = 0;
        for (RoomAllocationRecord record : sourceRecords) {
            if (matchesFilter(record, tierFilter)) {
                matchingCount++;
            }
        }

        RoomAllocationRecord[] result = new RoomAllocationRecord[matchingCount];
        int index = 0;
        for (RoomAllocationRecord record : sourceRecords) {
            if (matchesFilter(record, tierFilter)) {
                result[index++] = record;
            }
        }

        sortByTierThenRecency(result);

        return result;
    }

    private boolean matchesFilter(RoomAllocationRecord record, Loyalty tierFilter) {
        if (record == null || record.getRequest() == null || record.getRequest().getGuest() == null) {
            return false;
        }
        return tierFilter == null || record.getRequest().getGuest().getTier() == tierFilter;
    }

    private boolean shouldComeBefore(RoomAllocationRecord a, RoomAllocationRecord b) {
        int aPoints = a.getRequest().getGuest().getTier().getMinPointsRequired();
        int bPoints = b.getRequest().getGuest().getTier().getMinPointsRequired();

        if (aPoints != bPoints) {
            return aPoints > bPoints;
        }
        return a.getAllocationTime().isAfter(b.getAllocationTime());
    }

    private void sortByTierThenRecency(RoomAllocationRecord[] records) {
        for (int i = 1; i < records.length; i++) {
            RoomAllocationRecord current = records[i];
            int j = i - 1;

            while (j >= 0 && shouldComeBefore(current, records[j])) {
                records[j + 1] = records[j];
                j--;
            }
            records[j + 1] = current;
        }
    }
}
