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
package org.jboss.tools.internal.deltacloud.test.context;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.deltacloud.client.DeltaCloudClient;
import org.apache.deltacloud.client.DeltaCloudClientException;
import org.apache.deltacloud.client.DeltaCloudClientImpl;
import org.apache.deltacloud.client.Image;
import org.apache.deltacloud.client.Instance;
import org.apache.deltacloud.client.StateAware.State;

/**
 * A class that holds the integration test context
 * 
 * @author Andre Dietisheim
 * 
 */
public class MockIntegrationTestContext {

	public static final String DELTACLOUD_URL = "http://localhost:3001";
	public static final String SERVERFAKE_URL = "http://localhost:3002";
	public static final String DELTACLOUD_USER = "mockuser";
	public static final String DELTACLOUD_PASSWORD = "mockpassword";

	private DeltaCloudClient client;
	private Instance testInstance;

	private ExecutorService executor = Executors.newSingleThreadExecutor();

	public void setUp() throws IOException, DeltaCloudClientException {
		ensureDeltaCloudIsRunning();
		this.client = new DeltaCloudClientImpl(DELTACLOUD_URL, DELTACLOUD_USER, DELTACLOUD_PASSWORD);
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
		executor.shutdownNow();
	}

	public void quietlyDestroyInstance(Instance instance) {
		if (instance != null) {
			try {
				if (instance.getState() == Instance.State.RUNNING) {
					instance.stop(client);
				}
				instance.destroy(client);
			} catch (Exception e) {
				// ignore
			}
		}
	}

	/**
	 * Waits for an instance to get the given state for a given timeout.
	 * 
	 * @param instanceId
	 *            the id of the instance to watch
	 * @param state
	 *            the state to wait for
	 * @param timeout
	 *            the timeout to wait for
	 * @return <code>true</code>, if the state was reached while waiting for
	 *         timeout, <code>false</code> otherwise
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public boolean waitForInstanceState(final String instanceId, final State state, final long timeout)
			throws InterruptedException, ExecutionException {
		final long startTime = System.currentTimeMillis();
		Callable<Boolean> waitingCallable = new Callable<Boolean>() {

			@Override
			public Boolean call() throws Exception {
				try {
					while (System.currentTimeMillis() < startTime + timeout) {
						if (client.listInstances(instanceId).getState() == state) {
							return true;
						}
						Thread.sleep(200);
					}
					return false;
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
		};
		return executor.submit(waitingCallable).get();
	}
}
