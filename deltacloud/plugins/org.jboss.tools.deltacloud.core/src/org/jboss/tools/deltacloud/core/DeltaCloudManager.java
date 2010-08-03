package org.jboss.tools.deltacloud.core;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.eclipse.core.runtime.ListenerList;

public class DeltaCloudManager {
	
	private static DeltaCloudManager cloudManager;
	private ArrayList<DeltaCloud> clouds = new ArrayList<DeltaCloud>();
	private ListenerList cloudManagerListeners;
	
	private DeltaCloudManager() {
	}
	
	public static DeltaCloudManager getDefault() {
		if (cloudManager == null)
			cloudManager = new DeltaCloudManager();
		return cloudManager;
	}
	
	public DeltaCloud[] getClouds() {
		// FIXME: testing only
//		if (clouds.size() == 0) {
//			try {
//				DeltaCloud x = new DeltaCloud("Red Hat Cloud", new URL("http://localhost:3001/api"), "mockuser", "mockpassword");
//				addCloud(x);
//			} catch (MalformedURLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		return clouds.toArray(new DeltaCloud[clouds.size()]);
	}

	public DeltaCloud findCloud(String name) {
		for (DeltaCloud cloud : clouds) {
			if (cloud.getName().equals(name))
				return cloud;
		}
		return null;
	}
	
	public void addCloud(DeltaCloud d) {
		clouds.add(d);
		notifyListeners(ICloudManagerListener.ADD_EVENT);
	}
	
	public void removeCloud(DeltaCloud d) {
		clouds.remove(d);
		notifyListeners(ICloudManagerListener.REMOVE_EVENT);
	}
	
	public void addCloudManagerListener(ICloudManagerListener listener) {
		if (cloudManagerListeners == null)
			cloudManagerListeners = new ListenerList(ListenerList.IDENTITY);
		cloudManagerListeners.add(listener);
	}

	public void removeCloudManagerListener(ICloudManagerListener listener) {
		if (cloudManagerListeners != null)
			cloudManagerListeners.remove(listener);
	}

	public void notifyListeners(int type) {
		if (cloudManagerListeners != null) {
			Object[] listeners = cloudManagerListeners.getListeners();
			for (int i = 0; i < listeners.length; ++i) {
				((ICloudManagerListener)listeners[i]).changeEvent(type);
			}
		}
	}
	
}
