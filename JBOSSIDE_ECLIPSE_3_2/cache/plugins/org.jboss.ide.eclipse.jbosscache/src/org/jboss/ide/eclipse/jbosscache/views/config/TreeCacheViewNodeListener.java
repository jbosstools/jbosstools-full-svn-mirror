/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.views.config;

import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.custom.BusyIndicator;
import org.jboss.cache.Fqn;
import org.jboss.cache.TreeCache;
import org.jboss.ide.eclipse.jbosscache.ICacheConstants;
import org.jboss.ide.eclipse.jbosscache.JBossCachePlugin;
import org.jboss.ide.eclipse.jbosscache.model.cache.AbstractTreeCacheManagerListener;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheInstance;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheRootInstance;
import org.jboss.ide.eclipse.jbosscache.model.factory.CacheInstanceFactory;
import org.jboss.ide.eclipse.jbosscache.utils.CacheUtil;

/**
 * This class is related with node operations. 
 * @see org.jboss.ide.eclipse.jbosscache.model.cache.AbstractTreeCacheManagerListener
 * @author Owner
 */
public class TreeCacheViewNodeListener extends AbstractTreeCacheManagerListener
{

   public TreeCacheViewNodeListener(ICacheRootInstance rootInstance)
   {
      super(rootInstance);
   }

   /**
    * @see AbstractTreeCacheManagerListener#nodeCreated(Fqn)
    */
   public void nodeCreated(Fqn fqnx)
   {
      final Fqn fqn = fqnx;

//      if (fqnx.toString().equals(ICacheConstants.SEPERATOR + ICacheConstants.JBOSS_INTERNAL)
//            || fqnx.toString().equals(ICacheConstants.SEPERATOR + ICacheConstants.JBOSS_INTERNAL_REF_MAP))
//         return;

      //TODO New Thread creation required
      JBossCachePlugin.getDisplay().asyncExec(new Runnable()
      {

         public void run()
         {
            StringTokenizer tokenize = null;
            String name = null;
            StringBuffer parentName = new StringBuffer();
            ICacheRootInstance rootInstance = getICacheRootInstance();
            List instanceChilds = rootInstance.getRootChilds();
            tokenize = new StringTokenizer(fqn.toString(), ICacheConstants.SEPERATOR);
            if (tokenize != null)
            {
               int tokenizeNumber = tokenize.countTokens();
               
               if(tokenizeNumber == 0)//This is /
                  return;
               
               /*This is root node*/
               if (tokenizeNumber == 1)
               {
                  name = tokenize.nextToken();
                  ICacheInstance cacheInstance = CacheInstanceFactory.getCacheInstance(name, ICacheConstants.SEPERATOR
                        + name, null);
                  rootInstance.addRootChild(cacheInstance);
                  instanceChilds = rootInstance.getRootChilds();
               }
               else
               {
                  for (int i = 0,j=tokenizeNumber; i < j - 1; i++)
                  {
                     parentName.append(ICacheConstants.SEPERATOR).append(tokenize.nextToken());
                  }

//                  if (parentName.equals(ICacheConstants.SEPERATOR + ICacheConstants.JBOSS_INTERNAL))
//                     return;

                  name = tokenize.nextToken();
//                  if (name.equals(ICacheConstants.JBOSS_INTERNAL))
//                     return;

                  Iterator it = instanceChilds.iterator();
                  boolean found = false;
                  try
                  {
                     while (it.hasNext())
                     {
                        ICacheInstance childInstance = (ICacheInstance) it.next();

                        if (CacheUtil.findParentOfChild(childInstance, parentName.toString()) == null)
                           continue;
                        else
                        {
                           ICacheInstance cacheInstance = CacheUtil.findParentOfChild(childInstance, parentName
                                 .toString());
                           childInstance = CacheInstanceFactory.getCacheInstance(name, parentName
                                 + ICacheConstants.SEPERATOR + name, cacheInstance);
                           if (cacheInstance.getRootInstance() != null)
                              childInstance.setRootInstance(cacheInstance.getRootInstance());

                           cacheInstance.addInstanceChild(childInstance);

                           found = true;
                           break;
                        }
                     }
                     if (!found)
                        throw new Exception("Parent fqn not found in the cache");
                  }
                  catch (Exception e)
                  {
                     IStatus status = new Status(Status.ERROR, ICacheConstants.CACHE_PLUGIN_UNIQUE_ID, Status.OK, e
                           .getMessage(), e);
                     JBossCachePlugin.getDefault().getLog().log(status);
                  }
               }//end of else						
            }//end of if tokenize != null									
         }
      });
   }//end of method

   /**
    * When node is removed from the cache
    */
   public void nodeRemoved(Fqn fqnx)
   {

      final Fqn fqn = fqnx;

      final ICacheRootInstance rootInstance = getICacheRootInstance();
      List instanceChilds = rootInstance.getRootChilds();

      boolean found = false;
      try
      {
         for (int i = 0; i < instanceChilds.size(); i++)
         {
            ICacheInstance cacheInstance = (ICacheInstance) instanceChilds.get(i);
            final ICacheInstance cacheInstance2 = CacheUtil.findParentOfChild(cacheInstance, fqn.toString());
            if (cacheInstance2 != null)
            {
               JBossCachePlugin.getDisplay().asyncExec(new Runnable()
               {
                  public void run()
                  {
                     if (cacheInstance2.isRootNode())
                        rootInstance.deleteRootChild(cacheInstance2);
                     else
                     {
                        ICacheInstance cacheInstanceParent = cacheInstance2.getInstanceParent();
                        cacheInstanceParent.removeInstanceChild(cacheInstance2);
                     }
                  }
               });
               found = true;
               break;
            }
            else
               continue;
         }
         if (!found)
            throw new Exception("Cache node with given fqn not found");
      }
      catch (Exception e)
      {
         IStatus status = new Status(Status.ERROR, ICacheConstants.CACHE_PLUGIN_UNIQUE_ID, Status.OK, e.getMessage(), e);
         JBossCachePlugin.getDefault().getLog().log(status);
      }
   }//end of node remove

   public void cacheStarted(TreeCache manager)
   {
      System.out.println("Tree Cache Started");
   }

   public void cacheStopped(TreeCache manager)
   {
      System.out.println("Tree Cache stopped");
   }

}
