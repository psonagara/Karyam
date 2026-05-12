package com.karyam.operations.exception;

public class AccessDeniedException extends RuntimeException {

	private static final long serialVersionUID = -3936603449421475935L;

	public AccessDeniedException() {
	}
	
	public AccessDeniedException(String message) {
		super(message);
	}
}
