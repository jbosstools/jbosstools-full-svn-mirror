/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;
import org.jboss.ide.eclipse.jbosscache.ICacheConstants;

/**
 * JBoss Cache IDE perspective according to the page layout
 * @author Gurkaner
 */
public class CachePerspective implements IPerspectiveFactory
{

   /**
    * Create the perspective layout
    * @param layout
    * @return none
    */
   public void createInitialLayout(IPageLayout layout)
   {
      IFolderLayout folderLayout = null;
      IFolderLayout folderLayoutNext = null;

      //LEFT
      folderLayout = layout.createFolder("side", IPageLayout.LEFT, (float) 0.25, layout.getEditorArea());
      folderLayout.addView(ICacheConstants.CACHE_CONTENT_VIEW_ID);

      //BOTTOM
      folderLayoutNext = layout.createFolder("bottom", IPageLayout.BOTTOM, (float) 0.75, layout.getEditorArea());
      folderLayoutNext.addView(ICacheConstants.CACHE_NODE_CONTENT_VIEW_ID);
      folderLayoutNext.addView(ICacheConstants.CACHE_STATISTIC_VIEW_ID);
      folderLayoutNext.addView(IConsoleConstants.ID_CONSOLE_VIEW);

      //RIGHT
      layout.addView(ICacheConstants.CACHE_GRAPH_VIEW_ID, IPageLayout.RIGHT, (float) 0.66, layout.getEditorArea());

   }//end of method 

}//end of class
