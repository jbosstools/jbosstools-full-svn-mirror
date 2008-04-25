/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.views.statistic;

import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;

import org.jboss.ide.eclipse.jbosscache.ICacheConstants;
import org.jboss.ide.eclipse.jbosscache.JBossCachePlugin;
import org.jboss.ide.eclipse.jbosscache.actions.ShowObjectFieldsAction;
import org.jboss.ide.eclipse.jbosscache.internal.CacheMessages;
import org.jboss.ide.eclipse.jbosscache.model.cache.AbstractCacheRootInstance;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheRootInstance;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheRootInstanceConnectionListener;
import org.jboss.ide.eclipse.jbosscache.model.internal.RemoteCacheManager;
import org.jboss.ide.eclipse.jbosscache.utils.CacheUtil;

/**
 * This view will show the node content.
 * 
 * @author Gurkaner
 */

public class CacheStatView extends ViewPart implements ICacheRootInstanceConnectionListener {

	/** Show contents of the cache node in the table */
	private TreeViewer treeViewer;

	/** Table clear action */
	private ClearTableAction clearTableAction;
	
	private String [] columnTitles = {"Attribute","Value"};

	class ClearTableAction extends Action {

		public ClearTableAction() {
			super(
					CacheUtil
							.getResourceBundleValue(ICacheConstants.NODECONTENTVIEW_CLEAR_TABLE_ACTION));
			setImageDescriptor(JBossCachePlugin.getDefault().getImageRegistry()
					.getDescriptor(ICacheConstants.IMAGE_KEY_DELETE_EDIT));
			setToolTipText(CacheUtil
					.getResourceBundleValue(ICacheConstants.NODECONTENTVIEW_CLEAR_TABLE_ACTION));
			setEnabled(false);
		}

		public void run() {
			treeViewer.getTree().clearAll(true);
			treeViewer.setInput(new Object());
			clearTableAction.setEnabled(false);
		}
	}

	/**
	 * The constructor.
	 */
	public CacheStatView() {
		AbstractCacheRootInstance.addConnectListener(this);
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {

		treeViewer = new TreeViewer(parent, SWT.FULL_SELECTION);
		treeViewer.setContentProvider(new CacheStatContentProvider());
		treeViewer.setLabelProvider(new CacheStatLabelProvider());
		
		treeViewer.getTree().setLinesVisible(true);
		treeViewer.getTree().setHeaderVisible(true);
		
		for(int i=0;i<columnTitles.length;i++){
			TreeColumn column = new TreeColumn(treeViewer.getTree(),SWT.CENTER);
	        column.setWidth(300);
	        column.setText(columnTitles[i]);
	        column.setAlignment(SWT.LEFT);
	        column.setResizable(true);
		}

		// Actions
		makeActions();
		hookContextMenu();
		contributeToActionBars();

		getSite().setSelectionProvider(treeViewer);
	}


	/**
	 * Selected object
	 * 
	 * @return
	 */
	public Object getSelection() {

		if (treeViewer.getSelection().isEmpty())
			return null;

		if (treeViewer.getSelection() instanceof IStructuredSelection) {
			IStructuredSelection strSel = (IStructuredSelection) treeViewer
					.getSelection();
			return strSel.getFirstElement();
		} else {
			return treeViewer.getSelection();
		}
	}

	private void handleFocusGained() {
		if (treeViewer.getTree().getItemCount() > 0)
			clearTableAction.setEnabled(true);
		else
			clearTableAction.setEnabled(false);
	}

	/**
	 * Contect menu registration
	 */
	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				CacheStatView.this.fillContextMenu(manager);
			}
		});

		Menu menu = menuMgr.createContextMenu(treeViewer.getControl());
		treeViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, treeViewer);
	}

	/**
	 * Tool bar contribution
	 */
	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	/**
	 * Local pull down actions
	 * 
	 * @param manager
	 */
	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(clearTableAction);
		manager.add(new Separator());
	}

	/**
	 * Context menu actions
	 * 
	 * @param manager
	 */
	private void fillContextMenu(IMenuManager manager) {
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));


	}

	/**
	 * Tool bar actions
	 * 
	 * @param manager
	 */
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(clearTableAction);
	}

	/**
	 * Make the actions to use
	 */
	private void makeActions() {
		clearTableAction = new ClearTableAction();
	}
	
	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		treeViewer.getControl().setFocus();
	}

	/**
	 * When selection changes in the view
	 */
	public void showStatContent(Object selection) {
		if (!(selection instanceof ICacheRootInstance))
			return;

		ICacheRootInstance rootInstance = (ICacheRootInstance)selection;
		RemoteCacheManager remManager = rootInstance.getRemoteCacheManager();
		if(!remManager.isUseMbeanAttributes()){
			ErrorDialog.openError(getSite().getShell(), "Statistic Error", "Set UseInterceptorMBeans Cache Parameter to Get Statistics.", null);
		}
		
		Map map = remManager.getStatisticsAttribute();	
						
		treeViewer.setInput(CacheStatModel.getInstance(map));
		handleFocusGained();
	}// end of method

	public void dispose() {
		super.dispose();
		AbstractCacheRootInstance.removeConnectListener(this);
	}

	public void cacheRootInstanceConnected(ICacheRootInstance rootInstance) {
		// TODO Auto-generated method stub

	}

	public void cacheRootInstanceDisConnected(ICacheRootInstance rootInstance) {
		clearTableAction.run();
	}
}// end of class
