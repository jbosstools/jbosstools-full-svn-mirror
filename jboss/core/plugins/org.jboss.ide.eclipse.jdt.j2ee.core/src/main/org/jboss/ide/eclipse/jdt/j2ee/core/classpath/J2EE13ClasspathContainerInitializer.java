/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.core.classpath;

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
public class J2EE13ClasspathContainerInitializer extends AbstractClasspathContainerInitializer
{
   /**Constructor for the J2EE13ClasspathContainerInitializer object */
   public J2EE13ClasspathContainerInitializer()
   {
      super();
   }


   /**
    * Gets the description attribute of the J2EE13ClasspathContainerInitializer object
    *
    * @param containerPath  Description of the Parameter
    * @param project        Description of the Parameter
    * @return               The description value
    */
   public String getDescription(IPath containerPath, IJavaProject project)
   {
      return "J2EE 1.3 Classpath Container Initializer";//$NON-NLS-1$
   }


   /**
    * Description of the Method
    *
    * @param path  Description of the Parameter
    * @return      Description of the Return Value
    */
   protected AbstractClasspathContainer createClasspathContainer(IPath path)
   {
      return new J2EE13ClasspathContainer(path);
   }


   /**
    * Gets the classpathContainerID attribute of the J2EE13ClasspathContainerInitializer object
    *
    * @return   The classpathContainerID value
    */
   protected String getClasspathContainerID()
   {
      return J2EE13ClasspathContainer.CLASSPATH_CONTAINER;
   }
}
