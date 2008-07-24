package org.jboss.ide.eclipse.archives.core.ant;

import org.eclipse.core.runtime.IPath;
import org.jboss.ide.eclipse.archives.core.model.IPreferenceManager;

/**
 * This is to manage preferences relating to projects.
 * This is a candidate to be moved to eclipse-only API, or to be
 * fixed or changed to be more versatile. 
 * @author rob stryker
 *
 */
public class AntPreferenceManager implements IPreferenceManager {

	public boolean areProjectSpecificPrefsEnabled(IPath path) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isBuilderEnabled(IPath path) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setBuilderEnabled(IPath path, boolean val) {
		// TODO Auto-generated method stub

	}

	public void setProjectSpecificPrefsEnabled(IPath path, boolean val) {
		// TODO Auto-generated method stub

	}

}
