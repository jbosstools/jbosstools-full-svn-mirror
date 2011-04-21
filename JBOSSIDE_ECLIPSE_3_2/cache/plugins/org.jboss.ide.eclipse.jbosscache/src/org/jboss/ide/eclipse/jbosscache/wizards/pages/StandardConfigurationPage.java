/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.wizards.pages;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.jboss.ide.eclipse.jbosscache.ICacheConstants;
import org.jboss.ide.eclipse.jbosscache.internal.CacheMessages;
import org.jboss.ide.eclipse.jbosscache.model.config.CacheConfigParams;
import org.jboss.ide.eclipse.jbosscache.utils.CacheUtil;

/**
 * Wizard page that is used in the CacheConfigurationWizard
 * <p>
 * This wizard page takes configuration informations related with  all JBoss Cache Modes
 * </p>
 * @author Gurkaner
 *
 */
public class StandardConfigurationPage extends WizardPage implements ModifyListener, ISelectionChangedListener
{

   private static final String DEFAULT_CACHE_NAME = "Default";
   
   private static final String DEFAULT_CACHE_XML_FILE_NAME = "treecache_generated_by_jbosscache_ide";

   private static final String DEFAULT_CACHE_GROUP = "GENERATED_BY_JBOSSCACHE_IDE";

   private static final String DEFAULT_REPL_QUEUE_INTERVAL = "0";

   private static final String DEFAULT_REPL_QUEUE_MAX_NUMBER = "0";

   private static final String DEFAULT_INIT_STATE_RETRV_TIMEOUT = "5000";

   private static final String DEFAULT_SYNC_REPL_TIMEOUT = "20000";

   private static final String DEFAULT_LOCK_ACK_TIMEOUT = "15000";

   private Label lblCacheName;

   private Text txtCacheName;

   private Label lblConfFileName;

   private Text txtConfFileName;

   private Label lblDirectoryName;

   private Text txtDirectoryName;

   private Button browseButton;

   private Label lblTransactionManager;

   private Combo cmbTransactionManager;

   private Label lblIsolationLevel;

   private Combo cmbIsolationLevel;

   private Label lblCacheMode;

   private Combo cmbCacheMode;

   private Button chkUseReplicationQueue;

   private Label lblReplicationQueueInterval;

   private Text txtReplicationQueueInterval;

   private Label lblReplQueueMaxElements;

   private Text txtReplQueueMaxElements;

   private Label lblClusterName;

   private Text txtClusterName;

   private Label lblInitialStateRetrievalTimeout;

   private Text txtInitialStateRetrievalTimeout;

   private Label lblSyncReplTimeout;

   private Text txtSyncReplTimeout;

   private Label lblLockAcquisitionTimeout;

   private Text txtLockAcquisitionTimeout;

   //	private Label lblEvictionPolicyClass;
   //	private Text txtEvictionPolicyClass;
   //	private Label lblNodeLockingScheme;
   //	private Combo cmbNodeLockingScheme;
   private Label lblCacheType;

   private Combo cmbCacheType;

   private Button chkFetchStateOnStartup;

   private CacheConfigParams configParams;

   //Jar related operations
   private Group grpAddJars;

   private TableViewer jarTableViewer;

   private Button btnAddJar;

   private Button btnRemoveJar;

   private ISelection selection;

   //TODO : Eviction Policy Configuration must be added;

   private Group grpConfGroup;

   //	private Button btnClusterConfig; /*Show new wizard for cluster configuration*/
   //	private Button btnCacheLoaderClass;/*Show new wizard for cache loader configuration*/

   /**
    * Constructor
    * @param name
    * @param title
    * @param imageDesc
    */
   public StandardConfigurationPage(String name, String title, ImageDescriptor imageDesc)
   {
      super(name, title, imageDesc);
      setDescription("Details with base cache configuration informations");
   }

