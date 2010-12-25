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

import org.jboss.tools.deltacloud.core.client.DeltaCloudClientException;
import org.jboss.tools.deltacloud.core.client.IInstanceAction;
import org.jboss.tools.deltacloud.core.client.Instance;
import org.jboss.tools.deltacloud.core.client.Instance.InstanceState;
import org.jboss.tools.deltacloud.core.client.InstanceAction;
import org.jboss.tools.deltacloud.core.client.InternalDeltaCloudClient;

/**
 * An instance that may be reached on a DeltaCloud instance. Wraps Instance from
 * upper layers.
 * 
 * @see Instance
 * @see DeltaCloud
 * 
 * @author Jeff Johnston
 * @author Andre Dietisheim
 */
public class DeltaCloudInstance extends AbstractDeltaCloudElement {

	public enum State {

		PENDING(Instance.InstanceState.PENDING.name()),
		RUNNING(Instance.InstanceState.RUNNING.name()),
		STOPPED(Instance.InstanceState.STOPPED.name()),
		TERMINATED(Instance.InstanceState.TERMINATED.name()),
		BOGUS(Instance.InstanceState.BOGUS.name());

		private String name;

		private State(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public boolean equals(String state) {
			return name.equals(state);
		}
	}

	public enum Action {

		START(IInstanceAction.START),
		STOP(IInstanceAction.STOP),
		REBOOT(IInstanceAction.REBOOT),
		DESTROY(InstanceAction.DESTROY);

		private String name;

		private Action(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

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

	public State getState() {
		return State.valueOf(instance.getState().name());
	}

	public DeltaCloudKey getKey() throws DeltaCloudException {
		// TODO: cache keys locally
		DeltaCloud cloud = getDeltaCloud();
		return cloud.getKey(instance.getKeyId());
	}

	public String getKeyId() {
		return instance.getKeyId();
	}
	
	public List<Action> getActions() {
		List<Action> actions = new ArrayList<Action>();
		for (InstanceAction action : instance.getActions()) {
			actions.add(Action.valueOf(action.getName()));
		}
		return actions;
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
	
	protected void setInstance(Instance instance) {
		this.instance = instance;
	}

	public String getHostName() {
		List<String> hostNames = getHostNames();
		String hostName = null;
		if (hostNames != null && hostNames.size() >= 1) {
			hostName = hostNames.get(0);
		}
		return hostName;
	}

	protected boolean performInstanceAction(Action action, InternalDeltaCloudClient client)
			throws DeltaCloudClientException {
		InstanceAction instanceAction = instance.getAction(action.getName());
		if (instanceAction == null) {
			return false;
		}
		return client.performInstanceAction(instanceAction);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("DeltaCloudInstance");
		builder.append(" [");
		appendActions(builder);
		builder.append("id: ").append(getId())
				.append("givenName: ").append(givenName)
				.append("name: ").append(getName())
				.append("hostName: ").append(getHostName())
				.append("imageId: ").append(getImageId())
				.append("key: ").append(getKeyId())
				.append("ownerId: ").append(getOwnerId())
				.append("profileId: ").append(getProfileId())
				.append("realmId: ").append(getRealmId())
				.append("state: ").append(getState());
		builder.append("]");
		return builder.toString();
	}

	private StringBuilder appendActions(StringBuilder builder) {
		builder.append(" actions: [");
		for (Action action : getActions()) {
			builder.append("action: ")
					.append(action.getName());
		}
		builder.append("] ");
		return builder;
	}
}
