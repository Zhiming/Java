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
			//����p
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
			//δ�ҵ�p
			if(curP == null){
				return null;
			}else if(curP.right != null){
				//���p��������
				//��p�������������ҵ���С����ڵ�
				curP = curP.right;
				while(curP.left != null){
					curP = curP.left;
				}
				return curP;
			}else{
				//û��������
				//��������ڵ㻹���ҽڵ㣬�����ظ��ڵ�
				return parentP;
			}
		}
	}
	
}
