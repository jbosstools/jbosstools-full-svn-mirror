/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.test.ui.pages;

import org.jboss.ide.eclipse.jdt.test.core.classpath.JUnitClasspathContainer;
import org.jboss.ide.eclipse.jdt.ui.pages.ClasspathContainerPage;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JUnitClasspathContainerPage extends ClasspathContainerPage
{
   /**Constructor for the JUnitClasspathContainerPage object */
   public JUnitClasspathContainerPage()
   {
      super();
   }


   /**
    * Gets the classpathContainerId attribute of the JUnitClasspathContainerPage object
    *
    * @return   The classpathContainerId value
    */
   protected String getClasspathContainerId()
   {
      return JUnitClasspathContainer.CLASSPATH_CONTAINER;
   }


   /**
    * Gets the classpathEntryDescription attribute of the JUnitClasspathContainerPage object
    *
    * @return   The classpathEntryDescription value
    */
   protected String getClasspathEntryDescription()
   {
      return JUnitClasspathContainer.DESCRIPTION;
   }
}
