package org.jboss.tools.profiler.internal.ui.launch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.JavaLaunchDelegate;
import org.jboss.tools.profiler.internal.ui.JBossProfilerUiPlugin;

public class JBossProfilerLaunchDelegate extends JavaLaunchDelegate {

	IProcess profiledProcess = null;

	@Override
	public String getVMArguments(ILaunchConfiguration configuration)
			throws CoreException {
		String vmArguments = super.getVMArguments(configuration);
		
		String agentJar = LaunchUtils.getAgentJar(configuration);
		
		String propertiesFile = getPropertiesFiles(configuration);
		
		return "-javaagent:" + agentJar +  " -Djboss-profiler.properties=" + propertiesFile + " " + vmArguments;
		
	}

	private String getPropertiesFiles(ILaunchConfiguration configuration) throws CoreException {
		
		if(configuration.getAttribute(JBossProfilerLauncherConstants.GENERATE_PROPERTIES, true)) {
			try {
				File temp = File.createTempFile("jboss-profiler", "properties");
				Properties p = new Properties();
				p.setProperty("enable", Boolean.toString(configuration.getAttribute(JBossProfilerLauncherConstants.ENABLE_ON_STARTUP, true)));
				p.setProperty("save", Boolean.toString(configuration.getAttribute(JBossProfilerLauncherConstants.SAVE_ON_EXIT, true)));
				
				p.setProperty("host", "localhost");
				p.setProperty("port", "5400");
				
				p.setProperty("savelocation",
				LaunchUtils.getSaveLocation(configuration));
		
				p.setProperty("remote", "no");
				
				p.setProperty("cpu", "yes");
				p.setProperty("memory", "yes");
				p.setProperty("includes", "*");
				p.setProperty("excludes", "");
				//p.setProperty("visibility", "private");
				
				p.setProperty("startup", "yes");
				p.setProperty("repository", "no");
				
				p.setProperty("store", "memory");
				p.setProperty("location", ".");
				p.setProperty("ejb", "yes");
				p.setProperty("servlet", "yes");
				p.setProperty("jsf", "yes");
				p.setProperty("jmx", "yes");
				p.setProperty("rmi", "yes");
				p.setProperty("corba", "yes");
				p.setProperty("plugin.1", "org.jboss.profiler.plugins.Hibernate");
				p.setProperty("plugin.2", "org.jboss.profiler.plugins.Seam");
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(temp);
					p.store(fos, "");
				} finally {
					if(fos!=null) {
						fos.close();
					}
				}
				
				return temp.getAbsolutePath();
			} catch (IOException e) {
				throw new CoreException(new Status(IStatus.ERROR, JBossProfilerUiPlugin.getPluginId(), Messages.JBossProfilerLaunchDelegate_error_writing_temporary_properties));
			}			
		} else {
			return getSubstitutedString(configuration.getAttribute(JBossProfilerLauncherConstants.PROPERTIES_FILE, ""));			
		}		
	}

	
	private static String getSubstitutedString(String text) throws CoreException {
		if (text == null)
			return ""; //$NON-NLS-1$
		IStringVariableManager mgr = VariablesPlugin.getDefault().getStringVariableManager();
		return mgr.performStringSubstitution(text);
	}

	@Override
	public IVMRunner getVMRunner(ILaunchConfiguration configuration, String mode)
			throws CoreException {
		IVMInstall vm = verifyVMInstall(configuration);		
		IVMRunner runner = vm.getVMRunner(ILaunchManager.RUN_MODE);		
		return runner;
	}
	
	@Override
	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		
		
		JBossProfilerUiPlugin.getDefault().getLaunchListener().manage(launch);
		
		super.launch(configuration, mode, launch, monitor);
				
	}
		
}
