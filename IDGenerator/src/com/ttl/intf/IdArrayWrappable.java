package com.ttl.intf;

import com.ttl.exception.NoMoreIdException;

public interface IdArrayWrappable {

	public byte[] getNext() throws NoMoreIdException;
	public boolean hasNext();
	public int requestedAmountOfId();
}
