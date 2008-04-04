package org.jboss.ide.eclipse.archives.core.ant;

import org.eclipse.core.runtime.IPath;
import org.jboss.ide.eclipse.archives.core.ArchivesCore;
import org.jboss.ide.eclipse.archives.core.build.ModelChangeListener;
import org.jboss.ide.eclipse.archives.core.model.ArchivesModel;
import org.jboss.ide.eclipse.archives.core.model.IArchivesLogger;
import org.jboss.ide.eclipse.archives.core.model.IExtensionManager;
import org.jboss.ide.eclipse.archives.core.model.IPreferenceManager;
import org.jboss.ide.eclipse.archives.core.model.IRuntimeVariables;

/**
 * A core API entry point for ant.
 * @author rob stryker (rob.stryker@redhat.com)
 *
 */
public class AntArchivesCore extends ArchivesCore {

	public AntArchivesCore () {
		super(STANDALONE);
		ArchivesModel.instance().addModelListener(new ModelChangeListener());
	}
	
	protected IExtensionManager createExtensionManager() {
		return new AntExtensionManager();
	}

	protected IPreferenceManager createPreferenceManager() {
		return new AntPreferenceManager();
	}

	protected IRuntimeVariables createVariables() {
		return new AntVariables();
	}

	public void preRegisterProject(IPath project) {
		
	}

	protected IArchivesLogger createLogger() {
		return null;
	}
}
