package org.zzm.priorityqueue;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ProrityQueueTest{

	private int[] testArray = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
	private PriorityQueue pq;
	
	@Before
	public void init(){
		this.pq = new PriorityQueue(testArray);
	}
	
	@Test
	public void testSize() {
		assertArrayEquals(new int[]{testArray.length}, new int[]{this.pq.size()});
	}
	

}
