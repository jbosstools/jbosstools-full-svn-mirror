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
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DeltaCloudManager {
	
	public final static String CLOUDFILE_NAME = "clouds.xml"; //$NON-NLS-1$
	
	private static DeltaCloudManager cloudManager;
	private ArrayList<DeltaCloud> clouds = new ArrayList<DeltaCloud>();
	private ListenerList cloudManagerListeners;
	
	private DeltaCloudManager() {
		loadClouds();
	}
	
	private void loadClouds() {
		IPath stateLocation = Activator.getDefault().getStateLocation();
		File cloudFile = stateLocation.append(CLOUDFILE_NAME).toFile();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			if (cloudFile.exists()) {
				Document d = db.parse(cloudFile);
				Element e = d.getDocumentElement();
				// Get the stored configuration data
				NodeList cloudNodes = e.getElementsByTagName("cloud"); // $NON-NLS-1$
				for (int x = 0; x < cloudNodes.getLength(); ++x) {
					Node n = cloudNodes.item(x);
					NamedNodeMap attrs = n.getAttributes();
					Node nameNode = attrs.getNamedItem("name"); // $NON-NLS-1$
					Node urlNode = attrs.getNamedItem("url"); // $NON-NLS-1$
					Node usernameNode = attrs.getNamedItem("username"); // $NON-NLS-1$
					Node typeNode = attrs.getNamedItem("type"); // $NON-NLS-1$
					String name = nameNode.getNodeValue();
					String url = urlNode.getNodeValue();
					String username = usernameNode.getNodeValue();
					String type = typeNode.getNodeValue();
					String key = DeltaCloud.getPreferencesKey(url, username);
					ISecurePreferences root = SecurePreferencesFactory.getDefault();
					ISecurePreferences node = root.node(key);
					String password;
					try {
						password = node.get("password", null); //$NON-NLS-1$
						DeltaCloud cloud = new DeltaCloud(name, url, username, password, type, false);
						cloud.loadChildren();
						clouds.add(cloud);
					} catch (Exception e1) {
						Activator.log(e1);
						continue; // skip cloud
					}
				}
			} 
		} catch (ParserConfigurationException e) {
			Activator.log(e);
		} catch (SAXException e) {
			Activator.log(e);
		} catch (IOException e) {
			Activator.log(e);
		}
	}

	private void saveClouds() {
		try {
			IPath stateLocation = Activator.getDefault().getStateLocation();
			File cloudFile = stateLocation.append(CLOUDFILE_NAME).toFile();
			if (!cloudFile.exists())
				cloudFile.createNewFile();
			if (cloudFile.exists()) {
				PrintWriter p = new PrintWriter(new BufferedWriter(new FileWriter(cloudFile)));
				p.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"); //$NON-NLS-1$
				p.println("<clouds>"); // $NON-NLS-1$
				for (DeltaCloud d : clouds) {
					p.println("<cloud name=\"" + d.getName() + "\" url=\"" //$NON-NLS-1$ //$NON-NLS-2$ 
							+ d.getURL() +
							"\" username=\"" + d.getUsername() + 
							"\" type=\"" + d.getType() + "\"/>"); //$NON-NLS-1$ //$NON-NLS-2$
				}
				p.println("</clouds>"); //$NON-NLS-1$
				p.close();
			}
		} catch (Exception e) {
			Activator.log(e);
		}
	}
	
	public static DeltaCloudManager getDefault() {
		if (cloudManager == null)
			cloudManager = new DeltaCloudManager();
		return cloudManager;
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
		boolean found = false;
		for (DeltaCloud cloud : clouds) {
			if (cloud.getURL().equals(url) && cloud.getUsername().equals(userName)) {
				found = true;
				break;
			}
		}
		// if we have removed a cloud and no other cloud shares the
		// url/username combo, then we should clear the node out which
		// includes the password.
		if (!found) {
			ISecurePreferences root = SecurePreferencesFactory.getDefault();
			String key = DeltaCloud.getPreferencesKey(d.getURL(), d.getUsername());
			ISecurePreferences node = root.node(key);
			node.clear();
		}
		saveClouds();
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
