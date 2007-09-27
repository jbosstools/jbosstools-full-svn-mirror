/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.core.compiler;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.core.JDTJ2EEJSPCoreMessages;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.core.compiler.jasper.JSPProject;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.core.compiler.jasper.JSPProjectManager;

/**
 * Notifier to report compilation progress.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPBuildNotifier
{
   private boolean cancelling;
   private JSPProject jspProject;

   private IProgressMonitor monitor;
   private float percentComplete;
   private IProject project;
   private String rootFolder;
   private int totalWork;
   private int workDone;


   /**Constructor for the JSPBuildNotifier object */
   public JSPBuildNotifier() { }


   /** Compilation beginning. */
   public void begin()
   {
      if (monitor != null)
      {
         monitor.beginTask(JDTJ2EEJSPCoreMessages.getString("JSPBuildNotifier.title.start") + this.rootFolder + JDTJ2EEJSPCoreMessages.getString("JSPBuildNotifier.title.end"), totalWork);//$NON-NLS-1$ //$NON-NLS-2$
      }
   }


   /**
    * Check if the user has requested a cancellation
    *
    * @return   Description of the Return Value
    */
   public boolean checkCancel()
   {
      return (monitor != null && monitor.isCanceled());
   }


   /**
    * A file is being compiled
    *
    * @param file  Description of the Parameter
    */
   public void compiled(IFile file)
   {
      String message = JDTJ2EEJSPCoreMessages.getString("JSPBuildNotifier.compiling") + file.getProjectRelativePath().toString();//$NON-NLS-1$
      this.subTask(message);
      this.updateProgress(1);
   }


   /** Called when the compilation is over. */
   public void done()
   {
      this.updateProgress(this.totalWork - this.workDone);
      this.subTask(JDTJ2EEJSPCoreMessages.getString("JSPBuildNotifier.done"));//$NON-NLS-1$
      if (monitor != null)
      {
         monitor.done();
      }
   }


   /**
    * Initialize the notifier.
    *
    * @param monitor  Description of the Parameter
    * @param project  Description of the Parameter
    */
   public void init(IProgressMonitor monitor, IProject project)
   {
      this.monitor = monitor;
      this.project = project;
      this.jspProject = JSPProjectManager.getJSPProject(this.project);
      this.rootFolder = this.jspProject.getUriRoot();
      this.cancelling = false;
      this.workDone = 0;
      this.totalWork = 1000000;
   }


   /**
    * Sets the cancelling attribute of the JSPBuildNotifier object
    *
    * @param cancelling  The new cancelling value
    */
   private void setCancelling(boolean cancelling)
   {
      this.cancelling = cancelling;
   }


   /**
    * Description of the Method
    *
    * @param message  Description of the Parameter
    */
   private void subTask(String message)
   {
      if (monitor != null)
      {
         monitor.subTask(message);
      }
   }


   /**
    * Description of the Method
    *
    * @param work  Description of the Parameter
    */
   private void updateProgress(int work)
   {
      this.workDone = this.workDone + work;
      if (monitor != null)
      {
         monitor.worked(work);
      }
   }
}
