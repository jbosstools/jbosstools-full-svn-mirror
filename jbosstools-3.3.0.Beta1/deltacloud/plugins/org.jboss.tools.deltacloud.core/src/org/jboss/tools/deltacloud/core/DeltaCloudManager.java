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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.deltacloud.client.utils.StringUtils;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.ListenerList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Jeff Johnston
 * @author André Dietisheim
 */
public class DeltaCloudManager {

	private static final DeltaCloudManager INSTANCE = new DeltaCloudManager();

	public final static String CLOUDFILE_NAME = "clouds.xml"; //$NON-NLS-1$
	private List<DeltaCloud> clouds;
	private ListenerList cloudManagerListeners;
	private boolean loaded = false;

	private DeltaCloudManager() {
	}

	private void loadClouds() throws DeltaCloudException {
		this.clouds = new ArrayList<DeltaCloud>(); // clear present clouds
		DeltaCloudMultiException connectionException = new DeltaCloudMultiException("Could not load some clouds");
		try {
			File cloudFile = getCloudFile();
			if (!cloudFile.exists()) {
				cloudFile.createNewFile();
			} else {
				doLoadClouds(connectionException, cloudFile);
			}
		} catch (Exception e) {
			connectionException.addError(e);
		}

		loaded = true;

		if (!connectionException.isEmpty()) {
			throw connectionException;
		}
	}

