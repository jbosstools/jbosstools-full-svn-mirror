/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.wizards;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.jboss.ide.eclipse.jbosscache.ICacheConstants;
import org.jboss.ide.eclipse.jbosscache.JBossCachePlugin;
import org.jboss.ide.eclipse.jbosscache.internal.CacheMessages;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheRootInstance;
import org.jboss.ide.eclipse.jbosscache.model.config.RemoteCacheConfigParams;
import org.jboss.ide.eclipse.jbosscache.model.factory.CacheInstanceFactory;
import org.jboss.ide.eclipse.jbosscache.utils.CacheUtil;
import org.jboss.ide.eclipse.jbosscache.wizards.pages.RemoteConfigurationPage;

public class RemoteConfigurationWizard extends Wizard implements INewWizard
{
   private RemoteConfigurationPage remoteConfigPage;
   private RemoteCacheConfigParams remoteConfigParams = null;
   private IWorkbench workbench = null;
   
   
   public RemoteConfigurationWizard(){
      initializeWizard();
   }
   
   private void initializeWizard(){
      setWindowTitle("New Remote Cache Configuration");
      remoteConfigPage = new RemoteConfigurationPage("remoteConfig","Remote Cache Configuration",JBossCachePlugin.getImageDescriptor("icons/new_wiz.gif"));
      addPage(remoteConfigPage);
      setNeedsProgressMonitor(true);
   }

   public boolean performFinish()
   {
      try{
         setRemoteConfigParams();
         String cacheName = remoteConfigPage.getTxtDefaultConfigName().getText().trim();
         String cacheType = remoteConfigPage.getCmbCacheType().getText().trim();
   
         //Name of the Cache Instance Check
         if (!CacheUtil.checkCacheName(cacheName))
         {
            MessageDialog.openError(getShell(),
                  CacheMessages.NewCacheConfigurationWizard_Same_Cache_Instance_Name_ErrorDialog_Title, CacheMessages
                        .bind(CacheMessages.NewCacheConfigurationWizard_Same_Cache_Instance_Name_ErrorDialog_Message,
                              cacheName));
            return false;
         }
         
         ICacheRootInstance rootInstance = CacheInstanceFactory.getCacheRootInstance(cacheName,null);
         rootInstance.setCacheType(cacheType);
         rootInstance.setRemoteCache(true);
         rootInstance.setIsDirty(true);
         rootInstance.setRemoteCacheConfigParams(remoteConfigParams);
         CacheInstanceFactory.getCacheRootMainInstance().addRootInstanceChild(rootInstance);

         if(workbench == null)
            workbench = PlatformUI.getWorkbench();
         
         IPerspectiveRegistry perspReg = workbench.getPerspectiveRegistry();
         IPerspectiveDescriptor desc = perspReg.findPerspectiveWithId(ICacheConstants.CACHE_PERSPECTIVE_ID);
         if (!workbench.getActiveWorkbenchWindow().getActivePage().getPerspective().equals(desc))
         {
            workbench.getActiveWorkbenchWindow().getActivePage().setPerspective(desc);
            System.out.println("Not in active perspective");
         }
                                             
      }catch(Exception e){
         IStatus status = new Status(IStatus.ERROR, ICacheConstants.CACHE_PLUGIN_UNIQUE_ID, IStatus.OK, e.getMessage(),
               e);
         JBossCachePlugin.getDefault().getLog().log(status);
         return false;         
      }
      return true;
   }

   private void setRemoteConfigParams()
   {
      remoteConfigParams = new RemoteCacheConfigParams();
      remoteConfigParams.setJndi(remoteConfigPage.getTxtDefaultJndi().getText().trim());
      remoteConfigParams.setPort(remoteConfigPage.getTxtDefaultPort().getText().trim());
      remoteConfigParams.setUrl(remoteConfigPage.getTxtDefaultUrl().getText().trim());
      remoteConfigParams.setJarList(remoteConfigPage.getJarList());
      
   }

   public void init(IWorkbench workbench, IStructuredSelection selection)
   {
      this.workbench = workbench;
      
   }

}
