/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.wizards;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.part.ViewPart;
import org.hibernate.tool.hbm2x.XMLPrettyPrinter;
import org.jboss.ide.eclipse.jbosscache.ICacheConstants;
import org.jboss.ide.eclipse.jbosscache.JBossCachePlugin;
import org.jboss.ide.eclipse.jbosscache.editors.input.CacheFileEditorInput;
import org.jboss.ide.eclipse.jbosscache.internal.CacheMessages;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheRootInstance;
import org.jboss.ide.eclipse.jbosscache.model.config.CacheConfigurationModel;
import org.jboss.ide.eclipse.jbosscache.model.config.CacheConfigurationModel.CacheLoaderConfigInternal;
import org.jboss.ide.eclipse.jbosscache.model.factory.CacheInstanceFactory;
import org.jboss.ide.eclipse.jbosscache.utils.CacheUtil;
import org.jboss.ide.eclipse.jbosscache.wizards.pages.CacheLoaderConfigurationPage;
import org.jboss.ide.eclipse.jbosscache.wizards.pages.StandardConfigurationPage;

/**
 * Used for getting necessary configuration informations from the user
 * 
 * @author Gurkaner
 */
public class NewCacheConfigurationWizard extends Wizard implements INewWizard {

	/* First wizard page of this wizard */
	private StandardConfigurationPage stdConfPage;

	// /*Cache loader wizard page*/
	// private CacheLoaderConfigurationPage cacheLoaderConfPage;

	private WizardNewFileCreationPage creationPage;

	private IStructuredSelection selection;

	private ViewPart viewPart;

	private CacheConfigurationModel cacheConfigModel;

	private IWorkbench workBench;

	public NewCacheConfigurationWizard() {
		super();
	}

	public NewCacheConfigurationWizard(ViewPart viewPart) {
		super();
		this.viewPart = viewPart;
		cacheConfigModel = new CacheConfigurationModel();
		setNeedsProgressMonitor(true);
		initializeWizard();
	}

	/**
	 * Initialization of this wizard
	 * 
	 */
	protected void initializeWizard() {

		setWindowTitle("New Cache Configuration");
		stdConfPage = new StandardConfigurationPage("stdConfPage",
				"Base Cache Configuration", JBossCachePlugin
						.getImageDescriptor("icons/new_wiz.gif"));
		addPage(stdConfPage);
		// cacheLoaderConfPage = new
		// CacheLoaderConfigurationPage("cachConfPage", "Cache Loader
		// Configuration",
		// JBossCachePlugin.getImageDescriptor("icons/new_wiz.gif"));
		// addPage(cacheLoaderConfPage);

	}

