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
package org.jboss.tools.deltacloud.core.client;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Martyn Taylor
 * @author Andre Dietisheim
 */
public class Instance extends AbstractDeltaCloudActionAwareObject<InstanceAction> {

	private static final long serialVersionUID = 1L;

	public static enum InstanceState {
		RUNNING, STOPPED, PENDING, TERMINATED, BOGUS
	};

	private String ownerId;

	private String name;

	private String imageId;

	private String profileId;

	private String memory;

	private String storage;

	private String cpu;

	private String realmId;

	private String keyId;

	private InstanceState state;

	private AddressList publicAddresses;

	private AddressList privateAddresses;

	public Instance() {
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	protected void setMemory(String memory) {
		this.memory = memory;
	}

	protected void setStorage(String storage) {
		this.storage = storage;
	}

	protected void setCPU(String cpu) {
		this.cpu = cpu;
	}

	public void setRealmId(String realmId) {
		this.realmId = realmId;
	}

	public void setState(String state) {
		try {
			this.state = InstanceState.valueOf(state);
		} catch (Exception e) {
			this.state = InstanceState.BOGUS;
		}
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public String getKeyId() {
		return keyId;
	}

	public void setPrivateAddresses(AddressList privateAddresses) {
		this.privateAddresses = privateAddresses;
	}

	public void setPublicAddresses(AddressList publicAddresses) {
		this.publicAddresses = publicAddresses;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public String getName() {
		return name;
	}

	public String getImageId() {
		return imageId;
	}

	public String getProfileId() {
		return profileId;
	}

	public String getMemory() {
		return memory;
	}

	public String getStorage() {
		return storage;
	}

	public String getCPU() {
		return cpu;
	}

	public String getRealmId() {
		return realmId;
	}

	public InstanceState getState() {
		return state;
	}

	public List<String> getActionNames() {
		ArrayList<String> names = new ArrayList<String>();
		for (InstanceAction action : getActions()) {
			names.add(action.getName());
		}
		return names;
	}

	public InstanceAction getAction(String name) {
		if (name == null) {
			return null;
		}

		for (InstanceAction action : getActions()) {
			if (name.equals(action.getName())) {
				return action;
			}
		}
		return null;
	}

	public boolean canStart() {
		return getAction(IInstanceAction.START) != null;
	}

	public boolean canStop() {
		return getAction(IInstanceAction.STOP) != null;
	}

	public boolean canReboot() {
		return getAction(IInstanceAction.REBOOT) != null;
	}

	public boolean canDestroy() {
		return getAction(InstanceAction.DESTROY) != null;
	}

	public boolean isRunning() {
		return getState() == InstanceState.RUNNING;
	}

	public List<String> getPublicAddresses() {
		return publicAddresses.getAddress();
	}

	public List<String> getPrivateAddresses() {
		return privateAddresses.getAddress();
	}

	public boolean start(DeltaCloudClient client) throws DeltaCloudClientException {
		return ((InternalDeltaCloudClient) client).performInstanceAction(getAction(IInstanceAction.START));
	}

	public boolean stop(DeltaCloudClient client) throws DeltaCloudClientException {
		return ((InternalDeltaCloudClient) client).performInstanceAction(getAction(IInstanceAction.STOP));
	}

	public boolean destroy(DeltaCloudClient client) throws DeltaCloudClientException {
		return ((InternalDeltaCloudClient) client).performInstanceAction(getAction(InstanceAction.DESTROY));
	}

	public boolean reboot(DeltaCloudClient client) throws DeltaCloudClientException {
		return ((InternalDeltaCloudClient) client).performInstanceAction(getAction(IInstanceAction.REBOOT));
	}

	@Override
	public String toString() {
		String s = "";
		s += "Instance:\t" + getId() + "\n";
		s += "Owner:\t\t" + getOwnerId() + "\n";
		s += "Image:\t\t" + getImageId() + "\n";
		s += "Realm:\t\t" + getRealmId() + "\n";
		s += "Profile:\t\t" + getProfileId() + "\n";
		if (getMemory() != null)
			s += "Memory:\t\t" + getMemory() + "\n";
		if (getStorage() != null) {
			s += "Storage:\t\t" + getStorage() + "\n";
		}
		if (getCPU() != null) {
			s += "CPU:\t\t" + getCPU() + "\n";
		}
		s += "State:\t\t" + getState() + "\n";

		List<InstanceAction> actions = getActions();
		if (actions != null) {
			for (int i = 0; i < actions.size(); i++) {
				if (i == 0) {
					s += "Actions:\t" + actions.get(i) + "\n";
				} else {
					s += "\t\t" + actions.get(i) + "\n";
				}
			}
		}

		for (int i = 0; i < publicAddresses.getAddress().size(); i++) {
			if (i == 0) {
				s += "Public Addr:\t" + publicAddresses.getAddress().get(i) + "\n";
			} else {
				s += "\t\t" + publicAddresses.getAddress().get(i) + "\n";
			}
		}

		for (int i = 0; i < publicAddresses.getAddress().size(); i++) {
			if (i == 0) {
				s += "Private Addr:\t" + publicAddresses.getAddress().get(i) + "\n";
			} else {
				s += "\t\t" + privateAddresses.getAddress().get(i) + "\n";
			}
		}

		return s;
	}
}
