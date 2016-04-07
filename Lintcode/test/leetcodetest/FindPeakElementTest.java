package leetcodetest;

import lintcode.FindPeakElement;

import org.junit.Before;
import org.junit.Test;

public class FindPeakElementTest {

	private int[][] arrs = { { 1, 2, 1, 3, 4, 5, 7, 6 },
			{ 1, 2, 4, 5, 6, 7, 8, 6 } };
	private FindPeakElement fpe;

	@Before
	public void setUp() throws Exception {
		fpe = new FindPeakElement();
	}

	@Test
	public void test1() {
		for (int i = 0; i < arrs.length; i++) {
			System.out.println(fpe.findPeakOn(arrs[i]));
		}
	}

	@Test
	public void test2() {
		for (int i = 0; i < arrs.length; i++) {
			System.out.println(fpe.findPealNlogN(arrs[i]));
		}
	}
	
}
