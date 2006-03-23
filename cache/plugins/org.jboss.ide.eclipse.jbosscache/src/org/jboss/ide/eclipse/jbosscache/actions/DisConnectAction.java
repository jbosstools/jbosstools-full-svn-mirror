/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.actions;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.jboss.ide.eclipse.jbosscache.ICacheConstants;
import org.jboss.ide.eclipse.jbosscache.JBossCachePlugin;
import org.jboss.ide.eclipse.jbosscache.internal.CacheMessages;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheRootInstance;
import org.jboss.ide.eclipse.jbosscache.model.internal.TreeCacheManager;
import org.jboss.ide.eclipse.jbosscache.views.config.TreeCacheView;

/**
 * DisConnect from the given cache root instance related with
 * its configuration file
 * @author Gurkaner
 *
 */
public class DisConnectAction extends AbstractCacheAction
{

   /**
    * Constructor
    * @param view
    * @param id
    */
   public DisConnectAction(TreeCacheView view, String id)
   {
      super(view, id);
   }

   /**
    * Real action
    */
   public void run()
   {
      ICacheRootInstance rootInstance = (ICacheRootInstance) getTreeViewer().getSelection();

      rootInstance.disconnect();
      
      TreeCacheManager manager = rootInstance.getTreeCacheManager();
      
      if(rootInstance.isRemoteCache())
      {
         rootInstance.setRemoteCacheManager(null);
      }
      else
      {
         if (manager != null)
         {
            ProgressMonitorDialog monitorDialog = new ProgressMonitorDialog(getTreeViewer().getShell());
            try{
               monitorDialog.run(true,false,getRunnable(manager));
            }catch(Exception e){
               e.printStackTrace();
               IStatus status = new Status(IStatus.ERROR, ICacheConstants.CACHE_PLUGIN_UNIQUE_ID, IStatus.OK, e
                     .getMessage(), e);
               JBossCachePlugin.getDefault().getLog().log(status);            
            }
            
            rootInstance.setTreeCacheManager(null);

         }         
      }

   }//end of method
   
   
   public IRunnableWithProgress getRunnable(final TreeCacheManager manager){
      return new IRunnableWithProgress(){

         public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
         {
            monitor.beginTask(CacheMessages.DisConnectAction_Monitor_Dialog_Task_Name,5);
            monitor.worked(3);
            TreeCacheManager.stopService_(manager);
            monitor.worked(5);
            TreeCacheManager.destroyService_(manager);
            monitor.done();
         }         
      };
   }
   
   
}//end of class
