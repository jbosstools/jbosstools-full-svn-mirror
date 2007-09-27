/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.wizards;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.Wizard;
import org.jboss.ide.eclipse.jbosscache.JBossCachePlugin;
import org.jboss.ide.eclipse.jbosscache.model.config.CacheConfigurationModel.ClusterConfigInternal;
import org.jboss.ide.eclipse.jbosscache.wizards.pages.ClusterConfigurationFirstPage;
import org.jboss.ide.eclipse.jbosscache.wizards.pages.ClusterConfigurationSecondPage;
import org.jboss.ide.eclipse.jbosscache.wizards.pages.ClusterConfigurationThirdPage;

/**
 * Used for getting necessary cluster configuration informations from the user 
 * @author Gurkaner
 */
public class ClusterConfigurationWizard extends Wizard
{

   /**First wizard page of this wizard*/
   private ClusterConfigurationFirstPage clusterConfPageFirst;

   /**Second wizard page of this wizard*/
   private ClusterConfigurationSecondPage clusterConfPageSecond;

   /**Third wizard page of this wizard*/
   private ClusterConfigurationThirdPage clusterConfPageThird;

   /**Root wizard contains this wizard*/
   private NewCacheConfigurationWizard rootWizard;

   public ClusterConfigurationWizard(IWizard wizard)
   {
      super();
      this.rootWizard = (NewCacheConfigurationWizard) wizard;
      initializeWizard();
   }

   /**
    * Initialization of this wizard
    *
    */
   protected void initializeWizard()
   {

      setWindowTitle("New Cluster Configuration");
      clusterConfPageFirst = new ClusterConfigurationFirstPage("clusterConfFirstPage",
            "Cluster Configuration First Step", JBossCachePlugin.getImageDescriptor("icons/sample.gif"));
      addPage(clusterConfPageFirst);
      clusterConfPageSecond = new ClusterConfigurationSecondPage("clusterConfSecondPage",
            "Cluster Configuration Second Step", JBossCachePlugin.getImageDescriptor("icons/sample.gif"));
      addPage(clusterConfPageSecond);
      clusterConfPageThird = new ClusterConfigurationThirdPage("clusterConfThirdPage",
            "Cluster Configuration Third Step", JBossCachePlugin.getImageDescriptor("icons/sample.gif"));
      addPage(clusterConfPageThird);
   }

   /**
    * Before return, implement necessary actions when the finish button pressed in wizard
    * @param none
    * @return true or false 
    */
   public boolean performFinish()
   {
      ClusterConfigInternal clusterConfigInternal = rootWizard.getCacheConfigurationModel().getClusterConfig();

      setClusterConfigModelFirstPage(clusterConfigInternal);
      setClusterConfigModelSecondPage(clusterConfigInternal);
      setClusterConfigModelThirdPage(clusterConfigInternal);

      return true;
   }//end of method

   /**
    * Set cluster configuration model part 1 from first page of this wizard
    * @param clusterConfig
    * @return void
    */
   private void setClusterConfigModelFirstPage(ClusterConfigInternal clusterConfigInternal)
   {
      clusterConfigInternal.setDown_threadForFd(clusterConfPageFirst.getBtndown_threadForFd().getSelection());
      clusterConfigInternal.setDown_threadForPing(clusterConfPageFirst.getBtndown_threadForPing().getSelection());
      clusterConfigInternal.setIp_ttlForUdp(clusterConfPageFirst.getTxtip_ttlForUdp().getText());
      clusterConfigInternal.setIp_mcastForUdp(clusterConfPageFirst.getBtnip_mcastForUdp().getSelection());
      clusterConfigInternal.setLoopbackForUdp(clusterConfPageFirst.getBtnloopbackForUdp().getSelection());
      clusterConfigInternal.setMax_intervalForMerge2(clusterConfPageFirst.getTxtmax_intervalForMerge2().getText());
      clusterConfigInternal.setMcast_addrForUdp(clusterConfPageFirst.getTxtmcast_addrForUdp().getText());
      clusterConfigInternal.setMcast_portForUdp(clusterConfPageFirst.getTxtmcast_portForUdp().getText());
      clusterConfigInternal.setMcast_recv_buf_sizeForUdp(clusterConfPageFirst.getTxtmcast_recv_buf_sizeForUdp()
            .getText());
      clusterConfigInternal.setMcast_send_buf_sizeForUdp(clusterConfPageFirst.getTxtmcast_send_buf_sizeForUdp()
            .getText());
      clusterConfigInternal.setMin_intervalForMerge2(clusterConfPageFirst.getTxtmin_intervalForMerge2().getText());
      clusterConfigInternal.setNum_initial_membersForPing(clusterConfPageFirst.getTxtnum_initial_membersForPing()
            .getText());
      clusterConfigInternal.setShunForFd(clusterConfPageFirst.getBtnshunForFd().getSelection());
      clusterConfigInternal.setTimeoutForPing(clusterConfPageFirst.getTxttimeoutForPing().getText());
      clusterConfigInternal.setUcast_recv_buf_sizeForUdp(clusterConfPageFirst.getTxtucast_recv_buf_sizeForUdp()
            .getText());
      clusterConfigInternal.setUcast_send_buf_sizeForUdp(clusterConfPageFirst.getTxtucast_send_buf_sizeForUdp()
            .getText());
      clusterConfigInternal.setUp_threadForFd(clusterConfPageFirst.getBtnup_threadForFd().getSelection());
      clusterConfigInternal.setUp_threadForPing(clusterConfPageFirst.getBtnup_threadForPing().getSelection());
   }//end of method

