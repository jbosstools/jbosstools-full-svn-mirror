package org.jboss.tools.deltacloud.core;

import org.jboss.tools.deltacloud.core.client.Realm;

public class DeltaCloudRealm {
	
	private Realm realm;
	
	public DeltaCloudRealm(Realm realm) {
		this.realm = realm;
	}
	
	public String getId() {
		return realm.getId();
	}
	
	public String getName() {
		return realm.getName();
	}
	
	public String getState() {
		return realm.getState();
	}

}
