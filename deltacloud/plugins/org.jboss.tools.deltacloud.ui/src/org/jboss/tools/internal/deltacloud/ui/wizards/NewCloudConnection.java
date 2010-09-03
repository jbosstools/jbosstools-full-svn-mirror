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

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudManager;
import org.jboss.tools.deltacloud.ui.Activator;

public class NewCloudConnection extends Wizard implements INewWizard {

	private static final String MAINPAGE_NAME = "NewCloudConnection.name"; //$NON-NLS-1$
	private NewCloudConnectionPage mainPage;
	
	public NewCloudConnection() {
		super();
	}
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub
	}

	@Override
	public void addPages() {
		// TODO Auto-generated method stub
		mainPage = new NewCloudConnectionPage(WizardMessages.getString(MAINPAGE_NAME), this);
		addPage(mainPage);
	}

	@Override
	public boolean canFinish() {
		return mainPage.isPageComplete();
	}

	public boolean performTest() {
		String name = mainPage.getName();
		String url = mainPage.getURL();
		String username = mainPage.getUsername();
		String password = mainPage.getPassword();
		try {
			DeltaCloud newCloud = new DeltaCloud(name, url, username, password);
			return newCloud.testConnection();
		} catch (MalformedURLException e) {
			Activator.log(e);
			return false;
		}
	}
	
	@Override
	public boolean performFinish() {
		String name = mainPage.getName();
		String url = mainPage.getURL();
		String username = mainPage.getUsername();
		String password = mainPage.getPassword();
		String type = mainPage.getType();
		try {
			DeltaCloud newCloud = new DeltaCloud(name, url, username, password, type, true);
			DeltaCloudManager.getDefault().addCloud(newCloud);
		} catch (MalformedURLException e) {
			Activator.log(e);
			return false;
		}
		return true;
	}

}
