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
package org.jboss.ide.eclipse.xdoclet.assist.ui;

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TreeItem;
import org.jboss.ide.eclipse.xdoclet.assist.XDocletAssistMessages;
import org.jboss.ide.eclipse.xdoclet.assist.model.DocletElement;
import org.jboss.ide.eclipse.xdoclet.assist.model.TemplateElement;
import org.jboss.ide.eclipse.xdoclet.assist.model.TemplateList;
import org.jboss.ide.eclipse.xdoclet.assist.model.TemplateTree;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class TemplateListViewer extends Composite
{
   /** Description of the Field */
   public Action fAddAction, fAddTreeAction, fRemoveTreeAction, fReplaceAction, fRemoveAction, fCopyAction,
         fRenameAction;

   /** Description of the Field */
   protected AddProposalListener addProposalListener = null;

   /** Description of the Field */
   protected Menu contextMenu;//$NON-NLS-1$

   /** Description of the Field */
   protected TreeViewer fViewer;

   /** Description of the Field */
   protected CompletionProposalPopup popup;

   /** Description of the Field */
   protected ReplaceProposalListener replaceProposalListener = null;

   /** Description of the Field */
   protected final static String CONTEXT_TREE_NAME = XDocletAssistMessages.getString(XDocletAssistMessages
         .getString("TemplateListViewer.TemplateListViewer.Contexts_1_1"));//$NON-NLS-1$

   /**
    * Constructor for TemplateTreeViewer.
    *
    * @param parent
    * @param style
    * @param templateList  Description of the Parameter
    */
   public TemplateListViewer(TemplateList templateList, Composite parent, int style)
   {
      super(parent, style);
      if (templateList == null)
      {
         throw new IllegalArgumentException();
      }
      init(templateList);
   }

   /**
    * Description of the Method
    *
    * @param item  Description of the Parameter
    * @return      Description of the Return Value
    */
   public Object[] computeProposals(TreeItem item)
   {
      if (item == null)
      {
         return new Object[0];
      }
      if (!(item.getData() instanceof TemplateTree))
      {
         return findTemplateTree(item).computeChildren(item.getData(), true);
      }
      return findTemplateTree(item).computeChildren(null, true);
   }

   /**
    * Description of the Method
    *
    * @param manager  Description of the Parameter
    */
   public void fillContextMenu(IMenuManager manager)
   {
      Object selected = getSelectedElement();
      if (selected instanceof TemplateTree)
      {
         fillTemplateTreeContextMenu(manager);
      }//		else if (selected instanceof ContextTree) {
      //			fillContextTreeContextMenu(manager);
      //		}
      else if (selected != null)
      {
         fillOtherThanSubTreesContextMenu(manager);
      }
      else
      {
         fillNoSelection(manager);
      }
   }

   /**
    * Description of the Method
    *
    * @param item  Description of the Parameter
    * @return      Description of the Return Value
    */
   public TemplateTree findTemplateTree(TreeItem item)
   {
      while (item.getParentItem() != null)
      {
         item = item.getParentItem();
      }
      return (TemplateTree) item.getData();
   }

   /**
    * Gets the contextMenu attribute of the TemplateListViewer object
    *
    * @return   The contextMenu value
    */
   public Menu getContextMenu()
   {
      return contextMenu;
   }

   /**
    * Gets the popup attribute of the TemplateListViewer object
    *
    * @return   The popup value
    */
   public CompletionProposalPopup getPopup()
   {
      if (popup == null)
      {
         popup = new CompletionProposalPopup(fViewer.getControl().getShell());
      }
      return popup;
   }

   /**
    * Returns the viewer.
    *
    * @return   TreeViewer
    */
   public TreeViewer getViewer()
   {
      return fViewer;
   }

   /**
    * Adds a feature to the Elements attribute of the TemplateListViewer object
    *
    * @param objects                  The feature to be added to the Elements attribute
    * @param templateElement          The feature to be added to the Elements attribute
    * @param docletElements           The feature to be added to the Elements attribute
    * @param onlyNotExistingElements  The feature to be added to the Elements attribute
    */
   protected void addElements(ArrayList objects, TemplateElement templateElement, DocletElement[] docletElements,
         boolean onlyNotExistingElements)
   {
      for (int i = 0; i < docletElements.length; i++)
      {
         if (!onlyNotExistingElements || templateElement.getChild(docletElements[i].getName()) == null)
         {
            objects.add(docletElements[i]);
         }
      }
   }

   /** Description of the Method */
   protected void doAdd()
   {
      getPopup().removeAllProposalListeners();
      getPopup().addProposalListener(getAddProposalListener());
      Object[] proposals = computeProposals(getSelectedItem());
      showPopup(getSelectedElement(), proposals);
   }

   /** Description of the Method */
   protected void doReplace()
   {
      getPopup().removeAllProposalListeners();
      getPopup().addProposalListener(getReplaceProposalListener());
      TreeItem selectedItem = fViewer.getTree().getSelection()[0];
      Object[] proposals = computeProposals(selectedItem.getParentItem());
      showPopup(selectedItem.getParentItem().getData(), proposals);
   }

   /**
    * Gets the addProposalListener attribute of the TemplateListViewer object
    *
    * @return   The addProposalListener value
    */
   protected AddProposalListener getAddProposalListener()
   {
      if (addProposalListener == null)
      {
         addProposalListener = new AddProposalListener();
      }
      return addProposalListener;
   }

   /**
    * Gets the replaceProposalListener attribute of the TemplateListViewer object
    *
    * @return   The replaceProposalListener value
    */
   protected ReplaceProposalListener getReplaceProposalListener()
   {
      if (replaceProposalListener == null)
      {
         replaceProposalListener = new ReplaceProposalListener();
      }
      return replaceProposalListener;
   }

   /**
    * Gets the selectedElement attribute of the TemplateListViewer object
    *
    * @return   The selectedElement value
    */
   protected Object getSelectedElement()
   {
      IStructuredSelection selection = (IStructuredSelection) fViewer.getSelection();
      if (selection.isEmpty())
      {
         return null;
      }
      return selection.getFirstElement();
   }

   /**
    * Gets the selectedItem attribute of the TemplateListViewer object
    *
    * @return   The selectedItem value
    */
   protected TreeItem getSelectedItem()
   {
      if (fViewer.getTree().getSelectionCount() > 0)
      {
         return fViewer.getTree().getSelection()[0];
      }
      return null;
   }

   /**
    * Gets the templateList attribute of the TemplateListViewer object
    *
    * @return   The templateList value
    */
   protected TemplateList getTemplateList()
   {
      return ((TemplateListContentProvider) fViewer.getContentProvider()).getTemplateList();
   }

   /**
    * Description of the Method
    *
    * @param templateList  Description of the Parameter
    */
   protected void init(TemplateList templateList)
   {
      FillLayout layout = new FillLayout();
      this.setLayout(layout);
      fViewer = new TreeViewer(this, SWT.SINGLE);
      fViewer.setLabelProvider(new LabelProvider());
      fViewer.setSorter(new NameSorter());
      fViewer.setContentProvider(new TemplateListContentProvider());
      fViewer.setInput(templateList);
      fViewer.setLabelProvider(new LocalLableProvider());
      fViewer.getTree().addMouseListener(new MouseAdapter()
      {
         public void mouseDoubleClick(MouseEvent e)
         {
            doAdd();
         }
      });
      fViewer.setAutoExpandLevel(AbstractTreeViewer.ALL_LEVELS);
      //
      makeActions();
      initContextMenu();
   }

   /**
    * Description of the Method
    *
    * @param item  Description of the Parameter
    * @return      Description of the Return Value
    */
   protected Point locationForPopup(TreeItem item)
   {
      int x = item.getBounds().x;
      int y = item.getBounds().y + item.getBounds().height;
      return fViewer.getTree().toDisplay(new Point(x, y));
   }

   /** Sets the actionEnablement attribute of the TemplateListViewer object */
   protected void setActionEnablement()
   {
      if (getSelectedItem() == null)
      {
         return;
      }
      Object[] proposals = computeProposals(getSelectedItem());
      fAddAction.setEnabled(proposals.length > 0);
      if (getSelectedItem().getParentItem() != null)
      {
         fReplaceAction.setEnabled(computeProposals(getSelectedItem().getParentItem()).length > 0);
      }
   }

   /**
    * Description of the Method
    *
    * @param selected   Description of the Parameter
    * @param proposals  Description of the Parameter
    */
   protected void showPopup(Object selected, Object[] proposals)
   {
      if (proposals.length == 0)
      {
         return;
      }
      ViewerSorter sorter = null;
      if (selected instanceof TemplateTree)
      {
         sorter = new NameSorter();
      }
      Point location = locationForPopup(fViewer.getTree().getSelection()[0]);
      getPopup().showFreshPopup(proposals, null, location, sorter);
   }

   /** Description of the Method */
   private void doAddTree()
   {
      TemplateTree tree = getTemplateList().addTemplate(getTemplateList().getNewName(), null);
      TreeNameDialog fTreeNameDialog = new TreeNameDialog(fViewer.getControl().getShell(), tree, getTemplateList());
      fTreeNameDialog.open();
      fViewer.refresh(false);
   }

   /** Description of the Method */
   private void doRemove()
   {
      TemplateTree tree = findTemplateTree(getSelectedItem());
      tree.changeTemplateTree(getSelectedElement(), true);
      fViewer.refresh(tree, false);
   }

   /** Description of the Method */
   private void doRemoveTree()
   {
      getTemplateList().deleteTemplate(((TemplateTree) getSelectedElement()).getName());
   }

   /** Description of the Method */
   private void doRename()
   {
      TemplateTree tree = (TemplateTree) getSelectedElement();
      TreeNameDialog fTreeNameDialog = new TreeNameDialog(fViewer.getControl().getShell(), tree, getTemplateList());
      fTreeNameDialog.open();
      fViewer.refresh(false);
   }

   //   private void fillContextTreeContextMenu(IMenuManager manager)
   //   {
   //      manager.add(fAddAction);
   //      // Other plug-ins can contribute there actions here
   //      manager.add(new Separator("Additions"));//$NON-NLS-1$
   //   }

   /**
    * Description of the Method
    *
    * @param manager  Description of the Parameter
    */
   private void fillNoSelection(IMenuManager manager)
   {
      manager.add(fAddTreeAction);
      // Other plug-ins can contribute there actions here
      manager.add(new Separator("Additions"));//$NON-NLS-1$
   }

   /**
    * Description of the Method
    *
    * @param manager  Description of the Parameter
    */
   private void fillOtherThanSubTreesContextMenu(IMenuManager manager)
   {
      manager.add(fAddAction);
      manager.add(fReplaceAction);
      manager.add(fRemoveAction);
      // Other plug-ins can contribute there actions here
      manager.add(new Separator("Additions"));//$NON-NLS-1$
   }

   /**
    * Description of the Method
    *
    * @param manager  Description of the Parameter
    */
   private void fillTemplateTreeContextMenu(IMenuManager manager)
   {
      manager.add(fAddAction);
      manager.add(fRemoveTreeAction);
      manager.add(fRenameAction);
      manager.add(new Separator(XDocletAssistMessages.getString("TemplateListViewer.AddTree_2")));//$NON-NLS-1$
      manager.add(fAddTreeAction);
      // Other plug-ins can contribute there actions here
      manager.add(new Separator("Additions"));//$NON-NLS-1$
   }

   /** Description of the Method */
   private void initContextMenu()
   {
      MenuManager menuManager = new MenuManager("#PopupMenu");//$NON-NLS-1$
      menuManager.setRemoveAllWhenShown(true);
      menuManager.addMenuListener(new IMenuListener()
      {
         public void menuAboutToShow(IMenuManager manager)
         {
            setActionEnablement();
            TemplateListViewer.this.fillContextMenu(manager);
         }
      });
      contextMenu = menuManager.createContextMenu(fViewer.getControl());
      fViewer.getControl().setMenu(contextMenu);
      // getSite().registerContextMenu(menuMgr, fViewer);
   }

   /** Description of the Method */
   private void makeActions()
   {
      fAddAction = new Action()
      {
         public void run()
         {
            doAdd();
         }
      };
      fAddAction.setText(XDocletAssistMessages.getString("TemplateListViewer.Add_3"));//$NON-NLS-1$
      fAddAction.setToolTipText(XDocletAssistMessages.getString("TemplateListViewer.Add_4"));//$NON-NLS-1$
      //		fAddAction.setImageDescriptor(
      //			XDocletPluginImages.DESC_TEMPLATE_ADD_ACTION);
      fAddAction.setEnabled(true);

      fReplaceAction = new Action()
      {
         public void run()
         {
            doReplace();
         }
      };
      fReplaceAction.setText(XDocletAssistMessages.getString("TemplateListViewer.Replace_5"));//$NON-NLS-1$
      fReplaceAction.setToolTipText(XDocletAssistMessages.getString("TemplateListViewer.Replace_6"));//$NON-NLS-1$
      //		fReplaceAction.setImageDescriptor(
      //			XDocletPluginImages.DESC_TEMPLATE_REPLACE_ACTION);
      fReplaceAction.setEnabled(true);

      fRemoveAction = new Action()
      {
         public void run()
         {
            doRemove();
         }
      };
      fRemoveAction.setText(XDocletAssistMessages.getString("TemplateListViewer.Remove_7"));//$NON-NLS-1$
      fRemoveAction.setToolTipText(XDocletAssistMessages.getString("TemplateListViewer.Remove_8"));//$NON-NLS-1$
      fRemoveAction.setEnabled(true);
      //		fRemoveAction.setImageDescriptor(
      //			XDocletPluginImages.DESC_TEMPLATE_REMOVE_ACTION);

      //		fCopyAction = new Action() {
      //			public void run() {
      //				doCopy();
      //			}
      //
      //		};
      //		fCopyAction.setText("Copy");
      //		fCopyAction.setToolTipText("Copy");
      //		fCopyAction.setEnabled(true);
      //		fCopyAction.setImageDescriptor(
      //			XDocletPluginImages.DESC_TEMPLATETREE_COPY_ACTION);

      fRenameAction = new Action()
      {
         public void run()
         {
            doRename();
         }
      };
      fRenameAction.setText(XDocletAssistMessages.getString("TemplateListViewer.Rename_9"));//$NON-NLS-1$
      fRenameAction.setToolTipText(XDocletAssistMessages.getString("TemplateListViewer.Rename_10"));//$NON-NLS-1$
      fRenameAction.setEnabled(true);
      //		fRenameAction.setImageDescriptor(
      //			XDocletPluginImages.DESC_TEMPLATETREE_RENAME_ACTION);

      fAddTreeAction = new Action()
      {
         public void run()
         {
            doAddTree();
         }
      };
      fAddTreeAction.setText(XDocletAssistMessages.getString("TemplateListViewer.Add_Tree_11"));//$NON-NLS-1$
      fAddTreeAction.setToolTipText(XDocletAssistMessages.getString("TemplateListViewer.Add_Tree_12"));//$NON-NLS-1$
      fAddTreeAction.setEnabled(true);
      //		fAddTreeAction.setImageDescriptor(
      //			XDocletPluginImages.DESC_TEMPLATETREE_ADD_TREE_ACTION);
      fRemoveTreeAction = new Action()
      {
         public void run()
         {
            doRemoveTree();
         }
      };
      fRemoveTreeAction.setText(XDocletAssistMessages.getString("TemplateListViewer.Remove_13"));//$NON-NLS-1$
      fRemoveTreeAction.setToolTipText(XDocletAssistMessages.getString("TemplateListViewer.Remove_14"));//$NON-NLS-1$
      fRemoveTreeAction.setEnabled(true);
   }

   /**
    * Description of the Class
    *
    * @author    Administrator
    * @version   $Revision$
    * @created   17 mai 2003
    */
   protected class AddProposalListener implements IProposalListener
   {
      /** Description of the Method */
      public void closedWithoutProposals()
      {
      }

      /**
       * Description of the Method
       *
       * @param event  Description of the Parameter
       */
      public void handleProposal(ProposalEvent event)
      {
         Object[] proposals = event.getProposals();
         TreeItem selectedItem = fViewer.getTree().getSelection()[0];
         TemplateTree tree = findTemplateTree(selectedItem);
         //boolean successiveAdd = (proposals.length == 1);
         for (int i = 0; i < proposals.length; i++)
         {
            tree.changeTemplateTree(proposals[i], false);
         }
         fViewer.refresh(selectedItem.getData(), false);
         //if (fViewer.getExpandedState(selectedItem.getData()))
         fViewer.expandToLevel(selectedItem.getData(), 1);
         if (proposals.length != 1)
         {
            return;
         }
         if (proposals[0] instanceof DocletElement)
         {
            proposals[0] = tree.getTemplateElement((DocletElement) proposals[0]);
         }
         TreeItem childItem = null;
         for (int i = 0; i < selectedItem.getItemCount(); i++)
         {
            if (selectedItem.getItems()[i].getData() == proposals[0])
            {
               childItem = selectedItem.getItems()[i];
               break;
            }
         }
         if (childItem != null)
         {
            fViewer.expandToLevel(childItem.getData(), 1);
            if (childItem.getItemCount() > 0)
            {
               fViewer.setSelection(new StructuredSelection(childItem.getItems()[0].getData()));
               doReplace();
            }
            else
            {
               fViewer.setSelection(new StructuredSelection(childItem.getData()));
               setActionEnablement();
               doAdd();
            }
         }
      }
   }

   /**
    * Description of the Class
    *
    * @author    Administrator
    * @version   $Revision$
    * @created   17 mai 2003
    */
   protected class LocalLableProvider extends LabelProvider
   {

      /**
       * Gets the image attribute of the LocalLableProvider object
       *
       * @param element  Description of the Parameter
       * @return         The image value
       */
      public Image getImage(Object element)
      {
         return ImageStore.getImage(element);
      }

      /**
       * @param element  Description of the Parameter
       * @return         The text value
       * @see            org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
       */
      public String getText(Object element)
      {
         //			if (element instanceof ContextTree)
         //				return CONTEXT_TREE_NAME;
         return super.getText(element);
      }
   }

   /**
    * Description of the Class
    *
    * @author    Administrator
    * @version   $Revision$
    * @created   17 mai 2003
    */
   protected class ReplaceProposalListener implements IProposalListener
   {
      /** Description of the Method */
      public void closedWithoutProposals()
      {
      }

      /**
       * Description of the Method
       *
       * @param event  Description of the Parameter
       */
      public void handleProposal(ProposalEvent event)
      {
         Object[] proposals = event.getProposals();
         TreeItem selectedItem = fViewer.getTree().getSelection()[0];
         TemplateTree tree = findTemplateTree(selectedItem);
         TreeItem parentItem = selectedItem.getParentItem();
         tree.changeTemplateTree(selectedItem.getData(), true);
         tree.changeTemplateTree(proposals[0], false);
         // As a replaceaction is not offered for a TemplateTree the parentItem can't be null
         fViewer.refresh(parentItem.getData(), false);
      }
   }

   /**
    * Description of the Class
    *
    * @author    Administrator
    * @version   $Revision$
    * @created   17 mai 2003
    */
   public static class NameSorter extends ViewerSorter
   {
      /**
       * Description of the Method
       *
       * @param element  Description of the Parameter
       * @return         Description of the Return Value
       */
      public int category(Object element)
      {
         //			if (element instanceof ContextTree)
         //				return -1;
         return 0;
      }
   }

}
