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

import org.jboss.tools.deltacloud.core.client.DeltaCloudClient;
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

	public CloudConnectionModel(String name, String url, String username, String password) {
		this.name = name;
		this.url = url;
		this.username = username;
		this.password = password;
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
		getPropertyChangeSupport().firePropertyChange(PROPERTY_URL, this.url, this.url = url);
	}

	public DeltaCloudClient.DeltaCloudType getType() {
		return cloudType;
	}

	public void setType(DeltaCloudClient.DeltaCloudType cloudType) {
		getPropertyChangeSupport().firePropertyChange(PROPERTY_TYPE, this.cloudType, this.cloudType = cloudType);
	}
}
