/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.launcher.core.logfiles;

import org.eclipse.jface.text.IDocument;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   18 mai 2003
 */
public interface ILogFileDocumentListener
{
   /**
    * Description of the Method
    *
    * @param document  Description of the Parameter
    */
   public void changedContent(IDocument document);
}
