/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.core.compiler;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.core.compiler.jasper.JSPProject;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.core.compiler.jasper.JSPProjectManager;

/**
 * Incremental builder used to compile the JSP file
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPBuilder extends IncrementalProjectBuilder implements IResourceVisitor, IResourceDeltaVisitor
{

   /** Notifier for the build progress */
   protected JSPBuildNotifier notifier = new JSPBuildNotifier();


   /** Default constructor */
   public JSPBuilder() { }


   /**
    * Implementation of the IResourceVisitor interface.
    *
    * @param resource  Description of the Parameter
    * @return          Description of the Return Value
    */
   public boolean visit(IResource resource)
   {
      // Stop if user has request interruption
      if (this.notifier.checkCancel())
      {
         return false;
      }

      if (resource.getType() == IResource.FILE)
      {
         IFile file = (IFile) resource;
         this.processFile(file);
      }
      return true;
   }


   /**
    * Implementation of the IResourceDeltaVisitor interface.
    *
    * @param delta              Description of the Parameter
    * @return                   Description of the Return Value
    * @exception CoreException  Description of the Exception
    */
   public boolean visit(IResourceDelta delta)
      throws CoreException
   {
      // Stop if user has request interruption
      if (this.notifier.checkCancel())
      {
         return false;
      }

      IResource resource = delta.getResource();
      if (resource.getType() == IResource.FILE)
      {
         IFile file = (IFile) resource;

         int kind = delta.getKind();
         switch (kind)
         {
            case IResourceDelta.ADDED:
            case IResourceDelta.CHANGED:
            case IResourceDelta.CONTENT:
            case IResourceDelta.ENCODING:
            case IResourceDelta.REPLACED:
               this.processFile(file);
               break;
            default:
            // Do nothing
         }
      }
      return true;
   }


   /**
    * Main build method.
    *
    * @param kind               Description of the Parameter
    * @param args               Description of the Parameter
    * @param monitor            Description of the Parameter
    * @return                   Description of the Return Value
    * @exception CoreException  Description of the Exception
    */
   protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
      throws CoreException
   {
      this.notifier.init(monitor, this.getProject());

      switch (kind)
      {
         case FULL_BUILD:
         case CLEAN_BUILD:
            this.fullBuild(monitor);
            break;
         case AUTO_BUILD:
         case INCREMENTAL_BUILD:
            IResourceDelta delta = this.getDelta(this.getProject());
            if ((delta == null)
            /*
             * || (delta.getResource() == this.getProject())
             */
               )
            {
               this.fullBuild(monitor);
            }
            else
            {
               this.incrementalBuild(delta, monitor);
            }
            break;
         default:
         // Do nothing
      }

      this.notifier.done();

      return null;
   }


   /**
    * Performs a full build on the project.
    *
    * @param monitor            Description of the Parameter
    * @exception CoreException  Description of the Exception
    */
   protected void fullBuild(final IProgressMonitor monitor)
      throws CoreException
   {
      JSPProject jspProject = JSPProjectManager.getJSPProject(this.getProject());
      jspProject.reset();
      this.getProject().accept(this);
   }


   /**
    * Performs a incremental build on the project.
    *
    * @param delta              Description of the Parameter
    * @param monitor            Description of the Parameter
    * @exception CoreException  Description of the Exception
    */
   protected void incrementalBuild(IResourceDelta delta, IProgressMonitor monitor)
      throws CoreException
   {
      delta.accept(this);
   }


   /**
    * Delegates the actual compilation to the JSPProject instance.
    *
    * @param file  Description of the Parameter
    */
   protected void processFile(IFile file)
   {
      this.notifier.compiled(file);
      JSPProject jspProject = JSPProjectManager.getJSPProject(this.getProject());
      jspProject.compileJSP(file);
   }
}
