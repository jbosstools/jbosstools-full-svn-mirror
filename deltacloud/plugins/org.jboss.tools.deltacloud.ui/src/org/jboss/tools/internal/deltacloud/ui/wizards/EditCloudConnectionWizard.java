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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.jboss.tools.common.log.StatusFactory;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudDriver;
import org.jboss.tools.deltacloud.ui.Activator;
import org.jboss.tools.internal.deltacloud.ui.utils.WizardUtils;

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
	public boolean performFinish() {
		String name = mainPage.getConnectionName();
		String url = mainPage.getUrl();
		String username = mainPage.getUsername();
		String password = mainPage.getPassword();
		DeltaCloudDriver driver = mainPage.getDriver();
		return editCloud(name, url, username, password, driver);
	}

	private boolean editCloud(final String name, final String url, final String username, final String password,
			final DeltaCloudDriver driver)  {
		Job job = new Job(MessageFormat.format("Create cloud \"{0}\"", name)) {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					initialCloud.update(name, url, username, password, driver);
					return Status.OK_STATUS;
				} catch (Exception e) {
					// TODO internationalize strings
					return StatusFactory.getInstance(IStatus.ERROR, Activator.PLUGIN_ID,
							MessageFormat.format("Could not edit create cloud {0}", name), e);
				}
			}
		};
		try {
			WizardUtils.runInWizard(job, getContainer());
			return job.getResult() != null && job.getResult().getCode() != IStatus.ERROR;
		} catch (Exception e) {
			return false;
		}
	}

}
