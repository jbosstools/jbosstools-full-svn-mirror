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
package org.jboss.ide.eclipse.launcher.ui.views;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.launcher.core.ServerLaunchManager;
import org.jboss.ide.eclipse.launcher.core.constants.IJBossConstants;
import org.jboss.ide.eclipse.launcher.core.constants.IServerLaunchConfigurationConstants;
import org.jboss.ide.eclipse.launcher.core.logfiles.LogFile;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   18 mai 2003
 */
public class ServerNavigatorContentProvider implements ITreeContentProvider
{

   /**
    * @see   org.eclipse.jface.viewers.IContentProvider#dispose()
    */
   public void dispose()
   {
   }

   /**
    * @param parentElement  Description of the Parameter
    * @return               The children value
    * @see                  org.eclipse.jface.viewers.ITreeContentProvider#getChildren(Object)
    */
   public Object[] getChildren(Object parentElement)
   {
      if (parentElement instanceof ILaunchConfiguration)
      {
         try
         {
            return ServerLaunchManager.getInstance().getLogFiles((ILaunchConfiguration) parentElement);
         }
         catch (CoreException e)
         {
            AbstractPlugin.log(e);
            return null;
         }
      }
      return null;
   }

   /**
    * @param inputElement  Description of the Parameter
    * @return              The elements value
    * @see                 org.eclipse.jface.viewers.IStructuredContentProvider#getElements(Object)
    */
   public Object[] getElements(Object inputElement)
   {
      try
      {
         return ServerLaunchManager.getInstance().getServerConfigurations();
      }
      catch (CoreException e)
      {
         AbstractPlugin.log(e);
         return null;
      }
   }

   /**
    * @param element  Description of the Parameter
    * @return         The parent value
    * @see            org.eclipse.jface.viewers.ITreeContentProvider#getParent(Object)
    */
   public Object getParent(Object element)
   {
      if (element instanceof LogFile)
      {
         return ((LogFile) element).getConfiguration();
      }
      return null;
   }

   /**
    * @param element  Description of the Parameter
    * @return         Description of the Return Value
    * @see            org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(Object)
    */
   public boolean hasChildren(Object element)
   {
      if (element instanceof ILaunchConfiguration)
      {
         try
         {
            return (ServerLaunchManager.getInstance().getLogFiles((ILaunchConfiguration) element).length > 0);
         }
         catch (CoreException e)
         {
            AbstractPlugin.log(e);
            return false;
         }
      }
      return false;
   }

   /**
    * @param viewer    Description of the Parameter
    * @param oldInput  Description of the Parameter
    * @param newInput  Description of the Parameter
    * @see             org.eclipse.jface.viewers.IContentProvider#inputChanged(Viewer,
    *      Object, Object)
    */
   public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
   {
   }

}
