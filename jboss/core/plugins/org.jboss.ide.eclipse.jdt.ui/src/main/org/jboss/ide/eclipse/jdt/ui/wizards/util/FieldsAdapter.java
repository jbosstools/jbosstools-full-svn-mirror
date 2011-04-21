/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.ui.wizards.util;

import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IStringButtonAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ListDialogField;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class FieldsAdapter implements IStringButtonAdapter, IDialogFieldListener
{
   private final FieldsAdapterListener page;


   /**
    *Constructor for the FieldsAdapter object
    *
    * @param page  Description of the Parameter
    */
   public FieldsAdapter(FieldsAdapterListener page)
   {
      this.page = page;
   }


   /**
    * Description of the Method
    *
    * @param field  Description of the Parameter
    */
   public void changeControlPressed(DialogField field)
   {
      page.pageChangeControlPressed(field);
   }


   /**
    * Description of the Method
    *
    * @param field  Description of the Parameter
    */
   public void dialogFieldChanged(DialogField field)
   {
      page.pageDialogFieldChanged(field);
   }


   /**
    * Description of the Method
    *
    * @param field  Description of the Parameter
    */
   public void doubleClicked(ListDialogField field) { }
}
