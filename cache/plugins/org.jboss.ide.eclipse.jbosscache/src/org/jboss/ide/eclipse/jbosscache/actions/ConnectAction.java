/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.actions;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;
import org.jboss.ide.eclipse.jbosscache.ICacheConstants;
import org.jboss.ide.eclipse.jbosscache.JBossCachePlugin;
import org.jboss.ide.eclipse.jbosscache.internal.CacheMessages;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheInstance;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheRootInstance;
import org.jboss.ide.eclipse.jbosscache.model.factory.CacheInstanceFactory;
import org.jboss.ide.eclipse.jbosscache.model.internal.TreeCacheManager;
import org.jboss.ide.eclipse.jbosscache.utils.CacheClassLoader;
import org.jboss.ide.eclipse.jbosscache.utils.CacheUtil;
import org.jboss.ide.eclipse.jbosscache.views.config.TreeCacheView;

/**
 * Connect to the given cache related with its configuration file
 * @author Gurkaner
 */
public class ConnectAction extends AbstractCacheAction
{

   /**
    * Constructor
    * @param view
    * @param id
    */
   public ConnectAction(TreeCacheView view, String id)
   {
      super(view, id);

   }

   /**
    * Real action
    */
   public void run()
   {

      ICacheRootInstance rootInstance = (ICacheRootInstance) getTreeViewer().getSelection();
      try
      {
         if (configureCache(rootInstance))
         {            
            if (!rootInstance.isConnected())
               rootInstance.setConnected(true);

            rootInstance.connect();
         }

      }
      catch (Exception e)
      {
         IStatus status = new Status(IStatus.ERROR, ICacheConstants.CACHE_PLUGIN_UNIQUE_ID, IStatus.OK, e.getMessage(),
               e);
         ErrorDialog.openError(getTreeViewer().getShell(), CacheUtil
               .getResourceBundleValue(ICacheConstants.TREECACHEVIEW_CONNECT_ACTION_ERROR_DIALOG_TITLE), CacheUtil
               .getResourceBundleValue(ICacheConstants.TREECACHEVIEW_CONNECT_ACTION_ERROR_DIALOG_MESSAGE), status);
         JBossCachePlugin.getDefault().getLog().log(status);
      }

   }//end of run

