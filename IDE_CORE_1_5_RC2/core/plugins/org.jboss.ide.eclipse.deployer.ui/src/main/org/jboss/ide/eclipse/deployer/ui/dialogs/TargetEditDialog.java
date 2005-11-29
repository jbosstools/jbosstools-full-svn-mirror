/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.deployer.ui.dialogs;

import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public abstract class TargetEditDialog extends StatusDialog
{
   /**
    *Constructor for the TargetEditDialog object
    *
    * @param shell  Description of the Parameter
    */
   protected TargetEditDialog(Shell shell)
   {
      super(shell);
   }


   /** Description of the Method */
   protected abstract void validate();
}
