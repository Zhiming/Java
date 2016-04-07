package com.ttl.exception;

public class NoMoreIdException extends Exception {

	private static final long serialVersionUID = 1L;

	public NoMoreIdException() {
		super("All requested IDs have been returned");
	}
}
