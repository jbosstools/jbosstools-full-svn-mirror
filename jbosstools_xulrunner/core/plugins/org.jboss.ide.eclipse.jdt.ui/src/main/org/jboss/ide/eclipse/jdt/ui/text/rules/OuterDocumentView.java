/*
 * JBoss, a division of Red Hat
 * Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
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
package org.jboss.ide.eclipse.jdt.ui.text.rules;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.AbstractDocument;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultLineTracker;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextStore;

/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * Outer view to parent document.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class OuterDocumentView extends AbstractDocument implements IDocumentView
{

   /** The parent document */
   IDocument parent;

   /** The section inside the parent document */
   List ranges;

   /**
    * Constructs outer view to parent document.
    *
    * @param parent  parent document
    * @param ranges  Description of the Parameter
    */
   public OuterDocumentView(IDocument parent, List ranges)
   {
      this.parent = parent;
      this.ranges = ranges;

      setTextStore(new TextStore());
      setLineTracker(new DefaultLineTracker());
      getTracker().set(getStore().get(0, getLength()));

      completeInitialization();
   }

   /**
    * Gets the localOffset attribute of the OuterDocumentView object
    *
    * @param parentOffset  Description of the Parameter
    * @return              The localOffset value
    */
   public int getLocalOffset(int parentOffset)
   {
      int localOffset = parentOffset;

      Iterator i = ranges.iterator();
      while (i.hasNext())
      {
         FlatNode range = (FlatNode) i.next();

         if (parentOffset <= range.offset)
         {
            break;
         }

         if (parentOffset <= range.offset + range.length)
         {
            localOffset -= parentOffset - range.offset;
            break;
         }

         localOffset -= range.length;
      }

      return localOffset;
   }

   /**
    * Gets the parentDocument attribute of the OuterDocumentView object
    *
    * @return   The parentDocument value
    */
   public IDocument getParentDocument()
   {
      return parent;
   }

   /**
    * Gets the parentOffset attribute of the OuterDocumentView object
    *
    * @param localOffset  Description of the Parameter
    * @return             The parentOffset value
    */
   public int getParentOffset(int localOffset)
   {
      int offset = localOffset;

      Iterator i = ranges.iterator();
      while (i.hasNext())
      {
         FlatNode range = (FlatNode) i.next();

         if (offset < range.offset)
         {
            break;
         }

         offset += range.length;
      }

      return offset;
   }

   /**
    * Description of the Method
    *
    * @param event  Description of the Parameter
    */
   protected void fireDocumentAboutToBeChanged(DocumentEvent event)
   {
      super.fireDocumentAboutToBeChanged(event);
   }

   /**
    * Description of the Method
    *
    * @param event  Description of the Parameter
    */
   protected void fireDocumentChanged(DocumentEvent event)
   {
      try
      {
         // TODO: move to a better place
         getTracker().replace(event.getOffset(), event.getLength(), event.getText());
      }
      catch (BadLocationException x)
      {
      }

      super.fireDocumentChanged(event);
   }

   /**
    * Implements ITextStore based on IDocument.
    *
    * @author    Laurent Etiemble
    * @version   $Revision$
    */
   class TextStore implements ITextStore
   {

      /*
       * @see ITextStore#get
       */
      /**
       * Description of the Method
       *
       * @param offset  Description of the Parameter
       * @param length  Description of the Parameter
       * @return        Description of the Return Value
       */
      public String get(int offset, int length)
      {
         StringBuffer buf = new StringBuffer(length);

         try
         {
            FlatNode range = null;

            Iterator i = ranges.iterator();
            while (i.hasNext())
            {
               range = (FlatNode) i.next();

               if (offset < range.offset)
               {
                  break;
               }

               offset += range.length;

               range = null;
            }

            int remainder = length - buf.length();
            while (remainder > 0)
            {
               if (range == null || offset + remainder < range.offset)
               {
                  buf.append(parent.get(offset, remainder));
                  break;
               }

               buf.append(parent.get(offset, range.offset - offset));
               offset = range.offset + range.length;
               range = i.hasNext() ? (FlatNode) i.next() : null;

               remainder = length - buf.length();
            }
         }
         catch (BadLocationException x)
         {
            return null;
         }

         return buf.toString();
      }

      /*
       * @see ITextStore#get
       */
      /**
       * Description of the Method
       *
       * @param offset  Description of the Parameter
       * @return        Description of the Return Value
       */
      public char get(int offset)
      {
         try
         {
            return parent.getChar(getParentOffset(offset));
         }
         catch (BadLocationException x)
         {
         }

         return (char) 0;
      }

      /*
       * @see ITextStore#getLength
       */
      /**
       * Gets the length attribute of the TextStore object
       *
       * @return   The length value
       */
      public int getLength()
      {
         int length = parent.getLength();

         Iterator i = ranges.iterator();
         while (i.hasNext())
         {
            length -= ((FlatNode) i.next()).length;
         }

         return length;
      }

      /*
       * @see ITextStore#replace
       */
      /**
       * Description of the Method
       *
       * @param offset  Description of the Parameter
       * @param length  Description of the Parameter
       * @param txt     Description of the Parameter
       */
      public void replace(int offset, int length, String txt)
      {
         try
         {
            int start = getParentOffset(offset);
            int end = getParentOffset(offset + length - 1) + 1;

            parent.replace(start, end - start, txt);
         }
         catch (BadLocationException x)
         {
            // ignored as surrounding document should have handled this
         }
      }

      /*
       * @see ITextStore#set
       */
      /**
       * Description of the Method
       *
       * @param txt  Description of the Parameter
       */
      public void set(String txt)
      {
         try
         {
            parent.replace(0, parent.getLength(), txt);
         }
         catch (BadLocationException x)
         {
            // cannot happen
         }
      }
   }
}
