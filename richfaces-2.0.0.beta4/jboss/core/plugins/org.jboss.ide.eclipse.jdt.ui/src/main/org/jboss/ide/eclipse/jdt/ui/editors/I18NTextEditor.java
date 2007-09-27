/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.ui.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.jboss.ide.eclipse.jdt.ui.JDTUIMessages;

/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * Base class for internationalized text editors.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class I18NTextEditor extends TextEditor
{

   /**
    * The <code>I18NTextEditor</code> implementation of this
    * <code>IEditorPart</code> method may be extended by subclasses.
    *
    * @param progressMonitor  the progress monitor for communicating
    *        result state or <code>null</code>
    */
   public void doSave(IProgressMonitor progressMonitor)
   {
      IDocumentProvider p = getDocumentProvider();
      if (p == null)
      {
         return;
      }

      if (p.isDeleted(getEditorInput()))
      {
         if (isSaveAsAllowed())
         {
            performSaveAs(progressMonitor);
         }
         else
         {
            Shell shell = getSite().getShell();

            String title = JDTUIMessages.getString("I18NTextEditor.error.save.deleted.title");//$NON-NLS-1$
            String msg = JDTUIMessages.getString("I18NTextEditor.error.save.deleted.message");//$NON-NLS-1$

            MessageDialog.openError(shell, title, msg);
         }
      }
      else
      {
         updateState(getEditorInput());
         validateState(getEditorInput());
         performSave(false, progressMonitor);
      }
   }


   /**
    * The <code>I18NTextEditor</code> implementation of this
    * <code>IEditorPart</code> method calls <code>performSaveAs</code>.
    *
    * Subclasses may reimplement.
    */
   public void doSaveAs()
   {
      performSaveAs(getProgressMonitor());
   }
}
