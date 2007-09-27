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

import java.io.File;
import java.io.PrintWriter;
import java.net.MalformedURLException;

import javax.servlet.ServletContext;

import org.apache.jasper.Options;
import org.apache.jasper.compiler.ErrorDispatcher;
import org.apache.jasper.compiler.JspConfig;
import org.apache.jasper.compiler.JspRuntimeContext;
import org.apache.jasper.compiler.TagPluginManager;
import org.apache.jasper.compiler.TldLocationsCache;
import org.apache.jasper.servlet.JspCServletContext;

/**
 * Holds the JSP compilation environment and its preferences
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPCompilationEnvironment implements Options
{
   private ServletContext context;

   private ErrorDispatcher errDispatcher;

   private String javaEncoding = "UTF-8";//$NON-NLS-1$

   private JspConfig jspConfig;

   private JspRuntimeContext rctxt;

   private File scratchDir;

   private TagPluginManager tagPluginManager;

   private TldLocationsCache tldLocationsCache;

   private String uriRoot;

   /**Constructor for the JSPCompilationEnvironment object */
   public JSPCompilationEnvironment()
   {
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public boolean genStringAsCharArray()
   {
      return false;
   }

   /**
    * Gets the checkInterval attribute of the JSPCompilationEnvironment object
    *
    * @return   The checkInterval value
    */
   public int getCheckInterval()
   {
      return 0;
   }

   /**
    * Gets the classDebugInfo attribute of the JSPCompilationEnvironment object
    *
    * @return   The classDebugInfo value
    */
   public boolean getClassDebugInfo()
   {
      return true;
   }

   /**
    * Gets the classPath attribute of the JSPCompilationEnvironment object
    *
    * @return   The classPath value
    */
   public String getClassPath()
   {
      return null;
   }

   /**
    * Gets the compiler attribute of the JSPCompilationEnvironment object
    *
    * @return   The compiler value
    */
   public String getCompiler()
   {
      return null;
   }

   /**
    * Gets the context attribute of the JSPCompilationEnvironment object
    *
    * @return   The context value
    */
   public ServletContext getContext()
   {
      return context;
   }

   /**
    * Gets the development attribute of the JSPCompilationEnvironment object
    *
    * @return   The development value
    */
   public boolean getDevelopment()
   {
      return false;
   }

   /**
    * Gets the errDispatcher attribute of the JSPCompilationEnvironment object
    *
    * @return   The errDispatcher value
    */
   public ErrorDispatcher getErrDispatcher()
   {
      return errDispatcher;
   }

   /**
    * Gets the errorOnUseBeanInvalidClassAttribute attribute of the JSPCompilationEnvironment object
    *
    * @return   The errorOnUseBeanInvalidClassAttribute value
    */
   public boolean getErrorOnUseBeanInvalidClassAttribute()
   {
      return true;
   }

   /**
    * Gets the fork attribute of the JSPCompilationEnvironment object
    *
    * @return   The fork value
    */
   public boolean getFork()
   {
      return false;
   }

   /**
    * Gets the ieClassId attribute of the JSPCompilationEnvironment object
    *
    * @return   The ieClassId value
    */
   public String getIeClassId()
   {
      return null;
   }

   /**
    * Gets the javaEncoding attribute of the JSPCompilationEnvironment object
    *
    * @return   The javaEncoding value
    */
   public String getJavaEncoding()
   {
      return javaEncoding;
   }

   /**
    * Gets the jspConfig attribute of the JSPCompilationEnvironment object
    *
    * @return   The jspConfig value
    */
   public JspConfig getJspConfig()
   {
      return jspConfig;
   }

   /**
    * Gets the keepGenerated attribute of the JSPCompilationEnvironment object
    *
    * @return   The keepGenerated value
    */
   public boolean getKeepGenerated()
   {
      return false;
   }

   /**
    * Gets the mappedFile attribute of the JSPCompilationEnvironment object
    *
    * @return   The mappedFile value
    */
   public boolean getMappedFile()
   {
      return false;
   }

   /**
    * Gets the reloading attribute of the JSPCompilationEnvironment object
    *
    * @return   The reloading value
    */
   public boolean getReloading()
   {
      return false;
   }

   /**
    * Gets the runtimeContext attribute of the JSPCompilationEnvironment object
    *
    * @return   The runtimeContext value
    */
   public JspRuntimeContext getRuntimeContext()
   {
      return rctxt;
   }

   /**
    * Gets the scratchDir attribute of the JSPCompilationEnvironment object
    *
    * @return   The scratchDir value
    */
   public File getScratchDir()
   {
      return scratchDir;
   }

   /**
    * Gets the sendErrorToClient attribute of the JSPCompilationEnvironment object
    *
    * @return   The sendErrorToClient value
    */
   public boolean getSendErrorToClient()
   {
      return false;
   }

   /**
    * Gets the tagPluginManager attribute of the JSPCompilationEnvironment object
    *
    * @return   The tagPluginManager value
    */
   public TagPluginManager getTagPluginManager()
   {
      return tagPluginManager;
   }

   /**
    * Gets the tldLocationsCache attribute of the JSPCompilationEnvironment object
    *
    * @return   The tldLocationsCache value
    */
   public TldLocationsCache getTldLocationsCache()
   {
      return tldLocationsCache;
   }

   /**
    * Gets the trimSpaces attribute of the JSPCompilationEnvironment object
    *
    * @return   The trimSpaces value
    */
   public boolean getTrimSpaces()
   {
      return false;
   }

   /**
    * Gets the uriRoot attribute of the JSPCompilationEnvironment object
    *
    * @return   The uriRoot value
    */
   public String getUriRoot()
   {
      return uriRoot;
   }

   /**
    * Gets the poolingEnabled attribute of the JSPCompilationEnvironment object
    *
    * @return   The poolingEnabled value
    */
   public boolean isPoolingEnabled()
   {
      return false;
   }

   /**
    * Gets the smapDumped attribute of the JSPCompilationEnvironment object
    *
    * @return   The smapDumped value
    */
   public boolean isSmapDumped()
   {
      return false;
   }

   /**
    * Gets the smapSuppressed attribute of the JSPCompilationEnvironment object
    *
    * @return   The smapSuppressed value
    */
   public boolean isSmapSuppressed()
   {
      return false;
   }

   /**
    * Gets the xpoweredBy attribute of the JSPCompilationEnvironment object
    *
    * @return   The xpoweredBy value
    */
   public boolean isXpoweredBy()
   {
      return false;
   }

   /**
    * Sets the context attribute of the JSPCompilationEnvironment object
    *
    * @param context  The new context value
    */
   public void setContext(ServletContext context)
   {
      this.context = context;
   }

   /**
    * Sets the errDispatcher attribute of the JSPCompilationEnvironment object
    *
    * @param errDispatcher  The new errDispatcher value
    */
   public void setErrDispatcher(ErrorDispatcher errDispatcher)
   {
      this.errDispatcher = errDispatcher;
   }

   /**
    * Sets the javaEncoding attribute of the JSPCompilationEnvironment object
    *
    * @param javaEncoding  The new javaEncoding value
    */
   public void setJavaEncoding(String javaEncoding)
   {
      this.javaEncoding = javaEncoding;
   }

   /**
    * Sets the jspConfig attribute of the JSPCompilationEnvironment object
    *
    * @param jspConfig  The new jspConfig value
    */
   public void setJspConfig(JspConfig jspConfig)
   {
      this.jspConfig = jspConfig;
   }

   /**
    * Sets the runtimeContext attribute of the JSPCompilationEnvironment object
    *
    * @param rctxt  The new runtimeContext value
    */
   public void setRuntimeContext(JspRuntimeContext rctxt)
   {
      this.rctxt = rctxt;
   }

   /**
    * Sets the scratchDir attribute of the JSPCompilationEnvironment object
    *
    * @param scratchDir  The new scratchDir value
    */
   public void setScratchDir(File scratchDir)
   {
      this.scratchDir = scratchDir;
   }

   /**
    * Sets the tagPluginManager attribute of the JSPCompilationEnvironment object
    *
    * @param tagPluginManager  The new tagPluginManager value
    */
   public void setTagPluginManager(TagPluginManager tagPluginManager)
   {
      this.tagPluginManager = tagPluginManager;
   }

   /**
    * Sets the tldLocationsCache attribute of the JSPCompilationEnvironment object
    *
    * @param tldLocationsCache  The new tldLocationsCache value
    */
   public void setTldLocationsCache(TldLocationsCache tldLocationsCache)
   {
      this.tldLocationsCache = tldLocationsCache;
   }

   /**
    * Sets the uriRoot attribute of the JSPCompilationEnvironment object
    *
    * @param uriRoot  The new uriRoot value
    */
   public void setUriRoot(String uriRoot)
   {
      this.uriRoot = uriRoot;
   }

   /** Description of the Method */
   private void initServletContext()
   {
      try
      {
         context = new JspCServletContext(new PrintWriter(System.out), new File(uriRoot).toURL());
         tldLocationsCache = new TldLocationsCache(context, true);
      }
      catch (MalformedURLException me)
      {
         System.out.println("**" + me);//$NON-NLS-1$
      }
      rctxt = new JspRuntimeContext(context, this);
      jspConfig = new JspConfig(context);
      tagPluginManager = new TagPluginManager(context);
   }
}
