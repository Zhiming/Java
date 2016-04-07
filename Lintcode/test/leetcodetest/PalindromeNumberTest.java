package leetcodetest;

import static org.junit.Assert.*;
import lintcode.PalindromeNumber;

import org.junit.Before;
import org.junit.Test;

public class PalindromeNumberTest {

	private PalindromeNumber pn = null;
	
	@Before
	public void setUp() throws Exception {
		pn = new PalindromeNumber();
	}

	@Test
	public void test1() {
		assertFalse(pn.isPalindrome(123));
		assertTrue(pn.isPalindrome(0));
		assertFalse(pn.isPalindrome(1231));
		assertTrue(pn.isPalindrome(1221));
		assertTrue(pn.isPalindrome(12321));
		assertFalse(pn.isPalindrome(-123));
		assertTrue(pn.isPalindrome(-1221));
	}

}
