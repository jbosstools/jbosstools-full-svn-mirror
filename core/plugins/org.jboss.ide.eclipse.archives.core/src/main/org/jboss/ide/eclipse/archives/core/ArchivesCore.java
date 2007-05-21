package org.jboss.ide.eclipse.archives.core;

import org.jboss.ide.eclipse.archives.core.model.other.IExtensionManager;
import org.jboss.ide.eclipse.archives.core.model.other.IPreferenceManager;
import org.jboss.ide.eclipse.archives.core.model.other.IRuntimeVariables;

public abstract class ArchivesCore {

	private static ArchivesCore instance;
	// Due to classloader restrictions we won't be able to lazy load, but that should be ok as long
	// as we keep the construction of ArchivesCore subclasses to a minimum
	public static ArchivesCore getInstance() {
		return instance;
	}
	
	public static final int STANDALONE = 0;
	public static final int WORKSPACE = 1;
	
	private int runType;
	private IRuntimeVariables variables;
	private IExtensionManager extensionManager;
	private IPreferenceManager preferenceManager;
	
	public ArchivesCore(int runType) {
		this.runType = runType;
		variables = createVariables();
		extensionManager = createExtensionManager();
		preferenceManager = createPreferenceManager();
		
		instance = this;
	}
	
	protected abstract IRuntimeVariables createVariables();
	protected abstract IExtensionManager createExtensionManager();
	protected abstract IPreferenceManager createPreferenceManager();
	
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
