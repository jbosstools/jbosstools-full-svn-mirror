/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.ui.text.rules;

import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.Position;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class MultiViewTranslator implements IPositionTranslator
{
   /** Description of the Field */
   public static MultiViewTranslator INSTANCE = new MultiViewTranslator();


   /**Constructor for the MultiViewTranslator object */
   public MultiViewTranslator() { }


   /**
    * Gets the positions attribute of the MultiViewTranslator object
    *
    * @param document                          Description of the Parameter
    * @param category                          Description of the Parameter
    * @return                                  The positions value
    * @exception BadPositionCategoryException  Description of the Exception
    */
   public Position[] getPositions(IDocument document, String category)
      throws BadPositionCategoryException
   {
      IDocumentPartitioner partitioner = document.getDocumentPartitioner();
      if (partitioner instanceof MultiViewPartitioner)
      {
         MultiViewPartitioner mvPartitioner = (MultiViewPartitioner) partitioner;
         OuterDocumentView odocument = mvPartitioner.getOuterDocument();
         if (odocument != null)
         {
            Position[] positions = odocument.getPositions(category);
            for (int i = 0; i < positions.length; i++)
            {
               Position pos = positions[i];
               int offset = odocument.getParentOffset(pos.getOffset());
               //pos.setOffset(offset);
            }
            return positions;
         }
      }
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
      IDocumentPartitioner partitioner = document.getDocumentPartitioner();
      if (partitioner instanceof MultiViewPartitioner)
      {
         MultiViewPartitioner mvPartitioner = (MultiViewPartitioner) partitioner;
         OuterDocumentView odocument = mvPartitioner.getOuterDocument();
         if (odocument != null)
         {
            translated = odocument.getLocalOffset(offset);
         }
      }
      return translated;
   }
}
