/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.ui.util;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.jboss.ide.eclipse.core.util.NameValuePair;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class NameValuePairLabelProvider extends LabelProvider implements ITableLabelProvider
{
   /**Constructor for the NameValuePairLabelProvider object */
   public NameValuePairLabelProvider()
   {
      super();
   }


   /**
    * Gets the columnImage attribute of the NameValuePairLabelProvider object
    *
    * @param element      Description of the Parameter
    * @param columnIndex  Description of the Parameter
    * @return             The columnImage value
    */
   public Image getColumnImage(Object element, int columnIndex)
   {
      return null;
   }


   /**
    * Gets the columnText attribute of the NameValuePairLabelProvider object
    *
    * @param element      Description of the Parameter
    * @param columnIndex  Description of the Parameter
    * @return             The columnText value
    */
   public String getColumnText(Object element, int columnIndex)
   {
      switch (columnIndex)
      {
         case 0:
            return ((NameValuePair) element).getName();
         case 1:
            return ((NameValuePair) element).getValue();
         default:
         // Can't happen
      }
      return null;
   }
}
