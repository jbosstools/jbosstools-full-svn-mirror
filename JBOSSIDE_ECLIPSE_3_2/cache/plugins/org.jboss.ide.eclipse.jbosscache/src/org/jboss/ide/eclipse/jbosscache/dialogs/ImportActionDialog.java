/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */

package org.jboss.ide.eclipse.jbosscache.dialogs;

import java.io.File;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.ide.eclipse.jbosscache.ICacheConstants;
import org.jboss.ide.eclipse.jbosscache.internal.CacheMessages;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheRootInstance;
import org.jboss.ide.eclipse.jbosscache.model.config.CacheConfigParams;
import org.jboss.ide.eclipse.jbosscache.model.factory.CacheInstanceFactory;

public class ImportActionDialog extends Dialog
{

   private static final String Select_File_To_Import = CacheMessages.ImportActionDialog_Select_File_To_Import;

   private Label lblCacheName;

   private Text txtCacheName;

   private Label lblCacheType;

   private Combo cmbCacheType;

   private Label lblConfFile;

   private Text txtConfFile;

   private Button btnSelectFile;

   public ImportActionDialog(Shell parentShell)
   {
      super(parentShell);
      setDefaultOrientation(SWT.LEFT_TO_RIGHT);
      setShellStyle(SWT.SHELL_TRIM);
   }

   protected Control createDialogArea(Composite parent)
   {
      Composite comp = (Composite) super.createDialogArea(parent);

      Composite comp1 = new Composite(comp, SWT.NONE);
      GridLayout layComp1 = new GridLayout(2, false);
      layComp1.horizontalSpacing = 20;
      comp1.setLayout(layComp1);
      comp1.setLayoutData(new GridData(GridData.FILL_BOTH));

      lblCacheName = new Label(comp1, SWT.NONE);
      lblCacheName.setText(CacheMessages.CachePropertyPage_lblCacheName);
      txtCacheName = new Text(comp1, SWT.BORDER);
      txtCacheName.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

      lblCacheType = new Label(comp1, SWT.NONE);
      lblCacheType.setText(CacheMessages.ImportActionDialog_lblCacheType);
      cmbCacheType = new Combo(comp1, SWT.READ_ONLY);
      cmbCacheType.setItems(ICacheConstants.CACHE_TYPE_MODE);
      cmbCacheType.select(0);
      cmbCacheType.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

      Composite comp2 = new Composite(comp, SWT.NONE);
      GridLayout layComp2 = new GridLayout(3, false);
      comp2.setLayout(layComp2);
      comp2.setLayoutData(new GridData(GridData.FILL_BOTH));

      lblConfFile = new Label(comp2, SWT.NONE);
      lblConfFile.setText(CacheMessages.CachePropertyPage_CachePropoerty_Page_LblConfDirectory);
      txtConfFile = new Text(comp2, SWT.BORDER);
      GridData gData = new GridData(GridData.FILL_HORIZONTAL);
      gData.minimumWidth = 250;
      txtConfFile.setLayoutData(gData);

      btnSelectFile = new Button(comp2, SWT.PUSH);
      btnSelectFile.setText(Select_File_To_Import);
      btnSelectFile.addSelectionListener(new SelectionAdapter()
      {
         public void widgetSelected(SelectionEvent e)
         {
            handleFileSelection(e);
         }
      });

      return comp;
   }

   private void handleFileSelection(SelectionEvent e)
   {
      FileDialog fileDialog = new FileDialog(getParentShell(), SWT.OPEN);
      fileDialog.setFilterExtensions(new String[]
      {"*.xml"}); //$NON-NLS-1$
      fileDialog.setText(CacheMessages.ImportAction_TreeCacheView_Import_Action_FileDialog_Title);
      if (!getLocalFilterPath().equals(""))
         fileDialog.setFilterPath(getLocalFilterPath());

      String fileName = fileDialog.open();
      if (fileName == null)
         fileName = "";

      this.txtConfFile.setText(fileName);

   }

   private String getLocalFilterPath()
   {
      if (getTxtConfFile().getText() != null && (!(getTxtConfFile().getText().equals(""))))
      {
         return getTxtConfFile().getText();
      }
      else
         return "";
   }

   protected void configureShell(Shell shell)
   {
      super.configureShell(shell);
      shell.setText(CacheMessages.TreeCacheView_importAction);
   }

   protected void okPressed()
   {
      String cacheName = getTxtCacheName().getText().trim();
      String fileName = getTxtConfFile().getText().trim();

      boolean isOk = checkFinish(cacheName, fileName);

      if (isOk)
      {
         addNewRootInstance();
         super.okPressed();
      }

   }

   private void addNewRootInstance()
   {
      ICacheRootInstance newRootInstance = CacheInstanceFactory.getCacheRootInstance(
            getTxtCacheName().getText().trim(), getTxtConfFile().getText().trim());
      newRootInstance.setCacheType(this.cmbCacheType.getText());
      newRootInstance.setIsDirty(true);
      CacheConfigParams configParams = new CacheConfigParams();
      newRootInstance.setCacheConfigParams(configParams);

      File file = new File(getTxtConfFile().getText().trim());
      String directoryName = file.getAbsolutePath();
      configParams.setConfDirectoryPath(directoryName);

      CacheInstanceFactory.getCacheRootMainInstance().addRootInstanceChild(newRootInstance);
   }

   private boolean checkFinish(String cacheName, String fileName)
   {
      try
      {

         ICacheRootInstance mainRootInstance = CacheInstanceFactory.getCacheRootMainInstance();
         if (mainRootInstance != null)
         {
            if (mainRootInstance.getRootInstanceChilds() != null)
            {
               List childList = mainRootInstance.getRootInstanceChilds();
               for (int i = 0, j = childList.size(); i < j; i++)
               {
                  ICacheRootInstance rootInstance = (ICacheRootInstance) childList.get(i);
                  if (rootInstance.getRootName().equals(cacheName))
                  {
                     MessageDialog.openError(getParentShell(), CacheMessages.ImportActionDialog_Error_Dialog_Title,
                           CacheMessages.bind(CacheMessages.ImportActionDialog_Already_Exist_Cache_Dialog_Message,
                                 cacheName));
                     return false;
                  }
               }
            }

         }

         if (cacheName.equals("") || fileName.equals(""))
         {
            MessageDialog.openError(getParentShell(), CacheMessages.ImportActionDialog_Error_Dialog_Title,
                  CacheMessages.ImportActionDialog_Fill_Blanks);
            return false;
         }

         boolean isFileExist = true;
         File file = new File(fileName);
         if (!file.exists())
            isFileExist = false;

         if (!isFileExist)
         {
            MessageDialog.openError(getParentShell(), CacheMessages.ImportActionDialog_Error_Dialog_Title,
                  CacheMessages.ImportActionDialog_Fill_Blanks);
            return false;
         }

      }
      catch (Exception e)
      {
         return false;
      }
      return true;
   }

   public Text getTxtCacheName()
   {
      return txtCacheName;
   }

   public Text getTxtConfFile()
   {
      return txtConfFile;
   }
}
