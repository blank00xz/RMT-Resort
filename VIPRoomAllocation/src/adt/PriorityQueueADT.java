package adt;

/**
 * Defines the operations of a generic priority queue collection.
 * Entries with a higher natural ordering (per {@link Comparable}) are
 * treated as having a higher priority.
 *
 * @param <T> the type of entry stored in the priority queue
 * @author Your Name
 */
public interface PriorityQueueADT<T extends Comparable<T>> {

    /**
     * Adds an entry and reorganises the queue so that priority order is kept.
     *
     * @param newEntry entry to add
     * @return true if added successfully; false if newEntry is null
     */
    boolean enqueue(T newEntry);

    /**
     * Removes and returns the highest-priority entry.
     *
     * @return the highest-priority entry, or null if the queue is empty
     */
    T dequeue();

    /**
     * Returns the highest-priority entry without removing it.
     *
     * @return the highest-priority entry, or null if the queue is empty
     */
    T peek();

    /**
     * Checks whether a matching entry exists in the queue.
     *
     * @param anEntry entry to search for
     * @return true if found
     */
    boolean contains(T anEntry);

    /**
     * Returns the current number of entries.
     *
     * @return number of entries
     */
    int getNumberOfEntries();

    /**
     * Checks whether the priority queue is empty.
     *
     * @return true if empty
     */
    boolean isEmpty();

    /**
     * Removes every entry from the queue.
     */
    void clear();

    /**
     * Returns all entries as an array without changing the queue. Order is
     * not guaranteed to be priority order.
     *
     * @return array containing every entry currently stored
     */
    Object[] toArray();
}
