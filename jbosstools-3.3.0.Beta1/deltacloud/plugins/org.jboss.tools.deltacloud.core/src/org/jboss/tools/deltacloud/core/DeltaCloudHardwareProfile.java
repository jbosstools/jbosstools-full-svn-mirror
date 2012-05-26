/*******************************************************************************
 * Copyright (c) 2010 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Incorporated - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.deltacloud.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.deltacloud.client.HardwareProfile;
import org.apache.deltacloud.client.Property;

/**
 * @author Jeff Johnston
 */
public class DeltaCloudHardwareProfile {

	private HardwareProfile profile;
	
	public DeltaCloudHardwareProfile(HardwareProfile profile) {
		this.profile = profile;
	}
	
	public String getId() {
		return profile.getId();
	}
	
	public String getArchitecture() {
		return profile.getArchitecture();
	}
	
	public List<DeltaCloudHardwareProperty> getProperties() {
		ArrayList<DeltaCloudHardwareProperty> properties = new ArrayList<DeltaCloudHardwareProperty>();
		List<Property> clientProperties = profile.getProperties();
		for (Property p : clientProperties) {
			DeltaCloudHardwareProperty hwp = new DeltaCloudHardwareProperty(p);
			properties.add(hwp);
		}
		return properties;
	}

	public DeltaCloudHardwareProperty getNamedProperty(String name) {
		List<Property> clientProperties = profile.getProperties();
		for (Property p : clientProperties) {
			if (p.getName().equals(name))
				return new DeltaCloudHardwareProperty(p);
			}
		return null;
	}
	
}
