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

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class IdentityTranslator implements IPositionTranslator
{
   /** Description of the Field */
   public static IdentityTranslator INSTANCE = new IdentityTranslator();


   /**Constructor for the IdentityTranslator object */
   public IdentityTranslator() { }


   /**
    * Gets the positions attribute of the IdentityTranslator object
    *
    * @param document                          Description of the Parameter
    * @param category                          Description of the Parameter
    * @return                                  The positions value
    * @exception BadPositionCategoryException  Description of the Exception
    */
   public Position[] getPositions(IDocument document, String category)
      throws BadPositionCategoryException
   {
      return document.getPositions(category);
   }


   /**
    * Description of the Method
    *
    * @param document  Description of the Parameter
    * @param offset    Description of the Parameter
    * @return          Description of the Return Value
    */
   public int translateParentOffset(IDocument document, int offset)
   {
      int translated = offset;
      return translated;
   }
}
