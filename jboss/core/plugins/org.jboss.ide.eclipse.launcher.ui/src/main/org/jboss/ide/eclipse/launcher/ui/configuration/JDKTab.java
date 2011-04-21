/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.launcher.ui.configuration;

import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaJRETab;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.swt.widgets.Composite;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.launcher.core.constants.IServerLaunchConfigurationConstants;
import org.jboss.ide.eclipse.launcher.core.util.ServerLaunchUtil;
import org.jboss.ide.eclipse.launcher.ui.LauncherUIMessages;

/**
 * @author    Hans Dockter
 * @author    Laurent Etiemble
 * @version   $Revision$
 * @created   18 mai 2003
 */
public class JDKTab extends JavaJRETab
{
   /**
    * @param parent  Description of the Parameter
    * @see           org.eclipse.jdt.debug.ui.launchConfigurations.JavaJRETab#createControl(org.eclipse.swt.widgets.Composite)
    */
   public void createControl(Composite parent)
   {
      super.createControl(parent);
   }


   /**
    * @return   The name value
    * @see      org.eclipse.jdt.debug.ui.launchConfigurations.JavaJRETab#getName()
    */
   public String getName()
   {
      return LauncherUIMessages.getString("JDKTab.name"); //$NON-NLS-1$
   }


   /**
    * @param launchConfig  Description of the Parameter
    * @return              The valid value
    * @see                 org.eclipse.debug.ui.ILaunchConfigurationTab#isValid(ILaunchConfiguration)
    */
   public boolean isValid(ILaunchConfiguration launchConfig)
   {
      if (super.isValid(launchConfig))
      {
         if (isMacOS())
         {
            return true;
         }
         IVMInstall vm = fJREBlock.getJRE();
         if (vm == null)
         {
            vm = JavaRuntime.getDefaultVMInstall();
         }
         List list;
         try
         {
            list = launchConfig.getAttribute(IServerLaunchConfigurationConstants.ATTR_RELATIVE_TO_JDK_CLASSPATH, Collections.EMPTY_LIST);
            for (Iterator iter = list.iterator(); iter.hasNext(); )
            {
               if (!(new File(vm.getInstallLocation().getAbsolutePath()
                     + File.separator + (String) iter.next())).exists())
               {
                  setErrorMessage(LauncherUIMessages.getString("JBossJRETTab.JRE_does_not_belong_to_a_JDK_1")); //$NON-NLS-1$
                  break;
               }
            }
         }
         catch (CoreException e)
         {
            AbstractPlugin.log(e);
         }
         return true;
      }
      return false;
   }


   /**
    * @param configuration  Description of the Parameter
    * @see                  org.eclipse.jdt.debug.ui.launchConfigurations.JavaJRETab#performApply(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
    */
   public void performApply(ILaunchConfigurationWorkingCopy configuration)
   {
      super.performApply(configuration);
      if (isMacOS())
      {
         return;
      }
      IVMInstall vm = fJREBlock.getJRE();
      if (vm == null)
      {
         vm = JavaRuntime.getDefaultVMInstall();
      }
      try
      {
         List relativeStartClasspath = configuration.getAttribute(IServerLaunchConfigurationConstants.ATTR_RELATIVE_TO_JDK_CLASSPATH, Collections.EMPTY_LIST);
         configuration.setAttribute(IServerLaunchConfigurationConstants.ATTR_JDK_CLASSPATH, ServerLaunchUtil.appendListElementToString(vm.getInstallLocation().getAbsolutePath(), File.separator, relativeStartClasspath));
      }
      catch (CoreException e)
      {
         AbstractPlugin.log(e);
      }
   }


   /**
    * Find and return the VMStandin with the specified name.
    *
    * @param vmName  Description of the Parameter
    * @return        The vM value
    */
   protected IVMInstall getVM(String vmName)
   {
      //      if (JavaRuntime.getDefaultVMInstall() != null
      //         && vmName.equals(JavaRuntime.getDefaultVMInstall()))
      //      {
      //         return JavaRuntime.getDefaultVMInstall();
      //      }
      //      Iterator iterator = fVMStandins.iterator();
      //      while (iterator.hasNext())
      //      {
      //         IVMInstall vmStandin = (IVMInstall) iterator.next();
      //         if (vmStandin.getName().equals(vmName))
      //         {
      //            return vmStandin;
      //         }
      //      }
      return null;
   }


   /**
    * Gets the macOS attribute of the JDKTab object
    *
    * @return   The macOS value
    */
   protected boolean isMacOS()
   {
      if (System.getProperty("os.name").startsWith("Mac OS"))  //$NON-NLS-1$//$NON-NLS-2$
      {
         return true;
      }
      return false;
   }


   /**
    * @param message  The new message value
    * @see            org.eclipse.debug.ui.AbstractLaunchConfigurationTab#setMessage(java.lang.String)
    */
   protected void setMessage(String message)
   {
      super.setMessage(LauncherUIMessages.getString("JDKTab.message")); //$NON-NLS-1$
   }
}
