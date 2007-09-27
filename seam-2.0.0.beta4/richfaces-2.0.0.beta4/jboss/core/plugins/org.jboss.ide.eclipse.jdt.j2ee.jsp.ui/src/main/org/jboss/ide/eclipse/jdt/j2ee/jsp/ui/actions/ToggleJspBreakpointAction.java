/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IBreakpointManager;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.jdt.debug.core.IJavaStratumLineBreakpoint;
import org.eclipse.jdt.debug.core.JDIDebugModel;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.source.IVerticalRulerInfo;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.core.compiler.jasper.JSPProject;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.core.compiler.jasper.JSPProjectManager;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.JDTJ2EEJSPUIMessages;

/**
 * Action that installs a JSP breakpoint. The installation is simple
 * thanks to the JSR45 support in the Eclipse Debug layer.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class ToggleJspBreakpointAction extends Action implements IAction
{
   private IVerticalRulerInfo rulerInfo;
   private ITextEditor textEditor;


   /**
    *Constructor for the ToggleJspBreakpointAction object
    *
    * @param editor     Description of the Parameter
    * @param rulerInfo  Description of the Parameter
    */
   public ToggleJspBreakpointAction(ITextEditor editor, IVerticalRulerInfo rulerInfo)
   {
      super(JDTJ2EEJSPUIMessages.getString("ToggleJspBreakpointAction.title"));//$NON-NLS-1$
      this.textEditor = editor;
      this.rulerInfo = rulerInfo;
   }


   /** Main processing method for the ToggleJspBreakpointAction object */
   public void run()
   {
      IBreakpointManager manager = DebugPlugin.getDefault().getBreakpointManager();
      IBreakpoint[] breakpoints = manager.getBreakpoints();
      IResource resource = this.getResource();
      int lineNumber = this.rulerInfo.getLineOfLastMouseButtonActivity() + 1;
      for (int i = 0; i < breakpoints.length; i++)
      {
         IBreakpoint bp = breakpoints[i];
         if (bp instanceof IJavaStratumLineBreakpoint)
         {
            IJavaStratumLineBreakpoint breakpoint = (IJavaStratumLineBreakpoint) bp;
            if (breakpoint.getMarker().getResource().equals(resource))
            {
               try
               {
                  if (breakpoint.getLineNumber() == lineNumber)
                  {
                     // remove breakpoint
                     breakpoint.delete();
                     return;
                  }
               }
               catch (CoreException e)
               {
                  AbstractPlugin.logError("Unable to setup breakpoint", e);//$NON-NLS-1$
               }
            }
         }
      }
      this.createBreakpoint();
   }


   /** Description of the Method */
   protected void createBreakpoint()
   {
      IResource resource = this.getResource();
      int lineNumber = rulerInfo.getLineOfLastMouseButtonActivity() + 1;
      try
      {
         IProject project = this.getResource().getProject();
         JSPProject jspProject = JSPProjectManager.getJSPProject(project);
         IFolder folder = jspProject.getUriRootFolder();
         String uriRootPath = folder.getProjectRelativePath().toString();
         String resourcePath = resource.getProjectRelativePath().toString();
         resourcePath = resourcePath.substring(uriRootPath.length() + 1);

         // Actual creation of the breakpoint :
         // - The stratum is JSP
         // - The source path is relative to the docroot.
         JDIDebugModel.createStratumBreakpoint(resource, "JSP", resource.getName(), resourcePath, null, lineNumber, -1, -1, 0, true, null);//$NON-NLS-1$
      }
      catch (CoreException e)
      {
         AbstractPlugin.logError("Unable to create breakpoint", e);//$NON-NLS-1$
      }
   }


   /**
    * Gets the resource attribute of the ToggleJspBreakpointAction object
    *
    * @return   The resource value
    */
   protected IResource getResource()
   {
      IEditorInput input = this.textEditor.getEditorInput();
      IResource resource = (IResource) input.getAdapter(IFile.class);
      if (resource == null)
      {
         resource = (IResource) input.getAdapter(IResource.class);
      }
      return resource;
   }
}
