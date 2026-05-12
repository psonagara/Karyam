package com.karyam.operations.exception;

public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 5106075698864632011L;
	
	public ResourceNotFoundException() {
	}
	
	public ResourceNotFoundException(String message) {
		super(message);
	}

}
