package com.ttl.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.ttl.exception.InvalidIdSectionException;
import com.ttl.model.IDUpdateSections;

public class IDUpdateSectionsTest {
	
	private IDUpdateSections ips;
	
	@Before
	public void setUp() throws Exception {
		this.ips = new IDUpdateSections();
	}

	@Test
	public void testAddUpdateNeededIdSection1() {
		try{
			this.ips.addUpdateNeededIdSection(new int[]{-1, -2, -3});
			fail("fail");
		}catch(Exception e){
			assertTrue(e instanceof InvalidIdSectionException);
		}
	}
	
	@Test
	public void testAddUpdateNeededIdSection2() {
		try{
			this.ips.addUpdateNeededIdSection(new int[]{-1, -2});
			fail("fail");
		}catch(Exception e){
			assertTrue(e instanceof InvalidIdSectionException);
		}
	}

	@Test
	public void testAddUpdateNeededIdSection3() {
		try{
			this.ips.addUpdateNeededIdSection(new int[]{-1, -2});
			fail("fail");
		}catch(Exception e){
			assertTrue(e instanceof InvalidIdSectionException);
		}
	}
	
}
