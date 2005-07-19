/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
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
