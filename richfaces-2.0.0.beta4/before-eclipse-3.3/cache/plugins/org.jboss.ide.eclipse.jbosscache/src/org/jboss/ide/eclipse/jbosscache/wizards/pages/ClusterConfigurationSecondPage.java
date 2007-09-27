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
public class ClusterConfigurationSecondPage extends WizardPage
{

   ///////////VERIFY SUSPECT////////
   private Group grpVerifySuspect;

   private Label lbltimeoutForVerifySuspect;

   private Text txttimeoutForVerifySuspect;

   private Button btnup_threadForVerifySuspect;

   private Button btndown_threadForVerifySuspect;

   /////////////////////////////////

   //////////PB CAST NAK ACK///////
   private Group grpPbCastNakAck;

   private Label lblgc_lagForPbCastNakAck;

   private Text txtgc_lagForPbCastNakAck;

   private Label lblretransmit_timeoutForPbCastNakAck;

   private Text txtretransmit_timeoutForPbCastNakAck;

   private Label lblmax_xmit_sizeForPbCastNakAck;

   private Text txtmax_xmit_sizeForPbCastNakAck;

   private Button btnup_threadForPbCastNakAck;

   private Button btndown_threadForPbCastNakAck;

   ////////////////////////////////

   ///////////UNI CAST/////////////
   private Group grpUniCast;

   private Label lbltimeoutForUniCast;

   private Text txttimeoutForUniCast;

   private Label lblwindow_sizeoutForUniCast;

   private Text txtwindow_sizeForUniCast;

   private Label lblmin_thresholdForUniCast;

   private Text txtmin_thresholdForUniCast;

   private Button btndown_threadForUniCast;

   ///////////////////////////////

   //////////PB CAST STABLE//////
   private Group grpPbCastStable;

   private Label lbldesired_avg_gossipForPbCastStable;

   private Text txtdesired_avg_gossipForPbCastStable;

   private Button btndown_threadForPbCastStable;

   private Button btnup_threadForPbCastStable;

   /////////////////////////////

