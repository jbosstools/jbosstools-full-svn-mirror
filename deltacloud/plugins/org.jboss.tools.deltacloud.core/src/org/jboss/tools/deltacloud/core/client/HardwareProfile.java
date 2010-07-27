package org.jboss.tools.deltacloud.core.client;

import java.util.ArrayList;
import java.util.List;

public class HardwareProfile extends DeltaCloudObject
{
	private static final long serialVersionUID = 1L;

	private List<Property> properties;
	
	private HardwareProfile()
	{	
	}

	public List<Property> getProperties() {
		if (properties == null)
			properties = new ArrayList<Property>();
		return properties;
	}
	
	private Property getNamedProperty(String name) {
		for (Property p : properties) {
			if (p.getName().equals(name))
				return p;
		}
		return null;
	}
	
	public String getArchitecture()
	{
		Property p = getNamedProperty("architecture");
		if (p != null)
			return p.toString();
		return null;
	}

	public String getMemory()
	{
		Property p = getNamedProperty("memory");
		if (p != null)
			return p.toString();
		return null;
	}
	
	public String getStorage()
	{
		Property p = getNamedProperty("storage");
		if (p != null)
			return p.toString();
		return null;
	}

	public String getCPU()
	{
		Property p = getNamedProperty("cpu");
		if (p != null)
			return p.toString();
		return null;
	}
	
	
	@Override
	public String toString()
	{
		String s = "";
		s += "Hardware-profile:\t\t" + getId() + "\n";
		for (Property p : properties) {
			s += p.getName() + ":\t\t" + p.getValue() + "\n";
		}
		return s;
	}
}
