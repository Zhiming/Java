package lintcode;

public class FindMinimumInRotatedSortedArray {

	/**
	 * Suppose a sorted array is rotated at some pivot unknown to you
	 * beforehand.
	 * 
	 * (i.e., 0 1 2 4 5 6 7 might become 4 5 6 7 0 1 2).
	 * 
	 * Find the minimum element.
	 * 
	 * @param num
	 * @return
	 */

	public int findMin(int[] num) {
		if (num == null || num.length == 0) {
			return -1;
		}
		int avg = num[num.length - 1];
		int start = 0, end = num.length - 1;
		while (start + 1 < end) {
			int mid = start + (end - start) / 2;
			if(num[mid] < avg){
				end = mid;
			}else{
				start = mid;
			}
		}
		if(num[start] < avg){
			return num[start];
		}else{
			return num[end];
		}
	}
}
