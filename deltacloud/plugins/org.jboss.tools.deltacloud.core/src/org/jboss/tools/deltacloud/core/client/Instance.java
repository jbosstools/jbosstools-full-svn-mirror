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

import javax.xml.bind.annotation.XmlElement;

public class Instance extends DeltaCloudObject {

	private static final long serialVersionUID = 1L;

	public static enum State {
		RUNNING, STOPPED, PENDING, TERMINATED, BOGUS
	};

	@XmlElement(name = "owner_id")
	private String ownerId;

	@XmlElement
	private String name;

	private String imageId;

	private String profileId;

	private String memory;

	private String storage;

	private String cpu;

	private String realmId;

	private String keyname;

	@XmlElement
	private State state;

	private List<InstanceAction> actions;

	@XmlElement(name = "public_addresses")
	private AddressList publicAddresses;

	@XmlElement(name = "private_addresses")
	private AddressList privateAddresses;

	private Instance() {
	}

	@SuppressWarnings("unused")
	private void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	@SuppressWarnings("unused")
	private void setName(String name) {
		this.name = name;
	}

	protected void setImageId(String imageId) {
		this.imageId = imageId;
	}

	protected void setProfileId(String profileId) {
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

	protected void setRealmId(String realmId) {
		this.realmId = realmId;
	}

	protected void setActions(List<InstanceAction> actions) {
		this.actions = actions;
	}

	protected void setState(String state) {
		try {
			this.state = State.valueOf(state);
		} catch (Exception e) {
			this.state = State.BOGUS;
		}
	}

	public void setKey(String keyname) {
		this.keyname = keyname;
	}

	public String getKey() {
		return keyname;
	}

	@SuppressWarnings("unused")
	private void setPrivateAddresses(AddressList privateAddresses) {
		this.privateAddresses = privateAddresses;
	}

	@SuppressWarnings("unused")
	private void setPublicAddresses(AddressList publicAddresses) {
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

	public State getState() {
		return state;
	}

	public List<InstanceAction> getActions() {
		return actions;
	}

	public List<String> getActionNames() {
		ArrayList<String> names = new ArrayList<String>();
		for (InstanceAction action : actions) {
			names.add(action.getName());
		}
		return names;
	}

	public InstanceAction getAction(String name) {
		if (name == null) {
			return null;
		}

		for (InstanceAction action : actions) {
			if (name.equals(action.getName())) {
				return action;
			}
		}
		return null;
	}

	public boolean canStart() {
		return getAction(InstanceAction.START) != null;
	}
	
	public boolean canStop() {
		return getAction(InstanceAction.STOP) != null;
	}

	public boolean canReboot() {
		return getAction(InstanceAction.REBOOT) != null;
	}

	public boolean canDestroy() {
		return getAction(InstanceAction.DESTROY) != null;
	}

	public List<String> getPublicAddresses() {
		return publicAddresses.getAddress();
	}

	public List<String> getPrivateAddresses() {
		return privateAddresses.getAddress();
	}

	public boolean start(DeltaCloudClient client) throws DeltaCloudClientException {
		return ((InternalDeltaCloudClient) client).performInstanceAction(getAction(InstanceAction.START));
	}

	public boolean stop(DeltaCloudClient client) throws DeltaCloudClientException {
		return ((InternalDeltaCloudClient) client).performInstanceAction(getAction(InstanceAction.STOP));
	}

	public boolean destroy(DeltaCloudClient client) throws DeltaCloudClientException {
		return ((InternalDeltaCloudClient) client).performInstanceAction(getAction(InstanceAction.DESTROY));
	}

	public boolean reboot(DeltaCloudClient client) throws DeltaCloudClientException {
		return ((InternalDeltaCloudClient) client).performInstanceAction(getAction(InstanceAction.REBOOT));
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

		for (int i = 0; i < actions.size(); i++) {
			if (i == 0) {
				s += "Actions:\t" + actions.get(i) + "\n";
			} else {
				s += "\t\t" + actions.get(i) + "\n";
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cpu == null) ? 0 : cpu.hashCode());
		result = prime * result + ((imageId == null) ? 0 : imageId.hashCode());
		result = prime * result + ((keyname == null) ? 0 : keyname.hashCode());
		result = prime * result + ((memory == null) ? 0 : memory.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((ownerId == null) ? 0 : ownerId.hashCode());
		result = prime * result + ((profileId == null) ? 0 : profileId.hashCode());
		result = prime * result + ((realmId == null) ? 0 : realmId.hashCode());
		result = prime * result + ((storage == null) ? 0 : storage.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Instance other = (Instance) obj;
		if (cpu == null) {
			if (other.cpu != null)
				return false;
		} else if (!cpu.equals(other.cpu))
			return false;
		if (imageId == null) {
			if (other.imageId != null)
				return false;
		} else if (!imageId.equals(other.imageId))
			return false;
		if (keyname == null) {
			if (other.keyname != null)
				return false;
		} else if (!keyname.equals(other.keyname))
			return false;
		if (memory == null) {
			if (other.memory != null)
				return false;
		} else if (!memory.equals(other.memory))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (ownerId == null) {
			if (other.ownerId != null)
				return false;
		} else if (!ownerId.equals(other.ownerId))
			return false;
		if (profileId == null) {
			if (other.profileId != null)
				return false;
		} else if (!profileId.equals(other.profileId))
			return false;
		if (realmId == null) {
			if (other.realmId != null)
				return false;
		} else if (!realmId.equals(other.realmId))
			return false;
		if (storage == null) {
			if (other.storage != null)
				return false;
		} else if (!storage.equals(other.storage))
			return false;
		return true;
	}
}
