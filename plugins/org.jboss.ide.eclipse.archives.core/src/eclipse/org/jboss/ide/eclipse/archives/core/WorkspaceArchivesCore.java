package org.jboss.ide.eclipse.archives.core;



import org.jboss.ide.eclipse.archives.core.ArchivesCore;
import org.jboss.ide.eclipse.archives.core.model.other.IExtensionManager;
import org.jboss.ide.eclipse.archives.core.model.other.IPreferenceManager;
import org.jboss.ide.eclipse.archives.core.model.other.IRuntimeVariables;
import org.jboss.ide.eclipse.archives.core.model.other.internal.WorkspaceExtensionManager;
import org.jboss.ide.eclipse.archives.core.model.other.internal.WorkspacePreferenceManager;
import org.jboss.ide.eclipse.archives.core.model.other.internal.WorkspaceVariables;

public class WorkspaceArchivesCore extends ArchivesCore {

	public WorkspaceArchivesCore ()
	{
		super(WORKSPACE);
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
}