	/**
	 * Before return, implement necessary actions when the finish button pressed
	 * in wizard
	 * 
	 * @param none
	 * @return true or false
	 */
	public boolean performFinish() {
		try {
			setStdConfigModel();
			// setCacheLoaderConfig(getCacheConfigurationModel().getCacheLoaderConfig());
			String cacheName = stdConfPage.getTxtCacheName().getText();
			String confFileName = stdConfPage.getTxtConfFileName().getText();
			// File file =
			// CacheUtil.createNewFile(cacheConfigModel.getCacheName()+ICacheConstants.XML_FILEEXTENSION);
			final File file = new Path(cacheConfigModel.getDirectoryName()
					+ File.separatorChar + confFileName
					+ ICacheConstants.XML_FILEEXTENSION).toFile();
			if (file.exists()) {
				MessageDialog
						.openError(
								getShell(),
								CacheUtil
										.getResourceBundleValue(ICacheConstants.CONFIGURATION_ERROR_MESSAGE_CACHEEXIST_TITLE),
								CacheUtil
										.getResourceBundleValue(ICacheConstants.CONFIGURATION_ERROR_MESSAGE_CACHEEXIST));
				return false;
			}

			// Name of the Cache Instance Check
			if (!CacheUtil.checkCacheName(cacheName)) {
				MessageDialog
						.openError(
								getShell(),
								CacheMessages.NewCacheConfigurationWizard_Same_Cache_Instance_Name_ErrorDialog_Title,
								CacheMessages
										.bind(
												CacheMessages.NewCacheConfigurationWizard_Same_Cache_Instance_Name_ErrorDialog_Message,
												cacheName));
				return false;
			}

			IRunnableWithProgress prog = new IRunnableWithProgress() {

				public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {
					monitor
							.beginTask(
									JBossCachePlugin
											.getResourceBundle()
											.getString(
													ICacheConstants.CONFIGURATION_MONITOR_TASK_NAME),
									10);
					try {
						monitor.worked(1);
						CacheUtil.saveMetaDataInformation(cacheConfigModel,
								monitor, file);
					} catch (Exception e) {
						throw new InvocationTargetException(e);
					} finally {
						monitor.done();
					}
				}

			};
			getContainer().run(true, false, prog);

			ICacheRootInstance rootInstance = CacheInstanceFactory
					.getCacheRootInstance(cacheName, cacheConfigModel
							.getDirectoryName()
							+ File.separatorChar + confFileName + ".xml");
			rootInstance.setIsDirty(true);
			rootInstance.setCacheConfigParams(stdConfPage.getConfigParams());
			rootInstance.setCacheType(stdConfPage.getCmbCacheType().getText());
			CacheInstanceFactory.getCacheRootMainInstance()
					.addRootInstanceChild(rootInstance);

			/*
			 * Set Cache Perspective
			 */
			if (workBench == null)
				workBench = viewPart.getSite().getWorkbenchWindow()
						.getWorkbench();

			IPerspectiveRegistry perspReg = workBench.getPerspectiveRegistry();
			IPerspectiveDescriptor desc = perspReg
					.findPerspectiveWithId(ICacheConstants.CACHE_PERSPECTIVE_ID);
			if (!workBench.getActiveWorkbenchWindow().getActivePage()
					.getPerspective().equals(desc)) {
				workBench.getActiveWorkbenchWindow().getActivePage()
						.setPerspective(desc);
				System.out.println("Not in active perspective");
			}

			FileInputStream inStream = new FileInputStream(file);
			ByteArrayOutputStream os = new ByteArrayOutputStream();

			XMLPrettyPrinter.prettyPrint(inStream, os);

			FileOutputStream outStream = new FileOutputStream(file);

			outStream.write(os.toByteArray());
			outStream.flush();
			outStream.close();
			inStream.close();

			CacheFileEditorInput edInput = new CacheFileEditorInput(file);

			PlatformUI
					.getWorkbench()
					.getActiveWorkbenchWindow()
					.getActivePage()
					.openEditor(edInput,
							"org.eclipse.wst.xml.ui.internal.tabletree.XMLMultiPageEditorPart");

		} catch (Exception e) {
			IStatus status = new Status(IStatus.ERROR,
					ICacheConstants.CACHE_PLUGIN_UNIQUE_ID, IStatus.OK, e
							.getMessage(), e);
			JBossCachePlugin.getDefault().getLog().log(status);
			return false;
		}
		return true;
	}// end of method

	/**
	 * Get standart configuration model information from this wizard page
	 * 
	 * @param none
	 * @return void
	 */
	private void setStdConfigModel() {
		cacheConfigModel.setCacheType(stdConfPage.getCmbCacheType().getText()
				.trim());
		cacheConfigModel.setDirectoryName(stdConfPage.getTxtDirectoryName()
				.getText().trim());
		cacheConfigModel.setCacheName(stdConfPage.getTxtCacheName().getText()
				.trim());
		cacheConfigModel.setCacheMode(stdConfPage.getCmbCacheMode().getText()
				.trim());
		cacheConfigModel.setClusterName(stdConfPage.getTxtClusterName()
				.getText().trim());
		cacheConfigModel.setTransManagerLookUpClass(stdConfPage
				.getCmbTransactionManager().getText().trim());
		cacheConfigModel.setNodeLockingSchema(stdConfPage
				.getCmbNodeLockingScheme().getText().trim());
		cacheConfigModel.setEvictionPolicyClass(stdConfPage
				.getCmbDefaultEvcitionPolicy().getText().trim());
		cacheConfigModel.setCacheLoaderClass(stdConfPage
				.getCmbCacheLoaderClass().getText().trim());
		cacheConfigModel.setBodyReplicationEnabled(stdConfPage
				.getChkBuddyReplicationEnabled().getSelection());
		cacheConfigModel.setUseInterceptorMBeans(stdConfPage
				.getChkUseInterceptorMBean().getSelection());
		cacheConfigModel.setFetchStateOnStartup(stdConfPage
				.getChkFetchStateOnStartup().getSelection());
		cacheConfigModel.setInitialStateRetrievalTimeOut(stdConfPage
				.getTxtInitialStateRetrievalTimeout().getText().trim());
		cacheConfigModel.setIsolationLevel(stdConfPage.getCmbIsolationLevel()
				.getText().trim());
		cacheConfigModel.setLockAcquisitionTimeOut(stdConfPage
				.getTxtLockAcquisitionTimeout().getText().trim());
		cacheConfigModel.setReplQueueInterval(stdConfPage
				.getTxtReplicationQueueInterval().getText().trim());
		cacheConfigModel.setReplQueueMaxElements(stdConfPage
				.getTxtReplQueueMaxElements().getText().trim());
		cacheConfigModel.setSyncReplicationTimeOut(stdConfPage
				.getTxtSyncReplTimeout().getText().trim());
		cacheConfigModel.setUseReplQueue(stdConfPage
				.getChkUseReplicationQueue().getSelection());
	}

