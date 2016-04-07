package lintcode;

/**
 * Given a binary tree, find the maximum path sum.
 * 
 * The path may start and end at any node in the tree.
 */
public class BinaryTreeMaximumPathSum {

	private class ResultType {
		public int singlePath;
		public int maxPath;

		public ResultType(int singlePath, int maxPath) {
			this.singlePath = singlePath;
			this.maxPath = maxPath;
		}
	}

	public int maxPathSum(TreeNode root) {
		return helper(root).maxPath;
	}

	private ResultType helper(TreeNode root) {
		ResultType rt = null;
		if (root == null) {
			rt = new ResultType(0, Integer.MIN_VALUE);
			return rt;
		}
		rt = new ResultType(0, 0);
		ResultType left = helper(root.left);
		ResultType right = helper(root.right);

		int singlePath = Math.max(left.singlePath, right.singlePath) + root.val;
		rt.singlePath = Math.max(singlePath, 0);

		int maxPath = Math.max(left.maxPath, right.maxPath);
		rt.maxPath = Math.max(maxPath, left.singlePath + right.singlePath
				+ root.val);
		return rt;
	}

}
