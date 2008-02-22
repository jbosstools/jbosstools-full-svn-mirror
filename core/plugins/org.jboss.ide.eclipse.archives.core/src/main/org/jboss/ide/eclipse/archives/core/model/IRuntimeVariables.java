package org.jboss.ide.eclipse.archives.core.model;

import java.net.URL;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

/**
 * 
 * @author rob.stryker <rob.stryker@redhat.com>
 *
 */
public interface IRuntimeVariables {

	public boolean isDebugging(String option);
	public IPath getProjectPath(String projectName);
	public String getProjectName(IPath path);
	
	// allow for variable replacement
	public String performStringSubstitution(String expression,	boolean reportUndefinedVariables) throws CoreException;

}
