package org.jboss.ide.eclipse.archives.core.ant;

import org.eclipse.core.runtime.IPath;
import org.jboss.ide.eclipse.archives.core.ArchivesCore;
import org.jboss.ide.eclipse.archives.core.build.ModelChangeListener;
import org.jboss.ide.eclipse.archives.core.model.ArchivesModel;
import org.jboss.ide.eclipse.archives.core.model.IArchivesLogger;
import org.jboss.ide.eclipse.archives.core.model.IExtensionManager;
import org.jboss.ide.eclipse.archives.core.model.IPreferenceManager;
import org.jboss.ide.eclipse.archives.core.model.IArchivesVFS;

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
		return null; // not necessary right now
	}

	protected IPreferenceManager createPreferenceManager() {
		return null; // not necessary right now
	}

	protected IArchivesVFS createVFS() {
		return new AntVFS();
	}

	public void preRegisterProject(IPath project) {
		// do nothing
	}

	protected IArchivesLogger createLogger() {
		return null;
	}
}
