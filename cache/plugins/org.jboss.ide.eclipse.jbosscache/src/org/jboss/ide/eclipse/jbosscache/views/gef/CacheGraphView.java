/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.views.gef;

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;
import org.jboss.ide.eclipse.jbosscache.ICacheConstants;
import org.jboss.ide.eclipse.jbosscache.JBossCachePlugin;
import org.jboss.ide.eclipse.jbosscache.internal.CacheMessages;
import org.jboss.ide.eclipse.jbosscache.model.cache.AbstractCacheInstance;
import org.jboss.ide.eclipse.jbosscache.model.cache.AbstractCacheRootInstance;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheInstance;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheInstanceListener;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheRootInstance;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheRootInstanceConnectionListener;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheRootInstanceListener;

/**
 * This class represents object graph related with cache
 * @author Gurkaner
 */
public class CacheGraphView extends ViewPart
      implements
         ICacheInstanceListener,
         ICacheRootInstanceListener,
         ICacheRootInstanceConnectionListener
{

   /**Graphical viewer of nodes*/
   private TreeViewer graphViewer;

   /**Edit part factory to create edit parts*/
   private EditPartFactory graphViewerEditPartFactory = new CacheEditPartFactory();

   /**Selected object(cache node) in the Cache View to initiate object graph*/
   private Object modelObject;

   private Action clearGraphAction;

   private ICacheRootInstance rootInstance;

   class ClearTableAction extends Action
   {

      public ClearTableAction()
      {
         super(CacheMessages.ObjectGraphView_clearActionText);
         setImageDescriptor(JBossCachePlugin.getDefault().getImageRegistry().getDescriptor(
               ICacheConstants.IMAGE_KEY_DELETE_EDIT));
         setToolTipText(CacheMessages.ObjectGraphView_clearActionText);
         setEnabled(false);
      }

      public void run()
      {
         Tree tree = (Tree) graphViewer.getControl();
         tree.removeAll();
         setEnabled(false);
      }
   }

   /**
    * Constructor
    */
   public CacheGraphView()
   {
      super();
      AbstractCacheRootInstance.addConnectListener(this);
      AbstractCacheInstance.addCacheInstanceListener(this);
      AbstractCacheRootInstance.addCacheRootListener(this);
   }

   /**
    * Contents of the view
    */
   public void createPartControl(Composite parent)
   {
      if (graphViewer == null)
         graphViewer = new TreeViewer();

      graphViewer.createControl(parent);
      graphViewer.setEditPartFactory(graphViewerEditPartFactory);
      graphViewer.setEditDomain(new DefaultEditDomain(null));

      makeActions();
      hookContextMenu();
      contributeToActionBars();

      getSite().setSelectionProvider(graphViewer);
   }

   /**
    * Make actions with this view
    */
   public void makeActions()
   {
      clearGraphAction = new ClearTableAction();
   }

   public void hookContextMenu()
   {

   }

   public void contributeToActionBars()
   {
      IActionBars actionBars = getViewSite().getActionBars();
      actionBars.getToolBarManager().add(clearGraphAction);
      actionBars.getMenuManager().add(clearGraphAction);
   }

   /**
    * Focus on control
    */
   public void setFocus()
   {
      graphViewer.getControl().setFocus();
   }

   /**
    * Get viewer
    * @return
    */
   public TreeViewer getGraphicalViewer()
   {
      return this.graphViewer;
   }

   /**
    * When selection changes in the view
    */
   public void showObjectGraph(Object selection)
   {
      if (!(selection instanceof ICacheRootInstance))
         return;

      ICacheRootInstance rootInstance = (ICacheRootInstance) selection;
      this.rootInstance = rootInstance;

      if ((rootInstance.getRootChilds() != null) && (rootInstance.getRootChilds().size() > 0))
      {
         clearGraphAction.setEnabled(true);
      }
      else
         clearGraphAction.setEnabled(false);

      this.modelObject = selection;

      graphViewer.setContents(modelObject);
   }//end of method

   /**
    * When the view dispose
    */
   public void dispose()
   {
      super.dispose();
      AbstractCacheRootInstance.removeConnectListener(this);
      AbstractCacheInstance.removeCacheInstanceListener(this);
      AbstractCacheRootInstance.removeCacheRootListener(this);
   }

   public void cacheInstanceAdded(ICacheInstance instance)
   {
      if (rootInstance != null)
      {
         if (instance.getRootInstance().equals(rootInstance))
         {
            clearGraphAction.run();
            showObjectGraph(rootInstance);
            if ((rootInstance.getRootChilds() != null) && (rootInstance.getRootChilds().size() > 0))
            {
               clearGraphAction.setEnabled(true);
            }
         }
      }
   }

   public void cacheInstanceRemoved(ICacheInstance instance)
   {
      if (rootInstance != null)
      {
         if (rootInstance.equals(instance.getRootInstance()))
         {
            clearGraphAction.run();
            graphViewer.setContents(rootInstance);
            if ((rootInstance.getRootChilds() != null) && (rootInstance.getRootChilds().size() > 0))
            {
               clearGraphAction.setEnabled(true);
            }
         }
      }
   }

   public void cacheRootInstanceConnected(ICacheRootInstance rootInstance)
   {
      // TODO Auto-generated method stub

   }

   public void cacheRootInstanceDisConnected(ICacheRootInstance rootInstance)
   {
      if (this.rootInstance != null)
      {
         if (rootInstance.equals(this.rootInstance))
         {
            clearGraphAction.run();
            this.rootInstance = null;
         }
      }
   }

   public void cacheRootInstanceAdded(ICacheRootInstance rootInstance)
   {
      //do nothing
   }

   public void cacheRootInstanceRemoved(ICacheRootInstance rootInstance)
   {
      if (this.rootInstance != null)
      {
         if (rootInstance.equals(this.rootInstance))
         {
            clearGraphAction.run();
            this.rootInstance = null;
         }
      }
   }

   public void cacheInstanceToRootAdded(ICacheInstance cacheInstance)
   {
      if (rootInstance != null)
      {
         if (cacheInstance.getRootInstance().equals(this.rootInstance))
         {
            clearGraphAction.run();
            graphViewer.setContents(rootInstance);
            if ((rootInstance.getRootChilds() != null) && (rootInstance.getRootChilds().size() > 0))
            {
               clearGraphAction.setEnabled(true);
            }
         }
      }

   }

   public void cacheInstanceToRootRemoved(ICacheInstance cacheInstance)
   {
      if (rootInstance != null)
      {
         if (cacheInstance.getRootInstance().equals(this.rootInstance))
         {
            clearGraphAction.run();
            graphViewer.setContents(rootInstance);
            if ((rootInstance.getRootChilds() != null) && (rootInstance.getRootChilds().size() > 0))
            {
               clearGraphAction.setEnabled(true);
            }
         }
      }
   }
}//end of class