package com.karyam.operations.exception;

public class ConflictException extends RuntimeException {

	private static final long serialVersionUID = 7209014926614369063L;
	
	public ConflictException() {
	}
	
	public ConflictException(String message) {
		super(message);
	}
}
