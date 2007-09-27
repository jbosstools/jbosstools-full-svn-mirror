/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.views.config;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.jboss.ide.eclipse.jbosscache.ICacheConstants;
import org.jboss.ide.eclipse.jbosscache.JBossCachePlugin;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheInstance;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheRootInstance;

/**
 * This class provides label provider to #TreeCacheView
 * @author Gurkaner
 * @see org.jboss.ide.eclipse.jbosscache.views.config.TreeCacheView
 */
public class TreeCacheViewLabelProvider implements ILabelProvider
{

   private static final String CacheInstance_Disconnected = " [DisConnected]";

   private static final String CacheInstance_Connected = " [Connected]";

   public Image getImage(Object element)
   {
      if (element instanceof ICacheRootInstance)
      {
         ICacheRootInstance instance = (ICacheRootInstance)element;
         if(!instance.isRemoteCache())
            return JBossCachePlugin.getDefault().getImageRegistry().get(ICacheConstants.IMAGE_KEY_DB16_GIF);
         else
         {
            if(instance.isConnected())
               return JBossCachePlugin.getDefault().getImageRegistry().get(ICacheConstants.IMAGE_KEY_SERVER_RUNNING);
            else
               return JBossCachePlugin.getDefault().getImageRegistry().get(ICacheConstants.IMAGE_KEY_SERVER_NOT_RUNNING);
         }
      }
      else if (element instanceof ICacheInstance)
         return JBossCachePlugin.getDefault().getImageRegistry().get(ICacheConstants.IMAGE_KEY_NEWPACK_WIZ);

      else
         return null;
   }

   public String getText(Object element)
   {
      if (element instanceof ICacheRootInstance)
      {
         ICacheRootInstance rootInstance = (ICacheRootInstance) element;
         if (!rootInstance.isConnected())
            return (rootInstance.getRootLabel() + CacheInstance_Disconnected);
         else
            return (rootInstance.getRootLabel() + CacheInstance_Connected);

      }
      else if (element instanceof ICacheInstance)
         return ((ICacheInstance) element).getInstanceName();
      else
         return null;
   }

   public void addListener(ILabelProviderListener listener)
   {
   }

   public void dispose()
   {
   }

   public boolean isLabelProperty(Object element, String property)
   {
      return false;
   }

   public void removeListener(ILabelProviderListener listener)
   {
   }

}
