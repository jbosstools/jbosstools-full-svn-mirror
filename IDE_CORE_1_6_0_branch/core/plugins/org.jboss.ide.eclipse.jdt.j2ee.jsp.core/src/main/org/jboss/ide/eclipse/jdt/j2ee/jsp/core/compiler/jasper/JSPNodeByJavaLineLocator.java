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
   public void doVisit(Node n) throws JasperException
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
