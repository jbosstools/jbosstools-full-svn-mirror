/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.xml.ui.text;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITypedRegion;
import org.jboss.ide.eclipse.jdt.ui.text.TextDoubleClickStrategy;

/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class AttValueDoubleClickStrategy extends TextDoubleClickStrategy
{

   /**
    * Select the attribute region
    *
    * @param viewer  Description of the Parameter
    */
   public void doubleClicked(ITextViewer viewer)
   {
      int offset = viewer.getSelectedRange().x;
      if (offset < 0)
      {
         return;
      }

      try
      {
         IDocument document = viewer.getDocument();
         ITypedRegion region = document.getPartition(offset);

         int start = region.getOffset();
         int length = region.getLength();
         int end = start + length - 1;

         if (offset == start)
         {
            if (document.getChar(start) == document.getChar(end))
            {
               viewer.setSelectedRange(start + 1, length - 2);
            }
            else
            {
               viewer.setSelectedRange(start + 1, length - 1);
            }
            return;
         }

         if (offset == end)
         {
            if (document.getChar(start) == document.getChar(end))
            {
               viewer.setSelectedRange(start + 1, length - 2);
               return;
            }
         }
         super.doubleClicked(viewer);
      }
      catch (BadLocationException e)
      {
         // Do nothing
      }
   }

}
