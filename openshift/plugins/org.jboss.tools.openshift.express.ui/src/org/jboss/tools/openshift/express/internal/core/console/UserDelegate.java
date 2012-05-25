/*******************************************************************************
 * Copyright (c) 2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.openshift.express.internal.core.console;

import java.net.SocketTimeoutException;
import java.util.List;

import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.common.ui.WizardUtils;
import org.jboss.tools.openshift.express.internal.ui.utils.Logger;
import org.jboss.tools.openshift.express.internal.ui.viewer.ConnectToOpenShiftWizard;

import com.openshift.client.ApplicationScale;
import com.openshift.client.IApplication;
import com.openshift.client.ICartridge;
import com.openshift.client.IDomain;
import com.openshift.client.IEmbeddableCartridge;
import com.openshift.client.IGearProfile;
import com.openshift.client.IOpenShiftSSHKey;
import com.openshift.client.ISSHPublicKey;
import com.openshift.client.IUser;
import com.openshift.client.OpenShiftException;
import com.openshift.client.OpenShiftUnknonwSSHKeyTypeException;

public class UserDelegate {
	private String username;
	private String password;
	private IUser delegate;
	private boolean rememberPassword;
	private boolean connected;
	private boolean alreadyPromptedForPassword;
	
	public UserDelegate(String username, String password) {
		this.username = username;
		this.password = password;
		this.rememberPassword = (password != null);
		this.setConnected(false);
	}
	
	public UserDelegate(IUser user, boolean rememberPassword) {
		setDelegate(user);
		this.rememberPassword = rememberPassword;
	}
	
	/**
	 * @return the delegate
	 */
	protected final IUser getDelegate() {
		return delegate;
	}

	/**
	 * @param delegate the delegate to set
	 */
	protected final void setDelegate(IUser delegate) {
		this.delegate = delegate;
		this.username = delegate.getRhlogin();
		this.password = delegate.getPassword();
		this.setConnected(true);
	} 

	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	
	public boolean isRememberPassword() {
		return rememberPassword;
	}

	/**
	 * @param rememberPassword the rememberPassword to set
	 */
	public final void setRememberPassword(boolean rememberPassword) {
		this.rememberPassword = rememberPassword;
	}

	public boolean canPromptForPassword() {
		return this.alreadyPromptedForPassword == false;
	}
	
	/**
	 * Prompts user for password if it was not given or retrieved from secure storage before.
	 * @return true if user entered credentials, false otherwise.
	 */
	public boolean checkForPassword() {
		if(delegate == null) {
			if( username != null && password != null ) {
				if( checkCurrentCredentials() )
					return true;
			}
			return promptForCredentials();
		}
		return true;
	}
	
	private boolean checkCurrentCredentials() {
		// First check if the current username / pw work
		IUser user = null;
		try {
			user = UserModel.getDefault().createUser(username, password);
			setDelegate(user);
			setConnected(true);
			return true;
		} catch( OpenShiftException ose ) {
			// ignore
		} catch( SocketTimeoutException ste ) {
			// ignore
		}
		return false;
	}
	
	private boolean promptForCredentials() {
		// The auto-login failed. Try to prompt
		try {
			this.alreadyPromptedForPassword = true;
			Display.getDefault().syncExec(new Runnable() { public void run() {
				final IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				final Shell shell = activeWorkbenchWindow != null ? activeWorkbenchWindow.getShell() : null;
				if(shell == null) {
					Logger.error("Could not open Credentials Wizard: no shell available");
					return;
				}
				final ConnectToOpenShiftWizard connectToOpenShiftWizard = new ConnectToOpenShiftWizard();
				int returnCode = WizardUtils.openWizardDialog(connectToOpenShiftWizard, shell);
				if (returnCode == Window.OK) {
					Logger.debug("OpenShift Auth succeeded.");
					UserDelegate created = connectToOpenShiftWizard.getUser();
					if( created != null ) {
						setDelegate(created.getDelegate());
						setConnected(true);
						setRememberPassword(created.isRememberPassword());
					}
				} else {
					setConnected(false);
				}
			}});
		} catch( Exception e ) {
			Logger.error("Failed to retrieve User's password", e);
		}
		return delegate != null;
	}
	
	public IApplication createApplication(final String applicationName, final ICartridge applicationType, final ApplicationScale scale, final IGearProfile gearProfile)
			throws OpenShiftException {
		if(checkForPassword()) {
			return delegate.getDefaultDomain().createApplication(applicationName, applicationType, scale, gearProfile);
		}
		return null;
	}

	/**
	 * Create a new domain with the given id
	 * @param id the domain id
	 * @return the created domain
	 * @throws OpenShiftException
	 * @throws SocketTimeoutException 
	 */
	public IDomain createDomain(String id)
			throws OpenShiftException {
		if(checkForPassword()) {
			return delegate.createDomain(id);
		} 
		return null;
	}
	public IApplication getApplicationByName(String arg0)
			throws OpenShiftException {
		if(checkForPassword() && delegate.hasDomain()) {
			return delegate.getDefaultDomain().getApplicationByName(arg0);
		} 
		return null;
	}
	public List<IApplication> getApplications() throws OpenShiftException {
		if(checkForPassword() && delegate.hasDomain()) {
			return delegate.getDefaultDomain().getApplications();
		} 
		return null;
	}
	
	public String getAuthIV() {
		if(checkForPassword()) {
			return delegate.getAuthIV();
		} 
		return null;
	}
	
	public String getAuthKey() {
		if(checkForPassword()) {
			return delegate.getAuthKey();
		} 
		return null;
	}
	
	public List<ICartridge> getStandaloneCartridgeNames() throws OpenShiftException {
		if(checkForPassword()) {
			return delegate.getConnection().getStandaloneCartridges();
		} 
		return null;
	}
	
	public IDomain getDefaultDomain() throws OpenShiftException {
		if(checkForPassword()) {
			return delegate.getDefaultDomain();
		} 
		return null;
	}
	
	public List<IEmbeddableCartridge> getEmbeddableCartridges()
			throws OpenShiftException {
		if(checkForPassword()) {
			return delegate.getConnection().getEmbeddableCartridges();
		} 
		return null;
	}
	
	public boolean hasApplication(String name) throws OpenShiftException {
		if(checkForPassword()) {
			return delegate.getDefaultDomain().hasApplicationByName(name);
		} 
		return false;
	}
	
	public boolean hasApplicationOfType(ICartridge type) throws OpenShiftException {
		if(hasDomain()) {
			return delegate.getDefaultDomain().hasApplicationByCartridge(type);
		} 
		return false;
	}
	
	public boolean hasDomain() throws OpenShiftException {
		if(checkForPassword()) {
			return delegate.hasDomain();
		}
		return false;
	}
	
	public boolean isValid() throws OpenShiftException {
		if(checkForPassword()) {
			return true;
		} 
		return false;
	}
	
	public void refresh() throws OpenShiftException {
		if(checkForPassword()) {
			delegate.refresh();
		} 
	}

	/**
	 * @return the connected
	 */
	public boolean isConnected() {
		return connected;
	}

	/**
	 * @param connected the connected to set
	 */
	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public IOpenShiftSSHKey getSSHKeyByPublicKey(String publicKey) throws OpenShiftUnknonwSSHKeyTypeException, OpenShiftException {
		return delegate.getSSHKeyByPublicKey(publicKey);
	}
	
	public IOpenShiftSSHKey putSSHKey(String name, ISSHPublicKey key) throws OpenShiftException {
		return delegate.putSSHKey(name, key);
	}
	
}
