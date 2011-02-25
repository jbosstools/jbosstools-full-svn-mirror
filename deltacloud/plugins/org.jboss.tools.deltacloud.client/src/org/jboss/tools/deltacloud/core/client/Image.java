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


/**
 * @author Martyn Taylor
 */
public class Image extends IdAware
{	
	private static final long serialVersionUID = 1L;

	private String ownerId;
	
	private String name;
	
	private String description;
	
	private String architecture;
	
	public Image()
	{
	}
		
	public void setOwnerId(String ownerId)
	{
		this.ownerId = ownerId;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public void setArchitecture(String architecture)
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

	public String getDescription() {
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
}
