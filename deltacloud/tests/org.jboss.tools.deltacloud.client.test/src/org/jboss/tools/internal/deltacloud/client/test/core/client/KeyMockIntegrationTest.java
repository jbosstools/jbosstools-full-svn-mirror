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
package org.jboss.tools.internal.deltacloud.client.test.core.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

import java.io.IOException;
import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jboss.tools.deltacloud.client.DeltaCloudClient;
import org.jboss.tools.deltacloud.client.DeltaCloudClientException;
import org.jboss.tools.deltacloud.client.DeltaCloudClientImpl;
import org.jboss.tools.deltacloud.client.DeltaCloudNotFoundClientException;
import org.jboss.tools.deltacloud.client.Key;
import org.jboss.tools.internal.deltacloud.client.test.context.MockIntegrationTestContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Integration tests for key related operations in delta cloud client.
 * 
 * @author Andre Dietisheim
 * 
 * @see DeltaCloudClientImpl#createKey(String, String)
 * @see DeltaCloudClientImpl#deleteKey(String)
 */
public class KeyMockIntegrationTest {

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

	@Test
	public void canCreateKey() throws DeltaCloudClientException {
		String id = "test" + System.currentTimeMillis();
		DeltaCloudClient client = testSetup.getClient();
		try {
			Key key = client.createKey(id);
			assertNotNull(key);
			assertEquals(id, key.getId());
		} finally {
			quietlyDeleteKey(id);
		}
	}

	@Test(expected = DeltaCloudClientException.class)
	public void createDuplicateKeyThrowsException() throws DeltaCloudClientException {
		String id = "test" + System.currentTimeMillis();
		DeltaCloudClient client = testSetup.getClient();
		try {
			client.createKey(id);
			client.createKey(id);
		} finally {
			quietlyDeleteKey(id);
		}
	}

	/**
	 * Checks if a key may be deleted.
	 */
	@Test(expected = DeltaCloudNotFoundClientException.class)
	public void canDeleteKey() throws DeltaCloudClientException {
		String id = "test" + System.currentTimeMillis();
		DeltaCloudClient client = testSetup.getClient();
		Key key = client.createKey(id);
		assertNotNull(key);
		assertEquals(id, key.getId());
		key.destroy(client);
		client.listKey(key.getId());
	}

	/**
	 * checks if the client throws a {@link DeltaCloudNotFoundClientException}
	 * if an unknown key is requested.
	 * 
	 * @throws DeltaCloudClientException
	 */
	@Test(expected = DeltaCloudNotFoundClientException.class)
	public void listUnknownKeyThrowsException() throws DeltaCloudClientException {
		String id = String.valueOf(System.currentTimeMillis());
		testSetup.getClient().listKey(id);
	}

	@Test
	public void canListKey() throws DeltaCloudClientException {
		String id = String.valueOf(System.currentTimeMillis());
		DeltaCloudClient client = testSetup.getClient();
		try {
			Key createdKey = client.createKey(id);
			Key listedKey = client.listKey(id);
			assertEquals(createdKey.getId(), listedKey.getId());
		} finally {
			quietlyDeleteKey(id);
		}
	}

	@Test
	public void canListKeys() throws DeltaCloudClientException {
		String id = String.valueOf(System.currentTimeMillis());
		DeltaCloudClient client = testSetup.getClient();
		try {
			final Key createdKey = client.createKey(id);
			List<Key> keys = client.listKeys();
			assertNotNull(keys);
			assertThat(keys, hasItem(new BaseMatcher<Key>() {

				@Override
				public boolean matches(Object item) {
					if (item instanceof Key) {
						Key listedKey = (Key) item;
						return
							createdKey.getId().equals(listedKey.getId())
									&& createdKey.getFingerprint().equals(listedKey.getFingerprint())
									&& createdKey.getPem().equals(listedKey.getPem())
									&& createdKey.getUrl().equals(listedKey.getUrl())
									&& createdKey.getActions().size() == listedKey.getActions().size();
						}
						return false;
					}

				@Override
				public void describeTo(Description description) {
					// TODO Auto-generated method stub

				}
			}));
		} finally {
			quietlyDeleteKey(id);
		}
	}

	private void quietlyDeleteKey(String id) {
		try {
			DeltaCloudClient client = testSetup.getClient();
			Key key = client.listKey(id);
			key.destroy(client);
		} catch (Exception e) {
			// ignore
		}
	}
}
