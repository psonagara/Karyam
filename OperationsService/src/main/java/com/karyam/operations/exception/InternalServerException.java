package com.karyam.operations.exception;

public class InternalServerException extends RuntimeException {

	private static final long serialVersionUID = 7437523837558426660L;

	public InternalServerException() {

	}

	public InternalServerException(String message) {
		super(message);
	}
}
