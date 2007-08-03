package org.jboss.ide.eclipse.archives.core.ant;

import java.util.Iterator;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.jboss.ide.eclipse.archives.core.ArchivesCore;
import org.jboss.ide.eclipse.archives.core.StandaloneArchivesCore;
import org.jboss.ide.eclipse.archives.core.model.ArchivesModel;
import org.jboss.ide.eclipse.archives.core.model.ArchivesModelCore;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XMLBinding;

public class GenerateArchivesTask extends Task {

	private String projectPath;
	
	public void init() throws BuildException {
		// Force standalone mode
		ArchivesCore standalone = new StandaloneArchivesCore();
	}
	
	public void execute() throws BuildException {
		IPath projectPath = new Path(this.projectPath);
		IProgressMonitor monitor = new NullProgressMonitor();
		
		for (Iterator iter = getProject().getProperties().keySet().iterator(); iter.hasNext(); )
		{
			String property = (String) iter.next();
			if (property.endsWith(".dir")) {
				System.setProperty(property, getProject().getProperty(property));
			}
		}
		
		// needed so the correct XML binding / TrueZIP jars are loaded
		ClassLoader original = Thread.currentThread().getContextClassLoader();
		ClassLoader myCL = getClass().getClassLoader();
		Thread.currentThread().setContextClassLoader(myCL);
		
		ArchivesModel.instance().registerProject(projectPath, monitor);
		ArchivesModelCore.buildProject(projectPath, monitor);
		
		Thread.currentThread().setContextClassLoader(original);
	}

	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}
}
