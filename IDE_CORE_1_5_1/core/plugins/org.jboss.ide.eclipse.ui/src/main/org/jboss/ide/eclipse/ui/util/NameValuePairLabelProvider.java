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
         case 0 :
            return ((NameValuePair) element).getName();
         case 1 :
            return ((NameValuePair) element).getValue();
         default :
      // Can't happen
      }
      return null;
   }
}
