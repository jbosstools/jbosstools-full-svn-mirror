package org.jboss.ide.eclipse.archives.core.model.other.internal;

import java.net.URL;
import java.util.Iterator;
import java.util.Properties;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.jboss.ide.eclipse.archives.core.model.other.IRuntimeVariables;
import org.jboss.ide.eclipse.archives.core.xpl.StringSubstitutionEngineClone;

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
		return new StringSubstitutionEngineClone().performStringSubstitution(expression, reportUndefinedVariables);
	}

}
