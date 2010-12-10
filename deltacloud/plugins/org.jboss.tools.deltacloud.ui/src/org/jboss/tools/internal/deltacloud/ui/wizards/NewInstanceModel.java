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

import org.jboss.tools.deltacloud.core.DeltaCloudImage;
import org.jboss.tools.internal.deltacloud.core.observable.ObservablePojo;

/**
 * @author Jeff Jonhston
 */
public class NewInstanceModel extends ObservablePojo {

	public static final String PROPERTY_URL = "url"; //$NON-NLS-1$
	public static final String PROPERTY_NAME = "name"; //$NON-NLS-1$
	public static final String PROPERTY_IMAGE = "image"; //$NON-NLS-1$
	public static final String PROPERTY_ARCH = "arch"; //$NON-NLS-1$
	public static final String PROPERTY_REALM = "realm"; //$NON-NLS-1$
	public static final String PROPERTY_KEYNAME = "keyname"; //$NON-NLS-1$
	public static final String PROPERTY_PROFILE = "profile"; //$NON-NLS-1$

	private String name;
	private DeltaCloudImage image;
	private String arch;
	private String realm;
	private String keyname;
	private String profile;

	protected NewInstanceModel(String keyname) {
		this.keyname = keyname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		getPropertyChangeSupport().firePropertyChange(PROPERTY_NAME, this.name, this.name = name);
	}

	public DeltaCloudImage getImage() {
		return image;
	}

	public void setImage(DeltaCloudImage image) {
		getPropertyChangeSupport().firePropertyChange(PROPERTY_IMAGE, this.image, this.image = image);
	}

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		getPropertyChangeSupport().firePropertyChange(PROPERTY_REALM, this.realm, this.realm = realm);
	}

	public String getKeyname() {
		return keyname;
	}

	public void setKeyname(String keyname) {
		getPropertyChangeSupport().firePropertyChange(PROPERTY_KEYNAME, this.keyname, this.keyname = keyname);
	}

	public String getArch() {
		return arch;
	}

	public void setArch(String arch) {
		getPropertyChangeSupport().firePropertyChange(PROPERTY_ARCH, this.arch, this.arch = arch);
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		getPropertyChangeSupport().firePropertyChange(PROPERTY_PROFILE, this.profile, this.profile = profile);
	}
}
