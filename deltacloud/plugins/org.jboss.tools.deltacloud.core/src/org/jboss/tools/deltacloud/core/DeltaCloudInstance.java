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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.deltacloud.client.Action;
import org.apache.deltacloud.client.DeltaCloudClient;
import org.apache.deltacloud.client.DeltaCloudClientException;
import org.apache.deltacloud.client.Instance;

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

		PENDING(Instance.State.PENDING.name()),
		RUNNING(Instance.State.RUNNING.name()),
		STOPPED(Instance.State.STOPPED.name()),
		TERMINATED(Instance.State.TERMINATED.name()),
		BOGUS(Instance.State.BOGUS.name());

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

	private Instance instance;
	private String alias;

	public DeltaCloudInstance(Instance instance, DeltaCloud cloud) {
		super(cloud);
		this.instance = instance;
	}

	public String getName() {
		return instance.getName();
	}

	public String getAlias() {
		if (alias == null || alias.length() == 0) {
			return instance.getName();
		}
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
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

	public List<DeltaCloudResourceAction> getActions() {
		List<DeltaCloudResourceAction> deltaCloudActions = new ArrayList<DeltaCloudResourceAction>();
		for (Action<Instance> action : instance.getActions()) {
			DeltaCloudResourceAction deltaCloudAction = DeltaCloudResourceAction.getByName(action.getName());
			if (deltaCloudAction != null) {
				deltaCloudActions.add(deltaCloudAction);
			}
		}
		return deltaCloudActions;
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
		return instance.isStopped();
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

	protected boolean performAction(DeltaCloudResourceAction action, DeltaCloudClient client)
			throws DeltaCloudClientException {
		Action<Instance> instanceAction = instance.getAction(action.getName());
		if (instanceAction == null) {
			return false;
		}
		InputStream in = client.performAction(instanceAction);
		return in != null;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("DeltaCloudInstance");
		builder.append(" [");
		appendActions(builder);
		builder.append("id: ").append(getId())
				.append("givenName: ").append(alias)
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
		for (DeltaCloudResourceAction action : getActions()) {
			builder.append("action: ")
					.append(action.getName());
		}
		builder.append("] ");
		return builder;
	}

	public boolean isInState(State state) {
		return getState() == state;
	}
}
