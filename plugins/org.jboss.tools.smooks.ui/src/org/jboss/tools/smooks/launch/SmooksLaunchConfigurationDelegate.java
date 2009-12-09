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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.junit.launcher.JUnitLaunchConfigurationDelegate;
import org.eclipse.jdt.launching.ExecutionArguments;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.VMRunnerConfiguration;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.smooks.configuration.ProcessNodeType;
import org.jboss.tools.smooks.configuration.RuntimeDependency;
import org.jboss.tools.smooks.configuration.RuntimeMetadata;
import org.jboss.tools.smooks.configuration.editors.uitls.ProjectClassLoader;
import org.jboss.tools.smooks.core.SmooksInputType;

/**
 * Smooks Launch Configuration Delegate.
 * 
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public class SmooksLaunchConfigurationDelegate extends JUnitLaunchConfigurationDelegate {
	
	private static final String PLUGIN_ID = "org.jboss.tools.smooks.ui.smooksLauncher"; //$NON-NLS-1$

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.ILaunchConfigurationDelegate#launch(org.eclipse.debug.core.ILaunchConfiguration, java.lang.String, org.eclipse.debug.core.ILaunch, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void launch(ILaunchConfiguration launchConfig, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
		IJavaProject javaProject = getJavaProject(launchConfig);
		IProject project = javaProject.getProject();
		final String smooksConfigName = launchConfig.getAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, ""); //$NON-NLS-1$
		final RuntimeMetadata launchMetadata = new RuntimeMetadata();

		launchMetadata.setSmooksConfig(project.findMember(smooksConfigName));
		
		if(!launchMetadata.isValidSmooksConfig()) {
			displayError(smooksConfigName, launchMetadata.getErrorMessage());
			return;
		} else {
			List<RuntimeDependency> dependencies = launchMetadata.getDependencies();
			ProjectClassLoader projectClassLoader = new ProjectClassLoader(javaProject);
			
			for(RuntimeDependency dependency : dependencies) {
				if(!dependency.isOnProjectClasspath(projectClassLoader)) {
					displayError(smooksConfigName, Messages.SmooksLaunchConfigurationDelegate_Error_missing_artifact + dependency.getGroupId() + ":" + dependency.getArtifactId() + Messages.SmooksLaunchConfigurationDelegate_Error_missing_artifact2); //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-1$
					return;
				}
			}
		}

		IVMRunner runner= getVMRunner(launchConfig, mode);		
		VMRunnerConfiguration runConfig = buildRunnerConfig(launchConfig);
		
		// check for cancellation
		if (monitor.isCanceled()) {
			return;
		}		

		String inputType = launchMetadata.getInputType();
		String inputPath = launchMetadata.getInputFile().getAbsolutePath();
		String nodeTypes = launchMetadata.getNodeTypesString();

		runConfig.setProgramArguments(new String[] {launchMetadata.getConfigFile().getAbsolutePath(), inputType, inputPath, nodeTypes});
		
		runner.run(runConfig, launch, monitor);
	}

	private void displayError(final String smooksConfigName, final String errorMessage) {
		final Display display = PlatformUI.getWorkbench().getDisplay();
		display.syncExec(new Runnable() {
		    public void run(){
				Shell shell = display.getActiveShell();
				ErrorDialog.openError(shell, Messages.SmooksLaunchConfigurationDelegate_Error_Title, Messages.SmooksLaunchConfigurationDelegate_Error_launching + smooksConfigName + "'.", new Status(IStatus.ERROR, PLUGIN_ID, IStatus.ERROR, errorMessage, new Exception())); //$NON-NLS-3$ //$NON-NLS-1$ //$NON-NLS-1$
		    }
		});
	}

	private VMRunnerConfiguration buildRunnerConfig(ILaunchConfiguration launchConfig) throws CoreException {
		List<String> classpath = new ArrayList<String>(Arrays.asList(getClasspath(launchConfig)));

		File wsRootDir = ResourcesPlugin.getWorkspace().getRoot().getRawLocation().toFile();
		File wsTempClasses = new File(wsRootDir, "temp/classes"); //$NON-NLS-1$
		
		// We need to add the SmooksLauncher to the launch classpath because it will not be part of the projects
		// classpath.  Bit of a hack... there's probably a nicer way of doing this!!!
		addToCP(wsTempClasses, SmooksLauncher.class);
		addToCP(wsTempClasses, SmooksInputType.class);
		addToCP(wsTempClasses, ProcessNodeType.class);
		classpath.add(wsTempClasses.getAbsolutePath());
		
		VMRunnerConfiguration runConfig= new VMRunnerConfiguration(SmooksLauncher.class.getName(), classpath.toArray(new String[classpath.size()]));
		String[] envp= getEnvironment(launchConfig);
		ArrayList<String> vmArguments= new ArrayList<String>();
		String vmArgs= getVMArguments(launchConfig);
		ExecutionArguments execArgs= new ExecutionArguments(vmArgs, ""); //$NON-NLS-1$
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

	private void addToCP(File wsTempClasses, Class<?> theClass) throws CoreException {
		String className = theClass.getName().replace(".", "/") + ".class"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		URL classURI = getClass().getResource("/" + className); //$NON-NLS-1$
		
		if(classURI != null) {
			try {
				InputStream classStream = classURI.openStream();
				
				if(classStream != null) {
					try {
						File classOutFile = new File(wsTempClasses, className);
						File classPackage = classOutFile.getParentFile();
						
						classPackage.mkdirs();
						if(classPackage.exists()) {
							FileOutputStream classOutStream = new FileOutputStream(classOutFile);
							
							try {
								byte[] readBuf = new byte[100];
								int readCount = 0;
								
								while(readCount != -1) {
									readCount = classStream.read(readBuf);
									if(readCount != -1) {
										classOutStream.write(readBuf, 0, readCount);
									}
								}
							} finally {
								try {
									classOutStream.flush();
								} finally {									
									classOutStream.close();
								}
							}
						}
					} finally {
						classStream.close();
					}
				}
			} catch (IOException e) {
				new CoreException(new Status(IStatus.ERROR, PLUGIN_ID, IStatus.ERROR, Messages.SmooksLaunchConfigurationDelegate_Error_Copying_Smooks_Launcher, e));
			}
		}
	}
}
