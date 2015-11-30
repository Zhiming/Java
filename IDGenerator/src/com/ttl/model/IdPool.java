package com.ttl.model;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import com.ttl.exception.InvalidIdSectionException;
import com.ttl.exception.NoMoreIdException;
import com.ttl.exception.NotValidRequestAmountException;
import com.ttl.thread.IdGeneratorThread;
import com.ttl.util.UniqueIdConstant;

public class IdPool {
	
	//Thread to generate ids
	private IdGeneratorThread igt;
	
	// array for id storage
	private byte[][] idArray;
	
	// indicate the used id range
	private int availableIdIndex;
	
	public IdPool() throws InvalidIdSectionException, InterruptedException, ExecutionException {
		IDUpdateSections ius = new IDUpdateSections();
		ius.addUpdateNeededIdSection(new int[]{0, UniqueIdConstant.AMOUNT_OF_ID_PER_BATCH});
		this.igt = new IdGeneratorThread(idArray, ius);
		FutureTask<byte[][]> ft = new FutureTask<byte[][]>(igt);
		new Thread(ft).start();
		while(UniqueIdConstant.ID_GENERATION_IN_PROGRESS){
			Thread.sleep(50);
		}
		idArray = ft.get();
		resetIdGenerationProgressFlag();
	}
	
