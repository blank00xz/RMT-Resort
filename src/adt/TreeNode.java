//a container class
package adt;

import entity.Guest;

public class TreeNode{

    private Guest data;
    private TreeNode leftChild;
    private TreeNode rightChild;

    //constructor
    public TreeNode(Guest data){
        this.data = data;
        this.leftChild = null;
        this.rightChild = null;
    }

    //get/set for bst
    public Guest getData(){
        return data;
    }

    public void setData(Guest data){
        this.data = data;

    }

    public TreeNode getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(TreeNode leftChild) {
        this.leftChild = leftChild;
    }

    public TreeNode getRightChild() {
        return rightChild;
    }

    public void setRightChild(TreeNode rightChild) {
        this.rightChild = rightChild;
    }

}