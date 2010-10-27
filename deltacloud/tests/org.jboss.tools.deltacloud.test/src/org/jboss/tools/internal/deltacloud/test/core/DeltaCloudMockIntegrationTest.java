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
package org.jboss.tools.internal.deltacloud.test.core;

import java.io.IOException;
import java.net.MalformedURLException;

import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.client.DeltaCloudClientException;
import org.jboss.tools.internal.deltacloud.test.context.MockIntegrationTestContext;
import org.jboss.tools.internal.deltacloud.test.fakes.ServerFake;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * An integration test that test the connection test method in DeltaCloud
 * 
 * @author Andre Dietisheim
 * 
 * @see DeltaCloud#testConnection()
 * 
 */
public class DeltaCloudMockIntegrationTest {

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
	public void testConnectionDoesNotThrowOnForbidden() throws MalformedURLException, DeltaCloudClientException {
		ServerFake serverFake = setupServerFake("HTTP/1.1 403 Forbidden\n\n\n");
		try {
			DeltaCloud deltaCloud = new DeltaCloud("aName", "http://localhost:" + ServerFake.DEFAULT_PORT, "badUser", "badPassword");
			deltaCloud.testConnection();
		} finally {
			serverFake.stop();
		}
	}

	@Test(expected=DeltaCloudClientException.class)
	public void testConnectionDoesThrowOnGeneralError() throws MalformedURLException, DeltaCloudClientException {
		ServerFake serverFake = setupServerFake("HTTP/1.1 501 Some Error\ndummy dummy dummy\n\n");
		try {
			DeltaCloud deltaCloud = new DeltaCloud("aName", "http://localhost:" + ServerFake.DEFAULT_PORT, "badUser", "badPassword");
			deltaCloud.testConnection();
		} finally {
			serverFake.stop();
		}
	}

	private ServerFake setupServerFake(String response) throws MalformedURLException {
		ServerFake serverFake = new ServerFake(response);
		serverFake.start();
		return serverFake;
	}
}
