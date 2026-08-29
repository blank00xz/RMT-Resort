package adt.housekeeping;

import java.util.Iterator;

public interface QueueInterface<T> {

    boolean enqueue(T newEntry);

    T dequeue();

    T getFront();

    boolean isEmpty();

    int getNumberOfEntries();

    void clear();

    boolean contains(T anEntry);

    Object[] toArray();

    Iterator<T> getIterator();
}