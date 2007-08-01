/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.core.classpath;

import org.eclipse.core.runtime.IPath;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.jdt.core.classpath.AbstractClasspathContainer;
import org.jboss.ide.eclipse.jdt.core.classpath.ClassPathConstants;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.core.JDTJ2EEJSPCoreMessages;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.core.JDTJ2EEJSPCorePlugin;

/**
 * Classpath container for the Jasper classes.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JasperClasspathContainer extends AbstractClasspathContainer
{
   /** Description of the Field */
   public final static String CLASSPATH_CONTAINER = ClassPathConstants.CLASSPATH_CONTAINER_PREFIX + "." + JasperClasspathContainer.SUFFIX;//$NON-NLS-1$
   /** Description of the Field */
   public final static String DESCRIPTION = JDTJ2EEJSPCoreMessages.getString(CLASSPATH_CONTAINER);
   /** Description of the Field */
   public final static String SUFFIX = "jasper-5.0.27";//$NON-NLS-1$


   /**
    *Constructor for the JasperClasspathContainer object
    *
    * @param path  Description of the Parameter
    */
   public JasperClasspathContainer(IPath path)
   {
      super(path);
   }


   /**
    * Gets the description attribute of the JasperClasspathContainer object
    *
    * @return   The description value
    */
   public String getDescription()
   {
      return DESCRIPTION;
   }


   /**
    * Gets the libFolder attribute of the JasperClasspathContainer object
    *
    * @return   The libFolder value
    */
   protected String getLibFolder()
   {
      return SUFFIX;
   }


   /**
    * Gets the plugin attribute of the JasperClasspathContainer object
    *
    * @return   The plugin value
    */
   protected AbstractPlugin getPlugin()
   {
      return JDTJ2EEJSPCorePlugin.getDefault();
   }
}
