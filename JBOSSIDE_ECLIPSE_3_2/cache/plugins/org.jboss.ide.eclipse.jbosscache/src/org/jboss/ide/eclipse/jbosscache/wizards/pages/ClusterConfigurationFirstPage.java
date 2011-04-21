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
public class ClusterConfigurationFirstPage extends WizardPage
{

   ////////////////UDP//////////////////
   private Group grpUdp;

   private Label lblmcast_addrForUdp;

   private Text txtmcast_addrForUdp;

   private Label lblmcast_portForUdp;

   private Text txtmcast_portForUdp;

   private Label lblip_ttlForUdp;

   private Text txtip_ttlForUdp;

   private Button btnip_mcastForUdp;

   private Label lblmcast_send_buf_sizeForUdp;

   private Text txtmcast_send_buf_sizeForUdp;

   private Label lblmcast_recv_buf_sizeForUdp;

   private Text txtmcast_recv_buf_sizeForUdp;

   private Label lblucast_recv_buf_sizeForUdp;

   private Text txtucast_recv_buf_sizeForUdp;

   private Label lblucast_send_buf_sizeForUdp;

   private Text txtucast_send_buf_sizeForUdp;

   private Button btnloopbackForUdp;

   ////////////////////////////////////

   ///////////////PING////////////////
   private Group grpPing;

   private Label lbltimeoutForPing;

   private Text txttimeoutForPing;

   private Label lblnum_initial_membersForPing;

   private Text txtnum_initial_membersForPing;

   private Button btnup_threadForPing;

   private Button btndown_threadForPing;

   //////////////////////////////////

   /////////////MERGE2//////////////
   private Group grpMerge2;

   private Label lblmin_intervalForMerge2;

   private Text txtmin_intervalForMerge2;

   private Label lblmax_intervalForMerge2;

   private Text txtmax_intervalForMerge2;

   /////////////////////////////////

   ////////////FD///////////////////
   private Group grpFd;

   private Button btnshunForFd;

   private Button btnup_threadForFd;

   private Button btndown_threadForFd;

   ////////////////////////////////

