/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.views.content;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.jboss.cache.aop.AOPInstance;
import org.jboss.cache.aop.InternalDelegate;
import org.jboss.ide.eclipse.jbosscache.ICacheConstants;
import org.jboss.ide.eclipse.jbosscache.JBossCachePlugin;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheInstance;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheRootInstance;
import org.jboss.ide.eclipse.jbosscache.model.internal.RemoteCacheManager;
import org.jboss.ide.eclipse.jbosscache.model.internal.TreeCacheManager;
import org.jboss.ide.eclipse.jbosscache.utils.CacheUtil;

/**
 * It is related with model of the table
 * @author Gurkaner
 */
public class TableContentProvider implements IStructuredContentProvider
{

   private TableContentModel[] contentModel;

   public static class TableContentModel
   {
      private static TreeCacheManager cacheManager;
      private static RemoteCacheManager remoteCacheManager;
      private static boolean isRemoteCache;
      
      private String parentFqn;

      private String fqn;

      private Object key;

      private Object value;

      TableContentModel(String parentFqn, String fqn, Object key, Object value)
      {
         this.parentFqn = parentFqn;
         this.fqn = fqn;
         this.key = key;
         this.value = value;
      }

      public String getFqn()
      {
         return fqn;
      }

      public void setFqn(String fqn)
      {
         this.fqn = fqn;
      }

      public Object getKey()
      {
         return key;
      }

      public void setKey(Object key)
      {
         this.key = key;
      }

      public String getParentFqn()
      {
         return parentFqn;
      }

      public void setParentFqn(String parentFqn)
      {
         this.parentFqn = parentFqn;
      }

      public Object getValue()
      {
         return value;
      }

      public void setValue(Object value)
      {
         this.value = value;
      }

      public static TreeCacheManager getCacheManager()
      {
         return cacheManager;
      }

      public static void setCacheManager(TreeCacheManager cacheManager)
      {
         TableContentModel.cacheManager = cacheManager;
      }
      
      public static RemoteCacheManager getRemoteCacheManager()
      {
         return remoteCacheManager;
      }

      public static void setRemoteCacheManager(RemoteCacheManager cacheManager)
      {
         TableContentModel.remoteCacheManager = cacheManager;
      }

      public static boolean isRemoteCache()
      {
         return isRemoteCache;
      }

      public static void setRemoteCache(boolean isRemoteCache)
      {
         TableContentModel.isRemoteCache = isRemoteCache;
      }
      
   }

   public Object[] getElements(Object inputElement)
   {
      List arrayList = new ArrayList();
      if (inputElement instanceof ICacheInstance)
      {
         ICacheInstance cacheInstance = (ICacheInstance) inputElement;
         ICacheRootInstance rootInstance = cacheInstance.getRootInstance();
         
         
         TreeCacheManager manager = null;
         RemoteCacheManager remoteManager = null;
         
         if(!rootInstance.isRemoteCache()){
            manager = cacheInstance.getRootInstance().getTreeCacheManager();
            TableContentModel.setCacheManager(manager);
            TableContentModel.setRemoteCache(false);
         }else{
            remoteManager = rootInstance.getRemoteCacheManager();
            TableContentModel.setRemoteCacheManager(remoteManager);
            TableContentModel.setRemoteCache(true);
         }
         
         boolean isTreeCacheAop = false;
         if (rootInstance.getCacheType().equals(ICacheConstants.JBOSS_CACHE_TREE_CACHE_AOP))
            isTreeCacheAop = true;
         try
         {
            Set set = null;
            
            if(!rootInstance.isRemoteCache())
               set = TreeCacheManager.getKeys_(manager, cacheInstance.getFqnName());
            else
               set = remoteManager.getKeys_(cacheInstance.getFqnName());
               
            if (set != null)
            {
               Iterator it = set.iterator();
               if (it != null)
               {
                  while (it.hasNext())
                  {
                     Object key = it.next();
                     Object value = null;
                     
                     if(!rootInstance.isRemoteCache())
                        value = TreeCacheManager.getValue_(manager, cacheInstance.getFqnName(), key);
                     else
                        value = remoteManager.getValue_(cacheInstance.getFqnName(),key);
                               

                     if (value == null)
                        value = "";
                     
                     if(key == null)
                        key = "";

                     if (isTreeCacheAop)
                     {
                        if (key.toString().equals(AOPInstance.KEY))
                        {
                           Map fields = CacheUtil.getAllFields(value.getClass());
                           Set keySet = fields.keySet();
                           if (keySet != null)
                           {
                              Iterator itFields = keySet.iterator();
                              boolean isReferencing = false;
                              while (itFields.hasNext())
                              {
                                 Field field = (Field) fields.get(itFields.next().toString());
                                 //Look referenced object if exist
                                 String fieldName = field.getName();
                                 if (fieldName.equals(ICacheConstants.REFERENCED_CACHE_KEY))
                                 {
                                    Object fieldValue = field.get(value);
                                    if (fieldValue != null)
                                    {
                                       isReferencing = true;
                                       key = "Referencing to --> ";
                                       
                                       if(!rootInstance.isRemoteCache())
                                       value = TreeCacheManager.getValue_(manager, InternalDelegate.JBOSS_INTERNAL_MAP.toString()+"/"+fieldValue.toString(), fieldValue.toString());
                                       else
                                          value = remoteManager.getValue_(InternalDelegate.JBOSS_INTERNAL_MAP.toString()+"/"+fieldValue.toString(), fieldValue.toString());
                                       
                                       if(value == null)
                                          value = "";
                                       break;
                                    }
                                 }
                              }
                              if (!isReferencing)
                                 continue;
                           }

                        }
                        else if (key.toString().equals(ICacheConstants.JBOSS_CLASS_INTERNAL))
                        {
                           //Not show internal class
                           continue;
                        }
                     }

                     
                     cacheInstance.addNewCacheInstanceKey(key, value);
                     TableContentModel model = null;
                     if (cacheInstance.isRootNode())
                     {
                        model = new TableContentModel(ICacheConstants.SEPERATOR, cacheInstance.getFqnName(), key, value);
                        arrayList.add(model);
                     }
                     else
                     {
                        model = new TableContentModel(cacheInstance.getInstanceParent().getFqnName(), cacheInstance
                              .getFqnName(), key, value);
                        arrayList.add(model);
                     }
                  }
               }

               this.contentModel = (TableContentModel[]) arrayList.toArray(new TableContentModel[arrayList.size()]);
               return arrayList.toArray();
            }

         }
         catch (Exception e)
         {
            e.printStackTrace();
            IStatus status = new Status(IStatus.ERROR, ICacheConstants.CACHE_PLUGIN_UNIQUE_ID, IStatus.OK, e
                  .getMessage(), e);
            JBossCachePlugin.getDefault().getLog().log(status);
         }
      }
      return new Object[0];
   }

   public void dispose()
   {

   }

   public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
   {

   }

   /**
    * Return content model
    * @return
    */
   public TableContentModel[] getContentModel()
   {
      return this.contentModel;
   }

}