   /**
    * Creates the content of the wizard page
    * @param Parent of this page
    * @return none
    */
   public void createControl(Composite parent)
   {
      initializeContent(parent);

   }//end of method

   /**
    * Initialize the controls
    */
   protected void initializeContent(Composite parent)
   {
      setPageComplete(true);
      Composite container = new Composite(parent, SWT.NONE);
      container.setLayout(new GridLayout(1, false));

      GridData gridData = new GridData(GridData.FILL_HORIZONTAL);

      Group filePath = new Group(container, SWT.SHADOW_ETCHED_IN);
      filePath.setText("File Location");
      GridData gDataPath = new GridData(GridData.FILL_HORIZONTAL);
      filePath.setLayoutData(gDataPath);
      filePath.setLayout(new GridLayout(3, false));

      lblDirectoryName = new Label(filePath, SWT.NONE);
      lblDirectoryName.setText("Directory Name");
      txtDirectoryName = new Text(filePath, SWT.BORDER);
      txtDirectoryName.setText(System.getProperty("user.home"));
      txtDirectoryName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      txtDirectoryName.setEditable(false);
      txtDirectoryName.addModifyListener(this);

      if (configParams == null)
      {
         configParams = new CacheConfigParams(txtDirectoryName.getText(), new ArrayList());
      }

      browseButton = new Button(filePath, SWT.PUSH);
      browseButton.setText("Browse...");
      browseButton.addSelectionListener(new SelectionAdapter()
      {

         public void widgetSelected(SelectionEvent e)
         {
            handleLocationBrowseButtonPressed();
         }

      });

      lblConfFileName = new Label(filePath, SWT.NONE);
      lblConfFileName.setText(CacheMessages.StandartConfigurationPage_lblConfFileName);
      txtConfFileName = new Text(filePath, SWT.BORDER);
      txtConfFileName.setText(DEFAULT_CACHE_XML_FILE_NAME);
      txtConfFileName.addModifyListener(this);
      GridData gDataForConfText = new GridData(GridData.FILL_HORIZONTAL);
      gDataForConfText.horizontalSpan = 2;
      txtConfFileName.setLayoutData(gDataForConfText);

      grpConfGroup = new Group(container, SWT.SHADOW_ETCHED_IN);
      grpConfGroup.setText("New Cache Configuration");

      grpConfGroup.setLayoutData(new GridData(GridData.FILL_BOTH));

      GridLayout gridLayout = new GridLayout(2, false);
      //gridLayout.numColumns = 2;

      grpConfGroup.setLayout(gridLayout);

      lblCacheType = new Label(grpConfGroup, SWT.NONE);
      lblCacheType.setText("Cache Type");

      cmbCacheType = new Combo(grpConfGroup, SWT.READ_ONLY);
      cmbCacheType.setItems(ICacheConstants.CACHE_TYPE_MODE);
      cmbCacheType.select(0);
      cmbCacheType.setLayoutData(gridData);

      lblCacheName = new Label(grpConfGroup, SWT.NONE);
      lblCacheName.setText("Cache Name");
      txtCacheName = new Text(grpConfGroup, SWT.BORDER);
      txtCacheName.setText(DEFAULT_CACHE_NAME);
      txtCacheName.addModifyListener(this);
      txtCacheName.setLayoutData(gridData);

      lblCacheMode = new Label(grpConfGroup, SWT.NONE);
      lblCacheMode.setText("Cache Mode:");/*TODO Internationalize for this and remainder !!!*/

      cmbCacheMode = new Combo(grpConfGroup, SWT.READ_ONLY);
      cmbCacheMode.setItems(ICacheConstants.CACHE_MODES);
      cmbCacheMode.select(0);
      cmbCacheMode.setLayoutData(gridData);

      lblClusterName = new Label(grpConfGroup, SWT.NONE);
      lblClusterName.setText("Cluster Name:");/*TODO Internationalize for this and remainder !!!*/

      txtClusterName = new Text(grpConfGroup, SWT.BORDER);
      txtClusterName.setText(DEFAULT_CACHE_GROUP);
      txtClusterName.setLayoutData(gridData);
      txtClusterName.addModifyListener(this);

      lblTransactionManager = new Label(grpConfGroup, SWT.NONE);
      lblTransactionManager.setText("Transaction Manager:");/*TODO Internationalize for this and remainder !!!*/

      cmbTransactionManager = new Combo(grpConfGroup, SWT.NONE);
      cmbTransactionManager.setItems(ICacheConstants.TRANSACTION_MANAGER_LOOKUP_CLASSES);
      cmbTransactionManager.select(1);
      cmbTransactionManager.setLayoutData(gridData);

      lblIsolationLevel = new Label(grpConfGroup, SWT.NONE);
      lblIsolationLevel.setText("Isolation Level:");/*TODO Internationalize for this and remainder !!!*/

      cmbIsolationLevel = new Combo(grpConfGroup, SWT.READ_ONLY);
      cmbIsolationLevel.setItems(ICacheConstants.ISOLATION_LEVELS);
      cmbIsolationLevel.select(3);
      cmbIsolationLevel.setLayoutData(gridData);

      GridData gDataForReplicationQueue = new GridData(GridData.FILL_HORIZONTAL);
      gDataForReplicationQueue.horizontalSpan = 2;

      chkUseReplicationQueue = new Button(grpConfGroup, SWT.CHECK);
      chkUseReplicationQueue.setText("Use Replication Queue");
      chkUseReplicationQueue.setLayoutData(gDataForReplicationQueue);

      lblReplicationQueueInterval = new Label(grpConfGroup, SWT.NONE);
      lblReplicationQueueInterval.setText("Replication Queue Interval:");/*TODO Internationalize for this and remainder !!!*/

      txtReplicationQueueInterval = new Text(grpConfGroup, SWT.BORDER);
      txtReplicationQueueInterval.setText(DEFAULT_REPL_QUEUE_INTERVAL);
      txtReplicationQueueInterval.setLayoutData(gridData);
      txtReplicationQueueInterval.addModifyListener(this);

      lblReplQueueMaxElements = new Label(grpConfGroup, SWT.NONE);
      lblReplQueueMaxElements.setText("Replication Queue Max Number:");/*TODO Internationalize for this and remainder !!!*/

      txtReplQueueMaxElements = new Text(grpConfGroup, SWT.BORDER);
      txtReplQueueMaxElements.setText(DEFAULT_REPL_QUEUE_MAX_NUMBER);
      txtReplQueueMaxElements.setLayoutData(gridData);
      txtReplQueueMaxElements.addModifyListener(this);

      lblInitialStateRetrievalTimeout = new Label(grpConfGroup, SWT.NONE);
      lblInitialStateRetrievalTimeout.setText("Initial State Retrival Timeout:");/*TODO Internationalize for this and remainder !!!*/

      txtInitialStateRetrievalTimeout = new Text(grpConfGroup, SWT.BORDER);
      txtInitialStateRetrievalTimeout.setText(DEFAULT_INIT_STATE_RETRV_TIMEOUT);

      txtInitialStateRetrievalTimeout.setLayoutData(gridData);
      txtInitialStateRetrievalTimeout.addModifyListener(this);

      lblSyncReplTimeout = new Label(grpConfGroup, SWT.NONE);
      lblSyncReplTimeout.setText("Sync Repl Timeout:");/*TODO Internationalize for this and remainder !!!*/

      txtSyncReplTimeout = new Text(grpConfGroup, SWT.BORDER);
      txtSyncReplTimeout.setText(DEFAULT_SYNC_REPL_TIMEOUT);
      txtSyncReplTimeout.setLayoutData(gridData);
      txtSyncReplTimeout.addModifyListener(this);

      lblLockAcquisitionTimeout = new Label(grpConfGroup, SWT.NONE);
      lblLockAcquisitionTimeout.setText("Lock Acquisition Timeout:");/*TODO Internationalize for this and remainder !!!*/

      txtLockAcquisitionTimeout = new Text(grpConfGroup, SWT.BORDER);
      txtLockAcquisitionTimeout.setText(DEFAULT_LOCK_ACK_TIMEOUT);
      txtLockAcquisitionTimeout.setLayoutData(gridData);
      txtLockAcquisitionTimeout.addModifyListener(this);

      chkFetchStateOnStartup = new Button(grpConfGroup, SWT.CHECK);
      chkFetchStateOnStartup.setText("FetchState On Startup");
      chkFetchStateOnStartup.setLayoutData(gDataForReplicationQueue);

      //		lblNodeLockingScheme = new Label(grpConfGroup,SWT.NONE);
      //		lblNodeLockingScheme.setText("Node Locking Scheme:");/*TODO Internationalize for this and remainder !!!*/
      //		
      //		cmbNodeLockingScheme = new Combo(grpConfGroup,SWT.READ_ONLY);
      //		cmbNodeLockingScheme.setItems(new String[]{"Pessimistic","Optimistic"});
      //		cmbNodeLockingScheme.select(0);
      //		cmbNodeLockingScheme.setEnabled(false);
      //		cmbNodeLockingScheme.setLayoutData(gridData);		
      //		
      //		lblEvictionPolicyClass = new Label(grpConfGroup,SWT.NONE);
      //		lblEvictionPolicyClass.setText("Eviction Policy Class:");/*TODO Internationalize for this and remainder !!!*/		
      //		
      //		txtEvictionPolicyClass = new Text(grpConfGroup,SWT.BORDER);
      //		txtEvictionPolicyClass.setLayoutData(gridData);

      //		chkUseMarshalling = new Button(grpConfGroup,SWT.CHECK);
      //		chkUseMarshalling.setText("Use Marshalling");
      //		chkUseMarshalling.setLayoutData(gDataForReplicationQueue);
      //		chkUseMarshalling.setEnabled(false);
      //		chkUseMarshalling.setSelection(true);

      grpAddJars = new Group(grpConfGroup, SWT.SHADOW_ETCHED_IN);
      grpAddJars.setLayout(new GridLayout(3, true));
      grpAddJars.setText(CacheUtil
            .getResourceBundleValue(ICacheConstants.WIZARDPAGE_STANDARD_CONFIGURATION_PAGE_ADD_JAR_TEXT));
      GridData gridDataForJar = new GridData(GridData.FILL_BOTH);
      gridDataForJar.horizontalSpan = 2;
      grpAddJars.setLayoutData(gridDataForJar);

      jarTableViewer = new TableViewer(grpAddJars, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
      jarTableViewer.setContentProvider(new AddJarTableContentProvider());
      jarTableViewer.setLabelProvider(new AddJarTableLabelProvider());
      jarTableViewer.addSelectionChangedListener(this);

      GridData gridForTable = new GridData(GridData.FILL_BOTH);
      gridForTable.horizontalSpan = 2;
      gridForTable.verticalSpan = 2;
      jarTableViewer.getTable().setLayoutData(gridForTable);

      btnAddJar = new Button(grpAddJars, SWT.PUSH);
      btnAddJar.setText(CacheUtil
            .getResourceBundleValue(ICacheConstants.WIZARDPAGE_STANDARD_CONFIGURATION_PAGE_ADD_JAR_BUTTON));
      GridData gDataForAddJar = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
      gDataForAddJar.widthHint = 25;
      btnAddJar.setLayoutData(gDataForAddJar);
      btnAddJar.addSelectionListener(new SelectionAdapter()
      {
         public void widgetSelected(SelectionEvent e)
         {
            handleAddJarSelected();
         }
      });

      btnRemoveJar = new Button(grpAddJars, SWT.PUSH);
      btnRemoveJar.setText(CacheUtil
            .getResourceBundleValue(ICacheConstants.WIZARDPAGE_STANDARD_CONFIGURATION_PAGE_REMOVE_JAR_BUTTON));
      GridData gDataForRemoveJar = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
      gDataForRemoveJar.widthHint = 25;
      btnRemoveJar.setLayoutData(gDataForAddJar);
      btnRemoveJar.setEnabled(false);
      btnRemoveJar.addSelectionListener(new SelectionAdapter()
      {
         public void widgetSelected(SelectionEvent e)
         {
            handleRemoveJarSelected();
         }
      });

      setControl(container);
   }//end of method

   /**
    * Jar selection
    */
   private void handleAddJarSelected()
   {
      FileDialog fileDialog = new FileDialog(getShell(), SWT.NONE);
      fileDialog.setFilterExtensions(new String[]
      {"*.jar", "*.zip"});
      fileDialog.setFilterPath(null);
      String file = fileDialog.open();

      if (file != null)
      {
         if (configParams == null)
         {
            configParams = new CacheConfigParams();
         }

         configParams.getConfJarUrls().add(file);
         jarTableViewer.setInput(configParams);
      }
   }

   /**
    * Jar removal
    */
   private void handleRemoveJarSelected()
   {
      if (selection != null)
      {

         if (selection instanceof IStructuredSelection)
         {
            IStructuredSelection sl = (IStructuredSelection) selection;
            Iterator it = sl.iterator();
            String obj = null;
            while (it.hasNext())
            {
               obj = it.next().toString();
               configParams.getConfJarUrls().remove(obj);
            }
         }

         jarTableViewer.refresh();
         btnRemoveJar.setEnabled(false);
      }
   }

   /**
    * Modify listner controls
    */
   public void modifyText(ModifyEvent e)
   {
      Widget widget = e.widget;

      if (widget == txtReplicationQueueInterval)
      {
         if (txtReplicationQueueInterval.getText().equals(""))
            updatePage("Please give correct Replication Queue Interval");
         try
         {
            Integer.parseInt(txtReplicationQueueInterval.getText());
            updatePage(null);
         }
         catch (NumberFormatException e1)
         {
            updatePage("Replication Queue Interval must be number");
         }
      }
      else if (widget == txtReplQueueMaxElements)
      {
         if (txtReplQueueMaxElements.getText().equals(""))
            updatePage("Please give correct Replication Queue Max Elements");
         try
         {
            Integer.parseInt(txtReplQueueMaxElements.getText());
            updatePage(null);
         }
         catch (NumberFormatException e1)
         {
            updatePage("Replication Queue Max Elements must be number");
         }
      }
      else if (widget == txtInitialStateRetrievalTimeout)
      {
         if (txtInitialStateRetrievalTimeout.getText().equals(""))
            updatePage("Please give correct Initial State Retrieval Timeout");
         try
         {
            Integer.parseInt(txtInitialStateRetrievalTimeout.getText());
            updatePage(null);
         }
         catch (NumberFormatException e1)
         {
            updatePage("Initial State Retrieval Timeout must be number");
         }
      }
      else if (widget == txtSyncReplTimeout)
      {
         if (txtSyncReplTimeout.getText().equals(""))
            updatePage("Please give correct Sync Replication Timeout");
         try
         {
            Integer.parseInt(txtSyncReplTimeout.getText());
            updatePage(null);
         }
         catch (NumberFormatException e1)
         {
            updatePage("Sync Replication Timeout must be number");
         }
      }
      else if (widget == txtLockAcquisitionTimeout)
      {
         if (txtLockAcquisitionTimeout.getText().equals(""))
            updatePage("Please give correct Lock Acquisition Timeout");
         try
         {
            Integer.parseInt(txtLockAcquisitionTimeout.getText());
            updatePage(null);
         }
         catch (NumberFormatException e1)
         {
            updatePage("Lock Acquisition Timeout must be number");
         }
      }
      else if (widget == txtCacheName || widget == txtClusterName || widget == txtDirectoryName
            || widget == txtConfFileName)
      {
         canFinish(null);
      }

   }

   private void updatePage(String message)
   {
      setErrorMessage(message);
      canFinish(message);
   }

   private void canFinish(String message)
   {

      if (message == null)
      {

         if (!txtCacheName.getText().trim().equals("") && !txtConfFileName.getText().trim().equals("")
               && !txtClusterName.getText().trim().equals("")
               && !txtReplicationQueueInterval.getText().trim().equals("")
               && !txtReplQueueMaxElements.getText().trim().equals("")
               && !txtInitialStateRetrievalTimeout.getText().trim().equals("")
               && !txtSyncReplTimeout.getText().trim().equals("")
               && !txtLockAcquisitionTimeout.getText().trim().equals("")
               && !txtDirectoryName.getText().trim().equals(""))
         {
            setPageComplete(true);
         }
         else
         {
            setPageComplete(false);
         }
      }
      else
         setPageComplete(false);
   }

   /**
    *	Open an appropriate directory browser
    */
   void handleLocationBrowseButtonPressed()
   {
      DirectoryDialog dialog = new DirectoryDialog(txtDirectoryName.getShell());
      dialog.setMessage("Specify Cache Configuration Directory");

      String dirName = getProjectLocationFieldValue();
      if (!dirName.equals("")) { //$NON-NLS-1$
         File path = new File(dirName);
         if (path.exists())
            dialog.setFilterPath(new Path(dirName).toOSString());
      }

      String selectedDirectory = dialog.open();
      if (selectedDirectory != null)
      {
         txtDirectoryName.setText(selectedDirectory);
         if (configParams == null)
         {
            configParams = new CacheConfigParams();
         }
         configParams.setConfDirectoryPath(txtDirectoryName.getText().trim());
      }
   }

   private String getProjectLocationFieldValue()
   {
      if (txtDirectoryName == null)
         return "";

      return txtDirectoryName.getText().trim();
   }

   public Text getTxtDirectoryName()
   {
      return txtDirectoryName;
   }

   public CacheConfigParams getConfigParams()
   {
      return configParams;
   }

   public void selectionChanged(SelectionChangedEvent event)
   {
      if (event.getSelection() != null)
      {
         this.selection = event.getSelection();
         btnRemoveJar.setEnabled(true);
      }
   }

   public Button getChkFetchStateOnStartup()
   {
      return chkFetchStateOnStartup;
   }

   public Button getChkUseReplicationQueue()
   {
      return chkUseReplicationQueue;
   }

   public Combo getCmbCacheMode()
   {
      return cmbCacheMode;
   }

   public Combo getCmbIsolationLevel()
   {
      return cmbIsolationLevel;
   }

   public Combo getCmbTransactionManager()
   {
      return cmbTransactionManager;
   }

   public Group getGrpConfGroup()
   {
      return grpConfGroup;
   }

   public Label getLblCacheMode()
   {
      return lblCacheMode;
   }

   public Text getTxtCacheName()
   {
      return txtCacheName;
   }

   public Text getTxtClusterName()
   {
      return txtClusterName;
   }

   public Text getTxtInitialStateRetrievalTimeout()
   {
      return txtInitialStateRetrievalTimeout;
   }

   public Text getTxtLockAcquisitionTimeout()
   {
      return txtLockAcquisitionTimeout;
   }

   public Text getTxtReplicationQueueInterval()
   {
      return txtReplicationQueueInterval;
   }

   public Text getTxtReplQueueMaxElements()
   {
      return txtReplQueueMaxElements;
   }

   public Text getTxtSyncReplTimeout()
   {
      return txtSyncReplTimeout;
   }

   public Combo getCmbCacheType()
   {
      return cmbCacheType;
   }

   public Text getTxtConfFileName()
   {
      return txtConfFileName;
   }

}//end of class