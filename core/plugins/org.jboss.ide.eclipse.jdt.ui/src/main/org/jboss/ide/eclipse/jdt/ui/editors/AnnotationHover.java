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
package org.jboss.ide.eclipse.jdt.ui.editors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.internal.ui.JavaUIMessages;
import org.eclipse.jface.internal.text.html.HTMLPrinter;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.texteditor.MarkerAnnotation;
import org.jboss.ide.eclipse.core.AbstractPlugin;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class AnnotationHover implements IAnnotationHover
{
   /**Constructor for the AnnotationHover object */
   public AnnotationHover()
   {
   }

   /**
    * Gets the hoverInfo attribute of the AnnotationHover object
    *
    * @param sourceViewer  Description of the Parameter
    * @param lineNumber    Description of the Parameter
    * @return              The hoverInfo value
    */
   public String getHoverInfo(ISourceViewer sourceViewer, int lineNumber)
   {
      List markers = this.getJSPAnnotationsForLine(sourceViewer, lineNumber);
      if (markers != null)
      {
         if (markers.size() == 1)
         {
            IMarker marker = (IMarker) markers.get(0);
            String message = marker.getAttribute(IMarker.MESSAGE, (String) null);
            if (message != null && message.trim().length() > 0)
            {
               return formatSingleMessage(message);
            }
         }
         else
         {
            List messages = new ArrayList();
            Iterator e = markers.iterator();
            while (e.hasNext())
            {
               IMarker marker = (IMarker) e.next();
               String message = marker.getAttribute(IMarker.MESSAGE, (String) null);
               if (message != null && message.trim().length() > 0)
               {
                  messages.add(message.trim());
               }
            }
            if (messages.size() == 1)
            {
               return this.formatSingleMessage((String) messages.get(0));
            }

            if (messages.size() > 1)
            {
               return this.formatMultipleMessages(messages);
            }
         }
      }
      return null;
   }

   /**
    * Gets the jSPAnnotationsForLine attribute of the AnnotationHover object
    *
    * @param viewer  Description of the Parameter
    * @param line    Description of the Parameter
    * @return        The jSPAnnotationsForLine value
    */
   protected List getJSPAnnotationsForLine(ISourceViewer viewer, int line)
   {
      IAnnotationModel model = viewer.getAnnotationModel();

      if (model == null)
      {
         return null;
      }

      List markers = new ArrayList();
      Iterator e = model.getAnnotationIterator();
      while (e.hasNext())
      {
         Object o = e.next();
         if (o instanceof MarkerAnnotation)
         {
            MarkerAnnotation a = (MarkerAnnotation) o;
            try
            {
               Integer ln = (Integer) a.getMarker().getAttribute(IMarker.LINE_NUMBER);
               if (ln.intValue() == line + 1)
               {
                  markers.add(a.getMarker());
               }
            }
            catch (CoreException ex)
            {
               AbstractPlugin.log(ex);
            }
         }
      }
      return markers;
   }

   /*
    * Formats several message as HTML text.
    */
   /**
    * Description of the Method
    *
    * @param messages  Description of the Parameter
    * @return          Description of the Return Value
    */
   private String formatMultipleMessages(List messages)
   {
      StringBuffer buffer = new StringBuffer();
      HTMLPrinter.addPageProlog(buffer);
      HTMLPrinter.addParagraph(buffer, HTMLPrinter
            .convertToHTMLContent(JavaUIMessages.JavaAnnotationHover_multipleMarkersAtThisLine));//$NON-NLS-1$

      HTMLPrinter.startBulletList(buffer);
      Iterator e = messages.iterator();
      while (e.hasNext())
      {
         HTMLPrinter.addBullet(buffer, HTMLPrinter.convertToHTMLContent((String) e.next()));
      }
      HTMLPrinter.endBulletList(buffer);

      HTMLPrinter.addPageEpilog(buffer);
      return buffer.toString();
   }

   /*
    * Formats a message as HTML text.
    */
   /**
    * Description of the Method
    *
    * @param message  Description of the Parameter
    * @return         Description of the Return Value
    */
   private String formatSingleMessage(String message)
   {
      StringBuffer buffer = new StringBuffer();
      HTMLPrinter.addPageProlog(buffer);
      HTMLPrinter.addParagraph(buffer, HTMLPrinter.convertToHTMLContent(message));
      HTMLPrinter.addPageEpilog(buffer);
      return buffer.toString();
   }
}
