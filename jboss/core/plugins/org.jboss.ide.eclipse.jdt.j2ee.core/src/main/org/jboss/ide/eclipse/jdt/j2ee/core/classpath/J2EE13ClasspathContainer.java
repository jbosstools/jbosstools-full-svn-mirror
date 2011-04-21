/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.core.classpath;

import org.eclipse.core.runtime.IPath;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.jdt.core.classpath.AbstractClasspathContainer;
import org.jboss.ide.eclipse.jdt.core.classpath.ClassPathConstants;
import org.jboss.ide.eclipse.jdt.j2ee.core.JDTJ2EECoreMessages;
import org.jboss.ide.eclipse.jdt.j2ee.core.JDTJ2EECorePlugin;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class J2EE13ClasspathContainer extends AbstractClasspathContainer
{
   /** Description of the Field */
   public final static String CLASSPATH_CONTAINER = ClassPathConstants.CLASSPATH_CONTAINER_PREFIX + "." + J2EE13ClasspathContainer.SUFFIX;//$NON-NLS-1$
   /** Description of the Field */
   public final static String DESCRIPTION = JDTJ2EECoreMessages.getString(CLASSPATH_CONTAINER);
   /** Description of the Field */
   public final static String SUFFIX = "j2ee-1.3";//$NON-NLS-1$


   /**
    *Constructor for the J2EE13ClasspathContainer object
    *
    * @param path  Description of the Parameter
    */
   public J2EE13ClasspathContainer(IPath path)
   {
      super(path);
   }


   /**
    * Gets the description attribute of the J2EE13ClasspathContainer object
    *
    * @return   The description value
    */
   public String getDescription()
   {
      return DESCRIPTION;
   }


   /**
    * Gets the libFolder attribute of the J2EE13ClasspathContainer object
    *
    * @return   The libFolder value
    */
   protected String getLibFolder()
   {
      return SUFFIX;
   }


   /**
    * Gets the plugin attribute of the J2EE13ClasspathContainer object
    *
    * @return   The plugin value
    */
   protected AbstractPlugin getPlugin()
   {
      return JDTJ2EECorePlugin.getDefault();
   }
}
