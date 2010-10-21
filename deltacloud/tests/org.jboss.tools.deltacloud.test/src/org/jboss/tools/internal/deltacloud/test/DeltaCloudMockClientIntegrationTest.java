package org.jboss.tools.internal.deltacloud.test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.jboss.tools.deltacloud.core.client.DeltaCloudClient;
import org.jboss.tools.deltacloud.core.client.DeltaCloudClientException;
import org.jboss.tools.internal.deltacloud.test.fakes.ServerFake;
import org.junit.Before;
import org.junit.Test;

public class DeltaCloudMockClientIntegrationTest {

	private static final String DELTACLOUD_URL = "http://localhost:3001";
	private static final String SERVERFAKE_URL = "http://localhost:3002";
	private static final String DELTACLOUD_USER = "mockuser";
	private static final String DELTACLOUD_PASSWORD = "mockpassword";

	private DeltaCloudClient client;

	@Before
	public void createClient() throws IOException {
		assertTrue(isDeltaCloudRunning());
		client = new DeltaCloudClient(DELTACLOUD_URL, DELTACLOUD_USER, DELTACLOUD_PASSWORD);
	}

	public boolean isDeltaCloudRunning() throws IOException {
		URLConnection connection = new URL(DELTACLOUD_URL).openConnection();
		connection.connect();
		return true;
	}

	@Test
	public void canRecognizeMockDeltaCloud() throws IOException {
		assertEquals(DeltaCloudClient.DeltaCloudType.MOCK, client.getServerType());
	}

	@Test
	public void reportsUnknownUrl() throws IOException {
		ServerFake serverFake = new ServerFake(new URL(SERVERFAKE_URL).getPort(), "<dummy></dummy>");
		serverFake.start();
		try {
			assertEquals(DeltaCloudClient.DeltaCloudType.UNKNOWN, new DeltaCloudClient(SERVERFAKE_URL, DELTACLOUD_USER,
					DELTACLOUD_PASSWORD).getServerType());
		} finally {
			serverFake.stop();
		}
	}

	@Test(expected = DeltaCloudClientException.class)
	public void notAuthenticatedCannotListImages() throws MalformedURLException, DeltaCloudClientException {
		DeltaCloudClient client = new DeltaCloudClient(DELTACLOUD_URL, "badUser", "badPassword");
		client.listImages();
	}

	@Test
	public void reports404OnUnknownResource() {
		try {
			DeltaCloudClient errorClient = new DeltaCloudClient(DELTACLOUD_URL) {
				@Override
				protected HttpUriRequest getRequest(RequestType requestType, String requestUrl) {
					return new HttpGet(DELTACLOUD_URL + "/DUMMY");
				}
			};
			errorClient.listImages();
		} catch (Exception e) {
			assertEquals(DeltaCloudClientException.class, e.getClass());
		}
		fail("no exception catched");
	}

	@Test
	public void canListImages() throws DeltaCloudClientException {
		client.listImages();
	}
}