	/**
	 * return certain amount of IDs, if the available ids are less than ID_GENERATION_BOUNDRY, this method
	 * will sleep for a while for id generation
	 * @param amount
	 * @return
	 * @throws ExceedLimitException 
	 * @throws NotValidRequestAmountException 
	 * @throws NoMoreIdException 
	 */
	public byte[][] getUids(int requestAmountOfIds) throws NotValidRequestAmountException{
		
		//Throw exception if the requested amount of ID is bigger than those that could be
		//generated in a batch
		
		if(requestAmountOfIds < 0){
			throw new NotValidRequestAmountException();
		}
		
		if(requestAmountOfIds == 0){
			return null;
		}
		
		int availableAmountOfId = idArray.length - availableIdIndex;
		byte[][] returnedIds = null;
		
		if(availableAmountOfId < requestAmountOfIds){
			//need to generate more IDs
			
			//copy the rest of IDs out first
			byte[][] restIds = copyRestIdInIdArray(availableAmountOfId);
			
			//Amount of loop needed
			int amountNeedToLoop = getLoopAmount(requestAmountOfIds,availableAmountOfId);
			
			//get restIds's length
			byte[][] trasitionIdArray = restIds;
			restIds = null;
			
			//loop to generate new IDs
			for(int i = 0; i < amountNeedToLoop; i++){
				IDUpdateSections ius = new IDUpdateSections();
				try {
					ius.addUpdateNeededIdSection(new int[]{0, UniqueIdConstant.AMOUNT_OF_ID_PER_BATCH});
					generateID(ius);
				} catch (InvalidIdSectionException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
				
				// reset availableIdIndex, put availableIdIndex in the beginning
				availableIdIndex = 0;
				
				byte[][] tempArray = trasitionIdArray;
				
				//get new trasitionIdArray
				trasitionIdArray = getNewTrasitionIdArray(requestAmountOfIds, amountNeedToLoop, trasitionIdArray, i);

				int stopIndex = -1;
				
				// ref back to tempArray instead of copying, faster and space efficiency
				stopIndex = refBackToTempArray(trasitionIdArray, tempArray, stopIndex);

				// When refBackToTempArray ends, stopIndex is not updated yet, need to add 1 more
				stopIndex++;

				prepareReturnedId(requestAmountOfIds, amountNeedToLoop, trasitionIdArray, i, tempArray, stopIndex);
				tempArray = null;

				returnedIds = trasitionIdArray;
			}
		}else{
			int[] idFetchIndices = retrieveStartNEndIndex(availableIdIndex, requestAmountOfIds);
			
			//update availableIdIndex to indicate where to start next time 
			availableIdIndex = idFetchIndices[1];
			
			UniqueIdArray uia = new UniqueIdArray(idArray, idFetchIndices[0], idFetchIndices[1]);
			returnedIds = packRequestedIds(uia);
		}
		return returnedIds;
	}

	private byte[][] getNewTrasitionIdArray(int requestAmountOfIds,
			int amountNeedToLoop, byte[][] trasitionIdArray, int i) {
		if (i < amountNeedToLoop - 1) {
			trasitionIdArray = new byte[trasitionIdArray.length + idArray.length][];
		} else {
			trasitionIdArray = new byte[requestAmountOfIds][];
		}
		return trasitionIdArray;
	}

	private void prepareReturnedId(int requestAmountOfIds,
			int amountNeedToLoop, byte[][] trasitionIdArray, int i,
			byte[][] tempArray, int stopIndex) {
		if(i < amountNeedToLoop - 1){
			//the rest of trasitionIdArray refs to new generated idArray
			for(int j = stopIndex; j < trasitionIdArray.length; j++){
				trasitionIdArray[j] = idArray[j - stopIndex];
			}
		}else{
			int dificientIds = requestAmountOfIds - tempArray.length;
			for(int j = 0; j < dificientIds; j++){
				trasitionIdArray[stopIndex++] = idArray[j];
				idArray[j] = null;
			}
		}
	}

	private int refBackToTempArray(byte[][] trasitionIdArray,
			byte[][] tempArray, int stopIndex) {
		for(int j = 0; j < tempArray.length; j++){
			trasitionIdArray[j] = tempArray[j];
			stopIndex = j;
		}
		return stopIndex;
	}

	private int getLoopAmount(int requestAmountOfIds, int availableAmountOfId) {
		double res = (requestAmountOfIds - availableAmountOfId) / ((double)UniqueIdConstant.AMOUNT_OF_ID_PER_BATCH);
		int amountNeedToLoop = (int)Math.ceil(res);
		return amountNeedToLoop;
	}

	private byte[][] copyRestIdInIdArray(int availableAmountOfId) {
		byte[][] restIds = new byte[availableAmountOfId][];
		for(int i = availableIdIndex; i < idArray.length; i++){
			restIds[i] = Arrays.copyOf(idArray[i], UniqueIdConstant.BYTES_PER_ID);
			//release id
			idArray[i] = null;
		}
		return restIds;
	}
	
	/**
	 * Calculate startIndex and endIndex of requested amount of IDs for idArray retrieval 
	 * based on availableIdIndex position
	 * the second int in the returned is where availableIdIndex is when fetching completes
	 * @param availableIdIndex
	 * @param requestAmountOfIds
	 * @return
	 */
	public int[] retrieveStartNEndIndex(int availableIdIndex, int requestAmountOfIds){
		if(availableIdIndex < 0){
			throw new IndexOutOfBoundsException();
		}
		//Speical caring for fetching all IDs at once
		if(requestAmountOfIds == idArray.length){
			//requested amount happens to be the length of idArrays, e.g. this.ip.retrieveStartNEndIndex(0, 10)
			return new int[]{-availableIdIndex, -availableIdIndex};
		}else if(requestAmountOfIds > idArray.length){
			throw new IndexOutOfBoundsException();
		}else{
			if(availableIdIndex + requestAmountOfIds < idArray.length){
				return new int[]{availableIdIndex, (availableIdIndex + requestAmountOfIds)};
			}
			else if(availableIdIndex + requestAmountOfIds == idArray.length){
				return new int[]{availableIdIndex, 0};
			}
			else{
				return new int[]{availableIdIndex, (availableIdIndex + requestAmountOfIds - idArray.length)};
			}
		}
	}

	/**
	 * fetch section indices of idArray which needs to put new IDs 
	 * @param amounf of IDs need to be updated
	 * @return
	 * @throws InvalidIdSectionException
	 */
	public IDUpdateSections fetchNeedUpdateSection(int amountOfIdNeedToUpdate) throws InvalidIdSectionException {
		if(amountOfIdNeedToUpdate > idArray.length || amountOfIdNeedToUpdate < 1){
			throw new InvalidIdSectionException();
		}
		IDUpdateSections ius = new IDUpdateSections();
		ius.addUpdateNeededIdSection(new int[]{0, amountOfIdNeedToUpdate});
		return ius;
	}

	/**
	 * Generate new ID for certain section in idArray
	 * @param ius
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public void generateID(IDUpdateSections ius) throws InterruptedException, ExecutionException {
		igt = new IdGeneratorThread(idArray, ius);
		FutureTask<byte[][]> ft = new FutureTask<byte[][]>(igt);
		new Thread(ft).start();
		while(UniqueIdConstant.ID_GENERATION_IN_PROGRESS){
			Thread.sleep(50);
		}
		idArray = ft.get();
		resetIdGenerationProgressFlag();
	}

	public byte[][] getIdArray() {
		return idArray;
	}

	/**
	 * Default progress flag should be true, make sure that the requested thread always wait for generation when 
	 * new IDs are needed to generate
	 */
	public void resetIdGenerationProgressFlag() {
		UniqueIdConstant.ID_GENERATION_IN_PROGRESS = true;
	}

	/**
	 * return an array of Ids in idArray
	 * @param uia
	 * @return
	 * @throws NoMoreIdException
	 */
	public byte[][] packRequestedIds(UniqueIdArray uia) {
		byte[][] returnedArray = new byte[uia.requestedAmountOfId()][UniqueIdConstant.BYTES_PER_ID];
		int i = 0;
		while(uia.hasNext()){
			try {
				returnedArray[i] = uia.getNext();
			} catch (NoMoreIdException e) {
				e.printStackTrace();
			}
		}
		return returnedArray;
	}
	
}
