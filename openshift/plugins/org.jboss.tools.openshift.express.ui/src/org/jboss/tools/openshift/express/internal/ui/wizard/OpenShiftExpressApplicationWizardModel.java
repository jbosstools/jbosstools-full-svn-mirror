package org.jboss.tools.openshift.express.internal.ui.wizard;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IServerType;
import org.eclipse.wst.server.core.ServerCore;
import org.jboss.tools.common.ui.databinding.ObservableUIPojo;
import org.jboss.tools.openshift.egit.core.EGitUtils;
import org.jboss.tools.openshift.express.internal.core.behaviour.ExpressServerUtils;
import org.jboss.tools.openshift.express.internal.ui.OpenShiftUIActivator;
import org.jboss.tools.openshift.express.internal.ui.messages.OpenShiftExpressUIMessages;
import org.jboss.tools.openshift.express.internal.ui.wizard.appimport.ConfigureGitSharedProject;
import org.jboss.tools.openshift.express.internal.ui.wizard.appimport.ConfigureUnsharedProject;
import org.jboss.tools.openshift.express.internal.ui.wizard.appimport.ImportNewProject;
import org.jboss.tools.openshift.express.internal.ui.wizard.appimport.ServerAdapterFactory;

import com.openshift.express.client.IApplication;
import com.openshift.express.client.ICartridge;
import com.openshift.express.client.IEmbeddableCartridge;
import com.openshift.express.client.IUser;
import com.openshift.express.client.OpenShiftApplicationNotAvailableException;
import com.openshift.express.client.OpenShiftException;

public class OpenShiftExpressApplicationWizardModel extends ObservableUIPojo implements IOpenShiftWizardModel {

	protected HashMap<String, Object> dataModel = new HashMap<String, Object>();
	
	private static final int APP_CREATION_TIMEOUT = 40;
	private static final String KEY_SELECTED_EMBEDDABLE_CARTRIDGES = "selectedEmbeddableCartridges";

	
	public OpenShiftExpressApplicationWizardModel() {
		super();
		// default value(s)
		setNewProject(true);
		setCreateServerAdapter(true);
		setRepositoryPath(DEFAULT_REPOSITORY_PATH);
		setRemoteName(NEW_PROJECT_REMOTE_NAME_DEFAULT);
		setServerType(ServerCore.findServerType(ExpressServerUtils.OPENSHIFT_SERVER_TYPE));
		setPublicationMode(PUBLISH_SOURCE);
		setUseExistingApplication(false);
	}

	
	/**
	 * Imports the project that the user has chosen into the workspace.
	 * 
	 * @param monitor
	 *            the monitor to report progress to
	 * @throws OpenShiftException
	 * @throws CoreException
	 * @throws InterruptedException
	 * @throws URISyntaxException
	 * @throws InvocationTargetException
	 */
	@Override
	public void importProject(IProgressMonitor monitor) throws OpenShiftException, CoreException, InterruptedException,
			URISyntaxException, InvocationTargetException {
		List<IProject> importedProjects = new ImportNewProject(getProjectName(), getApplication(), getRemoteName(),
				getRepositoryFile()).execute(monitor);
		createServerAdapter(monitor, importedProjects);
	}

	/**
	 * Enables the user chosen, unshared project to be used on the chosen
	 * OpenShift application. Clones the application git repository, copies the
	 * configuration files to the user project (in the workspace), shares the
	 * user project with git and creates the server adapter.
	 * 
	 * @param monitor
	 *            the monitor to report progress to
	 * @throws URISyntaxException
	 *             The OpenShift application repository could not be cloned,
	 *             because the uri it is located at is not a valid git uri
	 * @throws OpenShiftException
	 * 
	 * @throws InvocationTargetException
	 *             The OpenShift application repository could not be cloned, the
	 *             clone operation failed.
	 * @throws InterruptedException
	 *             The OpenShift application repository could not be cloned, the
	 *             clone operation was interrupted.
	 * @throws IOException
	 *             The configuration files could not be copied from the git
	 *             clone to the user project
	 * @throws CoreException
	 *             The user project could not be shared with the git
	 */
	@Override
	public void configureUnsharedProject(IProgressMonitor monitor)
			throws OpenShiftException, InvocationTargetException, InterruptedException, IOException, CoreException,
			URISyntaxException {
		List<IProject> importedProjects = new ConfigureUnsharedProject(
				getProjectName()
				, getApplication()
				, getRemoteName()
				, OpenShiftUIActivator.getDefault().getUser())
				.execute(monitor);
		createServerAdapter(monitor, importedProjects);
	}

