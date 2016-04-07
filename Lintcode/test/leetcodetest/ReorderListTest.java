package leetcodetest;

import static org.junit.Assert.*;
import lintcode.ListNode;
import lintcode.ReorderList;

import org.junit.Before;
import org.junit.Test;

public class ReorderListTest {
	
	ListNode data = null;
	
	@Before
	public void setUp() throws Exception {
//		ListNode node1 = new ListNode(0);
//		ListNode node2 = new ListNode(1);
//		ListNode node3 = new ListNode(2);
//		ListNode node4 = new ListNode(3);
//		ListNode node5 = new ListNode(4);
//		ListNode node6 = new ListNode(5);
//		ListNode node7 = new ListNode(6);
//		
//		node1.next = node2;
//		node2.next = node3;
//		node3.next = node4;
//		node4.next = node5;
//		node5.next = node6;
//		node6.next = node7;
//		
		ListNode node1 = new ListNode(2);
		ListNode node2 = new ListNode(-1);
		ListNode node3 = new ListNode(0);
		
		node1.next = node2;
		node2.next = node3;
		
		data = node1;
		
	}

	@Test
	public void test() {
		ReorderList rl = new ReorderList();
		rl.reorderList(data);
	}

}
