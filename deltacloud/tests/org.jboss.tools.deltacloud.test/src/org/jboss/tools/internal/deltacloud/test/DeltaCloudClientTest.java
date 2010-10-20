package org.jboss.tools.internal.deltacloud.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.client.DeltaCloudClient;
import org.jboss.tools.deltacloud.core.client.DeltaCloudClientException;
import org.jboss.tools.internal.deltacloud.test.fakes.ServerFake;
import org.junit.Before;
import org.junit.Test;

public class DeltaCloudClientTest {

	private static final String DELTACLOUD_URL = "http://localhost:3001";
	private static final String SERVERFAKE_URL = "http://localhost:3002";
	private static final String DELTACLOUD_USER = "user";
	private static final String DELTACLOUD_PASSWORD = "password";

	private DeltaCloudClient client;
	private DeltaCloudClient fakeServerClient;

	@Before
	public void createDeltaCloudClient() throws MalformedURLException {
		this.client = new DeltaCloudClient(new URL(DELTACLOUD_URL), DELTACLOUD_USER, DELTACLOUD_PASSWORD);
		this.fakeServerClient = new DeltaCloudClient(new URL(SERVERFAKE_URL), DELTACLOUD_USER, DELTACLOUD_PASSWORD);
	}

	@Test
	public void isDeltaCloudRunning() throws MalformedURLException, IOException {
		URLConnection connection = new URL(DELTACLOUD_URL).openConnection();
		connection.connect();
	}

	@Test
	public void canRecognizeMockDeltaCloud() throws IOException {
		assertEquals(DeltaCloudClient.DeltaCloudType.MOCK, new DeltaCloudClient(DELTACLOUD_URL, DELTACLOUD_USER, DELTACLOUD_PASSWORD).getServerType());
	}

	@Test
	public void reportsUnknownUrl() throws IOException {
		ServerFake serverFake = new ServerFake(new URL(SERVERFAKE_URL).getPort(), "<dummy></dummy>");
		serverFake.start();
		try {
			assertEquals(DeltaCloudClient.DeltaCloudType.UNKNOWN, new DeltaCloudClient(SERVERFAKE_URL, DELTACLOUD_USER, DELTACLOUD_PASSWORD).getServerType());
		} finally {
			serverFake.stop();
		}
	}
}