   /**
    * Constructor
    * @param name
    * @param title
    * @param imageDesc
    */
   public ClusterConfigurationSecondPage(String name, String title, ImageDescriptor imageDesc)
   {
      super(name, title, imageDesc);
      setDescription("Cluster configuration second step with default values");
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

      //Verify Suspect
      grpVerifySuspect = new Group(composite, SWT.SHADOW_ETCHED_IN);
      grpVerifySuspect.setLayout(gridLayout);
      grpVerifySuspect.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      grpVerifySuspect.setText("Verify Suspect");

      lbltimeoutForVerifySuspect = new Label(grpVerifySuspect, SWT.NONE);
      lbltimeoutForVerifySuspect.setText("Time Out:");/*TODO Internationalize for this and remainder !!!*/
      txttimeoutForVerifySuspect = new Text(grpVerifySuspect, SWT.BORDER);
      txttimeoutForVerifySuspect.setLayoutData(gridData);
      txttimeoutForVerifySuspect.setText("1500");

      btnup_threadForVerifySuspect = new Button(grpVerifySuspect, SWT.CHECK);
      btnup_threadForVerifySuspect.setText("Up Thread");
      btnup_threadForVerifySuspect.setLayoutData(gridDataNext);
      btnup_threadForVerifySuspect.setSelection(false);

      btndown_threadForVerifySuspect = new Button(grpVerifySuspect, SWT.CHECK);
      btndown_threadForVerifySuspect.setText("Down Thread");
      btndown_threadForVerifySuspect.setLayoutData(gridDataNext);
      btndown_threadForVerifySuspect.setSelection(false);

      //pbcast.NACK
      grpPbCastNakAck = new Group(composite, SWT.SHADOW_ETCHED_IN);
      grpPbCastNakAck.setLayout(gridLayout);
      grpPbCastNakAck.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      grpPbCastNakAck.setText("pbcast.NACK");

      lblgc_lagForPbCastNakAck = new Label(grpPbCastNakAck, SWT.NONE);
      lblgc_lagForPbCastNakAck.setText("GcLag:");/*TODO Internationalize for this and remainder !!!*/
      txtgc_lagForPbCastNakAck = new Text(grpPbCastNakAck, SWT.BORDER);
      txtgc_lagForPbCastNakAck.setLayoutData(gridData);
      txtgc_lagForPbCastNakAck.setText("50");

      lblretransmit_timeoutForPbCastNakAck = new Label(grpPbCastNakAck, SWT.NONE);
      lblretransmit_timeoutForPbCastNakAck.setText("Retransmit Timeout:");/*TODO Internationalize for this and remainder !!!*/
      txtretransmit_timeoutForPbCastNakAck = new Text(grpPbCastNakAck, SWT.BORDER);
      txtretransmit_timeoutForPbCastNakAck.setLayoutData(gridData);
      txtretransmit_timeoutForPbCastNakAck.setText("600,1200,2400,4800");

      lblmax_xmit_sizeForPbCastNakAck = new Label(grpPbCastNakAck, SWT.NONE);
      lblmax_xmit_sizeForPbCastNakAck.setText("Max Xmit Size:");/*TODO Internationalize for this and remainder !!!*/
      txtmax_xmit_sizeForPbCastNakAck = new Text(grpPbCastNakAck, SWT.BORDER);
      txtmax_xmit_sizeForPbCastNakAck.setLayoutData(gridData);
      txtmax_xmit_sizeForPbCastNakAck.setText("8192");

      btnup_threadForPbCastNakAck = new Button(grpPbCastNakAck, SWT.CHECK);
      btnup_threadForPbCastNakAck.setText("Up Thread");
      btnup_threadForPbCastNakAck.setLayoutData(gridDataNext);
      btnup_threadForPbCastNakAck.setSelection(false);

      btndown_threadForPbCastNakAck = new Button(grpPbCastNakAck, SWT.CHECK);
      btndown_threadForPbCastNakAck.setText("Down Thread");
      btndown_threadForPbCastNakAck.setLayoutData(gridDataNext);
      btndown_threadForPbCastNakAck.setSelection(false);

      //Unicast
      grpUniCast = new Group(composite, SWT.SHADOW_ETCHED_IN);
      grpUniCast.setLayout(gridLayout);
      grpUniCast.setText("Unicast");
      grpUniCast.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

      lbltimeoutForUniCast = new Label(grpUniCast, SWT.NONE);
      lbltimeoutForUniCast.setText("Timeout:");/*TODO Internationalize for this and remainder !!!*/
      txttimeoutForUniCast = new Text(grpUniCast, SWT.BORDER);
      txttimeoutForUniCast.setLayoutData(gridData);
      txttimeoutForUniCast.setText("600,1200,2400");

      lblwindow_sizeoutForUniCast = new Label(grpUniCast, SWT.NONE);
      lblwindow_sizeoutForUniCast.setText("Window Size:");/*TODO Internationalize for this and remainder !!!*/
      txtwindow_sizeForUniCast = new Text(grpUniCast, SWT.BORDER);
      txtwindow_sizeForUniCast.setLayoutData(gridData);
      txtwindow_sizeForUniCast.setText("100");

      lblwindow_sizeoutForUniCast = new Label(grpUniCast, SWT.NONE);
      lblwindow_sizeoutForUniCast.setText("Min Threshold:");/*TODO Internationalize for this and remainder !!!*/
      txtmin_thresholdForUniCast = new Text(grpUniCast, SWT.BORDER);
      txtmin_thresholdForUniCast.setLayoutData(gridData);
      txtmin_thresholdForUniCast.setText("10");

      btndown_threadForUniCast = new Button(grpUniCast, SWT.CHECK);
      btndown_threadForUniCast.setText("Down Thread");
      btndown_threadForUniCast.setLayoutData(gridDataNext);
      btndown_threadForUniCast.setSelection(false);

      //pbcastSTABLE
      grpPbCastStable = new Group(composite, SWT.SHADOW_ETCHED_IN);
      grpPbCastStable.setLayout(gridLayout);
      grpPbCastStable.setText("pbcast.STABLE");
      grpPbCastStable.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

      lbldesired_avg_gossipForPbCastStable = new Label(grpPbCastStable, SWT.NONE);
      lbldesired_avg_gossipForPbCastStable.setText("Desired Average Gossip:");/*TODO Internationalize for this and remainder !!!*/
      txtdesired_avg_gossipForPbCastStable = new Text(grpPbCastStable, SWT.BORDER);
      txtdesired_avg_gossipForPbCastStable.setLayoutData(gridData);
      txtdesired_avg_gossipForPbCastStable.setText("20000");

      btndown_threadForPbCastStable = new Button(grpPbCastStable, SWT.CHECK);
      btndown_threadForPbCastStable.setText("Up Thread");
      btndown_threadForPbCastStable.setLayoutData(gridDataNext);
      btndown_threadForPbCastStable.setSelection(false);

      btnup_threadForPbCastStable = new Button(grpPbCastStable, SWT.CHECK);
      btnup_threadForPbCastStable.setText("Down Thread");
      btnup_threadForPbCastStable.setLayoutData(gridDataNext);
      btnup_threadForPbCastStable.setSelection(false);

      setControl(composite);

   }//end of method

