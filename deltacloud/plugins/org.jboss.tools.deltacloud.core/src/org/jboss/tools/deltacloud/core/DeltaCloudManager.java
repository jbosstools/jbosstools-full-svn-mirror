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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DeltaCloudManager {

	private static final DeltaCloudManager INSTANCE = new DeltaCloudManager(); 
	
	public final static String CLOUDFILE_NAME = "clouds.xml"; //$NON-NLS-1$
	private ArrayList<DeltaCloud> clouds = new ArrayList<DeltaCloud>();
	private ListenerList cloudManagerListeners;

	private DeltaCloudManager() {
	}

	public DeltaCloudPersistedConnectionsException loadClouds() {
		DeltaCloudPersistedConnectionsException connectionException = new DeltaCloudPersistedConnectionsException();
		IPath stateLocation = Activator.getDefault().getStateLocation();
		File cloudFile = stateLocation.append(CLOUDFILE_NAME).toFile();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			if (cloudFile.exists()) {
				Document d = db.parse(cloudFile);
				Element element = d.getDocumentElement();
				// Get the stored configuration data
				NodeList cloudNodes = element.getElementsByTagName("cloud"); // $NON-NLS-1$
				for (int x = 0; x < cloudNodes.getLength(); ++x) {
					Node n = cloudNodes.item(x);
					try {
						String name = getCloudName(n);
						loadCloud(name, n);
					} catch (StorageException e) {
						connectionException.addError(e);
					} 
				}
			}
		} catch (Exception e) {
			connectionException.addError(e);
		}
		return connectionException;
	}

	private void loadCloud(String name, Node n) throws StorageException, MalformedURLException, DeltaCloudException {
		NamedNodeMap attrs = n.getAttributes();
		String url = attrs.getNamedItem("url").getNodeValue(); // $NON-NLS-1$
		String username = attrs.getNamedItem("username").getNodeValue(); // $NON-NLS-1$
		String type = attrs.getNamedItem("type").getNodeValue(); // $NON-NLS-1$
		String imageFilterRules = getImageFilterRules(attrs.getNamedItem("imagefilter")); // $NON-NLS-1$
		String key = DeltaCloud.getPreferencesKey(url, username); // $NON-NLS-1$
		String instanceFilterRules = getInstanceFilterRules(attrs.getNamedItem("instancefilter")); // $NON-NLS-1$
		String lastKeyName = getLastKeyName(attrs.getNamedItem("lastkeyname")); // $NON-NLS-1$
		String lastImageId = getLastKeyName(attrs.getNamedItem("lastimage")); // $NON-NLS-1$
		ISecurePreferences root = SecurePreferencesFactory.getDefault();
		ISecurePreferences node = root.node(key);
		String password = node.get("password", null); //$NON-NLS-1$
		DeltaCloud cloud = new DeltaCloud(
				name, url, username, password, type, false, imageFilterRules, instanceFilterRules);
		cloud.setLastImageId(lastImageId);
		cloud.setLastKeyname(lastKeyName);
		cloud.loadChildren();
		clouds.add(cloud);
	}

	private String getCloudName(Node n) {
		String name = n.getAttributes().getNamedItem("name").getNodeValue(); // $NON-NLS-1$
		return name;
	}

	private String getLastKeyName(Node lastKeyNameNode) {
		String lastKeyName = "";
		if (lastKeyNameNode != null) {
			lastKeyName = lastKeyNameNode.getNodeValue();
		}
		return lastKeyName;
	}

	private String getInstanceFilterRules(Node instanceFilterNode) {
		String instanceFilterRules = IInstanceFilter.ALL_STRING;
		if (instanceFilterNode != null) {
			instanceFilterRules = instanceFilterNode.getNodeValue();
		}
		return instanceFilterRules;
	}

	private String getImageFilterRules(Node imageFilterNode) {
		String imageFilterRules = IImageFilter.ALL_STRING;
		if (imageFilterNode != null) {
			imageFilterRules = imageFilterNode.getNodeValue();
		}
		return imageFilterRules;
	}

	public void saveClouds() {
		try {
			File cloudFile = getOrCreateCloudFile();
			if (cloudFile.exists()) {
				PrintWriter p = new PrintWriter(new BufferedWriter(new FileWriter(cloudFile)));
				p.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"); //$NON-NLS-1$
				p.println("<clouds>"); // $NON-NLS-1$
				for (DeltaCloud d : clouds) {
					p.println(createCloudXML(d)); //$NON-NLS-1$
				}
				p.println("</clouds>"); //$NON-NLS-1$
				p.close();
			}
		} catch (Exception e) {
			Activator.log(e);
		}
	}

	private String createCloudXML(DeltaCloud d) {
		return "<cloud name=\"" + d.getName() + "\" url=\"" //$NON-NLS-1$ //$NON-NLS-2$ 
				+ d.getURL() + "\" username=\"" + d.getUsername() + //$NON-NLS-1$ //$NON-NLS-2$ 
				"\" type=\"" + d.getType() + //$NON-NLS-1$ //$NON-NLS-2$
				"\" imagefilter=\"" + d.getImageFilter() + //$NON-NLS-1$ //$NON-NLS-2$
				"\" instancefilter=\"" + d.getInstanceFilter() + //$NON-NLS-1$ //$NON-NLS-2$
				"\" lastkeyname=\"" + d.getLastKeyname() + //$NON-NLS-1$ //$NON-NLS-2$
				"\" lastimage=\"" + d.getLastImageId() + //$NON-NLS-1$ //$NON-NLS-2$
				"\"/>";
	}

	private File getOrCreateCloudFile() throws IOException {
		IPath stateLocation = Activator.getDefault().getStateLocation();
		File cloudFile = stateLocation.append(CLOUDFILE_NAME).toFile();
		if (!cloudFile.exists()) {
			// try to create a new cloud storage file
			cloudFile.createNewFile();
		}
		return cloudFile;
	}

	public static DeltaCloudManager getDefault() {
		return INSTANCE;
	}

	public DeltaCloud[] getClouds() {
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
		saveClouds();
		notifyListeners(ICloudManagerListener.ADD_EVENT);
	}

	public void removeCloud(DeltaCloud d) {
		clouds.remove(d);
		String url = d.getURL();
		String userName = d.getUsername();
		// check if we have a duplicate cloud connection using the same
		// url/username combo.
		// if we have removed a cloud and no other cloud shares the
		// url/username combo, then we should clear the node out which
		// includes the password.
		if (!isHasAnyCloud(url, userName)) {
			ISecurePreferences root = SecurePreferencesFactory.getDefault();
			String key = DeltaCloud.getPreferencesKey(url, userName);
			ISecurePreferences node = root.node(key);
			node.clear();
		}
		saveClouds();
		notifyListeners(ICloudManagerListener.REMOVE_EVENT);
	}

	/**
	 * Checks if any cloud uses the given url and username
	 * 
	 * @param url
	 *            the url
	 * @param userName
	 *            the user name
	 * @return true, if is checks for any cloud
	 */
	private boolean isHasAnyCloud(String url, String userName) {
		for (DeltaCloud cloud : clouds) {
			if (cloud.getURL().equals(url) && cloud.getUsername().equals(userName)) {
				return true;
			}
		}
		return false;
	}

	public void notifyCloudRename() {
		saveClouds();
		notifyListeners(ICloudManagerListener.RENAME_EVENT);
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
				((ICloudManagerListener) listeners[i]).changeEvent(type);
			}
		}
	}

}
