/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.ui.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.actions.WorkbenchRunnableAdapter;
import org.eclipse.jdt.internal.ui.util.ExceptionHandler;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;
import org.jboss.ide.eclipse.core.AbstractPlugin;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public abstract class BaseWizard extends Wizard implements INewWizard
{
   private IStructuredSelection selection;
   private IWorkbench workbench;


   /**Constructor for the BaseWizard object */
   public BaseWizard()
   {
      this.setNeedsProgressMonitor(true);
   }


   /**
    * Gets the selection attribute of the BaseWizard object
    *
    * @return   The selection value
    */
   public IStructuredSelection getSelection()
   {
      return this.selection;
   }


   /**
    * Gets the workbench attribute of the BaseWizard object
    *
    * @return   The workbench value
    */
   public IWorkbench getWorkbench()
   {
      return this.workbench;
   }


   /**
    * Description of the Method
    *
    * @param workbench         Description of the Parameter
    * @param currentSelection  Description of the Parameter
    */
   public void init(IWorkbench workbench, IStructuredSelection currentSelection)
   {
      this.workbench = workbench;
      this.selection = currentSelection;
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public boolean performFinish()
   {
      IWorkspaceRunnable op =
         new IWorkspaceRunnable()
         {
            public void run(IProgressMonitor monitor)
               throws CoreException, OperationCanceledException
            {
               try
               {
                  finishPage(monitor);
               }
               catch (InterruptedException e)
               {
                  throw new OperationCanceledException(e.getMessage());
               }
            }
         };
      try
      {
         this.getContainer().run(false, true, new WorkbenchRunnableAdapter(op));
      }
      catch (InvocationTargetException e)
      {
         this.handleFinishException(getShell(), e);
         return false;
      }
      catch (InterruptedException e)
      {
         return false;
      }
      return true;
   }


   /**
    * Subclasses should override to perform the actions of the wizard.
    * This method is run in the wizard container's context as a workspace runnable.
    *
    * @param monitor                   Description of the Parameter
    * @exception InterruptedException  Description of the Exception
    * @exception CoreException         Description of the Exception
    */
   protected void finishPage(IProgressMonitor monitor)
      throws InterruptedException, CoreException { }


   /**
    * Description of the Method
    *
    * @param shell  Description of the Parameter
    * @param e      Description of the Parameter
    */
   protected void handleFinishException(Shell shell, InvocationTargetException e)
   {
      String title = NewWizardMessages.NewElementWizard_op_error_title;//$NON-NLS-1$
      String message = NewWizardMessages.NewElementWizard_op_error_message;//$NON-NLS-1$
      ExceptionHandler.handle(e, shell, title, message);
   }


   /**
    * Description of the Method
    *
    * @param resource  Description of the Parameter
    */
   protected void openResource(final IFile resource)
   {
      IWorkbenchPage activePage = JavaPlugin.getActivePage();
      if (activePage != null)
      {
         try
         {
            IDE.openEditor(activePage, resource);
         }
         catch (PartInitException pie)
         {
            AbstractPlugin.logError("Unable to open resource " + resource, pie);//$NON-NLS-1$
         }
      }
   }


   /**
    * Description of the Method
    *
    * @param newResource  Description of the Parameter
    */
   protected void selectAndReveal(IResource newResource)
   {
      BasicNewResourceWizard.selectAndReveal(newResource, workbench.getActiveWorkbenchWindow());
   }
}
