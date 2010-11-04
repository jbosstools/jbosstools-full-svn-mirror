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

import java.io.IOException;

import org.jboss.tools.deltacloud.core.client.DeltaCloudClientImpl;
import org.jboss.tools.deltacloud.core.client.DeltaCloudClientException;
import org.jboss.tools.internal.deltacloud.test.context.MockIntegrationTestContext;
import org.junit.After;
import org.junit.Before;

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
}
