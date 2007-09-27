package org.jboss.ide.eclipse.packaging.core.builder;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.jboss.ide.eclipse.packaging.core.PackagingCorePlugin;

public class PackagingBuilder extends IncrementalProjectBuilder {

	public static final String BUILDER_ID = "org.jboss.ide.eclipse.packaging.core.PackagingBuilder";
	
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
			throws CoreException {
		
		generatePackagingScript();
		
		return null;
	}
	
	private void generatePackagingScript ()
	{
		IProject project = getProject();
		try {
			PackagingCorePlugin.getDefault().createBuildFile(project);
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}
}
