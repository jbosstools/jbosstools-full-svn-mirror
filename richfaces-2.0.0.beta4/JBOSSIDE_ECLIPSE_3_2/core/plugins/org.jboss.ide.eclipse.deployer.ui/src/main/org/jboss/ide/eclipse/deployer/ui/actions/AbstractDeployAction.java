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
package org.jboss.ide.eclipse.deployer.ui.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.actions.ActionDelegate;
import org.jboss.ide.eclipse.deployer.core.target.ITarget;
import org.jboss.ide.eclipse.deployer.ui.decorators.DeployedDecorator;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public abstract class AbstractDeployAction extends ActionDelegate
      implements
         IObjectActionDelegate,
         IWorkbenchWindowActionDelegate
{
   /** Description of the Field */
   protected IWorkbenchPart part = null;

   /** Description of the Field */
   protected ISelection selection = null;

   /** Description of the Field */
   protected IWorkbenchWindow window = null;

   /**Constructor for the XDocletRunAction object */
   public AbstractDeployAction()
   {
      super();
   }

   /** Description of the Method */
   public void dispose()
   {
   }

   /**
    * Description of the Method
    *
    * @param window  Description of the Parameter
    */
   public void init(IWorkbenchWindow window)
   {
      this.window = window;
   }

   /**
    * Main processing method for the XDocletRunAction object
    *
    * @param action  Description of the Parameter
    */
   public void run(IAction action)
   {
      if (this.selection != null && (this.selection instanceof IStructuredSelection))
      {
         IStructuredSelection sel = (IStructuredSelection) this.selection;
         Collection resources = new ArrayList();
         Iterator it = sel.iterator();
         while (it.hasNext())
         {
            Object o = it.next();
            // For each resource
            if (o instanceof IResource)
            {
               resources.add(o);
            }
         }
         this.process(resources);
      }
      else
      {

      }
   }

   /**
    * Description of the Method
    *
    * @param action     Description of the Parameter
    * @param selection  Description of the Parameter
    */
   public void selectionChanged(IAction action, ISelection selection)
   {
      this.selection = selection;
   }

   /**
    * @param action      The new ActivePart value
    * @param targetPart  The new ActivePart value
    * @see               org.eclipse.ui.IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
    */
   public void setActivePart(IAction action, IWorkbenchPart targetPart)
   {
      this.part = targetPart;
   }

   /**
    * Gets the Deployment targets associated with the passed in resources
    *
    * @return          The deploymentTarget value
    */
   protected abstract ITarget[] getDeploymentTargets(IResource[] resources);

   /**
    * Description of the Method
    *
    * @param resource  Description of the Parameter
    * @return          The title value
    */
   protected abstract String getTitle(IResource resource);

   /**
    * Description of the Method
    *
    * @param resources  Description of the Parameter
    */
   protected void process(Collection resources)
   {
      IResource[] resourcesArray = (IResource[]) resources.toArray(new IResource[resources.size()]);
      ITarget[] targets = getDeploymentTargets(resourcesArray);
      for (int i = 0; i < resourcesArray.length; i++)
      {
         // If there is only 1 target, it should be the same for all resources
         final ITarget target = targets.length == 1 ? targets[0] : targets[i];
         final IResource resource = resourcesArray[i];

         if (target != null)
         {
            Job job = new Job(this.getTitle(resourcesArray[i]))
            {
               protected IStatus run(IProgressMonitor monitor)
               {
                  process(target, resource);
                  refresh(resource);
                  return Status.OK_STATUS;
               }
            };
            job.setRule(resource);
            job.setPriority(Job.BUILD);
            job.schedule();
         }
      }
   }

   /**
    * Description of the Method
    *
    * @param target    Description of the Parameter
    * @param resource  Description of the Parameter
    */
   protected abstract void process(ITarget target, IResource resource);

   /**
    * Description of the Method
    *
    * @param resource  Description of the Parameter
    */
   protected void refresh(IResource resource)
   {
      DeployedDecorator decorator = DeployedDecorator.getDeployedDecorator();
      if (decorator != null)
      {
         decorator.refresh(resource);
      }
   }
}
