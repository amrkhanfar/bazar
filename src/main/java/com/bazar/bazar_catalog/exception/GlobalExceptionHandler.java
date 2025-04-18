package com.bazar.bazar_catalog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<CustomErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
		
		CustomErrorResponse error = new CustomErrorResponse();
		error.setStatus(HttpStatus.NOT_FOUND.value());
		error.setMessage(ex.getMessage());
		error.setTimestamp(System.currentTimeMillis());
		
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	
	private class CustomErrorResponse {
		private int status;
		private String message;
		private long timestamp;
		@SuppressWarnings("unused")
		public int getStatus() {
			return status;
		}
		public void setStatus(int status) {
			this.status = status;
		}
		@SuppressWarnings("unused")
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		@SuppressWarnings("unused")
		public long getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}
	}
}
