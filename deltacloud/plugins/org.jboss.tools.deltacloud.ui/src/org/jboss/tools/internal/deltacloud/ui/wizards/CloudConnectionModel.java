/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.internal.deltacloud.ui.wizards;

import java.net.MalformedURLException;

import org.jboss.tools.deltacloud.core.client.DeltaCloudClient;
import org.jboss.tools.deltacloud.core.client.DeltaCloudClient.DeltaCloudType;
import org.jboss.tools.internal.deltacloud.ui.common.databinding.ObservablePojo;

/**
 * @author Andre Dietisheim
 */
public class CloudConnectionModel extends ObservablePojo {

	public static final String PROPERTY_URL = "url"; //$NON-NLS-1$
	public static final String PROPERTY_NAME = "name"; //$NON-NLS-1$
	public static final String PROPERTY_PASSWORD = "password"; //$NON-NLS-1$
	public static final String PROPERTY_USERNAME = "username"; //$NON-NLS-1$
	public static final String PROPERTY_TYPE = "type"; //$NON-NLS-1$

	public static final String UNKNOWN_TYPE_LABEL = "UnknownType.label"; //$NON-NLS-1$
	public static final String INVALID_URL = "ErrorInvalidURL.text"; //$NON-NLS-1$
	public static final String NONCLOUD_URL = "ErrorNonCloudURL.text"; //$NON-NLS-1$

	private String name;
	private String url;
	private String username;

	private String password;
	private DeltaCloudClient.DeltaCloudType cloudType;
	private String initialName;

	public CloudConnectionModel() {
		this("", "", "", "", (DeltaCloudType) null);
	}

	public CloudConnectionModel(String name, String url, String username, String password, String cloudType) throws MalformedURLException {
		this(name, url, username, password, getDeltaCloudType(cloudType, url));
	}

	public CloudConnectionModel(String name, String url, String username, String password) throws MalformedURLException {
		this(name, url, username, password, getDeltaCloudTypeFromUrl(url));
	}

	public CloudConnectionModel(String name, String url, String username, String password, DeltaCloudType deltaCloudType) {
		this.name = name;
		this.initialName = name;
		this.url = url;
		this.username = username;
		this.password = password;
		this.cloudType = deltaCloudType;
	}
	
	private static DeltaCloudType getDeltaCloudTypeFromUrl(String url) throws MalformedURLException {
		return new DeltaCloudClient(url, "", "").getServerType();
	}

	private static DeltaCloudType getDeltaCloudType(String cloudType, String url) throws MalformedURLException {
		if (cloudType == null) {
			return null;
		}

		try {
			return DeltaCloudType.valueOf(cloudType);
		} catch (IllegalArgumentException e) {
			return getDeltaCloudTypeFromUrl(url);
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		getPropertyChangeSupport().firePropertyChange(PROPERTY_USERNAME, this.username, this.username = username);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		getPropertyChangeSupport().firePropertyChange(PROPERTY_PASSWORD, this.password, this.password = password);
	}

	public String getInitialName() {
		return initialName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		getPropertyChangeSupport().firePropertyChange(PROPERTY_NAME, this.name, this.name = name);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		System.err.println("url set to: " + url);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_URL, this.url, this.url = url);
	}

	public DeltaCloudClient.DeltaCloudType getType() {
		return cloudType;
	}

	public void setType(DeltaCloudClient.DeltaCloudType cloudType) {
		System.err.println("cloud type set to " + cloudType);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_TYPE, this.cloudType, this.cloudType = cloudType);
	}
}
