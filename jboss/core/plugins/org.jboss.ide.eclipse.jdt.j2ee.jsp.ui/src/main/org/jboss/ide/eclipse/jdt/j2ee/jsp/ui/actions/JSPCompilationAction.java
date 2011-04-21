/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionDelegate;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.core.JDTJ2EEJSPCorePlugin;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.core.compiler.jasper.JSPProject;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.core.compiler.jasper.JSPProjectManager;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.JDTJ2EEJSPUIMessages;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPCompilationAction extends ActionDelegate implements IObjectActionDelegate
{
   /** Description of the Field */
   protected IWorkbenchPart part = null;


   /**Constructor for the JSPCompilationAction object */
   public JSPCompilationAction() { }


   /**
    * Main processing method for the XDocletRunAction object
    *
    * @param action  Description of the Parameter
    */
   public void run(IAction action)
   {
      // Gets the selection provider and extract what we need
      ISelectionProvider provider = this.part.getSite().getSelectionProvider();
      StructuredSelection selection = (StructuredSelection) provider.getSelection();
      Collection projects = new ArrayList();
      Collection files = new ArrayList();
      Iterator it = selection.iterator();
      while (it.hasNext())
      {
         Object o = it.next();

         if (o instanceof IProject)
         {
            projects.add(o);
         }
         else if (o instanceof IFolder)
         {
            IFolder folder = (IFolder) o;
            String webapp = JSPProjectManager.getPropertyFromWorkspace(folder.getProject(), JSPProjectManager.QNAME_WEBROOT);
            if (folder.getProjectRelativePath().toString().equals(webapp))
            {
               projects.add(folder.getProject());
            }
            else
            {
               this.appendFiles(files, (IFolder) o);
            }
         }
         else if (o instanceof IFile)
         {
            files.add(o);
         }
      }
      this.processProjects(projects);
      this.processFiles(files);
   }


   /**
    * Description of the Method
    *
    * @param action     Description of the Parameter
    * @param selection  Description of the Parameter
    */
   public void selectionChanged(IAction action, ISelection selection) { }


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
    * Description of the Method
    *
    * @param files   Description of the Parameter
    * @param folder  Description of the Parameter
    */
   protected void appendFiles(Collection files, IFolder folder)
   {
      try
      {
         IResource[] resources = folder.members();
         for (int i = 0; i < resources.length; i++)
         {
            IResource resource = resources[i];
            if (resource instanceof IFile)
            {
               files.add(resource);
            }
            else if (resource instanceof IFolder)
            {
               this.appendFiles(files, (IFolder) resource);
            }
         }
      }
      catch (CoreException e)
      {
         // Do nothing
      }
   }


   /**
    * Description of the Method
    *
    * @param files  Description of the Parameter
    */
   protected void processFiles(Collection files)
   {
      Iterator it = files.iterator();
      while (it.hasNext())
      {
         final IFile file = (IFile) it.next();
         String title = JDTJ2EEJSPUIMessages.getString("JSPCompilationAction.action") + file.getProjectRelativePath().toString();//$NON-NLS-1$

         Job job =
            new Job(title)
            {
               protected IStatus run(IProgressMonitor monitor)
               {
                  JSPProject jspProject = JSPProjectManager.getJSPProject(file.getProject());
                  jspProject.compileJSP(file);
                  return Status.OK_STATUS;
               }
            };
         job.setRule(file);
         job.setPriority(Job.BUILD);
         job.schedule();
      }
   }


   /**
    * Description of the Method
    *
    * @param projects  Description of the Parameter
    */
   protected void processProjects(Collection projects)
   {
      Iterator it = projects.iterator();
      while (it.hasNext())
      {
         final IProject project = (IProject) it.next();
         Job job =
            new Job(JDTJ2EEJSPUIMessages.getString("JSPCompilationAction.title")//$NON-NLS-1$
            )
            {

               protected IStatus run(IProgressMonitor monitor)
               {
                  try
                  {
                     project.build(IncrementalProjectBuilder.FULL_BUILD, JDTJ2EEJSPCorePlugin.JSP_BUILDER_ID, null, monitor);
                  }
                  catch (CoreException e)
                  {
                     // Do nothing
                  }
                  return Status.OK_STATUS;
               }
            };
         job.setPriority(Job.BUILD);
         job.schedule();
      }
   }
}
