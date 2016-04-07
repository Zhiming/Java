package com.ttl.test;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Test;

import com.ttl.exception.InvalidIdSectionException;
import com.ttl.exception.NotValidRequestAmountException;
import com.ttl.model.IdPool;
import com.ttl.model.UniqueId;
import com.ttl.util.UniqueIdConstant;

public class EfficiencyTest {

private IdPool ip;
	
	@Before
	public void init() throws InvalidIdSectionException, InterruptedException, ExecutionException{
		this.ip = new IdPool();
	}

	@Test
	public void testEfficienty() {
		try {
			long start = System.currentTimeMillis();
			this.ip.getUids(UniqueIdConstant.AMOUNT_OF_ID_PER_BATCH * 5);
			long end = System.currentTimeMillis();
			System.out.println((end - start) / 1000);
		} catch (NotValidRequestAmountException e) {
			e.printStackTrace();
		}
	}

}
