package lintcode;

import java.util.ArrayList;
import java.util.List;

/**
 * Design an iterator over a binary search tree with the following rules:
 * 
 * Elements are visited in ascending order (i.e. an in-order traversal) next()
 * and hasNext() queries run in O(1) time in average.
 */

public class BSTIterator {

	private List<TreeNode> list;
	private int curPos = -1;
	// @param root: The root of binary tree.
	public BSTIterator(TreeNode root) {
		init(root);
	}

	// @return: True if there has next node, or false
	public boolean hasNext() {
		if((curPos + 1) < list.size()){
			return true;
		}else{
			return false;
		}
	}

	// @return: return next node
	public TreeNode next() {
		if(hasNext()){
			return list.get(++curPos);
		}else{
			return null;
		}
	}

	private void init(TreeNode root) {
		list = new ArrayList<TreeNode>();
		inorderTraverse(root);
	}

	private void inorderTraverse(TreeNode root) {
		if(root != null){
			inorderTraverse(root.left);
			list.add(root);
			inorderTraverse(root.right);
		}
	}
}
