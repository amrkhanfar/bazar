package com.bazar.bazar_catalog.exception;

@SuppressWarnings("serial")
public class ResourceNotFoundException extends RuntimeException {
	
	public ResourceNotFoundException() {
		super();
	}
	
	public ResourceNotFoundException(String msg) {
		super(msg);
	}
}
