package com.karyam.audit.exception;

public class InternalServerException extends RuntimeException {

	private static final long serialVersionUID = -1365116235688534975L;

	public InternalServerException() {
	
	}

	public InternalServerException(String message) {
		super(message);
	}
}
