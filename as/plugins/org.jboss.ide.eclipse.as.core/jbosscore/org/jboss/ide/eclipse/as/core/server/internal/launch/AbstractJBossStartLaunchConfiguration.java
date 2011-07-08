/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.ide.eclipse.as.core.server.internal.launch;

import java.io.File;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.launching.AbstractJavaLaunchConfigurationDelegate;
import org.eclipse.jdt.launching.ExecutionArguments;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.VMRunnerConfiguration;
import org.eclipse.jst.server.core.ServerProfilerDelegate;
import org.eclipse.wst.server.core.IServer;
import org.jboss.ide.eclipse.as.core.JBossServerCorePlugin;
import org.jboss.ide.eclipse.as.core.Messages;
import org.jboss.ide.eclipse.as.core.extensions.polling.WebPortPoller;
import org.jboss.ide.eclipse.as.core.server.internal.DelegatingServerBehavior;
import org.jboss.ide.eclipse.as.core.util.JBossServerBehaviorUtils;
import org.jboss.ide.eclipse.as.core.util.LaunchCommandPreferences;

/**
 * @author Rob Stryker
 */
public abstract class AbstractJBossStartLaunchConfiguration extends AbstractJavaLaunchConfigurationDelegate {

	@Override
	public boolean preLaunchCheck(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor)
			throws CoreException {
		DelegatingServerBehavior jbsBehavior = JBossServerBehaviorUtils.getServerBehavior(configuration);
		if (!jbsBehavior.canStart(mode).isOK())
			throw new CoreException(jbsBehavior.canStart(mode));
		if (LaunchCommandPreferences.isIgnoreLaunchCommand(jbsBehavior.getServer())) {
			jbsBehavior.setServerStarting();
			jbsBehavior.setServerStarted();
			return false;
		}
		boolean started = WebPortPoller.onePing(jbsBehavior.getServer());
		if (started) {
			jbsBehavior.setServerStarting();
			jbsBehavior.setServerStarted();
			return false;
		}

		return true;
	}

	public void preLaunch(ILaunchConfiguration configuration,
			String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
		// override me
	}

	public void postLaunch(ILaunchConfiguration configuration,
			String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
		// override me
	}

	@Override
	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		preLaunch(configuration, mode, launch, monitor);
		actualLaunch(configuration, mode, launch, monitor);
		postLaunch(configuration, mode, launch, monitor);
	}
	
	protected void actualLaunch(ILaunchConfiguration configuration,
			String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
		// And off we go!
		IVMInstall vm = verifyVMInstall(configuration);
		IVMRunner runner = vm.getVMRunner(mode);

		if (runner == null && ILaunchManager.PROFILE_MODE.equals(mode)) {
			runner = vm.getVMRunner(ILaunchManager.RUN_MODE);
		}
		if (runner == null) {
			throw new CoreException(new Status(IStatus.ERROR, JBossServerCorePlugin.PLUGIN_ID, 0,
					Messages.runModeNotSupported, null));
		}

		File workingDir = verifyWorkingDirectory(configuration);
		String workingDirName = null;
		if (workingDir != null)
			workingDirName = workingDir.getAbsolutePath();

		// Program & VM args
		String pgmArgs = getProgramArguments(configuration);
		String vmArgs = getVMArguments(configuration);
		ExecutionArguments execArgs = new ExecutionArguments(vmArgs, pgmArgs);

		// VM-specific attributes
		Map<?, ?> vmAttributesMap = getVMSpecificAttributesMap(configuration);

		// Classpath
		String[] classpath = getClasspath(configuration);

		// Environment
		String[] environment = getEnvironment(configuration);

		// Create VM config
		String mainType = getMainTypeName(configuration);
		VMRunnerConfiguration runConfig = new VMRunnerConfiguration(mainType, classpath);
		runConfig.setProgramArguments(execArgs.getProgramArgumentsArray());
		runConfig.setVMArguments(execArgs.getVMArgumentsArray());
		runConfig.setWorkingDirectory(workingDirName);
		runConfig.setVMSpecificAttributesMap(vmAttributesMap);
		runConfig.setEnvironment(environment);

		// Bootpath
		String[] bootpath = getBootpath(configuration);
		if (bootpath != null && bootpath.length > 0)
			runConfig.setBootClassPath(bootpath);

		setDefaultSourceLocator(launch, configuration);

		if (ILaunchManager.PROFILE_MODE.equals(mode)) {
			try {
				ServerProfilerDelegate.configureProfiling(launch, vm, runConfig, monitor);
			} catch (CoreException ce) {
				IServer server = org.eclipse.wst.server.core.ServerUtil.getServer(configuration);
				DelegatingServerBehavior jbsb = (DelegatingServerBehavior) server.getAdapter(DelegatingServerBehavior.class);
				jbsb.stop(true);
				// genericServer.stopImpl();
				throw ce;
			}
		}
		// Launch the configuration
		runner.run(runConfig, launch, monitor);
	}
}
