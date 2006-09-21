/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.views.content;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;
import org.jboss.ide.eclipse.jbosscache.ICacheConstants;
import org.jboss.ide.eclipse.jbosscache.JBossCachePlugin;
import org.jboss.ide.eclipse.jbosscache.actions.ShowObjectFieldsAction;
import org.jboss.ide.eclipse.jbosscache.internal.CacheMessages;
import org.jboss.ide.eclipse.jbosscache.model.cache.AbstractCacheRootInstance;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheInstance;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheRootInstance;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheRootInstanceConnectionListener;
import org.jboss.ide.eclipse.jbosscache.utils.CacheUtil;

/**
 * This view will show the node content.
 * @author Gurkaner 
 */

public class NodeContentView extends ViewPart implements ICacheRootInstanceConnectionListener
{

   /**Show contents of the cache node in the table*/
   private TableViewer tableViewer;

   /**Table clear action*/
   private ClearTableAction clearTableAction;

   /**Show object field action to show objects field with values*/
   private ShowObjectFieldsAction showObjectsFieldAction;

   class ClearTableAction extends Action
   {

      public ClearTableAction()
      {
         super(CacheUtil.getResourceBundleValue(ICacheConstants.NODECONTENTVIEW_CLEAR_TABLE_ACTION));
         setImageDescriptor(JBossCachePlugin.getDefault().getImageRegistry().getDescriptor(
               ICacheConstants.IMAGE_KEY_DELETE_EDIT));
         setToolTipText(CacheUtil.getResourceBundleValue(ICacheConstants.NODECONTENTVIEW_CLEAR_TABLE_ACTION));
         setEnabled(false);
      }

      public void run()
      {
         tableViewer.getTable().clearAll();
         tableViewer.setInput(new Object());
         clearTableAction.setEnabled(false);
      }
   }

   /**
    * The constructor.
    */
   public NodeContentView()
   {
      AbstractCacheRootInstance.addConnectListener(this);
   }

   /**
    * This is a callback that will allow us
    * to create the viewer and initialize it.
    */
   public void createPartControl(Composite parent)
   {
      Table table = new Table(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER);
      tableViewer = new TableViewer(table);
      table.setLinesVisible(true);
      table.setHeaderVisible(true);

      TableColumn parentColumn = new TableColumn(table, SWT.CENTER);
      parentColumn.setText(CacheUtil.getResourceBundleValue(ICacheConstants.NODE_CONTENT_VIEW_TABLE_COLUMN_PARENT));
      parentColumn.setWidth(120);
      parentColumn.setResizable(true);

      TableColumn fqnColumn = new TableColumn(table, SWT.CENTER);
      fqnColumn.setText(CacheUtil.getResourceBundleValue(ICacheConstants.NODE_CONTENT_VIEW_TABLE_COLUMN_FQN));
      fqnColumn.setWidth(120);
      fqnColumn.setResizable(true);

      TableColumn keyColumn = new TableColumn(table, SWT.CENTER);
      keyColumn.setText(CacheUtil.getResourceBundleValue(ICacheConstants.NODE_CONTENT_VIEW_TABLE_COLUMN_KEY));
      keyColumn.setWidth(200);
      keyColumn.setResizable(true);

      TableColumn valueColumn = new TableColumn(table, SWT.CENTER);
      valueColumn.setText(CacheUtil.getResourceBundleValue(ICacheConstants.NODE_CONTENT_VIEW_TABLE_COLUMN_VALUE));
      valueColumn.setWidth(200);
      valueColumn.setResizable(true);

      tableViewer.setContentProvider(new TableContentProvider());
      tableViewer.setLabelProvider(new TableLabelProvider());
      tableViewer.setColumnProperties(new String[]
      {CacheUtil.getResourceBundleValue(ICacheConstants.NODE_CONTENT_VIEW_TABLE_COLUMN_PARENT),
            CacheUtil.getResourceBundleValue(ICacheConstants.NODE_CONTENT_VIEW_TABLE_COLUMN_FQN),
            CacheUtil.getResourceBundleValue(ICacheConstants.NODE_CONTENT_VIEW_TABLE_COLUMN_KEY),
            CacheUtil.getResourceBundleValue(ICacheConstants.NODE_CONTENT_VIEW_TABLE_COLUMN_VALUE)});

      //Actions
      makeActions();
      hookContextMenu();
      hookDoubleClickAction();
      hookGlobalActions();
      contributeToActionBars();
      enableActionStates();

      getSite().setSelectionProvider(tableViewer);
      //getSite().getPage().addSelectionListener(ICacheConstants.CACHE_CONTENT_VIEW_ID,this);
   }

