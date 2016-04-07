package lintcode;

/**
 * Given a binary search tree (See Definition) and a node in it, find the
 * in-order successor of that node in the BST.
 */

public class InorderSuccessorInBinarySearchTree {

	private TreeNode curP, parentP;
	public TreeNode inorderSuccessor(TreeNode root, TreeNode p) {
		if(root == null || p ==  null){
			return null;
		}else{
			curP = root;
			parentP = null;
			//搜索p
			while(curP != null && curP.val != p.val){
				if(p.val < curP.val){
					parentP = curP;
					curP = curP.left;
				}else if(p.val > curP.val){
					curP = curP.right;
				}else{
					break;
				}
			}
			//未找到p
			if(curP == null){
				return null;
			}else if(curP.right != null){
				//如果p有右子树
				//在p的右子树里面找到最小的左节点
				curP = curP.right;
				while(curP.left != null){
					curP = curP.left;
				}
				return curP;
			}else{
				//没有右子树
				//不管是左节点还是右节点，都返回父节点
				return parentP;
			}
		}
	}
	
}
