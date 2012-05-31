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

import org.jboss.tools.openshift.express.internal.core.console.UserDelegate;
import org.jboss.tools.openshift.express.internal.core.console.UserModel;

/**
 * @author Andre Dietisheim
 */
public class ConnectToOpenShiftWizardModel implements IUserAwareModel {
	
	protected UserDelegate user = null;
	
	/**
	 * Default constructor.
	 */
	public ConnectToOpenShiftWizardModel() {
		super();
	}
	
	/**
	 * Constructor 
	 * @param user the user to use to connect to OpenShift.
	 */
	public ConnectToOpenShiftWizardModel(final UserDelegate user) {
		this.user = user;
	}
	
	@Override
	public UserDelegate getUser() {
		return user == null ? UserModel.getDefault().getRecentUser() : user;
	}

	@Override
	public UserDelegate setUser(UserDelegate user) {
		UserModel.getDefault().addUser(user);
		this.user = user;
		return user;
	}

}
