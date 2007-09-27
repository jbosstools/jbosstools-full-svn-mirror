/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.packaging.ui.dialogs;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.core.util.ProjectUtil;
import org.jboss.ide.eclipse.packaging.core.model.PackagingArchive;
import org.jboss.ide.eclipse.packaging.core.model.PackagingFolder;
import org.jboss.ide.eclipse.packaging.ui.PackagingUIMessages;
import org.jboss.ide.eclipse.ui.dialogs.FolderSelectionDialog;
import org.jboss.ide.eclipse.ui.util.ProjectContentProvider;
import org.jboss.ide.eclipse.ui.util.ProjectLabelProvider;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class FolderEditDialog extends Dialog
{
   private PackagingArchive archive;
   private Text excludesText;
   private Button externalBrowseButton;
   private Text filenameText;
   private PackagingFolder folder;
   private Text includesText;
   private Button localBrowseButton;
   private Text prefixText;
   private PackagingFolder tempFolder;


   /**
    *Constructor for the AttributeEditDialog object
    *
    * @param parentShell  Description of the Parameter
    * @param folder       Description of the Parameter
    * @param archive      Description of the Parameter
    */
   public FolderEditDialog(Shell parentShell, PackagingArchive archive, PackagingFolder folder)
   {
      super(parentShell);
      this.archive = archive;
      this.folder = folder;
      this.tempFolder = (PackagingFolder) this.folder.clone();
   }


   /** Description of the Method */
   protected void assign()
   {
      this.localBrowseButton.addSelectionListener(
         new SelectionAdapter()
         {
            public void widgetSelected(SelectionEvent e)
            {
               addSelectLocal();
            }
         });
      this.externalBrowseButton.addSelectionListener(
         new SelectionAdapter()
         {
            public void widgetSelected(SelectionEvent e)
            {
               addSelectExternal();
            }
         });

      this.includesText.addModifyListener(
         new ModifyListener()
         {
            public void modifyText(ModifyEvent e)
            {
               tempFolder.setIncludes(((Text) e.widget).getText());
            }
         });

      this.excludesText.addModifyListener(
         new ModifyListener()
         {
            public void modifyText(ModifyEvent e)
            {
               tempFolder.setExcludes(((Text) e.widget).getText());
            }
         });

      this.prefixText.addModifyListener(
         new ModifyListener()
         {
            public void modifyText(ModifyEvent e)
            {
               tempFolder.setPrefix(((Text) e.widget).getText());
            }
         });
   }


   /**
    * Description of the Method
    *
    * @param shell  Description of the Parameter
    */
   protected void configureShell(Shell shell)
   {
      super.configureShell(shell);
      shell.setText(PackagingUIMessages.getString("FolderEditDialog.title"));//$NON-NLS-1$
   }


   /**
    * Description of the Method
    *
    * @param parent  Description of the Parameter
    * @return        Description of the Return Value
    * @see           org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
    */
   protected Control createDialogArea(Composite parent)
   {
      GridData layoutData;
      Composite global = (Composite) super.createDialogArea(parent);
      GridLayout gridLayout = new GridLayout(3, false);
      gridLayout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
      gridLayout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
      global.setLayout(gridLayout);

      Label descLabel = new Label(global, SWT.NONE);
      descLabel.setText(PackagingUIMessages.getString("FolderEditDialog.description"));//$NON-NLS-1$
      layoutData = new GridData(GridData.FILL_HORIZONTAL);
      layoutData.horizontalSpan = 3;
      descLabel.setLayoutData(layoutData);

      Label fileLabel = new Label(global, SWT.NONE);
      fileLabel.setText(PackagingUIMessages.getString("FolderEditDialog.label.folder"));//$NON-NLS-1$
      layoutData = new GridData();
      layoutData.horizontalAlignment = SWT.END;
      fileLabel.setLayoutData(layoutData);

      this.filenameText = new Text(global, SWT.BORDER);
      this.filenameText.setEditable(false);
      layoutData = new GridData(GridData.FILL_HORIZONTAL);
      layoutData.grabExcessHorizontalSpace = true;
      this.filenameText.setLayoutData(layoutData);

      this.localBrowseButton = new Button(global, SWT.PUSH);
      this.localBrowseButton.setText(PackagingUIMessages.getString("FolderEditDialog.button.local.browse"));//$NON-NLS-1$
      layoutData = new GridData(GridData.FILL_HORIZONTAL);
      this.localBrowseButton.setLayoutData(layoutData);

      // Spacers
      new Label(global, SWT.NONE);
      new Label(global, SWT.NONE);

      this.externalBrowseButton = new Button(global, SWT.PUSH);
      this.externalBrowseButton.setText(PackagingUIMessages.getString("FolderEditDialog.button.external.browse"));//$NON-NLS-1$
      layoutData = new GridData(GridData.FILL_HORIZONTAL);
      this.externalBrowseButton.setLayoutData(layoutData);

      Label includesLabel = new Label(global, SWT.NONE);
      includesLabel.setText(PackagingUIMessages.getString("FolderEditDialog.label.includes"));//$NON-NLS-1$
      layoutData = new GridData();
      layoutData.horizontalAlignment = SWT.RIGHT;
      includesLabel.setLayoutData(layoutData);

      this.includesText = new Text(global, SWT.BORDER);
      layoutData = new GridData(GridData.FILL_HORIZONTAL);
      layoutData.horizontalSpan = 2;
      layoutData.grabExcessHorizontalSpace = true;
      includesText.setLayoutData(layoutData);

      Label excludesLabel = new Label(global, SWT.NONE);
      excludesLabel.setText(PackagingUIMessages.getString("FolderEditDialog.label.excludes"));//$NON-NLS-1$
      layoutData = new GridData();
      layoutData.horizontalAlignment = SWT.RIGHT;
      excludesLabel.setLayoutData(layoutData);

      this.excludesText = new Text(global, SWT.BORDER);
      layoutData = new GridData(GridData.FILL_HORIZONTAL);
      layoutData.horizontalSpan = 2;
      layoutData.grabExcessHorizontalSpace = true;
      excludesText.setLayoutData(layoutData);

      Label prefixLabel = new Label(global, SWT.NONE);
      prefixLabel.setText(PackagingUIMessages.getString("FolderEditDialog.label.prefix"));//$NON-NLS-1$
      layoutData = new GridData();
      layoutData.horizontalAlignment = SWT.RIGHT;
      prefixLabel.setLayoutData(layoutData);

      this.prefixText = new Text(global, SWT.BORDER);
      layoutData = new GridData(GridData.FILL_HORIZONTAL);
      layoutData.horizontalSpan = 2;
      layoutData.grabExcessHorizontalSpace = true;
      prefixText.setLayoutData(layoutData);

      this.refresh();
      this.assign();

      return parent;
   }


   /** Description of the Method */
   protected void okPressed()
   {
      // Copy modified content
      if (this.tempFolder.isLocal())
      {
         this.folder.setProject(this.tempFolder.getProject());
         this.folder.setProjectLocation(this.tempFolder.getProjectLocation());
      }
      else
      {
         this.folder.setLocation(this.tempFolder.getLocation());
      }
      this.folder.setIncludes(this.tempFolder.getIncludes());
      this.folder.setExcludes(this.tempFolder.getExcludes());
      this.folder.setPrefix(this.tempFolder.getPrefix());

      super.okPressed();
   }


   /** Adds a feature to the SelectExternalFile attribute of the FileEditDialog object */
   private void addSelectExternal()
   {
      DirectoryDialog dialog = new DirectoryDialog(AbstractPlugin.getShell(), SWT.OPEN);
      String result = dialog.open();
      if (result != null)
      {
         IPath path = new Path(result);
         this.tempFolder.setLocation(path.toString());
         this.refresh();
      }
   }


   /** Adds a feature to the SelectLocalFile attribute of the FileEditDialog object */
   private void addSelectLocal()
   {
      FolderSelectionDialog dialog = new FolderSelectionDialog(AbstractPlugin.getShell(), new ProjectLabelProvider(), new ProjectContentProvider());

      // Select all projects as input
      dialog.setInput(ProjectUtil.getAllOpenedProjects());

      // If there is a selection, use it
      if (this.tempFolder.resolve(this.archive) != null)
      {
         dialog.setInitialSelection(this.tempFolder.resolve(this.archive));
      }

      // Open the dialog and wait for the result
      if (dialog.open() == IDialogConstants.OK_ID)
      {
         IResource resource = (IResource) dialog.getFirstResult();
         if (resource.getProject().equals(this.archive.getProject()))
         {
            this.tempFolder.setProject("");//$NON-NLS-1$
         }
         else
         {
            this.tempFolder.setProject(resource.getProject().getName());
         }
         this.tempFolder.setProjectLocation(resource.getProjectRelativePath().toString());
         this.refresh();
      }
   }


   /** Description of the Method */
   private void refresh()
   {
      if (this.tempFolder.isLocal())
      {
         this.filenameText.setText(this.tempFolder.resolve(this.archive).getFullPath().toString());
      }
      else
      {
         this.filenameText.setText(this.tempFolder.getLocation());
      }
      this.includesText.setText(this.tempFolder.getIncludes());
      this.excludesText.setText(this.tempFolder.getExcludes());
      this.prefixText.setText(this.tempFolder.getPrefix());
   }
}
