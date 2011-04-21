/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.ui.text.rules;

import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;

/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public interface IPositionTranslator
{
   /**
    * Gets the positions attribute of the IPositionTranslator object
    *
    * @param document                          Description of the Parameter
    * @param category                          Description of the Parameter
    * @return                                  The positions value
    * @exception BadPositionCategoryException  Description of the Exception
    */
   public abstract Position[] getPositions(IDocument document, String category)
      throws BadPositionCategoryException;


   /**
    * Description of the Method
    *
    * @param document  Description of the Parameter
    * @param offset    Description of the Parameter
    * @return          Description of the Return Value
    */
   public abstract int translateParentOffset(IDocument document, int offset);
}
