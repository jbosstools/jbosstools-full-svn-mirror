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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.errors.TransportException;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.jboss.tools.common.ui.DelegatingProgressMonitor;
import org.jboss.tools.common.ui.JobUtils;
import org.jboss.tools.common.ui.WizardUtils;
import org.jboss.tools.openshift.express.internal.core.console.UserDelegate;
import org.jboss.tools.openshift.express.internal.ui.ImportFailedException;
import org.jboss.tools.openshift.express.internal.ui.OpenShiftUIActivator;
import org.jboss.tools.openshift.express.internal.ui.WontOverwriteException;
import org.jboss.tools.openshift.express.internal.ui.job.AbstractDelegatingMonitorJob;
import org.jboss.tools.openshift.express.internal.ui.job.CreateApplicationJob;
import org.jboss.tools.openshift.express.internal.ui.job.EmbedCartridgesJob;
import org.jboss.tools.openshift.express.internal.ui.job.WaitForApplicationJob;

import com.openshift.client.IApplication;
import com.openshift.client.IEmbeddableCartridge;
import com.openshift.client.IEmbeddedCartridge;
import com.openshift.client.OpenShiftException;

/**
 * @author Andre Dietisheim
 * @author Xavier Coulon
 */
public abstract class OpenShiftExpressApplicationWizard extends Wizard implements IImportWizard, INewWizard {

	private static final int APP_CREATE_TIMEOUT = 2 * 60 * 1000;
	private static final int APP_WAIT_TIMEOUT = 2 * 60 * 1000;
	private static final long EMBED_CARTRIDGES_TIMEOUT = 2 * 60 * 1000;
	private static final int IMPORT_TIMEOUT = 1 * 60 * 1000;

	private final boolean skipCredentialsPage;

	private final OpenShiftExpressApplicationWizardModel wizardModel;

	OpenShiftExpressApplicationWizard(final boolean useExistingApplication, final String wizardTitle) {
		this(null, null, null, useExistingApplication, wizardTitle);
	}

	OpenShiftExpressApplicationWizard(UserDelegate user, IProject project, IApplication application,
			boolean useExistingApplication, String wizardTitle) {
		setWindowTitle(wizardTitle);
		setNeedsProgressMonitor(true);
		this.wizardModel = new OpenShiftExpressApplicationWizardModel(user, project, application,
				useExistingApplication);
		this.skipCredentialsPage = (user != null);
	}

	OpenShiftExpressApplicationWizardModel getWizardModel() {
		return wizardModel;
	}

	protected boolean isTransportException(InvocationTargetException e) {
		return e.getTargetException() instanceof JGitInternalException
				&& e.getTargetException().getCause() instanceof TransportException;
	}

	protected TransportException getTransportException(InvocationTargetException e) {
		if (isTransportException(e)) {
			return (TransportException) ((JGitInternalException) e.getTargetException()).getCause();
		}
		return null;
	}

