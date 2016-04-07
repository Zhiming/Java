package com.ttl.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.ttl.exception.NoMoreIdException;
import com.ttl.model.UniqueIdArray;

public class UniqueIdArrayTest {

	private UniqueIdArray uia;
	
	@Test
	public void testHasNext1() {
		this.uia = new UniqueIdArray(new byte[10][3], 1, 1);
		assertFalse(this.uia.hasNext());
	}
	
	@Test
	public void testHasNext2() {
		this.uia = new UniqueIdArray(new byte[10][3], 0, 0);
		assertTrue(this.uia.hasNext());
	}
	
	@Test
	public void testHasNext3() {
		this.uia = new UniqueIdArray(new byte[10][3], -1, -1);
		assertTrue(this.uia.hasNext());
	}
	
	@Test
	public void testHasNext4() {
		try{
			this.uia = new UniqueIdArray(new byte[10][3], -1, 3);
			fail("should be out of bound");
		}catch(Exception e){
			assertTrue(e instanceof IndexOutOfBoundsException);
		}
	}
	
	@Test
	public void testHasNext5() {
		try{
			this.uia = new UniqueIdArray(new byte[10][3], 8, 10);
			fail("should be out of bound");
		}catch(Exception e){
			assertTrue(e instanceof IndexOutOfBoundsException);
		}
	}
	
	@Test
	public void testHasNext6() {
		this.uia = new UniqueIdArray(new byte[10][3], 1, 5);
		assertTrue(this.uia.hasNext());
	}
	
	@Test
	public void testHasNext7() {
		this.uia = new UniqueIdArray(new byte[10][3], 7, 5);
		assertTrue(this.uia.hasNext());
	}
	
	@Test
	public void testHasNext8() {
		try{
			this.uia = new UniqueIdArray(new byte[10][3], 8, -1);
			fail("should be out of bound");
		}catch(Exception e){
			assertTrue(e instanceof IndexOutOfBoundsException);
		}
	}
	
	@Test
	public void testHasNext9() {
		try{
			this.uia = new UniqueIdArray(new byte[10][3], 10, 5);
			fail("should be out of bound");
		}catch(Exception e){
			assertTrue(e instanceof IndexOutOfBoundsException);
		}
	}
	
	@Test
	public void testHasNext10() {
		try{
			this.uia = new UniqueIdArray(new byte[10][3], -2, -5);
			fail("should be out of bound");
		}catch(Exception e){
			assertTrue(e instanceof IndexOutOfBoundsException);
		}
	}
	
	@Test
	public void testHasNext11() {
		try{
			this.uia = new UniqueIdArray(new byte[10][3], 4, -5);
			fail("should be out of bound");
		}catch(Exception e){
			assertTrue(e instanceof IndexOutOfBoundsException);
		}
	}
	
	@Test
	public void testHasNext12() {
		try{
			this.uia = new UniqueIdArray(new byte[10][3], 14, 15);
			fail("should be out of bound");
		}catch(Exception e){
			assertTrue(e instanceof IndexOutOfBoundsException);
		}
	}
	
	@Test
	public void testAmountOfIds1(){
		this.uia = new UniqueIdArray(new byte[10][3], 1, 6);
		assertEquals(5, this.uia.requestedAmountOfId());
	}
	
	@Test
	public void testAmountOfIds2(){
		this.uia = new UniqueIdArray(new byte[10][3], 6, 6);
		assertEquals(0, this.uia.requestedAmountOfId());
	}
	
	@Test
	public void testAmountOfIds3(){
		this.uia = new UniqueIdArray(new byte[10][3], 7, 2);
		assertEquals(5, this.uia.requestedAmountOfId());
	}
	
	@Test
	public void testAmountOfIds4(){
		this.uia = new UniqueIdArray(new byte[10][3], 0, 0);
		assertEquals(10, this.uia.requestedAmountOfId());
	}
	
	@Test
	public void testAmountOfIds5(){
		this.uia = new UniqueIdArray(new byte[10][3], 0, 9);
		assertEquals(9, this.uia.requestedAmountOfId());
	}
	
	@Test
	public void testAmountOfIds6(){
		try{
			this.uia = new UniqueIdArray(new byte[10][3], 0, 10);
			fail("should be out of bound");
		}catch(Exception e){
			assertTrue(e instanceof IndexOutOfBoundsException);
		}
	}
	
	@Test
	public void testAmountOfIds7(){
		try{
			this.uia = new UniqueIdArray(new byte[10][3], -1, 10);
			fail("should be out of bound");
		}catch(Exception e){
			assertTrue(e instanceof IndexOutOfBoundsException);
		}
	}
	
