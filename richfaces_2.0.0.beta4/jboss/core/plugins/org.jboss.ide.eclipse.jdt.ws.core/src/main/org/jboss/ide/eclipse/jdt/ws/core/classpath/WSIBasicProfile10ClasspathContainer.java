/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.ws.core.classpath;

import org.eclipse.core.runtime.IPath;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.jdt.core.classpath.AbstractClasspathContainer;
import org.jboss.ide.eclipse.jdt.core.classpath.ClassPathConstants;
import org.jboss.ide.eclipse.jdt.ws.core.JDTWSCoreMessages;
import org.jboss.ide.eclipse.jdt.ws.core.JDTWSCorePlugin;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class WSIBasicProfile10ClasspathContainer extends AbstractClasspathContainer
{
   /** Description of the Field */
   public final static String CLASSPATH_CONTAINER = ClassPathConstants.CLASSPATH_CONTAINER_PREFIX + "." + WSIBasicProfile10ClasspathContainer.SUFFIX;//$NON-NLS-1$
   /** Description of the Field */
   public final static String DESCRIPTION = JDTWSCoreMessages.getString(CLASSPATH_CONTAINER);
   /** Description of the Field */
   public final static String SUFFIX = "wsi-1.0";//$NON-NLS-1$


   /**
    *Constructor for the WSIBasicProfile10ClasspathContainer object
    *
    * @param path  Description of the Parameter
    */
   public WSIBasicProfile10ClasspathContainer(IPath path)
   {
      super(path);
   }


   /**
    * Gets the description attribute of the WSIBasicProfile10ClasspathContainer object
    *
    * @return   The description value
    */
   public String getDescription()
   {
      return DESCRIPTION;
   }


   /**
    * Gets the libFolder attribute of the WSIBasicProfile10ClasspathContainer object
    *
    * @return   The libFolder value
    */
   protected String getLibFolder()
   {
      return SUFFIX;
   }


   /**
    * Gets the plugin attribute of the WSIBasicProfile10ClasspathContainer object
    *
    * @return   The plugin value
    */
   protected AbstractPlugin getPlugin()
   {
      return JDTWSCorePlugin.getDefault();
   }
}
