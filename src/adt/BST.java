package adt;

import entity.Guest;

public class BST implements BSTInterface{

    private TreeNode root;
    
    public BST(){
        root = null;
    }

    public boolean isEmpty(){
        return root == null;

    }

    //bst add method
    public void add(Guest guest){

        if (root == null){
            root = new TreeNode(guest);
            return;

        }

        TreeNode current = root;

        while (true){
            String currentNumber = current.getData().getConfirmationNumber();
            int comparison = guest.getConfirmationNumber().compareTo(currentNumber);

            if (comparison < 0) {
                if (current.getLeftChild() == null){
                    current.setLeftChild(new TreeNode(guest));
                    return;
                }

                current = current.getLeftChild();

            }else if (comparison > 0){
                if (current.getRightChild() == null){
                    current.setRightChild(new TreeNode(guest));
                    return;
                }

            current = current.getRightChild();

            }else {
                return;
                }

    
        }

    }

    public Guest search(String confirmationNumber) {

        TreeNode current = root;

        while (current != null) {
            String currentNumber = current.getData().getConfirmationNumber();

            int comparison = confirmationNumber.compareTo(currentNumber);

            if (comparison == 0) {
                return current.getData();

            }else if (comparison < 0){
                current = current.getLeftChild();

            }else {
                current = current.getRightChild();

            }
        }

        return null;
    }


}
