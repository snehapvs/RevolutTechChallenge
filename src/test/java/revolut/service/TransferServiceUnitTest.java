package revolut.service;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.revolut.exception.InsufficientBalanceException;
import com.revolut.exception.InvalidAccountException;
import com.revolut.service.TransactionService;
import com.revolut.service.TransferService;

public class TransferServiceUnitTest {
	private TransactionService transactionService;
	private TransferService transferService;

	@Before
	public void init() {
		transactionService = mock(TransactionService.class);
		transferService = new TransferService(transactionService);
	}

	@Test
	public void transferAccountWithoutError() throws InsufficientBalanceException, InvalidAccountException {
		transferService.transferAmount(new UUID(0, 0), new UUID(0, 1), 100.0);
		verify(transactionService).withdraw(new UUID(0, 0), 100.0);
		verify(transactionService).deposit(new UUID(0, 1), 100.0);
	}

	@Test
	public void transferAccountWithDepositError() throws InsufficientBalanceException, InvalidAccountException {
		doThrow(new InvalidAccountException(1, "test")).when(transactionService).deposit(new UUID(0, 1), 100.0);
		transferService.transferAmount(new UUID(0, 0), new UUID(0, 1), 100.0);
		verify(transactionService).withdraw(new UUID(0, 0), 100.0);
		verify(transactionService).deposit(new UUID(0, 1), 100.0);
		verify(transactionService).deposit(new UUID(0, 0), 100.0);

	}
	
	@Test(expected = InsufficientBalanceException.class)
	public void transferAccountWithWithdrawError() throws InsufficientBalanceException, InvalidAccountException {
		doThrow(new InsufficientBalanceException(1, "test")).when(transactionService).withdraw(new UUID(0, 0), 100.0);
		transferService.transferAmount(new UUID(0, 0), new UUID(0, 1), 100.0);
		verify(transactionService).withdraw(new UUID(0, 0), 100.0);

	}

}
