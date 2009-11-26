/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, JBoss Inc., and others contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 * (C) 2005-2006, JBoss Inc.
 */
package org.jboss.tools.smooks.launch;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.junit.launcher.JUnitLaunchConfigurationDelegate;
import org.eclipse.jdt.launching.ExecutionArguments;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.VMRunnerConfiguration;
import org.eclipse.osgi.baseadaptor.loader.BaseClassLoader;
import org.eclipse.osgi.baseadaptor.loader.ClasspathEntry;
import org.eclipse.osgi.baseadaptor.loader.ClasspathManager;
import org.osgi.framework.BundleException;

/**
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public class SmooksLaunchConfigurationDelegate extends JUnitLaunchConfigurationDelegate {
	
	public static final String SMOOKS_INPUT = "SmooksInput";
	public static final String SMOOKS_INPUT_TYPE = "SmooksInputType";
	public static final String SMOOKS_PROCESS_TYPES = "SmooksProcessTypes";

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.ILaunchConfigurationDelegate#launch(org.eclipse.debug.core.ILaunchConfiguration, java.lang.String, org.eclipse.debug.core.ILaunch, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void launch(ILaunchConfiguration launchConfig, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
		IVMRunner runner= getVMRunner(launchConfig, mode);		
		VMRunnerConfiguration runConfig = buildRunnerConfig(launchConfig);
		
		// check for cancellation
		if (monitor.isCanceled()) {
			return;
		}		

		IResource smooksConfig = SmooksRunTab.getSmooksConfig(launchConfig);
		if(smooksConfig != null) {
			String inputType = launchConfig.getAttribute(SMOOKS_INPUT_TYPE, "");
			String inputPath = launchConfig.getAttribute(SMOOKS_INPUT, "");
			String nodeTypes = launchConfig.getAttribute(SMOOKS_PROCESS_TYPES, "");
	
			runConfig.setProgramArguments(new String[] {smooksConfig.getRawLocation().toOSString(), inputType, inputPath, nodeTypes});
			
			runner.run(runConfig, launch, monitor);
		}
	}

	private VMRunnerConfiguration buildRunnerConfig(ILaunchConfiguration launchConfig) throws CoreException {
		List<String> classpath = new ArrayList<String>(Arrays.asList(getClasspath(launchConfig)));

		// ====================================================================================================================
		// TODO Total Hack... Fixme: We're using classes here that we shouldn't, as well as adding bundle paths that we 
		// probably shouldn't, but how do I get the SmooksLauncher class on the launcher classpath?
		// I added the "bin" folder to the bundle classpath as a workaround (with the following code) for getting 
		// the SmooksLauncher class onto the classpath.  Need to fix this properly!!!!
		//
		ClassLoader classloader = getClass().getClassLoader();
		if(classloader instanceof BaseClassLoader) {
			ClasspathManager cpManager = ((BaseClassLoader)classloader).getClasspathManager();
			ClasspathEntry[] entries = cpManager.getHostClasspathEntries();
			
			for(ClasspathEntry entry : entries) {
				File baseFile = entry.getBundleFile().getBaseFile();
				try {
					String[] bundleEntries = entry.getBaseData().getClassPath();
					for(String bundleEntry : bundleEntries) {
						String path = baseFile.getAbsolutePath() + "/" + bundleEntry;
						if(!classpath.contains(path)) {
							classpath.add(path);
						}
					}
				} catch (BundleException e) {
					e.printStackTrace();
				}				
			}
		}
		//
		// ====================================================================================================================
		
		VMRunnerConfiguration runConfig= new VMRunnerConfiguration(SmooksLauncher.class.getName(), classpath.toArray(new String[classpath.size()]));
		String[] envp= getEnvironment(launchConfig);
		ArrayList<String> vmArguments= new ArrayList<String>();
		String vmArgs= getVMArguments(launchConfig);
		ExecutionArguments execArgs= new ExecutionArguments(vmArgs, "");
		File workingDir = verifyWorkingDirectory(launchConfig);

		vmArguments.addAll(Arrays.asList(execArgs.getVMArgumentsArray()));
		runConfig.setVMArguments((String[]) vmArguments.toArray(new String[vmArguments.size()]));
		runConfig.setEnvironment(envp);
		if(workingDir != null) {
			runConfig.setWorkingDirectory(workingDir.getAbsolutePath());
		}
		runConfig.setVMSpecificAttributesMap(getVMSpecificAttributesMap(launchConfig));
		runConfig.setBootClassPath(getBootpath(launchConfig));
		
		return runConfig;
	}
}
