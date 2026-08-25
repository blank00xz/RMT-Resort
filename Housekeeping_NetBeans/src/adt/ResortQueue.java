package adt;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ResortQueue<T> implements QueueInterface<T> {

    private class Node {
        private T data;
        private Node next;

        private Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node frontNode;
    private Node rearNode;
    private int numberOfEntries;

    public ResortQueue() {
        clear();
    }

    @Override
    public boolean isEmpty() {
        return numberOfEntries == 0;
    }

    @Override
    public int getNumberOfEntries() {
        return numberOfEntries;
    }

    @Override
    public void clear() {
        frontNode = null;
        rearNode = null;
        numberOfEntries = 0;
    }

    @Override
    public boolean enqueue(T newEntry) {
        Node newNode = new Node(newEntry);

        if (isEmpty()) {
            frontNode = newNode;
            rearNode = newNode;
        } else {
            rearNode.next = newNode;
            rearNode = newNode;
        }

        numberOfEntries++;
        return true;
    }

    @Override
    public T dequeue() {
        if (isEmpty()) {
            return null;
        }

        T frontData = frontNode.data;
        frontNode = frontNode.next;

        numberOfEntries--;

        if (isEmpty()) {
            rearNode = null;
        }

        return frontData;
    }

    @Override
    public T getFront() {
        if (isEmpty()) {
            return null;
        }

        return frontNode.data;
    }

    @Override
    public boolean contains(T anEntry) {
        Node currentNode = frontNode;

        while (currentNode != null) {
            if (anEntry == null) {
                if (currentNode.data == null) {
                    return true;
                }
            } else if (anEntry.equals(currentNode.data)) {
                return true;
            }

            currentNode = currentNode.next;
        }

        return false;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[numberOfEntries];

        Node currentNode = frontNode;
        int index = 0;

        while (currentNode != null) {
            result[index] = currentNode.data;
            index++;
            currentNode = currentNode.next;
        }

        return result;
    }

    @Override
    public Iterator<T> getIterator() {
        return new Iterator<T>() {

            private Node currentNode = frontNode;

            @Override
            public boolean hasNext() {
                return currentNode != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                T data = currentNode.data;
                currentNode = currentNode.next;

                return data;
            }
        };
    }
}