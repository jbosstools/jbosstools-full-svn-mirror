/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.core.classpath;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.jboss.ide.eclipse.jdt.core.classpath.AbstractClasspathContainer;
import org.jboss.ide.eclipse.jdt.core.classpath.AbstractClasspathContainerInitializer;

/**
 * Classpath container initializer for the Jasper runtime classes.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JasperClasspathContainerInitializer extends AbstractClasspathContainerInitializer
{
   /**Constructor for the JasperClasspathContainerInitializer object */
   public JasperClasspathContainerInitializer()
   {
      super();
   }


   /**
    * Gets the description attribute of the JasperClasspathContainerInitializer object
    *
    * @param containerPath  Description of the Parameter
    * @param project        Description of the Parameter
    * @return               The description value
    */
   public String getDescription(IPath containerPath, IJavaProject project)
   {
      return "Jasper Classpath Container Initializer";//$NON-NLS-1$
   }


   /**
    * Description of the Method
    *
    * @param path  Description of the Parameter
    * @return      Description of the Return Value
    */
   protected AbstractClasspathContainer createClasspathContainer(IPath path)
   {
      return new JasperClasspathContainer(path);
   }


   /**
    * Gets the classpathContainerID attribute of the JasperClasspathContainerInitializer object
    *
    * @return   The classpathContainerID value
    */
   protected String getClasspathContainerID()
   {
      return JasperClasspathContainer.CLASSPATH_CONTAINER;
   }
}
