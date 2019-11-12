package com.revolut.exception;


public class InsufficientBalanceException extends RuntimeException {

	private Integer value;
	private String message;

	public InsufficientBalanceException(Integer value, String message) {
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
