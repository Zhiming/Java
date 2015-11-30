package com.ttl.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.FutureTask;

import org.junit.Before;
import org.junit.Test;

import com.ttl.exception.InvalidIdSectionException;
import com.ttl.intf.IdParsable;
import com.ttl.model.IDUpdateSections;
import com.ttl.model.UniqueId;
import com.ttl.thread.IdGeneratorThread;
import com.ttl.util.ByteIdParser;
import com.ttl.util.UniqueIdConstant;

public class IdGeneratorThreadTest {

	private byte[][] idArray;
	private byte[][] controllerArray;
	private IdParsable parser;
	
	@Before
	public void setUp() throws Exception {
		idArray = new byte[UniqueIdConstant.AMOUNT_OF_ID_PER_BATCH][UniqueIdConstant.BYTES_PER_ID];
		controllerArray = new byte[UniqueIdConstant.AMOUNT_OF_ID_PER_BATCH][UniqueIdConstant.BYTES_PER_ID];
		
		IDUpdateSections ius = new IDUpdateSections();
		ius.addUpdateNeededIdSection(new int[]{0, UniqueIdConstant.AMOUNT_OF_ID_PER_BATCH});
		
		IdGeneratorThread igt = new IdGeneratorThread(idArray, ius);
		//need to wait for creating IDs so invoke run() instead of start()
		FutureTask<byte[][]> ft = new FutureTask<byte[][]>(igt);
		new Thread(ft).start();
		idArray = ft.get();
		
		for(int i = 0; i < idArray.length; i++){
			controllerArray[i] = Arrays.copyOf(idArray[i], UniqueIdConstant.BYTES_PER_ID);
//			controllerArray[i] = idArray[i];
		}
		parser = new ByteIdParser();
	}

	
	@Test
	public void testIdArray(){
		Set<String> set = new HashSet<String>();
		for(int i = 0; i < idArray.length; i++){
			assertTrue(set.add(parser.parse(idArray[i])));
		}
	}
	
	@Test
	public void testTwoExpArrays(){
		for(int i = 0; i < idArray.length; i++){
			assertTrue(parser.parse(idArray[i]).equals(parser.parse(controllerArray[i])));
		}
	}
	
	@Test
	public void testUpdate() {
		try {
			IDUpdateSections ius = new IDUpdateSections();
			ius.addUpdateNeededIdSection(new int[]{0, UniqueIdConstant.AMOUNT_OF_ID_PER_BATCH});
			IdGeneratorThread igt = new IdGeneratorThread(idArray, ius);
			//need to wait for updating so invoke run() instead of start()
			FutureTask<byte[][]> ft = new FutureTask<byte[][]>(igt);
			new Thread(ft).start();
			idArray = ft.get();
			for(int i = 0; i < idArray.length; i++){
				assertFalse(parser.parse(idArray[i]).equals(parser.parse(controllerArray[i])));
			}
		} catch (InvalidIdSectionException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