   /**
    * Constructor
    * @param name
    * @param title
    * @param imageDesc
    */
   public ClusterConfigurationFirstPage(String name, String title, ImageDescriptor imageDesc)
   {
      super(name, title, imageDesc);
      setDescription("Cluster configuration first step with default values");
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

      grpUdp = new Group(composite, SWT.SHADOW_ETCHED_IN);
      grpUdp.setLayout(gridLayout);
      grpUdp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      grpUdp.setText("Udp");

      lblmcast_addrForUdp = new Label(grpUdp, SWT.NONE);
      lblmcast_addrForUdp.setText("Mcast Addr:");/*TODO Internationalize for this and remainder !!!*/
      txtmcast_addrForUdp = new Text(grpUdp, SWT.BORDER);
      txtmcast_addrForUdp.setLayoutData(gridData);
      txtmcast_addrForUdp.setText("228.1.2.3");

      lblmcast_portForUdp = new Label(grpUdp, SWT.NONE);
      lblmcast_portForUdp.setText("Mcast Port:");/*TODO Internationalize for this and remainder !!!*/
      txtmcast_portForUdp = new Text(grpUdp, SWT.BORDER);
      txtmcast_portForUdp.setLayoutData(gridData);
      txtmcast_portForUdp.setText("48866");

      lblip_ttlForUdp = new Label(grpUdp, SWT.NONE);
      lblip_ttlForUdp.setText("Ip Ttl:");/*TODO Internationalize for this and remainder !!!*/
      txtip_ttlForUdp = new Text(grpUdp, SWT.BORDER);
      txtip_ttlForUdp.setLayoutData(gridData);
      txtip_ttlForUdp.setText("64");

      GridData gridDataNext = new GridData(GridData.FILL_HORIZONTAL);
      gridDataNext.horizontalSpan = 2;

      btnip_mcastForUdp = new Button(grpUdp, SWT.CHECK);
      btnip_mcastForUdp.setText("Ip Mcast");
      btnip_mcastForUdp.setLayoutData(gridDataNext);
      btnip_mcastForUdp.setSelection(true);

      lblmcast_send_buf_sizeForUdp = new Label(grpUdp, SWT.NONE);
      lblmcast_send_buf_sizeForUdp.setText("Mcast Send Buf Size:");/*TODO Internationalize for this and remainder !!!*/
      txtmcast_send_buf_sizeForUdp = new Text(grpUdp, SWT.BORDER);
      txtmcast_send_buf_sizeForUdp.setLayoutData(gridData);
      txtmcast_send_buf_sizeForUdp.setText("150000");

      lblmcast_recv_buf_sizeForUdp = new Label(grpUdp, SWT.NONE);
      lblmcast_recv_buf_sizeForUdp.setText("Mcast Recv Buf Size:");/*TODO Internationalize for this and remainder !!!*/
      txtmcast_recv_buf_sizeForUdp = new Text(grpUdp, SWT.BORDER);
      txtmcast_recv_buf_sizeForUdp.setLayoutData(gridData);
      txtmcast_recv_buf_sizeForUdp.setText("80000");

      lblucast_send_buf_sizeForUdp = new Label(grpUdp, SWT.NONE);
      lblucast_send_buf_sizeForUdp.setText("Ucast Send Buf Size:");/*TODO Internationalize for this and remainder !!!*/
      txtucast_send_buf_sizeForUdp = new Text(grpUdp, SWT.BORDER);
      txtucast_send_buf_sizeForUdp.setLayoutData(gridData);
      txtucast_send_buf_sizeForUdp.setText("150000");

      lblucast_recv_buf_sizeForUdp = new Label(grpUdp, SWT.NONE);
      lblucast_recv_buf_sizeForUdp.setText("Ucast Recv Buf Size:");/*TODO Internationalize for this and remainder !!!*/
      txtucast_recv_buf_sizeForUdp = new Text(grpUdp, SWT.BORDER);
      txtucast_recv_buf_sizeForUdp.setLayoutData(gridData);
      txtucast_recv_buf_sizeForUdp.setText("80000");

      btnloopbackForUdp = new Button(grpUdp, SWT.CHECK);
      btnloopbackForUdp.setText("Loop Back:");
      btnloopbackForUdp.setLayoutData(gridDataNext);
      btnloopbackForUdp.setSelection(false);

      //PING
      grpPing = new Group(composite, SWT.SHADOW_ETCHED_IN);
      grpPing.setLayout(gridLayout);
      grpPing.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      grpPing.setText("Ping");

      lbltimeoutForPing = new Label(grpPing, SWT.NONE);
      lbltimeoutForPing.setText("Time Out:");/*TODO Internationalize for this and remainder !!!*/
      txttimeoutForPing = new Text(grpPing, SWT.BORDER);
      txttimeoutForPing.setLayoutData(gridData);
      txttimeoutForPing.setText("2000");

      lblnum_initial_membersForPing = new Label(grpPing, SWT.NONE);
      lblnum_initial_membersForPing.setText("Num Initial Members:");/*TODO Internationalize for this and remainder !!!*/
      txtnum_initial_membersForPing = new Text(grpPing, SWT.BORDER);
      txtnum_initial_membersForPing.setLayoutData(gridData);
      txtnum_initial_membersForPing.setText("3");

      btnup_threadForPing = new Button(grpPing, SWT.CHECK);
      btnup_threadForPing.setText("Up Thread:");
      btnup_threadForPing.setLayoutData(gridDataNext);
      btnup_threadForPing.setSelection(false);

      btndown_threadForPing = new Button(grpPing, SWT.CHECK);
      btndown_threadForPing.setText("Down Thread:");
      btndown_threadForPing.setLayoutData(gridDataNext);
      btndown_threadForPing.setSelection(false);

      //Merge 2
      grpMerge2 = new Group(composite, SWT.SHADOW_ETCHED_IN);
      grpMerge2.setLayout(gridLayout);
      grpMerge2.setText("Merge2");
      grpMerge2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

      lblmin_intervalForMerge2 = new Label(grpMerge2, SWT.NONE);
      lblmin_intervalForMerge2.setText("Min Interval:");/*TODO Internationalize for this and remainder !!!*/
      txtmin_intervalForMerge2 = new Text(grpMerge2, SWT.BORDER);
      txtmin_intervalForMerge2.setLayoutData(gridData);
      txtmin_intervalForMerge2.setText("10000");

      lblmax_intervalForMerge2 = new Label(grpMerge2, SWT.NONE);
      lblmax_intervalForMerge2.setText("Max Interval:");/*TODO Internationalize for this and remainder !!!*/
      txtmax_intervalForMerge2 = new Text(grpMerge2, SWT.BORDER);
      txtmax_intervalForMerge2.setLayoutData(gridData);
      txtmax_intervalForMerge2.setText("20000");

      //Fd
      grpFd = new Group(composite, SWT.SHADOW_ETCHED_IN);
      grpFd.setLayout(gridLayout);
      grpFd.setText("Fd");
      grpFd.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

      btnshunForFd = new Button(grpFd, SWT.CHECK);
      btnshunForFd.setText("Shun:");
      btnshunForFd.setLayoutData(gridDataNext);
      //		btnshunForFd.setSelection(true);

      btnup_threadForFd = new Button(grpFd, SWT.CHECK);
      btnup_threadForFd.setText("Up Thread:");
      btnup_threadForFd.setLayoutData(gridDataNext);
      //		btnup_threadForFd.setSelection(true);

      btndown_threadForFd = new Button(grpFd, SWT.CHECK);
      btndown_threadForFd.setText("Down Thread:");
      btndown_threadForFd.setLayoutData(gridDataNext);
      //		btndown_threadForFd.setSelection(true);

      setControl(composite);

   }//end of method

   public Button getBtndown_threadForFd()
   {
      return btndown_threadForFd;
   }

