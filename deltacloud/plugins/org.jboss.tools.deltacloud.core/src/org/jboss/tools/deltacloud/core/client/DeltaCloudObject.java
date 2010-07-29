package org.jboss.tools.deltacloud.core.client;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;

public abstract class DeltaCloudObject implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	protected String id;
	
	@SuppressWarnings("unused")
	private void setId(String id) 
	{
		this.id = id;
	}
	
	@XmlAttribute
	public String getId() 
	{
		return id;
	}
}
