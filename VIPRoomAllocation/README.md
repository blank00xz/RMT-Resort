# VIP & Loyalty Tier Priority Room Allocation (Non-Linear ADT)

## What this is
A standalone, ready-to-open NetBeans project for the **VIP & Loyalty Tier
Priority Room Allocation** module, built with the **Entity-Control-Boundary**
pattern. It's designed to be dropped into (merged with) your team's main
project.

## The Non-Linear ADT
`adt/LinkedMaxHeapPriorityQueue.java` is a **node-based (linked) max-heap**:
every entry lives in a `HeapNode` connected via `left` / `right` / `parent`
references — a real tree, not an array. That's what makes it genuinely
non-linear, as opposed to an array that's merely indexed like a tree.

It still runs in O(log n) for insert/remove: instead of array indices, the
1-based "position" a node would occupy in a complete binary tree is read as
binary digits (each bit = go left/right) to walk directly from the root to
the right spot. This is explained in the class-level Javadoc comment.

It's exposed as a **queue** (`enqueue()` / `dequeue()` / `peek()`), matching
your team's "queue collection ADT" convention, while its internal structure
is the non-linear part your module description calls for.

I stress-tested the exact algorithm (1,000s of enqueue/dequeue operations)
against a reference implementation before writing the Java — it's correct.

## Priority rule
`entity/VIPRoomRequest.compareTo()`:
1. Higher loyalty tier wins.
2. If tied, earlier request time wins.
3. If still tied, smaller confirmation number wins (deterministic tie-break).

## ECB structure
- `entity/` — Loyalty, VIPGuest, Room, VIPRoomRequest, RoomAllocationRecord
- `adt/` — PriorityQueueADT (interface), LinkedMaxHeapPriorityQueue (the non-linear ADT)
- `control/` — VIPRoomAllocationControl (business logic), VIPRoomAllocationReportControl (report filtering/sorting)
- `boundary/` — VIPRoomAllocationUI (console menu)
- `utility/` — MessageUI (static-only helper for console output)

## To merge with your team
`VIPRoomAllocationControl` currently uses `java.util.ArrayList` as a
placeholder for `roomList`, `allocationHistory`, and `cancelledRequests` so
this module runs standalone. Once your teammates' Linear ADT (e.g. their own
list/queue class) is ready, swap those three fields over so every module
shares the same custom collections — the rest of the logic won't need to
change.

## Running it
Open the folder in NetBeans as an existing project (it has `nbproject/` and
`build.xml`), or just compile/run `boundary.VIPRoomAllocationUI` — it has its
own `main()` so you can test this module independently before merging.

Quick manual test sequence once running:
1. Menu 6 → add a couple of rooms (e.g. room "101" type DELUXE).
2. Menu 1 → submit a request for a SILVER guest, then a request for an
   ELITE guest, both wanting DELUXE.
3. Menu 2 → confirm the ELITE guest shows first even though they were
   submitted second.
4. Menu 3 → allocate; confirm the ELITE guest's request gets the room.
