package leetcodetest;

import static org.junit.Assert.*;
import lintcode.CopyListWithRandomPointer;
import lintcode.RandomListNode;

import org.junit.Before;
import org.junit.Test;

public class CopyListWithRandomPointerTest {

	RandomListNode[] testcases;
	CopyListWithRandomPointer clrp;
	
	@Before
	public void setUp() throws Exception {
		RandomListNode h1 = new RandomListNode(-1);
		RandomListNode n2 = new RandomListNode(-1);
		
		h1.next = n2;
		h1.random = n2.random = null;
		
		testcases = new RandomListNode[]{h1};
		clrp = new CopyListWithRandomPointer();
	}

	@Test
	public void test() {
		for(int i = 0; i < testcases.length; i++){
			clrp.copyRandomList(testcases[i]);
		}
	}

}
