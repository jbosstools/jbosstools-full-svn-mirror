package org.jboss.ide.eclipse.archives.core.ant;

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

public class GenerateArchivesTask extends Task {

	private String projectPath;
	
	@Override
	public void init() throws BuildException {
//		 Force standalone mode
		ArchivesCore standalone = new StandaloneArchivesCore();
	}
	
	public void execute() throws BuildException {
		IPath projectPath = new Path(this.projectPath);
		IProgressMonitor monitor = new NullProgressMonitor();
		
		ArchivesModel.instance().registerProject(projectPath, monitor);
		ArchivesModelCore.buildProject(projectPath, monitor);
	}
}
