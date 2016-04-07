package lintcode;

/**
 * For a given sorted array (ascending order) and a target number, find the
 * first index of this number in O(log n) time complexity.
 * 
 * If the target number does not exist in the array, return -1.
 * 
 * @author Athrun
 *
 */

public class FirstPositionOfTarget {
	public int binarySearch(int[] nums, int target) {
		int start = 0, end = nums.length - 1;
		while (start + 1 < end) {
			int mid = start + (end - start) - 1;
			if (nums[mid] == target) {
				end = mid;
			} else if (nums[mid] < target) {
				start = mid;
			} else {
				end = mid;
			}
		}
		int pos = -1;
		if (nums[start] == target) {
			pos = start;
		} else if (nums[end] == target) {
			pos = end;
		}

		// 看看前面有没有重复的元素
		if (pos != -1) {
			while (pos >= 1 && nums[pos - 1] == target) {
				pos--;
			}
		}
		return pos;
	}
}