   public Button getBtndown_threadForPbCastNakAck()
   {
      return btndown_threadForPbCastNakAck;
   }

   public void setBtndown_threadForPbCastNakAck(Button btndown_threadForPbCastNakAck)
   {
      this.btndown_threadForPbCastNakAck = btndown_threadForPbCastNakAck;
   }

   public Button getBtndown_threadForPbCastStable()
   {
      return btndown_threadForPbCastStable;
   }

   public void setBtndown_threadForPbCastStable(Button btndown_threadForPbCastStable)
   {
      this.btndown_threadForPbCastStable = btndown_threadForPbCastStable;
   }

   public Button getBtndown_threadForUniCast()
   {
      return btndown_threadForUniCast;
   }

   public void setBtndown_threadForUniCast(Button btndown_threadForUniCast)
   {
      this.btndown_threadForUniCast = btndown_threadForUniCast;
   }

   public Button getBtndown_threadForVerifySuspect()
   {
      return btndown_threadForVerifySuspect;
   }

   public void setBtndown_threadForVerifySuspect(Button btndown_threadForVerifySuspect)
   {
      this.btndown_threadForVerifySuspect = btndown_threadForVerifySuspect;
   }

   public Button getBtnup_threadForPbCastNakAck()
   {
      return btnup_threadForPbCastNakAck;
   }

   public void setBtnup_threadForPbCastNakAck(Button btnup_threadForPbCastNakAck)
   {
      this.btnup_threadForPbCastNakAck = btnup_threadForPbCastNakAck;
   }

   public Button getBtnup_threadForPbCastStable()
   {
      return btnup_threadForPbCastStable;
   }

   public void setBtnup_threadForPbCastStable(Button btnup_threadForPbCastStable)
   {
      this.btnup_threadForPbCastStable = btnup_threadForPbCastStable;
   }

   public Button getBtnup_threadForVerifySuspect()
   {
      return btnup_threadForVerifySuspect;
   }

   public void setBtnup_threadForVerifySuspect(Button btnup_threadForVerifySuspect)
   {
      this.btnup_threadForVerifySuspect = btnup_threadForVerifySuspect;
   }

   public Group getGrpPbCastNakAck()
   {
      return grpPbCastNakAck;
   }

   public void setGrpPbCastNakAck(Group grpPbCastNakAck)
   {
      this.grpPbCastNakAck = grpPbCastNakAck;
   }

   public Group getGrpPbCastStable()
   {
      return grpPbCastStable;
   }

   public void setGrpPbCastStable(Group grpPbCastStable)
   {
      this.grpPbCastStable = grpPbCastStable;
   }

   public Group getGrpUniCast()
   {
      return grpUniCast;
   }

   public void setGrpUniCast(Group grpUniCast)
   {
      this.grpUniCast = grpUniCast;
   }

   public Group getGrpVerifySuspect()
   {
      return grpVerifySuspect;
   }

   public void setGrpVerifySuspect(Group grpVerifySuspect)
   {
      this.grpVerifySuspect = grpVerifySuspect;
   }

   public Label getLbldesired_avg_gossipForPbCastStable()
   {
      return lbldesired_avg_gossipForPbCastStable;
   }

   public void setLbldesired_avg_gossipForPbCastStable(Label lbldesired_avg_gossipForPbCastStable)
   {
      this.lbldesired_avg_gossipForPbCastStable = lbldesired_avg_gossipForPbCastStable;
   }

   public Label getLblgc_lagForPbCastNakAck()
   {
      return lblgc_lagForPbCastNakAck;
   }

   public void setLblgc_lagForPbCastNakAck(Label lblgc_lagForPbCastNakAck)
   {
      this.lblgc_lagForPbCastNakAck = lblgc_lagForPbCastNakAck;
   }

   public Label getLblmax_xmit_sizeForPbCastNakAck()
   {
      return lblmax_xmit_sizeForPbCastNakAck;
   }

   public void setLblmax_xmit_sizeForPbCastNakAck(Label lblmax_xmit_sizeForPbCastNakAck)
   {
      this.lblmax_xmit_sizeForPbCastNakAck = lblmax_xmit_sizeForPbCastNakAck;
   }

