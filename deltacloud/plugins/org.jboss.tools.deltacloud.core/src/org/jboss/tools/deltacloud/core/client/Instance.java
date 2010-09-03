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

public class Instance extends DeltaCloudObject
{	
	private static final long serialVersionUID = 1L;
	
	public static enum State { RUNNING, STOPPED, PENDING, TERMINATED, BOGUS };
	
	public static enum Action { START, STOP, REBOOT, DESTROY };
	
	@XmlElement(name="owner_id")
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
	
	private List<Action> actions;
	
	@XmlElement(name="public_addresses")
	private AddressList publicAddresses;
	
	@XmlElement(name="private_addresses")
	private AddressList privateAddresses;
	
	private Instance()
	{
	}

	@SuppressWarnings("unused")
	private void setOwnerId(String ownerId)
	{
		this.ownerId = ownerId;
	}
	
	@SuppressWarnings("unused")
	private void setName(String name)
	{
		this.name = name;
	}
	
	protected void setImageId(String imageId)
	{
		this.imageId = imageId;
	}
	
	protected void setProfileId(String profileId)
	{
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
	
	protected void setRealmId(String realmId)
	{
		this.realmId = realmId;
	}
	
	protected void setActions(List<Action> actions)
	{
		this.actions = actions;
	}
	
	protected void setState(String state)
	{
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
	private void setPrivateAddresses(AddressList privateAddresses)
	{
		this.privateAddresses = privateAddresses;
	}
	
	@SuppressWarnings("unused")
	private void setPublicAddresses(AddressList publicAddresses)
	{
		this.publicAddresses = publicAddresses;
	}
	
	public String getOwnerId()
	{
		return ownerId;
	}

	public String getName()
	{
		return name;
	}

	public String getImageId()
	{
		return imageId;
	}

	public String getProfileId()
	{
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

	public String getRealmId()
	{
		return realmId;
	}

	public State getState()
	{
		return state;
	}
	
	public List<Action> getActions()
	{
		return actions;
	}
	
	public List<String> getActionNames()
	{
		ArrayList<String>names = new ArrayList<String>();
		for (Action action : actions)
		{
			names.add(action.name());
		}
		return names;
	}
	
	public List<String> getPublicAddresses()
	{
		return publicAddresses.getAddress();
	}

	public List<String> getPrivateAddresses()
	{
		return privateAddresses.getAddress();
	}	
	
	@Override
	public String toString()
	{
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
		
		
		for(int i = 0; i < actions.size(); i ++)
		{
			if(i == 0)
			{
				s += "Actions:\t" + actions.get(i)  + "\n";
			}
			else
			{
				s += "\t\t" + actions.get(i) + "\n";
			}
		}
		
		
		for(int i = 0; i < publicAddresses.getAddress().size(); i ++)
		{
			if(i == 0)
			{
				s += "Public Addr:\t" + publicAddresses.getAddress().get(i) + "\n";
			}
			else
			{
				s += "\t\t" + publicAddresses.getAddress().get(i) + "\n";
			}
		}
		
		for(int i = 0; i < publicAddresses.getAddress().size(); i ++)
		{
			if(i == 0)
			{
				s += "Private Addr:\t" + publicAddresses.getAddress().get(i) + "\n";
			}
			else
			{
				s += "\t\t" + privateAddresses.getAddress().get(i) + "\n";
			}
		}
		
		return s;
	}
}
