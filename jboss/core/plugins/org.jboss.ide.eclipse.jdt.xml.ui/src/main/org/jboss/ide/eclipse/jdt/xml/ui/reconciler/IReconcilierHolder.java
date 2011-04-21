/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.xml.ui.reconciler;

import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Description of the Interface
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public interface IReconcilierHolder extends ITextEditor
{

   /**
    * Sets the reconcilier attribute of the IReconcilierHolder object
    *
    * @param reconcilier  The new reconcilier value
    */
   public void setReconcilier(NodeReconciler reconcilier);


   /**
    * Gets the reconcilier attribute of the IReconcilierHolder object
    *
    * @return   The reconcilier value
    */
   public NodeReconciler getReconcilier();

}