   /**
    * Set cluster configuration model part 2 from second page of this wizard
    * @param clusterConfig
    * @return void
    */
   private void setClusterConfigModelSecondPage(ClusterConfigInternal clusterConfigInternal)
   {
      clusterConfigInternal.setSecdesired_avg_gossipForPbCastStable(clusterConfPageSecond
            .getTxtdesired_avg_gossipForPbCastStable().getText());
      clusterConfigInternal.setSecdown_threadForPbCastNakAck(clusterConfPageSecond.getBtndown_threadForPbCastNakAck()
            .getSelection());
      clusterConfigInternal.setSecdown_threadForPbCastStable(clusterConfPageSecond.getBtndown_threadForPbCastStable()
            .getSelection());
      clusterConfigInternal.setSecdown_threadForUniCast(clusterConfPageSecond.getBtndown_threadForUniCast()
            .getSelection());
      clusterConfigInternal.setSecdown_threadForVerifySuspect(clusterConfPageSecond.getBtndown_threadForVerifySuspect()
            .getSelection());
      clusterConfigInternal.setSecgc_lagForPbCastNakAck(clusterConfPageSecond.getTxtgc_lagForPbCastNakAck().getText());
      clusterConfigInternal.setSecmax_xmit_sizeForPbCastNakAck(clusterConfPageSecond
            .getTxtmax_xmit_sizeForPbCastNakAck().getText());
      clusterConfigInternal.setSecmin_thresholdForUniCast(clusterConfPageSecond.getTxtmin_thresholdForUniCast()
            .getText());
      clusterConfigInternal.setSecretransmit_timeoutForPbCastNakAck(clusterConfPageSecond
            .getTxtretransmit_timeoutForPbCastNakAck().getText());
      clusterConfigInternal.setSectimeoutForUniCast(clusterConfPageSecond.getTxttimeoutForUniCast().getText());
      clusterConfigInternal.setSectimeoutForVerifySuspect(clusterConfPageSecond.getTxttimeoutForVerifySuspect()
            .getText());
      clusterConfigInternal.setSecup_threadForPbCastNakAck(clusterConfPageSecond.getBtnup_threadForPbCastNakAck()
            .getSelection());
      clusterConfigInternal.setSecup_threadForPbCastStable(clusterConfPageSecond.getBtnup_threadForPbCastStable()
            .getSelection());
      clusterConfigInternal.setSecup_threadForVerifySuspect(clusterConfPageSecond.getBtnup_threadForVerifySuspect()
            .getSelection());
      clusterConfigInternal.setSecwindow_sizeForUniCast(clusterConfPageSecond.getTxtwindow_sizeForUniCast().getText());
   }//end of method

   /**
    * Set cluster configuration model part 3 from third page of this wizard
    * @param clusterConfig
    * @return void
    */
   private void setClusterConfigModelThirdPage(ClusterConfigInternal clusterConfigInternal)
   {
      clusterConfigInternal.setThirddown_threadForFrag(clusterConfPageThird.getBtndown_threadForFrag().getSelection());
      clusterConfigInternal.setThirddown_threadForPbCastStateTransfer(clusterConfPageThird
            .getBtndown_threadForPbCastStateTransfer().getSelection());
      clusterConfigInternal.setThirdfrag_sizeForFrag(clusterConfPageThird.getTxtfrag_sizeForFrag().getText());
      clusterConfigInternal.setThirdjoin_retry_timeoutForPbCastGms(clusterConfPageThird
            .getTxtjoin_retry_timeoutForPbCastGms().getText());
      clusterConfigInternal.setThirdjoin_timeoutForPbCastGms(clusterConfPageThird.getTxtjoin_timeoutForPbCastGms()
            .getText());
      clusterConfigInternal.setThirdprint_local_addrForPbCastGms(clusterConfPageThird
            .getBtnprint_local_addrForPbCastGms().getSelection());
      clusterConfigInternal.setThirdshunForPbCastGms(clusterConfPageThird.getBtnshunForPbCastGms().getSelection());
      clusterConfigInternal.setThirdup_threadForFrag(clusterConfPageThird.getBtnup_threadForFrag().getSelection());
      clusterConfigInternal.setThirdup_threadForPbCastStateTransfer(clusterConfPageThird
            .getBtnup_threadForPbCastStateTransfer().getSelection());
   }//end of method

   public NewCacheConfigurationWizard getNewCacheConfigurationWizard()
   {
      return this.rootWizard;
   }

}//end of class