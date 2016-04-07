package lintcode;

import java.util.Arrays;

/**
 * Given an array of integers, find how many pairs in the array such that 
 * their sum is bigger than a specific target number. Please return the number of pairs.
 * @author Athrun
 *
 */

public class TwoSumII {
	public int twoSum2(int[] nums, int target) {
		Arrays.sort(nums);
		int totPairs = 0;
		int left = 0;
		int right = nums.length - 1;
		while(left < right){
			if(nums[left] + nums[right] > target){
				totPairs += right - left;
				right--;
			}else{
				left++;
			}
		}
		return totPairs;
	}
	
	public static void main(String[] args) {
		int[] nums = {2, 7, 11, 15};
		int target = 24;
		System.out.println(new TwoSumII().twoSum2(nums, target));
	}
}
