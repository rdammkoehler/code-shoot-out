package com.noradltd.shootout.rest;

import static com.noradltd.shootout.rest.HttpGetResponseMatcher.matches;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.junit.Test;

public class JettyManager {

	private static final String H1_HELLO_WORLD_H1 = "<h1>Hello World</h1>";
	private static final int SERVER_PORT = 9999;
	private Server jettyServer;

	public Server startJetty(HelloHandler handler) {
		jettyServer = new Server(SERVER_PORT);
		jettyServer.setHandler(handler);
		try {
			jettyServer.start();
		} catch (Exception e) {
			jettyServer = null;
			throw new RuntimeException(e);
		}
		return jettyServer;
	}

	public void stopJetty() {
		try {
			jettyServer.stop();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			jettyServer = null;
		}
	}

	class HelloHandler extends AbstractHandler {

		@Override
		public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException {
			response.setContentType("text/html;charset=utf-8");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(H1_HELLO_WORLD_H1);
			baseRequest.setHandled(true);
		}

	}

	@Test
	public void startStopJettyTest() throws ClientProtocolException, IOException {
		startJetty(new HelloHandler());
		String urlString = "http://localhost:" + SERVER_PORT + "/";
		try {
			assertThat(new HttpGet(urlString), matches(HttpServletResponse.SC_OK, H1_HELLO_WORLD_H1));
		} finally {
			stopJetty();
		}
	}

}
