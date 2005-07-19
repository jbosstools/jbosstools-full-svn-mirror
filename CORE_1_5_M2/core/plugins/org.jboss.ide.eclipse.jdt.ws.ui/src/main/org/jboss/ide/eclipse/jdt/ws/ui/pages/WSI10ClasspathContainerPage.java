/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.ws.ui.pages;

import org.jboss.ide.eclipse.jdt.ui.pages.ClasspathContainerPage;
import org.jboss.ide.eclipse.jdt.ws.core.classpath.WSIBasicProfile10ClasspathContainer;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class WSI10ClasspathContainerPage extends ClasspathContainerPage
{
   /**Constructor for the WSI10ClasspathContainerPage object */
   public WSI10ClasspathContainerPage() { }


   /**
    * Gets the classpathContainerId attribute of the WSI10ClasspathContainerPage object
    *
    * @return   The classpathContainerId value
    */
   protected String getClasspathContainerId()
   {
      return WSIBasicProfile10ClasspathContainer.CLASSPATH_CONTAINER;
   }


   /**
    * Gets the classpathEntryDescription attribute of the WSI10ClasspathContainerPage object
    *
    * @return   The classpathEntryDescription value
    */
   protected String getClasspathEntryDescription()
   {
      return WSIBasicProfile10ClasspathContainer.DESCRIPTION;
   }
}
