/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.pages;

import org.jboss.ide.eclipse.jdt.j2ee.jsp.core.classpath.JasperClasspathContainer;
import org.jboss.ide.eclipse.jdt.ui.pages.ClasspathContainerPage;

/**
 * Description of the Class
 *
 * @author       Laurent Etiemble
 * @version      $Revision$
 * @deprecated
 */
public class JasperClasspathContainerPage extends ClasspathContainerPage
{
   /**Constructor for the JasperClasspathContainerPage object */
   public JasperClasspathContainerPage() { }


   /**
    * Gets the classpathContainerId attribute of the JasperClasspathContainerPage object
    *
    * @return   The classpathContainerId value
    */
   protected String getClasspathContainerId()
   {
      return JasperClasspathContainer.CLASSPATH_CONTAINER;
   }


   /**
    * Gets the classpathEntryDescription attribute of the JasperClasspathContainerPage object
    *
    * @return   The classpathEntryDescription value
    */
   protected String getClasspathEntryDescription()
   {
      return JasperClasspathContainer.DESCRIPTION;
   }
}
