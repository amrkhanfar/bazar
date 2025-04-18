package com.bazar.bazar_catalog.exception;

public class ResourceNotFoundException extends RuntimeException {
	
	public ResourceNotFoundException() {
		super();
	}
	
	public ResourceNotFoundException(String msg) {
		super(msg);
	}
}
