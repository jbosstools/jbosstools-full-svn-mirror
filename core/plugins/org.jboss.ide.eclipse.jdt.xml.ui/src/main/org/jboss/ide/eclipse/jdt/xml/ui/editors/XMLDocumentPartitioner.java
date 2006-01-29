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
package org.jboss.ide.eclipse.jdt.xml.ui.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.Assert;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.DefaultPositionUpdater;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.IDocumentPartitionerExtension;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.TypedPosition;
import org.eclipse.jface.text.TypedRegion;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.jface.text.rules.IToken;
import org.jboss.ide.eclipse.jdt.xml.ui.reconciler.XMLNode;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class XMLDocumentPartitioner implements IDocumentPartitioner, IDocumentPartitionerExtension
{
   /** Description of the Field */
   protected int fDeleteOffset;

   /** Description of the Field */
   protected IDocument fDocument;

   /** Description of the Field */
   protected int fEndOffset;

   /** Description of the Field */
   protected String fLegalContentTypes[];

   /** Description of the Field */
   protected DefaultPositionUpdater fPositionUpdater;

   /** Description of the Field */
   protected int fPreviousDocumentLength;//$NON-NLS-1$

   /** Description of the Field */
   protected IPartitionTokenScanner fScanner;

   /** Description of the Field */
   protected int fStartOffset;

   private String fPositionCategory;

   /** Description of the Field */
   public final static String CONTENT_TYPES_CATEGORY = "__xml_content_types_category";//$NON-NLS-1$

   /**
    *Constructor for the XMLDocumentPartitioner object
    *
    * @param scanner            Description of the Parameter
    * @param legalContentTypes  Description of the Parameter
    */
   public XMLDocumentPartitioner(IPartitionTokenScanner scanner, String legalContentTypes[])
   {
      fScanner = scanner;
      fLegalContentTypes = legalContentTypes;
      fPositionCategory = CONTENT_TYPES_CATEGORY;
      fPositionUpdater = new DefaultPositionUpdater(fPositionCategory);
   }

   /**
    * Description of the Method
    *
    * @param offset  Description of the Parameter
    * @param length  Description of the Parameter
    * @return        Description of the Return Value
    */
   public ITypedRegion[] computePartitioning(int offset, int length)
   {
      List list = new ArrayList();
      try
      {
         int endOffset = offset + length;
         Position category[] = fDocument.getPositions(this.getPositionCategory());
         TypedPosition previous = null;
         TypedPosition current = null;
         Position gap = null;
         for (int i = 0; i < category.length; i++)
         {
            current = (TypedPosition) category[i];
            if (current.isDeleted())
            {
               new Exception("w" + current).printStackTrace();//$NON-NLS-1$
            }
            int gapOffset = previous == null ? 0 : previous.getOffset() + previous.getLength();
            gap = new Position(gapOffset, current.getOffset() - gapOffset);
            if (gap.getLength() > 0 && gap.overlapsWith(offset, length))
            {
               int start = Math.max(offset, gapOffset);
               int end = Math.min(endOffset, gap.getOffset() + gap.getLength());
               list.add(new TypedRegion(start, end - start, IDocument.DEFAULT_CONTENT_TYPE));
            }
            if (current.overlapsWith(offset, length))
            {
               int start = Math.max(offset, current.getOffset());
               int end = Math.min(endOffset, current.getOffset() + current.getLength());
               list.add(new TypedRegion(start, end - start, current.getType()));
            }
            previous = current;
         }

         if (previous != null)
         {
            int gapOffset = previous.getOffset() + previous.getLength();
            gap = new Position(gapOffset, fDocument.getLength() - gapOffset);
            if (gap.getLength() > 0 && gap.overlapsWith(offset, length))
            {
               int start = Math.max(offset, gapOffset);
               int end = Math.min(endOffset, fDocument.getLength());
               list.add(new TypedRegion(start, end - start, IDocument.DEFAULT_CONTENT_TYPE));
            }
         }
         if (list.isEmpty())
         {
            list.add(new TypedRegion(offset, length, IDocument.DEFAULT_CONTENT_TYPE));
         }
      }
      catch (BadPositionCategoryException _ex)
      {
      }
      TypedRegion result[] = new TypedRegion[list.size()];
      list.toArray(result);
      return result;
   }

   /**
    * Description of the Method
    *
    * @param document  Description of the Parameter
    */
   public void connect(IDocument document)
   {
      Assert.isNotNull(document);
      Assert.isTrue(!document.containsPositionCategory(this.getPositionCategory()));
      fDocument = document;
      fDocument.addPositionCategory(this.getPositionCategory());
      initialize();
   }

   /** Description of the Method */
   public void disconnect()
   {
      Assert.isTrue(fDocument.containsPositionCategory(this.getPositionCategory()));
      try
      {
         fDocument.removePositionCategory(this.getPositionCategory());
      }
      catch (BadPositionCategoryException _ex)
      {
      }
   }

   /**
    * Description of the Method
    *
    * @param e  Description of the Parameter
    */
   public void documentAboutToBeChanged(DocumentEvent e)
   {
      Assert.isTrue(e.getDocument() == fDocument);
      fPreviousDocumentLength = e.getDocument().getLength();
      fStartOffset = -1;
      fEndOffset = -1;
      fDeleteOffset = -1;
   }

   /**
    * Description of the Method
    *
    * @param e  Description of the Parameter
    * @return   Description of the Return Value
    */
   public boolean documentChanged(DocumentEvent e)
   {
      IRegion region = documentChanged2(e);
      return region != null;
   }

   /**
    * Description of the Method
    *
    * @param e  Description of the Parameter
    * @return   Description of the Return Value
    */
   public IRegion documentChanged2(DocumentEvent e)
   {
      try
      {
         IDocument d = e.getDocument();
         Position category[] = d.getPositions(this.getPositionCategory());
         IRegion line = d.getLineInformationOfOffset(e.getOffset());
         int reparseStart = line.getOffset();
         int partitionStart = -1;
         String contentType = null;
         int first = d.computeIndexInCategory(this.getPositionCategory(), reparseStart);
         if (first > 0)
         {
            TypedPosition partition = (TypedPosition) category[first - 1];
            if (partition.includes(reparseStart))
            {
               partitionStart = partition.getOffset();
               contentType = partition.getType();
               if (e.getOffset() == partition.getOffset() + partition.getLength())
               {
                  reparseStart = partitionStart;
               }
               first--;
            }
            else if (reparseStart == e.getOffset() && reparseStart == partition.getOffset() + partition.getLength())
            {
               partitionStart = partition.getOffset();
               contentType = partition.getType();
               reparseStart = partitionStart;
               first--;
            }
            else
            {
               partitionStart = partition.getOffset() + partition.getLength();
               contentType = IDocument.DEFAULT_CONTENT_TYPE;
            }
         }
         fPositionUpdater.update(e);
         for (int i = first; i < category.length; i++)
         {
            Position p = category[i];
            if (!p.isDeleted)
            {
               continue;
            }
            rememberDeletedOffset(e.getOffset());
            d.removePosition(this.getPositionCategory(), p);
            //               break;
         }

         category = d.getPositions(this.getPositionCategory());
         fScanner.setPartialRange(d, reparseStart, d.getLength() - reparseStart, contentType, partitionStart);
         int lastScannedPosition = reparseStart;
         for (IToken token = fScanner.nextToken(); !token.isEOF();)
         {
            contentType = getTokenContentType(token);
            if (!isSupportedContentType(contentType))
            {
               token = fScanner.nextToken();
            }
            else
            {
               int start = fScanner.getTokenOffset();
               int length = fScanner.getTokenLength();
               lastScannedPosition = (start + length) - 1;
               for (; first < category.length; first++)
               {
                  TypedPosition p = (TypedPosition) category[first];
                  if (lastScannedPosition < ((Position) (p)).offset + ((Position) (p)).length
                        && (!p.overlapsWith(start, length) || d.containsPosition(this.getPositionCategory(), start,
                              length)
                              && contentType.equals(p.getType())))
                  {
                     break;
                  }
                  rememberRegion(((Position) (p)).offset, ((Position) (p)).length);
                  p.delete();
                  d.removePosition(this.getPositionCategory(), p);
               }

               if (d.containsPosition(this.getPositionCategory(), start, length))
               {
                  if (lastScannedPosition > e.getOffset())
                  {
                     return createRegion();
                  }
                  first++;
               }
               else
               {
                  try
                  {
                     d.addPosition(this.getPositionCategory(), new XMLNode(start, length, contentType, fDocument));
                     rememberRegion(start, length);
                  }
                  catch (BadPositionCategoryException _ex)
                  {
                  }
                  catch (BadLocationException _ex)
                  {
                  }
               }
               token = fScanner.nextToken();
            }
         }

         if (lastScannedPosition != reparseStart)
         {
            lastScannedPosition++;
         }
         for (first = d.computeIndexInCategory(this.getPositionCategory(), lastScannedPosition); first < category.length;)
         {
            TypedPosition p = (TypedPosition) category[first++];
            p.delete();
            d.removePosition(this.getPositionCategory(), p);
            rememberRegion(((Position) (p)).offset, ((Position) (p)).length);
         }

      }
      catch (BadPositionCategoryException _ex)
      {
      }
      catch (BadLocationException _ex)
      {
      }
      return createRegion();
   }

   /**
    * Gets the contentType attribute of the XMLDocumentPartitioner object
    *
    * @param offset  Description of the Parameter
    * @return        The contentType value
    */
   public String getContentType(int offset)
   {
      TypedPosition p = findClosestPosition(offset);
      if (p != null && p.includes(offset))
      {
         return p.getType();
      }
      return IDocument.DEFAULT_CONTENT_TYPE;
   }

   /**
    * Gets the legalContentTypes attribute of the XMLDocumentPartitioner object
    *
    * @return   The legalContentTypes value
    */
   public String[] getLegalContentTypes()
   {
      return fLegalContentTypes;
   }

   /**
    * Gets the managingPositionCategories attribute of the XMLDocumentPartitioner object
    *
    * @return   The managingPositionCategories value
    */
   public String[] getManagingPositionCategories()
   {
      return new String[]
      {fPositionCategory};
   }

   /**
    * Gets the partition attribute of the XMLDocumentPartitioner object
    *
    * @param offset  Description of the Parameter
    * @return        The partition value
    */
   public ITypedRegion getPartition(int offset)
   {
      try
      {
         Position category[] = fDocument.getPositions(this.getPositionCategory());
         if (category == null || category.length == 0)
         {
            return new TypedRegion(0, fDocument.getLength(), IDocument.DEFAULT_CONTENT_TYPE);
         }
         int index = fDocument.computeIndexInCategory(this.getPositionCategory(), offset);
         if (index < category.length)
         {
            TypedPosition next = (TypedPosition) category[index];
            if (offset == ((Position) (next)).offset)
            {
               return new TypedRegion(next.getOffset(), next.getLength(), next.getType());
            }
            if (index == 0)
            {
               return new TypedRegion(0, ((Position) (next)).offset, IDocument.DEFAULT_CONTENT_TYPE);
            }
            TypedPosition previous = (TypedPosition) category[index - 1];
            if (previous.includes(offset))
            {
               return new TypedRegion(previous.getOffset(), previous.getLength(), previous.getType());
            }

            int endOffset = previous.getOffset() + previous.getLength();
            return new TypedRegion(endOffset, next.getOffset() - endOffset, IDocument.DEFAULT_CONTENT_TYPE);
         }
         TypedPosition previous = (TypedPosition) category[category.length - 1];
         if (previous.includes(offset))
         {
            return new TypedRegion(previous.getOffset(), previous.getLength(), previous.getType());
         }

         int endOffset = previous.getOffset() + previous.getLength();
         return new TypedRegion(endOffset, fDocument.getLength() - endOffset, IDocument.DEFAULT_CONTENT_TYPE);
      }
      catch (BadPositionCategoryException _ex)
      {
      }
      catch (BadLocationException _ex)
      {
      }
      return new TypedRegion(0, fDocument.getLength(), IDocument.DEFAULT_CONTENT_TYPE);
   }

   /**
    * Gets the positionCategory attribute of the XMLDocumentPartitioner object
    *
    * @return   The positionCategory value
    */
   public String getPositionCategory()
   {
      return this.getManagingPositionCategories()[0];
   }

   /**
    * Description of the Method
    *
    * @param offset  Description of the Parameter
    * @return        Description of the Return Value
    */
   protected TypedPosition findClosestPosition(int offset)
   {
      try
      {
         int index = fDocument.computeIndexInCategory(this.getPositionCategory(), offset);
         Position category[] = fDocument.getPositions(this.getPositionCategory());
         if (category.length == 0)
         {
            return null;
         }
         if (index < category.length && offset == category[index].offset)
         {
            return (TypedPosition) category[index];
         }
         if (index > 0)
         {
            index--;
         }
         return (TypedPosition) category[index];
      }
      catch (BadPositionCategoryException _ex)
      {
      }
      catch (BadLocationException _ex)
      {
      }
      return null;
   }

   /**
    * Gets the tokenContentType attribute of the XMLDocumentPartitioner object
    *
    * @param token  Description of the Parameter
    * @return       The tokenContentType value
    */
   protected String getTokenContentType(IToken token)
   {
      Object data = token.getData();
      if (data instanceof String)
      {
         return (String) data;
      }
      return null;
   }

   /** Description of the Method */
   protected void initialize()
   {
      fScanner.setRange(fDocument, 0, fDocument.getLength());
      try
      {
         for (IToken token = fScanner.nextToken(); !token.isEOF(); token = fScanner.nextToken())
         {
            String contentType = getTokenContentType(token);
            if (isSupportedContentType(contentType))
            {
               TypedPosition p = new XMLNode(fScanner.getTokenOffset(), fScanner.getTokenLength(), contentType,
                     fDocument);
               fDocument.addPosition(this.getPositionCategory(), p);
            }
         }
      }
      catch (BadLocationException _ex)
      {
      }
      catch (BadPositionCategoryException _ex)
      {
      }
   }

   /**
    * Gets the supportedContentType attribute of the XMLDocumentPartitioner object
    *
    * @param contentType  Description of the Parameter
    * @return             The supportedContentType value
    */
   protected boolean isSupportedContentType(String contentType)
   {
      if (contentType != null)
      {
         for (int i = 0; i < fLegalContentTypes.length; i++)
         {
            if (fLegalContentTypes[i].equals(contentType))
            {
               return true;
            }
         }

      }
      return false;
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   private IRegion createRegion()
   {
      if (fDeleteOffset == -1)
      {
         if (fStartOffset == -1 || fEndOffset == -1)
         {
            return null;
         }
         return new Region(fStartOffset, fEndOffset - fStartOffset);
      }
      if (fStartOffset == -1 || fEndOffset == -1)
      {
         return new Region(fDeleteOffset, 0);
      }

      int offset = Math.min(fDeleteOffset, fStartOffset);
      int endOffset = Math.max(fDeleteOffset, fEndOffset);
      return new Region(offset, endOffset - offset);
   }

   /**
    * Description of the Method
    *
    * @param offset  Description of the Parameter
    */
   private void rememberDeletedOffset(int offset)
   {
      fDeleteOffset = offset;
   }

   /**
    * Description of the Method
    *
    * @param offset  Description of the Parameter
    * @param length  Description of the Parameter
    */
   private void rememberRegion(int offset, int length)
   {
      if (fStartOffset == -1)
      {
         fStartOffset = offset;
      }
      else if (offset < fStartOffset)
      {
         fStartOffset = offset;
      }
      int endOffset = offset + length;
      if (fEndOffset == -1)
      {
         fEndOffset = endOffset;
      }
      else if (endOffset > fEndOffset)
      {
         fEndOffset = endOffset;
      }
   }

}
