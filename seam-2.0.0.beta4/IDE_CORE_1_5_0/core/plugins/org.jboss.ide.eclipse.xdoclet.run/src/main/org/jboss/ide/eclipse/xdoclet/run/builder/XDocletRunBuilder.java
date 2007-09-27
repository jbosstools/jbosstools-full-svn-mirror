package org.jboss.ide.eclipse.xdoclet.run.builder;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.jboss.ide.eclipse.xdoclet.run.XDocletRunPlugin;

/**
 * This builder replaces the need to re-export the .xdoclet file to an actual ant script that runs XDoclet.
 */
public class XDocletRunBuilder extends IncrementalProjectBuilder {

	public static final String BUILDER_ID = "org.jboss.ide.eclipse.xdoclet.run.XDocletRunBuilder";
	
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
			throws CoreException {
		
		generateXDocletRunScript();
		
		return null;
	}
	
	private void generateXDocletRunScript ()
	{
		IProject project = getProject();
		try {
			XDocletRunPlugin.getDefault().createBuildFile(JavaCore.create(project));
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}

}
