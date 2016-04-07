package lintcode;

public class PalindromeNumber {
	
	public boolean isPalindrome(int x) {
		String num = Integer.toString(x);
		int leftP = 0;
		int rightP = num.length() - 1;
		while (leftP != rightP && leftP <= rightP) {
			if (num.charAt(leftP) == num.charAt(rightP)) {
				leftP++;
				rightP--;
				continue;
			} else {
				return false;
			}
		}
		return true;
	}
}
