package lintcode;

public class FirstBadVersion {

	/**
	 * The code base version is an integer start from 1 to n. One day, someone
	 * committed a bad version in the code case, so it caused this version and
	 * the following versions are all failed in the unit tests. Find the first
	 * bad version.
	 * 
	 * You can call isBadVersion to help you determine which version is the
	 * first bad one. The details interface can be found in the code's
	 * annotation part.
	 * 
	 * @param n
	 * @return
	 */
//	public int findFirstBadVersion(int n) {
//		int start = 0, end = n;
//		while(start + 1 < end){
//			int mid = start + (end - start) / 2;
//			boolean isBad = SVNRepo.isBadVersion(mid);
//			if(isBad){
//				end = mid;
//			}else{
//				start = mid;
//			}
//		}
//		if(SVNRepo.isBadVersion(start)){
//			return start;
//		}else{
//			return end;
//		}
//	}
}
