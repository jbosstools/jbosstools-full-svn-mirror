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
package org.jboss.tools.internal.deltacloud.ui.wizards;

import java.net.MalformedURLException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.jboss.tools.common.log.StatusFactory;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudManager;
import org.jboss.tools.deltacloud.core.client.DeltaCloudClientException;
import org.jboss.tools.deltacloud.ui.Activator;

public class EditCloudConnection extends Wizard implements INewWizard, CloudConnection {

	private static final String MAINPAGE_NAME = "EditCloudConnection.name"; //$NON-NLS-1$
	private CloudConnectionPage mainPage;
	private DeltaCloud cloud;

	public EditCloudConnection(DeltaCloud cloud) {
		super();
		this.cloud = cloud;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

	@Override
	public void addPages() {
		String password = getPassword();
		try {
			mainPage = new CloudConnectionPage(WizardMessages.getString(MAINPAGE_NAME),
					cloud.getName(), cloud.getURL(), cloud.getUsername(), password,
					cloud.getType(), this);
			addPage(mainPage);
		} catch (MalformedURLException e) {
			IStatus status = StatusFactory.getInstance(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e);
			ErrorDialog.openError(getShell(), WizardMessages.getString("EditCloudConnectionError.title"),
					WizardMessages.getString("EditCloudConnectionError.message"), status);
		}
	}

	private String getPassword() {
		String password = "";
		String key = DeltaCloud.getPreferencesKey(cloud.getURL(), cloud.getUsername());
		ISecurePreferences root = SecurePreferencesFactory.getDefault();
		ISecurePreferences node = root.node(key);
		try {
			password = node.get("password", null); //$NON-NLS-1$
		} catch (Exception e) {
			Activator.log(e);
		}
		return password;
	}

	@Override
	public boolean canFinish() {
		return mainPage.isPageComplete();
	}

	public boolean performTest() {
		String name = mainPage.getName();
		String url = mainPage.getModel().getUrl();
		String username = mainPage.getModel().getUsername();
		String password = mainPage.getModel().getPassword();
		try {
			DeltaCloud newCloud = new DeltaCloud(name, url, username, password);
			return newCloud.testConnection();
		} catch (MalformedURLException e) {
			Activator.log(e);
			return false;
		} catch (DeltaCloudClientException e) {
			IStatus status = StatusFactory.getInstance(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e);
			Activator.log(status);
			ErrorDialog.openError(
					getShell(),
					WizardMessages.getString("CloudConnectionAuthError.title"),
					WizardMessages.getFormattedString("CloudConnectionAuthError.message", url),
					status);
			return true;
		}
	}

	@Override
	public boolean performFinish() {
		String name = mainPage.getModel().getName();
		String url = mainPage.getModel().getUrl();
		String username = mainPage.getModel().getUsername();
		String password = mainPage.getModel().getPassword();
		String type = mainPage.getModel().getType().toString();
		try {
			String oldName = cloud.getName();
			cloud.editCloud(name, url, username, password, type);
			if (!name.equals(oldName))
				DeltaCloudManager.getDefault().notifyCloudRename();
		} catch (MalformedURLException e) {
			Activator.log(e);
		}
		return true;
	}
	
	@Override
	public boolean needsProgressMonitor() {
		return true;
	}

}
