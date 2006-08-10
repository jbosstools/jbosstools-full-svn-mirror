/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.wizards.pages;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * This wizard page configures the cluster configuration of the cache
 * @author Gurkaner
 */
public class ClusterConfigurationThirdPage extends WizardPage
{

   //////////FRAG////////////////
   private Group grpFrag;

   private Label lblfrag_sizeForFrag;

   private Text txtfrag_sizeForFrag;

   private Button btndown_threadForFrag;

   private Button btnup_threadForFrag;

   /////////////////////////////

   ////////////PB CAST GMS//////
   private Group grpPbCastGms;

   private Label lbljoin_timeoutForPbCastGms;

   private Text txtjoin_timeoutForPbCastGms;

   private Label lbljoin_retry_timeoutForPbCastGms;

   private Text txtjoin_retry_timeoutForPbCastGms;

   private Button btnshunForPbCastGms;

   private Button btnprint_local_addrForPbCastGms;

   /////////////////////////////

   ///////////PB CAST STATE TRANSFER
   private Group grpPbCastStateTransfer;

   private Button btndown_threadForPbCastStateTransfer;

   private Button btnup_threadForPbCastStateTransfer;

   //////////////////////////////////

   /**
    * Constructor
    * @param name
    * @param title
    * @param imageDesc
    */
   public ClusterConfigurationThirdPage(String name, String title, ImageDescriptor imageDesc)
   {
      super(name, title, imageDesc);
      setDescription("Cluster configuration third step with default values. ");
   }//end of method

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
      Composite composite = new Composite(parent, SWT.NONE);

      GridLayout gridLayout = new GridLayout();
      gridLayout.numColumns = 1;
      composite.setLayout(gridLayout);

      gridLayout = new GridLayout();
      gridLayout.numColumns = 2;

      GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
      GridData gridDataNext = new GridData(GridData.FILL_HORIZONTAL);
      gridDataNext.horizontalSpan = 2;

      //Frag
      grpFrag = new Group(composite, SWT.SHADOW_ETCHED_IN);
      grpFrag.setLayout(gridLayout);
      grpFrag.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      grpFrag.setText("Frag");

      lblfrag_sizeForFrag = new Label(grpFrag, SWT.NONE);
      lblfrag_sizeForFrag.setText("Frag Size:");/*TODO Internationalize for this and remainder !!!*/
      txtfrag_sizeForFrag = new Text(grpFrag, SWT.BORDER);
      txtfrag_sizeForFrag.setLayoutData(gridData);
      txtfrag_sizeForFrag.setText("8192");

      btndown_threadForFrag = new Button(grpFrag, SWT.CHECK);
      btndown_threadForFrag.setText("Up Thread");
      btndown_threadForFrag.setLayoutData(gridDataNext);
      btndown_threadForFrag.setSelection(false);

      btnup_threadForFrag = new Button(grpFrag, SWT.CHECK);
      btnup_threadForFrag.setText("Down Thread");
      btnup_threadForFrag.setLayoutData(gridDataNext);
      btnup_threadForFrag.setSelection(false);

      //pbcast.CMS
      grpPbCastGms = new Group(composite, SWT.SHADOW_ETCHED_IN);
      grpPbCastGms.setLayout(gridLayout);
      grpPbCastGms.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      grpPbCastGms.setText("pbcast.GMS");

      lbljoin_timeoutForPbCastGms = new Label(grpPbCastGms, SWT.NONE);
      lbljoin_timeoutForPbCastGms.setText("Join Timeout:");/*TODO Internationalize for this and remainder !!!*/
      txtjoin_timeoutForPbCastGms = new Text(grpPbCastGms, SWT.BORDER);
      txtjoin_timeoutForPbCastGms.setLayoutData(gridData);
      txtjoin_timeoutForPbCastGms.setText("5000");

      lbljoin_retry_timeoutForPbCastGms = new Label(grpPbCastGms, SWT.NONE);
      lbljoin_retry_timeoutForPbCastGms.setText("Join Retry Timeout:");/*TODO Internationalize for this and remainder !!!*/
      txtjoin_retry_timeoutForPbCastGms = new Text(grpPbCastGms, SWT.BORDER);
      txtjoin_retry_timeoutForPbCastGms.setLayoutData(gridData);
      txtjoin_retry_timeoutForPbCastGms.setText("2000");

      btnshunForPbCastGms = new Button(grpPbCastGms, SWT.CHECK);
      btnshunForPbCastGms.setText("Shun");
      btnshunForPbCastGms.setLayoutData(gridDataNext);
      btnshunForPbCastGms.setSelection(true);

      btnprint_local_addrForPbCastGms = new Button(grpPbCastGms, SWT.CHECK);
      btnprint_local_addrForPbCastGms.setText("Print Local Address");
      btnprint_local_addrForPbCastGms.setLayoutData(gridDataNext);
      btnprint_local_addrForPbCastGms.setSelection(true);

      //State transfer
      grpPbCastStateTransfer = new Group(composite, SWT.SHADOW_ETCHED_IN);
      grpPbCastStateTransfer.setLayout(gridLayout);
      grpPbCastStateTransfer.setText("pbcast.STATE_TRANSFER");
      grpPbCastStateTransfer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

