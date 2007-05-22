package org.jboss.ide.eclipse.archives.core;

import org.eclipse.core.runtime.IPath;
import org.jboss.ide.eclipse.archives.core.model.other.IExtensionManager;
import org.jboss.ide.eclipse.archives.core.model.other.IPreferenceManager;
import org.jboss.ide.eclipse.archives.core.model.other.IRuntimeVariables;
import org.jboss.ide.eclipse.archives.core.model.other.internal.StandaloneExtensionManager;
import org.jboss.ide.eclipse.archives.core.model.other.internal.StandalonePreferenceManager;
import org.jboss.ide.eclipse.archives.core.model.other.internal.StandaloneVariables;

public class StandaloneArchivesCore extends ArchivesCore {

	public StandaloneArchivesCore ()
	{
		super(STANDALONE);
	}
	
	protected IExtensionManager createExtensionManager() {
		return new StandaloneExtensionManager();
	}

	protected IPreferenceManager createPreferenceManager() {
		return new StandalonePreferenceManager();
	}

	protected IRuntimeVariables createVariables() {
		return new StandaloneVariables();
	}

	public void preRegister(IPath project) {
		
	}
}
