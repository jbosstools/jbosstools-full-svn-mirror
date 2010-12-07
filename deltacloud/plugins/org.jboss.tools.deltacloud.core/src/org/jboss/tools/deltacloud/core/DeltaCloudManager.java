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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.ListenerList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DeltaCloudManager {

	private static final DeltaCloudManager INSTANCE = new DeltaCloudManager();

	public final static String CLOUDFILE_NAME = "clouds.xml"; //$NON-NLS-1$
	private List<DeltaCloud> clouds;
	private ListenerList cloudManagerListeners;

	private DeltaCloudManager() {
	}

	private void loadClouds() throws DeltaCloudException {
		this.clouds = new ArrayList<DeltaCloud>(); // clear present clouds
		DeltaCloudMultiException connectionException = new DeltaCloudMultiException("Could not load some clouds");
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
						loadCloud(n, clouds);
					} catch (DeltaCloudException e) {
						connectionException.addError(e);
					}
				}
			}
		} catch (Exception e) {
			connectionException.addError(e);
		}

		if (!connectionException.isEmpty()) {
			throw connectionException;
		}
	}

	private DeltaCloud loadCloud(Node n, List<DeltaCloud> clouds) throws DeltaCloudException {
		String name = "<UNKNOWN>";
		try {
			DeltaCloud cloud = null;
			NamedNodeMap attrs = n.getAttributes();
			name = attrs.getNamedItem("name").getNodeValue(); // $NON-NLS-1$
			String url = attrs.getNamedItem("url").getNodeValue(); // $NON-NLS-1$
			String username = attrs.getNamedItem("username").getNodeValue(); // $NON-NLS-1$
			String type = attrs.getNamedItem("type").getNodeValue(); // $NON-NLS-1$
			String imageFilterRules = getImageFilterRules(attrs.getNamedItem("imagefilter")); // $NON-NLS-1$
			String instanceFilterRules = getInstanceFilterRules(attrs.getNamedItem("instancefilter")); // $NON-NLS-1$
			String lastKeyName = getLastKeyName(attrs.getNamedItem("lastkeyname")); // $NON-NLS-1$
			String lastImageId = getLastKeyName(attrs.getNamedItem("lastimage")); // $NON-NLS-1$
			cloud = new DeltaCloud(name, url, username, type, imageFilterRules, instanceFilterRules);
			clouds.add(cloud);
			cloud.setLastImageId(lastImageId);
			cloud.setLastKeyname(lastKeyName);
			return cloud;
		} catch (DeltaCloudException e) {
			throw e;
		} catch (Exception e) {
			// TODO: internationalize strings
			throw new DeltaCloudException(MessageFormat.format("Could not load cloud {0}", name), e);
		}
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

	public void saveClouds() throws DeltaCloudException {
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
			// TODO: internationalize strings
			throw new DeltaCloudException("Could not save clouds", e);
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

	public DeltaCloud[] getClouds() throws DeltaCloudException {
		return doGetClouds().toArray(new DeltaCloud[clouds.size()]);
	}

	private List<DeltaCloud> doGetClouds() throws DeltaCloudException {
		if (clouds == null) {
			loadClouds();
		}
		return clouds;
	}

	public DeltaCloud findCloud(String name) throws DeltaCloudException {
		for (DeltaCloud cloud : getClouds()) {
			if (cloud.getName().equals(name))
				return cloud;
		}
		return null;
	}

	public void addCloud(DeltaCloud d) throws DeltaCloudException {
		doGetClouds().add(d);
		saveClouds();
		notifyListeners(ICloudManagerListener.ADD_EVENT, d);
	}

	public void removeCloud(DeltaCloud d) throws DeltaCloudException {
		doGetClouds().remove(d);
		d.dispose();
		saveClouds();
		notifyListeners(ICloudManagerListener.REMOVE_EVENT, d);
	}

	public void notifyCloudRename(DeltaCloud cloud) throws DeltaCloudException {
		saveClouds();
		notifyListeners(ICloudManagerListener.RENAME_EVENT, cloud);
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

	public void notifyListeners(int type, DeltaCloud cloud) {
		if (cloudManagerListeners != null) {
			Object[] listeners = cloudManagerListeners.getListeners();
			for (int i = 0; i < listeners.length; ++i) {
				((ICloudManagerListener) listeners[i]).cloudsChanged(type, cloud);
			}
		}
	}

}
