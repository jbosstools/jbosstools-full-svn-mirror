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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.jboss.tools.deltacloud.core.client.DeltaCloudClient;
import org.jboss.tools.deltacloud.core.client.DeltaCloudClientException;
import org.jboss.tools.deltacloud.core.client.Image;
import org.jboss.tools.deltacloud.core.client.Instance;
import org.jboss.tools.deltacloud.core.client.Instance.Action;
import org.jboss.tools.deltacloud.core.client.Instance.State;
import org.jboss.tools.internal.deltacloud.test.context.MockIntegrationTestContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Integration tests for instance related methods in {@link DeltaCloudClient}.
 * 
 * @author Andre Dietisheim
 * 
 * @see DeltaCloudClient#listInstances()
 * @see DeltaCloudClient#createInstance(String)
 * @see DeltaCloudClient#destroyInstance(String)
 * @see DeltaCloudClient#startInstance(String)
 * @see DeltaCloudClient#shutdownInstance(String)
 */
public class InstanceMockIntegrationTest {

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

	/**
	 * #listInstance contains the test instance created in {@link #setUp()}
	 * 
	 * @throws DeltaCloudClientException
	 *             the delta cloud client exception
	 */
	@Ignore
	@Test
	public void listContainsTestInstance() throws DeltaCloudClientException {
		DeltaCloudClient client = testSetup.getClient();
		List<Instance> instances = client.listInstances();
		assertTrue(instances.size() > 0);
		Instance testInstance = testSetup.getTestInstance();
		assertNotNull(testSetup.getInstanceById(testInstance.getId(), client));
	}

	@Ignore
	@Test
	public void listTestInstance() throws DeltaCloudClientException {
		Instance instance = testSetup.getClient().listInstances(testSetup.getTestInstance().getId());
		assertNotNull(instance);
		Instance testInstance = testSetup.getTestInstance();
		assertEquals(testInstance.getId(), instance.getId());
		assertInstance(
				testInstance.getName()
				, testInstance.getOwnerId()
				, testInstance.getImageId()
				, testInstance.getRealmId()
				, testInstance.getProfileId()
				, testInstance.getMemory()
				, testInstance.getPrivateAddresses()
				, testInstance.getPublicAddresses()
				, instance);
	}

	@Ignore
	@Test(expected = DeltaCloudClientException.class)
	public void listDestroyedInstanceThrowsException() throws DeltaCloudClientException {
		Instance testInstance = testSetup.getTestInstance();
		testSetup.quietlyDestroyInstance(testInstance);
		testSetup.getClient().listInstances(testInstance.getId());
	}

	private void assertInstance(String name, String owner, String ImageId, String realmId, String profile,
			String memory, List<String> privateAddresses, List<String> publicAddresses, Instance instance) {
		assertNotNull(instance);
		assertEquals(name, instance.getName());
		assertEquals(owner, instance.getOwnerId());
		assertEquals(realmId, instance.getRealmId());
		assertEquals(profile, instance.getProfileId());
		assertEquals(memory, instance.getMemory());
		assertTrue(privateAddresses.equals(instance.getPrivateAddresses()));
		assertTrue(publicAddresses.equals(instance.getPublicAddresses()));
	}

	@Ignore
	@Test(expected = DeltaCloudClientException.class)
	public void cannotDestroyIfNotAuthenticated() throws MalformedURLException, DeltaCloudClientException {
		DeltaCloudClient unauthenticatedClient = new DeltaCloudClient(MockIntegrationTestContext.DELTACLOUD_URL,
				"badUser", "badPassword");
		Image image = testSetup.getFirstImage(unauthenticatedClient);
		unauthenticatedClient.createInstance(image.getId());
	}

	@Ignore
	@Test
	public void canCreateInstance() throws DeltaCloudClientException {
		Instance instance = null;
		try {
			Image image = testSetup.getFirstImage(testSetup.getClient());
			instance = testSetup.getClient().createInstance(image.getId());
			assertTrue(instance != null);
			assertEquals(image.getId(), instance.getImageId());
			assertEquals(State.RUNNING, instance.getState());
		} finally {
			testSetup.quietlyDestroyInstance(instance);
		}
	}

