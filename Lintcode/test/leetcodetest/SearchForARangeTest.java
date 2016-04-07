package leetcodetest;

import lintcode.SearchForARange;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SearchForARangeTest {

	private int[][] arrs = { { 5, 7, 7, 8, 8, 10 }, { 1 } };
	private int[][] corAns = { { 3, 4 }, { 0, 0 } };
	private int[] targets = {8, 1};
	private SearchForARange sfr;

	@Before
	public void setUp() throws Exception {
		sfr = new SearchForARange();
	}

	@Test
	public void test() {
		int[][] ans = new int[arrs.length][2];
		for (int i = 0; i < arrs.length; i++) {
			ans[i] = sfr.searchRange(arrs[i], targets[i]);
		}
		for(int i = 0; i < corAns.length; i++){
			Assert.assertArrayEquals(corAns[i], ans[i]);
		}
	}

}
