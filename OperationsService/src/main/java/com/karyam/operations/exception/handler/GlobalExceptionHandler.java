package com.karyam.operations.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.karyam.operations.exception.AccessDeniedException;
import com.karyam.operations.exception.BadCredentialsException;
import com.karyam.operations.exception.BadRequestException;
import com.karyam.operations.exception.ConflictException;
import com.karyam.operations.exception.DisabledAccountException;
import com.karyam.operations.exception.InternalServerException;
import com.karyam.operations.exception.ResourceNotFoundException;
import com.karyam.operations.util.CommonUtil;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<?> handleConflictException(ConflictException exception) {
		log.error("ConflictException caught: {}", exception.getMessage(), exception);
		return CommonUtil.prepareErrorResponse(exception.getMessage(), HttpStatus.CONFLICT);
	}

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
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException exception) {
		log.error("BadCredentialsException caught: {}", exception.getMessage());
		return CommonUtil.prepareErrorResponse(exception.getMessage(), HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException exception) {
		log.error("AccessDeniedException caught: {}", exception.getMessage());
		return CommonUtil.prepareErrorResponse(exception.getMessage(), HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(DisabledAccountException.class)
	public ResponseEntity<?> handleDisabledAccountException(DisabledAccountException exception) {
		log.error("DisabledAccountException caught: {}", exception.getMessage());
		return CommonUtil.prepareErrorResponse(exception.getMessage(), HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<?> handleBadRequestException(BadRequestException exception) {
		log.error("BadRequestException caught: {}", exception.getMessage());
		return CommonUtil.prepareErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}
}
