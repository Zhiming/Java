package leetcodetest;

import static org.junit.Assert.*;
import lintcode.LongestCommonPrefix;

import org.junit.Before;
import org.junit.Test;

public class LongestCommonPrefixTest {

	private LongestCommonPrefix lcp = null;
	private String[] str1 = null;
	private String[] str2 = {"", "123"};
	private String[] str3 = {"123", "134"};
	private String[] str4 = {"123", "134", "14"};
	private String[] str5 = {"123", "456", "78"};
	private String[] str6 = {"c", "c"};
	
	@Before
	public void setUp() throws Exception {
		lcp = new LongestCommonPrefix();
	}

	@Test
	public void test1() {
		assertEquals("", lcp.longestCommonPrefix(str1));
	}

	@Test
	public void test2() {
		assertEquals("", lcp.longestCommonPrefix(str2));
	}
	
	@Test
	public void test3(){
		assertEquals("1", lcp.longestCommonPrefix(str3));
	}
	
	@Test
	public void test4(){
		assertEquals("1", lcp.longestCommonPrefix(str4));
	}
	
	@Test
	public void test5(){
		assertEquals("", lcp.longestCommonPrefix(str5));
	}
	
	@Test
	public void test6(){
		assertEquals("c", lcp.longestCommonPrefix(str6));
	}
	
}
