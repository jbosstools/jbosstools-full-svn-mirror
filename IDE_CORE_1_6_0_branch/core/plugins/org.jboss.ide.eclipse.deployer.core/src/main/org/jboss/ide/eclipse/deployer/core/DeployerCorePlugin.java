/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.ide.eclipse.deployer.core;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.deployer.core.target.FileSystemCopy;
import org.jboss.ide.eclipse.deployer.core.target.ITarget;
import org.jboss.ide.eclipse.launcher.core.ServerLaunchManager;
import org.jboss.ide.eclipse.launcher.core.configuration.jboss.IJBossLaunchConfigurationDelegate;
import org.jboss.ide.eclipse.launcher.core.constants.IJBossConstants;
import org.jboss.ide.eclipse.launcher.core.util.JBossType;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 *
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class DeployerCorePlugin extends AbstractPlugin
{
   /** Description of the Field */
   private Collection debugTargets = new ArrayList();

   /** Description of the Field */
   private Collection targets = new ArrayList();

   /** Description of the Field */
   private static String TARGET = "target";//$NON-NLS-1$

   /** Description of the Field */
   private static String TARGET_COUNT = "target.count";//$NON-NLS-1$

   /** Description of the Field */
   private static DeployerCorePlugin plugin;

   /** The constructor. */
   public DeployerCorePlugin()
   {
      plugin = this;
   }

   /**
    * Adds a feature to the Target attribute of the DeployerCorePlugin object
    *
    * @param target  The feature to be added to the Target attribute
    */
   public void addTarget(ITarget target)
   {
      this.targets.add(target);
   }

   /**
    * Gets the debugTargets attribute of the DeployerCorePlugin object
    *
    * @return   The debugTargets value
    */
   public Collection getDebugTargets()
   {
      return this.debugTargets;
   }

   /**
    * Gets the targets attribute of the DeployerCorePlugin object
    *
    * @return   The targets value
    */
   public Collection getTargets()
   {
      return this.targets;
   }

   /** Description of the Method */
   public void refreshDebugTargets()
   {
      this.debugTargets.clear();

      // Load debug defined target from launcher plugin
      try
      {
         ILaunchConfiguration[] configurations = ServerLaunchManager.getInstance().getServerConfigurations();
         for (int i = 0; i < configurations.length; i++)
         {
            ILaunchConfiguration configuration = configurations[i];
            if (ServerLaunchManager.getInstance().isServerConfiguration(configuration)
                  && configuration.getType().getDelegate(ILaunchManager.DEBUG_MODE) instanceof IJBossLaunchConfigurationDelegate)
            {
               IJBossLaunchConfigurationDelegate delegate = (IJBossLaunchConfigurationDelegate) configuration.getType()
                     .getDelegate(ILaunchManager.DEBUG_MODE);
               String homedir = configuration.getAttribute(IJBossConstants.ATTR_JBOSS_HOME_DIR, "");//$NON-NLS-1$
               String serverConfig = configuration.getAttribute(IJBossConstants.ATTR_SERVER_CONFIGURATION, "default");//$NON-NLS-1$
               try
               {
                  if (!"".equals(homedir)//$NON-NLS-1$
                  )
                  {
                     FileSystemCopy target;
                     if (delegate.getType().equals(JBossType.JBoss_2_x))
                     {
                        target = new FileSystemCopy();
                        target.setName(configuration.getName());
                        File dir = new File(homedir);
                        File deploy = new File(dir, "deploy");//$NON-NLS-1$
                        target.setURL(deploy.toURL());
                        this.debugTargets.add(target);
                     }
                     else if (delegate.getType().equals(JBossType.JBoss_3_0_X)
                           || delegate.getType().equals(JBossType.JBoss_3_2_X)
                           || delegate.getType().equals(JBossType.JBoss_4_0_X))
                     {
                        File dir = new File(homedir);
                        File server = new File(dir, "server");//$NON-NLS-1$
                        File config = new File(server, serverConfig);
                        File deploy = new File(config, "deploy");//$NON-NLS-1$
                        if (deploy.exists() && deploy.isDirectory())
                        {
                           target = new FileSystemCopy();
                           target.setName(configuration.getName());
                           target.setURL(deploy.toURL());
                           this.debugTargets.add(target);
                        }
                     }
                  }
               }
               catch (MalformedURLException mfue)
               {
                  AbstractPlugin.logError("Error while transforming JBoss launch configurations into target", mfue);//$NON-NLS-1$
               }
            }
         }
      }
      catch (CoreException ce)
      {
         AbstractPlugin.logError("Error while fetching JBoss launch configurations", ce);//$NON-NLS-1$
      }
   }

   /** Description of the Method */
   public void refreshTargets()
   {
      IPreferenceStore store = this.getPreferenceStore();

      this.targets.clear();

      // Load user defined target from preference store
      int count = store.getInt(TARGET_COUNT);
      for (int i = 0; i < count; i++)
      {
         String targetName = store.getString(TARGET + "." + i + ".name");//$NON-NLS-1$ //$NON-NLS-2$
         String targetClass = store.getString(TARGET + "." + i + ".class");//$NON-NLS-1$ //$NON-NLS-2$
         String targetParameters = store.getString(TARGET + "." + i //$NON-NLS-1$
               + ".parameters");//$NON-NLS-1$

         try
         {
            Class clazz = Class.forName(targetClass);
            ITarget target = (ITarget) clazz.newInstance();
            target.setName(targetName);
            target.setParameters(targetParameters);
            this.targets.add(target);
         }
         catch (Exception e)
         {
            AbstractPlugin.logError("Can't create the target '" + targetName //$NON-NLS-1$
                  + "'", e);//$NON-NLS-1$
         }
      }
   }

   /**
    * Description of the Method
    *
    * @param target  Description of the Parameter
    */
   public void removeTarget(ITarget target)
   {
      this.targets.remove(target);
   }

   /** Description of the Method */
   public void saveTargets()
   {
      IPreferenceStore store = this.getPreferenceStore();
      int i = 0;
      for (Iterator iterator = this.targets.iterator(); iterator.hasNext();)
      {
         ITarget target = (ITarget) iterator.next();

         store.setValue(TARGET + "." + i + ".name", target.getName());//$NON-NLS-1$ //$NON-NLS-2$
         store.setValue(TARGET + "." + i + ".class", target.getClass().getName());//$NON-NLS-1$ //$NON-NLS-2$
         store.setValue(TARGET + "." + i + ".parameters", target.getParameters());//$NON-NLS-1$ //$NON-NLS-2$

         i++;
      }
      store.setValue(TARGET_COUNT, i);

      // Force saving of targets
      this.savePluginPreferences();
   }

   /**
    * Description of the Method
    *
    * @param context        Description of the Parameter
    * @exception Exception  Description of the Exception
    */
   public void start(BundleContext context) throws Exception
   {
      super.start(context);
      this.refreshTargets();
   }

   /**
    * Returns the shared instance.
    *
    * @return   The default value
    */
   public static DeployerCorePlugin getDefault()
   {
      return plugin;
   }

   /**
    * Convenience method which returns the unique identifier of this plugin.
    *
    * @return   The unique indentifier value
    */
   public static String getUniqueIdentifier()
   {
      if (getDefault() == null)
      {
         // If the default instance is not yet initialized,
         // return a static identifier. This identifier must
         // match the plugin id defined in plugin.xml
         return "org.jboss.ide.eclipse.deployer.core";//$NON-NLS-1$
      }
      return getDefault().getBundle().getSymbolicName();
   }
}
