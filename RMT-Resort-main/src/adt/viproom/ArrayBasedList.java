package adt.viproom;

/**
 * A user-defined, resizable array-based implementation of {@link ListADT}.
 * Written from scratch (no java.util.ArrayList / Collections Framework)
 * so this module stays a Linear ADT built by hand, as required by the
 * assignment.
 *
 * @param <T> the type of entry stored in the list
 * @author Goh Wei Hong
 */
public class ArrayBasedList<T> implements ListADT<T> {

    private static final int DEFAULT_CAPACITY = 10;

    private Object[] entries;
    private int numberOfEntries;

    public ArrayBasedList() {
        entries = new Object[DEFAULT_CAPACITY];
        numberOfEntries = 0;
    }

    private void ensureCapacity() {
        if (numberOfEntries == entries.length) {
            Object[] biggerArray = new Object[entries.length * 2];
            for (int i = 0; i < entries.length; i++) {
                biggerArray[i] = entries[i];
            }
            entries = biggerArray;
        }
    }

    @Override
    public boolean add(T newEntry) {
        if (newEntry == null) {
            return false;
        }
        ensureCapacity();
        entries[numberOfEntries] = newEntry;
        numberOfEntries++;
        return true;
    }

    @Override
    public boolean remove(T anEntry) {
        if (anEntry == null) {
            return false;
        }

        for (int i = 0; i < numberOfEntries; i++) {
            if (entries[i].equals(anEntry)) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T removeAt(int index) {
        if (index < 0 || index >= numberOfEntries) {
            return null;
        }

        T removedEntry = (T) entries[index];

        for (int i = index; i < numberOfEntries - 1; i++) {
            entries[i] = entries[i + 1];
        }
        entries[numberOfEntries - 1] = null;
        numberOfEntries--;

        return removedEntry;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index < 0 || index >= numberOfEntries) {
            return null;
        }
        return (T) entries[index];
    }

    @Override
    public boolean contains(T anEntry) {
        if (anEntry == null) {
            return false;
        }
        for (int i = 0; i < numberOfEntries; i++) {
            if (entries[i].equals(anEntry)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return numberOfEntries;
    }

    @Override
    public boolean isEmpty() {
        return numberOfEntries == 0;
    }

    @Override
    public void clear() {
        entries = new Object[DEFAULT_CAPACITY];
        numberOfEntries = 0;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[numberOfEntries];
        for (int i = 0; i < numberOfEntries; i++) {
            result[i] = entries[i];
        }
        return result;
    }
}
