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
import org.apache.jasper.compiler.PageInfo;


/**
 * Extends the Jasper compiler to override the default
 * ErrorDispatcher for a given PageInfo. These hooks allow Eclipse
 * to report Problem and Error to the UI.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPCompiler extends Compiler
{

   /**
    * Extended constructor
    *
    *
    * @param ctxt
    * @param errDispatcher
    * @param pageInfo
    */
   public JSPCompiler(JspCompilationContext ctxt, ErrorDispatcher errDispatcher, PageInfo pageInfo)
   {
      super(ctxt);
      this.errDispatcher = errDispatcher;
      this.pageInfo = pageInfo;
   }

}
