/**
 * Author: Karlson Tan Zhi Ming
 * Class: QueueImplementation
 * Description: Implements the Queue ADT using linked nodes.
 */

package resort.queue;

public class QueueImplementation<T>
        implements QueueInterface<T> {

    private Node<T> front;
    private Node<T> rear;
    private int size;

    // ==============================
    // Node
    // ==============================

    private static class Node<T> {

        private T data;
        private Node<T> next;

        public Node(T data) {

            this.data = data;
            this.next = null;
        }
    }

    // ==============================
    // Constructor
    // ==============================

    public QueueImplementation() {

        front = null;
        rear = null;
        size = 0;
    }

    // ==============================
    // Enqueue
    // ==============================

    @Override
    public void enqueue(T item) {

        Node<T> newNode =
                new Node<>(item);

        if (isEmpty()) {

            front = newNode;
            rear = newNode;

        } else {

            rear.next = newNode;
            rear = newNode;
        }

        size++;
    }

    // ==============================
    // Dequeue
    // ==============================

    @Override
    public T dequeue() {

        if (isEmpty()) {

            return null;
        }

        T item = front.data;

        front = front.next;

        if (front == null) {

            rear = null;
        }

        size--;

        return item;
    }

    // ==============================
    // Peek
    // ==============================

    @Override
    public T peek() {

        if (isEmpty()) {

            return null;
        }

        return front.data;
    }

    // ==============================
    // Is Empty
    // ==============================

    @Override
    public boolean isEmpty() {

        return size == 0;
    }

    // ==============================
    // Size
    // ==============================

    @Override
    public int size() {

        return size;
    }
}