package org.jboss.ide.eclipse.archives.core.ant;

import org.apache.tools.ant.Task;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.jboss.ide.eclipse.archives.core.model.IArchivesVFS;
import org.jboss.ide.eclipse.archives.core.model.IVariableManager;
import org.jboss.ide.eclipse.archives.core.xpl.StringSubstitutionEngineClone;

public class AntVariables implements IArchivesVFS, IVariableManager {
	private Task currentTask;
	public void setCurrentTask(Task task) { currentTask = task; }
	public Task getCurrentTask() { return currentTask; }
	
	public String performStringSubstitution(String expression,
			boolean reportUndefinedVariables) throws CoreException {
		return new StringSubstitutionEngineClone().performStringSubstitution(expression, reportUndefinedVariables, this);
	}
	public String performStringSubstitution(String expression,
			String projectName, boolean reportUndefinedVariables)
			throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean containsVariable(String variable) {
		return false;
	}

	public String getVariableValue(String variable, String arg) {
		return null;
	}
	public IPath[] getWorkspaceChildren(IPath path) {
		// TODO Auto-generated method stub
		return null;
	}
	public IPath workspacePathToAbsolutePath(IPath path) {
		// TODO Auto-generated method stub
		return null;
	}
	public IPath[] workspacePathToAbsolutePath(IPath[] paths) {
		// TODO Auto-generated method stub
		return null;
	}
	public String getVariableValue(String variable) {
		// TODO Auto-generated method stub
		return null;
	}
	public String findProject(IPath absolutePath) {
		// TODO Auto-generated method stub
		return null;
	}

}
