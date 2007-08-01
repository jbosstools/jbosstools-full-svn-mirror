/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.ws.ui.pages;

import org.jboss.ide.eclipse.jdt.ui.pages.ClasspathContainerPage;
import org.jboss.ide.eclipse.jdt.ws.core.classpath.Axis12ClasspathContainer;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class Axis12ClasspathContainerPage extends ClasspathContainerPage
{
   /**Constructor for the Axis12ClasspathContainerPage object */
   public Axis12ClasspathContainerPage()
   {
      super();
   }


   /**
    * Gets the classpathContainerId attribute of the Axis12ClasspathContainerPage object
    *
    * @return   The classpathContainerId value
    */
   protected String getClasspathContainerId()
   {
      return Axis12ClasspathContainer.CLASSPATH_CONTAINER;
   }


   /**
    * Gets the classpathEntryDescription attribute of the Axis12ClasspathContainerPage object
    *
    * @return   The classpathEntryDescription value
    */
   protected String getClasspathEntryDescription()
   {
      return Axis12ClasspathContainer.DESCRIPTION;
   }
}
