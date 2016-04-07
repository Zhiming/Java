package lintcode;

public class ReverseInteger {
	
	public static void main(String[] args) {
		ReverseInteger ri = new ReverseInteger();
		System.out.println(ri.reverse(1534236469));
	}

	public int reverse(int x) {
		if (x >= -9 && x <= 9) {
			return x;
		} else {
			int receiver = x;
			String xStr = Integer.toString(x);
			int amountofdigit = xStr.length();
			if(x < 0){
				amountofdigit = amountofdigit - 1;
			}
			int[] numArr = new int[amountofdigit];
			for (int i = 0; i < amountofdigit; i++) {
				int divisor = (int)Math.pow(10, (amountofdigit - 1 - i));
				numArr[i] = (int)(receiver / divisor);
				receiver = receiver - numArr[i] * divisor;
			}
			
			long returnedInt = 0;
			for(int i = 0; i < numArr.length; i++){
				int multiplier = (int)Math.pow(10, i);
				long temp = (long)numArr[i] * (long)multiplier;
				returnedInt += temp;
			}
			if(returnedInt > Integer.MAX_VALUE || returnedInt < Integer.MIN_VALUE){
				return 0;
			}else{
				return (int)returnedInt;
			}
		}
	}
}