   public void setBtndown_threadForFd(Button btndown_threadForFd)
   {
      this.btndown_threadForFd = btndown_threadForFd;
   }

   public Button getBtndown_threadForPing()
   {
      return btndown_threadForPing;
   }

   public void setBtndown_threadForPing(Button btndown_threadForPing)
   {
      this.btndown_threadForPing = btndown_threadForPing;
   }

   public Button getBtnip_mcastForUdp()
   {
      return btnip_mcastForUdp;
   }

   public void setBtnip_mcastForUdp(Button btnip_mcastForUdp)
   {
      this.btnip_mcastForUdp = btnip_mcastForUdp;
   }

   public Button getBtnloopbackForUdp()
   {
      return btnloopbackForUdp;
   }

   public void setBtnloopbackForUdp(Button btnloopbackForUdp)
   {
      this.btnloopbackForUdp = btnloopbackForUdp;
   }

   public Button getBtnshunForFd()
   {
      return btnshunForFd;
   }

   public void setBtnshunForFd(Button btnshunForFd)
   {
      this.btnshunForFd = btnshunForFd;
   }

   public Button getBtnup_threadForFd()
   {
      return btnup_threadForFd;
   }

   public void setBtnup_threadForFd(Button btnup_threadForFd)
   {
      this.btnup_threadForFd = btnup_threadForFd;
   }

   public Button getBtnup_threadForPing()
   {
      return btnup_threadForPing;
   }

   public void setBtnup_threadForPing(Button btnup_threadForPing)
   {
      this.btnup_threadForPing = btnup_threadForPing;
   }

   public Text getTxtip_ttlForUdp()
   {
      return txtip_ttlForUdp;
   }

   public void setTxtip_ttlForUdp(Text txtip_ttlForUdp)
   {
      this.txtip_ttlForUdp = txtip_ttlForUdp;
   }

   public Text getTxtmax_intervalForMerge2()
   {
      return txtmax_intervalForMerge2;
   }

   public void setTxtmax_intervalForMerge2(Text txtmax_intervalForMerge2)
   {
      this.txtmax_intervalForMerge2 = txtmax_intervalForMerge2;
   }

   public Text getTxtmcast_addrForUdp()
   {
      return txtmcast_addrForUdp;
   }

   public void setTxtmcast_addrForUdp(Text txtmcast_addrForUdp)
   {
      this.txtmcast_addrForUdp = txtmcast_addrForUdp;
   }

   public Text getTxtmcast_portForUdp()
   {
      return txtmcast_portForUdp;
   }

   public void setTxtmcast_portForUdp(Text txtmcast_portForUdp)
   {
      this.txtmcast_portForUdp = txtmcast_portForUdp;
   }

   public Text getTxtmcast_recv_buf_sizeForUdp()
   {
      return txtmcast_recv_buf_sizeForUdp;
   }

   public void setTxtmcast_recv_buf_sizeForUdp(Text txtmcast_recv_buf_sizeForUdp)
   {
      this.txtmcast_recv_buf_sizeForUdp = txtmcast_recv_buf_sizeForUdp;
   }

   public Text getTxtmcast_send_buf_sizeForUdp()
   {
      return txtmcast_send_buf_sizeForUdp;
   }

   public void setTxtmcast_send_buf_sizeForUdp(Text txtmcast_send_buf_sizeForUdp)
   {
      this.txtmcast_send_buf_sizeForUdp = txtmcast_send_buf_sizeForUdp;
   }

   public Text getTxtmin_intervalForMerge2()
   {
      return txtmin_intervalForMerge2;
   }

   public void setTxtmin_intervalForMerge2(Text txtmin_intervalForMerge2)
   {
      this.txtmin_intervalForMerge2 = txtmin_intervalForMerge2;
   }

   public Text getTxtnum_initial_membersForPing()
   {
      return txtnum_initial_membersForPing;
   }

   public void setTxtnum_initial_membersForPing(Text txtnum_initial_membersForPing)
   {
      this.txtnum_initial_membersForPing = txtnum_initial_membersForPing;
   }

   public Text getTxttimeoutForPing()
   {
      return txttimeoutForPing;
   }

   public void setTxttimeoutForPing(Text txttimeoutForPing)
   {
      this.txttimeoutForPing = txttimeoutForPing;
   }

   public Text getTxtucast_recv_buf_sizeForUdp()
   {
      return txtucast_recv_buf_sizeForUdp;
   }

   public void setTxtucast_recv_buf_sizeForUdp(Text txtucast_recv_buf_sizeForUdp)
   {
      this.txtucast_recv_buf_sizeForUdp = txtucast_recv_buf_sizeForUdp;
   }

   public Text getTxtucast_send_buf_sizeForUdp()
   {
      return txtucast_send_buf_sizeForUdp;
   }

   public void setTxtucast_send_buf_sizeForUdp(Text txtucast_send_buf_sizeForUdp)
   {
      this.txtucast_send_buf_sizeForUdp = txtucast_send_buf_sizeForUdp;
   }

}//end of class