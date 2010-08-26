package org.jboss.tools.usage;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.jboss.tools.usage.util.HttpEncodingUtils;
import org.jboss.tools.usage.util.StatusUtils;
import org.jboss.tools.usage.util.reader.ReaderUtils;

public abstract class HttpResourceMap {

	static final String GET_METHOD_NAME = "GET"; //$NON-NLS-1$

	protected Plugin plugin;
	protected HttpURLConnection urlConnection;
	private Map<String, String> valuesMap;

	private String[] keys;

	private String url;

	private char valueDelimiter;

	public HttpResourceMap(String url, char valueDelimiter, Plugin plugin, String... keys) {
		this.url = url;
		this.keys = keys;
		this.valueDelimiter = valueDelimiter;
		this.plugin = plugin;
	}

	protected Map<String, String> getValueMap() throws UnsupportedEncodingException, IOException {
		if (valuesMap == null) {
			this.valuesMap = parse(keys, valueDelimiter, request(url), new HashMap<String, String>());
		}
		return valuesMap;
	}

	/**
	 * Sends a http GET request to the given URL. Returns the response string or
	 * <tt>null</tt> if an error occurred. The errors catched are Exceptions or
	 * HTTP error codes.
	 * 
	 * @param url
	 *            the url to send the GET request to
	 * @return the response or <tt>null</tt> if an error occured.
	 * @throws UnsupportedEncodingException
	 * 
	 * @see HttpURLConnection
	 */
	protected InputStreamReader request(String url) throws UnsupportedEncodingException {
		InputStreamReader responseReader = null;
		try {
			urlConnection.connect();
			int responseCode = getResponseCode(urlConnection);
			if (responseCode == HttpURLConnection.HTTP_OK) {
				IStatus status = StatusUtils.getDebugStatus(
						plugin.getBundle().getSymbolicName()
						, UsageMessages.KillSwitchPreference_Info_HttpQuery
						, url);
				plugin.getLog().log(status);
				responseReader = getInputStreamReader(urlConnection.getContentType());
			} else {
				IStatus status = StatusUtils.getErrorStatus(
						plugin.getBundle().getSymbolicName()
						, UsageMessages.KillSwitchPreference_Error_Http, null, url);
				plugin.getLog().log(status);
			}
		} catch (Exception e) {
			IStatus status = StatusUtils.getErrorStatus(
					plugin.getBundle().getSymbolicName()
					, UsageMessages.KillSwitchPreference_Error_Http, e, url);
			plugin.getLog().log(status);
		}
		return responseReader;
	}

	private InputStreamReader getInputStreamReader(String contentType) throws UnsupportedEncodingException, IOException {
		String contentTypeCharset = HttpEncodingUtils.getContentTypeCharset(contentType);
		if (contentTypeCharset != null && contentTypeCharset.length() > 0) {
			return new InputStreamReader(new BufferedInputStream(urlConnection.getInputStream()),
					contentTypeCharset);
		} else {
			return new InputStreamReader(new BufferedInputStream(urlConnection.getInputStream()));
		}
	}

	/**
	 * Parses the given string and extracts the enablement value.
	 * 
	 * @param valueDelimiter
	 * 
	 * @param input
	 *            stream that holds
	 * @return
	 * @return true, if successful
	 */
	private Map<String, String> parse(String[] keys, char valueDelimiter, InputStreamReader reader,
			Map<String, String> valuesMap) throws IOException {
		for (String key = null; (key = ReaderUtils.skipUntil(reader, keys)) != null;) {
			String value = ReaderUtils.readStringUntil(reader, valueDelimiter);
			valuesMap.put(key, value);
		}
		return valuesMap;
	}

	/**
	 * Creates a new url connection.
	 * 
	 * @param urlString
	 *            the url string
	 * @return the http url connection
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	protected HttpURLConnection createURLConnection(String urlString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setInstanceFollowRedirects(true);
		urlConnection.setRequestMethod(GET_METHOD_NAME);
		return urlConnection;
	}

	/**
	 * Returns the return code from the given {@link HttpURLConnection}.
	 * Provided to be called by test cases so that they can retrieve the return
	 * code.
	 * 
	 * @param urlConnection
	 *            to get the response code from
	 * @return the return code the HttpUrlConnection received
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	protected int getResponseCode(HttpURLConnection urlConnection) throws IOException {
		return urlConnection.getResponseCode();
	}

}