   public Label getLblmin_thresholdForUniCast()
   {
      return lblmin_thresholdForUniCast;
   }

   public void setLblmin_thresholdForUniCast(Label lblmin_thresholdForUniCast)
   {
      this.lblmin_thresholdForUniCast = lblmin_thresholdForUniCast;
   }

   public Label getLblretransmit_timeoutForPbCastNakAck()
   {
      return lblretransmit_timeoutForPbCastNakAck;
   }

   public void setLblretransmit_timeoutForPbCastNakAck(Label lblretransmit_timeoutForPbCastNakAck)
   {
      this.lblretransmit_timeoutForPbCastNakAck = lblretransmit_timeoutForPbCastNakAck;
   }

   public Label getLbltimeoutForUniCast()
   {
      return lbltimeoutForUniCast;
   }

   public void setLbltimeoutForUniCast(Label lbltimeoutForUniCast)
   {
      this.lbltimeoutForUniCast = lbltimeoutForUniCast;
   }

   public Label getLbltimeoutForVerifySuspect()
   {
      return lbltimeoutForVerifySuspect;
   }

   public void setLbltimeoutForVerifySuspect(Label lbltimeoutForVerifySuspect)
   {
      this.lbltimeoutForVerifySuspect = lbltimeoutForVerifySuspect;
   }

   public Label getLblwindow_sizeoutForUniCast()
   {
      return lblwindow_sizeoutForUniCast;
   }

   public void setLblwindow_sizeoutForUniCast(Label lblwindow_sizeoutForUniCast)
   {
      this.lblwindow_sizeoutForUniCast = lblwindow_sizeoutForUniCast;
   }

   public Text getTxtdesired_avg_gossipForPbCastStable()
   {
      return txtdesired_avg_gossipForPbCastStable;
   }

   public void setTxtdesired_avg_gossipForPbCastStable(Text txtdesired_avg_gossipForPbCastStable)
   {
      this.txtdesired_avg_gossipForPbCastStable = txtdesired_avg_gossipForPbCastStable;
   }

   public Text getTxtgc_lagForPbCastNakAck()
   {
      return txtgc_lagForPbCastNakAck;
   }

   public void setTxtgc_lagForPbCastNakAck(Text txtgc_lagForPbCastNakAck)
   {
      this.txtgc_lagForPbCastNakAck = txtgc_lagForPbCastNakAck;
   }

   public Text getTxtmax_xmit_sizeForPbCastNakAck()
   {
      return txtmax_xmit_sizeForPbCastNakAck;
   }

   public void setTxtmax_xmit_sizeForPbCastNakAck(Text txtmax_xmit_sizeForPbCastNakAck)
   {
      this.txtmax_xmit_sizeForPbCastNakAck = txtmax_xmit_sizeForPbCastNakAck;
   }

   public Text getTxtmin_thresholdForUniCast()
   {
      return txtmin_thresholdForUniCast;
   }

   public void setTxtmin_thresholdForUniCast(Text txtmin_thresholdForUniCast)
   {
      this.txtmin_thresholdForUniCast = txtmin_thresholdForUniCast;
   }

   public Text getTxtretransmit_timeoutForPbCastNakAck()
   {
      return txtretransmit_timeoutForPbCastNakAck;
   }

   public void setTxtretransmit_timeoutForPbCastNakAck(Text txtretransmit_timeoutForPbCastNakAck)
   {
      this.txtretransmit_timeoutForPbCastNakAck = txtretransmit_timeoutForPbCastNakAck;
   }

   public Text getTxttimeoutForUniCast()
   {
      return txttimeoutForUniCast;
   }

   public void setTxttimeoutForUniCast(Text txttimeoutForUniCast)
   {
      this.txttimeoutForUniCast = txttimeoutForUniCast;
   }

   public Text getTxttimeoutForVerifySuspect()
   {
      return txttimeoutForVerifySuspect;
   }

   public void setTxttimeoutForVerifySuspect(Text txttimeoutForVerifySuspect)
   {
      this.txttimeoutForVerifySuspect = txttimeoutForVerifySuspect;
   }

   public Text getTxtwindow_sizeForUniCast()
   {
      return txtwindow_sizeForUniCast;
   }

   public void setTxtwindow_sizeForUniCast(Text txtwindow_sizeForUniCast)
   {
      this.txtwindow_sizeForUniCast = txtwindow_sizeForUniCast;
   }

}//end of class