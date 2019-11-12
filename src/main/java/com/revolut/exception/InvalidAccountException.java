package com.revolut.exception;

public class InvalidAccountException extends Exception {

	private Integer value;
	private String message;

	public InvalidAccountException(Integer value, String message) {
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
