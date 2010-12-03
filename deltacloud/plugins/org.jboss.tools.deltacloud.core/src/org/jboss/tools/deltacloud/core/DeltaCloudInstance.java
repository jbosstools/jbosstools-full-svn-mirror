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
import org.jboss.tools.deltacloud.core.client.Instance;
import org.jboss.tools.deltacloud.core.client.Instance.InstanceState;
import org.jboss.tools.deltacloud.core.client.InstanceAction;
import org.jboss.tools.deltacloud.core.client.InternalDeltaCloudClient;

/**
 * An instance that may be reached on a DeltaCloud instance. Wraps Instance from upper layers.
 * 
 * @see Instance
 * @see DeltaCloud
 * 
 * @author Jeff Johnston
 * @author Andre Dietisheim
 */
public class DeltaCloudInstance extends AbstractDeltaCloudElement {

	public final static String PENDING = Instance.InstanceState.PENDING.toString();
	public final static String RUNNING = Instance.InstanceState.RUNNING.toString();
	public final static String STOPPED = Instance.InstanceState.STOPPED.toString();
	public final static String TERMINATED = Instance.InstanceState.TERMINATED.toString();
	public final static String BOGUS = Instance.InstanceState.BOGUS.toString();

	public final static String START = InstanceAction.START;
	public final static String STOP = InstanceAction.STOP;
	public final static String REBOOT = InstanceAction.REBOOT;
	public final static String DESTROY = InstanceAction.DESTROY;

	private Instance instance;
	private String givenName;

	public DeltaCloudInstance(DeltaCloud cloud, Instance instance) {
		super(cloud);
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
		return instance.getState() == InstanceState.STOPPED;
	}
	
	public boolean canStart() {
		return instance.canStart();
	}

	public boolean canStop() {
		return instance.canStop();
	}

	public boolean canReboot() {
		return instance.canReboot();
	}

	public boolean canDestroy() {
		return instance.canDestroy();
	}

	
	public boolean isRunning() {
		return instance.isRunning();
	}

	public String getHostName() {
		List<String> hostNames = getHostNames();
		if (hostNames != null && hostNames.size() > 0)
			return hostNames.get(0);
		return null;
	}
	
	protected boolean performInstanceAction(String actionId, InternalDeltaCloudClient client)
			throws DeltaCloudClientException {
		InstanceAction action = instance.getAction(actionId);
		if (action == null) {
			return false;
		}
		return client.performInstanceAction(action);
	}
}
