package lintcode;

/**
 * A linked list is given such that each node contains an additional random
 * pointer which could point to any node in the list or null.
 * 
 * Return a deep copy of the list.
 */
public class CopyListWithRandomPointer {
	public RandomListNode copyRandomList(RandomListNode head) {
		if (head == null) {
			return null;
		}

		RandomListNode curP = head;
		while (curP != null) {
			RandomListNode copyP = new RandomListNode(curP.label);
			copyP.next = curP.next;
			curP.next = copyP;
			curP = copyP.next;
		}

		RandomListNode dupL = head.next;
		RandomListNode dupP = head.next;
		curP = head;

		while (curP != null) {
			if (curP.random != null) {
				dupP.random = curP.random.next;
			} else {
				dupP.random = null;
			}
			curP.next = dupP.next;
			curP = dupP.next;
			if (curP != null) {
				dupP.next = curP.next;
				dupP = curP.next;
			} else {
				dupP.next = null;
			}
		}

		return dupP;
	}
}
