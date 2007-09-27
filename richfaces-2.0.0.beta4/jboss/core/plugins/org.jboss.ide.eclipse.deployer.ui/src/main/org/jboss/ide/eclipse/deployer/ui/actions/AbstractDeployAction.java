/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
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
public abstract class AbstractDeployAction extends ActionDelegate implements IObjectActionDelegate, IWorkbenchWindowActionDelegate
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
   public void dispose() { }


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
      }else {
      	
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
    * Gets the deploymentTarget attribute of the AbstractDeployAction object
    *
    * @param resource  Description of the Parameter
    * @return          The deploymentTarget value
    */
   protected abstract ITarget getDeploymentTarget(IResource resource);


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
      Iterator it = resources.iterator();
      while (it.hasNext())
      {
         final IResource resource = (IResource) it.next();
         final ITarget target = this.getDeploymentTarget(resource);
         if (target != null)
         {
            Job job =
               new Job(this.getTitle(resource))
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
