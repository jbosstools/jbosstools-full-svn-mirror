package org.jboss.ide.eclipse.archives.core.ant;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import org.apache.tools.ant.Task;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.jboss.ide.eclipse.archives.core.model.IRuntimeVariables;
import org.jboss.ide.eclipse.archives.core.model.IVariableManager;
import org.jboss.ide.eclipse.archives.core.xpl.StringSubstitutionEngineClone;

public class AntVariables implements IRuntimeVariables, IVariableManager {
	private Task currentTask;
	public void setCurrentTask(Task task) { currentTask = task; }
	public Task getCurrentTask() { return currentTask; }
	
	public IPath getProjectPath(String projectName) {
//		HashMap<Object, Object> map = ResourceModel.getDefault().getTaskEnvironment(currentTask);
//		if( map.containsKey(projectName)) return new Path((String)map.get(projectName));
//		if( map.containsKey(IPath.SEPARATOR + projectName)) return new Path((String)map.get(IPath.SEPARATOR + projectName));
		projectName = projectName.replace(' ', '_');
		String projectPath = System.getProperty(projectName + ".dir");
		if (projectPath != null)
			return new Path(projectPath);
		return null;
	}

	public boolean isDebugging(String option) {
		return System.getProperty("archives.debug", "true")
				.equals("true");
	}

	public String getProjectName(IPath path) {
		Properties props = System.getProperties();
		Object key, val;
		for( Iterator i = props.keySet().iterator(); i.hasNext(); ) {
			key = i.next();
			if( key instanceof String && ((String)key).endsWith(".dir")) {
				val = props.get(key);
				if( path.toOSString().equals(new Path((String)val).toOSString()))
					return (String)key;
			}
		}
		return null;
	}

	public String performStringSubstitution(String expression,
			boolean reportUndefinedVariables) throws CoreException {
		return new StringSubstitutionEngineClone().performStringSubstitution(expression, reportUndefinedVariables, this);
	}

	public boolean containsVariable(String variable) {
		return false;
	}

	public String getVariableValue(String variable, String arg) {
		return null;
	}

}
