package adt;

import java.util.Iterator;

public interface BSTInterface<T extends Comparable<T>> {
    //any bst can provide these operations:
    boolean isEmpty();
    void clear();
    T add(T newEntry); //update old 8 num entry with new guest entry data
    T remove(T entry);
    boolean contains(T entry);
    T getEntry(T entry);
    Iterator<T> getInOrderIterator();

}