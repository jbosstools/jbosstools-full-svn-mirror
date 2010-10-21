package org.jboss.tools.internal.deltacloud.test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.jboss.tools.deltacloud.core.client.DeltaCloudClient;
import org.jboss.tools.deltacloud.core.client.DeltaCloudClientException;
import org.jboss.tools.deltacloud.core.client.Image;
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
			fail("no exception catched");
		} catch (Exception e) {
			assertEquals(DeltaCloudClientException.class, e.getClass());
		}
	}

	@Test
	public void canListMockImages() throws DeltaCloudClientException {
		List<Image> images = client.listImages();
		assertEquals(3, images.size());
		assertImage("img2", "Fedora 10", "fedoraproject", "Fedora 10", "i386", images.get(0));
		assertImage("img1", "Fedora 10", "fedoraproject", "Fedora 10", "x86_64", images.get(1));
		assertImage("img3", "JBoss", "mockuser", "JBoss", "i386", images.get(2));
	}

	private void assertImage(String id, String name, String owner, String description, String architecture, Image image) {
		assertEquals(id, image.getId());
		assertEquals(name, image.getName());
		assertEquals(owner, image.getOwnerId());
		assertEquals(architecture, image.getArchitecture());
		assertEquals(description, image.getDescription());
	}
}
