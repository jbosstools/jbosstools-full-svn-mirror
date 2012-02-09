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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jgit.errors.TransportException;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.jboss.tools.common.ui.DelegatingProgressMonitor;
import org.jboss.tools.common.ui.JobUtils;
import org.jboss.tools.common.ui.WizardUtils;
import org.jboss.tools.openshift.express.internal.ui.ImportFailedException;
import org.jboss.tools.openshift.express.internal.ui.OpenShiftUIActivator;
import org.jboss.tools.openshift.express.internal.ui.WontOverwriteException;

import com.openshift.express.client.IApplication;
import com.openshift.express.client.IUser;
import com.openshift.express.client.OpenShiftException;

/**
 * @author Andre Dietisheim
 * @author Xavier Coulon
 */
public class OpenShiftExpressApplicationWizard extends
		AbstractOpenShiftApplicationWizard<OpenShiftExpressApplicationWizardModel> implements IImportWizard, INewWizard {

	public OpenShiftExpressApplicationWizard() {
		super();
		setWizardModel(new OpenShiftExpressApplicationWizardModel());
	}

	public void setSelectedApplication(IApplication application) {
		getWizardModel().setApplication(application);
	}

	public void setSelectedProject(IProject project) {
		if (project != null && project.exists()) {
			getWizardModel().setExistingProject(false);
			getWizardModel().setProjectName(project.getName());
		} else {
			getWizardModel().setExistingProject(true);
			getWizardModel().setProjectName(null);
		}
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setWindowTitle("OpenShift Application Wizard");
		setNeedsProgressMonitor(true);
	}


	@Override
	public void addPages() {
		final IUser user = OpenShiftUIActivator.getDefault().getUser();
		try {
			if (user == null || !user.isValid()) {
				addPage(new CredentialsWizardPage(this));
			}
		} catch (OpenShiftException e) {
			// if the user's validity can't be checked, we may want to re-connect..
			addPage(new CredentialsWizardPage(this));
		}
		addPage(new ApplicationConfigurationWizardPage(this, getWizardModel()));
		addPage(new ProjectAndServerAdapterSettingsWizardPage(this, getWizardModel()));
		addPage(new GitCloningSettingsWizardPage(this, getWizardModel()));
	}

	@Override
	public boolean performFinish() {
		try {
			final DelegatingProgressMonitor delegatingMonitor = new DelegatingProgressMonitor();
			IStatus jobResult = WizardUtils.runInWizard(new ImportJob(delegatingMonitor), delegatingMonitor,
					getContainer());
			return JobUtils.isOk(jobResult);
		} catch (Exception e) {
			ErrorDialog.openError(getShell(), "Error", "Could not create local git repository.", new Status(
					IStatus.ERROR, OpenShiftUIActivator.PLUGIN_ID,
					"An exception occurred while creating local git repository.", e));
			return false;
		}
	}

	/**
	 * A workspace job that will create a new project or enable the selected project to be used with OpenShift.
	 */
	class ImportJob extends WorkspaceJob {

		private DelegatingProgressMonitor delegatingMonitor;

		public ImportJob(DelegatingProgressMonitor delegatingMonitor) {
			super("Importing project to workspace...");
			this.delegatingMonitor = delegatingMonitor;
		}

		@Override
		public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
			try {
				delegatingMonitor.add(monitor);
				OpenShiftExpressApplicationWizardModel wizardModel = getWizardModel();
				if (wizardModel.isNewProject()) {
					wizardModel.importProject(delegatingMonitor);
				} else if (!wizardModel.isGitSharedProject()) {
					if (!askForConfirmation(
							NLS.bind("OpenShift application {0} will be enabled on project {1} by "
									+ "copying OpenShift configuration and enabling Git for the project.\n "
									+ "This cannot be undone. Do you wish to continue ?",
									wizardModel.getApplicationName(), wizardModel.getProjectName()),
							wizardModel.getApplicationName())) {
						return Status.CANCEL_STATUS;
					}
					getWizardModel().configureUnsharedProject(delegatingMonitor);
				} else {
					if (!askForConfirmation(
							NLS.bind("OpenShift application {0} will be enabled on project {1} by copying OpenShift "
									+ "configuration and adding the OpenShift git repo as remote.\n "
									+ "This cannot be undone. Do you wish to continue ?",
									wizardModel.getApplicationName(), wizardModel.getProjectName()),
							wizardModel.getApplicationName())) {
						return Status.CANCEL_STATUS;
					}
					wizardModel.configureGitSharedProject(delegatingMonitor);
				}
				return Status.OK_STATUS;
			} catch (final WontOverwriteException e) {
				openError("Project already present", e.getMessage());
				return Status.CANCEL_STATUS;
			} catch (final ImportFailedException e) {
				return OpenShiftUIActivator.createErrorStatus("Could not import maven project {0}.", e,
						getWizardModel().getProjectName());
			} catch (IOException e) {
				return OpenShiftUIActivator.createErrorStatus(
						"Could not copy openshift configuration files to project {0}", e, getWizardModel()
								.getProjectName());
			} catch (OpenShiftException e) {
				return OpenShiftUIActivator.createErrorStatus("Could not import project to the workspace.", e);
			} catch (URISyntaxException e) {
				return OpenShiftUIActivator.createErrorStatus("The url of the remote git repository is not valid", e);
			} catch (InvocationTargetException e) {
				if (isTransportException(e)) {
					TransportException te = getTransportException(e);
					return OpenShiftUIActivator.createErrorStatus(
							"Could not clone the repository. Authentication failed.\n"
									+ " Please make sure that you added your private key to the ssh preferences.", te);
				} else {
					return OpenShiftUIActivator.createErrorStatus(
							"An exception occurred while creating local git repository.", e);
				}
			} catch (Exception e) {
				return OpenShiftUIActivator.createErrorStatus("Could not import project to the workspace.", e);
			} finally {
				delegatingMonitor.done();
			}
		}
	}

}
