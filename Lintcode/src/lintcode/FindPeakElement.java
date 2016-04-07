package lintcode;

public class FindPeakElement {
	/**
	 * There is an integer array which has the following features:
	 * 
	 * The numbers in adjacent positions are different. A[0] < A[1] &&
	 * A[A.length - 2] > A[A.length - 1]. We define a position P is a peek if:
	 * 
	 * A[P] > A[P-1] && A[P] > A[P+1] Find a peak element in this array. Return
	 * the index of the peak.
	 * 
	 * @param A
	 * @return
	 */

	public int findPeakOn(int[] A) {
		if (A == null || A.length == 0) {
			return -1;
		}
		for (int i = 1; i <= A.length - 2; i++) {
			if (A[i] > A[i - 1] && A[i] > A[i + 1]) {
				return i;
			}
		}
		return 0;
	}
	
	public int findPealNlogN(int[] A){
		if(A == null || A.length == 0){
			return -1;
		}
		int start = 0, end = A.length - 1;
		while(start + 1 < end){
			int mid = start + (end - start) / 2;
			if(mid - 1 > 0 && A[mid - 1] >= A[mid]){
				end = mid;
			}else if(mid + 1 < A.length && A[mid] <= A[mid + 1]){
				start = mid;
			}else{
				end = mid;
			}
		}
		if(A[start] > A[end]){
			return start;
		}else{
			return end;
		}
	}
}
