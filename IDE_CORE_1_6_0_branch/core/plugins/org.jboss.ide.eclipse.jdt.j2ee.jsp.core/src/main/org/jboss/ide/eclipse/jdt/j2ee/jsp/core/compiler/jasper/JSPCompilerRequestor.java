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
   public JSPCompilerRequestor()
   {
   }

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
