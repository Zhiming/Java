package lintcode;

public class SearchInABigSortedArray {
	/**
	 * Given a big sorted array with positive integers sorted by ascending
	 * order. The array is so big so that you can not get the length of the
	 * whole array directly, and you can only access the kth number by
	 * ArrayReader.get(k) (or ArrayReader->get(k) for C++). Find the first index
	 * of a target number. Your algorithm should be in O(log k), where k is the
	 * first index of the target number.
	 * 
	 * Return -1, if the number doesn't exist in the array.
	 * 
	 * @param reader
	 * @param target
	 * @return
	 */
	public int searchBigSortedArray(ArrayReader reader, int target) {
		int end = target;
		while (reader.get(end) > -1 && reader.get(end) < target) {
			end = 2 * end;
		}
		int start = 0;
		while (start + 1 < end) {
			int mid = start + (end - start) / 2;
			int midNum = reader.get(mid);
			if (midNum == target) {
				end = mid;
			} else if (target > midNum) {
				start = mid;
			} else {
				end = mid;
			}
		}
		if (reader.get(start) == target) {
			return start;
		}
		if (reader.get(end) == target) {
			return end;
		}
		return -1;
	}

	private class ArrayReader {
		public int get(int k) {
			return k;
		}
	}
}
