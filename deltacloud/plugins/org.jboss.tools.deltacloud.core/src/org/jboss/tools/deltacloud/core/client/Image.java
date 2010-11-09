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

import javax.xml.bind.annotation.XmlElement;

public class Image extends DeltaCloudObject
{	
	private static final long serialVersionUID = 1L;

	@XmlElement(name="owner_id")
	private String ownerId;
	
	@XmlElement
	private String name;
	
	@XmlElement
	private String description;
	
	@XmlElement
	private String architecture;
	
	private Image()
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

	@SuppressWarnings("unused")
	private void setDescription(String description)
	{
		this.description = description;
	}

	@SuppressWarnings("unused")
	private void setArchitecture(String architecture)
	{
		this.architecture = architecture;
	}
	
	public String getOwnerId() 
	{
		return ownerId;
	}

	public String getName()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}

	public String getArchitecture()
	{
		return architecture;
	}
	
	@Override
	public String toString()
	{
		String s = "";
		s += "Image:\t\t" + getId() + "\n";
		s += "Owner:\t\t" + getOwnerId() + "\n";
		s += "Name:\t\t" + getName() + "\n";
		s += "Desc:\t\t" + getDescription() + "\n";
		s += "Arch:\t\t" + getArchitecture() + "\n";
		return s;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((architecture == null) ? 0 : architecture.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((ownerId == null) ? 0 : ownerId.hashCode());
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
		Image other = (Image) obj;
		if (architecture == null) {
			if (other.architecture != null)
				return false;
		} else if (!architecture.equals(other.architecture))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
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
		return true;
	}
	
	
}
