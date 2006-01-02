/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.launcher.core.configuration;

import java.text.MessageFormat;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.internal.launching.LaunchingMessages;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IVMConnector;
import org.eclipse.jdt.launching.JavaRuntime;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class RemoteServerLaunchConfigurationDelegate extends AbstractServerLaunchConfigurationDelegate
{
   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public boolean hasShutdown()
   {
      return false;
   }


   /**
    * Description of the Method
    *
    * @param configuration      Description of the Parameter
    * @param mode               Description of the Parameter
    * @param launch             Description of the Parameter
    * @param monitor            Description of the Parameter
    * @exception CoreException  Description of the Exception
    */
   public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException
   {
      if (monitor == null)
      {
         monitor = new NullProgressMonitor();
      }

      monitor.beginTask(MessageFormat.format(LaunchingMessages.JavaRemoteApplicationLaunchConfigurationDelegate_Attaching_to__0_____1, new String[]{configuration.getName()}), 3);//$NON-NLS-1$
      // check for cancellation
      if (monitor.isCanceled())
      {
         return;
      }
      monitor.subTask(LaunchingMessages.JavaRemoteApplicationLaunchConfigurationDelegate_Verifying_launch_attributes____1);//$NON-NLS-1$

      String connectorId = getVMConnectorId(configuration);
      IVMConnector connector = null;
      if (connectorId == null)
      {
         connector = JavaRuntime.getDefaultVMConnector();
      }
      else
      {
         connector = JavaRuntime.getVMConnector(connectorId);
      }
      if (connector == null)
      {
         abort(LaunchingMessages.JavaRemoteApplicationLaunchConfigurationDelegate_Connector_not_specified_2, null, IJavaLaunchConfigurationConstants.ERR_CONNECTOR_NOT_AVAILABLE);//$NON-NLS-1$
      }

      Map argMap = configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_CONNECT_MAP, (Map) null);

      // check for cancellation
      if (monitor.isCanceled())
      {
         return;
      }
      // connect to remote VM
      connector.connect(argMap, monitor, launch);
      // check for cancellation
      if (monitor.isCanceled())
      {
         return;
      }

//      launch.setSourceLocator(getAllOpenProjects());

      monitor.done();
   }


   /**
    * Description of the Method
    *
    * @param configuration  Description of the Parameter
    * @param monitor        Description of the Parameter
    */
   public void shutdown(ILaunchConfiguration configuration, IProgressMonitor monitor) { }
}
