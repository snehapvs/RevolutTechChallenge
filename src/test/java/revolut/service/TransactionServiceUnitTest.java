package revolut.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.revolut.exception.BadRequestException;
import com.revolut.exception.InsufficientBalanceException;
import com.revolut.exception.InvalidAccountException;
import com.revolut.model.Account;
import com.revolut.repository.TransactionRepository;
import com.revolut.service.TransactionService;

public class TransactionServiceUnitTest {
	private TransactionService transactionService;
	private TransactionRepository repository;

	@Before
	public void init() {
		repository = mock(TransactionRepository.class);
		transactionService = new TransactionService(repository);
	}

	@Test
	public void createAccountWithoutError() {
		Account newAccount = transactionService.createAccount();
		verify(repository).createAccount(newAccount);
	}

	@Test
	public void depositWithoutError() throws BadRequestException, InvalidAccountException {
		when(repository.isValidAccount(new UUID(0, 0))).thenReturn(true);
		transactionService.deposit(new UUID(0, 0), 100.0);
		verify(repository).deposit(new UUID(0, 0), 100.0);
	}

	@Test(expected = InvalidAccountException.class)
	public void depositWithError() throws BadRequestException, InvalidAccountException {
		when(repository.isValidAccount(new UUID(0, 0))).thenReturn(false);
		transactionService.deposit(new UUID(0, 0), 100.0);
		verify(repository).deposit(new UUID(0, 0), 100.0);
	}
	
	@Test
	public void withdrawWithoutError() throws BadRequestException, InvalidAccountException {
		when(repository.isValidAccount(new UUID(0, 0))).thenReturn(true);
		when(repository.findById(new UUID(0, 0))).thenReturn(1000.0);
		transactionService.withdraw(new UUID(0, 0), 100.0);
		verify(repository).withdraw(new UUID(0, 0), 100.0);
	}

	@Test(expected = InsufficientBalanceException.class)
	public void withdrawWithError() throws BadRequestException, InsufficientBalanceException, InvalidAccountException {
		when(repository.isValidAccount(new UUID(0, 0))).thenReturn(true);
		when(repository.findById(new UUID(0, 0))).thenReturn(10.0);
		transactionService.withdraw(new UUID(0, 0), 100.0);
		verify(repository).withdraw(new UUID(0, 0), 100.0);
	}

	@Test
	public void getBalanceWithoutError() throws InvalidAccountException {
		when(repository.isValidAccount(new UUID(0, 0))).thenReturn(true);
		Account newAccount=transactionService.getBalance(new UUID(0, 0));
		verify(repository).findById(newAccount.getAccountNumber());
	}

}
