package org.jboss.ide.eclipse.archives.core;

import org.eclipse.core.runtime.IPath;
import org.jboss.ide.eclipse.archives.core.ArchivesCore;
import org.jboss.ide.eclipse.archives.core.build.ModelChangeListenerWithRefresh;
import org.jboss.ide.eclipse.archives.core.model.ArchivesModel;
import org.jboss.ide.eclipse.archives.core.model.IArchivesLogger;
import org.jboss.ide.eclipse.archives.core.model.IExtensionManager;
import org.jboss.ide.eclipse.archives.core.model.IPreferenceManager;
import org.jboss.ide.eclipse.archives.core.model.IRuntimeVariables;
import org.jboss.ide.eclipse.archives.core.model.other.internal.ArchivesWorkspaceLogger;
import org.jboss.ide.eclipse.archives.core.model.other.internal.WorkspaceExtensionManager;
import org.jboss.ide.eclipse.archives.core.model.other.internal.WorkspacePreferenceManager;
import org.jboss.ide.eclipse.archives.core.model.other.internal.WorkspaceVariables;
import org.jboss.ide.eclipse.archives.core.project.ProjectUtils;

public class WorkspaceArchivesCore extends ArchivesCore {

	public WorkspaceArchivesCore ()
	{
		super(WORKSPACE);
		ArchivesCore.setInstance(this);
		ArchivesModel.instance().addModelListener(new ModelChangeListenerWithRefresh());
	}
	
	protected IExtensionManager createExtensionManager() {
		return new WorkspaceExtensionManager();
	}
	
	protected IPreferenceManager createPreferenceManager() {
		return new WorkspacePreferenceManager();
	}
	
	protected IRuntimeVariables createVariables() {
		return new WorkspaceVariables();
	}

	public void preRegisterProject(IPath project) {
		ProjectUtils.addProjectNature(project);
	}

	protected IArchivesLogger createLogger() {
		return new ArchivesWorkspaceLogger();
	}
	
}
