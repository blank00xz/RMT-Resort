package adt;

import entity.Guest;

public interface BSTInterface {
    //any bst can provide these operations:
    boolean isEmpty();
    void add(Guest guest);
    Guest search(String confirmationNumber);
}