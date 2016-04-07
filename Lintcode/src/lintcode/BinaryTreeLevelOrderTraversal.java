package lintcode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Given a binary tree, return the level order traversal of its nodes' values.
 * (ie, from left to right, level by level).
 */
public class BinaryTreeLevelOrderTraversal {

	public ArrayList<ArrayList<Integer>> levelOrder(TreeNode root) {
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
		if(root != null){
			Queue<TreeNode> queue =  new LinkedList<TreeNode>();
			queue.add(root);
			while(queue.size() != 0){
				ArrayList<Integer> list = new ArrayList<Integer>();
				int size = queue.size();
				for(int i = 0; i < size; i++){
					TreeNode node = queue.poll();
					list.add(node.val);
					if(node.left != null){
						queue.add(node.left);
					}
					if(node.right != null){
						queue.add(node.right);
					}
				}
				result.add(list);
			}
		}
		return result;
	}

}
