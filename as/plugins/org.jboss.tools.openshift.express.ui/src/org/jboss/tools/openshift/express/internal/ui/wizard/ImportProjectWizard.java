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

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.jboss.tools.common.ui.WizardUtils;
import org.jboss.tools.openshift.express.client.OpenshiftException;
import org.jboss.tools.openshift.express.internal.ui.OpenshiftUIActivator;

/**
 * @author André Dietisheim
 */
public class ImportProjectWizard extends Wizard implements INewWizard {

	private ImportProjectWizardModel model;

	public ImportProjectWizard() {
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setWindowTitle("OpenShift application wizard");
		setNeedsProgressMonitor(true);
	}

	@Override
	public boolean performFinish() {
		try {
			WizardUtils.runInWizard(
					new Job("Creating local git repo...") {

						@Override
						protected IStatus run(IProgressMonitor monitor) {
							IStatus status = Status.OK_STATUS;
							try {
								File repositoryFile = model.cloneRepository(monitor);
								model.importProject(repositoryFile, monitor);
							} catch (OpenshiftException e) {
								status = new Status(IStatus.ERROR, OpenshiftUIActivator.PLUGIN_ID,
										"An exception occurred while creating local git repository.", e);
							} catch (URISyntaxException e) {
								status = new Status(IStatus.ERROR, OpenshiftUIActivator.PLUGIN_ID,
										"The url of the remote git repository is not valid", e);
							} catch (InvocationTargetException e) {
								if (e.getTargetException() instanceof JGitInternalException) {
									status = new Status(IStatus.ERROR, OpenshiftUIActivator.PLUGIN_ID,
											"Could not clone the repository. Authentication failed.", e
													.getTargetException());
								} else {
									status = new Status(IStatus.ERROR, OpenshiftUIActivator.PLUGIN_ID,
											"An exception occurred while creating local git repository.", e);
								}
							} catch (Exception e) {
								status = new Status(IStatus.ERROR, OpenshiftUIActivator.PLUGIN_ID,
										"An exception occurred while creating local git repository.", e);
							}

							if (!status.isOK()) {
								OpenshiftUIActivator.log(status);
							}
							return status;
						}
					}, getContainer());
			return true;
		} catch (Exception e) {
			ErrorDialog.openError(getShell(), "Error", "Could not create local git repository.",
					new Status(IStatus.ERROR, OpenshiftUIActivator.PLUGIN_ID,
							"An exception occurred while creating local git repository.", e));
			return false;
		}
	}

	@Override
	public void addPages() {
		this.model = new ImportProjectWizardModel();
		addPage(new CredentialsWizardPage(this, model));
		addPage(new ApplicationWizardPage(this, model));
		addPage(new AdapterWizardPage(this, model));
	}
}
