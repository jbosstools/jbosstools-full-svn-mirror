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
package org.jboss.tools.internal.deltacloud.test.core.client;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.jboss.tools.deltacloud.core.client.DeltaCloudClientImpl;
import org.jboss.tools.deltacloud.core.client.DeltaCloudClientException;
import org.jboss.tools.deltacloud.core.client.Image;
import org.jboss.tools.internal.deltacloud.test.context.MockIntegrationTestContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * An integration test that test various image related operations in DeltaCloudClient
 * 
 * @author Andre Dietisheim
 * 
 * @see DeltaCloudClientImpl#listImages()
 * @see DeltaCloudClientImpl#listImages(String)
 * 
 */
public class ImageMockIntegrationTest {

	private MockIntegrationTestContext testSetup;

	@Before
	public void setUp() throws IOException, DeltaCloudClientException {
		this.testSetup = new MockIntegrationTestContext();
		testSetup.setUp();
	}

	@After
	public void tearDown() {
		testSetup.tearDown();
	}

	@Test(expected = DeltaCloudClientException.class)
	public void cannotListIfNotAuthenticated() throws MalformedURLException, DeltaCloudClientException {
		DeltaCloudClientImpl client = new DeltaCloudClientImpl(MockIntegrationTestContext.DELTACLOUD_URL, "badUser", "badPassword");
		client.listImages();
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
