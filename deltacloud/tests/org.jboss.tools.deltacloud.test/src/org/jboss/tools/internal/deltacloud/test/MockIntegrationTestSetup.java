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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.jboss.tools.deltacloud.core.client.DeltaCloudClient;
import org.jboss.tools.deltacloud.core.client.DeltaCloudClientException;
import org.jboss.tools.deltacloud.core.client.Image;
import org.jboss.tools.deltacloud.core.client.Instance;

public class MockIntegrationTestSetup {

	public static final String DELTACLOUD_URL = "http://localhost:3001";
	public static final String SERVERFAKE_URL = "http://localhost:3002";
	public static final String DELTACLOUD_USER = "mockuser";
	public static final String DELTACLOUD_PASSWORD = "mockpassword";

	private DeltaCloudClient client;
	private Instance testInstance;

	public void setUp() throws IOException, DeltaCloudClientException {
		ensureDeltaCloudIsRunning();
		this.client = new DeltaCloudClient(DELTACLOUD_URL, DELTACLOUD_USER, DELTACLOUD_PASSWORD);
		Image image = getFirstImage(client);
		this.testInstance = createTestInstance(image);
	}

	private Instance createTestInstance(Image image) throws DeltaCloudClientException {
		assertNotNull(image);
		Instance instance = client.createInstance(image.getId());
		return instance;
	}
	
	public void ensureDeltaCloudIsRunning() throws IOException {
		try {
			URLConnection connection = new URL(DELTACLOUD_URL).openConnection();
			connection.connect();
		} catch (ConnectException e) {
			fail("Local DeltaCloud instance is not running. Please start a DeltaCloud instance before running these tests.");
		}
	}
	
	public DeltaCloudClient getClient() {
		return client;
	}

	public Instance getTestInstance() {
		return testInstance;
	}

	public Image getFirstImage(DeltaCloudClient client) throws DeltaCloudClientException {
		List<Image> images = client.listImages();
		assertTrue(images.size() >= 1);
		Image image = images.get(0);
		return image;
	}
	

	public Instance getInstanceById(String id, DeltaCloudClient client) throws DeltaCloudClientException {
		for (Instance availableInstance : client.listInstances()) {
			if (id.equals(availableInstance.getId())) {
				return availableInstance;
			}
		}
		return null;
	}
	
	public void tearDown() {
		quietlyDestroyInstance(testInstance);
	}
	
	public void quietlyDestroyInstance(Instance instance) {
		if (instance != null) {
			try {
				client.destroyInstance(instance.getId());
			} catch (Exception e) {
				// ignore
			}
		}
	}
}
