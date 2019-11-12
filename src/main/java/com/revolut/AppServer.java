package com.revolut;

import com.revolut.repository.TransactionRepository;
import com.revolut.service.TransactionService;
import com.revolut.service.TransferService;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AppServer {
	public static void main(String[] args) {

		Server jettyServer = new Server(8080);

		ServletContextHandler context = new ServletContextHandler(jettyServer, "/");
		ServletHolder jerseyServlet = new ServletHolder(new ServletContainer(resourceConfig()));
		context.addServlet(jerseyServlet, "/*");

		try {
			jettyServer.start();
			jettyServer.join();
		} catch (Exception ex) {
			Logger.getLogger(AppServer.class.getName()).log(Level.SEVERE, null, ex);
			jettyServer.destroy();
		}
	}

	private static ResourceConfig resourceConfig() {
		return new ResourceConfig().packages("com.revolut").register(new AbstractBinder() {
			@Override
			protected void configure() {
				TransactionService transactionService = new TransactionService(new TransactionRepository());
				
				bind(transactionService).to(TransactionService.class);
				bind(new TransferService(transactionService))
						.to(TransferService.class);
			}
		});
	}
}
