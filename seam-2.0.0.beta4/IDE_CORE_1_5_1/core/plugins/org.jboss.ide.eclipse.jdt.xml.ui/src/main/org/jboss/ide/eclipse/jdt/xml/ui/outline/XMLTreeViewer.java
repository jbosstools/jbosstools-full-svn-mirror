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
package org.jboss.ide.eclipse.jdt.xml.ui.outline;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;
import org.jboss.ide.eclipse.jdt.xml.ui.reconciler.XMLNode;

/*
 * This file contains materials derived from the
 * XMen project. License can be found at :
 * http://www.eclipse.org/legal/cpl-v10.html
 */
/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class XMLTreeViewer extends TreeViewer
{

   /**
    * Indicates an item which has been reused. At the point of
    * its reuse it has been expanded. This field is used to
    * communicate between <code>internalExpandToLevel</code> and
    * <code>reuseTreeItem</code>.
    */
   private Item fReusedExpandedItem;

   /**
    *Constructor for the XMLTreeViewer object
    *
    * @param tree  Description of the Parameter
    */
   public XMLTreeViewer(Tree tree)
   {
      super(tree);
      setAutoExpandLevel(2);
   }

   /**
    * Description of the Method
    *
    * @param delta  Description of the Parameter
    */
   public void reconcile(final XMLNode delta)
   {
      if (getSorter() == null)
      {
         Display d = getTree().getDisplay();
         d.asyncExec(new Runnable()
         {
            public void run()
            {
               /*
                * if (getTree().getData() != null) {
                * update(getTree(), (XMLElementList) getTree().getData());
                * }
                */
               //System.out.println("CCC: found for doc:"+getTree().getData());
               Widget w = findItem(delta);
               if (w != null)
               {
                  update(w, delta);
               }
            }
         });
      }
      else
      {
         refresh();
      }
   }

   /**
    * Description of the Method
    *
    * @param parent   Description of the Parameter
    * @param element  Description of the Parameter
    * @param ix       Description of the Parameter
    */
   protected void createTreeItem(Widget parent, Object element, int ix)
   {
      try
      {
         Item[] children = getChildren(parent);
         boolean expand = (parent instanceof Item && (children == null || children.length == 0));

         Item item;
         if (ix == -1)
         {
            item = newItem(parent, SWT.NULL, children.length);
         }
         else
         {
            item = newItem(parent, SWT.NULL, ix);
         }
         updateItem(item, element);
         updatePlus(item, element);

         if (expand)
         {
            setExpanded((Item) parent, true);
         }

         internalExpandToLevel(item, 0);
      }
      catch (Exception e)
      {
         e.printStackTrace();
         //XMenPlugin.getDefault().error(null, e);
      }
   }

   /**
    * Description of the Method
    *
    * @param parent  Description of the Parameter
    * @param child   Description of the Parameter
    * @return        Description of the Return Value
    */
   protected boolean filtered(XMLNode parent, XMLNode child)
   {
      Object[] result = new Object[]
      {child};
      ViewerFilter[] filters = getFilters();
      for (int i = 0; i < filters.length; i++)
      {
         result = filters[i].filter(this, parent, result);
         if (result.length == 0)
         {
            return true;
         }
      }

      return false;
   }

   /**
    * Description of the Method
    *
    * @param item     Description of the Parameter
    * @param element  Description of the Parameter
    */
   protected void reuseTreeItem(Item item, Object element)
   {
      Item[] c = getChildren(item);
      if (c != null && c.length > 0)
      {

         if (getExpanded(item))
         {
            fReusedExpandedItem = item;
         }

         for (int k = 0; k < c.length; k++)
         {
            if (c[k].getData() != null)
            {
               disassociate(c[k]);
            }
            c[k].dispose();
         }
      }

      setExpanded(item, getExpanded(item));

      updateItem(item, element);
      updatePlus(item, element);
      internalExpandToLevel(item, 0);

      fReusedExpandedItem = null;
   }

   /**
    * Description of the Method
    *
    * @param w      Description of the Parameter
    * @param delta  Description of the Parameter
    */
   protected void update(Widget w, XMLNode delta)
   {
      try
      {
         if (w == null)
         {
            return;
         }

         Object[] o = delta.getChildren(delta);
         if (o == null)
         {
            o = new XMLNode[0];
         }
         XMLNode[] affected = (XMLNode[]) o;
         Item[] children = getChildren(w);

         for (int i = 0; i < children.length; i++)
         {
            Item item = children[i];
            if (i < affected.length)
            {
               XMLNode affectedElement = affected[i];

               if (item != null)
               {
                  if (item.getData() != null)
                  {
                     if ((affectedElement.getLabel(null) != null)
                           && affectedElement.getLabel(null).equals(item.getText()))
                     {
                        item.setData(affectedElement);
                     }
                     else
                     {
                        reuseTreeItem(item, affectedElement);
                     }
                     if (getExpanded(item))
                     {
                        update(item, affectedElement);
                     }
                     else
                     {
                        Item[] c = getChildren(item);
                        if (c.length > 0)
                        {
                           if (c.length != affectedElement.getChildren(affectedElement).length)
                           {
                              reuseTreeItem(item, affectedElement);
                           }
                        }
                     }
                     updatePlus(item, affectedElement);
                  }
                  else
                  {
                     reuseTreeItem(item, affectedElement);
                     updatePlus(item, affectedElement);
                  }
               }
            }
            else
            {
               if (item != null)
               {
                  disassociate(item);
                  item.dispose();
               }
            }
         }
         for (int i = children.length; i < affected.length; i++)
         {
            createTreeItem(w, affected[i], -1);
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
         //XMenPlugin.getDefault().error(null, e);
      }
   }

}
