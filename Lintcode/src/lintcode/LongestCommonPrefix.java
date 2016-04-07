package lintcode;

public class LongestCommonPrefix {
	public String longestCommonPrefix(String[] strs) {
		if (strs == null || strs.length == 0) {
			return "";
		} else if (strs.length == 1) {
			return strs[0];
		} else {
			int minLeng = Integer.MAX_VALUE;
			for (int i = 0; i < strs.length; i++) {
				if (strs[i].equals("")) {
					return "";
				}
				if (minLeng > strs[i].length()) {
					minLeng = strs[i].length();
				}
			}

			String commonPrefix = "";
			boolean longCommonPrefixFound = false;

			for (int i = 1; i < minLeng + 1; i++) {
				String testPrefix = strs[0].substring(0, i);
				for (int j = 1; j < strs.length; j++) {
					if (testPrefix.equals(strs[j].substring(0, i))) {
						if (j == strs.length - 1) {
							commonPrefix = testPrefix;
						}
						continue;
					} else {
						longCommonPrefixFound = true;
						break;
					}
				}
				if (longCommonPrefixFound) {
					break;
				}
			}
			return commonPrefix;
		}
	}
}
