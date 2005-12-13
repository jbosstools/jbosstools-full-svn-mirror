/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.views.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.dialogs.PropertyDialogAction;
import org.eclipse.ui.part.ViewPart;
import org.jboss.ide.eclipse.jbosscache.ICacheConstants;
import org.jboss.ide.eclipse.jbosscache.JBossCachePlugin;
import org.jboss.ide.eclipse.jbosscache.actions.ConnectAction;
import org.jboss.ide.eclipse.jbosscache.actions.CreateNodeAction;
import org.jboss.ide.eclipse.jbosscache.actions.DeleteConfigurationAction;
import org.jboss.ide.eclipse.jbosscache.actions.DeleteNodeAction;
import org.jboss.ide.eclipse.jbosscache.actions.DisConnectAction;
import org.jboss.ide.eclipse.jbosscache.actions.DuplicateAction;
import org.jboss.ide.eclipse.jbosscache.actions.EditConfigurationAction;
import org.jboss.ide.eclipse.jbosscache.actions.ExportAction;
import org.jboss.ide.eclipse.jbosscache.actions.ImportAction;
import org.jboss.ide.eclipse.jbosscache.actions.NewCacheConfigurationAction;
import org.jboss.ide.eclipse.jbosscache.actions.RenameAction;
import org.jboss.ide.eclipse.jbosscache.actions.ShowContentAction;
import org.jboss.ide.eclipse.jbosscache.actions.ShowObjectGraphAction;
import org.jboss.ide.eclipse.jbosscache.model.cache.AbstractCacheInstance;
import org.jboss.ide.eclipse.jbosscache.model.cache.AbstractCacheRootInstance;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheInstance;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheInstanceListener;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheRootInstance;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheRootInstanceConnectionListener;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheRootInstanceListener;
import org.jboss.ide.eclipse.jbosscache.model.factory.CacheInstanceFactory;
import org.jboss.ide.eclipse.jbosscache.model.internal.ITreeCacheManagerListener;
import org.jboss.ide.eclipse.jbosscache.model.internal.TreeCacheManager;
import org.jboss.ide.eclipse.jbosscache.utils.CacheUtil;

/**
 * This view will show the nodes that are contained in the cache.
 */

