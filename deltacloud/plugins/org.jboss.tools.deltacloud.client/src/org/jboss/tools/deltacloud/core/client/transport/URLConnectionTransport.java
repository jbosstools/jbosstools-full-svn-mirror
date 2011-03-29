package org.jboss.tools.deltacloud.core.client.transport;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;

import org.jboss.tools.deltacloud.core.client.DeltaCloudNotFoundClientException;
import org.jboss.tools.deltacloud.core.client.HttpMethod;
import org.jboss.tools.deltacloud.core.client.request.DeltaCloudRequest;

public class URLConnectionTransport extends AbstractHttpTransport {

	private static final char USERNAME_PASSWORD_DELIMITER = ':';
	private static final String PROPERTY_AUTHORIZATION = "Authorization";
	private static final String PROPERTY_ACCEPT = "Accept";
	private static final String PREFIX_BASIC_AUTHENTICATION = "Basic ";
	private static final int TIMEOUT = 10 * 1024;

	public URLConnectionTransport(String username, String password) {
		super(username, password);
	}

	@Override
	protected InputStream doRequest(DeltaCloudRequest request) throws Exception {
		try {
			URL url = request.getUrl();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setAllowUserInteraction(false);
			connection.setConnectTimeout(TIMEOUT);
			connection.setRequestProperty(PROPERTY_ACCEPT, "application/xml;q=1");
			connection.setInstanceFollowRedirects(true);
			addCredentials(connection);
			setRequestMethod(request, connection);
			BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
			throwOnHttpErrors(
					connection.getResponseCode(), connection.getResponseMessage(), request.getUrl());
			return in;
		} catch (FileNotFoundException e) {
			/*
			 * thrown by #connect when server resonds with 404
			 */
			throw new DeltaCloudNotFoundClientException(
					MessageFormat.format("Could not find resource {0}", request.getUrlString()));

		}
	}

	private void setRequestMethod(DeltaCloudRequest request, HttpURLConnection connection) throws IOException {
		HttpMethod httpMethod = request.getHttpMethod();
		connection.setRequestMethod(httpMethod.name());
		switch (httpMethod) {
		case PUT:
		case POST:
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length", "0");// String.valueOf(request.getParametersLength()));
			connection.setDoOutput(true);
			connection.getOutputStream().flush();
			break;
		case GET:
			connection.setDoOutput(false);
			break;
		}

	}

	/**
	 * Adds the credentials to the given http url connection.
	 * 
	 * The current implementation uses low level API. Alternatively
	 * {@link Authenticator#setDefault(Authenticator)} could be used which would
	 * then rule all url connections in the same jvm.
	 * 
	 * @param httpClient
	 *            the http client
	 * @return the default http client
	 * @throws IOException
	 */
	private void addCredentials(URLConnection urlConnection) throws IOException {
		String username = getUsername();
		String password = getPassword();
		if (username != null && password != null) {
			String credentials = new StringBuilder()
					.append(PREFIX_BASIC_AUTHENTICATION)
					.append(getAuthenticationValue(username, password))
					.toString();
			urlConnection.setRequestProperty(PROPERTY_AUTHORIZATION, credentials);
		}

	}

	private String getAuthenticationValue(String username, String password) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(username.getBytes());
		out.write(USERNAME_PASSWORD_DELIMITER);
		out.write(password.getBytes());
		char[] encoded = Base64Coder.encode(out.toByteArray());
		return new String(encoded);
	}
}
