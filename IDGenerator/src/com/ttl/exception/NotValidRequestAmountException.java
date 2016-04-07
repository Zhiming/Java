package com.ttl.exception;

public class NotValidRequestAmountException extends Exception {
	public NotValidRequestAmountException() {
		super("The requested amount of ID is not valid");
	}
}
