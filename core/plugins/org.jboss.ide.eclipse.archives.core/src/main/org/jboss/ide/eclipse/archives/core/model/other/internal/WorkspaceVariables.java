package org.jboss.ide.eclipse.archives.core.model.other.internal;

import java.net.URL;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.jboss.ide.eclipse.archives.core.ArchivesCorePlugin;
import org.jboss.ide.eclipse.archives.core.model.other.IRuntimeVariables;

public class WorkspaceVariables implements IRuntimeVariables {

	public URL getBindingLog4j() {
		return ArchivesCorePlugin.getDefault().getBundle().getEntry("log4j.xml");
	}

	public URL getBindingSchema() {
		return ArchivesCorePlugin.getDefault().getBundle().getEntry("xml/packages.xsd");
	}

	public IPath getWorkspacePath() {
		return ResourcesPlugin.getWorkspace().getRoot().getLocation();
	}

	public boolean isDebugging(String option) {
		return ArchivesCorePlugin.getDefault().isDebugging()
		&& "true".equalsIgnoreCase(Platform.getDebugOption(option));
	}
}
