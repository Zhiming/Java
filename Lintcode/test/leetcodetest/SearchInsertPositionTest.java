package leetcodetest;

import lintcode.SearchInsertPosition;

import org.junit.Before;
import org.junit.Test;

public class SearchInsertPositionTest {

	private int[] arrs;
	private SearchInsertPosition sip;
	private int[] testArr;
	
	@Before
	public void setUp() throws Exception {
		arrs = new int[]{1, 3, 5, 6};
		testArr = new int[]{5, 2, 7, 0};
		sip = new SearchInsertPosition();
	}

	@Test
	public void test() {
		for(int i = 0; i < testArr.length; i++){
			System.out.println(sip.searchInsert(arrs, testArr[i]));
		}
	}

}
