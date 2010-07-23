package org.jboss.tools.deltacloud.core;

import java.util.List;

import org.jboss.deltacloud.client.Instance;

public class DeltaCloudInstance {

	private Instance instance;
	
	public DeltaCloudInstance(Instance instance) {
		this.instance = instance;
	}
	
	public String getName() {
		return instance.getName();
	}
	
	public String getId() {
		return instance.getId();
	}
	
	public String getState() {
		return instance.getState().toString();
	}
	
	public String getFlavorId() {
		return instance.getFlavorId();
	}
	
	public String getRealmId() {
		return instance.getRealmId();
	}
	
	public String getImageId() {
		return instance.getImageId();
	}
	
	public List<String> getHostNames() {
		return instance.getPublicAddresses();
	}
}
