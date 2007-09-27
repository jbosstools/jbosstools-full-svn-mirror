/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.core.compiler.jasper;

import org.apache.jasper.JasperException;
import org.apache.jasper.compiler.Node;

/**
 * Node visitor used to locate the node designated by a Java line number
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPNodeByJavaLineLocator extends Node.Visitor
{
   /** JSP node whose Java source code range in the generated servlet contains the Java source line number to be mapped */
   private Node found = null;
   /** Java Line number to be mapped */
   private int lineNum;
   /** Line offset calculated one the node has been found */
   private int offset;


   /**
    * Constructor.
    *
    * @param lineNum  Source line number
    */
   public JSPNodeByJavaLineLocator(int lineNum)
   {
      this.lineNum = lineNum;
   }


   /**
    * Callback visitor method.
    *
    * @param n                    Description of the Parameter
    * @exception JasperException  Description of the Exception
    */
   public void doVisit(Node n)
      throws JasperException
   {
      if ((this.lineNum >= n.getBeginJavaLine()) && (this.lineNum < n.getEndJavaLine()))
      {
         this.found = n;
         this.offset = lineNum - n.getBeginJavaLine();
      }
   }


   /**
    * Gets the JSP node to which the source line number in the generated
    * servlet code was mapped.
    *
    * @return   The node value
    */
   public Node getNode()
   {
      return this.found;
   }


   /**
    * Get the line offset to the JSP line
    *
    * @return   The offset value
    */
   public int getOffset()
   {
      return this.offset;
   }
}
