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
package org.jboss.ide.eclipse.jdt.ui.text.rules;

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
 * Inner view to parent document.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class InnerDocumentView extends AbstractDocument implements IDocumentView
{

   /** The parent document */
   IDocument parent;

   /** The section inside the parent document */
   ViewNode range;

   /**
    * Constructs inner view to parent document.
    *
    * @param parent  parent document
    * @param range
    */
   public InnerDocumentView(IDocument parent, ViewNode range)
   {
      this.parent = parent;
      this.range = range;

      setTextStore(new TextStore());
      setLineTracker(new DefaultLineTracker());
      getTracker().set(getStore().get(0, getLength()));
      completeInitialization();
   }

   /**
    * Gets the localOffset attribute of the InnerDocumentView object
    *
    * @param parentOffset  Description of the Parameter
    * @return              The localOffset value
    */
   public int getLocalOffset(int parentOffset)
   {
      return parentOffset - range.offset;
   }

   /**
    * Gets the parentDocument attribute of the InnerDocumentView object
    *
    * @return   The parentDocument value
    */
   public IDocument getParentDocument()
   {
      return parent;
   }

   /**
    * Gets the parentOffset attribute of the InnerDocumentView object
    *
    * @param localOffset  Description of the Parameter
    * @return             The parentOffset value
    */
   public int getParentOffset(int localOffset)
   {
      return localOffset + range.offset;
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
         try
         {
            return parent.get(range.offset + offset, length);
         }
         catch (BadLocationException x)
         {
         }

         return null;
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
            return parent.getChar(range.offset + offset);
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
         return range.length;
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
            parent.replace(range.offset + offset, length, txt);
         }
         catch (BadLocationException x)
         {
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
            parent.replace(range.offset, range.length, txt);
         }
         catch (BadLocationException x)
         {
         }
      }
   }
}