   /**
    * Conifgure the TreeCache with this root instances configuration file
    *
    */
   private boolean configureCache(final ICacheRootInstance rootInstance) throws Exception
   {

      final ClassLoader cl = Thread.currentThread().getContextClassLoader();

      final List jarPaths = rootInstance.getCacheConfigParams().getConfJarUrls();

      boolean isTreeCacheAop = false;
      if (rootInstance.getCacheType().equals(ICacheConstants.JBOSS_CACHE_TREE_CACHE_AOP))
         isTreeCacheAop = true;

      final TreeCacheManager treeCacheManager = TreeCacheManager.getTreeCacheManagerInstance(isTreeCacheAop);
      
      ProgressMonitorDialog monitorDialog = new ProgressMonitorDialog(getTreeViewer().getShell());
      monitorDialog.setCancelable(true);

      final File file = new File(rootInstance.getRootConfigurationFileName());
      if (!file.exists())
      {
         MessageDialog.openError(getTreeViewer().getShell(), CacheUtil
               .getResourceBundleValue(ICacheConstants.TREECACHEVIEW_CONNECT_ACTION_MESSAGE_DIALOG_TITLE), CacheUtil
               .getResourceBundleValue(ICacheConstants.TREECACHEVIEW_CONNECT_ACTION_MESSAGE_DIALOG_MESSAGE));
         return false;
      }

      IRunnableWithProgress runnable = new IRunnableWithProgress()
      {

         public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
         {
            ClassLoader thCl = Thread.currentThread().getContextClassLoader().getParent();
            try
            {
               monitor.beginTask(CacheMessages.ConnectAction_Monitor_Dialog_Task_Name, 5);
               FileInputStream stream = new FileInputStream(file);

               if (monitor.isCanceled())
                  throw new InterruptedException();

               TreeCacheManager.configureTreeCacheManager(treeCacheManager, stream);
               monitor.worked(1);

               //Check jars
               if (jarPaths != null)
                  Thread.currentThread().setContextClassLoader(
                        CacheClassLoader.getCacheClassLoaderInstance(jarPaths, cl));

               treeCacheManager.setManagerClassLoader(Thread.currentThread().getContextClassLoader());

               TreeCacheManager.createService_(treeCacheManager);
               monitor.worked(1);

               if (monitor.isCanceled())
                  throw new InterruptedException();

               TreeCacheManager.startService_(treeCacheManager);
               monitor.worked(2);

               if (monitor.isCanceled())
                  throw new InterruptedException();
               
               monitor.subTask(CacheMessages.ConnectAction_Job_Monitor_Dialog_Task_Name);
               //More Good solution? Just wait getting contents of the cache???
               Thread.sleep(5000);
               monitor.worked(1);
               monitor.done();
            }
            catch (InterruptedException e)
            {
               throw e;
            }
            catch (Exception e)
            {
               throw new InvocationTargetException(e);
            }
            finally
            {
               Thread.currentThread().setContextClassLoader(thCl);
            }
         }

      };
      

      try
      {
         monitorDialog.run(true, true, runnable);
      }
      catch (InvocationTargetException e)
      {
         throw new Exception(e);
      }
      catch (InterruptedException e)
      {
         if (treeCacheManager != null)
         {
            TreeCacheManager.stopService_(treeCacheManager);
            TreeCacheManager.destroyService_(treeCacheManager);
         }
         return false;
      }

         rootInstance.setTreeCacheManager(treeCacheManager);
              
          Set set = null;
          set = TreeCacheManager.getChildrenNames_(treeCacheManager, ICacheConstants.SEPERATOR);
      
          if (set != null)
          {
             if (!set.isEmpty())
             {
                Iterator it = set.iterator();
                while (it.hasNext())
                {
                   String nodeName = it.next().toString();
                   //__JBossInternal__ node is not shown to the user
                   if (!nodeName.equals(ICacheConstants.JBOSS_INTERNAL)){
                         addTreeNodes(treeCacheManager, rootInstance, nodeName, ICacheConstants.SEPERATOR + nodeName);
                   }
                }
             }
      
          }
      
      return true;
   }

   /**
    * Add cache nodes to the tree
    * @param manager
    * @param obj Parent obj (1. ICacheRootInstance or ICacheInstance)
    * @param nodeName
    * @throws Exception
    */
   private void addTreeNodes(TreeCacheManager manager, Object obj, String nodeName, String fqn) throws Exception
   {
      ICacheRootInstance cacheRootInstance = null;
      ICacheInstance cacheInstance = null;
      ICacheInstance childInstance = null;
      Set childSet = null;
      Iterator it = null;
      String childName = null;
      //We add tree cache node roots below the CacheRootInstance
      if (obj instanceof ICacheRootInstance)
      {
         cacheRootInstance = (ICacheRootInstance) obj;
         cacheInstance = CacheInstanceFactory.getCacheInstance(nodeName, fqn, null);
         cacheRootInstance.addRootChild(cacheInstance);

         childSet = TreeCacheManager.getChildrenNames_(manager, fqn);
         if (childSet != null)
         {
            if (!childSet.isEmpty())
            {
               it = childSet.iterator();
               while (it.hasNext())
               {
                  childName = it.next().toString();
                  addTreeNodes(manager, cacheInstance, childName, fqn + ICacheConstants.SEPERATOR + childName);
               }
            }
         }
      }//end of outer if
      else if (obj instanceof ICacheInstance)
      {
         cacheInstance = (ICacheInstance) obj;
         childInstance = CacheInstanceFactory.getCacheInstance(nodeName, fqn, cacheInstance);
         cacheInstance.addInstanceChild(childInstance);
         if (cacheInstance.getRootInstance() != null)
            childInstance.setRootInstance(cacheInstance.getRootInstance());

         Set newSet = TreeCacheManager.getChildrenNames_(manager, fqn);
         if (newSet != null)
         {
            if (!newSet.isEmpty())
            {
               Iterator itNext = newSet.iterator();
               while (itNext.hasNext())
               {
                  childName = itNext.next().toString();
                  addTreeNodes(manager, childInstance, childName, fqn + ICacheConstants.SEPERATOR + childName);
               }
            }
         }
      }
   }
}//end of class