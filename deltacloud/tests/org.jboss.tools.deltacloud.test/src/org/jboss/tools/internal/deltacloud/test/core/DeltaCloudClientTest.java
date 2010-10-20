package org.jboss.tools.internal.deltacloud.test.core;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.jboss.tools.deltacloud.core.client.DeltaCloudClient;
import org.junit.Before;
import org.junit.Test;

public class DeltaCloudClientTest {

	private static final String DELTACLOUD_URL = "http://localhost:3001";
	private static final String DELTACLOUD_USER = "user";
	private static final String DELTACLOUD_PASSWORD = "password";

	private DeltaCloudClient client;
	
	@Before
	public void createDeltaCloudClient() throws MalformedURLException {
		this.client = new DeltaCloudClient(new URL(DELTACLOUD_URL), DELTACLOUD_USER, DELTACLOUD_PASSWORD);
	}
	
	@Test
	public void isDeltaCloudRunning() throws MalformedURLException, IOException {
		URLConnection connection = new URL(DELTACLOUD_URL).openConnection();
		connection.connect();
	}
	
	@Test
	public void canRecognizeMockDeltaCloud() throws IOException {
		Process process = launchDeltaCloudMock();
		assertEquals(DeltaCloudClient.DeltaCloudType.MOCK, DeltaCloudClient.getDeltaCloudType(DELTACLOUD_URL));
		process.destroy();
	}

	private Process launchDeltaCloudMock() throws IOException {
		return Runtime.getRuntime().exec("/usr/bin/deltacloudd -i mock");
	}
}
