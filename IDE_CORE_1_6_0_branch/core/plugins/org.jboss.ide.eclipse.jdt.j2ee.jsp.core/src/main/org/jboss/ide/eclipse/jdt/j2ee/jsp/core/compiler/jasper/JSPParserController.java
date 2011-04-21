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
