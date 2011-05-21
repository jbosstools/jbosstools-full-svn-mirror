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
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.jboss.tools.deltacloud.client.DeltaCloudClientImpl;
import org.jboss.tools.deltacloud.client.DeltaCloudClientException;
import org.jboss.tools.deltacloud.client.Realm;
import org.jboss.tools.internal.deltacloud.client.test.context.MockIntegrationTestContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Integration tests for key related operations in delta cloud client.
 * 
 * @author Andre Dietisheim
 * 
 * @see DeltaCloudClientImpl#listRealms()
 * @see DeltaCloudClientImpl#listRealm(String)
 */
public class RealmMockIntegrationTest {

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
	public void canListRealms() throws DeltaCloudClientException {
		List<Realm> realms = testSetup.getClient().listRealms();
		assertNotNull(realms);
		assertTrue(realms.size() > 0);
	}

	@Test
	public void canGetProfile() throws DeltaCloudClientException {
		// get a profile seen in the web UI
		Realm realm = testSetup.getClient().listRealms("eu");
		assertNotNull(realm);
		assertRealm("Europe", Realm.RealmState.AVAILABLE, 0, realm);
	}

	public void assertRealm(String name, Realm.RealmState state, int limit, Realm realm) {
		assertEquals(name, realm.getName());
		assertEquals(state, realm.getState());
		assertEquals(limit, realm.getLimit());
	}
}
