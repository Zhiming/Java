package org.zzm.binarytree;

import java.util.LinkedList;
import java.util.Queue;

public class BinaryTree {

	private Node root;
	private Queue<Node> queue;
	
	public BinaryTree() {
		queue = new LinkedList<Node>();
	}
	
	public Node getRoot() {
		return root;
	}
	
	public void setRoot(Node node){
		this.root = node;
	}

	public Node insert(Node root, int data) {
		if (root == null) {
			root = new Node(data);
		} else {
			if (data <= root.getData()) {
				// go to left child
				root.setLeftChild(insert(root.getLeftChild(), data));
			} else {
				// go to right child
				root.setRightChild(insert(root.getRightChild(), data));
			}
		}
		return root;
	}
	
	public Node getMin(Node root){
		while(root.getLeftChild() != null){
			root = root.getLeftChild();
		}
		return root;
	}
	
	public Node getMax(Node root){
		while(root.getRightChild() != null){
			root = root.getRightChild();
		}
		return root;
	}
	
	public int getTreeHeight(Node node){
		if(node == null){
			return -1;
		}
		int leftSubTreeHeight = getTreeHeight(node.getLeftChild());
		int rightSubTreeHeight = getTreeHeight(node.getRightChild());
		return Math.max(leftSubTreeHeight, rightSubTreeHeight) + 1;
	}
	
	public void levelOrderTravs(){
		queue.add(root);
		while(!queue.isEmpty()){
			Node node = queue.poll();
			System.out.print(node.getData() + " ");
			if(node.getLeftChild() != null){
				queue.offer(node.getLeftChild());
			}
			if(node.getRightChild() != null){
				queue.offer(node.getRightChild());
			}
		}
	}
	
	public void preorderTravs(Node root){
		if(root != null){
			System.out.print(root.getData() + " ");
			preorderTravs(root.getLeftChild());
			preorderTravs(root.getRightChild());
		}
	}
	
	public void inorderTravs(Node root){
		if(root != null){
			inorderTravs(root.getLeftChild());
			System.out.print(root.getData() + " ");
			inorderTravs(root.getRightChild());
		}
	}
	
	public void postorderTravs(Node root){
		if(root != null){
			postorderTravs(root.getLeftChild());
			postorderTravs(root.getRightChild());
			System.out.print(root.getData() + " ");
		}
	}

	//delete a node in bst
	public Node deleteNode(Node root, int data) {
		if (root != null) {
			if (data < root.getData()) {
				// 递归删除时，要传入子树的节点，如果还是传入root的话，会陷入无限递归中
				Node temp = deleteNode(root.getLeftChild(), data);
				root.setLeftChild(temp);
			} else if (data > root.getData()) {
				Node temp = deleteNode(root.getRightChild(), data);
				root.setRightChild(temp);
			} else {
				// 待删除的元素是叶节点
				if (root.getLeftChild() == null && root.getRightChild() == null) {
					return null;
				} else if (root.getLeftChild() == null) {
					// 左子树为空
					return (root.getRightChild());
				} else if (root.getRightChild() == null) {
					// 右子树为空
					return (root.getLeftChild());
				} else {
					// 这里要把右子数的根节点传进去，不然会找到左子数上
					Node minNodeInRightSubTree = getMin(root.getRightChild());
					// 把右子树最小节点的值传到待删节点，然后删除右子树的最小节点
					root.setData(minNodeInRightSubTree.getData());
					Node temp = deleteNode(root.getRightChild(), minNodeInRightSubTree.getData());
					root.setRightChild(temp);
				}
			}
		}
		return root;
	}
	
	/**
	 * 动态的设定每个节点的范围，来检查该节点是否在该取值范围内，根节点可以取任意的数值
	 * @return
	 */
	public Boolean bstValidate(Node root, int min, int max){
		if(root != null){
			if(root.getData() > min && root.getData() < max){
				boolean f1 = bstValidate(root.getLeftChild(), min, root.getData());
				boolean f2 = bstValidate(root.getRightChild(), root.getData(), max);
				return f1 && f2;
			}else {
				return false;
			}
		}
		return true;
	}
	
	public Node findNode(Node root, int data) {
		Node node = null;
		if (root != null) {
			if (root.getData() == data) {
				return root;
			} else if (data < root.getData()) {
				node = findNode(root.getLeftChild(), data);
			} else if (data > root.getData()) {
				node = findNode(root.getRightChild(), data);
			}
		}
		return node;
	}
	
	//find inorder successor node in a bst
	public Node findInorderSuccessor(Node root, int data){
		return null;
	}
	
	public static void main(String[] args) {
		BinaryTree bt = new BinaryTree();
		bt.setRoot(bt.insert(bt.getRoot(), 15));
		bt.insert(bt.getRoot(), 10);
		bt.insert(bt.getRoot(), 20);
		bt.insert(bt.getRoot(), 5);
		bt.insert(bt.getRoot(), 12);
		bt.insert(bt.getRoot(), 16);
	}
}
