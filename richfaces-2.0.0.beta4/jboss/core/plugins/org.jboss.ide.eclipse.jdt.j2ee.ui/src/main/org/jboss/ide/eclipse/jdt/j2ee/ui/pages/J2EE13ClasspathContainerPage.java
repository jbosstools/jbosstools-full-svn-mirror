/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.ui.pages;

import org.jboss.ide.eclipse.jdt.j2ee.core.classpath.J2EE13ClasspathContainer;
import org.jboss.ide.eclipse.jdt.ui.pages.ClasspathContainerPage;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class J2EE13ClasspathContainerPage extends ClasspathContainerPage
{
   /**Constructor for the J2EE13ClasspathContainerPage object */
   public J2EE13ClasspathContainerPage() { }


   /**
    * Gets the classpathContainerId attribute of the J2EE13ClasspathContainerPage object
    *
    * @return   The classpathContainerId value
    */
   protected String getClasspathContainerId()
   {
      return J2EE13ClasspathContainer.CLASSPATH_CONTAINER;
   }


   /**
    * Gets the classpathEntryDescription attribute of the J2EE13ClasspathContainerPage object
    *
    * @return   The classpathEntryDescription value
    */
   protected String getClasspathEntryDescription()
   {
      return J2EE13ClasspathContainer.DESCRIPTION;
   }
}
