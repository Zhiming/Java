package lintcode;

/**
 * Given a binary tree, determine if it is a valid binary search tree (BST).
 * 
 * Assume a BST is defined as follows:
 * 
 * The left subtree of a node contains only nodes with keys less than the node's
 * key. The right subtree of a node contains only nodes with keys greater than
 * the node's key. Both the left and right subtrees must also be binary search
 * trees. A single node tree is a BST
 *
 */
public class ValidateBinarySearchTree {

	public boolean isValidBST(TreeNode root) {
		if (root == null) {
			return true;
		}
		return isValidBST(root, Long.MIN_VALUE,Long.MAX_VALUE);
	}

	private boolean isValidBST(TreeNode root, long min, long max) {
		if(root.val > min && root.val < max){
			boolean valLeft = true, valRight = true;
			if(root.left != null){
				valLeft = isValidBST(root.left, min, root.val);
			}
			if(root.right != null){
				valRight = isValidBST(root.right, root.val, max);
			}
			return valLeft && valRight;
		}else{
			return false;
		}
	}
}
