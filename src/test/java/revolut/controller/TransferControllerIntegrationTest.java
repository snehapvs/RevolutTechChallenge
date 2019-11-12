package revolut.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.UUID;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revolut.model.Account;
import com.revolut.repository.TransactionRepository;
import com.revolut.request.Transaction;
import com.revolut.service.TransactionService;
import com.revolut.service.TransferService;

public class TransferControllerIntegrationTest extends JerseyTest {

	@Override
	protected Application configure() {
		return new ResourceConfig().packages("com.revolut").register(new AbstractBinder() {
			@Override
			protected void configure() {
				TransactionService transactionService = new TransactionService(new TransactionRepository());

				bind(transactionService).to(TransactionService.class);
				bind(new TransferService(transactionService)).to(TransferService.class);
			}
		});
	}

	@Test
	public void createAccountAndTestDepositandWithdrawfromBalance()
			throws JsonParseException, JsonMappingException, IOException {
		
		/**
		 * creating source account
		 */

		Response response1 = target("/api/account/create").request().post(Entity.json(""));
		Account sourceAccount = new ObjectMapper().convertValue(response1.readEntity(Object.class), Account.class);

		/**
		 * creating target account
		 */

		Response response2 = target("/api/account/create").request().post(Entity.json(""));
		Account targetAccount = new ObjectMapper().convertValue(response2.readEntity(Object.class), Account.class);

		/**
		 * deposit into first account
		 */

		String depositTransaction = "{\"accountNumber\":\"" + sourceAccount.getAccountNumber()
				+ "\",\"amount\":\"1000\"}";
		Response depositResponse = target("/api/account/deposit").request().post(Entity.json(depositTransaction));

		assertEquals("Http Response for amount deposit in account should be 200: ", Status.OK.getStatusCode(),
				depositResponse.getStatus());

		/**
		 * test transfer to account
		 */

		String transferRequest = "{\"sourceAccount\":\"" + sourceAccount.getAccountNumber() + "\",\"targetAccount\":\""
				+ targetAccount.getAccountNumber() + "\",\"amount\":\"100\"}";
		Response transferResponse = target("/api/transfer").request().post(Entity.json(transferRequest));

		assertEquals("Http Response for transfer amount should be 200: ", Status.OK.getStatusCode(),
				transferResponse.getStatus());

		/**
		 * Check balance after transfer in source account
		 */

		Response getBalanceResponseSource = target("/api/account/balance/" + sourceAccount.getAccountNumber()).request()
				.get();

		Account testAccountDetailsSource = new ObjectMapper()
				.convertValue(getBalanceResponseSource.readEntity(Object.class), Account.class);

		assertEquals("Balance after withdraw from source should be 900: ", new Double(900.0),
				testAccountDetailsSource.getBalance());

		/**
		 * Check balance after transfer in target account
		 */

		Response getBalanceResponsetarget = target("/api/account/balance/" + targetAccount.getAccountNumber()).request()
				.get();

		Account testAccountDetailsTarget = new ObjectMapper()
				.convertValue(getBalanceResponsetarget.readEntity(Object.class), Account.class);

		assertEquals("Balance after withdraw from target should be 100: ", new Double(100.0),
				testAccountDetailsTarget.getBalance());

	}

}
