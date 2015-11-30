package com.ttl.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Test;

import com.ttl.exception.InvalidIdSectionException;
import com.ttl.exception.NoMoreIdException;
import com.ttl.exception.NotValidRequestAmountException;
import com.ttl.intf.IdParsable;
import com.ttl.model.IDUpdateSections;
import com.ttl.model.IdPool;
import com.ttl.model.UniqueId;
import com.ttl.model.UniqueIdArray;
import com.ttl.util.ByteIdParser;
import com.ttl.util.UniqueIdConstant;

@SuppressWarnings("unused")
public class IdPoolTest {

	private IdPool ip;
	private IdParsable parser;
	
	@Before
	public void init() throws InvalidIdSectionException, InterruptedException, ExecutionException{
		this.ip = new IdPool();
		parser = new ByteIdParser();
	}
	
	@Test
	public void testretrieveStartNEndIndex1(){
		try{
			this.ip.retrieveStartNEndIndex(-1, 4);
			 fail("should be out of bound");  
		}catch(Exception e){
			assertTrue(e instanceof IndexOutOfBoundsException);
		}
	}
	
	@Test
	public void testretrieveStartNEndIndex2(){
		//a section in idArray
		assertArrayEquals(new int[]{0, 4}, this.ip.retrieveStartNEndIndex(0, 4));
	}
	
	@Test
	public void testretrieveStartNEndIndex3(){
		//endIndex at the end of idArray
		assertArrayEquals(new int[]{4, 9}, this.ip.retrieveStartNEndIndex(4, 5));
	}
	
	@Test
	public void testretrieveStartNEndIndex4(){
		//the rear section is just enough, endIndex is 0
		assertArrayEquals(new int[]{4, 0}, this.ip.retrieveStartNEndIndex(4, UniqueIdConstant.AMOUNT_OF_ID_PER_BATCH - 4));
	}

	@Test
	public void testretrieveStartNEndIndex5(){
		try{
			this.ip.retrieveStartNEndIndex(4, UniqueIdConstant.AMOUNT_OF_ID_PER_BATCH + 1);
			fail("should be out of bound"); 
		}catch(Exception e){
			assertTrue(e instanceof IndexOutOfBoundsException);
		}
	}
	
	@Test
	public void testretrieveStartNEndIndex6(){
		//part of the end and part of the beginning
		assertArrayEquals(new int[] {UniqueIdConstant.AMOUNT_OF_ID_PER_BATCH - 3, 2 },
				this.ip.retrieveStartNEndIndex(UniqueIdConstant.AMOUNT_OF_ID_PER_BATCH - 3, 5));
	}
	
	/**
	 * check amount
	 */
	@Test
	public void testPackRequestedIds1(){
		UniqueIdArray uia = new UniqueIdArray(new byte[UniqueIdConstant.AMOUNT_OF_ID_PER_BATCH][UniqueIdConstant.BYTES_PER_ID], 0, 0);
		byte[][] returnedArray;
		returnedArray = this.ip.packRequestedIds(uia);
		assertEquals(UniqueIdConstant.AMOUNT_OF_ID_PER_BATCH, returnedArray.length);
	}
	
	/**
	 * check amount
	 */
	@Test
	public void testPackRequestedIds2(){
		UniqueIdArray uia = new UniqueIdArray(new byte[UniqueIdConstant.AMOUNT_OF_ID_PER_BATCH][UniqueIdConstant.BYTES_PER_ID], -1, -1);
		byte[][] returnedArray;
		returnedArray = this.ip.packRequestedIds(uia);
		assertEquals(UniqueIdConstant.AMOUNT_OF_ID_PER_BATCH, returnedArray.length);
	}
	
	/**
	 * check amount
	 */
	@Test
	public void testPackRequestedIds3(){
		UniqueIdArray uia = new UniqueIdArray(new byte[10][3], 1, 5);
		byte[][] returnedArray;
		returnedArray = this.ip.packRequestedIds(uia);
		assertEquals(4, returnedArray.length);
	}
	
	/**
	 * check amount
	 */
	@Test
	public void testPackRequestedIds4(){
		UniqueIdArray uia = new UniqueIdArray(new byte[10][3], 7, 5);
		byte[][] returnedArray;
		returnedArray = this.ip.packRequestedIds(uia);
		assertEquals(8, returnedArray.length);
	}
	
	@Test
	public void testGetUids1(){
		try{
			this.ip.getUids(-1);
			fail("Invalid input");
		}catch(Exception e){
			assertTrue(e instanceof NotValidRequestAmountException);
		}
	}
	
