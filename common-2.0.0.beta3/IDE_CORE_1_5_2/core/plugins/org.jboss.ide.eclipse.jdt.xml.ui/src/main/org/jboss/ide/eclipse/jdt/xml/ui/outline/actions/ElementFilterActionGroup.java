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
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionGroup;
import org.jboss.ide.eclipse.jdt.ui.JDTUIImages;
import org.jboss.ide.eclipse.jdt.xml.ui.text.scanners.XMLPartitionScanner;

/*
 * This file contains materials derived from the
 * XMen project. License can be found at :
 * http://www.eclipse.org/legal/cpl-v10.html
 */
/**
 * This Action Group adds buttons for filtering XML elements and attributes in view parts.
 * The contributed filters are: hide attributes, hide comments, and hide processing
 * instructions.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class ElementFilterActionGroup extends ActionGroup
{
   /** The different viewer filters that can be applied */
   private Action fAttributeFilterAction;

   private Action fCommentFilterAction;

   private Action fProcessorFilterAction;

   /**
    *Constructor for the ElementFilterActionGroup object
    *
    * @param viewer  Description of the Parameter
    */
   public ElementFilterActionGroup(StructuredViewer viewer)
   {
      fAttributeFilterAction = new ElementFilterAction(viewer, new ElementFilter(XMLPartitionScanner.XML_ATTRIBUTE),
            "Hide Attributes");//$NON-NLS-1$
      fCommentFilterAction = new ElementFilterAction(viewer, new ElementFilter(XMLPartitionScanner.XML_COMMENT),
            "Hide Comments");//$NON-NLS-1$
      fProcessorFilterAction = new ElementFilterAction(viewer, new ElementFilter(XMLPartitionScanner.XML_PI),
            "Hide Processing Instructions");//$NON-NLS-1$

      fAttributeFilterAction.setImageDescriptor(JDTUIImages.getImageDescriptor("hideattr.gif"));//$NON-NLS-1$
      fCommentFilterAction.setImageDescriptor(JDTUIImages.getImageDescriptor("hidedecl.gif"));//$NON-NLS-1$
      fProcessorFilterAction.setImageDescriptor(JDTUIImages.getImageDescriptor("hideproc.gif"));//$NON-NLS-1$

      fAttributeFilterAction.setToolTipText("Hide Attributes");//$NON-NLS-1$
      fCommentFilterAction.setToolTipText("Hide Markup Declarations");//$NON-NLS-1$
      fProcessorFilterAction.setToolTipText("Hide Processing Instructions");//$NON-NLS-1$

      fAttributeFilterAction.setChecked(false);
      fCommentFilterAction.setChecked(false);
      fProcessorFilterAction.setChecked(false);
   }

   /**
    * Description of the Method
    *
    * @param actionBars  Description of the Parameter
    */
   public void fillActionBars(IActionBars actionBars)
   {
      IToolBarManager manager = actionBars.getToolBarManager();

      manager.add(fAttributeFilterAction);
      manager.add(fCommentFilterAction);
      manager.add(fProcessorFilterAction);
   }

   /**
    * Description of the Method
    *
    * @param menu  Description of the Parameter
    */
   public void fillContextMenu(IMenuManager menu)
   {
      if (!menu.isEmpty())
      {
         return;
      }
   }

}
