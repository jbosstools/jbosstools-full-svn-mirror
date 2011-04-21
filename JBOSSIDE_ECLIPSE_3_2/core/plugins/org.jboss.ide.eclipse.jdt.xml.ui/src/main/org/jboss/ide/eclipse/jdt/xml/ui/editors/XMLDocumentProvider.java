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
   protected IDocument createDocument(Object element) throws CoreException
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
