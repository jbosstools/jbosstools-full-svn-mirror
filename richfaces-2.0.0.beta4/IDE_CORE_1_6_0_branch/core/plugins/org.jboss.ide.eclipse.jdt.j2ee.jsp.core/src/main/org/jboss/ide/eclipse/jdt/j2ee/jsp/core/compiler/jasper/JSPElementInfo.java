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
import org.apache.jasper.compiler.PageInfo;
import org.eclipse.core.resources.IFile;

/**
 * Wrapper for various informations about the JSP.
 * Each JSP has its own ErrorDispatcher
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPElementInfo
{
   private String content;

   private JSPErrorDispatcher errDispatcher;

   private IFile file;

   private String javaEncoding;

   private String javaFileName;

   private String jspFileName;

   private Node.Nodes nodes;

   private PageInfo pageInfo;

   private String[] smap;

   private JSPCompilationUnit unit;

   /** Default constuctor */
   public JSPElementInfo()
   {
   }

   /**
    * Full required featured constuctor
    *
    *
    * @param file
    * @param jspFileName
    * @param javaFileName
    * @param javaEncoding
    */
   public JSPElementInfo(IFile file, String jspFileName, String javaFileName, String javaEncoding)
   {
      this.file = file;
      this.jspFileName = jspFileName;
      this.javaFileName = javaFileName;
      this.javaEncoding = javaEncoding;
      this.errDispatcher = new JSPErrorDispatcher(this);
   }

   /**
    * Return a cached CompilationUnit for both the Eclipse compiler
    * and the ContentAssist processor.
    *
    * @return   The compilationUnit value
    */
   public JSPCompilationUnit getCompilationUnit()
   {
      if (this.unit == null)
      {
         this.unit = new JSPCompilationUnit(content.toCharArray(), javaFileName, javaEncoding);
      }
      return this.unit;
   }

   /**
    * Gets the content attribute of the JSPElementInfo object
    *
    * @return   The content value
    */
   public String getContent()
   {
      return this.content;
   }

   /**
    * Gets the errorDispatcher attribute of the JSPElementInfo object
    *
    * @return   The errorDispatcher value
    */
   public JSPErrorDispatcher getErrorDispatcher()
   {
      return this.errDispatcher;
   }

   /**
    * Gets the file attribute of the JSPElementInfo object
    *
    * @return   The file value
    */
   public IFile getFile()
   {
      return this.file;
   }

   /**
    * Gets the javaEncoding attribute of the JSPElementInfo object
    *
    * @return   The javaEncoding value
    */
   public String getJavaEncoding()
   {
      return this.javaEncoding;
   }

   /**
    * Gets the javaFileName attribute of the JSPElementInfo object
    *
    * @return   The javaFileName value
    */
   public String getJavaFileName()
   {
      return this.javaFileName;
   }

   /**
    * Gets the jspFileName attribute of the JSPElementInfo object
    *
    * @return   The jspFileName value
    */
   public String getJspFileName()
   {
      return this.jspFileName;
   }

   /**
    * Gets the nodes attribute of the JSPElementInfo object
    *
    * @return   The nodes value
    */
   public Node.Nodes getNodes()
   {
      return this.nodes;
   }

   /**
    * Gets the pageInfo attribute of the JSPElementInfo object
    *
    * @return   The pageInfo value
    */
   public PageInfo getPageInfo()
   {
      return pageInfo;
   }

   /**
    * Gets the smap attribute of the JSPElementInfo object
    *
    * @return   The smap value
    */
   public String[] getSmap()
   {
      return this.smap;
   }

   /**
    * Sets the content attribute of the JSPElementInfo object
    *
    * @param content  The new content value
    */
   public void setContent(String content)
   {
      this.content = content;
   }

   /**
    * Sets the nodes attribute of the JSPElementInfo object
    *
    * @param nodes  The new nodes value
    */
   public void setNodes(Node.Nodes nodes)
   {
      this.nodes = nodes;
   }

   /**
    * Sets the pageInfo attribute of the JSPElementInfo object
    *
    * @param pageInfo  The new pageInfo value
    */
   public void setPageInfo(PageInfo pageInfo)
   {
      this.pageInfo = pageInfo;
   }

   /**
    * Sets the smap attribute of the JSPElementInfo object
    *
    * @param smap  The new smap value
    */
   public void setSmap(String[] smap)
   {
      this.smap = smap;
   }
}
