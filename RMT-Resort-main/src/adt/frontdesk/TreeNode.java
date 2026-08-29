// @author : Ong Wei Chen 
//a container class
package adt.frontdesk;

public class TreeNode<T>{

    private T data;
    private TreeNode<T> leftChild;
    private TreeNode<T> rightChild;

    //constructor
    public TreeNode(T data){
        this.data = data;
        this.leftChild = null;
        this.rightChild = null;
    }

    //get/set for bst
    public T getData(){
        return data;
    }

    public void setData(T data){
        this.data = data;

    }

    public TreeNode<T> getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(TreeNode<T> leftChild) {
        this.leftChild = leftChild;
    }

    public TreeNode<T> getRightChild() {
        return rightChild;
    }

    public void setRightChild(TreeNode<T> rightChild) {
        this.rightChild = rightChild;
    }

}