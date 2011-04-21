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
package org.jboss.ide.eclipse.jdt.xml.ui.reconciler;

import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.jboss.ide.eclipse.jdt.xml.ui.editors.XMLDocumentPartitioner;
import org.jboss.ide.eclipse.jdt.xml.ui.outline.XMLOutlinePage;

/*
 * This file contains materials derived from the
 * XMen project. License can be found at :
 * http://www.eclipse.org/legal/cpl-v10.html
 */
/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class XMLReconciler extends NodeReconciler
{

   /**
    *Constructor for the XMLReconciler object
    *
    * @param editor  Description of the Parameter
    */
   public XMLReconciler(ITextEditor editor)
   {
      super(editor);
   }

   /**
    * Gets the positionCategory attribute of the XMLReconciler object
    *
    * @return   The positionCategory value
    */
   protected String getPositionCategory()
   {
      return XMLDocumentPartitioner.CONTENT_TYPES_CATEGORY;
   }

   /** Description of the Method */
   protected void update()
   {
      // Update the outline page
      XMLOutlinePage op = (XMLOutlinePage) this.editor.getAdapter(IContentOutlinePage.class);
      if (op != null && op.getControl() != null && !op.getControl().isDisposed() && op.getControl().isVisible())
      {
         op.update(root);
      }
   }

}
