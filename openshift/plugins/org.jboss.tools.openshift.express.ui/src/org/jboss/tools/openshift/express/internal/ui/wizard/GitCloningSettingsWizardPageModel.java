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

import static org.jboss.tools.openshift.express.internal.ui.wizard.IOpenShiftExpressWizardModel.EXISTING_PROJECT_REMOTE_NAME_DEFAULT;
import static org.jboss.tools.openshift.express.internal.ui.wizard.IOpenShiftExpressWizardModel.NEW_PROJECT_REMOTE_NAME_DEFAULT;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.osgi.util.NLS;
import org.jboss.tools.common.ui.databinding.ObservableUIPojo;
import org.jboss.tools.openshift.egit.core.EGitUtils;
import org.jboss.tools.openshift.egit.ui.util.EGitUIUtils;
import org.jboss.tools.openshift.express.internal.ui.OpenShiftUIActivator;

import com.openshift.express.client.IApplication;
import com.openshift.express.client.ICartridge;
import com.openshift.express.client.OpenShiftException;

/**
 * @author Andre Dietisheim
 * @author Rob Stryker
 * @author Xavier Coulon
 */
public class GitCloningSettingsWizardPageModel extends ObservableUIPojo {

	public static final String PROPERTY_NEW_PROJECT = "newProject";
	public static final String PROPERTY_CLONE_URI = "cloneUri";
	public static final String PROPERTY_APPLICATION_URL = "applicationUrl";
	public static final String PROPERTY_REPO_PATH = "repositoryPath";
	public static final String PROPERTY_REMOTE_NAME = "remoteName";
	public static final String PROPERTY_LOADING = "loading";

	public static final String CREATE_SERVER = "createServer";
	public static final String MODE = "serverMode";
	public static final String MODE_SOURCE = "serverModeSource";
	public static final String MODE_BINARY = "serverModeBinary";
	public static final String SERVER_TYPE = "serverType";

	public static final String PROPERTY_USE_DEFAULT_REPO_PATH = "useDefaultRepoPath";

	public static final String PROPERTY_CUSTOM_REPO_PATH_VALIDITY = "customRepoPathValidity";

	public static final String PROPERTY_USE_DEFAULT_REMOTE_NAME = "useDefaultRemoteName";

	public static final String PROPERTY_CUSTOM_REMOTE_NAME_VALIDITY = "customRemoteNameValidity";

	private IOpenShiftExpressWizardModel wizardModel;
	private boolean loading;

	private boolean useDefaultRepoPath = true;

	private IStatus customRepoPathValidity = null;

	private IStatus customRemoteNameValidity = null;

	private boolean useDefaultRemoteName = true;

	public GitCloningSettingsWizardPageModel(IOpenShiftExpressWizardModel wizardModel) {
		this.wizardModel = wizardModel;
		setRepositoryPath(getDefaultRepositoryPath());
	}

	public boolean isNewProject() {
		return wizardModel.isNewProject();
	}

	public void loadGitUri() throws OpenShiftException {
		setLoading(true);
		setCloneUri("Loading...");
		setCloneUri(getCloneUri());
		setLoading(false);
	}

	private void setCloneUri(String gitUri) {
		firePropertyChange(PROPERTY_CLONE_URI, null, gitUri);
	}

	public String getCloneUri() throws OpenShiftException {
		IApplication application = wizardModel.getApplication();
		if (application == null) {
			return null;
		}
		return application.getGitUri();
	}

	public void loadApplicationUrl() throws OpenShiftException {
		setLoading(true);
		setApplicationUrl("Loading...");
		setApplicationUrl(getApplicationUrl());
		setLoading(false);
	}

	public String getApplicationUrl() throws OpenShiftException {
		IApplication application = wizardModel.getApplication();
		if (application == null) {
			return null;
		}
		return application.getApplicationUrl();
	}

	public String getApplicationName() {
		return wizardModel.getApplicationName();
	}

