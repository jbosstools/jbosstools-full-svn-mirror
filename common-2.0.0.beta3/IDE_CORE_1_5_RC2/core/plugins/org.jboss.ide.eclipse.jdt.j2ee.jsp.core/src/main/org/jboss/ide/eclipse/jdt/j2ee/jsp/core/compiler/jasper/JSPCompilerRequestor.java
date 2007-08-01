/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.core.compiler.jasper;

import org.apache.jasper.compiler.Node;
import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ICompilerRequestor;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.core.compiler.JSPMarkerFactory;

/**
 * Captures all the problem during the compilation of the JSP Java source file.
 * A requestor is always needed by the Eclipse compiler.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPCompilerRequestor implements ICompilerRequestor
{

   /** The JSP informations */
   private JSPElementInfo info;


   /** Default constructor  */
   public JSPCompilerRequestor() { }


   /**
    * Callback method called by the Eclipse compiler to report problems.
    *
    *
    * @param result
    */
   public void acceptResult(CompilationResult result)
   {
      IProblem[] problems = result.getProblems();
      if (problems != null)
      {
         for (int i = 0; i < problems.length; i++)
         {
            IProblem problem = problems[i];

            try
            {
               // Create a new marker
               IMarker marker = JSPMarkerFactory.createMarker(this.info.getFile());

               // Set various attributes
               if (problem.isWarning())
               {
                  marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
               }
               else
               {
                  marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
               }
               marker.setAttribute(IMarker.MESSAGE, problem.getMessage());

               // Map the Java line to a JSP node
               JSPNodeByJavaLineLocator visitor = new JSPNodeByJavaLineLocator(problem.getSourceLineNumber());
               Node.Nodes nodes = info.getNodes();
               nodes.visit(visitor);
               Node srcNode = visitor.getNode();

               // If found, add line location
               if (srcNode != null)
               {
                  int line = srcNode.getStart().getLineNumber() + visitor.getOffset();
                  marker.setAttribute(IMarker.LINE_NUMBER, line);
               }
            }
            catch (Exception e)
            {
               AbstractPlugin.log(e);
            }
         }
      }
   }


   /**
    * Set the JSP informations for the current JSP
    *
    *
    * @param info
    */
   public void setInfo(JSPElementInfo info)
   {
      this.info = info;
   }
}
