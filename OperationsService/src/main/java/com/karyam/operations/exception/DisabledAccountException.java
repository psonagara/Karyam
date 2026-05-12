package com.karyam.operations.exception;

public class DisabledAccountException extends RuntimeException {

	private static final long serialVersionUID = -4459728498703927981L;

	public DisabledAccountException() {
	}
	
	public DisabledAccountException(String message) {
		super(message);
	}
}
