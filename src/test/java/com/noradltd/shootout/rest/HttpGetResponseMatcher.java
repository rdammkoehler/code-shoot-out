package com.noradltd.shootout.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class HttpGetResponseMatcher extends TypeSafeMatcher<HttpGet> {

	private int httpStatusCode;
	private String htmlString;

	public HttpGetResponseMatcher(int httpStatusCode, String htmlString) {
		this.httpStatusCode = httpStatusCode;
		this.htmlString = htmlString;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("does not match " + htmlString);
	}

	@Override
	protected boolean matchesSafely(HttpGet get) {
		try {
			HttpClients.createDefault().execute(get, new ResponseHandler<Void>() {

				public Void handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					assertThat(status, is(httpStatusCode));
					assertThat(EntityUtils.toString(response.getEntity()), is(htmlString + "\n"));
					return null;
				}

			});
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return true;
	}

	@Factory
	public static <T> Matcher<HttpGet> matches(int httpStatusCode, String htmlString) {
		return new HttpGetResponseMatcher(httpStatusCode, htmlString);
	}

}
