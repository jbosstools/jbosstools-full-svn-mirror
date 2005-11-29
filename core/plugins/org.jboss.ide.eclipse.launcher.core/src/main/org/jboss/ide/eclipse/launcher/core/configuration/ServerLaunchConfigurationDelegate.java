/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.launcher.core.configuration;

import java.io.File;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.Launch;
import org.eclipse.debug.core.model.ISourceLocator;
import org.eclipse.jdt.internal.launching.LaunchingMessages;
import org.eclipse.jdt.launching.ExecutionArguments;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.VMRunnerConfiguration;
import org.jboss.ide.eclipse.launcher.core.LauncherCoreMessages;
import org.jboss.ide.eclipse.launcher.core.ServerLaunchManager;
import org.jboss.ide.eclipse.launcher.core.constants.IServerLaunchConfigurationConstants;
import org.jboss.ide.eclipse.launcher.core.util.ServerLaunchUtil;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public abstract class ServerLaunchConfigurationDelegate extends AbstractServerLaunchConfigurationDelegate
{
   /** Constructor for ServerLaunchConfigurationDelegate. */
   public ServerLaunchConfigurationDelegate()
   {
      super();
   }


   /**
    * This is copied, pasted and modified from
    * JavaLocalApplicationLaunchConfigurationDelegate.
    *
    * @param configuration      Description of the Parameter
    * @param mode               Description of the Parameter
    * @param launch             Description of the Parameter
    * @param monitor            Description of the Parameter
    * @exception CoreException  Description of the Exception
    * @see                      org.eclipse.debug.core.model.ILaunchConfigurationDelegate#launch(ILaunchConfiguration,
    *      String, ILaunch, IProgressMonitor)
    */
   public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException
   {

      if (ServerLaunchManager.getInstance().getRunningStartLaunch(configuration) != null)
      {
         abort(LauncherCoreMessages.getString("Server_is_already_running_1"), null, 0);//$NON-NLS-1$
      }

      boolean configurationError = configuration.getAttribute(IServerLaunchConfigurationConstants.ATTR_CONFIGURATION_ERROR, true);

      String mainTypeName = verifyMainTypeName(configuration);

      IVMInstall vm = verifyVMInstall(configuration);

      File workingDir = verifyWorkingDirectory(configuration);
      String workingDirName = null;
      if (workingDir != null)
      {
         workingDirName = workingDir.getAbsolutePath();
      }

      // Program & VM args
      String pgmArgs = getProgramArguments(configuration);
      String vmArgs = getVMArguments(configuration);
      // ExecutionArguments execArgs = new ExecutionArguments(vmArgs, pgmArgs);

      // VM-specific attributes
      Map vmAttributesMap = getVMSpecificAttributesMap(configuration);

      // Classpath
      String[] userClasspath = getClasspath(configuration);
      List classpathList = configuration.getAttribute(IServerLaunchConfigurationConstants.ATTR_CLASSPATH, Collections.EMPTY_LIST);

      for (int i = 0; i < userClasspath.length; i++)
      {
         classpathList.add(0, userClasspath[i]);
      }
      String[] classpath = (String[]) classpathList.toArray(new String[classpathList.size()]);

      // stop in main
      prepareStopInMain(configuration);

      launchInternal(mode, launch, monitor, mainTypeName, vm, pgmArgs, vmArgs, vmAttributesMap, classpath, null
      /*
       * getAllOpenProjects()
       */
            , workingDirName, getBootpath(configuration), configurationError);
   }


   /**
    * Description of the Method
    *
    * @param mode                Description of the Parameter
    * @param launch              Description of the Parameter
    * @param monitor             Description of the Parameter
    * @param mainTypeName        Description of the Parameter
    * @param vm                  Description of the Parameter
    * @param pgmArgs             Description of the Parameter
    * @param vmArgs              Description of the Parameter
    * @param vmAttributesMap     Description of the Parameter
    * @param classpath           Description of the Parameter
    * @param sourceLocator       Description of the Parameter
    * @param workingDirectory    Description of the Parameter
    * @param bootpath            Description of the Parameter
    * @param configurationError  Description of the Parameter
    * @exception CoreException   Description of the Exception
    */
   public void launchInternal(String mode, ILaunch launch, IProgressMonitor monitor, String mainTypeName, IVMInstall vm, String pgmArgs, String vmArgs, Map vmAttributesMap, String[] classpath, ISourceLocator sourceLocator, String workingDirectory, String[] bootpath, boolean configurationError) throws CoreException
   {

      if (configurationError)
      {
         abort(LauncherCoreMessages.getString("ServerLaunchConfigurationDelegateConfiguration_Error_2"), null, 0);//$NON-NLS-1$
      }

      if (monitor == null)
      {
         monitor = new NullProgressMonitor();
      }

      monitor.beginTask("Launching JBoss", 3);//$NON-NLS-1$
      // check for cancellation
      if (monitor.isCanceled())
      {
         return;
      }

      monitor.subTask(LaunchingMessages.JavaLocalApplicationLaunchConfigurationDelegate_Verifying_launch_attributes____1);//$NON-NLS-1$

      IVMRunner runner = vm.getVMRunner(mode);
      if (runner == null)
      {
         if (mode == ILaunchManager.DEBUG_MODE)
         {
            abort(MessageFormat.format(LaunchingMessages.JavaLocalApplicationLaunchConfigurationDelegate_0, new String[]//$NON-NLS-1$
            {vm.getName()}), null, IJavaLaunchConfigurationConstants.ERR_VM_RUNNER_DOES_NOT_EXIST);
         }
         else
         {
            abort(MessageFormat.format(LaunchingMessages.JavaLocalApplicationLaunchConfigurationDelegate_1, new String[]//$NON-NLS-1$
            {vm.getName()}), null, IJavaLaunchConfigurationConstants.ERR_VM_RUNNER_DOES_NOT_EXIST);
         }
      }

      ExecutionArguments execArgs = new ExecutionArguments(vmArgs, pgmArgs);

      //		Create VM config
      VMRunnerConfiguration runConfig = new VMRunnerConfiguration(mainTypeName, classpath);
      runConfig.setProgramArguments(execArgs.getProgramArgumentsArray());
      runConfig.setVMArguments(execArgs.getVMArgumentsArray());
      runConfig.setWorkingDirectory(workingDirectory);
      runConfig.setVMSpecificAttributesMap(vmAttributesMap);
      runConfig.setBootClassPath(bootpath);

      //		check for cancellation
      if (monitor.isCanceled())
      {
         return;
      }

      // done the verification phase
      monitor.worked(1);

      // Launch the configuration - 1 unit of work
      runner.run(runConfig, launch, monitor);

      // check for cancellation
      if (monitor.isCanceled())
      {
         return;
      }

      monitor.subTask(LaunchingMessages.JavaLocalApplicationLaunchConfigurationDelegate_Creating_source_locator____2);//$NON-NLS-1$
      // set the default source locator if required
//      launch.setSourceLocator(sourceLocator);
      monitor.worked(1);

      monitor.done();
   }


   /**
    * Description of the Method
    *
    * @param configuration      Description of the Parameter
    * @param monitor            Description of the Parameter
    * @exception CoreException  Description of the Exception
    */
   public void shutdown(ILaunchConfiguration configuration, IProgressMonitor monitor) throws CoreException
   {

      if (!hasShutdown())
      {
         abort(LauncherCoreMessages.getString("ServerLaunchConfigurationDelegatehasShutdown_must_not_be_null_1"), null, 0);//$NON-NLS-1$
      }

      String mainTypeName = configuration.getAttribute(IServerLaunchConfigurationConstants.ATTR_SHUTDOWN_TYPE, IServerLaunchConfigurationConstants.EMPTY_STRING);
      IVMInstall vm = verifyVMInstall(configuration);

      // Program & VM args
      String pgmArgs = configuration.getAttribute(IServerLaunchConfigurationConstants.ATTR_SHUTDOWN_PROGRAM_ARGS, IServerLaunchConfigurationConstants.EMPTY_STRING);
      String vmArgs = configuration.getAttribute(IServerLaunchConfigurationConstants.ATTR_SHUTDOWN_VM_ARGS, IServerLaunchConfigurationConstants.EMPTY_STRING);

      // VM-specific attributes
      Map vmAttributesMap = null;

      // Classpath
      String[] classpath = ServerLaunchUtil.getArrayFromList(configuration, IServerLaunchConfigurationConstants.ATTR_SHUTDOWN_CLASSPATH);

      ILaunch launch = new Launch(configuration, ILaunchManager.RUN_MODE, null);
      launch.setAttribute(IServerLaunchConfigurationConstants.ATTR_LAUNCH_SHUTDOWN, IServerLaunchConfigurationConstants.EMPTY_STRING);

      boolean configurationError = configuration.getAttribute(IServerLaunchConfigurationConstants.ATTR_CONFIGURATION_ERROR, true);

      prepareStopInMain(configuration);

      launchInternal(ILaunchManager.RUN_MODE, launch, monitor, mainTypeName, vm, pgmArgs, vmArgs, vmAttributesMap, classpath, null, null, getBootpath(configuration), configurationError);
   }
}
