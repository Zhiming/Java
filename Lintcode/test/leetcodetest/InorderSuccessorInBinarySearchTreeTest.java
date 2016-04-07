package leetcodetest;

import static org.junit.Assert.*;
import lintcode.InorderSuccessorInBinarySearchTree;
import lintcode.TreeNode;

import org.junit.Before;
import org.junit.Test;

public class InorderSuccessorInBinarySearchTreeTest {

	private TreeNode[] trees = new TreeNode[3];
	private int[] ans;
	private int[] myAns;
	private int[] queries;
	InorderSuccessorInBinarySearchTree isbst;
	
	@Before
	public void setUp() throws Exception {
		isbst = new InorderSuccessorInBinarySearchTree();

		TreeNode tree1 = new TreeNode(1);
		tree1.right = new TreeNode(2);
		
		TreeNode tree2 = new TreeNode(2);
		tree2.left = new TreeNode(1);
		
		TreeNode tree3 = new TreeNode(0);
		
		trees[0] = tree1;
		trees[1] = tree2;
		trees[2] = tree3;
		
		queries = new int[]{1, 1, 0};
		
		ans = new int[]{2, 2, 3};
		
		myAns = new int[trees.length];
	}

	@Test
	public void inorderSuccessor() {
		for(int i = 0; i < trees.length; i++){
			myAns[i] = (isbst.inorderSuccessor(trees[i], new TreeNode(queries[i]))).val;
		}
		assertArrayEquals(ans, myAns);
	}

}
