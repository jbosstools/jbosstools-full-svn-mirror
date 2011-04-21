/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.wizards;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.Wizard;
import org.jboss.ide.eclipse.jbosscache.JBossCachePlugin;
import org.jboss.ide.eclipse.jbosscache.model.config.CacheConfigurationModel.CacheLoaderConfigInternal;
import org.jboss.ide.eclipse.jbosscache.wizards.pages.CacheLoaderConfigurationPage;

/**
 * Used for getting necessary cache loader configuration informations from the user 
 * @author Gurkaner
 */
public class CacheLoaderConfigurationWizard extends Wizard
{

   /**Cache loader wizard page */
   private CacheLoaderConfigurationPage cacheLoaderConfPage;

   /**Root wizard contains this wizard*/
   private NewCacheConfigurationWizard rootWizard;

   /**
    * Constructor
    * @param wizard
    */
   public CacheLoaderConfigurationWizard(IWizard wizard)
   {
      super();
      this.rootWizard = (NewCacheConfigurationWizard) wizard;
      initializeWizard();
   }

   /**
    * Initialization of this wizard
    *
    */
   protected void initializeWizard()
   {
      setWindowTitle("New CacheLoader Configuration");
      cacheLoaderConfPage = new CacheLoaderConfigurationPage("cachConfPage", "Cache Loader Configuration",
            JBossCachePlugin.getImageDescriptor("icons/sample.gif"));
      addPage(cacheLoaderConfPage);
   }

   /**
    * Before return, implement necessary actions when the finish button pressed in wizard
    * @param none
    * @return true or false 
    */
   public boolean performFinish()
   {
      CacheLoaderConfigInternal cacheLoaderConfig = rootWizard.getCacheConfigurationModel().getCacheLoaderConfig();
      setCacheLoaderConfig(cacheLoaderConfig);
      return true;
   }//end of method

   /**
    * Set the cache loader model information from this wizard page
    * @param cacheLoaderConfig
    */
   private void setCacheLoaderConfig(CacheLoaderConfigInternal cacheLoaderConfig)
   {
      int indexOfCacheLoaderClass = cacheLoaderConfPage.getCmbCacheLoaderClass().getSelectionIndex();

      cacheLoaderConfig.setCacheLoaderAsynchronous(cacheLoaderConfPage.getChkCacheLoaderAsynchronous().getSelection());
      cacheLoaderConfig.setCacheLoaderClass(cacheLoaderConfPage.getCmbCacheLoaderClass().getText());
      cacheLoaderConfig.setCacheLoaderFetchPersistentState(cacheLoaderConfPage.getChkCacheLoaderFetchPersistentState()
            .getSelection());
      cacheLoaderConfig.setCacheLoaderFetchTransientState(cacheLoaderConfPage.getChkCacheLoaderFetchTransientState()
            .getSelection());
      cacheLoaderConfig.setCacheLoaderPassivation(cacheLoaderConfPage.getChkCacheLoaderPassivation().getSelection());
      cacheLoaderConfig.setCacheLoaderPreload(cacheLoaderConfPage.getTxtCacheLoaderPreload().getText());
      cacheLoaderConfig.setCacheLoaderShared(cacheLoaderConfPage.getChkCacheLoaderShared().getSelection());

      switch (indexOfCacheLoaderClass)
      {
         case 0 :

            cacheLoaderConfig.setJdbcCacheLoader(true);
            cacheLoaderConfig.setDataSource(cacheLoaderConfPage.getTxtDataSource().getText());
            cacheLoaderConfig.setDriver(cacheLoaderConfPage.getCmbDriver().getText());
            cacheLoaderConfig.setDriverUrl(cacheLoaderConfPage.getCmbDriverUrl().getText());
            cacheLoaderConfig.setFqnColumn(cacheLoaderConfPage.getTxtFqnColumn().getText());
            cacheLoaderConfig.setFqnType(cacheLoaderConfPage.getTxtFqnType().getText());
            cacheLoaderConfig.setNodeColumn(cacheLoaderConfPage.getTxtNodeColumn().getText());
            cacheLoaderConfig.setNodeType(cacheLoaderConfPage.getTxtNodeType().getText());
            cacheLoaderConfig.setParentColumn(cacheLoaderConfPage.getTxtParentColumn().getText());
            cacheLoaderConfig.setPassword(cacheLoaderConfPage.getTxtPassword().getText());
            cacheLoaderConfig.setTableCreate(cacheLoaderConfPage.getChkTableCreate().getSelection());
            cacheLoaderConfig.setTableDrop(cacheLoaderConfPage.getChkTableDrop().getSelection());
            cacheLoaderConfig.setTableName(cacheLoaderConfPage.getTxtTableName().getText());
            cacheLoaderConfig.setUserName(cacheLoaderConfPage.getTxtUser().getText());

            break;

         case 1 :

            cacheLoaderConfig.setFileCacheLoader(true);
            cacheLoaderConfig.setFileLocation(cacheLoaderConfPage.getTxtLocation().getText());

            break;

         default :
      }

   }//end of method

}//end of class
