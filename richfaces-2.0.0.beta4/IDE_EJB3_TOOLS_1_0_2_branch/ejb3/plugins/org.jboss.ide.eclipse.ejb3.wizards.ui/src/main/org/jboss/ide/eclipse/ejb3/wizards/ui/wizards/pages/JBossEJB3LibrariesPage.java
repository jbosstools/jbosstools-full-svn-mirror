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
package org.jboss.ide.eclipse.ejb3.wizards.ui.wizards.pages;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPage;
import org.jboss.ide.eclipse.ejb3.wizards.core.EJB3WizardsCorePlugin;
import org.jboss.ide.eclipse.ejb3.wizards.core.classpath.EJB3ClasspathContainer;
import org.jboss.ide.eclipse.ejb3.wizards.ui.EJB3WizardsUIPlugin;
import org.jboss.ide.eclipse.launcher.core.constants.IJBossConstants;

public class JBossEJB3LibrariesPage extends JBossSelectionPage implements IClasspathContainerPage
{

   private IClasspathEntry classpathEntry;

   public JBossEJB3LibrariesPage()
   {
      super();
   }

   private boolean jbossConfigurationHasEJB3(ILaunchConfiguration jbossConfiguration)
   {
      String jbossBaseDir = null, jbossConfigDir = null;

      IPath jarToCheck = EJB3ClasspathContainer.jbossConfigRelativeJarPaths[0];
      try
      {
         jbossBaseDir = jbossConfiguration.getAttribute(IJBossConstants.ATTR_JBOSS_HOME_DIR, "");
         jbossConfigDir = jbossConfiguration.getAttribute(IJBossConstants.ATTR_SERVER_CONFIGURATION, "default");
      }
      catch (CoreException e)
      {
         return false;
      }

      IPath absoluteJarPath = new Path(jbossBaseDir).append("server").append(jbossConfigDir).append(jarToCheck);

      return absoluteJarPath.toFile().exists();
   }

   public boolean finish()
   {

      if (configuration != null)
      {
         ILaunchConfiguration jbossConfiguration = EJB3WizardsCorePlugin.getDefault().getSelectedLaunchConfiguration();
         if (jbossConfigurationHasEJB3(jbossConfiguration))
         {
            classpathEntry = JavaCore.newContainerEntry(new Path(EJB3ClasspathContainer.CONTAINER_ID)
                  .append(jbossConfiguration.getName()), true);
            return true;
         }
         else
         {
            EJB3WizardsUIPlugin
                  .error("The selected configuration (\""
                        + jbossConfiguration.getName()
                        + "\")"
                        + " does not contain the expected EJB3 libraries. Please install JBoss with EJB3 enabled, or try another configuration. ");
         }
      }

      return false;
   }

   public boolean isPageComplete()
   {
      return configuration != null && isCurrentPage();
   }

   public IClasspathEntry getSelection()
   {
      return classpathEntry;
   }

   public void setSelection(IClasspathEntry containerEntry)
   {
      classpathEntry = containerEntry;
   }

}
