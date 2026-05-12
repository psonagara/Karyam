package com.karyam.operations.exception;

public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = 4852412666338139079L;

	public BadRequestException() {
	}
	
	public BadRequestException(String message) {
		super(message);
	}
}