	/**
	 * Set the cache loader model information from this wizard page
	 * 
	 * @param cacheLoaderConfig
	 */
	// private void setCacheLoaderConfig(CacheLoaderConfigInternal
	// cacheLoaderConfig)
	// {
	// int indexOfCacheLoaderClass =
	// cacheLoaderConfPage.getCmbCacheLoaderClass().getSelectionIndex();
	//
	// cacheLoaderConfig.setCacheLoaderAsynchronous(cacheLoaderConfPage.getChkCacheLoaderAsynchronous().getSelection());
	// cacheLoaderConfig.setCacheLoaderClass(cacheLoaderConfPage.getCmbCacheLoaderClass().getText().trim());
	// cacheLoaderConfig.setCacheLoaderFetchPersistentState(cacheLoaderConfPage.getChkCacheLoaderFetchPersistentState()
	// .getSelection());
	// cacheLoaderConfig.setCacheLoaderFetchTransientState(cacheLoaderConfPage.getChkCacheLoaderFetchTransientState()
	// .getSelection());
	// cacheLoaderConfig.setCacheLoaderPassivation(cacheLoaderConfPage.getChkCacheLoaderPassivation().getSelection());
	// cacheLoaderConfig.setCacheLoaderPreload(cacheLoaderConfPage.getTxtCacheLoaderPreload().getText().trim());
	// cacheLoaderConfig.setCacheLoaderShared(cacheLoaderConfPage.getChkCacheLoaderShared().getSelection());
	//
	// switch (indexOfCacheLoaderClass)
	// {
	// case 0 :
	//
	// cacheLoaderConfig.setUseDataSource(cacheLoaderConfPage.getBtnUseDataSource().getSelection());
	// cacheLoaderConfig.setJdbcCacheLoader(true);
	// cacheLoaderConfig.setDataSource(cacheLoaderConfPage.getTxtDataSource().getText().trim());
	// cacheLoaderConfig.setDriver(cacheLoaderConfPage.getCmbDriver().getText().trim());
	// cacheLoaderConfig.setDriverUrl(cacheLoaderConfPage.getCmbDriverUrl().getText().trim());
	// cacheLoaderConfig.setFqnColumn(cacheLoaderConfPage.getTxtFqnColumn().getText().trim());
	// cacheLoaderConfig.setFqnType(cacheLoaderConfPage.getTxtFqnType().getText().trim());
	// cacheLoaderConfig.setNodeColumn(cacheLoaderConfPage.getTxtNodeColumn().getText().trim());
	// cacheLoaderConfig.setNodeType(cacheLoaderConfPage.getTxtNodeType().getText().trim());
	// cacheLoaderConfig.setParentColumn(cacheLoaderConfPage.getTxtParentColumn().getText().trim());
	// cacheLoaderConfig.setPassword(cacheLoaderConfPage.getTxtPassword().getText().trim());
	// cacheLoaderConfig.setTableCreate(cacheLoaderConfPage.getChkTableCreate().getSelection());
	// cacheLoaderConfig.setTableDrop(cacheLoaderConfPage.getChkTableDrop().getSelection());
	// cacheLoaderConfig.setTableName(cacheLoaderConfPage.getTxtTableName().getText().trim());
	// cacheLoaderConfig.setUserName(cacheLoaderConfPage.getTxtUser().getText().trim());
	//
	// break;
	//
	// case 1 :
	//
	// cacheLoaderConfig.setFileCacheLoader(true);
	// cacheLoaderConfig.setFileLocation(cacheLoaderConfPage.getTxtLocation().getText().trim());
	//
	// break;
	//
	// default :
	// }
	//
	// }//end of method
	/**
	 * Get this configuration model
	 * 
	 * @return
	 */
	public CacheConfigurationModel getCacheConfigurationModel() {
		return this.cacheConfigModel;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workBench = workbench;
		this.selection = selection;

		cacheConfigModel = new CacheConfigurationModel();
		setNeedsProgressMonitor(true);
		initializeWizard();
	}

}// end of class
