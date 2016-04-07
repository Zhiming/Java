package leetcodetest;

import static org.junit.Assert.*;
import lintcode.FindMinimumInRotatedSortedArray;

import org.junit.Before;
import org.junit.Test;

public class FindMinimumInRotatedSortedArrayTest {

	private int[] arr = {2, 1};
	private  FindMinimumInRotatedSortedArray fmrsa = new FindMinimumInRotatedSortedArray();
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		System.out.println(fmrsa.findMin(arr));
	}

}
