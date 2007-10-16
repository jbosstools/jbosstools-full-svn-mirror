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
import org.jboss.ide.eclipse.jbosscache.views.content.NodeContentView;

/**
 * This action is related with show the contents of the tree cache node
 * @author Gurkaner
 */
public class ShowContentAction extends AbstractCacheAction
{

   /**
    * Constructor
    * @param view
    * @param actionId
    */
   public ShowContentAction(TreeCacheView view, String actionId)
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
         NodeContentView part = (NodeContentView) getTreeViewer().getViewSite().getPage().showView(
               ICacheConstants.CACHE_NODE_CONTENT_VIEW_ID);
         part.showNodeContent(getTreeViewer().getSelection());

      }
      catch (PartInitException e)
      {

      }
   }
}//end of class