package lintcode;

/**
 * Given a binary tree, find the maximum path sum from root.
 * The path may end at any node in the tree and contain at least one node in it.
 */

public class BinaryTreeMaximumPathSumII {

	private class TreeNode {
		public int val;
		public TreeNode left, right;

		public TreeNode(int val) {
			this.val = val;
			this.left = this.right = null;
		}
	}
	
	public int maxPathSum2(TreeNode root) {
		if(root == null){
			return 0;
		}
		int leftValue = maxPathSum2(root.left);
		int rightValue = maxPathSum2(root.right);
		if(leftValue < 0 && rightValue <0){
			return root.val;
		}else{
			return Math.max(leftValue, rightValue) + root.val;
		}
	}
	
}
