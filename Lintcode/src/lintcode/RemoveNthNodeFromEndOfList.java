package lintcode;

/*
 * Given a linked list, remove the nth node from the end of list and return its head.
 */
public class RemoveNthNodeFromEndOfList {
	ListNode removeNthFromEnd(ListNode head, int n) {
		if (head == null || n < 0) {
			return null;
		}
		ListNode dummy = new ListNode(Integer.MIN_VALUE);
		ListNode preDelete = dummy;
		dummy.next = head;
		
		for(int i = 0; i < n; i++){
			head = head.next;
		}
		
		while(head != null){
			head = head.next;
			preDelete = preDelete.next;
		}
		
		preDelete.next = preDelete.next.next;
		return dummy.next;
	}
}
