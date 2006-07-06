/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.part.ViewPart;
import org.jboss.ide.eclipse.jbosscache.ICacheConstants;
import org.jboss.ide.eclipse.jbosscache.JBossCachePlugin;
import org.jboss.ide.eclipse.jbosscache.internal.CacheMessages;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheRootInstance;
import org.jboss.ide.eclipse.jbosscache.model.config.CacheConfigParams;
import org.jboss.ide.eclipse.jbosscache.model.config.RemoteCacheConfigParams;
import org.jboss.ide.eclipse.jbosscache.model.factory.CacheInstanceFactory;
import org.jboss.ide.eclipse.jbosscache.views.config.TreeCacheView;

/**
 * Duplicate the cache configuration
 * @author Gurkaner
 */
public class DuplicateAction extends AbstractCacheAction
{

   ViewPart viewPart;

   public DuplicateAction(TreeCacheView view, String id)
   {
      super(view, id);
      setImageDescriptor(JBossCachePlugin.getDefault().getImageRegistry().getDescriptor(
            ICacheConstants.IMAGE_KEY_FILE_OBJ));
      setText(CacheMessages.TreeCacheView_duplicateAction);
      setToolTipText(CacheMessages.TreeCacheView_duplicateAction);
      this.viewPart = view;
   }

   public void run()
   {
      ICacheRootInstance originalRootInstance = (ICacheRootInstance) ((TreeCacheView) viewPart).getSelection();
      if (originalRootInstance == null)
         return;

      ICacheRootInstance newRootInstance = CacheInstanceFactory.getCacheRootInstance(CacheMessages.bind(
            CacheMessages.DuplicateAction_Duplicated_CacheName, originalRootInstance.getRootName()),
            originalRootInstance.getRootConfigurationFileName());
      newRootInstance.setCacheType(originalRootInstance.getCacheType());
      newRootInstance.setIsDirty(true);
      CacheConfigParams configParams = null;
      RemoteCacheConfigParams remParams = null;


      List originalJarList = null;
      if(!originalRootInstance.isRemoteCache()){
         
         originalJarList = originalRootInstance.getCacheConfigParams().getConfJarUrls();
         configParams = new CacheConfigParams();         
         
         newRootInstance.setCacheConfigParams(configParams);                  
         
         configParams.setConfDirectoryPath(originalRootInstance.getCacheConfigParams().getConfDirectoryPath());

      }
      else{
         newRootInstance.setRemoteCache(true);
         
         originalJarList = originalRootInstance.getRemoteCacheConfigParams().getJarList();
         
         remParams = new RemoteCacheConfigParams();
         remParams.setJndi(originalRootInstance.getRemoteCacheConfigParams().getJndi());
         remParams.setPort(originalRootInstance.getRemoteCacheConfigParams().getPort());
         remParams.setUrl(originalRootInstance.getRemoteCacheConfigParams().getUrl());
         
         newRootInstance.setRemoteCacheConfigParams(remParams);
      }
      
      List list = new ArrayList();
      if (originalJarList != null)
      {
         for(int i=0;i<originalJarList.size();i++){
            list.add(originalJarList.get(i));
         }
      }

      if(!originalRootInstance.isRemoteCache())
         configParams.setConfJarUrls(list);
      else
         remParams.setJarList(list);
      
      CacheInstanceFactory.getCacheRootMainInstance().addRootInstanceChild(newRootInstance);

   }
}
