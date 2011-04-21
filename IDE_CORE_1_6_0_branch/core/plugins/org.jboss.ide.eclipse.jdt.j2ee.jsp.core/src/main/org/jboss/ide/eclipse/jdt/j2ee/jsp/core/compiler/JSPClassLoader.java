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
package org.jboss.ide.eclipse.jdt.j2ee.jsp.core.compiler;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

/**
 * ClassLoader that alter the delegation model.
 * Useful in an Eclipse environment to avoid the ClassCastException.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPClassLoader extends URLClassLoader
{

   /**
    *Constructor for the JSPClassLoader object
    *
    * @param urls  Description of the Parameter
    */
   public JSPClassLoader(URL[] urls)
   {
      super(urls);
   }

   /**
    *Constructor for the JSPClassLoader object
    *
    * @param urls    Description of the Parameter
    * @param parent  Description of the Parameter
    */
   public JSPClassLoader(URL[] urls, ClassLoader parent)
   {
      super(urls, parent);
   }

   /**
    *Constructor for the JSPClassLoader object
    *
    * @param urls     Description of the Parameter
    * @param parent   Description of the Parameter
    * @param factory  Description of the Parameter
    */
   public JSPClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory)
   {
      super(urls, parent, factory);
   }

   /**
    * Override to alter the delegation model.
    * Check parent first before the current ClassLoader.
    *
    * @param name                        Description of the Parameter
    * @return                            Description of the Return Value
    * @exception ClassNotFoundException  Description of the Exception
    */
   public Class loadClass(String name) throws ClassNotFoundException
   {
      try
      {
         return this.getParent().loadClass(name);
      }
      catch (ClassNotFoundException cnfe)
      {
      }
      return super.loadClass(name);
   }
}
