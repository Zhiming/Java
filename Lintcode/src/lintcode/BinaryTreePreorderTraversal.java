package lintcode;

import java.util.ArrayList;

public class BinaryTreePreorderTraversal {

	/**
	 * Given a binary tree, return the preorder traversal of its nodes' values.
	 */

	private class TreeNode {
		public int val;
		public TreeNode left, right;

		public TreeNode(int val) {
			this.val = val;
			this.left = this.right = null;
		}
	}

	ArrayList<Integer> preorderList = new ArrayList<Integer>();

	public ArrayList<Integer> preorderTraversal(TreeNode root) {
		if (root != null) {
			preorderList.add(root.val);
			preorderTraversal(root.left);
			preorderTraversal(root.right);
		}
		return preorderList;
	}
}
