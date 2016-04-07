package leetcodetest;

import org.junit.Before;
import org.junit.Test;

public class FirstPositionOfTarget {

	private int[] nums = {2,6,8,13,15,17,17,18,19,20};
	private int target = 15;
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		lintcode.FirstPositionOfTarget fpt = new lintcode.FirstPositionOfTarget();
		System.out.println(fpt.binarySearch(nums, target));
	}

}
