// @author : Ong Wei Chen 
//BST takes T that compare with another T
package adt.frontdesk;

import java.util.Iterator;

public class BST<T extends Comparable<T>> implements BSTInterface<T> {
    //root
    private TreeNode<T> root;
    
    public BST(){
        root = null;
    }

    public boolean isEmpty(){
        return root == null;

    }

    //bst add method
    public T add(T newEntry) {

        if (root == null){
            root = new TreeNode<>(newEntry);
            return null;

        }

        TreeNode<T> current = root;

        while (true){
            int comparison = newEntry.compareTo(current.getData());

            if (comparison < 0) {
                if (current.getLeftChild() == null){
                    current.setLeftChild(new TreeNode<>(newEntry));
                    return null;
                }

                current = current.getLeftChild();

            }else if (comparison > 0){
                if (current.getRightChild() == null){
                    current.setRightChild(new TreeNode<>(newEntry));
                    return null;
                }

            current = current.getRightChild();

            }else {

                T oldEntry = current.getData(); //set current as old entry
                current.setData(newEntry); //update new guest data for old entry(confirmation number)
                return oldEntry; //still using old entry but with updated guest data
            }
        }
    }
    //user enter T(8 num) as a search entry
    public T getEntry(T entry) {

        TreeNode<T> current = root;

        while (current != null) {

            int comparison = entry.compareTo(current.getData());
            
            if (comparison == 0) { //match
                return current.getData(); //returns guest object

            } else if (comparison < 0) {
                current = current.getLeftChild();

            } else {
                current = current.getRightChild();
            }
        }

        return null;
    }

    public boolean contains(T entry) {
        return getEntry(entry) != null;
    }

    public void clear() {
        root = null;
    }

    public T remove(T entry) {

        TreeNode<T> parent = null;
        TreeNode<T> current = root;

        // search for the node to remove
        while (current != null) {

            int comparison = entry.compareTo(current.getData());

            if (comparison == 0) {
                break;
            }

            parent = current;

            if (comparison < 0) {
                current = current.getLeftChild();
            } else {
                current = current.getRightChild();
            }
        }

        // entry was not found
        if (current == null) {
            return null;
        }

        T removedEntry = current.getData();

        // case 1: node has no children
        if (current.getLeftChild() == null &&
            current.getRightChild() == null) {

            if (parent == null) {
                // removing the root
                root = null;
            } else if (parent.getLeftChild() == current) {
                parent.setLeftChild(null);
            } else {
                parent.setRightChild(null);
            }
        }

        // case 2: node has only a left child
        else if (current.getRightChild() == null) {

            if (parent == null) {
                // removing root
                root = current.getLeftChild();
            } else if (parent.getLeftChild() == current) {
                parent.setLeftChild(current.getLeftChild());
            } else {
                parent.setRightChild(current.getLeftChild());
            }
        }

        // case 2: node has only a right child
        else if (current.getLeftChild() == null) {

            if (parent == null) {
                // removing root
                root = current.getRightChild();
            } else if (parent.getLeftChild() == current) {
                parent.setLeftChild(current.getRightChild());
            } else {
                parent.setRightChild(current.getRightChild());
            }
        }

        // case 3: node has two children
        else {

            TreeNode<T> largestParent = current;
            TreeNode<T> largest = current.getLeftChild();

            // find largest node in left subtree
            while (largest.getRightChild() != null) {
                largestParent = largest;
                largest = largest.getRightChild();
            }

            // replace current data with largest data
            current.setData(largest.getData());

            // remove the largest node from the left subtree
            if (largestParent == current) {
                largestParent.setLeftChild(largest.getLeftChild());
            } else {
                largestParent.setRightChild(largest.getLeftChild());
            }
        }

        return removedEntry;
    }

    public Iterator<T> getInOrderIterator() {
        return new InOrderIterator();
    }

        private class InOrderIterator implements Iterator<T> {

        private QueueInterface<T> queue = new ArrayQueue<>();

        public InOrderIterator() {
            inOrder(root);
        }

        private void inOrder(TreeNode<T> node) {

            if (node != null) {

                inOrder(node.getLeftChild());

                queue.enqueue(node.getData());

                inOrder(node.getRightChild());
            }
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public T next() {

            if (!queue.isEmpty()) {
                return queue.dequeue();
            }

            return null;
        }
    }


}
