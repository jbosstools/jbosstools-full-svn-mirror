package org.jboss.tools.deltacloud.core;

import java.util.List;

import org.jboss.tools.deltacloud.core.client.Instance;


public class DeltaCloudInstance {

	public final static String PENDING = Instance.State.PENDING.toString();
	public final static String RUNNING = Instance.State.RUNNING.toString();
	public final static String STOPPED = Instance.State.STOPPED.toString();
	
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
	
	public String getProfileId() {
		return instance.getProfileId();
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
	
	public String getHostName() {
		List<String> hostNames = getHostNames();
		if (hostNames != null && hostNames.size() > 0)
			return hostNames.get(0);
		return null;
	}
}
