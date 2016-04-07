package lintcode;

public class SearchForARange {
	/**
	 * Given a sorted array of n integers, find the starting and ending position
	 * of a given target value.
	 * 
	 * If the target is not found in the array, return [-1, -1].
	 */

	public int[] searchRange(int[] A, int target) {
		if (A == null || A.length == 0) {
			return new int[] { -1, -1 };
		}
		int start = 0, end = A.length - 1;
		while (start + 1 < end) {
			int mid = start + (end - start) / 2;
			if (A[mid] > target) {
				end = mid;
			} else {
				start = mid;
			}
		}
		if (A[start] != target && A[end] != target) {
			return new int[] { -1, -1 };
		} else {
			int targetLoc = -1;
			if (A[start] == target) {
				targetLoc = start;
			} else {
				targetLoc = end;
			}
			start = end = targetLoc;
			while (start >= 1 && A[start - 1] == target) {
				start--;
			}
			while (end < A.length - 1 && A[end + 1] == target) {
				end++;
			}
			return new int[] { start, end };
		}
	}
}
