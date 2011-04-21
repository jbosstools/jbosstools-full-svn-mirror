package org.jboss.tools.profiler.internal.ui.launch;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.jboss.tools.profiler.internal.ui.JBossProfilerUiPlugin;
import org.jboss.tools.profiler.internal.ui.Messages;

public class LaunchUtils {

	static public String getProfilerRuntime(ILaunchConfiguration configuration)
	throws CoreException {
		String agentJar = null;
		if(configuration.getAttribute(JBossProfilerLauncherConstants.USE_EMBEDDED_JAR, true)) {
			URL entry = JBossProfilerUiPlugin.getDefault().getBundle().getEntry("/embedded");
			File urlFile = null;			
			try {
				URL fileURL = FileLocator.toFileURL(entry);				
				urlFile = new File(fileURL.getPath());
			} catch (IOException e) {
				throw new CoreException(new Status(IStatus.ERROR, JBossProfilerUiPlugin.getPluginId(), Messages.LaunchUtils_probem_finding_embedded_runtime));
			}
			agentJar = urlFile.getAbsolutePath(); 
		} else {
			agentJar = getSubstitutedString(configuration.getAttribute(JBossProfilerLauncherConstants.PROFILER_JAR, (String)null));			
		}
		return agentJar;
	}

	static public String getProfilerClientJar(ILaunchConfiguration configuration) throws CoreException {
		try {
			return new File(getProfilerRuntime(configuration), "jboss-profiler-client.jar").getAbsolutePath();
		} catch (CoreException e) {
			throw new CoreException(new Status(IStatus.ERROR, JBossProfilerUiPlugin.getPluginId(), Messages.LaunchUtils_problem_finding_embedded_client));
		}		
	}
	
	static public String getAgentJar(ILaunchConfiguration configuration) throws CoreException {
		try {
			return new File(getProfilerRuntime(configuration), "jboss-profiler.jar").getAbsolutePath();
		} catch (CoreException e) {
			throw new CoreException(new Status(IStatus.ERROR, JBossProfilerUiPlugin.getPluginId(), Messages.LaunchUtils_problem_finding_embedded_agent_runtime));
		}
	}
	
	public static String getSaveLocation(ILaunchConfiguration configuration) throws CoreException {
		return getSubstitutedString(configuration.getAttribute(JBossProfilerLauncherConstants.SAVE_LOCATION, "."));				
	}

	private static String getSubstitutedString(String text) throws CoreException {
		if (text == null)
			return ""; //$NON-NLS-1$
		IStringVariableManager mgr = VariablesPlugin.getDefault().getStringVariableManager();
		return mgr.performStringSubstitution(text);
	}

}
