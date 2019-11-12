package com.revolut.controller;

import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.http.HttpStatus;

import com.revolut.exception.BadRequestException;
import com.revolut.exception.InsufficientBalanceException;
import com.revolut.exception.InvalidAccountException;
import com.revolut.request.Transaction;
import com.revolut.response.ErrorResponse;
import com.revolut.service.TransactionService;

@Path("/api/account")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TransactionController {

	@Inject
	TransactionService service;

	@POST
	@Path("/deposit")
	public Response deposit(Transaction transaction) {
		try {
			return Response.status(Response.Status.OK)
					.entity(service.deposit(UUID.fromString(transaction.getAccountNumber()), transaction.getAmount()))
					.build();
		} catch (BadRequestException e) {
			return Response.status(e.getValue()).entity(new ErrorResponse(e.getValue(), "Bad Request", e.getMessage()))
					.build();
		} catch (InvalidAccountException e) {
			return Response.status(e.getValue())
					.entity(new ErrorResponse(e.getValue(), "Invalid Account", e.getMessage())).build();
		} catch (Exception e) {
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(new ErrorResponse(500,
					"Internal Server Error.", "Something went wrong while processing the request")).build();
		}
	}

	@POST
	@Path("/withdraw")
	public Response withdraw(Transaction transaction) {
		try {
			return Response.status(Response.Status.OK)
					.entity(service.withdraw(UUID.fromString(transaction.getAccountNumber()), transaction.getAmount()))
					.build();
		} catch (InsufficientBalanceException e) {
			return Response.status(e.getValue())
					.entity(new ErrorResponse(e.getValue(), "Insufficient Balance", e.getMessage())).build();
		} catch (BadRequestException e) {
			return Response.status(e.getValue()).entity(new ErrorResponse(e.getValue(), "Bad Request", e.getMessage()))
					.build();
		} catch (InvalidAccountException e) {
			return Response.status(e.getValue())
					.entity(new ErrorResponse(e.getValue(), "Invalid Account", e.getMessage())).build();
		} catch (Exception e) {
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(new ErrorResponse(500,
					"Internal Server Error.", "Something went Wrong While processing the request")).build();
		}

	}

	@POST
	@Path("/create")
	public Response createAccount() {
		try {
			return Response.status(Response.Status.OK).entity(service.createAccount()).build();
		} catch (Exception e) {
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(new ErrorResponse(500,
					"Internal Server Error.", "Something went Wrong While processing the request")).build();
		}
	}

	@GET
	@Path("/balance/{accountNumber}")
	public Response balance(@PathParam(value = "accountNumber") String accountNumber) {
		try {
			return Response.status(Response.Status.OK).entity(service.getBalance(UUID.fromString(accountNumber)))
					.build();
		} catch (InvalidAccountException e) {
			return Response.status(e.getValue())
					.entity(new ErrorResponse(e.getValue(), "Invalid Account", e.getMessage())).build();
		} catch (Exception e) {
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(new ErrorResponse(500,
					"Internal Server Error.", "Something went Wrong While processing the request")).build();
		}
	}

}
