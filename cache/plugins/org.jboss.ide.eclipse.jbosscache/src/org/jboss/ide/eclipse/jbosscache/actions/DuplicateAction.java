/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.ui.part.ViewPart;
import org.jboss.ide.eclipse.jbosscache.ICacheConstants;
import org.jboss.ide.eclipse.jbosscache.JBossCachePlugin;
import org.jboss.ide.eclipse.jbosscache.internal.CacheMessages;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheRootInstance;
import org.jboss.ide.eclipse.jbosscache.model.config.CacheConfigParams;
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
      CacheConfigParams configParams = new CacheConfigParams();
      newRootInstance.setCacheConfigParams(configParams);
      configParams.setConfDirectoryPath(originalRootInstance.getCacheConfigParams().getConfDirectoryPath());

      List list = new ArrayList();

      List originalJarList = originalRootInstance.getCacheConfigParams().getConfJarUrls();
      if (originalJarList != null)
      {
         Collections.copy(list, originalJarList);
      }

      configParams.setConfJarUrls(list);
      CacheInstanceFactory.getCacheRootMainInstance().addRootInstanceChild(newRootInstance);

   }
}
