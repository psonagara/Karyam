package com.karyam.operations.exception;

public class BadCredentialsException extends RuntimeException {

	private static final long serialVersionUID = 790448620838297758L;
	
	public BadCredentialsException() {
	}
	
	public BadCredentialsException(String message) {
		super(message);
	}

}