	public boolean isJBossAS7Application() {
		IApplication application = wizardModel.getApplication();
		if (application == null) {
			return false;
		}
		return ICartridge.JBOSSAS_7.equals(application.getCartridge());
	}

	public void setApplicationUrl(String applicationUrl) {
		firePropertyChange(PROPERTY_APPLICATION_URL, null, applicationUrl);
	}

	public String getRepositoryPath() {
		return wizardModel.getRepositoryPath();
	}

	public void setRepositoryPath(String repositoryPath) {
		firePropertyChange(PROPERTY_REPO_PATH, wizardModel.getRepositoryPath(),
				wizardModel.setRepositoryPath(repositoryPath));
		validateRepoPathProject();

	}

	// public void resetRepositoryPath() {
	// if (wizardModel.isNewProject()
	// || getRepositoryPath() == null) {
	// setRepositoryPath(getDefaultRepositoryPath());
	// }
	// }

	public void resetRemoteName() {
		// if existing project and remote name is still 'origin' -> switch to
		// 'openshift'
		// (so, if existing project and remote name is not 'origin', leave as-is
		if (!wizardModel.isNewProject() && NEW_PROJECT_REMOTE_NAME_DEFAULT.equals(getRemoteName())) {
			setRemoteName(EXISTING_PROJECT_REMOTE_NAME_DEFAULT);
		}
		// if new project and remote name is not 'origin' -> restore 'origin'
		else if (wizardModel.isNewProject() && !NEW_PROJECT_REMOTE_NAME_DEFAULT.equals(getRemoteName())) {
			setUseDefaultRemoteName(true);
			setRemoteName(NEW_PROJECT_REMOTE_NAME_DEFAULT);
		}
	}

	private String getDefaultRepositoryPath() {
		return EGitUIUtils.getEGitDefaultRepositoryPath();
	}

	public String getRemoteName() {
		return wizardModel.getRemoteName();
	}

	public void setRemoteName(String remoteName) {
		firePropertyChange(PROPERTY_REMOTE_NAME, wizardModel.getRemoteName(), wizardModel.setRemoteName(remoteName));
		validateRemoteName();
	}

	public boolean isLoading() {
		return loading;
	}

	public void setLoading(boolean loading) {
		firePropertyChange(PROPERTY_LOADING, this.loading, this.loading = loading);
	}

	public boolean isCompatibleToApplicationCartridge(ICartridge cartridge) {
		IApplication application = wizardModel.getApplication();
		return application != null && application.getCartridge() != null
				&& application.getCartridge().equals(cartridge);
	}

	public static class GitUri {

		private String label;
		private String gitUri;
		private ICartridge cartridge;

		private GitUri(String label, String gitUri, ICartridge cartridge) {
			this.label = label;
			this.gitUri = gitUri;
			this.cartridge = cartridge;
		}

		public String getLabel() {
			return label;
		}

		public String getGitUri() {
			return gitUri;
		}

		public String toString() {
			return getLabel();
		}

		public ICartridge getCartridge() {
			return cartridge;
		}

		public boolean isCompatible(ICartridge cartridge) {
			return this.cartridge.equals(cartridge);
		}
	}

	public void setUseDefaultRepoPath(boolean useDefaultRepoPath) {
		firePropertyChange(PROPERTY_USE_DEFAULT_REPO_PATH, useDefaultRepoPath,
				this.useDefaultRepoPath = useDefaultRepoPath);
		if (this.useDefaultRepoPath) {
			setRepositoryPath(getDefaultRepositoryPath());
		} else {

		}
		validateRepoPathProject();
	}

	public boolean isUseDefaultRepoPath() {
		return useDefaultRepoPath;
	}

