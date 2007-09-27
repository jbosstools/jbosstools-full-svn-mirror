/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.ui.text.rules;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.IDocumentPartitioningListener;
import org.eclipse.jface.text.IDocumentPartitioningListenerExtension;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.TypedRegion;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;

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
public abstract class MultiViewPartitioner extends AbstractPartitioner
{

   private OuterDocumentView outerDocument;
   private DocumentEvent outerDocumentEvent;

   private ViewListener viewListener = new ViewListener();


   /**
    *Constructor for the MultiViewPartitioner object
    *
    * @param scanner  Description of the Parameter
    */
   public MultiViewPartitioner(IPartitionTokenScanner scanner)
   {
      super(scanner);
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
         FlatNode prev = (index > 0) ? (FlatNode) nodes.get(index - 1) : null;

         if (prev != null)
         {
            if (prev.overlapsWith(offset, length))
            {
               addInnerPartitions(list, offset, length, prev);
            }

            if (end <= prev.offset + prev.length)
            {
               break;
            }
         }

         FlatNode next = (index < nodes.size()) ? (FlatNode) nodes.get(index) : null;

         if (next == null || offset < next.offset)
         {
            addOuterPartitions(list, offset, length, prev, next);
         }

         if (next == null)
         {
            break;
         }

         ++index;
      }

