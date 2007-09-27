/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.editors.query;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

/**
 * This view will show the nodes that are contained in the cache.
 */

public class CacheQueryView extends ViewPart
{
   private TreeViewer treeViewer;

   private Action action1;

   private Action action2;

   private Action action3;

   private Action action4;

   private Action doubleClickAction;

   class TreeContentProvider implements ITreeContentProvider
   {

      public Object[] getChildren(Object parentElement)
      {
         // TODO Auto-generated method stub
         return null;
      }

      public Object getParent(Object element)
      {
         // TODO Auto-generated method stub
         return "CACHE";
      }

      public boolean hasChildren(Object element)
      {
         // TODO Auto-generated method stub
         return false;
      }

      public Object[] getElements(Object inputElement)
      {
         return new String[]
         {"One", "Two", "Three"};
      }

      public void dispose()
      {
         // TODO Auto-generated method stub

      }

      public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
      {
         // TODO Auto-generated method stub

      }

   }

   class TreeLabelProvider implements ILabelProvider
   {

      public Image getImage(Object element)
      {
         return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
      }

      public String getText(Object element)
      {

         return element.toString();
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

   /**
    * The constructor.
    */
   public CacheQueryView()
   {
   }

   /**
    * This is a callback that will allow us
    * to create the viewer and initialize it.
    */
   public void createPartControl(Composite parent)
   {

      treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
      treeViewer.setContentProvider(new TreeContentProvider());
      treeViewer.setLabelProvider(new TreeLabelProvider());
      treeViewer.setAutoExpandLevel(1);
      treeViewer.setInput(getViewSite());

      makeActions();
      hookContextMenu();
      hookDoubleClickAction();
      hookGlobalActions();
      contributeToActionBars();
   }

   private void hookGlobalActions()
   {
   }

   private void hookContextMenu()
   {
      MenuManager menuMgr = new MenuManager("#PopupMenu1");
      menuMgr.setRemoveAllWhenShown(true);
      menuMgr.addMenuListener(new IMenuListener()
      {
         public void menuAboutToShow(IMenuManager manager)
         {
            CacheQueryView.this.fillContextMenu(manager);
         }
      });
      Menu menu = menuMgr.createContextMenu(treeViewer.getControl());
      treeViewer.getControl().setMenu(menu);
      getSite().registerContextMenu(menuMgr, treeViewer);
   }

   private void contributeToActionBars()
   {
      IActionBars bars = getViewSite().getActionBars();
      fillLocalPullDown(bars.getMenuManager());
      fillLocalToolBar(bars.getToolBarManager());
   }

   private void fillLocalPullDown(IMenuManager manager)
   {
      manager.add(action1);
      manager.add(new Separator());
      manager.add(action2);
      manager.add(action3);
      manager.add(new Separator());
      manager.add(action4);
   }

   private void fillContextMenu(IMenuManager manager)
   {
      manager.add(action1);
      manager.add(action2);
      // Other plug-ins can contribute there actions here
      manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
      manager.add(new Separator("gurkan"));
   }

   private void fillLocalToolBar(IToolBarManager manager)
   {
      manager.add(action1);
      manager.add(action2);
   }

   private void makeActions()
   {
      action1 = new Action()
      {
         public void run()
         {
            showMessage("Action 1 executed");
         }
      };
      action1.setText("Action 1");
      action1.setToolTipText("Action 1 tooltip");
      action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(
            ISharedImages.IMG_OBJS_INFO_TSK));

      action2 = new Action()
      {
         public void run()
         {
            showMessage("Action 2 executed");
         }
      };
      action2.setText("Action 2");
      action2.setToolTipText("Action 2 tooltip");
      action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(
            ISharedImages.IMG_OBJS_INFO_TSK));

      action3 = new Action()
      {
         public void run()
         {
            showMessage("Action 3 executed");
         }
      };
      action3.setText("Action 3");
      action3.setToolTipText("Action 3 tooltip");
      action3.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(
            ISharedImages.IMG_OBJS_INFO_TSK));

      action4 = new Action()
      {
         public void run()
         {
            showMessage("Action 4 executed");
         }
      };
      action4.setText("Action 4");
      action4.setToolTipText("Action 4 tooltip");
      action4.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(
            ISharedImages.IMG_OBJS_INFO_TSK));

      doubleClickAction = new Action()
      {
         public void run()
         {
            ISelection selection = treeViewer.getSelection();
            Object obj = ((IStructuredSelection) selection).getFirstElement();
            showMessage("Double-click detected on " + obj.toString());
         }
      };
   }

   private void hookDoubleClickAction()
   {
      treeViewer.addDoubleClickListener(new IDoubleClickListener()
      {
         public void doubleClick(DoubleClickEvent event)
         {
            doubleClickAction.run();
         }
      });
   }

   private void showMessage(String message)
   {
      MessageDialog.openInformation(treeViewer.getControl().getShell(), "Cache View", message);
   }

   /**
    * Passing the focus request to the viewer's control.
    */
   public void setFocus()
   {
      treeViewer.getControl().setFocus();
   }
}