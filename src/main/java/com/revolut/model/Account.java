package com.revolut.model;

import java.util.UUID;

public class Account {

	UUID accountNumber;
	Double balance;

	public UUID getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(UUID accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Account(UUID accountNumber, Double balance) {
		this.accountNumber = accountNumber;
		this.balance = balance;
	}

	public Account() {

	}

}
