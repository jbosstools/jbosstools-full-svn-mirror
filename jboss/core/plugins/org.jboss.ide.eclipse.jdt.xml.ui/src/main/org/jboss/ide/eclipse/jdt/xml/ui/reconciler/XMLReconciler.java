/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
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
