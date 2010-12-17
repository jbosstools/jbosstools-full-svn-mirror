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
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.jboss.tools.deltacloud.core.client.DeltaCloudClientException;
import org.jboss.tools.deltacloud.core.client.DeltaCloudClientImpl;
import org.jboss.tools.deltacloud.core.client.DeltaCloudNotFoundClientException;
import org.jboss.tools.deltacloud.core.client.Key;
import org.jboss.tools.internal.deltacloud.test.context.MockIntegrationTestContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
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

	/**
	 * checks if the client throws a {@link DeltaCloudNotFoundClientException} if an
	 * unknown key is requested.
	 * 
	 * @throws DeltaCloudClientException
	 */
	@Ignore
	@Test(expected = DeltaCloudNotFoundClientException.class)
	public void listUnknownKeyThrowsException() throws DeltaCloudClientException {
		String keyName = String.valueOf(System.currentTimeMillis());
		testSetup.getClient().listKey(keyName);
	}

	@Test
	public void canCreateKey() throws DeltaCloudClientException {
		String keyName = "test" + System.currentTimeMillis();
		Key key = testSetup.getClient().createKey(keyName);
		assertNotNull(key);
		assertEquals(keyName, key.getId());
	}

	@Ignore
	@Test
	public void canListKey() throws DeltaCloudClientException {
		String keyName = String.valueOf(System.currentTimeMillis());
		Key createdKey = testSetup.getClient().createKey(keyName);
		Key listedKey = testSetup.getClient().listKey(keyName);
		assertEquals(createdKey.getId(), listedKey.getId());
	}

	@Test
	public void canListKeys() {

	}
}
