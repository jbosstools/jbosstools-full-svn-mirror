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
public class SimpleDoubleClickStrategy extends TextDoubleClickStrategy
{

   /**
    * Description of the Method
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

         if (offset == start)
         {
            viewer.setSelectedRange(start, length);
            return;
         }

         int end = start + length - 1;

         if (offset == end && document.getChar(end) == '>')
         {
            viewer.setSelectedRange(start, length);
            return;
         }

         super.doubleClicked(viewer);
      }
      catch (BadLocationException e)
      {
      }
   }
}
