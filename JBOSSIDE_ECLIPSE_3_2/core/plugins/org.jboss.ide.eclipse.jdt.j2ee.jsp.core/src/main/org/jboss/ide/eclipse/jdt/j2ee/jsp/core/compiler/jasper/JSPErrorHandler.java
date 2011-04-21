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
import org.apache.jasper.compiler.DefaultErrorHandler;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.core.compiler.JSPMarkerFactory;

/**
 * Extends the DefaultErrorHandler to capture JSP compilation problem
 * and generate marker to the Eclispe UI.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPErrorHandler extends DefaultErrorHandler
{

   /** The JSP informations */
   private JSPElementInfo info;

   /**
    * Constructor with the JSP informations
    *
    * @param info  Description of the Parameter
    */
   public JSPErrorHandler(JSPElementInfo info)
   {
      this.info = info;
   }

   /**
    * Callback method called by the Jasper compiler
    *
    * @param errMsg               Description of the Parameter
    * @param ex                   Description of the Parameter
    * @exception JasperException  Description of the Exception
    */
   public void jspError(String errMsg, Exception ex) throws JasperException
   {
      try
      {
         // Create a new marker
         IMarker marker = JSPMarkerFactory.createMarker(this.info.getFile());
         marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
         marker.setAttribute(IMarker.MESSAGE, errMsg);
      }
      catch (CoreException e)
      {
         throw new JasperException(e);
      }
   }

   /**
    * Callback method called by the Jasper compiler
    *
    * @param fname                Description of the Parameter
    * @param line                 Description of the Parameter
    * @param column               Description of the Parameter
    * @param errMsg               Description of the Parameter
    * @param ex                   Description of the Parameter
    * @exception JasperException  Description of the Exception
    */
   public void jspError(String fname, int line, int column, String errMsg, Exception ex) throws JasperException
   {
      try
      {
         // Create a new marker
         IMarker marker = JSPMarkerFactory.createMarker(this.info.getFile());
         marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
         marker.setAttribute(IMarker.MESSAGE, errMsg);
         marker.setAttribute(IMarker.LINE_NUMBER, line);
      }
      catch (CoreException e)
      {
         throw new JasperException(e);
      }
   }
}
