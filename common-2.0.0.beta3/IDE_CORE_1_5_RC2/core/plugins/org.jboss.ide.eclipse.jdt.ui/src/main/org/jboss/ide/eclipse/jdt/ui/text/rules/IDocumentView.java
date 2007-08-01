/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.ui.text.rules;

import org.eclipse.jface.text.IDocument;

/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * View to part of parent document. Provides methods for translating
 * character offsets between this view and parent document.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public interface IDocumentView extends IDocument
{
   /**
    * Gets the parentDocument attribute of the IDocumentView object
    *
    * @return   The parentDocument value
    */
   IDocument getParentDocument();


   /**
    * Gets the parentOffset attribute of the IDocumentView object
    *
    * @param localOffset  Description of the Parameter
    * @return             The parentOffset value
    */
   int getParentOffset(int localOffset);


   /**
    * Gets the localOffset attribute of the IDocumentView object
    *
    * @param parentOffset  Description of the Parameter
    * @return              The localOffset value
    */
   int getLocalOffset(int parentOffset);
}
