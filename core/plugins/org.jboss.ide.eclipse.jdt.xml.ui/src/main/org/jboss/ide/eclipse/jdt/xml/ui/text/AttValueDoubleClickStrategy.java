/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