	private IStatus validateRepoPathProject() {
		IStatus status = Status.OK_STATUS;
		// skip the validation if the user wants to create a new project. The
		// name and state of the existing project do
		// not matter...
		String applicationName = getApplicationName();
		if (applicationName == null
				|| applicationName.length() == 0) {
			status = OpenShiftUIActivator
					.createCancelStatus("You have to choose an application name / existing application");
		} else {
			if (!isUseDefaultRepoPath()) {
				final IPath repoPath = new Path(getRepositoryPath());
				if (repoPath.isEmpty()
						|| !repoPath.isAbsolute()
						|| !repoPath.toFile().canWrite()) {
					status = OpenShiftUIActivator.createErrorStatus("The path does not exist or is not writeable.");
				} else {
					final IPath applicationPath = repoPath.append(new Path(getApplicationName()));
					if (applicationPath.toFile().exists()) {
						status = OpenShiftUIActivator.createErrorStatus(
								"The location '" + repoPath.toOSString() + "' already contains a folder named '"
										+ getApplicationName() + "'.");
					}
				}
			}
		}

		setCustomRepoPathValidity(status);
		return status;
	}

	public void setCustomRepoPathValidity(IStatus status) {
		firePropertyChange(PROPERTY_CUSTOM_REPO_PATH_VALIDITY, this.customRepoPathValidity,
				this.customRepoPathValidity = status);
	}

	public IStatus getCustomRepoPathValidity() {
		return this.customRepoPathValidity;
	}

	public void setUseDefaultRemoteName(boolean useDefaultRemoteName) {
		firePropertyChange(PROPERTY_USE_DEFAULT_REMOTE_NAME, useDefaultRemoteName,
				this.useDefaultRemoteName = useDefaultRemoteName);
		if (useDefaultRemoteName) {
			setRemoteName(isNewProject() ? NEW_PROJECT_REMOTE_NAME_DEFAULT : EXISTING_PROJECT_REMOTE_NAME_DEFAULT);
		}
		validateRemoteName();
	}

	public boolean isUseDefaultRemoteName() {
		return useDefaultRemoteName;
	}

	private IStatus validateRemoteName() {
		IStatus status = Status.OK_STATUS;
		// skip the validation if the user wants to create a new project. The
		// name and state of the existing project do
		// not matter...
		if (!isUseDefaultRemoteName()) {
			final String remoteName = getRemoteName();
			if (remoteName == null || remoteName.isEmpty()) {
				status = new Status(IStatus.ERROR, OpenShiftUIActivator.PLUGIN_ID,
						"The custom remote name must not be empty.");
			} else if (!remoteName.matches("\\S+")) {
				status = new Status(IStatus.ERROR, OpenShiftUIActivator.PLUGIN_ID,
						"The custom remote name must not contain spaces.");
			} else if (hasRemoteName(remoteName, getProject())) {
				status = new Status(IStatus.ERROR,
						OpenShiftUIActivator.PLUGIN_ID, NLS.bind(
								"The existing project already has a remote named {0}.", remoteName));
			}
		}
		setCustomRemoteNameValidity(status);
		return status;
	}

	private boolean hasRemoteName(String remoteName, IProject project) {
		try {
			if (project == null
					|| !project.isAccessible()) {
				return false;
			}

			Repository repository = EGitUtils.getRepository(project);
			return EGitUtils.hasRemote(remoteName, repository);
		} catch (Exception e) {
			OpenShiftUIActivator.log(OpenShiftUIActivator.createErrorStatus(e.getMessage(), e));
			return false;
		}
	}

	private IProject getProject() {
		String projectName = wizardModel.getProjectName();
		if (projectName == null) {
			return null;
		}

		return ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
	}

	public void setCustomRemoteNameValidity(IStatus status) {
		firePropertyChange(PROPERTY_CUSTOM_REMOTE_NAME_VALIDITY, this.customRemoteNameValidity,
				this.customRemoteNameValidity = status);
	}

	public IStatus getCustomRemoteNameValidity() {
		return this.customRemoteNameValidity;
	}

}