	@Test
	public void testAmountOfIds8(){
		this.uia = new UniqueIdArray(new byte[10][3], -1, -1);
		assertEquals(10, this.uia.requestedAmountOfId());
	}
	
	@Test
	public void testGetNext1(){
		this.uia = new UniqueIdArray(new byte[10][3], 1, 1);
		try{
			this.uia.getNext();
			fail("Should throw NoMoreIdException");
		}catch(Exception e){
			assertTrue(e instanceof NoMoreIdException);
		}
	}
	
	@Test
	public void testGetNext2(){
		try{
			this.uia = new UniqueIdArray(new byte[10][3], 0, 0);
			ArrayList<byte[]> temp = new ArrayList<byte[]>();
			while (this.uia.hasNext()) {
				temp.add(this.uia.getNext());
			}
			assertEquals(10, temp.size());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetNext3() throws NoMoreIdException {

		try {
			this.uia = new UniqueIdArray(new byte[10][3], -1, -1);
			ArrayList<byte[]> temp = new ArrayList<byte[]>();
			while (this.uia.hasNext()) {
				temp.add(this.uia.getNext());
			}
			assertEquals(10, temp.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetNext4(){
		try{
			this.uia = new UniqueIdArray(new byte[10][3], -1, 3);
			ArrayList<byte[]> temp = new ArrayList<byte[]>();
			while (this.uia.hasNext()) {
				temp.add(this.uia.getNext());
			}
			fail("should be out of bound");
		}catch(Exception e){
			assertTrue(e instanceof IndexOutOfBoundsException);
		}
	}
	
	@Test
	public void testGetNext5(){
		try{
			this.uia = new UniqueIdArray(new byte[10][3], 8, 10);
			ArrayList<byte[]> temp = new ArrayList<byte[]>();
			while (this.uia.hasNext()) {
				temp.add(this.uia.getNext());
			}
			fail("should be out of bound");
		}catch(Exception e){
			assertTrue(e instanceof IndexOutOfBoundsException);
		}
	}
	
	@Test
	public void testGetNext6(){
		this.uia = new UniqueIdArray(new byte[10][3], 1, 5);
		try{
			ArrayList<byte[]> temp = new ArrayList<byte[]>();
			while (this.uia.hasNext()) {
				temp.add(this.uia.getNext());
			}
			assertEquals(4, temp.size());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetNext7(){
		this.uia = new UniqueIdArray(new byte[10][3], 7, 5);
		try{
			ArrayList<byte[]> temp = new ArrayList<byte[]>();
			while (this.uia.hasNext()) {
				temp.add(this.uia.getNext());
			}
			assertEquals(8, temp.size());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetNext8(){
		try{
			this.uia = new UniqueIdArray(new byte[10][3], 8, -1);
			ArrayList<byte[]> temp = new ArrayList<byte[]>();
			while (this.uia.hasNext()) {
				temp.add(this.uia.getNext());
			}
			fail("should be out of bound");
		}catch(Exception e){
			assertTrue(e instanceof IndexOutOfBoundsException);
		}
	}
	
	@Test
	public void testGetNext9(){
		try{
			this.uia = new UniqueIdArray(new byte[10][3], 10, 5);
			ArrayList<byte[]> temp = new ArrayList<byte[]>();
			while (this.uia.hasNext()) {
				temp.add(this.uia.getNext());
			}
			fail("should be out of bound");
		}catch(Exception e){
			assertTrue(e instanceof IndexOutOfBoundsException);
		}
	}
	
	@Test
	public void testGetNext10(){
		try{
			this.uia = new UniqueIdArray(new byte[10][3], -2, -5);
			ArrayList<byte[]> temp = new ArrayList<byte[]>();
			while (this.uia.hasNext()) {
				temp.add(this.uia.getNext());
			}
			fail("should be out of bound");
		}catch(Exception e){
			assertTrue(e instanceof IndexOutOfBoundsException);
		}
	}
	
	@Test
	public void testGetNext11(){
		try{
			this.uia = new UniqueIdArray(new byte[10][3], 4, -5);
			ArrayList<byte[]> temp = new ArrayList<byte[]>();
			while (this.uia.hasNext()) {
				temp.add(this.uia.getNext());
			}
			fail("should be out of bound");
		}catch(Exception e){
			assertTrue(e instanceof IndexOutOfBoundsException);
		}
	}
	
	@Test
	public void testGetNext12(){
		try{
			this.uia = new UniqueIdArray(new byte[10][3], 14, 15);
			ArrayList<byte[]> temp = new ArrayList<byte[]>();
			while (this.uia.hasNext()) {
				temp.add(this.uia.getNext());
			}
			fail("should be out of bound");
		}catch(Exception e){
			assertTrue(e instanceof IndexOutOfBoundsException);
		}
	}
	
	
}
