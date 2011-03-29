package org.jboss.tools.deltacloud.core.client.transport;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

import org.jboss.tools.deltacloud.core.client.DeltaCloudAuthClientException;
import org.jboss.tools.deltacloud.core.client.DeltaCloudClientException;
import org.jboss.tools.deltacloud.core.client.DeltaCloudNotFoundClientException;
import org.jboss.tools.deltacloud.core.client.HttpStatusCode;
import org.jboss.tools.deltacloud.core.client.HttpStatusRange;
import org.jboss.tools.deltacloud.core.client.request.DeltaCloudRequest;

public abstract class AbstractHttpTransport implements IHttpTransport {

	private String username;
	private String password;

	public AbstractHttpTransport(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public final InputStream request(DeltaCloudRequest request) throws DeltaCloudClientException {
		try {
			return doRequest(request);
		} catch (MalformedURLException e) {
			throw new DeltaCloudClientException(MessageFormat.format(
					"Could not connect to \"{0}\". The url is invalid.", request.getUrlString()), e);
		} catch(DeltaCloudClientException e) {
			throw e;
		} catch (Exception e) {
			throw new DeltaCloudClientException(e);
		}
	}

	protected abstract InputStream doRequest(DeltaCloudRequest request) throws Exception;

	protected void throwOnHttpErrors(int statusCode, String statusMessage, URL requestUrl)
			throws DeltaCloudClientException {
		if (HttpStatusCode.OK.isStatus(statusCode)) {
			return;
		} else if (HttpStatusCode.UNAUTHORIZED.isStatus(statusCode)) {
			throw new DeltaCloudAuthClientException(
					MessageFormat.format("The server reported an authorization error \"{0}\" on requesting \"{1}\"",
									statusMessage, requestUrl));
		} else if (HttpStatusCode.NOT_FOUND.isStatus(statusCode)) {
			throw new DeltaCloudNotFoundClientException(MessageFormat.format(
					"The server could not find the resource \"{0}\"",
					requestUrl));
		} else if (HttpStatusRange.CLIENT_ERROR.isInRange(statusCode)
				|| HttpStatusRange.SERVER_ERROR.isInRange(statusCode)) {
			throw new DeltaCloudClientException(
					MessageFormat.format("The server reported an error \"{0}\" on requesting \"{1}\"",
									statusMessage, requestUrl));
		}
	}

	protected String getUsername() {
		return username;
	}

	protected String getPassword() {
		return password;
	}

}
