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
   public void doubleClicked(ListDialogField field)
   {
   }
}
