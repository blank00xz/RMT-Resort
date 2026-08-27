/**
 * Author: Karlson Tan Zhi Ming
 * Class: QueueInterface
 * Description: Defines the operations of the Queue Abstract Data Type.
 */

package resort.queue;

public interface QueueInterface<T> {

    void enqueue(T item);

    T dequeue();

    T peek();

    boolean isEmpty();

    int size();
}