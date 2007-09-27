/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.core.compiler.jasper;

import org.eclipse.jdt.internal.compiler.batch.CompilationUnit;

/**
 * Wrapper for a JSP Java source file.
 * This is required to generate the class file by the Eclipse compiler.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPCompilationUnit extends CompilationUnit
{

   /**
    * Extended constructor
    *
    *
    * @param contents
    * @param fileName
    * @param encoding
    */
   public JSPCompilationUnit(char[] contents, String fileName, String encoding)
   {
      super(contents, fileName, encoding);
   }

}
