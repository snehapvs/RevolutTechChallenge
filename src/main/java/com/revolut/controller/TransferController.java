package com.revolut.controller;

import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.http.HttpStatus;

import com.revolut.exception.BadRequestException;
import com.revolut.exception.InsufficientBalanceException;
import com.revolut.exception.InvalidAccountException;
import com.revolut.request.TransferRequest;
import com.revolut.response.ErrorResponse;
import com.revolut.service.TransferService;

@Path("/api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TransferController {

	@Inject
	TransferService service;

	@POST
	@Path("/transfer")
	public Response transfer(TransferRequest transRequest) {

		try {
			service.transferAmount(UUID.fromString(transRequest.getSourceAccount()),
					UUID.fromString(transRequest.getTargetAccount()), transRequest.getAmount());
			return Response.status(Response.Status.OK).build();
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

}