   /**
    * Set action states
    */
   private void enableActionStates()
   {
      if (getSelection() == null)
      {
         showObjectsFieldAction.setEnabled(false);
      }
      else
      {
         showObjectsFieldAction.setEnabled(true);
      }
   }

   /**
    * Selected object
    * @return
    */
   public Object getSelection()
   {

      if (tableViewer.getSelection().isEmpty())
         return null;

      if (tableViewer.getSelection() instanceof IStructuredSelection)
      {
         IStructuredSelection strSel = (IStructuredSelection) tableViewer.getSelection();
         return strSel.getFirstElement();
      }
      else
      {
         return tableViewer.getSelection();
      }
   }

   private void handleFocusGained()
   {
      if (tableViewer.getTable().getItemCount() > 0)
         clearTableAction.setEnabled(true);
      else
         clearTableAction.setEnabled(false);
   }

   /**
    * Global actions handler
    */
   private void hookGlobalActions()
   {
   }//end of method

   /**
    * Contect menu registration
    */
   private void hookContextMenu()
   {
      MenuManager menuMgr = new MenuManager("#PopupMenu");
      menuMgr.setRemoveAllWhenShown(true);
      menuMgr.addMenuListener(new IMenuListener()
      {
         public void menuAboutToShow(IMenuManager manager)
         {
            NodeContentView.this.fillContextMenu(manager);
         }
      });

      Menu menu = menuMgr.createContextMenu(tableViewer.getControl());
      tableViewer.getControl().setMenu(menu);
      getSite().registerContextMenu(menuMgr, tableViewer);
   }

   /** 
    * Tool bar contribution
    */
   private void contributeToActionBars()
   {
      IActionBars bars = getViewSite().getActionBars();
      fillLocalPullDown(bars.getMenuManager());
      fillLocalToolBar(bars.getToolBarManager());
   }

   /**
    * Local pull down actions 
    * @param manager
    */
   private void fillLocalPullDown(IMenuManager manager)
   {
      manager.add(clearTableAction);
      manager.add(new Separator());
   }

   /**
    * Context menu actions
    * @param manager
    */
   private void fillContextMenu(IMenuManager manager)
   {
      manager.add(showObjectsFieldAction);
      // Other plug-ins can contribute there actions here
      manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));

      enableActionStates();

   }

   /**
    * Tool bar actions
    * @param manager
    */
   private void fillLocalToolBar(IToolBarManager manager)
   {
      manager.add(clearTableAction);
   }

   /**
    * Make the actions to use
    */
   private void makeActions()
   {
      clearTableAction = new ClearTableAction();

      showObjectsFieldAction = new ShowObjectFieldsAction(this, "showObjectsFieldAction");
      showObjectsFieldAction.setText(CacheMessages.NodeContentView_showObjectsFieldActionText);
      showObjectsFieldAction.setToolTipText(CacheMessages.NodeContentView_showObjectsFieldActionText);
   }

   /**
    * Double click action
    */
   private void hookDoubleClickAction()
   {
   }

   /**
    * Show message
    * @param message
    */
   private void showMessage(String message)
   {
   }

   /**
    * Passing the focus request to the viewer's control.
    */
   public void setFocus()
   {
      tableViewer.getControl().setFocus();
   }

   /**
    * When selection changes in the view
    */
   public void showNodeContent(Object selection)
   {
      if (!(selection instanceof ICacheInstance))
         return;

      tableViewer.setInput(selection);
      handleFocusGained();
   }//end of method

   public void dispose()
   {
      super.dispose();
      AbstractCacheRootInstance.removeConnectListener(this);
   }

   public void cacheRootInstanceConnected(ICacheRootInstance rootInstance)
   {
      // TODO Auto-generated method stub

   }

   public void cacheRootInstanceDisConnected(ICacheRootInstance rootInstance)
   {
      clearTableAction.run();
   }
}//end of class