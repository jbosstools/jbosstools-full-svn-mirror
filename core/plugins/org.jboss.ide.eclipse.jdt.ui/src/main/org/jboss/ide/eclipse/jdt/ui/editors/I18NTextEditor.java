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
