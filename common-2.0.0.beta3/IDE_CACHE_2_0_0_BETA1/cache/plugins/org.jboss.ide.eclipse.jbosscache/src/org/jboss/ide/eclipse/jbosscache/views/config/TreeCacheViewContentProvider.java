/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.views.config;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheInstance;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheRootInstance;

/**
 * This class provides content for the TreeCacheView
 * @author Gurkaner
 * @see org.jboss.ide.eclipse.jbosscache.views.config.TreeCacheView
 */
public class TreeCacheViewContentProvider implements ITreeContentProvider
{

   public Object[] getChildren(Object parentElement)
   {
      if (parentElement instanceof ICacheRootInstance)
      {
         ICacheRootInstance rootInstance = (ICacheRootInstance) parentElement;
         if (rootInstance.getRootChilds() == null)
         {
            if (rootInstance.getRootInstanceChilds() == null)
               return new Object[0];
            else
               return rootInstance.getRootInstanceChilds().toArray();
         }
         else
            return ((ICacheRootInstance) parentElement).getRootChilds().toArray();
      }
      else if (parentElement instanceof ICacheInstance)
      {
         ICacheInstance cacheInstance = (ICacheInstance) parentElement;
         if (cacheInstance.getInstanceChilds() != null)
         {
            return cacheInstance.getInstanceChilds().toArray();
         }
         else
            return new Object[0];
      }
      else
         return null;
   }

   public Object getParent(Object element)
   {
      if (element instanceof ICacheRootInstance)
      {
         if (((ICacheRootInstance) element).getRootName() == null)
            return null;
         else
            return ((ICacheRootInstance) element).getMainRootCacheInstance();
      }
      else if (element instanceof ICacheInstance)
      {
         ICacheInstance cacheInstance = (ICacheInstance) element;
         if (cacheInstance.getRootInstance() != null)
            return cacheInstance.getRootInstance();
         else if (cacheInstance.getInstanceParent() != null)
            return cacheInstance.getInstanceParent();
         else
            return null;
      }
      else
      {
         return null;
      }
   }

   public boolean hasChildren(Object element)
   {
      if (element instanceof ICacheRootInstance)
      {
         ICacheRootInstance rootInstance = (ICacheRootInstance) element;
         if (rootInstance.getRootName() == null)
         {
            return rootInstance.hasRootInstanceChilds();
         }
         else
         {
            return rootInstance.hasRootChilds();
         }
      }
      else if (element instanceof ICacheInstance)
      {
         ICacheInstance cacheInstance = (ICacheInstance) element;
         return cacheInstance.hasChilds();
      }
      else
         return false;
   }

   public Object[] getElements(Object inputElement)
   {
      return getChildren(inputElement);
   }

   public void dispose()
   {
   }

   public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
   {
   }

}//end of class