	@Test(expected = DeltaCloudClientException.class)
	public void cannotDestroyUnknownImageId() throws DeltaCloudClientException {
		testSetup.getClient().createInstance("dummy");
	}

	@Ignore
	@Test
	public void canDestroy() throws DeltaCloudClientException {
		Image image = testSetup.getFirstImage(testSetup.getClient());
		Instance instance = testSetup.getClient().createInstance(image.getId());
		testSetup.getClient().destroyInstance(instance.getId());
		assertNull(testSetup.getInstanceById(instance.getId(), testSetup.getClient()));
	}

	@Ignore
	@Test(expected = DeltaCloudClientException.class)
	public void destroyThrowExceptionOnUnknowInstanceId() throws DeltaCloudClientException {
		testSetup.getClient().destroyInstance("dummy");
	}

	@Ignore
	@Test
	public void canShutdownInstance() throws DeltaCloudClientException {
		Instance testInstance = testSetup.getTestInstance();
		DeltaCloudClient client = testSetup.getClient();
		client.shutdownInstance(testInstance.getId());
		testInstance = client.listInstances(testInstance.getId()); // reload!
		assertEquals(State.STOPPED, testInstance.getState());
	}

	@Ignore
	@Test
	public void canStartInstance() throws DeltaCloudClientException {
		Instance testInstance = testSetup.getTestInstance();
		DeltaCloudClient client = testSetup.getClient();
		if (testInstance.getState() == State.RUNNING) {
			client.shutdownInstance(testInstance.getId());
		}
		client.startInstance(testInstance.getId());
		testInstance = client.listInstances(testInstance.getId()); // reload!
		assertEquals(State.RUNNING, testInstance.getState());
	}

	@Test
	public void canStartInstanceByAction() throws DeltaCloudClientException {
		Instance testInstance = testSetup.getTestInstance();
		DeltaCloudClient client = testSetup.getClient();
		if (testInstance.getState() == State.RUNNING) {
			client.performInstanceAction(testInstance.getId(), Action.STOP.toString());
		}
		assertTrue(client.performInstanceAction(testInstance.getId(), Action.START.toString()));
		testInstance = client.listInstances(testInstance.getId()); // reload!
		assertEquals(State.RUNNING, testInstance.getState());
	}

	@Test
	public void cannotStartRunningInstance() throws DeltaCloudClientException {
		Instance testInstance = testSetup.getTestInstance();
		DeltaCloudClient client = testSetup.getClient();
		client.startInstance(testInstance.getId());
		assertFalse(client.performInstanceAction(testInstance.getId(), Action.START.toString()));
	}

	@Test
	public void cannotStopStoppedInstance() throws DeltaCloudClientException {
		Instance testInstance = testSetup.getTestInstance();
		DeltaCloudClient client = testSetup.getClient();
		try {
			client.shutdownInstance(testInstance.getId());
			assertFalse(client.performInstanceAction(testInstance.getId(), Action.STOP.toString()));
		} finally {
			client.startInstance(testInstance.getId());
		}
	}

	@Test
	public void cannotDestroyRunningInstance() throws DeltaCloudClientException {
		Instance testInstance = testSetup.getTestInstance();
		DeltaCloudClient client = testSetup.getClient();
		testInstance = client.listInstances(testInstance.getId()); // reload
		assertTrue(testInstance.getState() == State.RUNNING);
		assertFalse(client.performInstanceAction(testInstance.getId(), Action.DESTROY.toString()));
	}

	@Test
	public void cannotRebootStoppedInstance() throws DeltaCloudClientException, InterruptedException, ExecutionException {
		Instance testInstance = testSetup.getTestInstance();
		DeltaCloudClient client = testSetup.getClient();
		try {
			client.shutdownInstance(testInstance.getId());
			testInstance = client.listInstances(testInstance.getId()); // reload
			assertTrue(testInstance.getState() == State.STOPPED);
			assertFalse(client.performInstanceAction(testInstance.getId(), Action.REBOOT.toString()));
		} finally {
			client.startInstance(testInstance.getId());
			client.listInstances(testInstance.getId()); // reload
		}
	}
}
