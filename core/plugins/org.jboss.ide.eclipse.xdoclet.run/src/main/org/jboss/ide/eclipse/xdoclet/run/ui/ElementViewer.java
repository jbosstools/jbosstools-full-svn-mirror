/*
 * JBoss, a division of Red Hat
 * Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
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
package org.jboss.ide.eclipse.xdoclet.run.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.jboss.ide.eclipse.ui.util.StringViewSorter;
import org.jboss.ide.eclipse.xdoclet.run.XDocletRunMessages;
import org.jboss.ide.eclipse.xdoclet.run.model.XDocletAttribute;
import org.jboss.ide.eclipse.xdoclet.run.model.XDocletElement;
import org.jboss.ide.eclipse.xdoclet.run.ui.dialogs.AttributeEditDialog;
import org.jboss.ide.eclipse.xdoclet.run.util.ElementContentProvider;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 * @todo      Javadoc to complete
 */
public class ElementViewer
{
   /** Description of the Field */
   private XDocletElement element;

   /** Description of the Field */
   private Composite parentComposite;

   /** Description of the Field */
   private CheckboxTableViewer viewer;

   /**
    *Constructor for the ElementViewer object
    *
    * @param parent  Description of the Parameter
    */
   public ElementViewer(Composite parent)
   {
      this.parentComposite = parent;

      Table table = new Table(parent, SWT.CHECK | SWT.BORDER | SWT.FULL_SELECTION);
      table.setLayoutData(new GridData(GridData.FILL_BOTH));

      TableLayout tableLayout = new TableLayout();
      tableLayout.addColumnData(new ColumnWeightData(100));
      tableLayout.addColumnData(new ColumnWeightData(100));
      table.setLayout(tableLayout);

      TableColumn column1 = new TableColumn(table, SWT.NONE);
      column1.setText(XDocletRunMessages.getString("ElementViewer.column.property"));//$NON-NLS-1$
      TableColumn column2 = new TableColumn(table, SWT.NONE);
      column2.setText(XDocletRunMessages.getString("ElementViewer.column.value"));//$NON-NLS-1$

      this.viewer = new CheckboxTableViewer(table);
      table.setHeaderVisible(true);
      table.setLinesVisible(true);

      this.viewer.setLabelProvider(new LabelProviderImpl());
      this.viewer.setContentProvider(new ElementContentProvider());
      this.viewer.setSorter(new StringViewSorter());

      this.viewer.addDoubleClickListener(new IDoubleClickListener()
      {
         public void doubleClick(DoubleClickEvent event)
         {
            IStructuredSelection selection = (IStructuredSelection) event.getSelection();
            if (!selection.isEmpty())
            {
               XDocletAttribute attribute = (XDocletAttribute) selection.getFirstElement();
               AttributeEditDialog dialog = new AttributeEditDialog(parentComposite.getShell(), attribute);
               dialog.open();
               viewer.refresh();
               viewer.setCheckedElements(getUsedAttributes().toArray());
            }
         }
      });

      this.viewer.addCheckStateListener(new ICheckStateListener()
      {
         public void checkStateChanged(CheckStateChangedEvent event)
         {
            XDocletAttribute attribute = (XDocletAttribute) event.getElement();
            attribute.setUsed(event.getChecked());
         }
      });
   }

   /**
    * Sets the element attribute of the ElementPropertiesViewer object
    *
    * @param element  The new element value
    */
   public void setElement(XDocletElement element)
   {
      this.element = element;
      this.viewer.setInput(this.element);

      if (this.element != null)
      {
         this.viewer.setCheckedElements(this.getUsedAttributes().toArray());
      }
   }

   /**
    * Gets the usedAttributes attribute of the XDocletElement object
    *
    * @return   The usedAttributes value
    */
   private Collection getUsedAttributes()
   {
      Iterator iterator = this.element.getAttributes().iterator();
      ArrayList result = new ArrayList();
      while (iterator.hasNext())
      {
         XDocletAttribute attribute = (XDocletAttribute) iterator.next();
         if (attribute.isUsed())
         {
            result.add(attribute);
         }
      }
      return result;
   }

   /**
    * Description of the Class
    *
    * @author    letiembl
    * @version   $Revision$
    * @created   18 mars 2003
    */
   private class LabelProviderImpl extends LabelProvider implements ITableLabelProvider
   {
      /**
       * Gets the columnImage attribute of the TableLabelProviderImpl object
       *
       * @param element      Description of the Parameter
       * @param columnIndex  Description of the Parameter
       * @return             The columnImage value
       * @see                org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
       */
      public Image getColumnImage(Object element, int columnIndex)
      {
         return null;
      }

      /**
       * Gets the columnText attribute of the TableLabelProviderImpl object
       *
       * @param element      Description of the Parameter
       * @param columnIndex  Description of the Parameter
       * @return             The columnText value
       * @see                org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
       */
      public String getColumnText(Object element, int columnIndex)
      {
         switch (columnIndex)
         {
            case 0 :
               return ((XDocletAttribute) element).getName();
            case 1 :
               return ((XDocletAttribute) element).getValue();
            default :
         // Can't happen
         }
         return null;
      }
   }
}
