/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.dialogs;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.jboss.ide.eclipse.jbosscache.ICacheConstants;
import org.jboss.ide.eclipse.jbosscache.JBossCachePlugin;
import org.jboss.ide.eclipse.jbosscache.dialogs.CViewDialogContentProvider.DialogViewModel;
import org.jboss.ide.eclipse.jbosscache.dialogs.CViewDialogContentProvider.ParentModel;

/**
 * Label provider for <code>ContentViewDialog</code>
 * @author Owner
 * @see org.jboss.ide.eclipse.jbosscache.dialogs.ContentViewDialog
 */
public class CViewDialogLabelProvider implements ITableLabelProvider
{

   public Image getColumnImage(Object element, int columnIndex)
   {
      switch (columnIndex)
      {

         case 0 :
            if (element instanceof ParentModel)
            {
               return JBossCachePlugin.getDefault().getImageRegistry().get(ICacheConstants.IMAGE_KEY_CLASS_OBJ);
            }
            else if (element instanceof DialogViewModel)
            {
               return JBossCachePlugin.getDefault().getImageRegistry().get(ICacheConstants.IMAGE_KEY_FIELD_PUBLIC_OBJ);
            }
            else
               return null;
         case 1 :
         default :
            return null;
      }
   }

   public String getColumnText(Object element, int columnIndex)
   {
      switch (columnIndex)
      {

         case 0 :
            if (element instanceof ParentModel)
            {
               ParentModel parentModel = (ParentModel) element;
               return parentModel.getName();
            }
            else if (element instanceof DialogViewModel)
            {
               DialogViewModel dialogModel = (DialogViewModel) element;
               return dialogModel.getFieldName();
            }
            else
               return "";

         case 1 :
            if (element instanceof ParentModel)
            {
               return "";
            }
            else if (element instanceof DialogViewModel)
            {
               DialogViewModel dialogModel = (DialogViewModel) element;
               return dialogModel.getFieldValue();
            }
            else
               return "";
         default :
            return "";
      }
   }

   public void addListener(ILabelProviderListener listener)
   {
      // TODO Auto-generated method stub

   }

   public void dispose()
   {
      // TODO Auto-generated method stub

   }

   public boolean isLabelProperty(Object element, String property)
   {
      // TODO Auto-generated method stub
      return false;
   }

   public void removeListener(ILabelProviderListener listener)
   {
      // TODO Auto-generated method stub

   }

}
