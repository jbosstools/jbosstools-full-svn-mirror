/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.deployer.ui.dialogs;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.ide.eclipse.deployer.core.target.ITarget;
import org.jboss.ide.eclipse.deployer.core.target.LocalMainDeployer;
import org.jboss.ide.eclipse.deployer.ui.DeployerUIMessages;
import org.jboss.ide.eclipse.deployer.ui.DeployerUIPlugin;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class LocalMainDeployerEditDialog extends TargetEditDialog
{
   /** Description of the Field */
   private Text nameText;
   /** Description of the Field */
//   private Button pingButton;
   /** Description of the Field */
   private LocalMainDeployer target;
   /** Description of the Field */
   private LocalMainDeployer tempTarget;
   /** Description of the Field */
   private Text urlText;


   /**
    *Constructor for the AttributeEditDialog object
    *
    * @param parentShell  Description of the Parameter
    * @param target       Description of the Parameter
    */
   public LocalMainDeployerEditDialog(Shell parentShell, ITarget target)
   {
      super(parentShell);
      this.setStatusLineAboveButtons(true);
      this.target = (LocalMainDeployer) target;
      this.tempTarget = (LocalMainDeployer) this.target.cloneTarget();
   }


   /** Description of the Method */
   protected void assign()
   {
      this.nameText.addModifyListener(
         new ModifyListener()
         {
            public void modifyText(ModifyEvent e)
            {
               tempTarget.setName(((Text) e.widget).getText());
            }
         });

      this.urlText.addModifyListener(
         new ModifyListener()
         {
            public void modifyText(ModifyEvent e)
            {
               tempTarget.setUrl(((Text) e.widget).getText());
               validate();
            }
         });

//      this.pingButton.addSelectionListener(
//         new SelectionAdapter()
//         {
//            public void widgetSelected(SelectionEvent e)
//            {
//               doPing();
//            }
//         });
   }


   /**
    * Description of the Method
    *
    * @param shell  Description of the Parameter
    */
   protected void configureShell(Shell shell)
   {
      this.setTitle(DeployerUIMessages.getString("LocalMainDeployerEditDialog.title"));//$NON-NLS-1$
      super.configureShell(shell);
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
      GridLayout gridLayout = new GridLayout(2, false);
      gridLayout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
      gridLayout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
      global.setLayout(gridLayout);

      Label descLabel = new Label(global, SWT.NONE);
      descLabel.setText(DeployerUIMessages.getString("LocalMainDeployerEditDialog.description"));//$NON-NLS-1$
      layoutData = new GridData(GridData.FILL_HORIZONTAL);
      layoutData.horizontalSpan = 2;
      descLabel.setLayoutData(layoutData);

      Label fileLabel = new Label(global, SWT.NONE);
      fileLabel.setText(DeployerUIMessages.getString("LocalMainDeployerEditDialog.label.name"));//$NON-NLS-1$
      layoutData = new GridData();
      layoutData.horizontalAlignment = SWT.END;
      fileLabel.setLayoutData(layoutData);

      this.nameText = new Text(global, SWT.BORDER);
      layoutData = new GridData(GridData.FILL_HORIZONTAL);
      layoutData.grabExcessHorizontalSpace = true;
      this.nameText.setLayoutData(layoutData);

      Label urlLabel = new Label(global, SWT.NONE);
      urlLabel.setText(DeployerUIMessages.getString("LocalMainDeployerEditDialog.label.url"));//$NON-NLS-1$
      layoutData = new GridData();
      layoutData.horizontalAlignment = SWT.END;
      urlLabel.setLayoutData(layoutData);

      this.urlText = new Text(global, SWT.BORDER);
      layoutData = new GridData(GridData.FILL_HORIZONTAL);
      layoutData.grabExcessHorizontalSpace = true;
      this.urlText.setLayoutData(layoutData);

//      this.pingButton = new Button(global, SWT.PUSH);
//      this.pingButton.setText(DeployerUIMessages.getString("LocalMainDeployerEditDialog.button.ping"));//$NON-NLS-1$
//      layoutData = new GridData(GridData.FILL_HORIZONTAL);
//      layoutData.horizontalSpan = 2;
//      this.pingButton.setLayoutData(layoutData);

      this.assign();
      this.refresh();

      return parent;
   }



   /** Description of the Method */
   protected void okPressed()
   {
      // Copy modified content
      this.target.setName(this.tempTarget.getName());
      this.target.setUrl(this.tempTarget.getUrl());

      super.okPressed();
   }


   /** Description of the Method */
   protected void validate()
   {
      String temp;
      IStatus status = null;

      temp = this.tempTarget.getUrl();
      if ((temp == null) || ("".equals(temp))//$NON-NLS-1$
      )
      {
         status = new Status(IStatus.ERROR, DeployerUIPlugin.getUniqueIdentifier(), 0, DeployerUIMessages.getString("LocalMainDeployerEditDialog.status.host.not.valid.message"), null);//$NON-NLS-1$
      }

      if (status == null)
      {
         status = new Status(IStatus.OK, DeployerUIPlugin.getUniqueIdentifier(), 0, "", null);//$NON-NLS-1$
         this.updateStatus(status);
      }
   }


   /** Description of the Method */
//   private void doPing()
//   {
//      try
//      {
//         this.tempTarget.ping();
//         DeployerUIPlugin.getDefault().showInfoMessage(this.tempTarget.toString() + DeployerUIMessages.getString("LocalMainDeployerEditDialog.reachable.message"));//$NON-NLS-1$
//      }
//      catch (IOException ioe)
//      {
//         DeployerUIPlugin.getDefault().showErrorMessage(DeployerUIMessages.getString("LocalMainDeployerEditDialog.unreachable.message") + ioe.getMessage());//$NON-NLS-1$
//      }
//   }


   /** Description of the Method */
   private void refresh()
   {
      this.nameText.setText(this.tempTarget.getName());

      if (this.tempTarget.getUrl() != null)
      {
         this.urlText.setText(this.tempTarget.getUrl().toString());
      }
   }
}
