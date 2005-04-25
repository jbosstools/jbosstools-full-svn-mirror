/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.ui.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.util.ExceptionHandler;
import org.eclipse.jdt.internal.ui.wizards.ClassPathDetector;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jdt.ui.wizards.JavaCapabilityConfigurationPage;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

/**
 * Base class for New Project creation wizards.
 * Derived from org.eclipse.jdt.internal.ui.wizards.NewProjectCreationWizardPage
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public abstract class ProjectWizardPage extends JavaCapabilityConfigurationPage
{
   /** Description of the Field */
   protected boolean fCanRemoveContent;
   /** Description of the Field */
   protected IProject fCurrProject;
   /** Description of the Field */
   protected IPath fCurrProjectLocation;
   /** Description of the Field */
   protected WizardNewProjectCreationPage fMainPage;


   /**
    *Constructor for the ProjectWizardPage object
    *
    * @param mainPage  Description of the Parameter
    */
   public ProjectWizardPage(WizardNewProjectCreationPage mainPage)
   {
      super();
      this.fMainPage = mainPage;
      this.fCurrProjectLocation = null;
      this.fCurrProject = null;
      this.fCanRemoveContent = false;
   }


   /** Called from the wizard on cancel. */
   public void performCancel()
   {
      this.removeProject();
   }


   /**
    * Called from the wizard on finish.
    *
    * @param monitor                   Description of the Parameter
    * @exception CoreException         Description of the Exception
    * @exception InterruptedException  Description of the Exception
    */
   public void performFinish(IProgressMonitor monitor)
      throws CoreException, InterruptedException
   {
      try
      {
         monitor.beginTask(NewWizardMessages.getString("NewProjectCreationWizardPage.createproject.desc"), 3);//$NON-NLS-1$
         if (fCurrProject == null)
         {
            this.updateProject(true, new SubProgressMonitor(monitor, 1));
         }
         this.configureJavaProject(new SubProgressMonitor(monitor, 2));
      }
      finally
      {
         monitor.done();
         fCurrProject = null;
      }
   }


   /**
    * Sets the visible attribute of the ProjectWizardPage object
    *
    * @param visible  The new visible value
    */
   public void setVisible(boolean visible)
   {
      if (visible)
      {
         this.changeToNewProject();
      }
      else
      {
         this.removeProject();
      }
      super.setVisible(visible);
   }


   /** Description of the Method */
   protected void changeToNewProject()
   {
      IProject newProjectHandle = fMainPage.getProjectHandle();
      IPath newProjectLocation = fMainPage.getLocationPath();

      if (fMainPage.useDefaults())
      {
         fCanRemoveContent = !newProjectLocation.append(fMainPage.getProjectName()).toFile().exists();
      }
      else
      {
         fCanRemoveContent = !newProjectLocation.toFile().exists();
      }

      final boolean initialize = !(newProjectHandle.equals(fCurrProject) && newProjectLocation.equals(fCurrProjectLocation));

      IRunnableWithProgress op =
         new IRunnableWithProgress()
         {
            public void run(IProgressMonitor monitor)
               throws InvocationTargetException, InterruptedException
            {
               try
               {
                  updateProject(initialize, monitor);
               }
               catch (CoreException e)
               {
                  throw new InvocationTargetException(e);
               }
            }
         };

      try
      {
         this.getContainer().run(false, true, op);
      }
      catch (InvocationTargetException e)
      {
         String title = NewWizardMessages.getString("NewProjectCreationWizardPage.EarlyCreationOperation.error.title");//$NON-NLS-1$
         String message = NewWizardMessages.getString("NewProjectCreationWizardPage.EarlyCreationOperation.error.desc");//$NON-NLS-1$
         ExceptionHandler.handle(e, getShell(), title, message);
      }
      catch (InterruptedException e)
      {
         // cancel pressed
      }
   }


   /**
    * Add necessary source and libraries
    *
    * @param entries
    * @return
    * @throws CoreException
    */
   protected abstract IClasspathEntry[] checkEntries(IClasspathEntry[] entries)
      throws CoreException;


   /** Description of the Method */
   protected void removeProject()
   {
      if (fCurrProject == null || !fCurrProject.exists())
      {
         return;
      }

      IRunnableWithProgress op =
         new IRunnableWithProgress()
         {
            public void run(IProgressMonitor monitor)
               throws InvocationTargetException, InterruptedException
            {
               boolean noProgressMonitor = Platform.getLocation().equals(fCurrProjectLocation);
               if (monitor == null || noProgressMonitor)
               {
                  monitor = new NullProgressMonitor();
               }
               monitor.beginTask(NewWizardMessages.getString("NewProjectCreationWizardPage.removeproject.desc"), 3);//$NON-NLS-1$

               try
               {
                  fCurrProject.delete(fCanRemoveContent, false, monitor);
               }
               catch (CoreException e)
               {
                  throw new InvocationTargetException(e);
               }
               finally
               {
                  monitor.done();
                  fCurrProject = null;
                  fCanRemoveContent = false;
               }
            }
         };

      try
      {
         this.getContainer().run(false, true, op);
      }
      catch (InvocationTargetException e)
      {
         String title = NewWizardMessages.getString("NewProjectCreationWizardPage.op_error.title");//$NON-NLS-1$
         String message = NewWizardMessages.getString("NewProjectCreationWizardPage.op_error_remove.message");//$NON-NLS-1$
         ExceptionHandler.handle(e, getShell(), title, message);
      }
      catch (InterruptedException e)
      {
         // cancel pressed
      }
   }


   /**
    * Description of the Method
    *
    * @param initialize                Description of the Parameter
    * @param monitor                   Description of the Parameter
    * @exception CoreException         Description of the Exception
    * @exception InterruptedException  Description of the Exception
    */
   protected void updateProject(boolean initialize, IProgressMonitor monitor)
      throws CoreException, InterruptedException
   {
      fCurrProject = fMainPage.getProjectHandle();
      fCurrProjectLocation = fMainPage.getLocationPath();
      boolean noProgressMonitor = !initialize && fCanRemoveContent;

      if (monitor == null || noProgressMonitor)
      {
         monitor = new NullProgressMonitor();
      }
      try
      {
         monitor.beginTask(NewWizardMessages.getString("NewProjectCreationWizardPage.EarlyCreationOperation.desc"), 2);//$NON-NLS-1$

         createProject(fCurrProject, fCurrProjectLocation, new SubProgressMonitor(monitor, 1));
         if (initialize)
         {
            IClasspathEntry[] entries = null;
            IPath outputLocation = null;

            if (fCurrProjectLocation.toFile().exists() && !Platform.getLocation().equals(fCurrProjectLocation))
            {
               // detect classpath
               if (!fCurrProject.getFile(".classpath").exists()//$NON-NLS-1$
               )
               {
                  // if .classpath exists noneed to look for files
                  ClassPathDetector detector = new ClassPathDetector(fCurrProject.getProject(), monitor);
                  entries = detector.getClasspath();
                  outputLocation = detector.getOutputLocation();
               }
            }

            entries = this.checkEntries(entries);
            //            outputLocation = new Path(fCurrProject.getName() + "/bin");

            this.init(JavaCore.create(fCurrProject), outputLocation, entries, false);
         }
         monitor.worked(1);
      }
      finally
      {
         monitor.done();
      }
   }
}
