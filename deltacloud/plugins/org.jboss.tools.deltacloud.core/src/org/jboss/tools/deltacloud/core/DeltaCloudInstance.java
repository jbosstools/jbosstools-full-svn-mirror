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

import org.jboss.tools.deltacloud.core.client.DeltaCloudClientException;
import org.jboss.tools.deltacloud.core.client.DeltaCloudClientImpl;
import org.jboss.tools.deltacloud.core.client.Instance;
import org.jboss.tools.deltacloud.core.client.Instance.State;
import org.jboss.tools.deltacloud.core.client.InstanceAction;

public class DeltaCloudInstance {

	public final static String PENDING = Instance.State.PENDING.toString();
	public final static String RUNNING = Instance.State.RUNNING.toString();
	public final static String STOPPED = Instance.State.STOPPED.toString();
	public final static String TERMINATED = Instance.State.TERMINATED.toString();
	public final static String BOGUS = Instance.State.BOGUS.toString();

	public final static String START = InstanceAction.START;
	public final static String STOP = InstanceAction.STOP;
	public final static String REBOOT = InstanceAction.REBOOT;
	public final static String DESTROY = InstanceAction.DESTROY;

	private DeltaCloud cloud;
	private Instance instance;
	private String givenName;

	public DeltaCloudInstance(DeltaCloud cloud, Instance instance) {
		this.cloud = cloud;
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

	public boolean isStopped() {
		return instance.getState() == State.STOPPED;
	}
	
	public String getHostName() {
		List<String> hostNames = getHostNames();
		if (hostNames != null && hostNames.size() > 0)
			return hostNames.get(0);
		return null;
	}

	public DeltaCloud getDeltaCloud() {
		return cloud;
	}
	
	protected boolean performInstanceAction(String actionId, DeltaCloudClientImpl client)
			throws DeltaCloudClientException {
		InstanceAction action = instance.getAction(actionId);
		if (action == null) {
			return false;
		}
		return client.performInstanceAction(action);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((givenName == null) ? 0 : givenName.hashCode());
		result = prime * result + ((instance == null) ? 0 : instance.hashCode());
		return result;
	}

	/**
	 * The current strategy regarding instances is to create new instances (and
	 * not update instances). We therefore need equals to be able to match
	 * domain objects. We might have to change that since in my experience it is
	 * not a good choice to create new instances, better is to update the ones
	 * that are available in the client.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeltaCloudInstance other = (DeltaCloudInstance) obj;
		if (givenName == null) {
			if (other.givenName != null)
				return false;
		} else if (!givenName.equals(other.givenName))
			return false;
		if (instance == null) {
			if (other.instance != null)
				return false;
		} else if (!instance.equals(other.instance))
			return false;
		return true;
	}
}
