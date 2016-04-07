package com.ttl.model;

import java.util.ArrayList;
import java.util.List;

import com.ttl.exception.InvalidIdSectionException;

public class IDUpdateSections {
	
	private List<int[]> needUpdatedIdSections;
	
	public IDUpdateSections() {
		needUpdatedIdSections = new ArrayList<int[]>();
	}
	
	public void addUpdateNeededIdSection(int[] section) throws InvalidIdSectionException{
		if(section.length > 2){
			throw new InvalidIdSectionException();
		}
		if(section[0] < 0 || section[1] < 0){
			throw new InvalidIdSectionException();
		}
		needUpdatedIdSections.add(section);
	}
	
	public List<int[]> getNeedUpdatedIdSections() {
		return needUpdatedIdSections;
	}
}
