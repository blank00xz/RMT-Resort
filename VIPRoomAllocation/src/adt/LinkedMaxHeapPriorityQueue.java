package adt;

/**
 * A true node-based (linked) max-heap implementation of a priority queue.
 *
 * Unlike an array-backed heap, entries here are stored in real tree nodes
 * connected by {@code left}, {@code right}, and {@code parent} references,
 * which is what makes this a genuinely NON-LINEAR abstract data type (a
 * complete binary tree of nodes) rather than a sequential structure dressed
 * up as one.
 *
 * Even though there is no backing array, insertion and removal still run in
 * O(log n): the 1-based "array position" a node would occupy in a complete
 * binary tree is used purely as a set of left/right directions (its binary
 * representation) to walk down from the root to the correct node.
 *
 * Example: position 6 in binary is 110. Dropping the leading 1 (which
 * represents the root itself) leaves "10": 1 = go right, 0 = go left.
 * So position 6 is reached by root -> right -> left. This matches the usual
 * array-heap rule where a node at index i has children at 2i and 2i+1.
 *
 * @param <T> type of comparable entry stored in the heap
 * @author Your Name
 */
public class LinkedMaxHeapPriorityQueue<T extends Comparable<T>>
        implements PriorityQueueADT<T> {

    /**
     * A single node of the linked heap tree.
     */
    private static class HeapNode<T> {

        private T data;
        private HeapNode<T> left;
        private HeapNode<T> right;
        private HeapNode<T> parent;

        private HeapNode(T data) {
            this.data = data;
        }
    }

    private HeapNode<T> root;
    private int numberOfEntries;

    public LinkedMaxHeapPriorityQueue() {
        root = null;
        numberOfEntries = 0;
    }

    @Override
    public boolean enqueue(T newEntry) {
        if (newEntry == null) {
            return false;
        }

        numberOfEntries++;

        HeapNode<T> newNode = new HeapNode<>(newEntry);

        if (root == null) {
            // First node becomes the root of the tree
            root = newNode;
        } else {
            // The new node's position in the "complete tree" is numberOfEntries;
            // its parent sits at position numberOfEntries / 2
            HeapNode<T> parentNode = findNodeAtPosition(numberOfEntries / 2);

            newNode.parent = parentNode;

            if (parentNode.left == null) {
                parentNode.left = newNode;
            } else {
                parentNode.right = newNode;
            }
        }

        heapifyUp(newNode);

        return true;
    }

    /**
     * Restores the max-heap property by repeatedly swapping the given node's
     * data with its parent's data while it has higher priority than its
     * parent.
     */
    private void heapifyUp(HeapNode<T> node) {
        while (node.parent != null
                && node.data.compareTo(node.parent.data) > 0) {

            T temp = node.data;
            node.data = node.parent.data;
            node.parent.data = temp;

            node = node.parent;
        }
    }

    @Override
    public T dequeue() {
        if (isEmpty()) {
            return null;
        }

        T highestPriorityEntry = root.data;

        if (numberOfEntries == 1) {
            root = null;
            numberOfEntries--;
            return highestPriorityEntry;
        }

        // The last node in level order (at position numberOfEntries) is
        // moved to the root, then the removed leaf is detached from its
        // parent.
        HeapNode<T> lastNode = findNodeAtPosition(numberOfEntries);

        root.data = lastNode.data;

        detachNode(lastNode);

        numberOfEntries--;

        heapifyDown(root);

        return highestPriorityEntry;
    }

    /**
     * Removes a leaf node from its parent's left/right reference.
     */
    private void detachNode(HeapNode<T> node) {
        HeapNode<T> parentNode = node.parent;

        if (parentNode.right == node) {
            parentNode.right = null;
        } else {
            parentNode.left = null;
        }

        node.parent = null;
    }

    /**
     * Restores the max-heap property downward from the given node by
     * repeatedly swapping with the higher-priority child.
     */
    private void heapifyDown(HeapNode<T> node) {
        while (node.left != null) {

            HeapNode<T> higherPriorityChild = node.left;

            if (node.right != null
                    && node.right.data.compareTo(node.left.data) > 0) {
                higherPriorityChild = node.right;
            }

            if (higherPriorityChild.data.compareTo(node.data) > 0) {

                T temp = node.data;
                node.data = higherPriorityChild.data;
                higherPriorityChild.data = temp;

                node = higherPriorityChild;
            } else {
                break;
            }
        }
    }

    /**
     * Walks from the root to the node that would sit at the given 1-based
     * "array position" of a complete binary tree, using the binary digits
     * of the position as left/right directions.
     */
    private HeapNode<T> findNodeAtPosition(int position) {
        if (position <= 1) {
            return root;
        }

        // highestOneBit isolates the leading 1, which represents the root
        // and should not be treated as a direction
        int highestBit = Integer.highestOneBit(position);

        HeapNode<T> current = root;

        for (int bit = highestBit >> 1; bit >= 1; bit >>= 1) {

            if ((position & bit) != 0) {
                current = current.right;
            } else {
                current = current.left;
            }
        }

        return current;
    }

    @Override
    public T peek() {
        return isEmpty() ? null : root.data;
    }

    @Override
    public boolean contains(T anEntry) {
        return containsHelper(root, anEntry);
    }

    private boolean containsHelper(HeapNode<T> node, T anEntry) {
        if (node == null) {
            return false;
        }

        if (node.data.equals(anEntry)) {
            return true;
        }

        return containsHelper(node.left, anEntry)
                || containsHelper(node.right, anEntry);
    }

    @Override
    public int getNumberOfEntries() {
        return numberOfEntries;
    }

    @Override
    public boolean isEmpty() {
        return numberOfEntries == 0;
    }

    @Override
    public void clear() {
        root = null;
        numberOfEntries = 0;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[numberOfEntries];
        fillArray(root, result, new int[]{0});
        return result;
    }

    /**
     * Fills the array with every node's data using a preorder traversal.
     * A single-element int array is used as a mutable index counter since
     * Java does not allow primitive parameters to be modified by reference.
     */
    private void fillArray(HeapNode<T> node, Object[] array, int[] indexHolder) {
        if (node == null) {
            return;
        }

        array[indexHolder[0]] = node.data;
        indexHolder[0]++;

        fillArray(node.left, array, indexHolder);
        fillArray(node.right, array, indexHolder);
    }
}
