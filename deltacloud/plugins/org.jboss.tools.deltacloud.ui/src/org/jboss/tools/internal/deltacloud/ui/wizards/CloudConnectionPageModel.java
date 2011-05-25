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

import java.text.MessageFormat;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.Driver;
import org.jboss.tools.internal.deltacloud.ui.common.databinding.validator.ObservableUIPojo;
import org.jboss.tools.internal.deltacloud.ui.utils.URIUtils;

/**
 * @author Andre Dietisheim
 */
public class CloudConnectionPageModel extends ObservableUIPojo {

	public static final String PROPERTY_URL = "url"; //$NON-NLS-1$
	public static final String PROPERTY_NAME = "name"; //$NON-NLS-1$
	public static final String PROPERTY_PASSWORD = "password"; //$NON-NLS-1$
	public static final String PROPERTY_USERNAME = "username"; //$NON-NLS-1$
	public static final String PROPERTY_DRIVER = "driver"; //$NON-NLS-1$

	public static final String UNKNOWN_TYPE_LABEL = "UnknownType.label"; //$NON-NLS-1$
	public static final String INVALID_URL = "ErrorInvalidURL.text"; //$NON-NLS-1$
	public static final String NONCLOUD_URL = "ErrorNonCloudURL.text"; //$NON-NLS-1$

	private String name;
	private String url;
	private String username;

	private String password;
	private Driver driver;

	public CloudConnectionPageModel(String name, String url, String username, String password){
		this(name, url, username, password, Driver.UNKNOWN);
	}

	public CloudConnectionPageModel(String name, String url, String username, String password, Driver driver) {
		this.name = name;
		setUrl(URIUtils.prependHttp(url));
		this.username = username;
		this.password = password;
		// this.driver = driver;
		setDriverByUrl(url);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		firePropertyChange(PROPERTY_USERNAME, this.username, this.username = username);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		firePropertyChange(PROPERTY_PASSWORD, this.password, this.password = password);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		firePropertyChange(PROPERTY_NAME, this.name, this.name = name);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		firePropertyChange(PROPERTY_URL, this.url, this.url = url);
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		firePropertyChange(PROPERTY_DRIVER, this.driver, this.driver = driver);
	}

	/**
	 * Sets the driver for a given cloud url. Queries the cloud at the given url
	 * and determine its driver.
	 * 
	 * @param cloudUrl
	 *            the url of the cloud to query
	 */
	public void setDriverByUrl(final String url) {
		setDriver(Driver.UNKNOWN);
		new Job(MessageFormat.format("Determining driver for cloud at url {0}", url)) {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				Driver driver = getDriverFor(url);
				setDriver(driver);
				return Status.OK_STATUS;
			}

		}.schedule();
	}

	private Driver getDriverFor(String url) {
		try {
			return DeltaCloud.getServerDriver(url);
		} catch (DeltaCloudException e) {
			return Driver.UNKNOWN;
		}
	}

	public boolean isKnownCloud(Object value) {
		if (value instanceof Driver) {
			Driver driver = (Driver) value;
			return driver != Driver.UNKNOWN;
		}
		return false;
	}
}
