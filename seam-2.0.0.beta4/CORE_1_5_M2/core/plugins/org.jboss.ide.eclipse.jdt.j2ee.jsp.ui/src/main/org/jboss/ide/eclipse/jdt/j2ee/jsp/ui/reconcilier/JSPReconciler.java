/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.reconcilier;

import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.Position;
import org.eclipse.ui.texteditor.ITextEditor;
import org.jboss.ide.eclipse.jdt.ui.text.rules.MultiViewTranslator;
import org.jboss.ide.eclipse.jdt.xml.ui.reconciler.XMLReconciler;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPReconciler extends XMLReconciler
{
   private MultiViewTranslator translator = new MultiViewTranslator();


   /**
    *Constructor for the JSPReconciler object
    *
    * @param editor  Description of the Parameter
    */
   public JSPReconciler(ITextEditor editor)
   {
      super(editor);
   }


   /**
    * Gets the positions attribute of the JSPReconciler object
    *
    * @return                                  The positions value
    * @exception BadPositionCategoryException  Description of the Exception
    */
   protected Position[] getPositions()
      throws BadPositionCategoryException
   {
      return this.translator.getPositions(document, this.getPositionCategory());
   }


   /** Description of the Method */
   protected void update()
   {
      // Do nothing
   }

}
