/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.core.compiler.jasper;

import org.apache.jasper.JspCompilationContext;
import org.apache.jasper.compiler.Compiler;
import org.apache.jasper.compiler.ErrorDispatcher;
import org.apache.jasper.compiler.ParserController;


/**
 * Extends the Jasper ParserController to override the ErrorDispatcher
 * used to dispatch problem from JSP compilation
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPParserController extends ParserController
{

   /**
    * Extended constructor
    *
    *
    * @param ctxt
    * @param compiler
    * @param errDispatcher
    */
   public JSPParserController(JspCompilationContext ctxt, Compiler compiler, ErrorDispatcher errDispatcher)
   {
      super(ctxt, compiler);
      this.err = errDispatcher;
   }

}
