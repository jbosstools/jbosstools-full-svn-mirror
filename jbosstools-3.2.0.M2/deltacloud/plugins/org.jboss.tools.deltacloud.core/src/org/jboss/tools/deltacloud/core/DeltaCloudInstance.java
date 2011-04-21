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

import java.util.List;

import org.jboss.tools.deltacloud.core.client.Instance;


public class DeltaCloudInstance {

	public final static String PENDING = Instance.State.PENDING.toString();
	public final static String RUNNING = Instance.State.RUNNING.toString();
	public final static String STOPPED = Instance.State.STOPPED.toString();
	public final static String TERMINATED = Instance.State.TERMINATED.toString();
	public final static String BOGUS = Instance.State.BOGUS.toString();
	
	public final static String START = Instance.Action.START.toString();
	public final static String STOP = Instance.Action.STOP.toString();
	public final static String REBOOT = Instance.Action.REBOOT.toString();
	public final static String DESTROY = Instance.Action.DESTROY.toString();
	
	public final static String EC2_TYPE = "EC2"; //$NON-NLS-1$
	public final static String MOCK_TYPE = "MOCK"; //$NON-NLS-1$
	
	private Instance instance;
	private String givenName;
	
	public DeltaCloudInstance(Instance instance) {
		this.instance = instance;
	}
	
	public String getName() {
		return instance.getName();
	}
	
	public String getGivenName() {
		return givenName;
	}
	
	public void setGivenName(String name) {
		givenName = name;
	}
	
	public String getId() {
		return instance.getId();
	}
	
	public String getOwnerId() {
		return instance.getOwnerId();
	}
	
	public String getState() {
		return instance.getState().toString();
	}
	
	public String getKey() {
		return instance.getKey();
	}
	
	public List<String> getActions() {
		return instance.getActionNames();
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
