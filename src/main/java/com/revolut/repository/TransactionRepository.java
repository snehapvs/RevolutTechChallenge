package com.revolut.repository;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Singleton;

import com.revolut.exception.InsufficientBalanceException;
import com.revolut.model.Account;

@Singleton
public class TransactionRepository {

	public static ConcurrentHashMap<UUID, Double> accounts = new ConcurrentHashMap<UUID, Double>();

	public void createAccount(Account account) {
		accounts.put(account.getAccountNumber(), account.getBalance());

	}

	public Double findById(UUID accountNumber) {
		return accounts.get(accountNumber);

	}

	public boolean isValidAccount(UUID accountNumber) {
		if (accounts.containsKey(accountNumber))
			return true;
		return false;
	}

	public Account withdraw(UUID accountNumber, Double debitAmount) throws InsufficientBalanceException {
		Double currentBalance = accounts.get(accountNumber);
		Double updatedBalance = currentBalance - debitAmount;
		accounts.put(accountNumber, updatedBalance);
		return new Account(accountNumber, updatedBalance);

	}

	public Account deposit(UUID accountNumber, Double creditAmount) {
		Double curretBalance = accounts.get(accountNumber);
		Double updatedBalance = curretBalance + creditAmount;
		accounts.put(accountNumber, updatedBalance);
		return new Account(accountNumber, updatedBalance);
	}

}