	/**
	 * Enables the user chosen, unshared project to be used on the chosen
	 * OpenShift application. Clones the application git repository, copies the
	 * configuration files to the user project (in the workspace), adds the
	 * appication git repo as remote and creates the server adapter.
	 * 
	 * @param monitor
	 *            the monitor to report progress to
	 * @throws URISyntaxException
	 *             The OpenShift application repository could not be cloned,
	 *             because the uri it is located at is not a valid git uri
	 * @throws OpenShiftException
	 * 
	 * @throws InvocationTargetException
	 *             The OpenShift application repository could not be cloned, the
	 *             clone operation failed.
	 * @throws InterruptedException
	 *             The OpenShift application repository could not be cloned, the
	 *             clone operation was interrupted.
	 * @throws IOException
	 *             The configuration files could not be copied from the git
	 *             clone to the user project
	 * @throws CoreException
	 *             The user project could not be shared with the git
	 */
	@Override
	public void configureGitSharedProject(IProgressMonitor monitor)
			throws OpenShiftException, InvocationTargetException, InterruptedException, IOException, CoreException,
			URISyntaxException {
		List<IProject> importedProjects = new ConfigureGitSharedProject(
				getProjectName()
				, getApplication()
				, getRemoteName()
//				, getUser())
				, OpenShiftUIActivator.getDefault().getUser())
				.execute(monitor);
		createServerAdapter(monitor, importedProjects);
	}

	private void createServerAdapter(IProgressMonitor monitor, List<IProject> importedProjects)
			throws OpenShiftException {
		Assert.isTrue(importedProjects.size() > 0);
		IProject project = importedProjects.get(0);
		new ServerAdapterFactory().create(project, this, monitor);
	}

	@Override
	public File getRepositoryFile() {
		String repositoryPath = getRepositoryPath();
		if (repositoryPath == null || repositoryPath.length() == 0) {
			return null;
		}
		return new File(repositoryPath, getApplicationName());
	}

	@Override
	public Object setProperty(String key, Object value) {
		Object oldVal = dataModel.get(key);
		dataModel.put(key, value);
		firePropertyChange(key, oldVal, value);
		return value;
	}

	@Override
	public Object getProperty(String key) {
		return dataModel.get(key);
	}

//	@Override
//	public void setUser(IUser user) {
//		setProperty(USER, user);
//		OpenShiftUIActivator.getDefault().setUser(user);
//	}

//	@Override
//	public IUser getUser() {
//		return (IUser) getProperty(USER);
//		return OpenShiftUIActivator.getDefault().getUser();
//	}

	@Override
	public IApplication getApplication() {
		return (IApplication) getProperty(APPLICATION);
	}

	@Override
	public String getApplicationName() {
		String applicationName = null;
		IApplication application = getApplication();
		if (application != null) {
			applicationName = application.getName();
		}
		return applicationName;
	}

	@Override
	public ICartridge getApplicationCartridge() {
		ICartridge cartridge = null;
		IApplication application = getApplication();
		if (application != null) {
			cartridge = application.getCartridge();
		}
		return cartridge;
	}

	@Override
	public String getApplicationCartridgeName() {
		String cartridgeName = null;
		ICartridge cartridge = getApplicationCartridge();
		if (cartridge != null) {
			cartridgeName = cartridge.getName();
		}
		return cartridgeName;
	}

	@Override
	public void setApplication(IApplication application) {
		setProperty(APPLICATION, application);
		if(application == null) {
			setUseExistingApplication(false);
		} else {
			setUseExistingApplication(true);
		}
	}

	@Override
	public String setRemoteName(String remoteName) {
		setProperty(REMOTE_NAME, remoteName);
		return remoteName;
	}

	@Override
	public String getRemoteName() {
		return (String) getProperty(REMOTE_NAME);
	}

	@Override
	public String setRepositoryPath(String repositoryPath) {
		return (String) setProperty(REPOSITORY_PATH, repositoryPath);
	}

	@Override
	public String getRepositoryPath() {
		return (String) getProperty(REPOSITORY_PATH);
	}

	@Override
	public boolean isNewProject() {
		return (Boolean) getProperty(NEW_PROJECT);
	}

