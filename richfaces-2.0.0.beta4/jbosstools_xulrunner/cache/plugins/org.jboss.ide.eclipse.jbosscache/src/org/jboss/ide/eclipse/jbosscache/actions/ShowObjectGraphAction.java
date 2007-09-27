/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.actions;

import org.eclipse.ui.PartInitException;
import org.jboss.ide.eclipse.jbosscache.ICacheConstants;
import org.jboss.ide.eclipse.jbosscache.views.config.TreeCacheView;
import org.jboss.ide.eclipse.jbosscache.views.gef.CacheGraphView;

/**
 * This class represnts actions that is run whenever the node object graph is shown
 * @author Gurkaner
 */
public class ShowObjectGraphAction extends AbstractCacheAction
{

   /**
    * Constructor
    * @param view
    * @param actionId
    */
   public ShowObjectGraphAction(TreeCacheView view, String actionId)
   {
      super(view, actionId);
   }

   /**
    * Real action is here 
    */
   public void run()
   {
      try
      {
         //Show the content view if not showing
         CacheGraphView part = (CacheGraphView) getTreeViewer().getViewSite().getPage().showView(
               ICacheConstants.CACHE_GRAPH_VIEW_ID);
         part.showObjectGraph(getTreeViewer().getSelection());

      }
      catch (PartInitException e)
      {

      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }

}
