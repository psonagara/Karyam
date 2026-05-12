package com.karyam.audit.exception;

public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 760489823438892889L;

	public ResourceNotFoundException() {
	}
	
	public ResourceNotFoundException(String message) {
		super(message);
	}

}
