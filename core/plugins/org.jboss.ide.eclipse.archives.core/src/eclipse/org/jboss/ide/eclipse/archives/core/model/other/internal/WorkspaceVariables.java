package org.jboss.ide.eclipse.archives.core.model.other.internal;

import java.net.URL;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.jboss.ide.eclipse.archives.core.ArchivesCorePlugin;
import org.jboss.ide.eclipse.archives.core.model.other.IRuntimeVariables;

public class WorkspaceVariables implements IRuntimeVariables {

	public URL getBindingLog4j() {
		return getClass().getClassLoader().getResource("log4j.xml");
	}

	public URL getBindingSchema() {
		return getClass().getClassLoader().getResource("packages.xsd");
	}

	public IPath getProjectPath (String projectName) {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		
		if (project.exists() && project.isAccessible())
		{
			IPath location = project.getRawLocation();
			if (location == null) return project.getLocation();
			
			return location;
		}
		
		return null;
	}

	public boolean isDebugging(String option) {
		return ArchivesCorePlugin.getDefault().isDebugging()
		&& "true".equalsIgnoreCase(Platform.getDebugOption(option));
	}
}
