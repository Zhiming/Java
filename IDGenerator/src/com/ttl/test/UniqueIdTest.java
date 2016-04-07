package com.ttl.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.ttl.intf.IdParsable;
import com.ttl.model.IdPool;
import com.ttl.model.UniqueId;
import com.ttl.thread.IdGeneratorThread;
import com.ttl.util.ByteIdParser;
import com.ttl.util.UniqueIdConstant;

@SuppressWarnings("unused")
public class UniqueIdTest {

	private UniqueId uid;
	private IdParsable parser;
	
	@Before
	public void setup(){
		uid = new UniqueId();
		parser = new ByteIdParser();
	}
	
	@Test
	public void testGetId1(){
		assertEquals(16, uid.getId().length);
	}
	
	@Test
	public void testGetId2(){
		byte[][] byteArray = new byte[TestConstant.TEST_ARRAY_LENGTH][UniqueIdConstant.BYTES_PER_ID];
		
		for(int i = 0; i < TestConstant.TEST_ARRAY_LENGTH; i++){
			byteArray[i] = Arrays.copyOf(uid.getId(), UniqueIdConstant.BYTES_PER_ID);
		}
	
		for(int i = 0; i < byteArray.length; i++){
			for(int j = i + 1; j < byteArray.length; j++){
				assertFalse(parser.parse(byteArray[i]).equals(parser.parse(byteArray[j])));
			}
		}
	}

}
