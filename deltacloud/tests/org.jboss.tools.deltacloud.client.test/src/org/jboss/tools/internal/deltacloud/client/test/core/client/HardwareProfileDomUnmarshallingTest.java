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
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.jboss.tools.deltacloud.client.DeltaCloudClientException;
import org.jboss.tools.deltacloud.client.HardwareProfile;
import org.jboss.tools.deltacloud.client.Property;
import org.jboss.tools.deltacloud.client.unmarshal.HardwareProfileUnmarshaller;
import org.jboss.tools.deltacloud.client.unmarshal.HardwareProfilesUnmarshaller;
import org.jboss.tools.internal.deltacloud.client.test.fakes.HardwareProfileResponseFakes.HardwareProfile1Response;
import org.jboss.tools.internal.deltacloud.client.test.fakes.HardwareProfileResponseFakes.HardwareProfile2Response;
import org.jboss.tools.internal.deltacloud.client.test.fakes.HardwareProfileResponseFakes.HardwareProfilesResponse;
import org.junit.Test;

/**
 * @author André Dietisheim
 */
public class HardwareProfileDomUnmarshallingTest {

	@Test
	public void HardwareProfilesCanBeUnmarshalled() throws MalformedURLException, JAXBException,
			DeltaCloudClientException {
		List<HardwareProfile> profiles = new ArrayList<HardwareProfile>();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(HardwareProfilesResponse.response.getBytes());
		new HardwareProfilesUnmarshaller().unmarshall(inputStream, profiles);
		assertEquals(2, profiles.size());
	}
	
	@Test
	public void fixedPropertyHardwareProfileMayBeUnmarshalled() throws MalformedURLException, JAXBException,
			DeltaCloudClientException {
		HardwareProfile profile = new HardwareProfile();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(HardwareProfile1Response.response.getBytes());
		new HardwareProfileUnmarshaller().unmarshall(inputStream, profile);
		assertNotNull(profile);
		assertEquals(HardwareProfile1Response.id, profile.getId());
		assertEquals(HardwareProfile1Response.propMemValue + ' ' + HardwareProfile1Response.propMemUnit,
				profile.getMemory());
		assertEquals(HardwareProfile1Response.propStorageValue + ' ' + HardwareProfile1Response.propStorageUnit,
				profile.getStorage());
		assertEquals(HardwareProfile1Response.propCPUValue, profile.getCPU());
		assertEquals(HardwareProfile1Response.propArchValue, profile.getArchitecture());
	}

	@Test
	public void mixedPropertiesHardwareProfileMayBeUnmarshalled() throws MalformedURLException, JAXBException,
			DeltaCloudClientException {
		HardwareProfile profile = new HardwareProfile();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(HardwareProfile2Response.response.getBytes());
		new HardwareProfileUnmarshaller().unmarshall(inputStream, profile);
		assertNotNull(profile);
		assertEquals(HardwareProfile2Response.id, profile.getId());
		assertEquals(HardwareProfile2Response.propMemValue + ' ' + HardwareProfile2Response.propMemUnit,
				profile.getMemory());
		Property property = profile.getNamedProperty(Property.Names.MEMORY);
		assertNotNull(property);
		assertEquals(HardwareProfile2Response.propMemRangeFirst, property.getRange().getFirst());
		assertEquals(HardwareProfile2Response.propMemRangeLast, property.getRange().getLast());
		assertEquals(HardwareProfile2Response.propStorageValue + ' ' + HardwareProfile2Response.propStorageUnit,
				profile.getStorage());
		property = profile.getNamedProperty(Property.Names.STORAGE);
		assertNotNull(property);
		assertNotNull(property.getEnums());
		assertEquals(2, property.getEnums().size());
		assertEquals(HardwareProfile2Response.propStorageEnum1, property.getEnums().get(0));
		assertEquals(HardwareProfile2Response.propStorageEnum2, property.getEnums().get(1));
		assertEquals(HardwareProfile2Response.propCPUValue, profile.getCPU());
		assertEquals(HardwareProfile2Response.propArchValue, profile.getArchitecture());
	}

}