public class TreeCacheView extends ViewPart
      implements
         ICacheRootInstanceListener,
         ICacheInstanceListener,
         ICacheRootInstanceConnectionListener,
         IShellProvider
{

   private static final String ROOT_INSTANCES = "rootinstances";

   private static final String ROOT_PART = "rootpart";

   private static final String ROOT_NAME = "rootname";

   private static final String FILE_NAME = "rootfilename";

   /**Tree View control*/
   private TreeViewer treeViewer;

   /**New cache configuration action*/
   private Action newCacheConfigurationAction;

   /**Edit Configuration Action*/
   private Action editCacheConfigurationAction;

   /**Delete Configuration Action*/
   private Action deleteCacheConfigurationAction;

   /**Connect to the cache action*/
   private Action connectAction;

   /**Disconnect from the cache*/
   private Action disconnectAction;

   /**Add node to the cache*/
   private Action addNodeAction;

   /**Remove node to the cache*/
   private Action removeNodeAction;

   /**Show Contents of the cache node*/
   private Action showContentAction;

   /**Show object graph of the selected cache node*/
   private Action showObjectGraphAction;

   /**Export Action*/
   private Action exportAction;

   /**Import Action*/
   private Action importAction;

   /**Duplicate Action*/
   private Action duplicateAction;

   /**Rename Action*/
   private Action renameAction;

   private PropertyDialogAction propDialogAction;

   /**All cache root instance view by this viewer*/
   private Map cacheRootInstanceMap = null;

   /**All cache node listners with given root instances*/
   private Map cacheNodeListenerMap = null;

   private Action doubleClickAction;

   private IMemento memento;

   /**
    * The constructor.
    */
   public TreeCacheView()
   {
      super();
      AbstractCacheRootInstance.addCacheRootListener(this);
      AbstractCacheRootInstance.addConnectListener(this);
      AbstractCacheInstance.addCacheInstanceListener(this);
   }

   /**
    * This is a callback that will allow us
    * to create the viewer and initialize it.
    */
   public void createPartControl(Composite parent)
   {

      treeViewer = new TreeViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
      treeViewer.setContentProvider(new TreeCacheViewContentProvider());
      treeViewer.setLabelProvider(new TreeCacheViewLabelProvider());
      treeViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);
      treeViewer.addSelectionChangedListener(new ISelectionChangedListener()
      {

         public void selectionChanged(SelectionChangedEvent event)
         {
            handleSelectionChange(event.getSelection());
         }

      });

      ICacheRootInstance mainRootInstance = CacheInstanceFactory.getCacheRootMainInstance();
      treeViewer.setInput(mainRootInstance);
      addTreeCacheViewNodeListener(mainRootInstance);

      //		if(memento != null)
      //			restoreState(memento);

      makeActions();
      hookContextMenu();
      hookDoubleClickAction();
      hookGlobalActions();
      contributeToActionBars();

      getSite().setSelectionProvider(treeViewer);
   }

   private void handleSelectionChange(ISelection e)
   {
      Object obj = null;
      if (e instanceof IStructuredSelection)
      {
         IStructuredSelection strSle = (IStructuredSelection) e;
         obj = strSle.getFirstElement();
      }
      else
      {
         obj = (Object) e;
      }

      if (obj instanceof ICacheRootInstance)
      {
         editCacheConfigurationAction.setEnabled(true);
         deleteCacheConfigurationAction.setEnabled(true);
         exportAction.setEnabled(true);
         duplicateAction.setEnabled(true);
      }
      else
      {
         editCacheConfigurationAction.setEnabled(false);
         deleteCacheConfigurationAction.setEnabled(false);
         exportAction.setEnabled(false);
         duplicateAction.setEnabled(false);

      }

   }

   /**
    * Readd cache listener because of
    * @param mainRootInstance
    */
   private void addTreeCacheViewNodeListener(ICacheRootInstance mainRootInstance)
   {
      List listChilds = mainRootInstance.getRootInstanceChilds();
      if (listChilds != null)
      {
         for (int i = 0; i < listChilds.size(); i++)
         {
            ICacheRootInstance rootInstChilds = (ICacheRootInstance) listChilds.get(i);
            if (rootInstChilds.isConnected())
            {
               if (cacheNodeListenerMap == null)
               {
                  cacheNodeListenerMap = new HashMap();
               }
               cacheNodeListenerMap.put(rootInstChilds.getRootName(), rootInstChilds.getCacheViewNodeListener());
            }

            if (cacheRootInstanceMap == null)
               cacheRootInstanceMap = new HashMap();

            cacheRootInstanceMap.put(rootInstChilds.getRootName(), rootInstChilds);
         }
      }
   }

   private void hookGlobalActions()
   {

   }

   /**
    * Context-menu
    *
    */
   private void hookContextMenu()
   {
      MenuManager menuMgr = new MenuManager("#PopupMenu");
      menuMgr.setRemoveAllWhenShown(true);
      menuMgr.addMenuListener(new IMenuListener()
      {
         public void menuAboutToShow(IMenuManager manager)
         {
            TreeCacheView.this.fillContextMenu(manager);
         }
      });
      Menu menu = menuMgr.createContextMenu(treeViewer.getControl());
      treeViewer.getControl().setMenu(menu);
      getSite().registerContextMenu(menuMgr, treeViewer);
   }

   /**
    * Action-bars
    *
    */
   private void contributeToActionBars()
   {
      IActionBars bars = getViewSite().getActionBars();
      fillLocalPullDown(bars.getMenuManager());
      fillLocalToolBar(bars.getToolBarManager());
   }

   /**
    * Pull down menu actions
    * @param manager
    */
   private void fillLocalPullDown(IMenuManager manager)
   {
      manager.add(newCacheConfigurationAction);
      //manager.add(editCacheConfigurationAction);
      //manager.add(deleteCacheConfigurationAction);
      manager.add(new Separator());
      manager.add(importAction);
      //manager.add(exportAction);
      //manager.add(duplicateAction);
   }

   /**
    * Context menu actions
    * @param manager
    */
   private void fillContextMenu(IMenuManager manager)
   {
      manager.add(newCacheConfigurationAction);

      if (getSelection() != null)
      {
         if (getSelection() instanceof ICacheRootInstance)
         {
            ICacheRootInstance rootInstance = (ICacheRootInstance) getSelection();
            manager.add(editCacheConfigurationAction);
            manager.add(deleteCacheConfigurationAction);
            manager.add(new Separator());
            manager.add(exportAction);
            manager.add(duplicateAction);
            manager.add(new Separator());
            manager.add(renameAction);
            manager.add(new Separator());
            manager.add(connectAction);
            manager.add(disconnectAction);

            if (!rootInstance.isConnected())
            {
               disconnectAction.setEnabled(false);
               connectAction.setEnabled(true);
               renameAction.setEnabled(true);
            }
            else
            {
               disconnectAction.setEnabled(true);
               connectAction.setEnabled(false);
               renameAction.setEnabled(false);
            }
            editCacheConfigurationAction.setEnabled(true);
            deleteCacheConfigurationAction.setEnabled(true);
            exportAction.setEnabled(true);
            duplicateAction.setEnabled(true);
            

         }

         manager.add(new Separator());
         if (getSelection() instanceof ICacheInstance)
         {
            addNodeAction.setEnabled(true);
            removeNodeAction.setEnabled(true);
            editCacheConfigurationAction.setEnabled(false);
            deleteCacheConfigurationAction.setEnabled(false);
            exportAction.setEnabled(false);
            duplicateAction.setEnabled(false);
            manager.add(addNodeAction);
            manager.add(removeNodeAction);
            manager.add(showContentAction);
         }
         else if (getSelection() instanceof ICacheRootInstance)
         {
            ICacheRootInstance rootInstance = (ICacheRootInstance) getSelection();
            manager.add(addNodeAction);
            manager.add(showObjectGraphAction);
            if (rootInstance.isConnected())
            {
               addNodeAction.setEnabled(true);

               if (rootInstance.getCacheType().equals(ICacheConstants.JBOSS_CACHE_TREE_CACHE_AOP))
                  showObjectGraphAction.setEnabled(true);
               else
                  showObjectGraphAction.setEnabled(false);
            }
            else
            {
               addNodeAction.setEnabled(false);
               showObjectGraphAction.setEnabled(false);
            }
         }
      }

      if (getSelection() instanceof ICacheRootInstance)
      {
         manager.add(new Separator());
         manager.add(propDialogAction);
      }

      // Other plug-ins can contribute there actions here
      manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
   }

   /**
    * Tool bar actions
    * @param manager
    */
   private void fillLocalToolBar(IToolBarManager manager)
   {
      manager.add(newCacheConfigurationAction);
      manager.add(editCacheConfigurationAction);
      manager.add(deleteCacheConfigurationAction);
      manager.add(new Separator());
      manager.add(importAction);
      manager.add(exportAction);
      manager.add(duplicateAction);
   }

   /**
    * Create actions
    *
    */
   private void makeActions()
   {

      newCacheConfigurationAction = new NewCacheConfigurationAction(this, "newConfigurationAction");
      newCacheConfigurationAction.setText(CacheUtil
            .getResourceBundleValue(ICacheConstants.TREECACHEVIEW_NEW_CONFIGURATION_ACTION));
      newCacheConfigurationAction.setToolTipText(CacheUtil
            .getResourceBundleValue(ICacheConstants.TREECACHEVIEW_NEW_CONFIGURATION_ACTION));
      newCacheConfigurationAction.setImageDescriptor(JBossCachePlugin.getDefault().getImageRegistry().getDescriptor(
            ICacheConstants.IMAGE_KEY_NEW_CON));

      editCacheConfigurationAction = new EditConfigurationAction(this, "editConfigurationAction");
      editCacheConfigurationAction.setText(CacheUtil
            .getResourceBundleValue(ICacheConstants.TREECACHEVIEW_EDIT_CONFIGURATION_ACTION));
      editCacheConfigurationAction.setToolTipText(CacheUtil
            .getResourceBundleValue(ICacheConstants.TREECACHEVIEW_EDIT_CONFIGURATION_ACTION));
      editCacheConfigurationAction.setImageDescriptor(JBossCachePlugin.getDefault().getImageRegistry().getDescriptor(
            ICacheConstants.IMAGE_KEY_EDIT_CON));
      editCacheConfigurationAction.setEnabled(false);

      deleteCacheConfigurationAction = new DeleteConfigurationAction(this, "deleteConfigurationAction");
      deleteCacheConfigurationAction.setText(CacheUtil
            .getResourceBundleValue(ICacheConstants.TREECACHEVIEW_DELETE_CONFIGURATION_ACTION));
      deleteCacheConfigurationAction.setToolTipText(CacheUtil
            .getResourceBundleValue(ICacheConstants.TREECACHEVIEW_DELETE_CONFIGURATION_ACTION));
      deleteCacheConfigurationAction.setImageDescriptor(JBossCachePlugin.getDefault().getImageRegistry().getDescriptor(
            ICacheConstants.IMAGE_KEY_DELETE_EDIT));
      deleteCacheConfigurationAction.setEnabled(false);

      connectAction = new ConnectAction(this, "newConnectAction");
      connectAction.setText(CacheUtil.getResourceBundleValue(ICacheConstants.TREECACHEVIEW_CONNECT_ACTION));
      connectAction.setToolTipText(CacheUtil.getResourceBundleValue(ICacheConstants.TREECACHEVIEW_CONNECT_ACTION));
      connectAction.setImageDescriptor(JBossCachePlugin.getDefault().getImageRegistry().getDescriptor(
            ICacheConstants.IMAGE_KEY_RUN_EXC));

      disconnectAction = new DisConnectAction(this, "newDisconnectAction");
      disconnectAction
            .setToolTipText(CacheUtil.getResourceBundleValue(ICacheConstants.TREECACHEVIEW_DISCONNECT_ACTION));
      disconnectAction.setText(CacheUtil.getResourceBundleValue(ICacheConstants.TREECACHEVIEW_DISCONNECT_ACTION));
      disconnectAction.setImageDescriptor(JBossCachePlugin.getDefault().getImageRegistry().getDescriptor(
            ICacheConstants.IMAGE_KEY_TERM_SBOOK));

      addNodeAction = new CreateNodeAction(this, "createNodeAction");
      addNodeAction.setToolTipText(CacheUtil.getResourceBundleValue(ICacheConstants.TREECACHEVIEW_ADD_NODE_ACTION));
      addNodeAction.setText(CacheUtil.getResourceBundleValue(ICacheConstants.TREECACHEVIEW_ADD_NODE_ACTION));
      addNodeAction.setImageDescriptor(JBossCachePlugin.getDefault().getImageRegistry().getDescriptor(
            ICacheConstants.IMAGE_KEY_NEWPACK_WIZ));

      removeNodeAction = new DeleteNodeAction(this, "deleteNodeAction");
      removeNodeAction.setToolTipText(CacheUtil
            .getResourceBundleValue(ICacheConstants.TREECACHEVIEW_DELETE_NODE_ACTION));
      removeNodeAction.setText(CacheUtil.getResourceBundleValue(ICacheConstants.TREECACHEVIEW_DELETE_NODE_ACTION));
      removeNodeAction.setImageDescriptor(JBossCachePlugin.getDefault().getImageRegistry().getDescriptor(
            ICacheConstants.IMAGE_KEY_DELETE_EDIT));

      showContentAction = new ShowContentAction(this, "showContentAction");
      showContentAction.setToolTipText(CacheUtil
            .getResourceBundleValue(ICacheConstants.TREECACHEVIEW_SHOW_NODE_CONTENT_ACTION));
      showContentAction.setText(CacheUtil
            .getResourceBundleValue(ICacheConstants.TREECACHEVIEW_SHOW_NODE_CONTENT_ACTION));
      showContentAction.setImageDescriptor(JBossCachePlugin.getDefault().getImageRegistry().getDescriptor(
            ICacheConstants.IMAGE_KEY_SEARCH_REF_OBJ));

      showObjectGraphAction = new ShowObjectGraphAction(this, "showObjectGraphAction");
      showObjectGraphAction.setToolTipText(CacheUtil
            .getResourceBundleValue(ICacheConstants.TREECACHEVIEW_SHOW_OBJECT_GRAPH_ACTION));
      showObjectGraphAction.setText(CacheUtil
            .getResourceBundleValue(ICacheConstants.TREECACHEVIEW_SHOW_OBJECT_GRAPH_ACTION));
      showObjectGraphAction.setImageDescriptor(JBossCachePlugin.getDefault().getImageRegistry().getDescriptor(
            ICacheConstants.IMAGE_KEY_WATCHLIST_VIEW));

      importAction = new ImportAction(this, "importAction");

      exportAction = new ExportAction(this, "exportAction");
      exportAction.setEnabled(false);

      duplicateAction = new DuplicateAction(this, "duplicateAction");
      duplicateAction.setEnabled(false);

      propDialogAction = new PropertyDialogAction(this, treeViewer);

      renameAction = new RenameAction(this, "renameAction");
      renameAction.setEnabled(false);

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

   /**
    * Show messages to the user
    * @param message
    */
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

   /**
    * When new cache root instance added, refresh tree view
    * @param Newly added cache root instance (New Cache Configuration)
    * @return 
    */
   public void cacheRootInstanceAdded(ICacheRootInstance cacheRootInstance)
   {
      if (cacheRootInstanceMap == null)
      {
         cacheRootInstanceMap = new HashMap();
         cacheRootInstanceMap.put(cacheRootInstance.getRootName(), cacheRootInstance);
      }
      else
      {
         cacheRootInstanceMap.put(cacheRootInstance.getRootName(), cacheRootInstance);
      }
      treeViewer.refresh(cacheRootInstance.getMainRootCacheInstance(), false);

   }

   /**
    * When existing cache root instance removed, refresh tree view
    * @param Removed cache root instance (Existing configuration removed)
    */
   public void cacheRootInstanceRemoved(ICacheRootInstance cacheRootInstance)
   {
      if (cacheRootInstance.isConnected())
      {
         TreeCacheManager treeCacheManager = cacheRootInstance.getTreeCacheManager();
         TreeCacheManager.removeTreeCacheManagerListener(treeCacheManager,
               (ITreeCacheManagerListener) cacheNodeListenerMap.get(cacheRootInstance.getRootName()));
         cacheNodeListenerMap.remove(cacheRootInstance.getRootName());
      }

      cacheRootInstanceMap.remove(cacheRootInstance.getRootName());

      ICacheRootInstance rootMain = cacheRootInstance.getMainRootCacheInstance();
      rootMain.getRootInstanceChilds().remove(cacheRootInstance);
      cacheRootInstance = null;

      treeViewer.refresh(rootMain, false);
   }

   //	/**
   //	 * Initiliaze with memonto
   //	 */
   //	public void init(IViewSite site,IMemento memento) throws PartInitException{
   //		super.init(site,memento);
   //		this.memento = memento;
   //	}

   //	/**
   //	 * Saves this view 
   //	 */
   //	public void saveState(IMemento memento){
   //	       if (treeViewer == null) {
   //	            if (this.memento != null)
   //	                memento.putMemento(this.memento);
   //	            return;
   //	        }
   //	       
   //	       ICacheRootInstance rootInstance = (ICacheRootInstance)treeViewer.getInput();
   //	       
   //	       if(rootInstance == null)
   //	    	   return;
   //	       
   //	       IMemento childMemento = memento.createChild(ROOT_INSTANCES);
   //	       List list = rootInstance.getRootInstanceChilds();
   //	       if(list != null){
   //	    	   Iterator it = list.iterator();
   //	    	   while(it.hasNext()){
   //	    		   ICacheRootInstance childRootInstance = (ICacheRootInstance)it.next();
   //	    		   IMemento childMementoNext = childMemento.createChild(ROOT_PART);
   //	    		   childMementoNext.putString(ROOT_NAME,childRootInstance.getRootName());
   //	    		   childMementoNext.putString(FILE_NAME,childRootInstance.getRootConfigurationFileName());	    		   
   //	    	   }
   //	       }else
   //	    	   return;
   //		
   //	}
   //	
   //	/**
   //	 * Restore the state
   //	 * @param memento
   //	 */
   //	private void restoreState(IMemento memento){
   //		IMemento[] childs = memento.getChild(ROOT_INSTANCES).getChildren(ROOT_PART);
   //		
   //		if(childs != null){
   //			for(int i=0;i<childs.length;i++){
   //				IMemento child = childs[i];
   //				ICacheRootInstance rootInstance = CacheInstanceFactory.getCacheRootInstance(child.getString(ROOT_NAME),child.getString(FILE_NAME));
   //				CacheInstanceFactory.getCacheRootMainInstance().addRootInstanceChild(rootInstance);				
   //			}
   //		}
   //	}

   /**
    * Return shell
    * @return
    */
   public Shell getShell()
   {
      return getViewSite().getShell();
   }

   /**
    * Return the selected object. If the selection is structured selection 
    * just select first selection
    * @return
    */
   public Object getSelection()
   {
      if (treeViewer.getSelection() instanceof IStructuredSelection)
      {
         IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
         return (Object) selection.getFirstElement();
      }
      else
         return treeViewer.getSelection();
   }

   /**
    * When new node instance added
    */
   public void cacheInstanceAdded(ICacheInstance instance)
   {
      ICacheRootInstance rootInstance = instance.getRootInstance();
      if (rootInstance != null)
         treeViewer.refresh(rootInstance, false);
      else
      {
         treeViewer.refresh(instance.getInstanceParent(), false);
      }
   }

   /**
    * When new node instances remove
    */
   public void cacheInstanceRemoved(ICacheInstance instance)
   {
      ICacheRootInstance rootInstance = instance.getRootInstance();
      if (rootInstance != null)
         treeViewer.refresh(rootInstance, false);
      else
      {
         treeViewer.refresh(instance.getInstanceParent(), false);
      }
   }

   /**
    * When new node added to the root instance
    */
   public void cacheInstanceToRootAdded(ICacheInstance cacheInstance)
   {
      ICacheRootInstance rootInstance = cacheInstance.getRootInstance();
      treeViewer.refresh(rootInstance, false);
   }

   /**
    * When new node removed from the root instance
    */
   public void cacheInstanceToRootRemoved(ICacheInstance cacheInstance)
   {
      ICacheRootInstance rootInstance = cacheInstance.getRootInstance();
      treeViewer.refresh(rootInstance, false);
   }

   /**
    * When root instance connect to the cache, add TreeCacheManagerListener
    * @param rootInstance
    */
   public void cacheRootInstanceConnected(ICacheRootInstance rootInstance)
   {
      TreeCacheManager treeCacheManager = rootInstance.getTreeCacheManager();
      TreeCacheViewNodeListener listener = new TreeCacheViewNodeListener(rootInstance);
      if (cacheNodeListenerMap == null)
      {
         cacheNodeListenerMap = new HashMap();
      }
      cacheNodeListenerMap.put(rootInstance.getRootName(), listener);
      TreeCacheManager.addTreeCacheManagerListener(treeCacheManager, listener);

      //		if(rootInstance.getRootLabel().indexOf("[") == -1)
      //			rootInstance.setRootLabel(rootInstance.getRootLabel()+"[Connected]");
      //		else
      //			rootInstance.setRootLabel(rootInstance.getRootLabel().substring(0,rootInstance.getRootLabel().indexOf("["))+"[Connected]");
      treeViewer.refresh(rootInstance, true);
   }

   /**
    * When root instance disconnect from the cache, remove TreeCacheManagerListener
    * @param rootInstance
    */
   public void cacheRootInstanceDisConnected(ICacheRootInstance rootInstance)
   {
      TreeCacheManager treeCacheManager = rootInstance.getTreeCacheManager();
      TreeCacheManager.removeTreeCacheManagerListener(treeCacheManager,
            (ITreeCacheManagerListener) cacheNodeListenerMap.get(rootInstance.getRootName()));
      //		rootInstance.setRootLabel(rootInstance.getRootLabel().substring(0,rootInstance.getRootLabel().indexOf("["))+"[Disconnected]");
      treeViewer.refresh(rootInstance, true);

   }

   /**
    * When view dispose remove listeners
    */
   public void dispose()
   {
      super.dispose();
      AbstractCacheRootInstance.removeCacheRootListener(this);
      AbstractCacheRootInstance.removeConnectListener(this);
      AbstractCacheInstance.removeCacheInstanceListener(this);
   }

   public TreeViewer getTreeViewer()
   {
      return this.treeViewer;
   }
}//end of class