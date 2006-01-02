/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.xml.ui.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.jboss.ide.eclipse.jdt.xml.ui.JDTXMLUIPlugin;
import org.jboss.ide.eclipse.jdt.xml.ui.reconciler.XMLReconciler;
import org.jboss.ide.eclipse.jdt.xml.ui.text.AbstractDocumentProvider;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class XMLDocumentProvider extends AbstractDocumentProvider
{

   private XMLEditor editor;


   /**
    *Constructor for the XMLDocumentProvider object
    *
    * @param editor  Description of the Parameter
    */
   public XMLDocumentProvider(XMLEditor editor)
   {
      this.editor = editor;
   }


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
      IDocument document = super.createDocument(element);
      if (document != null)
      {
         IDocumentPartitioner partitioner = JDTXMLUIPlugin.getDefault().getXMLTextTools().createXMLPartitioner();

         partitioner.connect(document);
         document.setDocumentPartitioner(partitioner);

         XMLReconciler rec = new XMLReconciler(this.editor);
         this.editor.setReconcilier(rec);
         rec.createTree(document);
         document.addDocumentListener(rec);
      }

      return document;
   }

}
