package org.jboss.ide.eclipse.archives.core;

import org.jboss.ide.eclipse.archives.core.model.other.IExtensionManager;
import org.jboss.ide.eclipse.archives.core.model.other.IPreferenceManager;
import org.jboss.ide.eclipse.archives.core.model.other.IRuntimeVariables;
import org.jboss.ide.eclipse.archives.core.model.other.internal.WorkspaceExtensionManager;
import org.jboss.ide.eclipse.archives.core.model.other.internal.WorkspacePreferenceManager;
import org.jboss.ide.eclipse.archives.core.model.other.internal.WorkspaceVariables;

public class ArchivesCore {

	private static ArchivesCore instance;
	public static ArchivesCore getInstance() {
		if( instance == null )
			instance = new ArchivesCore(WORKSPACE);
		return instance;
	}
	public static void create(int type) {
		instance = new ArchivesCore(type);
	}
	
	public static final int STANDALONE = 0;
	public static final int WORKSPACE = 1;
	
	private int runType;
	private IRuntimeVariables variables;
	private IExtensionManager extensionManager;
	private IPreferenceManager preferenceManager;
	
	public ArchivesCore(int runType) {
		this.runType = runType;
		if( this.runType == STANDALONE) {
			//variables = new StandaloneVariables();
		} else {
			variables = new WorkspaceVariables();
			extensionManager = new WorkspaceExtensionManager();
			preferenceManager = new WorkspacePreferenceManager();
		}
	}
	
	public boolean isWorkspaceRuntype() {
		return runType == WORKSPACE;
	}
	
	public IRuntimeVariables getVariables() {
		return variables;
	}
	public IExtensionManager getExtensionManager() {
		return extensionManager;
	}
	public IPreferenceManager getPreferenceManager() {
		return preferenceManager;
	}
}