	@Test
	public void testGetUids2(){
		try {
			assertNull(this.ip.getUids(0));
		} catch (NotValidRequestAmountException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Request amount is less than UniqueIdConstant.AMOUNT_OF_ID_PER_BATCH
	 */
	@Test
	public void testGetUids3(){
		byte[][] ids = null;
		try {
			ids = this.ip.getUids(5);
			assertTrue(5 == ids.length);
		} catch (NotValidRequestAmountException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Request amount equals to UniqueIdConstant.AMOUNT_OF_ID_PER_BATCH
	 */
	@Test
	public void testGetUids4(){
		byte[][] ids = null;
		try {
			ids = this.ip.getUids(UniqueIdConstant.AMOUNT_OF_ID_PER_BATCH);
			assertTrue(UniqueIdConstant.AMOUNT_OF_ID_PER_BATCH == ids.length);
		} catch (NotValidRequestAmountException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Request amount more than UniqueIdConstant.AMOUNT_OF_ID_PER_BATCH
	 */
	@Test
	public void testGetUids5(){
		byte[][] ids = null;
		try {
			ids = this.ip.getUids(56);
			assertTrue(56 == ids.length);
		} catch (NotValidRequestAmountException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Check whether there is any duplicate id
	 */
	@Test
	public void testGetUids6(){
		byte[][] ids = null;
		try {
			ids = this.ip.getUids(UniqueIdConstant.AMOUNT_OF_ID_PER_BATCH * 5);
			UniqueId uid = new UniqueId();
			Set<String> set = new HashSet<String>();
			for(int i = 0; i < ids.length; i++){
				String id = parser.parse(ids[i]);
				assertTrue(set.add(id));
			}
		} catch (NotValidRequestAmountException e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testFetchNeedUpdateSection1(){
		try{
			IDUpdateSections ius = this.ip.fetchNeedUpdateSection(-1);
			fail("fail");
		}catch(Exception e){
			assertTrue(e instanceof InvalidIdSectionException);
		}
	}
	
	@Test
	public void testFetchNeedUpdateSection2(){
		try{
			IDUpdateSections ius = this.ip.fetchNeedUpdateSection(0);
			fail("fail");
		}catch(Exception e){
			assertTrue(e instanceof InvalidIdSectionException);
		}
	}
	
	@Test
	public void testFetchNeedUpdateSection3(){
		try{
			IDUpdateSections ius = this.ip.fetchNeedUpdateSection(UniqueIdConstant.AMOUNT_OF_ID_PER_BATCH);
			assertArrayEquals(new int[] { 0, UniqueIdConstant.AMOUNT_OF_ID_PER_BATCH}, 
					ius.getNeedUpdatedIdSections().get(0));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testFetchNeedUpdateSection4(){
		try{
			IDUpdateSections ius = this.ip.fetchNeedUpdateSection(UniqueIdConstant.AMOUNT_OF_ID_PER_BATCH + 1);
		}catch(Exception e){
			assertTrue(e instanceof InvalidIdSectionException);
		}
	}
	
	@Test
	public void testFetchNeedUpdateSection5(){
		try{
			IDUpdateSections ius = this.ip.fetchNeedUpdateSection(6);
			assertArrayEquals(new int[]{0, 6}, ius.getNeedUpdatedIdSections().get(0));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGenerateId1(){
		try{
			IDUpdateSections ius = this.ip.fetchNeedUpdateSection(-1);
			this.ip.generateID(ius);
			fail("invalid section");
		}catch(Exception e){
			assertTrue(e instanceof InvalidIdSectionException);
		}
	}
	
	@Test
	public void testGenerateId2(){
		try{
			IDUpdateSections ius = this.ip.fetchNeedUpdateSection(0);
			this.ip.generateID(ius);
			fail("invalid section");
		}catch(Exception e){
			assertTrue(e instanceof InvalidIdSectionException);
		}
	}
	
	/*
	 * make sure that the controller id is the same as the original one
	 */
	@Test
	public void testGenerateId3Premise1(){
		try{
			IDUpdateSections ius = this.ip.fetchNeedUpdateSection(1);
			byte[] controller = Arrays.copyOf(this.ip.getIdArray()[0], UniqueIdConstant.BYTES_PER_ID);
			UniqueId uid = new UniqueId();
			assertTrue(parser.parse(controller).equals(parser.parse(this.ip.getIdArray()[0])));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * New idArray and old idArray is differnt
	 */
	@Test
	public void testGenerateId3_1(){
		try{
			byte[][] oldIdArray = this.ip.getIdArray();
			IDUpdateSections ius = this.ip.fetchNeedUpdateSection(1);
			UniqueId uid = new UniqueId();
			this.ip.generateID(ius);
			byte[][] newIdArray = this.ip.getIdArray();
			assertTrue(oldIdArray != newIdArray);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * New generated ID should be different with the old one
	 */
	@Test
	public void testGenerateId3_2(){
		try{
			byte[] controller = Arrays.copyOf(this.ip.getIdArray()[0], UniqueIdConstant.BYTES_PER_ID);
			IDUpdateSections ius = this.ip.fetchNeedUpdateSection(1);
			UniqueId uid = new UniqueId();
			this.ip.generateID(ius);
			assertFalse(parser.parse(controller).equals(parser.parse(this.ip.getIdArray()[0])));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Rest IDs keep the same besides new generated IDs
	 */
	@Test
	public void testGenerateId3_3(){
		try{
			byte[][] controller = new byte[UniqueIdConstant.AMOUNT_OF_ID_PER_BATCH - 1][UniqueIdConstant.BYTES_PER_ID];
			for(int i = 1; i < this.ip.getIdArray().length; i++){
				controller[i - 1] = Arrays.copyOf(this.ip.getIdArray()[i], UniqueIdConstant.BYTES_PER_ID);
			}
			IDUpdateSections ius = this.ip.fetchNeedUpdateSection(1);
			UniqueId uid = new UniqueId();
			this.ip.generateID(ius);
			for(int i = 1; i < this.ip.getIdArray().length; i++){
				assertTrue(parser.parse(controller[i - 1]).equals(parser.parse(this.ip.getIdArray()[i])));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGenerateId4Premise(){
		try{
			byte[][] controller = new byte[6][UniqueIdConstant.BYTES_PER_ID];
			for(int i = 0; i < 6; i++){
				controller[i] = Arrays.copyOf(this.ip.getIdArray()[i], UniqueIdConstant.BYTES_PER_ID);
			}
			UniqueId uid = new UniqueId();
			for(int i = 0; i < 6; i++){
				assertTrue(parser.parse(controller[i]).equals(parser.parse(this.ip.getIdArray()[i])));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGenerateId4(){
		try{
			byte[][] controller = new byte[6][UniqueIdConstant.BYTES_PER_ID];
			for(int i = 0; i < 6; i++){
				controller[i] = Arrays.copyOf(this.ip.getIdArray()[i], UniqueIdConstant.BYTES_PER_ID);
			}
			UniqueId uid = new UniqueId();
			IDUpdateSections ius = this.ip.fetchNeedUpdateSection(6);
			this.ip.generateID(ius);
			for(int i = 0; i < 6; i++){
				assertFalse(parser.parse(controller[i]).equals(parser.parse(this.ip.getIdArray()[i])));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGenerateId5Premise(){
		try{
			byte[][] controller = new byte[UniqueIdConstant.AMOUNT_OF_ID_PER_BATCH][UniqueIdConstant.BYTES_PER_ID];
			for(int i = 0; i < UniqueIdConstant.AMOUNT_OF_ID_PER_BATCH; i++){
				controller[i] = Arrays.copyOf(this.ip.getIdArray()[i], UniqueIdConstant.BYTES_PER_ID);
			}
			UniqueId uid = new UniqueId();
			for(int i = 0; i < UniqueIdConstant.AMOUNT_OF_ID_PER_BATCH; i++){
				assertTrue(parser.parse(controller[i]).equals(parser.parse(this.ip.getIdArray()[i])));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGenerateId5(){
		try{
			byte[][] controller = new byte[UniqueIdConstant.AMOUNT_OF_ID_PER_BATCH][UniqueIdConstant.BYTES_PER_ID];
			for(int i = 0; i < UniqueIdConstant.AMOUNT_OF_ID_PER_BATCH; i++){
				controller[i] = Arrays.copyOf(this.ip.getIdArray()[i], UniqueIdConstant.BYTES_PER_ID);
			}
			UniqueId uid = new UniqueId();
			IDUpdateSections ius = this.ip.fetchNeedUpdateSection(UniqueIdConstant.AMOUNT_OF_ID_PER_BATCH);
			this.ip.generateID(ius);
			for(int i = 0; i < UniqueIdConstant.AMOUNT_OF_ID_PER_BATCH; i++){
				assertFalse(parser.parse(controller[i]).equals(parser.parse(this.ip.getIdArray()[i])));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGenerateId6(){
		try{
			UniqueId uid = new UniqueId();
			IDUpdateSections ius = this.ip.fetchNeedUpdateSection(UniqueIdConstant.AMOUNT_OF_ID_PER_BATCH + 1);
			this.ip.generateID(ius);
			fail("exceed idArray bound");
		}catch(Exception e){
			assertTrue(e instanceof InvalidIdSectionException);
		}
	}
	
}
