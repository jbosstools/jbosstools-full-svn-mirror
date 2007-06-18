package org.jboss.ide.eclipse.archives.core.model.other.internal;

import java.net.URL;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.jboss.ide.eclipse.archives.core.model.other.IRuntimeVariables;

public class StandaloneVariables implements IRuntimeVariables {

	public URL getBindingLog4j() {
		return getClass().getClassLoader().getResource("log4j.xml");
	}

	public URL getBindingSchema() {
		return getClass().getClassLoader().getResource("packages.xsd");
	}

	public IPath getProjectPath(String projectName) {
		projectName = projectName.replace(' ', '_');
		
		String projectPath = System.getProperty(projectName + ".dir");
		if (projectPath != null)
			return new Path(projectPath);
		
		return null;
	}

	public boolean isDebugging(String option) {
		// TODO Auto-generated method stub
		return false;
	}

}
