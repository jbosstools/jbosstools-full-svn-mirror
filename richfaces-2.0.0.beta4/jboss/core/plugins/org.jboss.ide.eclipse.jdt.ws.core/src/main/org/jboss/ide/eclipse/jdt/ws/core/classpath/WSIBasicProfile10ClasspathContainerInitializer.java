/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.ws.core.classpath;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.jboss.ide.eclipse.jdt.core.classpath.AbstractClasspathContainer;
import org.jboss.ide.eclipse.jdt.core.classpath.AbstractClasspathContainerInitializer;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class WSIBasicProfile10ClasspathContainerInitializer extends AbstractClasspathContainerInitializer
{
   /**Constructor for the WSIBasicProfile10ClasspathContainerInitializer object */
   public WSIBasicProfile10ClasspathContainerInitializer()
   {
      super();
   }


   /**
    * Gets the description attribute of the WSIBasicProfile10ClasspathContainerInitializer object
    *
    * @param containerPath  Description of the Parameter
    * @param project        Description of the Parameter
    * @return               The description value
    */
   public String getDescription(IPath containerPath, IJavaProject project)
   {
      return "Web Services Classpath Container Initializer";//$NON-NLS-1$
   }


   /**
    * Description of the Method
    *
    * @param path  Description of the Parameter
    * @return      Description of the Return Value
    */
   protected AbstractClasspathContainer createClasspathContainer(IPath path)
   {
      return new WSIBasicProfile10ClasspathContainer(path);
   }


   /**
    * Gets the classpathContainerID attribute of the WSIBasicProfile10ClasspathContainerInitializer object
    *
    * @return   The classpathContainerID value
    */
   protected String getClasspathContainerID()
   {
      return WSIBasicProfile10ClasspathContainer.CLASSPATH_CONTAINER;
   }
}
