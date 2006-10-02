/*
 * JBoss, a division of Red Hat
 * Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
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
package org.jboss.ide.eclipse.jdt.aop.ui.actions;

import java.util.Iterator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.internal.Workbench;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;
import org.jboss.ide.eclipse.jdt.aop.core.util.PointcuttableValidator;

/**
 * The parent of Pointcuttable Actions.
 * This class can either be used as a popupMenu action delegate or a standalone action (associated with some sort of viewer)
 * 
 * @author aoswald, Marshall Culpepper
 */
public abstract class PointcuttableAction extends Action implements IObjectActionDelegate
{

   protected abstract void setSelection(ISelection selection);

   protected Viewer viewer;

   public PointcuttableAction()
   {
   }

   public PointcuttableAction(Viewer viewer)
   {
      this.viewer = viewer;
   }

   //based on the selection, enable/disable the button
   public void selectionChanged(IAction action, ISelection selection)
   {
      setSelection(selection);
      action.setEnabled(validatePointcuttable(selection));
   }

   // validate all objects in the current selection -- not just the first :)	
   protected boolean validatePointcuttable(ISelection selection)
   {
      if ((selection != null) && (selection instanceof IStructuredSelection))
      {
         IStructuredSelection structured = (IStructuredSelection) selection;
         Iterator iter = structured.iterator();

         while (iter.hasNext())
         {
            Object selected = iter.next();
            if (!PointcuttableValidator.validatePointcuttable(selected))
            {
               return false;
            }
         }
         return true;
      }
      return false;
   }

   /**
    * All pointcuttable actions will need to implement runAction (IAction) instead of the standard run(IAction).
    * We want to make sure that the project is cleaned after every pointcuttable action is executed.
    * 
    * (I.e... new methods need to be re-woven if an interceptor is applied to them, even though the JDT sees the class
    * as up to date, and therefore doesn't invoke the build)
    */
   public void run(IAction action)
   {
      runAction(action);

      try
      {

         AopCorePlugin.getDefault().cleanProject(AopCorePlugin.getCurrentJavaProject());

      }
      catch (CoreException e)
      {
         e.printStackTrace();
      }
   }

   public abstract void runAction(IAction action);

   public void run()
   {
      if (viewer != null)
      {
         setSelection(viewer.getSelection());
         setActivePart(this, Workbench.getInstance().getActiveWorkbenchWindow().getActivePage().getActivePart());
      }

      runAction(this);
   }
}
