package adt.viproom;

/**
 * Defines the operations of a generic, user-defined list collection.
 * This exists so this module never needs to rely on the Java Collections
 * Framework (java.util.List / ArrayList / etc.), per the assignment's
 * FAQ rules.
 *
 * @param <T> the type of entry stored in the list
 * @author Goh Wei Hong
 */
public interface ListADT<T> {

    /**
     * Appends an entry to the end of the list.
     *
     * @param newEntry entry to add
     * @return true if added successfully; false if newEntry is null
     */
    boolean add(T newEntry);

    /**
     * Removes the first occurrence of a matching entry.
     *
     * @param anEntry entry to remove
     * @return true if an entry was found and removed
     */
    boolean remove(T anEntry);

    /**
     * Removes and returns the entry at the given 0-based index.
     *
     * @param index position of the entry to remove
     * @return the removed entry, or null if index is out of range
     */
    T removeAt(int index);

    /**
     * Returns the entry at the given 0-based index without removing it.
     *
     * @param index position of the entry
     * @return the entry at that position, or null if index is out of range
     */
    T get(int index);

    /**
     * Checks whether a matching entry exists in the list.
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
    int size();

    /**
     * Checks whether the list is empty.
     *
     * @return true if empty
     */
    boolean isEmpty();

    /**
     * Removes every entry from the list.
     */
    void clear();

    /**
     * Returns all entries as a plain array, in list order.
     *
     * @return array containing every entry currently stored
     */
    Object[] toArray();
}
