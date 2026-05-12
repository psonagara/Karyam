package com.karyam.operations.exception;

public class BaseException extends RuntimeException {

	private static final long serialVersionUID = -4805796177442274966L;

	public BaseException() {
	}
	
	public BaseException(String message) {
		super(message);
	}
}
