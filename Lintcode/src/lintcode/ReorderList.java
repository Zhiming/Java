package lintcode;

public class ReorderList {
	public void reorderList(ListNode head) {
		if (head != null) {
			// ���е�
			ListNode slowp = head, fastp = head;
			while (fastp != null && fastp.next != null) {
				slowp = slowp.next;
				fastp = fastp.next.next;
			}
			ListNode rightList = slowp.next;
			slowp.next = null;
			slowp = fastp = null;

			// ���ұߵ���������
			if (rightList != null) {
				ListNode tail = null;
				while(rightList != null){
					ListNode temp = rightList.next;
					rightList.next = tail;
					tail = rightList;
					rightList = temp;
				}
				rightList = tail;
			}

			// �ϲ���������
			ListNode leftList = head;
			ListNode dummy = new ListNode(0);
			ListNode leftcur = leftList, rightcur = rightList, tail = dummy;
			while (leftList != null && rightList != null) {
				tail.next = leftList;
				leftcur = leftList.next;
				leftList.next = null;
				leftList = leftcur;

				tail = tail.next;

				tail.next = rightList;
				rightcur = rightList.next;
				rightList.next = null;
				rightList = rightcur;

				tail = tail.next;
			}

			if (leftList == null) {
				tail.next = rightList;
			}

			if (rightList == null) {
				tail.next = leftList;
			}

			head = dummy.next;
		}
	}
}
