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
 * Node visitor used to locate the node designated by a JSP line number
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPNodeByJSPLineLocator extends Node.Visitor
{
   /** JSP column number to be mapped */
   private int column;
   /** Min line offset */
   private int columnOffset;
   /** JSP node whose JSP source code range  contains the JSP source line number to be mapped */
   private Node found = null;
   /** JSP line number to be mapped */
   private int line;
   /** Min line offset */
   private int lineOffset;


   /**
    * Constructor.
    *
    * @param line    Description of the Parameter
    * @param column  Description of the Parameter
    */
   public JSPNodeByJSPLineLocator(int line, int column)
   {
      this.line = line;
      this.column = column;
      this.lineOffset = Integer.MAX_VALUE;
      this.columnOffset = Integer.MAX_VALUE;
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
      int lineNumber = n.getStart().getLineNumber();
      int lineOffset = this.line - lineNumber;
      if (lineOffset >= 0 && lineOffset <= this.lineOffset)
      {
         this.lineOffset = lineOffset;

         if (this.lineOffset == 0)
         {
            int columnNumber = n.getStart().getColumnNumber();
            int columnOffset = this.column - columnNumber;
            if (columnOffset >= 0 && columnOffset <= this.columnOffset)
            {
               this.columnOffset = columnOffset;
               this.found = n;
            }
         }
         else
         {
            this.found = n;
         }
      }
   }


   /**
    * Get the column offset to the node
    *
    * @return   The columnOffset value
    */
   public int getColumnOffset()
   {
      return this.columnOffset;
   }


   /**
    * Get the line offset to the node
    *
    * @return   The lineOffset value
    */
   public int getLineOffset()
   {
      return this.lineOffset;
   }


   /**
    * Gets the JSP node to which the source line number was mapped.
    *
    * @return   The node value
    */
   public Node getNode()
   {
      return this.found;
   }
}
