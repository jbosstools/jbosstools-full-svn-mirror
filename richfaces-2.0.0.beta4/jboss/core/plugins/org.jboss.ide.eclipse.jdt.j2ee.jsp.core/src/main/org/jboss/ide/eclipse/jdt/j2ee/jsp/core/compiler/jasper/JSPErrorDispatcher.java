/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.core.compiler.jasper;

import org.apache.jasper.compiler.ErrorDispatcher;


/**
 * Extends the default ErrorDispatcher to override the ErrorHandler used.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPErrorDispatcher extends ErrorDispatcher
{

   /**
    * Constructor with the JSP informations
    *
    * @param info  Description of the Parameter
    */
   public JSPErrorDispatcher(JSPElementInfo info)
   {
      super(true);
      this.errHandler = new JSPErrorHandler(info);
   }

}
