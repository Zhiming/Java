package lintcode;

/**
 * Given a binary tree, determine if it is height-balanced.
 * 
 * For this problem, a height-balanced binary tree is defined as a binary tree
 * in which the depth of the two subtrees of every node never differ by more
 * than 1.
 * 
 * 先看左右子树是否是平衡的，如果是平衡的，再看左右子树的高度差是否小于或等于1
 */
public class BalancedBinaryTree {

	public boolean isBalanced(TreeNode root) {
		if (root == null) {
			return true;
		}
		boolean isLeftBal = isBalanced(root.left);
		boolean isRightBal = isBalanced(root.right);
		if(isLeftBal && isRightBal){
			return (Math.abs((maxDepth(root.left) - maxDepth(root.right))) <= 1);
		}
		return false;
	}

	
	
	private int maxDepth(TreeNode root){
		if(root == null){
			return 0;
		}
		int left = maxDepth(root.left);
		int right  = maxDepth(root.right);
		return Math.max(left, right) + 1;
	}
}
