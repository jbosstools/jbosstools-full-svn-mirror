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

import org.eclipse.jface.util.ListenerList;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.part.Page;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.jboss.ide.eclipse.jdt.xml.ui.editors.XMLEditor;
import org.jboss.ide.eclipse.jdt.xml.ui.outline.actions.ElementFilterActionGroup;
import org.jboss.ide.eclipse.jdt.xml.ui.reconciler.XMLNode;

/*
 * This file contains materials derived from the
 * XMen project. License can be found at :
 * http://www.eclipse.org/legal/cpl-v10.html
 */
/**
 * The xml outline page.  Note that we don't subclass the abstract
 * <code>ContentOutlinePage</code> because we use our own particular
 * tree viewer.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class XMLOutlinePage extends Page implements IContentOutlinePage
{
   private XMLEditor editor;

   private ElementFilterActionGroup filterActionGroup;

   private ListenerList selectionChangedListeners = new ListenerList();

   private XMLTreeViewer treeViewer;

   /**
    *Constructor for the XMLOutlinePage object
    *
    * @param editor  Description of the Parameter
    */
   public XMLOutlinePage(XMLEditor editor)
   {
      this.editor = editor;
   }

   /**
    * Adds a feature to the SelectionChangedListener attribute of the XMLOutlinePage object
    *
    * @param listener  The feature to be added to the SelectionChangedListener attribute
    */
   public void addSelectionChangedListener(ISelectionChangedListener listener)
   {
      if (treeViewer != null)
      {
         treeViewer.addPostSelectionChangedListener(listener);
      }
      else
      {
         selectionChangedListeners.add(listener);
      }
   }

   /**
    * Description of the Method
    *
    * @param parent  Description of the Parameter
    */
   public void createControl(Composite parent)
   {
      // Create the tree
      Tree tree = new Tree(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);

      // Create the tree viewer
      treeViewer = new XMLTreeViewer(tree);
      treeViewer.setContentProvider(new WorkbenchContentProvider());
      treeViewer.setLabelProvider(new XMLLabelProvider());
      treeViewer.setInput(editor.getReconcilier().getRoot());

      // Setup the selection listeners
      Object[] listeners = selectionChangedListeners.getListeners();
      for (int i = 0; i < listeners.length; i++)
      {
         selectionChangedListeners.remove(listeners[i]);
         treeViewer.addPostSelectionChangedListener((ISelectionChangedListener) listeners[i]);
      }
      getSite().setSelectionProvider(treeViewer);

      // Create the various action groups
      // filterActionGroup = new ElementFilterActionGroup(treeViewer);
      // filterActionGroup.fillActionBars(getSite().getActionBars());
   }

   /**
    * Gets the control attribute of the XMLOutlinePage object
    *
    * @return   The control value
    */
   public Control getControl()
   {
      if (treeViewer != null)
      {
         return treeViewer.getControl();
      }
      return null;
   }

   /**
    * Gets the selection attribute of the XMLOutlinePage object
    *
    * @return   The selection value
    */
   public ISelection getSelection()
   {
      if (treeViewer == null)
      {
         return StructuredSelection.EMPTY;
      }
      return treeViewer.getSelection();
   }

   /**
    * Description of the Method
    *
    * @param listener  Description of the Parameter
    */
   public void removeSelectionChangedListener(ISelectionChangedListener listener)
   {
      if (treeViewer != null)
      {
         treeViewer.removePostSelectionChangedListener(listener);
      }
      else
      {
         selectionChangedListeners.remove(listener);
      }
   }

   /** Sets the focus attribute of the XMLOutlinePage object */
   public void setFocus()
   {
      if (treeViewer != null)
      {
         treeViewer.getControl().setFocus();
      }
   }

   /**
    * Sets the selection attribute of the XMLOutlinePage object
    *
    * @param selection  The new selection value
    */
   public void setSelection(ISelection selection)
   {
      if (treeViewer != null)
      {
         treeViewer.setSelection(selection);
      }
   }

   /**
    * Description of the Method
    *
    * @param root  Description of the Parameter
    */
   public void update(XMLNode root)
   {
      if (treeViewer.getInput() != root)
      {
         System.out.println("setInput!");//$NON-NLS-1$
         treeViewer.setInput(root);
      }
      treeViewer.reconcile(root);
      treeViewer.getControl().redraw();
   }

}