	private void doLoadClouds(DeltaCloudMultiException connectionException, File cloudFile)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document d = db.parse(cloudFile);
		Element element = d.getDocumentElement();
		// Get the stored configuration data
		NodeList cloudNodes = element.getElementsByTagName(DeltaCloudXMLBuilder.TAG_CLOUD); // $NON-NLS-1$
		for (int x = 0; x < cloudNodes.getLength(); ++x) {
			Node n = cloudNodes.item(x);
			try {
				loadCloud(n, clouds);
			} catch (DeltaCloudException e) {
				connectionException.addError(e);
			}
		}
	}

	// TODO: move to unmarshaler component
	private DeltaCloud loadCloud(Node n, List<DeltaCloud> clouds) throws DeltaCloudException {
		String name = "<UNKNOWN>";
		try {
			DeltaCloud cloud = null;
			NamedNodeMap attrs = n.getAttributes();
			name = attrs.getNamedItem(DeltaCloudXMLBuilder.ATTR_NAME).getNodeValue(); // $NON-NLS-1$
			String url = getNodeValue(attrs.getNamedItem(DeltaCloudXMLBuilder.ATTR_URL)); // $NON-NLS-1$
			String username = URLEncoder.encode(
					attrs.getNamedItem(DeltaCloudXMLBuilder.ATTR_USERNAME).getNodeValue(),
					DeltaCloudXMLBuilder.ENCODING); // $NON-NLS-1$
			DeltaCloudDriver driver = DeltaCloudDriver.checkedValueOf(attrs.getNamedItem(DeltaCloudXMLBuilder.ATTR_TYPE).getNodeValue()); // $NON-NLS-1$
			String imageFilterRules = getImageFilterRules(attrs.getNamedItem("imagefilter")); // $NON-NLS-1$
			String instanceFilterRules = getNodeValue(attrs.getNamedItem("instancefilter")); // $NON-NLS-1$
			String lastKeyName = getNodeValue(attrs.getNamedItem(DeltaCloudXMLBuilder.ATTR_LASTKEYNAME)); // $NON-NLS-1$
			String lastImageId = getNodeValue(attrs.getNamedItem(DeltaCloudXMLBuilder.ATTR_LASTIMAGE)); // $NON-NLS-1$
			String lastRealmName = getNodeValue(attrs.getNamedItem(DeltaCloudXMLBuilder.ATTR_LASTREALM)); // $NON-NLS-1$
			String lastProfileId = getNodeValue(attrs.getNamedItem(DeltaCloudXMLBuilder.ATTR_LASTPROFILE)); // $NON-NLS-1$
			Collection<IInstanceAliasMapping> aliasMappings = getInstanceMappings(n);
			cloud = new DeltaCloud(name, url, username, driver, imageFilterRules, instanceFilterRules, aliasMappings);
			clouds.add(cloud);
			cloud.setLastImageId(lastImageId);
			cloud.setLastKeyname(lastKeyName);
			cloud.setLastRealmName(lastRealmName);
			cloud.setLastProfileId(lastProfileId);
			return cloud;
		} catch (DeltaCloudException e) {
			throw e;
		} catch (Exception e) {
			// TODO: internationalize strings
			throw new DeltaCloudException(MessageFormat.format("Could not load cloud {0}", name), e);
		}
	}

	// TODO: move to unmarshaler component
	private Collection<IInstanceAliasMapping> getInstanceMappings(Node n) {
		Assert.isLegal(n instanceof Element);
		Collection<IInstanceAliasMapping> aliasMappings = new ArrayList<IInstanceAliasMapping>();
		Element element = (Element) n;
		NodeList instanceNodes = element.getElementsByTagName(DeltaCloudXMLBuilder.TAG_INSTANCE);
		for (int i = 0; i < instanceNodes.getLength(); i++) {
			IInstanceAliasMapping aliasMapping = createInstanceAliasMapping(instanceNodes, i);
			if (aliasMapping != null) {
				aliasMappings.add(aliasMapping);
			}
		}
		return aliasMappings;
	}

	private IInstanceAliasMapping createInstanceAliasMapping(NodeList instanceNodes, int i) {
		IInstanceAliasMapping aliasMapping = null;
		Node instanceNode = instanceNodes.item(i);
		NamedNodeMap attributes = instanceNode.getAttributes();
		Node idNode = attributes.getNamedItem(DeltaCloudXMLBuilder.ATTR_ID);
		String id = null;
		if (idNode != null) {
			id = idNode.getNodeValue();
			attributes = instanceNode.getAttributes();
			Node aliasNode = attributes.getNamedItem(DeltaCloudXMLBuilder.ATTR_ALIAS);
			if (aliasNode != null) {
				String alias = StringUtils.emptyString2Null(aliasNode.getNodeValue());
				aliasMapping = new InstanceAliasMapping(id, alias);
			}
		}
		return aliasMapping;
	}

	private String getNodeValue(Node node) {
		if (node != null) {
			return node.getNodeValue();
		}
		return null;
	}

	private String getImageFilterRules(Node imageFilterNode) {
		String imageFilterRules = null;
		if (imageFilterNode != null) {
			imageFilterRules = imageFilterNode.getNodeValue();
		}
		return imageFilterRules;
	}

	// TODO: move to marshaler component
	protected void saveClouds() throws DeltaCloudException {
		if (!loaded) {
			return;
		}

		try {
			File cloudFile = getCloudFile();
			PrintWriter p = new PrintWriter(new BufferedWriter(new FileWriter(cloudFile)));
			DeltaCloudXMLBuilder.xmlHeader(p);
			DeltaCloudXMLBuilder.tag(DeltaCloudXMLBuilder.TAG_CLOUDS, p);
			DeltaCloudXMLBuilder.closeTag(p);
			for (DeltaCloud d : clouds) {
				printCloud(d, p);
			}
			DeltaCloudXMLBuilder.endTag(DeltaCloudXMLBuilder.TAG_CLOUDS, p);
			p.close();
		} catch (Exception e) {
			// TODO: internationalize strings
			throw new DeltaCloudException("Could not save clouds", e);
		}
	}

	// TODO: move to marshaler component
	private void printCloud(DeltaCloud d, PrintWriter printWriter) throws UnsupportedEncodingException,
			DeltaCloudException {
		String username = URLEncoder.encode(d.getUsername(), DeltaCloudXMLBuilder.ENCODING);
		DeltaCloudXMLBuilder.tag(DeltaCloudXMLBuilder.TAG_CLOUD, printWriter);
		DeltaCloudXMLBuilder.attribute(DeltaCloudXMLBuilder.ATTR_NAME, d.getName(), printWriter);
		DeltaCloudXMLBuilder.attribute(DeltaCloudXMLBuilder.ATTR_URL, d.getURL(), printWriter);
		DeltaCloudXMLBuilder.attribute(DeltaCloudXMLBuilder.ATTR_USERNAME, username, printWriter);
		DeltaCloudXMLBuilder.attribute(DeltaCloudXMLBuilder.ATTR_TYPE, d.getDriver().toString(), printWriter);
		DeltaCloudXMLBuilder.attribute(
				DeltaCloudXMLBuilder.ATTR_IMAGEFILTER, d.getImageFilter().toString(), printWriter);
		DeltaCloudXMLBuilder.attribute(
				DeltaCloudXMLBuilder.ATTR_INSTANCEFILTER, d.getInstanceFilter().toString(), printWriter);
		DeltaCloudXMLBuilder.attribute(DeltaCloudXMLBuilder.ATTR_LASTKEYNAME, d.getLastKeyname(), printWriter);
		DeltaCloudXMLBuilder.attribute(DeltaCloudXMLBuilder.ATTR_LASTIMAGE, d.getLastImageId(), printWriter);
		DeltaCloudXMLBuilder.attribute(DeltaCloudXMLBuilder.ATTR_LASTREALM, d.getLastRealmName(), printWriter);
		DeltaCloudXMLBuilder.attribute(DeltaCloudXMLBuilder.ATTR_LASTPROFILE, d.getLastProfileId(), printWriter);
		DeltaCloudXMLBuilder.closeTag(printWriter);
		printInstances(d, printWriter);
		DeltaCloudXMLBuilder.endTag(DeltaCloudXMLBuilder.TAG_CLOUD, printWriter);
	}

	// TODO: move to marshaler component
	private void printInstances(DeltaCloud d, PrintWriter printWriter) {
		try {
			for (DeltaCloudInstance instance : d.getInstances()) {
				String alias = instance.getAlias();
				if (alias != null) {
					DeltaCloudXMLBuilder.tag(DeltaCloudXMLBuilder.TAG_INSTANCE, printWriter);
					DeltaCloudXMLBuilder.attribute(DeltaCloudXMLBuilder.ATTR_ID, instance.getId(), printWriter);
					DeltaCloudXMLBuilder.attribute(DeltaCloudXMLBuilder.ATTR_ALIAS, alias, printWriter);
					DeltaCloudXMLBuilder.closeTag(printWriter);
					DeltaCloudXMLBuilder.endTag(DeltaCloudXMLBuilder.TAG_INSTANCE, printWriter);
				}
			}
		} catch (DeltaCloudException e) {
			// ignore
		}
	}

	private File getCloudFile() throws IOException {
		IPath stateLocation = Activator.getDefault().getStateLocation();
		return stateLocation.append(CLOUDFILE_NAME).toFile();
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
		notifyListeners(IDeltaCloudManagerListener.ADD_EVENT, d);
	}

	public void removeCloud(DeltaCloud d) throws DeltaCloudException {
		doGetClouds().remove(d);
		d.dispose();
		saveClouds();
		notifyListeners(IDeltaCloudManagerListener.REMOVE_EVENT, d);
	}

	public void addCloudManagerListener(IDeltaCloudManagerListener listener) {
		if (cloudManagerListeners == null)
			cloudManagerListeners = new ListenerList(ListenerList.IDENTITY);
		cloudManagerListeners.add(listener);
	}

	public void removeCloudManagerListener(IDeltaCloudManagerListener listener) {
		if (cloudManagerListeners != null)
			cloudManagerListeners.remove(listener);
	}

	public void notifyListeners(int type, DeltaCloud cloud) {
		if (cloudManagerListeners != null) {
			Object[] listeners = cloudManagerListeners.getListeners();
			for (int i = 0; i < listeners.length; ++i) {
				((IDeltaCloudManagerListener) listeners[i]).cloudsChanged(type, cloud);
			}
		}
	}

	public void dispose() throws DeltaCloudException {
		saveClouds();
	}
}
