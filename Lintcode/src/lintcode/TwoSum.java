package lintcode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Given an array of integers, find two numbers such that they add up to a
 * specific target number.
 * 
 * The function twoSum should return indices of the two numbers such that they
 * add up to the target, where index1 must be less than index2. Please note that
 * your returned answers (both index1 and index2) are not zero-based.
 * 
 * You may assume that each input would have exactly one solution.
 * 
 * Input: numbers={2, 7, 11, 15}, target=9 Output: index1=1, index2=2
 * 
 * @author Athrun
 *
 */

public class TwoSum {
	
	public int[] twoSum(int[] nums, int target) {
		HashMap<Integer, List<Integer>> numIndex = new HashMap<>();
		for(int i = 0; i < nums.length; i++){
			List<Integer> list = numIndex.get(nums[i]);
			if(list == null){
				list = new LinkedList<Integer>();
				list.add(i+1);
			}else{
				list.add(i+1);
			}
			numIndex.put(nums[i], list);
		}
		Arrays.sort(nums);  //-5, -4, -3, -2, -1
		int leftInx = 0;
		int rightInx = nums.length - 1;
		int sum = Integer.MIN_VALUE;
		while(sum != target){
			sum = nums[leftInx] + nums[rightInx];
			if(sum < target){
				leftInx++;
			}else if(sum > target){
				rightInx--;
			}else{
				break;
			}
		}
		List<Integer> index1List = numIndex.get(nums[leftInx]);
		int index1 = index1List.get(0);
		index1List.remove(0);
		
		List<Integer> index2List = numIndex.get(nums[rightInx]);
		int index2 = index2List.get(0);
		index2List.remove(0);
		
		
		return new int[]{Math.min(index1, index2), Math.max(index1, index2)};
	}
	
	public static void main(String[] args) {
		int[] index = new TwoSum().twoSum(new int[]{-1, -2, -3, -4, -5}, -8);
		System.out.println(index[0]);
		System.out.println(index[1]);
	}
}
