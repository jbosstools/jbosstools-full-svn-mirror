/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyPage;
import org.jboss.ide.eclipse.jbosscache.internal.CacheMessages;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheRootInstance;
import org.jboss.ide.eclipse.jbosscache.model.config.CacheConfigParams;
import org.jboss.ide.eclipse.jbosscache.utils.CacheUtil;
import org.jboss.ide.eclipse.jbosscache.wizards.pages.AddJarTableContentProvider;
import org.jboss.ide.eclipse.jbosscache.wizards.pages.AddJarTableLabelProvider;

public class CachePropertyPage extends PropertyPage implements ISelectionChangedListener
{

   private static final String CachePropoerty_Page_LblConfDirectory = CacheMessages.CachePropertyPage_CachePropoerty_Page_LblConfDirectory;

   private ICacheRootInstance rootInstance;

   private Label lblCacheName;

   private Text txtCacheName;

   private Label lblConfDirectory;

   private Text txtConfDirectory;

   private Group grpAddJars;

   private Button btnAddJar;

   private Button btnRemoveJar;

   private TableViewer jarTableViewer;

   private ISelection selection;

   private CacheConfigParams configParams = new CacheConfigParams();

   private CacheConfigParams defaultConfigParams = new CacheConfigParams();

   public CachePropertyPage()
   {
      super();
   }

   protected Control createContents(Composite parent)
   {
      this.rootInstance = (ICacheRootInstance) getElement();

      if (rootInstance.getCacheConfigParams() != null)
      {
         configParams.setConfDirectoryPath(rootInstance.getCacheConfigParams().getConfDirectoryPath());
         configParams.setConfJarUrls(new ArrayList(rootInstance.getCacheConfigParams().getConfJarUrls()));

      }

      Composite comp = new Composite(parent, SWT.NONE);
      GridLayout gridLayout = new GridLayout(2, false);
      comp.setLayout(gridLayout);

      lblCacheName = new Label(comp, SWT.NONE);
      lblCacheName.setText(CacheMessages.CachePropertyPage_lblCacheName);

      GridData lblGridData = new GridData(GridData.FILL_HORIZONTAL);
      txtCacheName = new Text(comp, SWT.BORDER);
      txtCacheName.setText(rootInstance.getRootName());
      txtCacheName.setLayoutData(lblGridData);
      txtCacheName.setEditable(false);

      lblConfDirectory = new Label(comp, SWT.NONE);
      lblConfDirectory.setText(CachePropoerty_Page_LblConfDirectory);

      txtConfDirectory = new Text(comp, SWT.BORDER);
      txtConfDirectory.setText(rootInstance.getRootConfigurationFileName());
      txtConfDirectory.setEditable(false);
      txtConfDirectory.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

      grpAddJars = new Group(comp, SWT.SHADOW_ETCHED_IN);
      grpAddJars.setText(CacheMessages.CachePropertyPage_grpText);
      GridData grpData = new GridData(GridData.FILL_BOTH);
      grpData.horizontalSpan = 2;
      grpData.verticalSpan = 2;
      grpAddJars.setLayoutData(grpData);
      grpAddJars.setLayout(new GridLayout(3, false));

      jarTableViewer = new TableViewer(grpAddJars, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
      jarTableViewer.setContentProvider(new AddJarTableContentProvider());
      jarTableViewer.setLabelProvider(new AddJarTableLabelProvider());
      jarTableViewer.addSelectionChangedListener(this);
      jarTableViewer.setInput(this.configParams);

      GridData gridForTable = new GridData(GridData.FILL_BOTH);
      gridForTable.horizontalSpan = 2;
      gridForTable.verticalSpan = 2;
      jarTableViewer.getTable().setLayoutData(gridForTable);

      btnAddJar = new Button(grpAddJars, SWT.PUSH);
      btnAddJar.setText(CacheUtil
            .getResourceBundleValue(ICacheConstants.WIZARDPAGE_STANDARD_CONFIGURATION_PAGE_ADD_JAR_BUTTON));
      GridData gDataForAddJar = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
      //gDataForAddJar.widthHint = 25;
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
      //gDataForRemoveJar.widthHint = 25;
      btnRemoveJar.setLayoutData(gDataForRemoveJar);
      btnRemoveJar.setEnabled(false);
      btnRemoveJar.addSelectionListener(new SelectionAdapter()
      {
         public void widgetSelected(SelectionEvent e)
         {
            handleRemoveJarSelected();
         }
      });

      if (this.rootInstance.isConnected())
      {
         noDefaultAndApplyButton();
         btnAddJar.setEnabled(false);
         btnRemoveJar.setEnabled(false);
         jarTableViewer.getTable().setEnabled(false);
      }

      return comp;
   }

   protected void handleRemoveJarSelected()
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
               //this.rootInstance.getCacheConfigParams().getConfJarUrls().remove(obj);
            }
         }

         jarTableViewer.refresh();
         btnRemoveJar.setEnabled(false);
      }

   }

   protected void handleAddJarSelected()
   {
      FileDialog fileDialog = new FileDialog(getShell(), SWT.NONE);
      fileDialog.setFilterExtensions(new String[]
      {"*.jar", "*.zip"}); //$NON-NLS-1$ //$NON-NLS-2$
      fileDialog.setFilterPath(null);
      String file = fileDialog.open();

      if (file != null)
      {

         //this.rootInstance.getCacheConfigParams().getConfJarUrls().add(file);
         configParams.getConfJarUrls().add(file);
         jarTableViewer.refresh();
      }

   }

   public void selectionChanged(SelectionChangedEvent event)
   {
      if (event.getSelection() != null)
      {
         this.selection = event.getSelection();
         btnRemoveJar.setEnabled(true);
      }
   }

   protected void performApply()
   {
      defaultConfigParams.setConfDirectoryPath(rootInstance.getCacheConfigParams().getConfDirectoryPath());
      defaultConfigParams.setConfJarUrls(new ArrayList(rootInstance.getCacheConfigParams().getConfJarUrls()));

      rootInstance.getCacheConfigParams().setConfDirectoryPath(configParams.getConfDirectoryPath());
      rootInstance.getCacheConfigParams().setConfJarUrls(new ArrayList(configParams.getConfJarUrls()));
      rootInstance.setIsDirty(true);
   }

   protected void performDefaults()
   {

      rootInstance.getCacheConfigParams().setConfDirectoryPath(defaultConfigParams.getConfDirectoryPath());
      rootInstance.getCacheConfigParams().setConfJarUrls(new ArrayList(defaultConfigParams.getConfJarUrls()));
      rootInstance.setIsDirty(false);

      configParams.setConfDirectoryPath(defaultConfigParams.getConfDirectoryPath());
      configParams.setConfJarUrls(new ArrayList(defaultConfigParams.getConfJarUrls()));

      jarTableViewer.refresh();
   }

}
