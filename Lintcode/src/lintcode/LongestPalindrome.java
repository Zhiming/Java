package lintcode;

import java.util.HashSet;

/**
 * Given a string S, find the longest palindromic substring in S. You may assume that 
 * the maximum length of S is 1000, and there exists one unique longest palindromic substring.
 * @author Athrun
 *
 */

public class LongestPalindrome {
	
	public static void main(String[] args) {
		LongestPalindrome lp = new LongestPalindrome();
		System.out.println(lp.longestPalindrome(""));
		System.out.println(lp.longestPalindrome("aaaaaa"));
		System.out.println(lp.longestPalindrome("aba"));
		System.out.println(lp.longestPalindrome("ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg"));
		System.out.println(lp.longestPalindrome("abacdfgdcaba"));
	}
	
	public String longestPalindrome(String s) {
		if(s.equals("") || s == null){
			return null;
		}else{
			HashSet<Character> charSet = new HashSet<Character>();
			for(int i = 0; i < s.length(); i++){
				charSet.add(s.charAt(i));
			}
			if(charSet.size() == 1){
				return s;
			}
			else{
				String maxPStr = "";
				
				for(int i = 1; i < s.length() - 1; i++){
					int leftP = i - 1;
					int rightP = i + 1;
					maxPStr = palindromeCheck(s, maxPStr, leftP, rightP);
					
					//以字母中间为分界线
					if(i + 2 < s.length()){
						leftP = i - 1;
						rightP = i + 2;
						if(s.charAt(leftP) == s.charAt(rightP)){
							maxPStr = palindromeCheck(s, maxPStr, leftP, rightP);
						}
					}
				}
				return maxPStr;
			}
		}
    }

	public String palindromeCheck(String s, String maxPStr, int leftP,
			int rightP) {
		String curStr;
		for(; leftP >= 0 && rightP < s.length(); leftP--, rightP++){
			curStr = s.substring(leftP, rightP+1);
			if(isPalindrome(curStr)){
				if(curStr.length() > maxPStr.length()){
					maxPStr = curStr;
				}
			}else{
				break;
			}
		}
		return maxPStr;
	}
	
	public boolean isPalindrome(String s){
		String rStr = new StringBuilder(s).reverse().toString();
		return s.equals(rStr);
	}
}
