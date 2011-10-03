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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.jboss.tools.common.ui.WizardUtils;
import org.jboss.tools.common.ui.preferencevalue.StringPreferenceValue;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudDriver;
import org.jboss.tools.deltacloud.core.job.AbstractCloudJob;
import org.jboss.tools.deltacloud.ui.Activator;
import org.jboss.tools.deltacloud.ui.IDeltaCloudPreferenceConstants;

/**
 * @author Jeff Johnston
 * @author André Dietisheim
 */
public class EditCloudConnectionWizard extends NewCloudConnectionWizard {

	private static final String MAINPAGE_NAME = "EditCloudConnection.name"; //$NON-NLS-1$

	public EditCloudConnectionWizard(DeltaCloud cloud) {
		super(WizardMessages.getString(MAINPAGE_NAME), cloud);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setWindowTitle(WizardMessages.getString("EditCloudConnection.name"));
	}

	@Override
	public boolean performFinish() {
		String name = mainPage.getConnectionName();
		new StringPreferenceValue(IDeltaCloudPreferenceConstants.LAST_NAME, Activator.PLUGIN_ID)
				.store(name);
		String url = mainPage.getUrl();
		new StringPreferenceValue(IDeltaCloudPreferenceConstants.LAST_URL, Activator.PLUGIN_ID)
				.store(url);
		String username = mainPage.getUsername();
		new StringPreferenceValue(IDeltaCloudPreferenceConstants.LAST_USERNAME, Activator.PLUGIN_ID)
				.store(username);
		String password = mainPage.getPassword();
		DeltaCloudDriver driver = mainPage.getDriver();
		return editCloud(initialCloud, name, url, username, password, driver);
	}

	private boolean editCloud(final DeltaCloud cloud, final String name, final String url, final String username,
			final String password, final DeltaCloudDriver driver) {
		try {
			Job job = new AbstractCloudJob(WizardMessages.getFormattedString("EditCloudConnection.message",
					cloud.getName()), cloud) {

				@Override
				protected IStatus doRun(IProgressMonitor monitor) throws Exception {
					initialCloud.update(name, url, username, password, driver);
					return Status.OK_STATUS;
				}
			};
			WizardUtils.runInWizard(job, getContainer());
			return job.getResult() != null && job.getResult().getCode() != IStatus.ERROR;
		} catch (Exception e) {
			return false;
		}
	}

}