	@Override
	public boolean isExistingProject() {
		return !((Boolean) getProperty(NEW_PROJECT));
	}

	@Override
	public Boolean setNewProject(boolean newProject) {
		return (Boolean) setProperty(NEW_PROJECT, newProject);
	}

	@Override
	public Boolean setExistingProject(boolean existingProject) {
		return (Boolean) setProperty(NEW_PROJECT, !existingProject);
	}

	@Override
	public String setProjectName(String projectName) {
		return (String) setProperty(PROJECT_NAME, projectName);
	}

	@Override
	public boolean isGitSharedProject() {
		return EGitUtils.isSharedWithGit(getProject());
	}

	private IProject getProject() {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(getProjectName());
	}
	@Override
	public Boolean setCreateServerAdapter(Boolean createServerAdapter) {
		return (Boolean) setProperty(CREATE_SERVER_ADAPTER, createServerAdapter);
	}

	@Override
	public String getProjectName() {
		return (String) getProperty(PROJECT_NAME);
	}

	@Override
	public String setMergeUri(String mergeUri) {
		return (String) setProperty(MERGE_URI, mergeUri);
	}

	@Override
	public String getMergeUri() {
		return (String) getProperty(MERGE_URI);
	}

	@Override
	public IRuntime getRuntime() {
		return (IRuntime) getProperty(RUNTIME_DELEGATE);
	}

	@Override
	public String getMode() {
		return (String) getProperty(PUBLICATION_MODE);
	}

	@Override
	public boolean isCreateServerAdapter() {
		Boolean isCreateServer = (Boolean) getProperty(CREATE_SERVER_ADAPTER);
		return isCreateServer != null && isCreateServer.booleanValue();
	}

	@Override
	public IServerType getServerType() {
		return (IServerType) getProperty(SERVER_TYPE);
	}

	@Override
	public void setServerType(IServerType serverType) {
		setProperty(SERVER_TYPE, serverType);
	}
	
	private void setPublicationMode(String mode) {
		setProperty(PUBLICATION_MODE, mode);
	}


	@Override
	public boolean isExistingApplication() {
		return (Boolean) getProperty(USE_EXISTING_APPLICATION);
	}


	@Override
	public void setUseExistingApplication(boolean useExistingApplication) {
		setProperty(USE_EXISTING_APPLICATION, useExistingApplication);
	}


	private void waitForAccessible(IApplication application, IProgressMonitor monitor)
			throws OpenShiftApplicationNotAvailableException, OpenShiftException {
		// monitor.subTask("waiting for application to become accessible...");
		if (!application.waitForAccessible(APP_CREATION_TIMEOUT * 1000)) {
			throw new OpenShiftApplicationNotAvailableException(NLS.bind(
					OpenShiftExpressUIMessages.HOSTNAME_NOT_ANSWERING, application.getApplicationUrl()));
		}
	}
	
	IApplication createApplication(String name, ICartridge cartridge, IProgressMonitor monitor) throws OpenShiftApplicationNotAvailableException, OpenShiftException {
		IUser user = OpenShiftUIActivator.getDefault().getUser();
		if (user == null) {
			throw new OpenShiftException("Could not create application, have no valid user credentials");
		}
		IApplication application = user.createApplication(name, cartridge);
		waitForAccessible(application, monitor);
		return application;
	}
	
	public void createApplication(IProgressMonitor monitor) throws OpenShiftApplicationNotAvailableException, OpenShiftException {
		IApplication application = createApplication(getApplicationName(), getApplicationCartridge(), monitor);
		setApplication(application);
		
	}


	public List<IEmbeddableCartridge> getSelectedEmbeddableCartridges() {
		@SuppressWarnings("unchecked")
		List<IEmbeddableCartridge> selectedEmbeddableCartridges = (List<IEmbeddableCartridge>) dataModel.get(KEY_SELECTED_EMBEDDABLE_CARTRIDGES);
		if(selectedEmbeddableCartridges == null) {
			selectedEmbeddableCartridges = new ArrayList<IEmbeddableCartridge>();
			dataModel.put(KEY_SELECTED_EMBEDDABLE_CARTRIDGES, selectedEmbeddableCartridges);
		}
		return selectedEmbeddableCartridges;
	}


	public void setApplicationCartridge(ICartridge cartridge) {
		// TODO Auto-generated method stub
		
	}


	public void setApplicationName(String applicationName) {
		// TODO Auto-generated method stub
		
	}

}