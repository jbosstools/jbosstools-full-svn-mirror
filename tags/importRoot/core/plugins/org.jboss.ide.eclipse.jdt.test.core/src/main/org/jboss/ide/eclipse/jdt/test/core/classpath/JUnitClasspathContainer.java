/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.test.core.classpath;

import org.eclipse.core.runtime.IPath;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.jdt.core.classpath.AbstractClasspathContainer;
import org.jboss.ide.eclipse.jdt.core.classpath.ClassPathConstants;
import org.jboss.ide.eclipse.jdt.test.core.JDTTestCoreMessages;
import org.jboss.ide.eclipse.jdt.test.core.JDTTestCorePlugin;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JUnitClasspathContainer extends AbstractClasspathContainer
{
   /** Description of the Field */
   public final static String CLASSPATH_CONTAINER = ClassPathConstants.CLASSPATH_CONTAINER_PREFIX + "." + JUnitClasspathContainer.SUFFIX;//$NON-NLS-1$
   /** Description of the Field */
   public final static String DESCRIPTION = JDTTestCoreMessages.getString(CLASSPATH_CONTAINER);
   /** Description of the Field */
   public final static String SUFFIX = "junit-3.8.1";//$NON-NLS-1$


   /**
    *Constructor for the JUnitClasspathContainer object
    *
    * @param path  Description of the Parameter
    */
   public JUnitClasspathContainer(IPath path)
   {
      super(path);
   }


   /**
    * Gets the description attribute of the JUnitClasspathContainer object
    *
    * @return   The description value
    */
   public String getDescription()
   {
      return DESCRIPTION;
   }


   /**
    * Gets the libFolder attribute of the JUnitClasspathContainer object
    *
    * @return   The libFolder value
    */
   protected String getLibFolder()
   {
      return SUFFIX;
   }


   /**
    * Gets the plugin attribute of the JUnitClasspathContainer object
    *
    * @return   The plugin value
    */
   protected AbstractPlugin getPlugin()
   {
      return JDTTestCorePlugin.getDefault();
   }
}