      btndown_threadForPbCastStateTransfer = new Button(grpPbCastStateTransfer, SWT.CHECK);
      btndown_threadForPbCastStateTransfer.setText("Up Thread");
      btndown_threadForPbCastStateTransfer.setLayoutData(gridDataNext);
      btndown_threadForPbCastStateTransfer.setSelection(true);

      btnup_threadForPbCastStateTransfer = new Button(grpPbCastStateTransfer, SWT.CHECK);
      btnup_threadForPbCastStateTransfer.setText("Down Thread");
      btnup_threadForPbCastStateTransfer.setLayoutData(gridDataNext);
      btnup_threadForPbCastStateTransfer.setSelection(true);

      setControl(composite);
   }//end of method

   public Button getBtndown_threadForFrag()
   {
      return btndown_threadForFrag;
   }

   public void setBtndown_threadForFrag(Button btndown_threadForFrag)
   {
      this.btndown_threadForFrag = btndown_threadForFrag;
   }

   public Button getBtndown_threadForPbCastStateTransfer()
   {
      return btndown_threadForPbCastStateTransfer;
   }

   public void setBtndown_threadForPbCastStateTransfer(Button btndown_threadForPbCastStateTransfer)
   {
      this.btndown_threadForPbCastStateTransfer = btndown_threadForPbCastStateTransfer;
   }

   public Button getBtnprint_local_addrForPbCastGms()
   {
      return btnprint_local_addrForPbCastGms;
   }

   public void setBtnprint_local_addrForPbCastGms(Button btnprint_local_addrForPbCastGms)
   {
      this.btnprint_local_addrForPbCastGms = btnprint_local_addrForPbCastGms;
   }

   public Button getBtnshunForPbCastGms()
   {
      return btnshunForPbCastGms;
   }

   public void setBtnshunForPbCastGms(Button btnshunForPbCastGms)
   {
      this.btnshunForPbCastGms = btnshunForPbCastGms;
   }

   public Button getBtnup_threadForFrag()
   {
      return btnup_threadForFrag;
   }

   public void setBtnup_threadForFrag(Button btnup_threadForFrag)
   {
      this.btnup_threadForFrag = btnup_threadForFrag;
   }

   public Button getBtnup_threadForPbCastStateTransfer()
   {
      return btnup_threadForPbCastStateTransfer;
   }

   public void setBtnup_threadForPbCastStateTransfer(Button btnup_threadForPbCastStateTransfer)
   {
      this.btnup_threadForPbCastStateTransfer = btnup_threadForPbCastStateTransfer;
   }

   public Group getGrpFrag()
   {
      return grpFrag;
   }

   public void setGrpFrag(Group grpFrag)
   {
      this.grpFrag = grpFrag;
   }

   public Group getGrpPbCastGms()
   {
      return grpPbCastGms;
   }

   public void setGrpPbCastGms(Group grpPbCastGms)
   {
      this.grpPbCastGms = grpPbCastGms;
   }

   public Group getGrpPbCastStateTransfer()
   {
      return grpPbCastStateTransfer;
   }

   public void setGrpPbCastStateTransfer(Group grpPbCastStateTransfer)
   {
      this.grpPbCastStateTransfer = grpPbCastStateTransfer;
   }

   public Label getLblfrag_sizeForFrag()
   {
      return lblfrag_sizeForFrag;
   }

   public void setLblfrag_sizeForFrag(Label lblfrag_sizeForFrag)
   {
      this.lblfrag_sizeForFrag = lblfrag_sizeForFrag;
   }

   public Label getLbljoin_retry_timeoutForPbCastGms()
   {
      return lbljoin_retry_timeoutForPbCastGms;
   }

   public void setLbljoin_retry_timeoutForPbCastGms(Label lbljoin_retry_timeoutForPbCastGms)
   {
      this.lbljoin_retry_timeoutForPbCastGms = lbljoin_retry_timeoutForPbCastGms;
   }

   public Label getLbljoin_timeoutForPbCastGms()
   {
      return lbljoin_timeoutForPbCastGms;
   }

   public void setLbljoin_timeoutForPbCastGms(Label lbljoin_timeoutForPbCastGms)
   {
      this.lbljoin_timeoutForPbCastGms = lbljoin_timeoutForPbCastGms;
   }

   public Text getTxtfrag_sizeForFrag()
   {
      return txtfrag_sizeForFrag;
   }

   public void setTxtfrag_sizeForFrag(Text txtfrag_sizeForFrag)
   {
      this.txtfrag_sizeForFrag = txtfrag_sizeForFrag;
   }

   public Text getTxtjoin_retry_timeoutForPbCastGms()
   {
      return txtjoin_retry_timeoutForPbCastGms;
   }

   public void setTxtjoin_retry_timeoutForPbCastGms(Text txtjoin_retry_timeoutForPbCastGms)
   {
      this.txtjoin_retry_timeoutForPbCastGms = txtjoin_retry_timeoutForPbCastGms;
   }

   public Text getTxtjoin_timeoutForPbCastGms()
   {
      return txtjoin_timeoutForPbCastGms;
   }

   public void setTxtjoin_timeoutForPbCastGms(Text txtjoin_timeoutForPbCastGms)
   {
      this.txtjoin_timeoutForPbCastGms = txtjoin_timeoutForPbCastGms;
   }

}//end of class