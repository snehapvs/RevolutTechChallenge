package revolut;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.revolut.model.Account;
import com.revolut.repository.TransactionRepository;
import com.revolut.service.TransactionService;
import com.revolut.service.TransferService;

public class CompleteFlowUnitTest {

	private TransactionService transactionService;
	private TransferService transferService;

	@Before
	public void init() {
		transactionService = new TransactionService(new TransactionRepository());
		transferService = new TransferService(transactionService);
	}

	@Test
	public void completeEndToEndFunctionality() throws Exception {

		/**
		 * creating source account
		 */

		Account sourceAccount = transactionService.createAccount();
		UUID sourceAccountNumber = sourceAccount.getAccountNumber();

		/**
		 * creating target account
		 */

		Account targetAccount = transactionService.createAccount();
		UUID targetAccountNumber = targetAccount.getAccountNumber();

		/**
		 * deposit into first account
		 */

		transactionService.deposit(sourceAccountNumber, 2000.0);

		/**
		 * test transfer to account
		 */

		transferService.transferAmount(sourceAccountNumber, targetAccountNumber, 100.0);

		/**
		 * test results from check baance from source and target accounts
		 */

		assertEquals(transactionService.getBalance(sourceAccountNumber).getBalance(), new Double(1900.0));
		assertEquals(transactionService.getBalance(targetAccountNumber).getBalance(), new Double(100.0));

	}

}
