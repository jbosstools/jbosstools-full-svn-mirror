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

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;

import javax.xml.bind.JAXBException;

import org.jboss.tools.deltacloud.core.client.DeltaCloudClientException;
import org.jboss.tools.deltacloud.core.client.Instance;
import org.jboss.tools.deltacloud.core.client.InstanceAction;
import org.jboss.tools.deltacloud.core.client.unmarshal.InstanceActionUnmarshaller;
import org.jboss.tools.deltacloud.core.client.unmarshal.InstanceUnmarshaller;
import org.jboss.tools.internal.deltacloud.test.fakes.ServerInstanceResponseFakes.InstanceActionResponse;
import org.jboss.tools.internal.deltacloud.test.fakes.ServerInstanceResponseFakes.InstanceResponse;
import org.junit.Test;

/**
 * @author André Dietisheim
 */
public class InstanceDomUnmarshallingTest {

	@Test
	public void keyActionMayBeUnmarshalled() throws MalformedURLException, JAXBException, DeltaCloudClientException {
		InstanceAction instanceAction = new InstanceAction();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(InstanceActionResponse.response.getBytes());
		new InstanceActionUnmarshaller().unmarshall(inputStream, instanceAction);
		assertNotNull(instanceAction);
		assertEquals(InstanceActionResponse.name, instanceAction.getName());
		assertEquals(InstanceActionResponse.url, instanceAction.getUrl().toString());
		assertEquals(InstanceActionResponse.method.toUpperCase(), instanceAction.getMethod().toString().toUpperCase());
	}

	@Test
	public void instanceMayBeUnmarshalled() throws DeltaCloudClientException {
		Instance instance = new Instance();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(InstanceResponse.instanceResponse.getBytes());
		new InstanceUnmarshaller().unmarshall(inputStream, instance);
		assertNotNull(instance);
		assertEquals(InstanceResponse.id1, instance.getId());
		assertEquals(InstanceResponse.name1, instance.getName());
		assertEquals(InstanceResponse.ownerId1, instance.getOwnerId());
		assertEquals(InstanceResponse.image1Id, instance.getImageId());
		assertEquals(InstanceResponse.hardwareProfile1Id, instance.getProfileId());
		assertEquals(InstanceResponse.realm1Id, instance.getRealmId());
		assertEquals(InstanceResponse.state, instance.getState());
		assertEquals(InstanceResponse.keyname1, instance.getKeyId());
		assertEquals(2, instance.getActions().size());
		assertEquals(InstanceResponse.actionNameStop, instance.getActions().get(0).getName());
		assertEquals(InstanceResponse.actionNameReboot, instance.getActions().get(1).getName());
		assertEquals(1, instance.getPublicAddresses().size());
		assertEquals(InstanceResponse.publicAddress1, instance.getPublicAddresses().get(0));
		assertEquals(1, instance.getPrivateAddresses().size());
		assertEquals(InstanceResponse.privateAddress1, instance.getPrivateAddresses().get(0));
		
	}
}
