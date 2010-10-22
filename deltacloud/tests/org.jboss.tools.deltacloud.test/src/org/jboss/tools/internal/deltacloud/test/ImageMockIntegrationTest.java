/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.internal.deltacloud.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.jboss.tools.deltacloud.core.client.DeltaCloudClient;
import org.jboss.tools.deltacloud.core.client.DeltaCloudClientException;
import org.jboss.tools.deltacloud.core.client.Image;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ImageMockIntegrationTest {

	private MockIntegrationTestSetup testSetup;

	@Before
	public void setUp() throws IOException, DeltaCloudClientException {
		this.testSetup = new MockIntegrationTestSetup();
		testSetup.setUp();
	}

	@After
	public void tearDown() {
		testSetup.tearDown();
	}

	@Test(expected = DeltaCloudClientException.class)
	public void cannotListIfNotAuthenticated() throws MalformedURLException, DeltaCloudClientException {
		DeltaCloudClient client = new DeltaCloudClient(MockIntegrationTestSetup.DELTACLOUD_URL, "badUser", "badPassword");
		client.listImages();
	}

	@Test
	public void throwsDeltaCloudClientExceptionOnUnknownResource() {
		try {
			DeltaCloudClient errorClient = new DeltaCloudClient(MockIntegrationTestSetup.DELTACLOUD_URL) {
				@Override
				protected HttpUriRequest getRequest(RequestType requestType, String requestUrl) {
					return new HttpGet(MockIntegrationTestSetup.DELTACLOUD_URL + "/DUMMY");
				}
			};
			errorClient.listImages();
			fail("no exception catched");
		} catch (Exception e) {
			assertEquals(DeltaCloudClientException.class, e.getClass());
		}
	}

	@Test
	public void assertImagesFromWebUI() throws DeltaCloudClientException {
		List<Image> images = testSetup.getClient().listImages();
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
