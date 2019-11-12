package com.revolut.service;

import java.util.UUID;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.revolut.exception.InsufficientBalanceException;
import com.revolut.exception.InvalidAccountException;

@Singleton
public class TransferService {

	@Inject
	TransactionService transactionService;

	private static final Logger log = Logger.getLogger(TransferService.class.getName());

	public TransferService(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	public void transferAmount(UUID sourceAccountNumber, UUID targetAccountNumber, Double amount)
			throws InsufficientBalanceException, InvalidAccountException {
		log.info("Amout withdrawn requested from account" + sourceAccountNumber);

		transactionService.withdraw(sourceAccountNumber, amount);
		//if there is anything wrong with deposit then money should be credited back
		try {
			log.info("Amount crediting into account" + targetAccountNumber);
			transactionService.deposit(targetAccountNumber, amount);
		} catch (Exception e) {
			log.info("Issue while processing transfer request to the account" + targetAccountNumber);
			transactionService.deposit(sourceAccountNumber, amount);
		}

	}

}
