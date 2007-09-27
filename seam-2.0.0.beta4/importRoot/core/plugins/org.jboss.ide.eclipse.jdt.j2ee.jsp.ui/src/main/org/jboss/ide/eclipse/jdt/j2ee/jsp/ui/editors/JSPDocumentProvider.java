/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.editors;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.DefaultLineTracker;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.ILineTracker;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.ui.IFileEditorInput;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.core.compiler.jasper.JSPProject;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.core.compiler.jasper.JSPProjectManager;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.core.compiler.jasper.JSPServletContext;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.JDTJ2EEJSPUIPlugin;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.reconcilier.JSPReconciler;
import org.jboss.ide.eclipse.jdt.ui.editors.I18NDocumentProvider;
import org.jboss.ide.eclipse.jdt.ui.text.rules.WhitespaceDetector;

/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * JSP document provider.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPDocumentProvider extends I18NDocumentProvider
{
   /** Description of the Field */
   protected IWhitespaceDetector detector = new WhitespaceDetector();
   private IDocument document;

   private JSPEditor editor;
   private IFile file;
   private JSPServletContext.IJSPContentProvider provider;


   /**
    *Constructor for the JSPDocumentProvider object
    *
    * @param editor  Description of the Parameter
    */
   public JSPDocumentProvider(JSPEditor editor)
   {
      this.editor = editor;
   }


   /**
    * Creates a line tracker working with the same line delimiters
    * as the document of the given element. Assumes the element to
    * be managed by this document provider.
    * REVISIT hack: java-like editor behavoir
    *
    * @param element  the element serving as blue print
    * @return         a line tracker based on the same line delimiters as
    *     the element's document
    */
   public ILineTracker createLineTracker(Object element)
   {
      return new DefaultLineTracker();
   }


   /**
    * Gets the declaredEncoding attribute of the JSPDocumentProvider object
    *
    * @param in               Description of the Parameter
    * @return                 The declaredEncoding value
    * @exception IOException  Description of the Exception
    */
   public String getDeclaredEncoding(InputStream in)
      throws IOException
   {
      if (!in.markSupported())
      {
         in = new BufferedInputStream(in, 512);
      }

      in.mark(512);
      String encoding = super.getDeclaredEncoding(in);
      if (encoding != null)
      {
         return encoding;
      }

      in.reset();

      while (true)
      {
         String directive = nextDirective(in);
         if (directive == null)
         {
            // end of stream
            return null;
         }

         int pos0;

         int pos1;

         for (pos0 = 0; pos0 < directive.length(); pos0++)
         {
            if (!detector.isWhitespace(directive.charAt(pos0)))
            {
               break;
            }
         }

         for (pos1 = pos0; pos1 < directive.length(); pos1++)
         {
            if (detector.isWhitespace(directive.charAt(pos1)))
            {
               break;
            }
         }

         if (!directive.substring(pos0, pos1).equals("page")//$NON-NLS-1$
         )
         {

            continue;
         }

         Map attributes = getAttributes(directive.substring(pos1));

         encoding = (String) attributes.get("pageEncoding");//$NON-NLS-1$
         if (encoding != null)
         {
            return encoding;
         }

         String contentType = (String) attributes.get("contentType");//$NON-NLS-1$
         if (contentType != null)
         {
            pos0 = contentType.indexOf("charset=");//$NON-NLS-1$
            if (pos0 < 0)
            {
               continue;
            }

            encoding = contentType.substring(pos0 + 8);

            return encoding;
         }
      }
   }


   /*
    * @see org.eclipse.ui.editors.text.IStorageDocumentProvider#getDefaultEncoding()
    */
   /**
    * Gets the defaultEncoding attribute of the JSPDocumentProvider object
    *
    * @return   The defaultEncoding value
    */
   public String getDefaultEncoding()
   {
      return "ISO-8859-1";//$NON-NLS-1$
   }


   /*
    * @see org.eclipse.ui.texteditor.AbstractDocumentProvider#createDocument(Object)
    */
   /**
    * Description of the Method
    *
    * @param element            Description of the Parameter
    * @return                   Description of the Return Value
    * @exception CoreException  Description of the Exception
    */
   protected IDocument createDocument(Object element)
      throws CoreException
   {
      this.document = super.createDocument(element);
      if (this.document != null)
      {
         IDocumentPartitioner partitioner = JDTJ2EEJSPUIPlugin.getDefault().getJSPTextTools().createJSPPartitioner();

         // Connect the partitioner
         partitioner.connect(this.document);
         this.document.setDocumentPartitioner(partitioner);

         // Connect the reconcilier
         JSPReconciler rec = new JSPReconciler(this.editor);
         this.editor.setReconcilier(rec);
         rec.createTree(this.document);
         document.addDocumentListener(rec);

         if (element instanceof IFileEditorInput)
         {
            IFileEditorInput input = (IFileEditorInput) element;
            if (input != null)
            {
               this.file = input.getFile();
            }

            if (this.file != null)
            {
               JSPProject project = JSPProjectManager.getJSPProject(this.file.getProject());
               if (project.isActive())
               {
                  this.provider =
                     new JSPServletContext.IJSPContentProvider()
                     {
                        public byte[] getContent()
                        {
                           return JSPDocumentProvider.this.document.get().getBytes();
                        }
                     };

                  String path = project.getJSPURI(this.file);
                  project.getServletContext().addContentProvider(path, this.provider);
               }
            }
         }
      }

      return this.document;
   }


   /** Description of the Method */
   protected void disconnected()
   {
      if (this.file != null)
      {
         JSPProject project = JSPProjectManager.getJSPProject(this.file.getProject());
         if (project.isActive())
         {
            String path = project.getJSPURI(this.file);
            project.getServletContext().removeContentProvider(path);
         }
      }

      super.disconnected();
   }


   /**
    * Gets the attributes attribute of the JSPDocumentProvider object
    *
    * @param str  Description of the Parameter
    * @return     The attributes value
    */
   private Map getAttributes(String str)
   {
      Map attributes = new TreeMap();

      int length = str.length();
      for (int i = 0; i < length; i++)
      {
         char ch = str.charAt(i);

         if (detector.isWhitespace(ch) || ch == '=')
         {
            continue;
         }

         if (ch == '\"' || ch == '\'')
         {
            i = findAttributeEnd(str, i);
            continue;
         }

         int start = i;

         for (++i; i < length; i++)
         {
            ch = str.charAt(i);

            if (detector.isWhitespace(ch))
            {
               break;
            }

            if (ch == '=' || ch == '\"' || ch == '\'')
            {
               break;
            }
         }

         String name = str.substring(start, i);

         // find '='
         while (i < length)
         {
            if (!detector.isWhitespace(ch))
            {
               break;
            }

            ch = str.charAt(++i);
         }

         // no explicit value
         if (ch != '=')
         {
            --i;
            attributes.put(name, "");//$NON-NLS-1$
            continue;
         }

         for (++i; i < length; i++)
         {
            ch = str.charAt(i);

            if (detector.isWhitespace(ch))
            {
               continue;
            }

            if (ch == '=')
            {
               break;
            }

            if (ch == '\"' || ch == '\'')
            {
               start = i + 1;
               i = findAttributeEnd(str, i);
               attributes.put(name, str.substring(start, i));
            }
            else
            {
               --i;
               attributes.put(name, "");//$NON-NLS-1$
            }

            break;
         }
      }

      return attributes;
   }


   /**
    * Description of the Method
    *
    * @param in               Description of the Parameter
    * @return                 Description of the Return Value
    * @exception IOException  Description of the Exception
    */
   private String nextDirective(InputStream in)
      throws IOException
   {
      int ch;

      // <%@
      ch = in.read();
      while (true)
      {
         if (ch < 0)
         {
            return null;
         }

         if (ch == '<')
         {
            ch = in.read();

            if (ch == '%')
            {
               ch = in.read();

               if (ch == '@')
               {
                  break;
               }
            }
         }
         else
         {
            ch = in.read();
         }
      }

      StringBuffer buf = new StringBuffer(512);

      // %>
      ch = in.read();
      while (true)
      {
         if (ch < 0)
         {
            return buf.toString();
         }

         if (ch == '%')
         {
            ch = in.read();

            if (ch == '>')
            {
               return buf.toString();
            }

            buf.append('%');
         }
         else
         {
            buf.append((char) ch);
            ch = in.read();
         }
      }
   }


   /**
    * Description of the Method
    *
    * @param str    Description of the Parameter
    * @param start  Description of the Parameter
    * @return       Description of the Return Value
    */
   private static int findAttributeEnd(String str, int start)
   {
      char quotes = str.charAt(start);
      int length = str.length();

      int end;

      for (end = start + 1; end < length; end++)
      {
         char ch = str.charAt(end);
         if (ch == quotes)
         {
            break;
         }

         // escape sequence
         if (ch == '\\')
         {
            ++end;
         }
      }

      return Math.min(end, length);
   }
}
