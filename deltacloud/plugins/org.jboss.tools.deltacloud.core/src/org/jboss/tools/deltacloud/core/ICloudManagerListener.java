package org.jboss.tools.deltacloud.core;

public interface ICloudManagerListener {
	
	public static int ADD_EVENT = 1;
	public static int REMOVE_EVENT = 2;
	
	void changeEvent(int type);
}
