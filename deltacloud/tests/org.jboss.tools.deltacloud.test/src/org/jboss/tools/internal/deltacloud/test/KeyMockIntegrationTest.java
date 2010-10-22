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

import java.io.IOException;

import org.jboss.tools.deltacloud.core.client.DeltaCloudClient;
import org.jboss.tools.deltacloud.core.client.DeltaCloudClientException;
import org.junit.After;
import org.junit.Before;

/**
 * Integration tests for key related operations in delta cloud client.
 * 
 * @see DeltaCloudClient#createKey(String, String)
 * @see DeltaCloudClient#deleteKey(String)
 */
public class KeyMockIntegrationTest {

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
}
