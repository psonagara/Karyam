package com.karyam.audit.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.karyam.audit.exception.InternalServerException;
import com.karyam.audit.exception.ResourceNotFoundException;
import com.karyam.audit.util.CommonUtil;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	
	@ExceptionHandler(InternalServerException.class)
	public ResponseEntity<?> handleInternalServerException(InternalServerException exception) {
		log.error("InternalServerException caught: {}", exception.getMessage());
		return CommonUtil.prepareErrorResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException exception) {
		log.error("ResourceNotFoundException caught: {}", exception.getMessage());
		return CommonUtil.prepareErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
	}
}