      return (TypedRegion[]) list.toArray(new TypedRegion[list.size()]);
   }


   /**
    * Description of the Method
    *
    * @param document  Description of the Parameter
    */
   public void connect(IDocument document)
   {
      //		outerDocument = new OuterDocumentView(document, innerPositions);

      super.connect(document);

      setOuterPartitioner(createPartitioner(null));
      //		IDocumentPartitioner partitioner =
      //			partitioner.connect(outerDocument);
      //		outerDocument.setDocumentPartitioner(partitioner);
      //		outerDocument.addDocumentPartitioningListener(viewListener);
   }


   /** Description of the Method */
   public void disconnect()
   {
      try
      {
         if (outerDocument != null)
         {
            outerDocument.removeDocumentPartitioningListener(viewListener);

            IDocumentPartitioner partitioner = outerDocument.getDocumentPartitioner();

            outerDocument.setDocumentPartitioner(null);
            partitioner.disconnect();
         }
      }
      finally
      {
         // TODO: cleanup listeners
         outerDocument = null;
      }
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
      super.documentAboutToBeChanged(event);

      outerDocumentEvent = null;

      int offset = event.getOffset();
      int length = event.getLength();
      int end = offset + length;

      // find left partition
      int first = computeFlatNodeIndex(offset);
      if (first > 0)
      {
         FlatNode p = (FlatNode) nodes.get(first - 1);

         int right = p.offset + p.length;
         if (offset < right)
         {
            // change overlaps with partition
            InnerDocumentView innerDocument = null;
            if (p instanceof ViewNode)
            {
               // TODO: revisit condition
               innerDocument = ((ViewNode) p).view;
            }

            if (end < right)
            {
               if (innerDocument != null)
               {
                  // cahnge completely inside partition
                  int start = innerDocument.getLocalOffset(offset);
                  innerDocument.fireDocumentAboutToBeChanged(new DocumentEvent(innerDocument, start, length, event.getText()));
               }

               return;
            }

            if (innerDocument != null)
            {
               // cut partition at right
               int start = innerDocument.getLocalOffset(offset);
               innerDocument.fireDocumentAboutToBeChanged(new DocumentEvent(innerDocument, start, innerDocument.getLength() - start, null));
            }
         }
      }

      // find right partition
      int last = computeFlatNodeIndex(end);
      if (last > 0)
      {
         FlatNode p = (FlatNode) nodes.get(last - 1);

         if (p instanceof ViewNode)
         {
            // TODO: revisit condition
            InnerDocumentView innerDocument = ((ViewNode) p).view;
            if (innerDocument != null)
            {
               int right = p.offset + p.length;
               if (end < right)
               {
                  // cut partition at left
                  int cut = innerDocument.getLocalOffset(end);
                  innerDocument.fireDocumentAboutToBeChanged(new DocumentEvent(innerDocument, 0, cut, null));
               }
            }
         }
      }

      if (outerDocument != null)
      {
         int left = outerDocument.getLocalOffset(offset);
         int right = outerDocument.getLocalOffset(end);

         String text = event.getText();

         if (left != right || text != null && text.length() > 0)
         {
            outerDocumentEvent = new DocumentEvent(outerDocument, left, right - left, text);

            outerDocument.fireDocumentAboutToBeChanged(outerDocumentEvent);
         }
      }
   }


   /**
    * Gets the outerDocument attribute of the MultiViewPartitioner object
    *
    * @return   The outerDocument value
    */
   public OuterDocumentView getOuterDocument()
   {
      return outerDocument;
   }


   /*
    * @see org.eclipse.jface.text.IDocumentPartitioner#getPartition(int)
    */
   /**
    * Gets the partition attribute of the MultiViewPartitioner object
    *
    * @param offset  Description of the Parameter
    * @return        The partition value
    */
   public ITypedRegion getPartition(int offset)
   {
      if (nodes.size() == 0)
      {
         return getOuterPartition(offset, null, null);
      }

      int index = computeFlatNodeIndex(offset);
      if (index < nodes.size())
      {
         FlatNode next = (FlatNode) nodes.get(index);

         if (offset == next.offset)
         {
            return getInnerPartition(offset, next);
         }

         if (index == 0)
         {
            return getOuterPartition(offset, null, next);
         }

         FlatNode prev = (FlatNode) nodes.get(index - 1);

         if (prev.includes(offset))
         {
            return getInnerPartition(offset, prev);
         }

         return getOuterPartition(offset, prev, next);
      }

      FlatNode prev = (FlatNode) nodes.get(nodes.size() - 1);

      if (prev.includes(offset))
      {
         return getInnerPartition(offset, prev);
      }

      return getOuterPartition(offset, prev, null);
   }


   /**
    * Sets the outerPartitioner attribute of the MultiViewPartitioner object
    *
    * @param partitioner  The new outerPartitioner value
    */
   public void setOuterPartitioner(IDocumentPartitioner partitioner)
   {
      if (outerDocument == null)
      {
         if (partitioner == null)
         {
            return;
         }

         outerDocument = new OuterDocumentView(document, nodes);
         outerDocument.addDocumentPartitioningListener(viewListener);
      }

      IDocumentPartitioner old = outerDocument.getDocumentPartitioner();
      if (old != null)
      {
         outerDocument.setDocumentPartitioner(null);
         old.disconnect();
      }

      if (partitioner != null)
      {
         partitioner.connect(outerDocument);
      }

      outerDocument.setDocumentPartitioner(partitioner);

      if (partitioner == null)
      {
         outerDocument.removeDocumentPartitioningListener(viewListener);
         outerDocument = null;
      }
   }


   /**
    * Adds a feature to the InnerRegion attribute of the MultiViewPartitioner object
    *
    * @param position  The feature to be added to the InnerRegion attribute
    */
   protected void addInnerRegion(FlatNode position)
   {
      if (outerDocument != null)
      {
         DocumentEvent event = new DocumentEvent(outerDocument, outerDocument.getLocalOffset(position.offset), position.length, null);

         outerDocument.fireDocumentAboutToBeChanged(event);
         super.addInnerRegion(position);
         outerDocument.fireDocumentChanged(event);
      }
      else
      {
         super.addInnerRegion(position);
      }

      if (position instanceof ViewNode)
      {
         // TODO: revisit condition
         IDocumentPartitioner partitioner = createPartitioner(position.type);
         if (partitioner != null)
         {
            InnerDocumentView innerDocument = new InnerDocumentView(document, (ViewNode) position);

            ((ViewNode) position).view = innerDocument;

            partitioner.connect(innerDocument);
            innerDocument.setDocumentPartitioner(partitioner);
            innerDocument.addDocumentPartitioningListener(viewListener);
         }
      }
   }


   /**
    * Create subpartitioner.
    *
    * @param contentType  name of inner partition or
    *     <code>null</code> for outer partition
    * @return             Description of the Return Value
    */
   protected abstract IDocumentPartitioner createPartitioner(String contentType);


   /**
    * Description of the Method
    *
    * @param position  Description of the Parameter
    */
   protected void deleteInnerRegion(FlatNode position)
   {
      super.deleteInnerRegion(position);

      if (position instanceof ViewNode)
      {
         // TODO: revisit condition
         InnerDocumentView innerDocument = ((ViewNode) position).view;
         if (innerDocument != null)
         {
            IDocumentPartitioner partitioner = innerDocument.getDocumentPartitioner();

            innerDocument.removeDocumentPartitioningListener(viewListener);
            innerDocument.setDocumentPartitioner(null);
            partitioner.disconnect();
         }
      }
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

      // fixup/notify inner views laying on change boundaries

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

               if (p instanceof ViewNode)
               {
                  // TODO: revisit condition
                  InnerDocumentView innerDocument = ((ViewNode) p).view;
                  if (innerDocument != null)
                  {
                     int start = innerDocument.getLocalOffset(offset);
                     innerDocument.fireDocumentChanged(new DocumentEvent(innerDocument, start, length, text));
                  }
               }
            }
            else
            {
               // cut partition at right
               int cut = p.offset + p.length - offset;
               p.length -= cut;

               if (p instanceof ViewNode)
               {
                  // TODO: revisit condition
                  InnerDocumentView innerDocument = ((ViewNode) p).view;
                  if (innerDocument != null)
                  {
                     int start = innerDocument.getLocalOffset(offset);
                     // TODO: ???fireDocumentAboutToBeChanged???
                     innerDocument.fireDocumentChanged(new DocumentEvent(innerDocument, start, cut, null));
                  }
               }
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

            if (p instanceof ViewNode)
            {
               // TODO: revisit condition
               InnerDocumentView innerDocument = ((ViewNode) p).view;
               if (innerDocument != null)
               {
                  // TODO: ???fireDocumentAboutToBeChanged???
                  innerDocument.fireDocumentChanged(new DocumentEvent(innerDocument, 0, cut, null));
               }
            }

            --last;
         }
      }

      // fixup partitions laying afrer change

      String text = event.getText();
      if (text != null)
      {
         length -= text.length();
      }

      for (int i = last, size = nodes.size(); i < size; i++)
      {
         ((FlatNode) nodes.get(i)).offset -= length;
      }

      // delete partitions laying completely inside change

      if (first < last)
      {
         do
         {
            deleteInnerRegion((FlatNode) nodes.get(--last));
         } while (first < last);

         rememberRegion(offset, 0);
      }

      // notify outer view about transtated change

      if (outerDocumentEvent != null)
      {
         outerDocument.fireDocumentChanged(outerDocumentEvent);
      }

      return first;
   }


   /*
    * @see org.eclipse.jface.text.IDocumentPartitioner#computePartitioning(int, int)
    */
   /**
    * Gets the contentType attribute of the MultiViewPartitioner object
    *
    * @param parent  Description of the Parameter
    * @param view    Description of the Parameter
    * @return        The contentType value
    */
   protected String getContentType(String parent, String view)
   {
      if (view != null)
      {
         return view;
      }

      if (parent != null)
      {
         return parent;
      }

      return IDocument.DEFAULT_CONTENT_TYPE;
   }


   /**
    * Gets the innerPartition attribute of the MultiViewPartitioner object
    *
    * @param offset    Description of the Parameter
    * @param position  Description of the Parameter
    * @return          The innerPartition value
    */
   protected ITypedRegion getInnerPartition(int offset, FlatNode position)
   {
      if (position instanceof ViewNode)
      {
         // TODO: revisit condition
         InnerDocumentView innerDocument = ((ViewNode) position).view;

         if (innerDocument != null)
         {
            // multiplexing to inner view
            try
            {
               // convert to inner offset
               ITypedRegion region = innerDocument.getPartition(innerDocument.getLocalOffset(offset));

               // convert to parent offset
               offset = innerDocument.getParentOffset(region.getOffset());

               return new TypedRegion(offset, region.getLength(), getContentType(position.type, region.getType()));
            }
            catch (BadLocationException x)
            {
            }
         }
      }

      // simple partition
      return new TypedRegion(position.offset, position.length, position.type);
   }


   /**
    * Gets the outerPartition attribute of the MultiViewPartitioner object
    *
    * @param offset  Description of the Parameter
    * @param prev    Description of the Parameter
    * @param next    Description of the Parameter
    * @return        The outerPartition value
    */
   protected ITypedRegion getOuterPartition(int offset, FlatNode prev, FlatNode next)
   {
      try
      {
         int start;
         int end;
         String type;

         if (outerDocument == null)
         {
            start = 0;
            end = document.getLength();
            type = getContentType(null, IDocument.DEFAULT_CONTENT_TYPE);
         }
         else
         {
            ITypedRegion region = outerDocument.getPartition(outerDocument.getLocalOffset(offset));

            start = region.getOffset();
            end = start + region.getLength();

            // convert to parent offset
            start = outerDocument.getParentOffset(start);
            end = outerDocument.getParentOffset(end);

            type = getContentType(null, region.getType());
         }

         if (prev != null)
         {
            offset = prev.offset + prev.length;
            if (start < offset)
            {
               start = offset;
            }
         }

         if (next != null)
         {
            offset = next.offset;
            if (offset < end)
            {
               end = offset;
            }
         }

         return new TypedRegion(start, end - start, type);
      }
      catch (BadLocationException x)
      {
         throw new IllegalArgumentException();
      }
   }


   /**
    * Description of the Method
    *
    * @param position  Description of the Parameter
    */
   protected void removeInnerRegion(FlatNode position)
   {
      try
      {
         if (outerDocument != null)
         {
            DocumentEvent event = new DocumentEvent(outerDocument, outerDocument.getLocalOffset(position.offset), 0, document.get(position.offset,
               position.length));

            outerDocument.fireDocumentAboutToBeChanged(event);
            super.removeInnerRegion(position);
            outerDocument.fireDocumentChanged(event);
         }
         else
         {
            super.removeInnerRegion(position);
         }

         if (position instanceof ViewNode)
         {
            // TODO: revisit condition
            InnerDocumentView innerDocument = ((ViewNode) position).view;
            if (innerDocument != null)
            {
               IDocumentPartitioner partitioner = innerDocument.getDocumentPartitioner();

               innerDocument.removeDocumentPartitioningListener(viewListener);
               innerDocument.setDocumentPartitioner(null);
               partitioner.disconnect();
            }
         }
      }
      catch (BadLocationException e)
      {
      }
   }


   /**
    * Adds a feature to the InnerPartitions attribute of the MultiViewPartitioner object
    *
    * @param list      The feature to be added to the InnerPartitions attribute
    * @param offset    The feature to be added to the InnerPartitions attribute
    * @param length    The feature to be added to the InnerPartitions attribute
    * @param position  The feature to be added to the InnerPartitions attribute
    */
   private void addInnerPartitions(List list, int offset, int length, FlatNode position)
   {
      InnerDocumentView innerDocument = null;
      if (position instanceof ViewNode)
      {
         // TODO: revisit condition
         innerDocument = ((ViewNode) position).view;
      }

      if (innerDocument == null)
      {
         // simple partition
         list.add(new TypedRegion(position.offset, position.length, getContentType(position.type, null)));
         return;
      }

      // multiplexing to inner view
      try
      {
         // limit region
         int start = Math.max(offset, position.offset);
         int end = Math.min(offset + length, position.offset + position.length);

         // convert to document offsets
         length = end - start;
         offset = innerDocument.getLocalOffset(start);

         ITypedRegion[] regions = innerDocument.computePartitioning(offset, length);

         for (int i = 0; i < regions.length; i++)
         {
            ITypedRegion region = regions[i];

            // convert back to parent offsets
            offset = innerDocument.getParentOffset(region.getOffset());
            length = region.getLength();

            list.add(new TypedRegion(offset, length, getContentType(position.type, region.getType())));
         }
      }
      catch (BadLocationException x)
      {
      }
   }


   /**
    * Adds a feature to the OuterPartitions attribute of the MultiViewPartitioner object
    *
    * @param list    The feature to be added to the OuterPartitions attribute
    * @param offset  The feature to be added to the OuterPartitions attribute
    * @param length  The feature to be added to the OuterPartitions attribute
    * @param prev    The feature to be added to the OuterPartitions attribute
    * @param next    The feature to be added to the OuterPartitions attribute
    */
   private void addOuterPartitions(List list, int offset, int length, FlatNode prev, FlatNode next)
   {
      // limit region
      int start = offset;
      int end = offset + length;

      if (prev != null && start < prev.offset + prev.length)
      {
         start = prev.offset + prev.length;
      }

      if (next != null && next.offset < end)
      {
         end = next.offset;
      }

      if (start == end)
      {
         return;
      }

      if (outerDocument == null)
      {
         list.add(new TypedRegion(start, end - start, getContentType(null, IDocument.DEFAULT_CONTENT_TYPE)));
         return;
      }

      try
      {
         // convert to outer offsets
         start = outerDocument.getLocalOffset(start);
         end = outerDocument.getLocalOffset(end);

         ITypedRegion[] regions = outerDocument.computePartitioning(start, end - start);

         for (int i = 0; i < regions.length; i++)
         {
            ITypedRegion region = regions[i];

            // convert back to parent offsets
            start = outerDocument.getParentOffset(region.getOffset());
            end = start + region.getLength();

            if (prev != null)
            {
               offset = prev.offset + prev.length;
               if (start < offset)
               {
                  start = offset;
               }
            }

            if (next != null)
            {
               offset = next.offset;
               if (offset < end)
               {
                  end = offset;
               }
            }

            list.add(new TypedRegion(start, end - start, getContentType(null, region.getType())));
         }
      }
      catch (BadLocationException x)
      {
      }
   }


   /**
    * Description of the Class
    *
    * @author    Laurent Etiemble
    * @version   $Revision$
    */
   class ViewListener implements IDocumentPartitioningListener, IDocumentPartitioningListenerExtension
   {
      /*
       * @see org.eclipse.jface.text.IDocumentPartitioningListener#documentPartitioningChanged(IDocument)
       */
      /**
       * Description of the Method
       *
       * @param document  Description of the Parameter
       */
      public void documentPartitioningChanged(IDocument document)
      {
         IDocumentView view = (IDocumentView) document;

         int start = view.getParentOffset(0);
         int end = view.getParentOffset(view.getLength());

         rememberRegion(start, end - start);
      }


      /*
       * @see org.eclipse.jface.text.IDocumentPartitioningListenerExtension#documentPartitioningChanged(IDocument, IRegion)
       */
      /**
       * Description of the Method
       *
       * @param document  Description of the Parameter
       * @param region    Description of the Parameter
       */
      public void documentPartitioningChanged(IDocument document, IRegion region)
      {
         IDocumentView view = (IDocumentView) document;

         int offset = region.getOffset();

         int start = view.getParentOffset(offset);
         int end = view.getParentOffset(offset + region.getLength());

         rememberRegion(start, end - start);
      }
   }
}
