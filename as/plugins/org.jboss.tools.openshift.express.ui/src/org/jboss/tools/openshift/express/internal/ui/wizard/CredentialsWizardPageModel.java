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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.jboss.tools.common.ui.databinding.ObservableUIPojo;
import org.jboss.tools.common.ui.preferencevalue.StringPreferenceValue;
import org.jboss.tools.openshift.express.client.IOpenshiftService;
import org.jboss.tools.openshift.express.client.IUser;
import org.jboss.tools.openshift.express.client.NotFoundOpenshiftException;
import org.jboss.tools.openshift.express.client.OpenshiftException;
import org.jboss.tools.openshift.express.client.User;
import org.jboss.tools.openshift.express.client.UserConfiguration;
import org.jboss.tools.openshift.express.internal.ui.OpenshiftUIActivator;

/**
 * @author André Dietisheim
 */
public class CredentialsWizardPageModel extends ObservableUIPojo {

	private static final String ID = OpenshiftUIActivator.PLUGIN_ID
			+ OpenshiftUIActivator.getDefault().getBundle().getVersion();

	private static final String RHLOGIN_PREFS_KEY = "org.jboss.tools.openshift.express.internal.ui.wizard.CredentialsWizardModel_RHLOGIN";

	public static final String PROPERTY_SERVER_URL = "serverUrl";
	public static final String PROPERTY_RHLOGIN = "rhLogin";
	public static final String PROPERTY_PASSWORD = "password";
	public static final String PROPERTY_CREDENTIALS_VALIDITY = "credentialsValidity";

	private String serverUrl;
	private String rhLogin;
	private String password;
	private IStatus credentialsValidity;
	private IUser user;
	private StringPreferenceValue rhLoginPreferenceValue;

	private ImportProjectWizardModel wizardModel;

	public CredentialsWizardPageModel(ImportProjectWizardModel model) {
		this.wizardModel = model;
		this.serverUrl = IOpenshiftService.BASE_URL;
		this.rhLoginPreferenceValue = new StringPreferenceValue(RHLOGIN_PREFS_KEY, OpenshiftUIActivator.PLUGIN_ID);
		this.rhLogin = initRhLogin();
		resetCredentialsStatus();
	}

	protected String initRhLogin() {
		String rhLogin = null;
		rhLogin = rhLoginPreferenceValue.get();
		if (rhLogin == null
				|| rhLogin.length() == 0) {
			rhLogin = getUserConfiguration();
		}
		return rhLogin;
	}

	protected String getUserConfiguration() {
		String configuredUsername = null;
		try {
			configuredUsername = new UserConfiguration().getRhlogin();
		} catch (Exception e) {
			// do nothing
		}
		return configuredUsername;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		firePropertyChange(PROPERTY_SERVER_URL, this.serverUrl, this.serverUrl = serverUrl);
	}

	public String getRhLogin() {
		return rhLogin;
	}

	public void setRhLogin(String rhLogin) {
		if (rhLogin != null
				&& !rhLogin.equals(this.rhLogin)) {
			rhLoginPreferenceValue.store(rhLogin);
			firePropertyChange(PROPERTY_RHLOGIN, this.rhLogin, this.rhLogin = rhLogin);
			resetCredentialsStatus();
		}
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		if (password != null
				&& !password.equals(this.password)) {
			firePropertyChange(PROPERTY_PASSWORD, this.password, this.password = password);
			resetCredentialsStatus();
		}
	}

	private void resetCredentialsStatus() {
		setCredentialsStatus(null);
	}

	private void setCredentialsStatus(IStatus status) {
		firePropertyChange(PROPERTY_CREDENTIALS_VALIDITY, this.credentialsValidity,
				this.credentialsValidity = status);
	}

	public IStatus getCredentialsValidity() {
		return credentialsValidity;
	}

	public boolean areCredentialsValidated() {
		return credentialsValidity != null;
	}

	public IStatus validateCredentials() {
		IStatus status = new Status(IStatus.ERROR, OpenshiftUIActivator.PLUGIN_ID, "Your credentails are not valid.");
		try {
			this.user = new User(getRhLogin(), getPassword(), ID);
			if (user.isValid()) {
				status = Status.OK_STATUS;
			}
		} catch (NotFoundOpenshiftException e) {
			// valid user without domain
			status = Status.OK_STATUS;
		} catch (OpenshiftException e) {
			this.user = null;
		}
		wizardModel.setUser(user);
		setCredentialsStatus(status);
		return status;
	}

	public IUser getUser() {
		return user;
	}
}
