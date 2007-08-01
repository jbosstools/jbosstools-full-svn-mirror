/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.ui.wizards.util;

import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogFieldGroup;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * Utility class for wizard field creation
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class FieldsUtil
{
   /** Avoid instantiation */
   private FieldsUtil() { }


   /**
    * Description of the Method
    *
    * @param text       Description of the Parameter
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   public static void createLabelControls(String text, Composite composite, int nColumns)
   {
      Label labelControl = new Label(composite, SWT.NONE);
      labelControl.setText(text);
      LayoutUtil.setHorizontalSpan(labelControl, nColumns);
   }


   /**
    * Description of the Method
    *
    * @param field      Description of the Parameter
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   public static void createSelectionButtonDialogFieldGroupControls(SelectionButtonDialogFieldGroup field, Composite composite, int nColumns)
   {
      Control labelControl = field.getLabelControl(composite);
      LayoutUtil.setHorizontalSpan(labelControl, nColumns);

      DialogField.createEmptySpace(composite);

      Control buttonGroup = field.getSelectionButtonsGroup(composite);
      LayoutUtil.setHorizontalSpan(buttonGroup, nColumns - 1);
   }


   /**
    * Description of the Method
    *
    * @param field      Description of the Parameter
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   public static void createStringButtonDialogFieldControls(StringButtonDialogField field, Composite composite, int nColumns)
   {
      field.doFillIntoGrid(composite, nColumns);
      LayoutUtil.setHorizontalGrabbing(field.getTextControl(null));
   }


   /**
    * Description of the Method
    *
    * @param field      Description of the Parameter
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   public static void createStringDialogFieldControls(StringDialogField field, Composite composite, int nColumns)
   {
      field.doFillIntoGrid(composite, nColumns - 1);
      DialogField.createEmptySpace(composite);
      LayoutUtil.setHorizontalGrabbing(field.getTextControl(null));
   }
}
