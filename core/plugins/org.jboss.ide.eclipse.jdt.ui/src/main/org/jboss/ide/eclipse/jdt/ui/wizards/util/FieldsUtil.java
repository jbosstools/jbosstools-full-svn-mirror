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
   private FieldsUtil()
   {
   }

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
   public static void createSelectionButtonDialogFieldGroupControls(SelectionButtonDialogFieldGroup field,
         Composite composite, int nColumns)
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
   public static void createStringButtonDialogFieldControls(StringButtonDialogField field, Composite composite,
         int nColumns)
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
