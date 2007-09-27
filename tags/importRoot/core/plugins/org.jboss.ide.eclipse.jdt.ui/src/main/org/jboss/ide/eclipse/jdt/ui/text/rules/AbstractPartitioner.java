/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.ui.text.rules;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.IDocumentPartitionerExtension;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.TypedRegion;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.jface.text.rules.IToken;

/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * Advanced partitioner which maintains partitions as views to connected
 * document. Views have own partitioners themselves. This class is designed
 * as a base for complex partitioners such as for JSP, PHP, ASP, etc.
 * languages.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public abstract class AbstractPartitioner
    implements IDocumentPartitioner, IDocumentPartitionerExtension
{

   /** Connected document */
   protected IDocument document;

   /** Flat structure of the document */
   protected List nodes = new ArrayList();

   /** The offset at which the last changed partition ends */
   protected int regionEnd;

   /** The offset at which the first changed partition starts */
   protected int regionStart;
   /** Partition scanner */
   protected IPartitionTokenScanner scanner;


   /**
    *Constructor for the AbstractPartitioner object
    *
    * @param scanner  Description of the Parameter
    */
   public AbstractPartitioner(IPartitionTokenScanner scanner)
   {
      this.scanner = scanner;
   }


   /*
    * @see org.eclipse.jface.text.IDocumentPartitioner#computePartitioning(int, int)
    */
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

      int end = offset + length;

      int index = computeFlatNodeIndex(offset);
      while (true)
      {
         FlatNode prev = (index > 0)
            ? (FlatNode) nodes.get(index - 1) : null;

         if (prev != null)
         {
            if (prev.overlapsWith(offset, length))
            {
               list.add(new TypedRegion(
                  prev.offset, prev.length, prev.type));
            }

            if (end <= prev.offset + prev.length)
            {
               break;
            }
         }

         FlatNode next = (index < nodes.size())
            ? (FlatNode) nodes.get(index) : null;

         if (next == null || offset < next.offset)
         {
            int off0 = offset;
            int off1 = offset + length;

            if (prev != null && off0 < prev.offset + prev.length)
            {
               off0 = prev.offset + prev.length;
            }

            if (next != null && next.offset < off1)
            {
               off1 = next.offset;
            }

            if (off0 < off1)
            {
               list.add(new TypedRegion(
                  off0, off1 - off0, IDocument.DEFAULT_CONTENT_TYPE));
            }
         }

         if (next == null)
         {
            break;
         }

         ++index;
      }

      return (TypedRegion[]) list.toArray(new TypedRegion[list.size()]);
   }


   /*
    * @see org.eclipse.jface.text.IDocumentPartitioner#connect(IDocument)
    */
   /**
    * Description of the Method
    *
    * @param document  Description of the Parameter
    */
   public void connect(IDocument document)
   {
      this.document = document;

      initialize();
   }


   /**
    * Description of the Method
    *
    * @param offset  Description of the Parameter
    * @param length  Description of the Parameter
    * @return        Description of the Return Value
    */
   public boolean containsPosition(int offset, int length)
   {
      int size = nodes.size();
      if (size == 0)
      {
         return false;
      }

      int index = computeFlatNodeIndex(offset);
      if (index < size)
      {
         FlatNode p = (FlatNode) nodes.get(index);

         while (p.offset == offset)
         {
            if (p.length == length)
            {
               return true;
            }

            if (++index < size)
            {
               p = (FlatNode) nodes.get(index);
            }
            else
            {
               break;
            }
         }
      }

      return false;
   }


   /*
    * @see org.eclipse.jface.text.IDocumentPartitioner#disconnect()
    */
   /** Description of the Method */
   public void disconnect()
   {
      nodes.clear();
      document = null;
   }


   /*
    * @see org.eclipse.jface.text.IDocumentPartitioner#documentAboutToBeChanged(DocumentEvent)
    */
   /**
    * Description of the Method
    *
    * @param event  Description of the Parameter
    */
   public void documentAboutToBeChanged(DocumentEvent event)
   {
      regionStart = regionEnd = -1;
   }


   /*
    * @see org.eclipse.jface.text.IDocumentPartitioner#documentChanged(DocumentEvent)
    */
   /**
    * Description of the Method
    *
    * @param event  Description of the Parameter
    * @return       Description of the Return Value
    */
   public boolean documentChanged(DocumentEvent event)
   {
      return (documentChanged2(event) != null);
   }


   /*
    * @see org.eclipse.jface.text.IDocumentPartitionerExtension#documentChanged2(DocumentEvent)
    */
   /**
    * Description of the Method
    *
    * @param event  Description of the Parameter
    * @return       Description of the Return Value
    */
   public IRegion documentChanged2(DocumentEvent event)
   {
      int first = fixupPartitions(event);

      FlatNode[] category = (FlatNode[]) nodes.toArray(new FlatNode[nodes.size()]);

      // repartition changed region

      String contentType = IDocument.DEFAULT_CONTENT_TYPE;

      int offset;

      if (first == 0)
      {
         offset = 0;// Bug #697414: first offset
      }
      else
      {
         offset = event.getOffset();

         FlatNode partition = category[first - 1];
         if (partition.includes(offset))
         {
            offset = partition.offset;
            contentType = partition.type;
            --first;
         }
         else if (offset == partition.offset + partition.length)
         {
            offset = partition.offset;
            contentType = partition.type;
            --first;
         }
         else
         {
            offset = partition.offset + partition.length;
         }
      }

      // should not be changed since last conversion
      // category = (FlatNode[]) nodes.toArray(new FlatNode[nodes.size()]);

      scanner.setPartialRange(document,
         offset, document.getLength(), contentType, offset);

      int lastScannedPosition = offset;
      IToken token = scanner.nextToken();
      while (!token.isEOF())
      {
         contentType = getTokenContentType(token);

         if (!isSupportedContentType(contentType))
         {
            token = scanner.nextToken();
            continue;
         }

         offset = scanner.getTokenOffset();
         int length = scanner.getTokenLength();

         lastScannedPosition = offset + length;

         // remove all affected positions
         while (first < category.length)
         {
            FlatNode p = category[first];
            if (p.offset + p.length < lastScannedPosition ||
               (p.overlapsWith(offset, length) &&
               (!containsPosition(offset, length) ||
               !contentType.equals(p.type))))
            {
               removeInnerRegion(p);
               rememberRegion(p.offset, p.length);
               ++first;
            }
            else
            {
               break;
            }
         }

         // if position already exists we are done
         if (containsPosition(offset, length))
         {
            if (lastScannedPosition > event.getOffset())
            {
               // TODO: optional repartition till end of doc
               return createRegion();
            }

            ++first;
         }
         else
         {
            // insert the new type position
            addInnerRegion(createNode(contentType, offset, length));
            rememberRegion(offset, length);
         }

         token = scanner.nextToken();
      }

      // remove all positions behind lastScannedPosition
      // since there aren't any further types

//		first = computeIndexInInnerDocuments(lastScannedPosition);
      while (first < category.length)
      {
         FlatNode p = category[first++];
         removeInnerRegion(p);
         rememberRegion(p.offset, p.length);
      }

      return createRegion();
   }


   /*
    * @see org.eclipse.jface.text.IDocumentPartitioner#getContentType(int)
    */
   /**
    * Gets the contentType attribute of the AbstractPartitioner object
    *
    * @param offset  Description of the Parameter
    * @return        The contentType value
    */
   public String getContentType(int offset)
   {
      return getPartition(offset).getType();
   }


   /*
    * @see org.eclipse.jface.text.IDocumentPartitioner#getLegalContentTypes()
    */
   /**
    * Gets the legalContentTypes attribute of the AbstractPartitioner object
    *
    * @return   The legalContentTypes value
    */
   public String[] getLegalContentTypes()
   {
      // TODO: implementation
      return null;
   }


   /*
    * @see org.eclipse.jface.text.IDocumentPartitioner#getPartition(int)
    */
   /**
    * Gets the partition attribute of the AbstractPartitioner object
    *
    * @param offset  Description of the Parameter
    * @return        The partition value
    */
   public ITypedRegion getPartition(int offset)
   {
      if (nodes.size() == 0)
      {
         return new TypedRegion(
            0, document.getLength(), IDocument.DEFAULT_CONTENT_TYPE);
      }

      int index = computeFlatNodeIndex(offset);
      if (index < nodes.size())
      {
         FlatNode next = (FlatNode) nodes.get(index);

         if (offset == next.offset)
         {
            return new TypedRegion(next.offset, next.length, next.type);
         }

         if (index == 0)
         {
            return new TypedRegion(
               0, next.offset, IDocument.DEFAULT_CONTENT_TYPE);
         }

         FlatNode prev = (FlatNode) nodes.get(index - 1);

         if (prev.includes(offset))
         {
            return new TypedRegion(prev.offset, prev.length, prev.type);
         }

         int end = prev.offset + prev.length;
         return new TypedRegion(
            end, next.offset - end, IDocument.DEFAULT_CONTENT_TYPE);
      }

      FlatNode prev = (FlatNode) nodes.get(nodes.size() - 1);

      if (prev.includes(offset))
      {
         return new TypedRegion(prev.offset, prev.length, prev.type);
      }

      int end = prev.offset + prev.length;

      return new TypedRegion(
         end, document.getLength() - end, IDocument.DEFAULT_CONTENT_TYPE);
   }


   /**
    * Adds a feature to the InnerRegion attribute of the AbstractPartitioner object
    *
    * @param position  The feature to be added to the InnerRegion attribute
    */
   protected void addInnerRegion(FlatNode position)
   {
      nodes.add(computeFlatNodeIndex(position.offset), position);
   }


   /**
    * Computes the index in the list of flat nodes at which an
    * flat node with the given offset would be inserted. The
    * flat node is supposed to become the first in this list
    * of all flat nodes with the same offset.
    *
    * @param offset  the offset for which the index is computed
    * @return        the computed index
    */
   protected int computeFlatNodeIndex(int offset)
   {
      if (nodes.size() == 0)
      {
         return 0;
      }

      int left = 0;

      int mid = 0;
      int right = nodes.size() - 1;

      FlatNode p = null;

      while (left < right)
      {
         mid = (left + right) / 2;

         p = (FlatNode) nodes.get(mid);

         if (offset < p.offset)
         {
            right = (left == mid) ? left : mid - 1;
         }
         else if (offset > p.offset)
         {
            left = (right == mid) ? right : mid + 1;
         }
         else if (offset == p.offset)
         {
            left = right = mid;
         }
      }

      int pos = left;
      p = (FlatNode) nodes.get(pos);
      if (offset > p.offset)
      {
         // append to the end
         pos++;
      }
      else
      {
         // entry will became the first of all entries with the same offset
         do
         {
            --pos;
            if (pos < 0)
            {
               break;
            }
            p = (FlatNode) nodes.get(pos);
         } while (offset == p.offset);
         ++pos;
      }

      return pos;
   }


   /**
    * Description of the Method
    *
    * @param type    Description of the Parameter
    * @param offset  Description of the Parameter
    * @param length  Description of the Parameter
    * @return        Description of the Return Value
    */
   protected FlatNode createNode(String type, int offset, int length)
   {
      FlatNode node = new FlatNode(type);
      node.offset = offset;
      node.length = length;
      return node;
   }


   /**
    * Description of the Method
    *
    * @param position  Description of the Parameter
    */
   protected void deleteInnerRegion(FlatNode position)
   {
      nodes.remove(position);// TODO: Indexed remove?
   }


   /**
    * Description of the Method
    *
    * @param event  Description of the Parameter
    * @return       Description of the Return Value
    */
   protected int fixupPartitions(DocumentEvent event)
   {
      int offset = event.getOffset();
      int length = event.getLength();
      int end = offset + length;

      // fixup flat nodes laying on change boundaries

      int first = computeFlatNodeIndex(offset);
      if (first > 0)
      {
         FlatNode p = (FlatNode) nodes.get(first - 1);

         int right = p.offset + p.length;
         if (offset < right)
         {
            // change overlaps with partition
            if (end < right)
            {
               // cahnge completely inside partition
               String text = event.getText();
               p.length -= length;
               if (text != null)
               {
                  p.length += text.length();
               }
            }
            else
            {
               // cut partition at right
               int cut = p.offset + p.length - offset;
               p.length -= cut;
            }
         }
      }

      int last = computeFlatNodeIndex(end);
      if (first < last)
      {
         FlatNode p = (FlatNode) nodes.get(last - 1);

         int right = p.offset + p.length;
         if (end < right)
         {
            // cut partition at left
            int cut = end - p.offset;
            p.length -= cut;
            p.offset = offset;

            String text = event.getText();
            if (text != null)
            {
               p.offset += text.length();
            }

            --last;
         }
      }

      // fixup flat nodes laying afrer change

      String text = event.getText();
      if (text != null)
      {
         length -= text.length();
      }

      for (int i = last, size = nodes.size(); i < size; i++)
      {
         ((FlatNode) nodes.get(i)).offset -= length;
      }

      // delete flat nodes laying completely inside change boundaries

      if (first < last)
      {
         do
         {
            deleteInnerRegion((FlatNode) nodes.get(--last));
         } while (first < last);

         rememberRegion(offset, 0);
      }

      return first;
   }


   /**
    * Returns a content type encoded in the given token. If the token's
    * data is not <code>null</code> and a string it is assumed that
    * it is the encoded content type.
    *
    * @param token  the token whose content type is to be determined
    * @return       the token's content type
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


   /** Performs the initial partitioning of the partitioner's document. */
   protected void initialize()
   {
      scanner.setRange(document, 0, document.getLength());

      IToken token = scanner.nextToken();
      while (!token.isEOF())
      {
         String contentType = getTokenContentType(token);

         if (isSupportedContentType(contentType))
         {
            addInnerRegion(createNode(contentType,
               scanner.getTokenOffset(), scanner.getTokenLength()));
         }

         token = scanner.nextToken();
      }
   }


   /**
    * Returns whether the given type is one of the legal content types.
    *
    * @param contentType  the content type to check
    * @return             <code>true</code> if the content type is a legal content type
    */
   protected boolean isSupportedContentType(String contentType)
   {
      /*
       * TODO: implementation
       * if (contentType != null) {
       * for (int i= 0; i < fLegalContentTypes.length; i++) {
       * if (fLegalContentTypes[i].equals(contentType)) {
       * return true;
       * }
       * }
       * }
       * return false;
       */
      return (contentType != null);
   }


   /**
    * Description of the Method
    *
    * @param position  Description of the Parameter
    */
   protected void removeInnerRegion(FlatNode position)
   {
      nodes.remove(position);// TODO: Indexed remove?
   }


   /**
    * Description of the Method
    *
    * @param position  Description of the Parameter
    */
   protected void resizeInnerRegion(FlatNode position)
   {
   }


   /**
    * Creates the minimal region containing all partition changes using
    * the remembered offsets.
    *
    * @return   the minimal region containing all the partition changes
    */
   protected final IRegion createRegion()
   {
      if (regionStart == -1 || regionEnd == -1)
      {
         return null;
      }

      return new Region(regionStart, regionEnd - regionStart);
   }


   /**
    * Helper method for tracking the minimal region containg all
    * partition changes. If <code>offset</code> is smaller than the
    * remembered offset, <code>offset</code> will from now on be
    * remembered. If <code>offset + length</code> is greater than
    * the remembered end offset, it will be remembered from now on.
    *
    * @param offset  the offset
    * @param length  the length
    */
   protected final void rememberRegion(int offset, int length)
   {
      // remember start offset
      if (regionStart == -1)
      {
         regionStart = offset;
      }
      else if (offset < regionStart)
      {
         regionStart = offset;
      }

      // remember end offset
      int endOffset = offset + length;

      if (regionEnd == -1)
      {
         regionEnd = endOffset;
      }
      else if (endOffset > regionEnd)
      {
         regionEnd = endOffset;
      }
   }
}
