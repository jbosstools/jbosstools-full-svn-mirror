/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
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
   public Class loadClass(String name)
      throws ClassNotFoundException
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
