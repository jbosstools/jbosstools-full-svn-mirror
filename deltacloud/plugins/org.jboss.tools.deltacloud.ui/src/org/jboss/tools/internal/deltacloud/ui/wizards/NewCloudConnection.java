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

public class NewCloudConnection extends Wizard implements INewWizard, CloudConnection {

	private static final String MAINPAGE_NAME = "NewCloudConnection.name"; //$NON-NLS-1$
	private CloudConnectionPage mainPage;

	public NewCloudConnection() {
		super();
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

	@Override
	public void addPages() {
		mainPage = new CloudConnectionPage(WizardMessages.getString(MAINPAGE_NAME), this);
		addPage(mainPage);
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
			DeltaCloud newCloud = new DeltaCloud(name, url, username, password, type, true);
			DeltaCloudManager.getDefault().addCloud(newCloud);
		} catch (MalformedURLException e) {
			Activator.log(e);
		}
		return true;
	}

}
