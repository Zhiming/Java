package com.ttl.model;

import java.util.Arrays;

import com.ttl.exception.NoMoreIdException;
import com.ttl.intf.IdArrayWrappable;
import com.ttl.util.UniqueIdConstant;

/**
 * The class is used to retrieve requested amount of IDs
 * 
 * As the requested IDs could be huge, this wrapped class aims to avoid array
 * copy but access the UniqueId array directly
 * 
 * @author Zhiming Zhang
 *
 */
public class UniqueIdArray implements IdArrayWrappable{

	// array for id storage
	private byte[][] idArray;
	public int startIndex;
	public int endIndex;
	private int requestedNum;
	private int amountOfIdToReturn;

	public UniqueIdArray(byte[][] idArray, int startIndex, int endIndex) throws IndexOutOfBoundsException{
		this.idArray = idArray;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		if(startIndex <= 0 && endIndex <= 0 && startIndex == endIndex){
			requestedNum = idArray.length;
		}else if(startIndex < 0 || endIndex < 0){
			throw new IndexOutOfBoundsException();
		}
		if(startIndex >= idArray.length || endIndex >= idArray.length){
			throw new IndexOutOfBoundsException();
		}else if(startIndex > 0 && endIndex > 0 && startIndex < endIndex){
			requestedNum = endIndex - startIndex;
		}
		if(startIndex > 0 && endIndex > 0 && startIndex > endIndex){
			requestedNum = idArray.length + endIndex - startIndex;
		}
		if(startIndex == 0 && endIndex > startIndex){
			requestedNum = endIndex - startIndex;
		}
		amountOfIdToReturn = requestedNum;
	}

	/**
	 * Copy and return an ID
	 * @param index
	 * @return
	 */
	private byte[] getReturnedId(int index){
		return Arrays.copyOf(idArray[index], UniqueIdConstant.BYTES_PER_ID);
	}
	
	private void releaseId(int index){
		idArray[index] = null;
	}
	
	/**
	 * return next available unique id
	 * 
	 * @return
	 * @throws NoMoreIdException
	 */
	public byte[] getNext() throws NoMoreIdException {
		int returnedIdIndex = -1;
		if(startIndex == 0 && endIndex == 0){
			endIndex = 0;
			startIndex = 1;
			amountOfIdToReturn--;
			returnedIdIndex = 0;
			byte[] returnId = getReturnedId(returnedIdIndex);
			releaseId(returnedIdIndex);
			return returnId;
		}
		
		//Retrieve all IDs at once
		if (startIndex < 0 && endIndex < 0 && startIndex == endIndex) {
			endIndex = Math.abs(startIndex);
			startIndex = Math.abs(startIndex) + 1 ;
			amountOfIdToReturn--;
			returnedIdIndex = endIndex;
			byte[] returnId = getReturnedId(returnedIdIndex);
			releaseId(returnedIdIndex);
			return returnId;
		}
		
		if(startIndex >  0 && endIndex > 0 && startIndex == endIndex){
			throw new NoMoreIdException();
		}else if (startIndex < endIndex) {
			// there are two situations, startIndex smaller than endIndex or bigger than endIndex
			amountOfIdToReturn--;
			int temp = startIndex;
			startIndex++;
			returnedIdIndex = temp;
			byte[] returnId = getReturnedId(returnedIdIndex);
			releaseId(returnedIdIndex);
			return returnId;
		}else if(startIndex > endIndex){
			//If the rear section of the idArray has been used up, reset the startIndex and return the last one
			//so when getNext() is invoked next time, it will start from the beginning
			if ((startIndex + 1) == idArray.length && amountOfIdToReturn == requestedNum) {
				startIndex = 0;
				amountOfIdToReturn--;
				returnedIdIndex = idArray.length - 1;
				byte[] returnId = getReturnedId(returnedIdIndex);
				releaseId(returnedIdIndex);
				return returnId;
			}
			
			if((startIndex + 1) == idArray.length && amountOfIdToReturn > 1){
				startIndex = 0;
				amountOfIdToReturn--;
				returnedIdIndex = idArray.length - 1;
				byte[] returnId = getReturnedId(returnedIdIndex);
				releaseId(returnedIdIndex);
				return returnId;
			}
			
			// return rear section of the idArray first
			if (startIndex < idArray.length) {
				amountOfIdToReturn--;
				int temp = startIndex;
				startIndex++;
				returnedIdIndex = temp;
				byte[] returnId = getReturnedId(returnedIdIndex);
				releaseId(returnedIdIndex);
				return returnId;
			} 
		}
		throw new NoMoreIdException();
	}
	
	/**
	 * whether there are any more requested IDs
	 * @return
	 */
	public boolean hasNext(){
		//Only for first check even before invoking getNext(), calling getNext() changes the two indices
		if(startIndex <= 0 && endIndex <= 0 && requestedNum == idArray.length){
			return startIndex == endIndex;
		}else if(startIndex == idArray.length && amountOfIdToReturn == requestedNum){
			/*
				we can't put == in the following condition as need to handle situation like:
			
			 	this.uia = new UniqueIdArray(new byte[10][3], 0, 0);
				ArrayList<byte[]> temp = new ArrayList<byte[]>();
			  	while (this.uia.hasNext()) {
					temp.add(this.uia.getNext());
				}
				
				when last id was returned, startIndex == idArray.length; however, if we put == in the following
				condition, there will be a outofbound exception which is not so reasonable for this test case
			 */
			
			//handle out of bound situation at init e.g. new UniqueIdArray(new byte[10][3], 10, 5)
			throw new IndexOutOfBoundsException();
		}else if(startIndex < 0 || endIndex >= idArray.length || (startIndex > idArray.length) || endIndex < 0){
			throw new IndexOutOfBoundsException();
		}
		return ((!(startIndex == endIndex)) && amountOfIdToReturn > 0);
	}
	
	/**
	 * return the amount of available IDs
	 * @return
	 */
	public int requestedAmountOfId(){
		return requestedNum;
	}
}
