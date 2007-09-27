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
package org.jboss.ide.eclipse.jdt.xml.ui.outline.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerFilter;

/*
 * This file contains materials derived from the
 * XMen project. License can be found at :
 * http://www.eclipse.org/legal/cpl-v10.html
 */
/**
 *	This class is designed to simplify applying and removing filters from a
 *	structured viewer.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class ElementFilterAction extends Action
{
   private ViewerFilter fFilter;

   private StructuredViewer fViewer;

   /**
    *Constructor for the ElementFilterAction object
    *
    * @param viewer  Description of the Parameter
    * @param filter  Description of the Parameter
    * @param text    Description of the Parameter
    */
   public ElementFilterAction(StructuredViewer viewer, ViewerFilter filter, String text)
   {
      super(text, AS_CHECK_BOX);
      fViewer = viewer;
      fFilter = filter;
   }

   /** Main processing method for the ElementFilterAction object */
   public void run()
   {
      if (isChecked())
      {
         fViewer.addFilter(fFilter);
      }
      else
      {
         fViewer.removeFilter(fFilter);
      }
   }

}