	protected void openError(final String title, final String message) {
		getShell().getDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				MessageDialog.openError(getShell(), title, message);
			}
		});
	}

	protected boolean askForConfirmation(final String message, final String applicationName) {
		final boolean[] confirmed = new boolean[1];
		getShell().getDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				confirmed[0] = MessageDialog.openConfirm(getShell(),
						NLS.bind("Import OpenShift Application ", applicationName), message);
			}
		});
		return confirmed[0];
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		Object o = selection.getFirstElement();
		if (o instanceof UserDelegate) {
			setUser((UserDelegate) o);
		}
	}

	protected void setUser(UserDelegate user) {
		getWizardModel().setUser(user);
	}

	@Override
	public void addPages() {
		if (!skipCredentialsPage) {
			addPage(new CredentialsWizardPage(this, getWizardModel()));
		}
		addPage(new ApplicationConfigurationWizardPage(this, getWizardModel()));
		addPage(new ProjectAndServerAdapterSettingsWizardPage(this, getWizardModel()));
		addPage(new GitCloningSettingsWizardPage(this, getWizardModel()));
	}

	@Override
	public boolean performFinish() {
		boolean success = getWizardModel().isUseExistingApplication();
		if (!success) {

			IStatus status = createApplication();
			if (JobUtils.isCancel(status)) {
				getContainer().getShell().close();
			} else if (!JobUtils.isOk(status)) {
				return false;
			}

			if (success = waitForApplication(wizardModel.getApplication())) {
				success = addRemoveCartridges(
						getWizardModel().getApplication(), getWizardModel().getSelectedEmbeddableCartridges());
			} else {
				getContainer().getShell().close();
			}
		}

		if (success) {
			success = importProject();
		}

		wizardModel.addUserToModel();
		return success;
	}

	private boolean waitForApplication(IApplication application) {
		try {
			AbstractDelegatingMonitorJob job = new WaitForApplicationJob(application, getShell());
			IStatus status = WizardUtils.runInWizard(
					job, job.getDelegatingProgressMonitor(), getContainer(), APP_WAIT_TIMEOUT);
			return status.isOK();
		} catch (Exception e) {
			return false;
		}
	}

	private boolean importProject() {
		try {
			final DelegatingProgressMonitor delegatingMonitor = new DelegatingProgressMonitor();
			IStatus jobResult = WizardUtils.runInWizard(
					new ImportJob(delegatingMonitor), delegatingMonitor, getContainer(), IMPORT_TIMEOUT);
			return JobUtils.isOk(jobResult);
		} catch (Exception e) {
			ErrorDialog.openError(getShell(), "Error", "Could not create local git repository.", OpenShiftUIActivator
					.createErrorStatus("An exception occurred while creating local git repository.", e));
			return false;
		}
	}

	private IStatus createApplication() {
		try {
			CreateApplicationJob job = new CreateApplicationJob(
					wizardModel.getApplicationName()
					, wizardModel.getApplicationCartridge() 
					, wizardModel.getApplicationScale()
					, wizardModel.getApplicationGearProfile()
					, wizardModel.getUser());
			IStatus status = WizardUtils.runInWizard(
					job, job.getDelegatingProgressMonitor(), getContainer(), APP_CREATE_TIMEOUT);
			wizardModel.setApplication(job.getApplication());
			return status;
		} catch (Exception e) {
			return OpenShiftUIActivator.createErrorStatus(
					NLS.bind("Could not create application {0}", wizardModel.getApplicationName()), e);
		}
	}

	private boolean addRemoveCartridges(final IApplication application,final Set<IEmbeddableCartridge> selectedCartridges) {
			try {
				EmbedCartridgesJob job = new EmbedCartridgesJob(
						new ArrayList<IEmbeddableCartridge>(wizardModel.getSelectedEmbeddableCartridges()), 
						wizardModel.getApplication());
				IStatus result = WizardUtils.runInWizard(job, job.getDelegatingProgressMonitor(), getContainer(), EMBED_CARTRIDGES_TIMEOUT);
				if (result.isOK()) {
					openLogDialog(job.getAddedCartridges());
				}
				return result.isOK();
			} catch (Exception e) {
				return false;
			}
	}

	private void openLogDialog(final List<IEmbeddedCartridge> embeddableCartridges) {
		if (embeddableCartridges == null
				|| embeddableCartridges.isEmpty()) {
			return;
		}
		getShell().getDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				new CreationLogDialog(getShell(), embeddableCartridges).open();
			}
		});
	}

	/**
	 * A workspace job that will create a new project or enable the selected
	 * project to be used with OpenShift.
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
							NLS.bind(
									"OpenShift application \"{0}\" will be enabled on project \"{1}\" by "
											+ "copying OpenShift configuration and enabling Git for the project.\n"
											+ "This cannot be undone.\n"
											+ "Furthermore publishing to your OpenShift app will eventually override existing content.\n"
											+ "\nDo you wish to continue ?",
									wizardModel.getApplicationName(), wizardModel.getProjectName()),
							wizardModel.getApplicationName())) {
						return Status.CANCEL_STATUS;
					}
					getWizardModel().configureUnsharedProject(delegatingMonitor);
				} else {
					if (!askForConfirmation(
							NLS.bind(
									"OpenShift application \"{0}\" will be enabled on project \"{1}\" by "
											+ "copying OpenShift configuration and enabling Git for the project.\n"
											+ "This cannot be undone.\n"
											+ "Furthermore publishing to your OpenShift app will eventually override existing content.\n"
											+ "\nDo you wish to continue ?",
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

	@Override
	public void dispose() {
		wizardModel.dispose();
	}

}
