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

import java.text.MessageFormat;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudDriver;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.DeltaCloudManager;
import org.jboss.tools.deltacloud.ui.Activator;
import org.jboss.tools.deltacloud.ui.ErrorUtils;
import org.jboss.tools.internal.deltacloud.ui.preferences.IPreferenceKeys;
import org.jboss.tools.internal.deltacloud.ui.preferences.StringPreferenceValue;

/**
 * @author Jeff Johnston
 * @author Andre Dietisheim
 */
public class NewCloudConnectionWizard extends Wizard implements INewWizard, CloudConnection {

	private static final String MAINPAGE_NAME = "CloudConnection.name"; //$NON-NLS-1$
	protected CloudConnectionPage mainPage;
	protected DeltaCloud initialCloud;
	private String pageTitle;

	public NewCloudConnectionWizard() {
		this(WizardMessages.getString(MAINPAGE_NAME));
	}

	public NewCloudConnectionWizard(String pageTitle) {
		super();
		this.pageTitle = pageTitle;
	}

	public NewCloudConnectionWizard(String pageTitle, DeltaCloud initial) {
		this(pageTitle);
		this.initialCloud = initial;
	}

	protected CloudConnectionPage createCloudConnectionPage() {
		try {
			if (initialCloud == null) {
				return new CloudConnectionPage(pageTitle, this);
			}

			return new CloudConnectionPage(pageTitle, initialCloud, this);
		} catch (Exception e) {
			ErrorUtils.handleError(WizardMessages.getString("EditCloudConnectionError.title"),
					WizardMessages.getString("EditCloudConnectionError.message"), e, getShell());
		}
		return null;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

	@Override
	public boolean canFinish() {
		return mainPage.isPageComplete();
	}

	@Override
	public void addPages() {
		mainPage = createCloudConnectionPage();
		if (mainPage != null) {
			addPage(mainPage);
		}
	}

	public boolean performTest() {
		String name = mainPage.getConnectionName();
		String url = mainPage.getUrl();
		String username = mainPage.getUsername();
		String password = mainPage.getPassword();
		try {
			DeltaCloud newCloud = new DeltaCloud(name, url, username, password);
			return newCloud.testCredentials();
		} catch (DeltaCloudException e) {
			ErrorUtils.handleErrorAsync(WizardMessages.getString("CloudConnectionAuthError.title"),
							WizardMessages.getFormattedString("CloudConnectionAuthError.message", url), e, getShell());
			return false;
		}
	}

	@Override
	public boolean needsProgressMonitor() {
		return true;
	}

	@Override
	public boolean performFinish() {
		String name = mainPage.getConnectionName();
		String url = mainPage.getUrl();

		new StringPreferenceValue(IPreferenceKeys.LAST_URL, Activator.PLUGIN_ID)
				.store(url);

		String username = mainPage.getUsername();
		String password = mainPage.getPassword();
		DeltaCloudDriver driver = mainPage.getDriver();

		try {
			DeltaCloud newCloud = new DeltaCloud(name, url, username, password, driver);
			DeltaCloudManager.getDefault().addCloud(newCloud);
		} catch (Exception e) {
			// TODO internationalize strings
			ErrorUtils
					.handleError("Error", MessageFormat.format("Could not create cloud {0}", name), e, getShell());
		}
		return true;
	}
}
