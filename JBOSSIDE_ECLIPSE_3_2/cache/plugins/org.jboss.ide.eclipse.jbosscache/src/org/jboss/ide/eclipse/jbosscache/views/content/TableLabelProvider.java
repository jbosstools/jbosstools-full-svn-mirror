/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.views.content;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.jboss.ide.eclipse.jbosscache.views.content.TableContentProvider.TableContentModel;

/**
 * It is related with the lable model of table
 * @author Gurkaner
 */
public class TableLabelProvider implements ITableLabelProvider
{

   public Image getColumnImage(Object element, int columnIndex)
   {
      return null;
   }

   public String getColumnText(Object element, int columnIndex)
   {
      TableContentModel model = null;
      if (element instanceof TableContentModel)
      {
         model = (TableContentModel) element;
         String colInfo = null;
         switch (columnIndex)
         {
            case 0 :
               colInfo = model.getParentFqn();
               break;

            case 1 :
               colInfo = model.getFqn();
               break;

            case 2 :
               colInfo = model.getKey().toString();
               break;

            case 3 :
               colInfo = model.getValue().toString();
               break;

            default :
               break;
         }
         return colInfo;
      }
      else
         return null;
   }

   public void addListener(ILabelProviderListener listener)
   {

   }

   public void dispose()
   {

   }

   public boolean isLabelProperty(Object element, String property)
   {
      return false;
   }

   public void removeListener(ILabelProviderListener listener)
   {
   }

}
