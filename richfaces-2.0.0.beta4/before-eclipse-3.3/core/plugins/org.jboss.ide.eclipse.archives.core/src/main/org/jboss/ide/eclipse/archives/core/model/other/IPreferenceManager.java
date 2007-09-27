package org.jboss.ide.eclipse.archives.core.model.other;

import org.eclipse.core.runtime.IPath;

public interface IPreferenceManager {
	public boolean isBuilderEnabled(IPath path);
	public void setBuilderEnabled(IPath path, boolean val);
	public boolean areProjectSpecificPrefsEnabled(IPath path);
	public void setProjectSpecificPrefsEnabled(IPath path, boolean val);
}
