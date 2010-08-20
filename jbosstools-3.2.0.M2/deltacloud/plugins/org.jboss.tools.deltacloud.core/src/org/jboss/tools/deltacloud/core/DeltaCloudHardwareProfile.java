package org.jboss.tools.deltacloud.core;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.deltacloud.core.client.HardwareProfile;
import org.jboss.tools.deltacloud.core.client.Property;

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
