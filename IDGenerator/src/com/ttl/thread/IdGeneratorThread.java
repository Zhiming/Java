package com.ttl.thread;

import java.util.Arrays;
import java.util.concurrent.Callable;

import com.ttl.model.IDUpdateSections;
import com.ttl.model.UniqueId;
import com.ttl.util.UniqueIdConstant;

public class IdGeneratorThread implements Callable<byte[][]>{
	
	private byte[][] idArray;
	private IDUpdateSections ius;
	private UniqueId uid;
	
	public IdGeneratorThread(byte[][] idArray, IDUpdateSections ius) {
		this.idArray = idArray;
		this.ius = ius;
		this.uid = new UniqueId();
	}
	
	@Override
	public byte[][] call() {
		UniqueIdConstant.ID_GENERATION_IN_PROGRESS = true;
		int updateStartIndex = ius.getNeedUpdatedIdSections().get(0)[0];
		int updateEndIndex = ius.getNeedUpdatedIdSections().get(0)[1];

		//Copy out the rest IDs in idArray
		byte[][] restIds = null;
		//idArray is null when initializing IdPool
		if(idArray != null && updateEndIndex < idArray.length){
			restIds = new byte[idArray.length - updateEndIndex][UniqueIdConstant.BYTES_PER_ID];
			for(int i = updateEndIndex; i < idArray.length; i++){
				restIds[i - updateEndIndex] = Arrays.copyOf(idArray[i], UniqueIdConstant.BYTES_PER_ID);
			}
		}
		
		//Need to renew idArray
		idArray = new byte[UniqueIdConstant.AMOUNT_OF_ID_PER_BATCH][UniqueIdConstant.BYTES_PER_ID];
		int generatedIdCount = 0;
		for(int i = updateStartIndex; i < updateEndIndex; i++){
			//Need to copy the id to idArray[i] because of the way generating id
			idArray[i] = Arrays.copyOf(uid.getId(), UniqueIdConstant.BYTES_PER_ID);
			generatedIdCount++;
			if(generatedIdCount == UniqueIdConstant.SLEEP_GAP){
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				generatedIdCount = 0;
			}
		}
		
		//Copy old IDs back to idArray
		if(restIds != null){
			for(int i = updateEndIndex; i < idArray.length; i++){
				idArray[i] = Arrays.copyOf(restIds[(i - updateEndIndex)], UniqueIdConstant.BYTES_PER_ID);
			}
			//release restIds
			restIds = null;
		}
		UniqueIdConstant.ID_GENERATION_IN_PROGRESS = false;
		return idArray;
	}
}
