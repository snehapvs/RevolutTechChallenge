package com.revolut.service;

import java.util.UUID;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.jetty.http.HttpStatus;

import com.revolut.exception.BadRequestException;
import com.revolut.exception.InsufficientBalanceException;
import com.revolut.exception.InvalidAccountException;
import com.revolut.model.Account;
import com.revolut.repository.TransactionRepository;

@Singleton
public class TransactionService {

	@Inject
	TransactionRepository repository;

	public TransactionService(TransactionRepository repository) {
		this.repository = repository;
	}

	private static final Logger log = Logger.getLogger(TransactionService.class.getName());

	public Account deposit(UUID accountNumber, Double creditAmount)
			throws BadRequestException, InvalidAccountException {
		isAmountPositive(creditAmount);
		isValidAccount(accountNumber);
		log.info("Depositing into account" + accountNumber);
		return repository.deposit(accountNumber, creditAmount);

	}

	public Account withdraw(UUID accountNumber, Double debitAmount)
			throws InvalidAccountException, InsufficientBalanceException {

		isAmountPositive(debitAmount);
		isValidAccount(accountNumber);

		Double currentBalance = repository.findById(accountNumber);
		if (currentBalance >= debitAmount) {
			log.info("Withdrawing amount from " + accountNumber);
			return repository.withdraw(accountNumber, debitAmount);
		} else {
			log.info("Insufficinet balance in account" + accountNumber);
			throw new InsufficientBalanceException(HttpStatus.BAD_REQUEST_400,
					"There is no sufficient Balance in the account");
		}

	}

	public Account createAccount() {
		Account account = new Account(UUID.randomUUID(), 0.0);
		repository.createAccount(account);
		log.info("Created account with account number" + account.getAccountNumber());
		return account;
	}

	public Account getBalance(UUID accountNumber) throws InvalidAccountException {
		isValidAccount(accountNumber);
		Account account = new Account(accountNumber, repository.findById(accountNumber));
		log.info("Balance with account number" + account.getAccountNumber());
		return account;

	}

	public void isAmountPositive(Double amount) throws BadRequestException {
		if (!(amount > 0)) {
			log.info("Amount given " + amount + " is negative");
			throw new BadRequestException(HttpStatus.BAD_REQUEST_400, "Given Amount should be positive");
		}

	}

	public void isValidAccount(UUID accountNumber) throws InvalidAccountException {
		if (!repository.isValidAccount(accountNumber)) {
			log.info("Invalid account number" + accountNumber);
			throw new InvalidAccountException(HttpStatus.BAD_REQUEST_400, "Given Account details are not valid.");
		}
	}
}
