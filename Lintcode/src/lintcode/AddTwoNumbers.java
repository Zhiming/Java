package lintcode;

import java.util.Stack;

/**
 * You are given two linked lists representing two non-negative numbers. The
 * digits are stored in reverse order and each of their nodes contain a single
 * digit. Add the two numbers and return it as a linked list. 
 * Input: (2 -> 4 -> 3) + (5 -> 6 -> 4) 
 * Output: 7 -> 0 -> 8
 * 
 * @author Athrun
 *
 */
public class AddTwoNumbers {
	
	public static void main(String[] args) {
		ListNode l1 = new ListNode(2);
		l1.next = new ListNode(4);
		l1.next.next = new ListNode(3);
		
		ListNode l2 = new ListNode(5);
		l2.next = new ListNode(6);
		l2.next.next = new ListNode(4);
		
		ListNode l3 = new AddTwoNumbers().addTwoNumbers(l1, l2);
		System.out.println(l3);
	}
	
	public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
		Stack<Integer> s1 = new Stack<Integer>();
		Stack<Integer> s2 = new Stack<Integer>();
		
		ListNode p = l1;
		while(p != null){
			s1.add(p.val);
			if(p.next != null){
				p = p.next;
			}else{
				break;
			}
		}
		
		p = l2;
		while(p != null){
			s2.add(p.val);
			if(p.next != null){
				p = p.next;
			}else{
				break;
			}
		}
		
		StringBuffer sb = new StringBuffer();
		
		while(!s1.isEmpty()){
			sb.append(s1.pop().toString());
		}
		String str1 = sb.toString();
		long val1 = Long.valueOf(str1);
		
		sb = new StringBuffer();
		while(!s2.isEmpty()){
			sb.append(s2.pop().toString());
		}
		String str2 = sb.toString();
		long val2 = Long.valueOf(str2);
		
		long sum = val1 + val2;
		String strSum = ((Long)sum).toString();
		strSum = new StringBuilder(strSum).reverse().toString();
		
		
		ListNode ln = new ListNode(Integer.parseInt(String.valueOf(strSum.charAt(0))));
		ListNode pln = ln;
		for(int i = 1; i < strSum.length(); i++){
			pln.next = new ListNode(Integer.parseInt(String.valueOf(strSum.charAt(i))));
			pln = pln.next;
		}
		
		return ln;
	}

	public static class ListNode {
		int val;
		ListNode next;

		ListNode(int x) {
			val = x;
		}
	}
}

