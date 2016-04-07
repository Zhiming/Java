package lintcode;

public class SearchInsertPosition {

	/**
	 * Given a sorted array and a target value, return the index if the target
	 * is found. If not, return the index where it would be if it were inserted
	 * in order.
	 */

	public int searchInsert(int[] A, int target) {
		if (A.length == 0 || A == null) {
			return 0;
		}

		int leftP = 0;
		int rightP = A.length - 1;

		while (leftP + 1 < rightP) {
			int mid = leftP + (rightP - leftP) / 2;
			if (A[mid] == target) {
				rightP = mid;
			} else if (A[mid] < target) {
				leftP = mid;
			} else {
				rightP = mid;
			}
		}
		
		if (target <= A[leftP]) {
			return leftP;
		} else if (A[rightP] == target) {
			return rightP;
		} else if(target > A[rightP]){
			return rightP + 1;
		}else{
			return rightP;
		}
	}
	
}
