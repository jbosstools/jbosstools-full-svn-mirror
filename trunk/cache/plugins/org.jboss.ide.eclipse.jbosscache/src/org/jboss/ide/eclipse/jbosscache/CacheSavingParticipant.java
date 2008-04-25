/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache;

import java.io.File;
import java.util.List;

import org.eclipse.core.resources.ISaveContext;
import org.eclipse.core.resources.ISaveParticipant;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheRootInstance;
import org.jboss.ide.eclipse.jbosscache.model.factory.CacheInstanceFactory;
import org.jboss.ide.eclipse.jbosscache.utils.CacheUtil;

public class CacheSavingParticipant implements ISaveParticipant
{

   private static boolean isDelete = false;

   public void doneSaving(ISaveContext context)
   {

      if (isDelete)
      {
         JBossCachePlugin plugin = JBossCachePlugin.getDefault();

         // delete the old saved state since it is not necessary anymore
         int previousSaveNumber = context.getPreviousSaveNumber();
         String oldFileName = CacheUtil
               .getResourceBundleValue(ICacheConstants.JBOSS_CACHE_PLUGIN_CACHE_SAVING_PARTICIPANT_LOGICAL_FILE_NAME)
               + Integer.toString(previousSaveNumber);
         File f = plugin.getStateLocation().append(oldFileName).toFile();
         f.delete();
      }

   }

   public void prepareToSave(ISaveContext context) throws CoreException
   {
      // TODO Auto-generated method stub

   }

   public void rollback(ISaveContext context)
   {
      // TODO Auto-generated method stub

   }

   public void saving(ISaveContext context) throws CoreException
   {
      switch (context.getKind())
      {
         case ISaveContext.FULL_SAVE :

            ICacheRootInstance mainRoot = CacheInstanceFactory.getCacheRootMainInstance();
            List childs = mainRoot.getRootInstanceChilds();
            int size = 0;
            boolean isAnythingChanged = false;
            if (childs != null)
            {
               size = childs.size();
            }

            for (int i = 0; i < size; i++)
            {
               ICacheRootInstance rootInstance = (ICacheRootInstance) childs.get(i);
               if (rootInstance.getIsDirty())
               {
                  isAnythingChanged = true;
                  break;
               }
            }

            if ((size != JBossCachePlugin.getNumberOfCacheInstances()) || isAnythingChanged)
            {
               JBossCachePlugin plugin = JBossCachePlugin.getDefault();
               int saveNumber = context.getSaveNumber();
               String saveFileName = CacheUtil
                     .getResourceBundleValue(ICacheConstants.JBOSS_CACHE_PLUGIN_CACHE_SAVING_PARTICIPANT_LOGICAL_FILE_NAME)
                     + Integer.toString(saveNumber);
               File f = plugin.getStateLocation().append(saveFileName).toFile();
               try
               {
                  plugin.writeStateToFile(f);
               }
               catch (Exception e)
               {
                  System.out.println("Failed to save Tree Cache Nodes...");
                  IStatus status = new Status(IStatus.ERROR, ICacheConstants.CACHE_PLUGIN_UNIQUE_ID, IStatus.OK, e
                        .getMessage(), e);
                  JBossCachePlugin.getDefault().getLog().log(status);
                  throw new CoreException(status);
               }
               context.map(new Path("save"), new Path(saveFileName));
               context.needSaveNumber();
               isDelete = true;
            }
            else
               isDelete = false;
            break;
         case ISaveContext.PROJECT_SAVE :
            break;
         case ISaveContext.SNAPSHOT :
            break;
      }

   }

}