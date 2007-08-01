/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.launcher.core.logfiles;

import java.io.File;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   18 mai 2003
 */
public interface ILogFileListener
{
   /**
    * Description of the Method
    *
    * @param file  Description of the Parameter
    */
   public void fileChanged(File file);
}
