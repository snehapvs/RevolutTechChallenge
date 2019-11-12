package com.revolut.exception;


public class BadRequestException extends RuntimeException {

	private Integer value;
	private String message;

	public BadRequestException(Integer value, String message) {
		this.value = value;
		this.message = message;
	}

	public Integer getValue() {
		return value;
	}

	public String getMessage() {
		return this.message;
	}

}
