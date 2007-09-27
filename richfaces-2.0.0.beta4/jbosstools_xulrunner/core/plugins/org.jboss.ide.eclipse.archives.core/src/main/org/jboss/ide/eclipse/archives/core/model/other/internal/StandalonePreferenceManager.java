package org.jboss.ide.eclipse.archives.core.model.other.internal;

import org.eclipse.core.runtime.IPath;
import org.jboss.ide.eclipse.archives.core.model.other.IPreferenceManager;

public class StandalonePreferenceManager implements IPreferenceManager {

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
