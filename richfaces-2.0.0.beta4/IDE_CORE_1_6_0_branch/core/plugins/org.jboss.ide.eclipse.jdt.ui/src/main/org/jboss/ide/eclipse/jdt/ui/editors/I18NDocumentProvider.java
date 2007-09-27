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
package org.jboss.ide.eclipse.jdt.ui.editors;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ContainerGenerator;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.eclipse.ui.texteditor.ResourceMarkerAnnotationModel;
import org.jboss.ide.eclipse.jdt.ui.JDTUIMessages;

/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class I18NDocumentProvider extends FileDocumentProvider
{
   private final static char BOM = 0xFEFF;

   /**
    * Tries to determine encoding from contents of the stream.
    * Returns <code>null</code> if encoding is unknown.
    *
    * @param in               Description of the Parameter
    * @return                 The declaredEncoding value
    * @exception IOException  Description of the Exception
    */
   public String getDeclaredEncoding(InputStream in) throws IOException
   {
      return getBOMEncoding(in);
   }

   /**
    * Gets the encoding attribute of the I18NDocumentProvider object
    *
    * @param element  Description of the Parameter
    * @return         The encoding value
    */
   public String getEncoding(Object element)
   {
      String encoding = super.getEncoding(element);
      if (encoding != null)
      {
         return encoding;
      }

      if (element instanceof IStorageEditorInput)
      {
         IStorageEditorInput sei = (IStorageEditorInput) element;

         try
         {
            InputStream in = sei.getStorage().getContents();
            try
            {
               encoding = getDeclaredEncoding(in);
            }
            finally
            {
               in.close();
            }
         }
         catch (CoreException e)
         {
         }
         catch (IOException e)
         {
         }

         if (encoding == null)
         {
            encoding = getDefaultEncoding();
         }

         setEncoding(element, encoding);
      }

      return encoding;
   }

   /*
    * @see org.eclipse.ui.editors.text.IStorageDocumentProvider#setEncoding(Object, String)
    */
   /**
    * Sets the encoding attribute of the I18NDocumentProvider object
    *
    * @param element   The new encoding value
    * @param encoding  The new encoding value
    */
   public void setEncoding(Object element, String encoding)
   {
      if (encoding == null)
      {
         encoding = getDefaultEncoding();
      }

      super.setEncoding(element, encoding);
   }

   /*
    * @see org.eclipse.ui.texteditor.AbstractDocumentProvider#doSaveDocument(IProgressMonitor, Object, IDocument, boolean)
    */
   /**
    * Description of the Method
    *
    * @param monitor            Description of the Parameter
    * @param element            Description of the Parameter
    * @param document           Description of the Parameter
    * @param overwrite          Description of the Parameter
    * @exception CoreException  Description of the Exception
    */
   protected void doSaveDocument(IProgressMonitor monitor, Object element, IDocument document, boolean overwrite)
         throws CoreException
   {
      if (!(element instanceof IFileEditorInput))
      {
         super.doSaveDocument(monitor, element, document, overwrite);
         return;
      }

      IFileEditorInput input = (IFileEditorInput) element;

      String content = document.get();
      String encoding = input.getFile().getCharset(true);

      if (encoding == null)
      {
         encoding = super.getEncoding(element);
         if (encoding == null
         /*
          * || !encoding.startsWith("UTF-16")
          */
         )
         {
            encoding = getDefaultEncoding();
         }
      }
      else
      {
         //            setEncoding(element, encoding);
      }

      if (encoding.startsWith("UTF-16")//$NON-NLS-1$
      )
      {
         content = BOM + content;
      }

      InputStream stream;
      try
      {
         stream = new ByteArrayInputStream(content.getBytes(encoding));
      }
      catch (UnsupportedEncodingException e)
      {
         IStatus s = new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID, IStatus.OK, JDTUIMessages
               .getString("I18NDocumentProvider.error.encoding"), e);//$NON-NLS-1$

         throw new CoreException(s);
      }

      IFile file = input.getFile();
      if (file.exists())
      {
         FileInfo info = (FileInfo) getElementInfo(element);

         if (info != null && !overwrite)
         {
            checkSynchronizationState(info.fModificationStamp, file);
         }

         // inform about the upcoming content change
         fireElementStateChanging(element);

         try
         {
            file.setContents(stream, overwrite, true, monitor);
         }
         catch (CoreException x)
         {
            // inform about failure
            fireElementStateChangeFailed(element);
            throw x;
         }
         catch (RuntimeException x)
         {
            // inform about failure
            fireElementStateChangeFailed(element);
            throw x;
         }

         // If here, the editor state will be flipped to "not dirty".
         // Thus, the state changing flag will be reset.

         if (info != null)
         {
            ResourceMarkerAnnotationModel model = (ResourceMarkerAnnotationModel) info.fModel;

            model.updateMarkers(info.fDocument);

            info.fModificationStamp = computeModificationStamp(file);
         }
      }
      else
      {
         try
         {
            monitor.beginTask(JDTUIMessages.getString("I18NDocumentProvider.task.saving"), 2000);//$NON-NLS-1$

            ContainerGenerator generator = new ContainerGenerator(file.getParent().getFullPath());

            generator.generateContainer(new SubProgressMonitor(monitor, 1000));

            file.create(stream, false, new SubProgressMonitor(monitor, 1000));
         }
         finally
         {
            monitor.done();
         }
      }
   }

   /**
    * Sets the documentContent attribute of the I18NDocumentProvider object
    *
    * @param document           The new documentContent value
    * @param contentStream      The new documentContent value
    * @param encoding           The new documentContent value
    * @exception CoreException  Description of the Exception
    */
   protected void setDocumentContent(IDocument document, InputStream contentStream, String encoding)
         throws CoreException
   {
      Reader in = null;

      try
      {
         if (encoding == null)
         {
            encoding = getDefaultEncoding();
         }

         in = new InputStreamReader(contentStream, encoding);

         StringBuffer buffer = new StringBuffer();

         char[] readBuffer = new char[2048];
         int n = in.read(readBuffer);
         while (n > 0)
         {
            buffer.append(readBuffer, 0, n);
            n = in.read(readBuffer);
         }

         if (buffer.length() > 0 && buffer.charAt(0) == BOM)
         {
            buffer.deleteCharAt(0);
         }

         document.set(buffer.toString());
      }
      catch (IOException x)
      {
         String msg = x.getMessage();
         if (msg == null)
         {
            msg = "";//$NON-NLS-1$
         }

         IStatus s = new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID, IStatus.OK, msg, x);

         throw new CoreException(s);
      }
      finally
      {
         if (in != null)
         {
            try
            {
               in.close();
            }
            catch (IOException x)
            {
            }
         }
      }
   }

   /**
    * Tries to determine encoding from the byte order mark.
    * Returns <code>null</code> if encoding is unknown.
    *
    * @param in               Description of the Parameter
    * @return                 The bOMEncoding value
    * @exception IOException  Description of the Exception
    */
   private String getBOMEncoding(InputStream in) throws IOException
   {
      int first = in.read();
      if (first < 0)
      {
         return null;
      }

      int second = in.read();
      if (second < 0)
      {
         return null;
      }

      // look for the UTF-16 Byte Order Mark (BOM)
      if (first == 0xFE && second == 0xFF)
      {
         return "UTF-16BE";//$NON-NLS-1$
      }

      if (first == 0xFF && second == 0xFE)
      {
         return "UTF-16LE";//$NON-NLS-1$
      }

      int third = in.read();
      if (third < 0)
      {
         return null;
      }

      // look for the UTF-8 BOM
      if (first == 0xEF && second == 0xBB && third == 0xBF)
      {
         return "UTF-8";//$NON-NLS-1$
      }

      return null;
   }
}
