/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.launcher.core.configuration;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public interface IServerLaunchConfigurationDelegate extends ILaunchConfigurationDelegate
{
   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public boolean hasShutdown();


   /**
    * Description of the Method
    *
    * @param configuration      Description of the Parameter
    * @param monitor            Description of the Parameter
    * @exception CoreException  Description of the Exception
    */
   public void shutdown(ILaunchConfiguration configuration, IProgressMonitor monitor) throws CoreException;
}
