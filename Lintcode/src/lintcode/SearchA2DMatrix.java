package lintcode;

public class SearchA2DMatrix {

	/**
	 * Write an efficient algorithm that searches for a value in an m x n
	 * matrix.
	 * 
	 * This matrix has the following properties:
	 * 
	 * Integers in each row are sorted from left to right. The first integer of
	 * each row is greater than the last integer of the previous row.
	 */

	public boolean searchMatrix(int[][] matrix, int target) {
		if(matrix == null || matrix.length == 0){
			return false;
		}
		
		int column = matrix[0].length, row = matrix.length;
		int start = 0, end = column * row - 1;
		
		while(start + 1 < end){
			int mid = start + (end - start) / 2;
			int midNum = matrix[mid / column][mid % column];
			
			if(target == midNum){
				end = mid;
			}else if(target > midNum){
				start = mid;
			}else{
				end = mid;
			}
		}
		
		if(matrix[start / column][start % column] == target){
			return true;
		}
		if(matrix[end / column][end % column] == target){
			return true;
		}
		return false;
		
	}
}
