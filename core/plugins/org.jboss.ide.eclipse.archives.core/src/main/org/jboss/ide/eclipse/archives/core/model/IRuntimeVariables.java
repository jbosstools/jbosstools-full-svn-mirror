package org.jboss.ide.eclipse.archives.core.model;

import java.net.URL;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

public interface IRuntimeVariables {
	/**
	 * Is the debugging option provided enabled?
	 * If the option is null, is debugging enabled at all?
	 * @param option
	 * @return
	 */
	public boolean isDebugging(String option);
	public IPath getProjectPath(String projectName);
	public String getProjectName(IPath path);
	public URL getBindingSchema();
	public URL getBindingLog4j();
	
	// allow for variable replacement
	public String performStringSubstitution(String expression,	boolean reportUndefinedVariables) throws CoreException;

}
