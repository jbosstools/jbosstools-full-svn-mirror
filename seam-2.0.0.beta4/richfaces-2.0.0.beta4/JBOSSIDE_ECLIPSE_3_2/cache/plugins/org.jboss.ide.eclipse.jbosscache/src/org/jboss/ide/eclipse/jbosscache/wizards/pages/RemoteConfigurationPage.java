/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.wizards.pages;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.jboss.ide.eclipse.jbosscache.ICacheConstants;
import org.jboss.ide.eclipse.jbosscache.model.config.CacheConfigParams;
import org.jboss.ide.eclipse.jbosscache.utils.CacheUtil;

public class RemoteConfigurationPage extends WizardPage implements ModifyListener,ISelectionChangedListener
{
   private static final String DEFAULT_CONFIGURATION_NAME = "Default";
   private static final String DEFAULT_URL = "localhost";
   private static final String DEFAULT_PORT = "1099";
   private static final String DEFAULT_JNDI_NAME = "jndi_name";
   
   private Label lblDefaultConfigName;
   private Text txtDefaultConfigName; 
   private Label lblDefaultUrl;
   private Text txtDefaultUrl;
   private Label lblDefaultPort;
   private Text txtDefaultPort;
   private Label lblDefaultJndi;
   private Text txtDefaultJndi;
   private Label lblCacheType;
   private Combo cmbCacheType;
   
   private TableViewer jarTableViewer;
   private Group grpAddJars;
   private Button btnAddJar;
   private Button btnRemoveJar;
   private List jarList = new ArrayList();
   private ISelection selection;
   
   
   /**
    * Constructor
    * @param name
    * @param title
    * @param imageDesc
    */
   public RemoteConfigurationPage(String name, String title, ImageDescriptor imageDesc)
   {
      super(name, title, imageDesc);
      setDescription("Remote Cache Configuration Related Paramters Information");
   }

   public void createControl(Composite parent)
   {
      setPageComplete(true);
      Composite container = new Composite(parent,SWT.NONE);
      container.setLayout(new GridLayout(2,false));
      container.setLayoutData(new GridData(GridData.FILL_BOTH));
      
      lblCacheType = new Label(container, SWT.NONE);
      lblCacheType.setText("Cache Type");

      cmbCacheType = new Combo(container, SWT.READ_ONLY);
      cmbCacheType.setItems(ICacheConstants.CACHE_TYPE_MODE);
      cmbCacheType.select(0);
      cmbCacheType.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      
      
      lblDefaultConfigName = new Label(container,SWT.NONE);
      lblDefaultConfigName.setText("Configuration Name");
      txtDefaultConfigName = new Text(container,SWT.BORDER);
      txtDefaultConfigName.setText(DEFAULT_CONFIGURATION_NAME);
      txtDefaultConfigName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      txtDefaultConfigName.addModifyListener(this);
      
      lblDefaultUrl = new Label(container,SWT.NONE);
      lblDefaultUrl.setText("Server Url");
      txtDefaultUrl = new Text(container,SWT.BORDER);
      txtDefaultUrl.setText(DEFAULT_URL);
      txtDefaultUrl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      txtDefaultUrl.addModifyListener(this);

      lblDefaultPort = new Label(container,SWT.NONE);
      lblDefaultPort.setText("Jndi Port");
      txtDefaultPort = new Text(container,SWT.BORDER);
      txtDefaultPort.setText(DEFAULT_PORT);
      txtDefaultPort.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      txtDefaultPort.addModifyListener(this);

      lblDefaultJndi = new Label(container,SWT.NONE);
      lblDefaultJndi.setText("Cache Jndi Name");
      txtDefaultJndi = new Text(container,SWT.BORDER);
      txtDefaultJndi.setText(DEFAULT_JNDI_NAME);
      txtDefaultJndi.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      txtDefaultJndi.addModifyListener(this);
      
      grpAddJars = new Group(container, SWT.SHADOW_ETCHED_IN);
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
      
   }

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
         jarList.add(file);
         jarTableViewer.setInput(jarList);
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
               jarList.remove(obj);
            }
         }

         jarTableViewer.refresh();
         btnRemoveJar.setEnabled(false);
      }
   }

   public void modifyText(ModifyEvent e)
   {
      Widget widget = e.widget;
      
      if(widget == txtDefaultConfigName){
         if(txtDefaultConfigName.getText().equals(""))
         {
            updatePage("Please give the configuration name");
         }
         else{
            updatePage(null);
         }
      }else if(widget == txtDefaultJndi){
         if(txtDefaultJndi.getText().equals(""))
         {
            updatePage("Please give the jndi name");
         }
         else{
            updatePage(null);
         }

      }else if(widget == txtDefaultPort){
         if(txtDefaultPort.getText().equals(""))
         {
            updatePage("Please give the port");
         }
         else{
            updatePage(null);
         }

      }else if(widget == txtDefaultUrl){
         if(txtDefaultUrl.getText().equals(""))
         {
            updatePage("Please give the url name");
         }
         else{
            updatePage(null);
         }

      }            
      
   }
   
   private void updatePage(String message)
   {
      setErrorMessage(message);
      canFinish(message);
   }
   
   private void canFinish(String message)
   {
      if(!txtDefaultConfigName.getText().equals("") &&
         !txtDefaultJndi.getText().equals("") && 
         !txtDefaultPort.getText().equals("") &&
         !txtDefaultUrl.getText().equals(""))
      {
         setPageComplete(true);
      }
      else
         setPageComplete(false);
   }

   public Text getTxtDefaultConfigName()
   {
      return txtDefaultConfigName;
   }

   public Text getTxtDefaultJndi()
   {
      return txtDefaultJndi;
   }

   public Text getTxtDefaultPort()
   {
      return txtDefaultPort;
   }

   public Text getTxtDefaultUrl()
   {
      return txtDefaultUrl;
   }

   public Combo getCmbCacheType()
   {
      return cmbCacheType;
   }

   public void selectionChanged(SelectionChangedEvent event)
   {
      if (event.getSelection() != null)
      {
         this.selection = event.getSelection();
         btnRemoveJar.setEnabled(true);
      }      
   }

   public List getJarList()
   {
      return jarList;
   }


   
   
}
