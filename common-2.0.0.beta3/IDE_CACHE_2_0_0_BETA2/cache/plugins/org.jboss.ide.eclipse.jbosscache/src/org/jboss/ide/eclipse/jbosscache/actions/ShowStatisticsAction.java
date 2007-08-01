package org.jboss.ide.eclipse.jbosscache.actions;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.PartInitException;
import org.jboss.ide.eclipse.jbosscache.ICacheConstants;
import org.jboss.ide.eclipse.jbosscache.JBossCachePlugin;
import org.jboss.ide.eclipse.jbosscache.views.config.TreeCacheView;
import org.jboss.ide.eclipse.jbosscache.views.statistic.CacheStatView;

public class ShowStatisticsAction extends AbstractCacheAction {

	/**
	 * Constructor
	 * 
	 * @param view
	 * @param id
	 */
	public ShowStatisticsAction(TreeCacheView view, String id) {
		super(view, id);

	}

	public void run() {
		try {
			// Show the content view if not showing
			CacheStatView part = (CacheStatView) getTreeViewer().getViewSite()
					.getPage()
					.showView(ICacheConstants.CACHE_STATISTIC_VIEW_ID);
			
			part.showStatContent(getTreeViewer().getSelection());

		} catch (PartInitException e) {
			IStatus status = new Status(IStatus.ERROR,
					ICacheConstants.CACHE_PLUGIN_UNIQUE_ID, IStatus.ERROR, e
							.getMessage(), e);
			
			JBossCachePlugin.getDefault().getLog().log(status);

		}

	}

}
