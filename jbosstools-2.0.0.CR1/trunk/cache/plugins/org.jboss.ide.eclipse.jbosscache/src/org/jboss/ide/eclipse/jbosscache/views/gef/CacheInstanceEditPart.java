/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.views.gef;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.swt.graphics.Image;
import org.jboss.ide.eclipse.jbosscache.ICacheConstants;
import org.jboss.ide.eclipse.jbosscache.JBossCachePlugin;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheInstance;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheRootInstance;
import org.jboss.ide.eclipse.jbosscache.model.internal.RemoteCacheManager;
import org.jboss.ide.eclipse.jbosscache.model.internal.TreeCacheManager;

/**
 * This class represents cache instance EditPart related with ICacheInstance model 
 * @author Gurkaner
 */
public class CacheInstanceEditPart extends AbstractTreeEditPart
{

   public CacheInstanceEditPart()
   {
      super();
   }

   protected Image getImage()
   {
      Object model = getModel();
      if (model instanceof ICacheRootInstance)
         return JBossCachePlugin.getDefault().getImageRegistry().get(ICacheConstants.IMAGE_KEY_TERM_SBOOK);
      else if (model instanceof ICacheInstance)
         return JBossCachePlugin.getDefault().getImageRegistry().get(ICacheConstants.IMAGE_KEY_SEARCH_REF_OBJ);
      else
         return null;
   }

   protected String getText()
   {
      String returnText = null;
      try
      {
         if (getModel() instanceof ICacheRootInstance)
         {
            ICacheRootInstance rootInstance = (ICacheRootInstance) getModel();
            returnText = rootInstance.getRootName();
         }
         else
         {
            ICacheInstance cacheInstance = (ICacheInstance) getModel();
            
            TreeCacheManager treeCacheManager = null;
            RemoteCacheManager remoteManager = null;
            
            if(!cacheInstance.getRootInstance().isRemoteCache())
               treeCacheManager = cacheInstance.getRootInstance().getTreeCacheManager();
            else
               remoteManager = cacheInstance.getRootInstance().getRemoteCacheManager();
            
            String fqn = cacheInstance.getFqnName();

            
            Class clazz = null;
            
            if(!cacheInstance.getRootInstance().isRemoteCache())
               clazz = (Class) TreeCacheManager.getValue_(treeCacheManager, fqn, "__jboss:internal:class__");
            else
               clazz = (Class) remoteManager.getValue_(fqn, "__jboss:internal:class__");
            //Object value = TreeCacheManager.getObject_(treeCacheManager,fqn);

            if (clazz != null)
            {
               returnText = cacheInstance.getInstanceName() + "->" + clazz.getName();
            }
            else
            {
               returnText = cacheInstance.getInstanceName();
            }
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
         IStatus status = new Status(IStatus.ERROR, ICacheConstants.CACHE_PLUGIN_UNIQUE_ID, IStatus.OK, e.getMessage(),
               e);
         JBossCachePlugin.getDefault().getLog().log(status);
      }
      finally
      {
      }
      return returnText;
   }

   /**
    * Edit policies
    */
   protected void createEditPolicies()
   {

   }

   /**
    * Childs of this cache nodes
    */
   protected List getModelChildren()
   {
      List list = new ArrayList();
      Object model = getModel();

      if (model instanceof ICacheRootInstance)
      {
         ICacheRootInstance rootInstance = (ICacheRootInstance) model;
         List childInstances = rootInstance.getRootChilds();
         if (childInstances != null)
         {
            for (int i = 0; i < childInstances.size(); i++)
            {
               ICacheInstance cacheInstance = (ICacheInstance) childInstances.get(i);
               list.add(cacheInstance);
            }
         }
      }
      else if (model instanceof ICacheInstance)
      {
         ICacheInstance cacheInstance = (ICacheInstance) model;
         List childInstance = cacheInstance.getInstanceChilds();
         if (childInstance != null)
            list.addAll(childInstance);
      }

      return list;
   }

}