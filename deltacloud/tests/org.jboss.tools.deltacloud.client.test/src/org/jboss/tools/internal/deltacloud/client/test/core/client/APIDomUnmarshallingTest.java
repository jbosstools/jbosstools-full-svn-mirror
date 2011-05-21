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

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;

import org.jboss.tools.deltacloud.client.API;
import org.jboss.tools.deltacloud.client.DeltaCloudClientException;
import org.jboss.tools.deltacloud.client.API.Driver;
import org.jboss.tools.deltacloud.client.unmarshal.APIUnmarshaller;
import org.jboss.tools.internal.deltacloud.client.test.fakes.APIResponseFakes.APIResponse;
import org.junit.Test;

/**
 * @author André Dietisheim
 */
public class APIDomUnmarshallingTest {

	@Test
	public void ec2DriverIsUnmarshalled() throws MalformedURLException, DeltaCloudClientException {
		API api = new API();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(APIResponse.apiResponse.getBytes());
		new APIUnmarshaller().unmarshall(inputStream, api);
		assertNotNull(api);
		assertEquals(APIResponse.driver, api.getDriver().name().toLowerCase());
	}

	@Test
	public void invalidDriverUnmarshalledToUnknown() throws MalformedURLException, DeltaCloudClientException {
		API api = new API();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(APIResponse.invalidDriverApiResponse.getBytes());
		new APIUnmarshaller().unmarshall(inputStream, api);
		assertNotNull(api);
		assertEquals(Driver.UNKNOWN, api.getDriver());
	}
}
