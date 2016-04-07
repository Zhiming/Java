package com.ttl.util;

public class UniqueIdConstant {
	
	//Amount of ID needed
	public final static int TOTAL_AMOUNT_OF_ID = 10; 
	
	//Amount of bytes for each id
	public static final int BYTES_PER_ID = 16;
	
	//Generated amount of ID in each batch
	public static final int AMOUNT_OF_ID_PER_BATCH = 5000000;
	
	//ID generation boundary
	public static final int ID_GENERATION_BOUNDRY = 9;
	
	//sleep after generating every certain amount of IDs
	public static final int SLEEP_GAP = 3000000;
	
	/**
	 * When IDs are generating, the request ID thread needs to sleep
	 */
	public static boolean ID_GENERATION_IN_PROGRESS = true;
}
