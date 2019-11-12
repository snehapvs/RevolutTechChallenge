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

public class TransactionControllerIntegrationTest extends JerseyTest {

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
		 * Test creating account
		 */

		Response response = target("/api/account/create").request().post(Entity.json(""));
		assertEquals("Http Response should be 200: ", Status.OK.getStatusCode(), response.getStatus());

		// save account details for further reference
		Account testAccountcreated = new ObjectMapper().convertValue(response.readEntity(Object.class), Account.class);

		/**
		 * Test depositing into account
		 */

		String depositTransaction = "{\"accountNumber\":\"" + testAccountcreated.getAccountNumber()
				+ "\",\"amount\":\"1000\"}";
		Response depositResponse = target("/api/account/deposit").request().post(Entity.json(depositTransaction));

		assertEquals("Http Response for amount deposit in account should be 200: ", Status.OK.getStatusCode(),
				depositResponse.getStatus());

		/**
		 * Test check balance
		 */

		Response getBalanceResponse = target("/api/account/balance/" + testAccountcreated.getAccountNumber()).request()
				.get();

		assertEquals("Http Response for Balance return should be 200: ", Status.OK.getStatusCode(),
				getBalanceResponse.getStatus());
		Account testAccountDetails = new ObjectMapper().convertValue(getBalanceResponse.readEntity(Object.class),
				Account.class);

		assertEquals("Balance after deposit should be 1000: ", new Double(1000.0), testAccountDetails.getBalance());

		/**
		 * Test withdraw from account
		 */

		String withdrawTransaction = "{\"accountNumber\":\"" + testAccountcreated.getAccountNumber()
				+ "\", \"amount\":\"100\"}";
		Response withDrawResponse = target("/api/account/withdraw").request().post(Entity.json(withdrawTransaction));

		assertEquals("Http Response for amount withdraw in account should be 200: ", Status.OK.getStatusCode(),
				withDrawResponse.getStatus());

		/**
		 * Test check balance after wthdraw
		 */

		Response getBalanceResponseLater = target("/api/account/balance/" + testAccountcreated.getAccountNumber())
				.request().get();

		assertEquals("Http Response for Balance return should be 200: ", Status.OK.getStatusCode(),
				getBalanceResponseLater.getStatus());
		Account testAccountDetailsLater = new ObjectMapper()
				.convertValue(getBalanceResponseLater.readEntity(Object.class), Account.class);

		assertEquals("Balance after withdraw should be 900: ", new Double(900.0), testAccountDetailsLater.getBalance());

	}

}
