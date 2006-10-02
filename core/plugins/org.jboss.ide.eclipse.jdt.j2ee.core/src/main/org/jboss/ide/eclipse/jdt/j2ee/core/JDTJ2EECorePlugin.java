/*
 * JBoss, a division of Red Hat
 * Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
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
package org.jboss.ide.eclipse.jdt.j2ee.core;

import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.jdt.core.classpath.ClassPathContainerRepository;
import org.jboss.ide.eclipse.jdt.j2ee.core.classpath.J2EE13ClasspathContainer;
import org.jboss.ide.eclipse.jdt.j2ee.core.classpath.J2EE14ClasspathContainer;
import org.jboss.ide.eclipse.jdt.test.core.JDTTestCorePlugin;
import org.jboss.ide.eclipse.jdt.ws.core.JDTWSCorePlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JDTJ2EECorePlugin extends AbstractPlugin
{

   public static final String WST_VALIDATION_BUILDER_ID = "org.eclipse.wst.validation.validationbuilder";

   /** The shared instance */
   private static JDTJ2EECorePlugin plugin;

   /** The constructor. */
   public JDTJ2EECorePlugin()
   {
      super();
      plugin = this;
   }

   /**
    * Description of the Method
    *
    * @param context        Description of the Parameter
    * @exception Exception  Description of the Exception
    */
   public void start(BundleContext context) throws Exception
   {
      super.start(context);

      ClassPathContainerRepository.getInstance().addClassPathEntry(J2EE13ClasspathContainer.CLASSPATH_CONTAINER);
      ClassPathContainerRepository.getInstance().addClassPathEntry(J2EE14ClasspathContainer.CLASSPATH_CONTAINER);

      // Force the plugin load
      JDTTestCorePlugin.getDefault().getBaseDir();
      JDTWSCorePlugin.getDefault().getBaseDir();
   }

   /**
    * Returns the shared instance.
    *
    * @return   The default value
    */
   public static JDTJ2EECorePlugin getDefault()
   {
      return plugin;
   }

   /**
    * Convenience method which returns the unique identifier of this plugin.
    *
    * @return   The unique indentifier value
    */
   public static String getUniqueIdentifier()
   {
      if (getDefault() == null)
      {
         // If the default instance is not yet initialized,
         // return a static identifier. This identifier must
         // match the plugin id defined in plugin.xml
         return "org.jboss.ide.eclipse.jdt.j2ee.core";//$NON-NLS-1$
      }
      return getDefault().getBundle().getSymbolicName();
   }
}
