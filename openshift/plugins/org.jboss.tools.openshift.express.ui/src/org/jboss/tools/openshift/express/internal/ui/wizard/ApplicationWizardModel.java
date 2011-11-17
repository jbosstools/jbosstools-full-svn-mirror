/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.openshift.express.internal.ui.wizard;

import org.jboss.tools.common.ui.databinding.ObservableUIPojo;
import org.jboss.tools.openshift.express.client.IApplication;
import org.jboss.tools.openshift.express.client.ICartridge;
import org.jboss.tools.openshift.express.client.IUser;
import org.jboss.tools.openshift.express.client.OpenShiftException;

/**
 * @author André Dietisheim
 */
public class ApplicationWizardModel extends ObservableUIPojo {

	public static final String PROPERTY_APPLICATION = "application";

	private IUser user;
	private IApplication application;
	private String name;
	private ICartridge cartridge;

	public ApplicationWizardModel(IUser user) {
		this.user = user;
	}

	public IUser getUser() {
		return user;
	}
	
	public String getName() {
		return name;
	}

	public String setName(String name) {
		return this.name = name;
	}
	
	public void setCartridge(ICartridge cartridge) {
		this.cartridge = cartridge;
	}

	public void createApplication() throws OpenShiftException {
		createApplication(name, cartridge);
	}


	public void setApplication(IApplication application) {
		firePropertyChange(PROPERTY_APPLICATION, this.application, this.application = application);
	}

	public IApplication getApplication() {
		return application;
	}
	
	public void createApplication(String name, ICartridge cartridge) throws OpenShiftException {
		IApplication application = getUser().createApplication(name, cartridge);
		setApplication(application);
	}

}
