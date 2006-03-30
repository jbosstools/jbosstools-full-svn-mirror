/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.model.config;

/**
 * This model holds all the configuration related information
 * @author Gurkaner
 */
public class CacheConfigurationModel
{

   /**Cache Loader Configuration Information*/
   private CacheLoaderConfigInternal cacheLoaderConfig;

   /**Cluster Configuration Information*/
   private ClusterConfigInternal clusterConfig;

   /**Eviction Policy Configuration Information*/
   private EvictionConfigInternal evictionPolicyConfig;

   private String directoryName;

   private String cacheName;

   private String cacheMode;

   private String clusterName;

   private String transManagerLookUpClass;

   private String isolationLevel;

   private boolean useReplQueue;

   private String replQueueInterval;

   private String replQueueMaxElements;

   private String initialStateRetrievalTimeOut;

   private String syncReplicationTimeOut;

   private String lockAcquisitionTimeOut;

   private boolean fetchStateOnStartup;

   private boolean useMarshalling = false;

   private String evictionPolicyClass;

   /**Model instance */
   private CacheConfigurationModel cachConfModel;

   /**
    * Constructor
    *
    */
   public CacheConfigurationModel()
   {
      cacheLoaderConfig = new CacheLoaderConfigInternal();
      clusterConfig = new ClusterConfigInternal();
      evictionPolicyConfig = new EvictionConfigInternal();
   }

   /**
    * Return new CacheConfigurationModel object instance
    * @return
    */
   public CacheConfigurationModel getInstance()
   {
      this.cachConfModel = new CacheConfigurationModel();
      return cachConfModel;
   }

   /**
    * Contains the cache loader configuration related params
    * @author Gurkaner
    *
    */
   public class CacheLoaderConfigInternal
   {
      private boolean isFileCacheLoader;
      
      private boolean isUseDataSource;

      private boolean isJdbcCacheLoader;

      private String cacheLoaderClass = "";

      private boolean cacheLoaderShared;

      private String cacheLoaderPreload = "";

      private boolean cacheLoaderPassivation;

      private boolean cacheLoaderFetchPersistentState;

      private boolean cacheLoaderFetchTransientState;

      private boolean cacheLoaderAsynchronous;

      private String tableName = "";

      private boolean tableCreate;

      private boolean tableDrop;

      private String fqnColumn;

      private String fqnType;

      private String nodeColumn;

      private String nodeType;

      private String parentColumn;

      private String driver;

      private String driverUrl;

      private String userName;

      private String password;

      private String dataSource;

      private String fileLocation;

      public boolean isCacheLoaderAsynchronous()
      {
         return cacheLoaderAsynchronous;
      }

      public void setCacheLoaderAsynchronous(boolean cacheLoaderAsynchronous)
      {
         this.cacheLoaderAsynchronous = cacheLoaderAsynchronous;
      }

      public String getCacheLoaderClass()
      {
         return cacheLoaderClass;
      }

      public void setCacheLoaderClass(String cacheLoaderClass)
      {
         this.cacheLoaderClass = cacheLoaderClass;
      }

      public boolean isCacheLoaderFetchPersistentState()
      {
         return cacheLoaderFetchPersistentState;
      }

      public void setCacheLoaderFetchPersistentState(boolean cacheLoaderFetchPersistentState)
      {
         this.cacheLoaderFetchPersistentState = cacheLoaderFetchPersistentState;
      }

      public boolean isCacheLoaderFetchTransientState()
      {
         return cacheLoaderFetchTransientState;
      }

      public void setCacheLoaderFetchTransientState(boolean cacheLoaderFetchTransientState)
      {
         this.cacheLoaderFetchTransientState = cacheLoaderFetchTransientState;
      }

      public boolean isCacheLoaderPassivation()
      {
         return cacheLoaderPassivation;
      }

      public void setCacheLoaderPassivation(boolean cacheLoaderPassivation)
      {
         this.cacheLoaderPassivation = cacheLoaderPassivation;
      }

      public String getCacheLoaderPreload()
      {
         return cacheLoaderPreload;
      }

      public void setCacheLoaderPreload(String cacheLoaderPreload)
      {
         this.cacheLoaderPreload = cacheLoaderPreload;
      }

      public boolean isCacheLoaderShared()
      {
         return cacheLoaderShared;
      }

      public void setCacheLoaderShared(boolean cacheLoaderShared)
      {
         this.cacheLoaderShared = cacheLoaderShared;
      }

      public String getDataSource()
      {
         return dataSource;
      }

      public void setDataSource(String dataSource)
      {
         this.dataSource = dataSource;
      }

      public String getDriver()
      {
         return driver;
      }

      public void setDriver(String driver)
      {
         this.driver = driver;
      }

      public String getDriverUrl()
      {
         return driverUrl;
      }

      public void setDriverUrl(String driverUrl)
      {
         this.driverUrl = driverUrl;
      }

      public String getFileLocation()
      {
         return fileLocation;
      }

      public void setFileLocation(String fileLocation)
      {
         this.fileLocation = fileLocation;
      }

      public String getFqnColumn()
      {
         return fqnColumn;
      }

      public void setFqnColumn(String fqnColumn)
      {
         this.fqnColumn = fqnColumn;
      }

      public String getFqnType()
      {
         return fqnType;
      }

      public void setFqnType(String fqnType)
      {
         this.fqnType = fqnType;
      }

      public boolean isFileCacheLoader()
      {
         return isFileCacheLoader;
      }

      public void setFileCacheLoader(boolean isFileCacheLoader)
      {
         this.isFileCacheLoader = isFileCacheLoader;
      }

      public boolean isJdbcCacheLoader()
      {
         return isJdbcCacheLoader;
      }

      public void setJdbcCacheLoader(boolean isJdbcCacheLoader)
      {
         this.isJdbcCacheLoader = isJdbcCacheLoader;
      }

      public String getNodeColumn()
      {
         return nodeColumn;
      }

      public void setNodeColumn(String nodeColumn)
      {
         this.nodeColumn = nodeColumn;
      }

      public String getNodeType()
      {
         return nodeType;
      }

      public void setNodeType(String nodeType)
      {
         this.nodeType = nodeType;
      }

      public String getParentColumn()
      {
         return parentColumn;
      }

      public void setParentColumn(String parentColumn)
      {
         this.parentColumn = parentColumn;
      }

      public String getPassword()
      {
         return password;
      }

      public void setPassword(String password)
      {
         this.password = password;
      }

      public boolean isTableCreate()
      {
         return tableCreate;
      }

      public void setTableCreate(boolean tableCreate)
      {
         this.tableCreate = tableCreate;
      }

      public boolean isTableDrop()
      {
         return tableDrop;
      }

      public void setTableDrop(boolean tableDrop)
      {
         this.tableDrop = tableDrop;
      }

      public String getTableName()
      {
         return tableName;
      }

      public void setTableName(String tableName)
      {
         this.tableName = tableName;
      }

      public String getUserName()
      {
         return userName;
      }

      public void setUserName(String userName)
      {
         this.userName = userName;
      }

      public boolean isUseDataSource()
      {
         return isUseDataSource;
      }

      public void setUseDataSource(boolean isUseDataSource)
      {
         this.isUseDataSource = isUseDataSource;
      }

   }

   /**
    * Contains the cluster configuration related params with default values
    * @author Gurkaner
    *
    */
   public class ClusterConfigInternal
   {
      //First part
      private String mcast_addrForUdp = "228.1.2.3";

      private String mcast_portForUdp = "45577";

      private String ip_ttlForUdp = "64";

      private boolean ip_mcastForUdp = true;

      private String mcast_send_buf_sizeForUdp = "150000";

      private String mcast_recv_buf_sizeForUdp = "80000";

      private String ucast_recv_buf_sizeForUdp = "150000";

      private String ucast_send_buf_sizeForUdp = "80000";

      private String timeoutForPing = "2000";

      private boolean loopbackForUdp = false;

      private String num_initial_membersForPing = "3";

      private boolean up_threadForPing = false;

      private boolean down_threadForPing = false;

      private String min_intervalForMerge2 = "10000";

      private String max_intervalForMerge2 = "20000";

      private boolean shunForFd = true;

      private boolean up_threadForFd = true;

      private boolean down_threadForFd = true;

      //Second part
      private String sectimeoutForVerifySuspect = "1500";

      private boolean secup_threadForVerifySuspect = false;

      private boolean secdown_threadForVerifySuspect = false;

      private String secgc_lagForPbCastNakAck = "50";

      private String secretransmit_timeoutForPbCastNakAck = "600,1200,2400,4800";

      private String secmax_xmit_sizeForPbCastNakAck = "8192";

      private boolean secup_threadForPbCastNakAck = false;

      private boolean secdown_threadForPbCastNakAck = false;

      private String sectimeoutForUniCast = "600,1200,2400";

      private String secwindow_sizeForUniCast = "100";

      private String secmin_thresholdForUniCast = "10";

      private boolean secdown_threadForUniCast = false;

      private String secdesired_avg_gossipForPbCastStable = "20000";

      private boolean secdown_threadForPbCastStable = false;

      private boolean secup_threadForPbCastStable = false;

      //Third Part
      private String thirdfrag_sizeForFrag = "8192";

      private boolean thirddown_threadForFrag = false;

      private boolean thirdup_threadForFrag = false;

      private String thirdjoin_timeoutForPbCastGms = "5000";

      private String thirdjoin_retry_timeoutForPbCastGms = "2000";

      private boolean thirdshunForPbCastGms = true;

      private boolean thirdprint_local_addrForPbCastGms = true;

      private boolean thirddown_threadForPbCastStateTransfer = true;

      private boolean thirdup_threadForPbCastStateTransfer = true;

      public boolean isDown_threadForFd()
      {
         return down_threadForFd;
      }

      public void setDown_threadForFd(boolean down_threadForFd)
      {
         this.down_threadForFd = down_threadForFd;
      }

      public boolean isDown_threadForPing()
      {
         return down_threadForPing;
      }

      public void setDown_threadForPing(boolean down_threadForPing)
      {
         this.down_threadForPing = down_threadForPing;
      }

      public String getIp_ttlForUdp()
      {
         return ip_ttlForUdp;
      }

      public void setIp_ttlForUdp(String ip_ttlForUdp)
      {
         this.ip_ttlForUdp = ip_ttlForUdp;
      }

      public String getMax_intervalForMerge2()
      {
         return max_intervalForMerge2;
      }

      public void setMax_intervalForMerge2(String max_intervalForMerge2)
      {
         this.max_intervalForMerge2 = max_intervalForMerge2;
      }

      public String getMcast_addrForUdp()
      {
         return mcast_addrForUdp;
      }

      public void setMcast_addrForUdp(String mcast_addrForUdp)
      {
         this.mcast_addrForUdp = mcast_addrForUdp;
      }

      public String getMcast_portForUdp()
      {
         return mcast_portForUdp;
      }

      public void setMcast_portForUdp(String mcast_portForUdp)
      {
         this.mcast_portForUdp = mcast_portForUdp;
      }

      public String getMcast_recv_buf_sizeForUdp()
      {
         return mcast_recv_buf_sizeForUdp;
      }

      public void setMcast_recv_buf_sizeForUdp(String mcast_recv_buf_sizeForUdp)
      {
         this.mcast_recv_buf_sizeForUdp = mcast_recv_buf_sizeForUdp;
      }

      public String getMcast_send_buf_sizeForUdp()
      {
         return mcast_send_buf_sizeForUdp;
      }

      public void setMcast_send_buf_sizeForUdp(String mcast_send_buf_sizeForUdp)
      {
         this.mcast_send_buf_sizeForUdp = mcast_send_buf_sizeForUdp;
      }

      public String getMin_intervalForMerge2()
      {
         return min_intervalForMerge2;
      }

      public void setMin_intervalForMerge2(String min_intervalForMerge2)
      {
         this.min_intervalForMerge2 = min_intervalForMerge2;
      }

      public String getNum_initial_membersForPing()
      {
         return num_initial_membersForPing;
      }

      public void setNum_initial_membersForPing(String num_initial_membersForPing)
      {
         this.num_initial_membersForPing = num_initial_membersForPing;
      }

      public boolean isShunForFd()
      {
         return shunForFd;
      }

      public void setShunForFd(boolean shunForFd)
      {
         this.shunForFd = shunForFd;
      }

      public String getTimeoutForPing()
      {
         return timeoutForPing;
      }

      public void setTimeoutForPing(String timeoutForPing)
      {
         this.timeoutForPing = timeoutForPing;
      }

      public String getUcast_recv_buf_sizeForUdp()
      {
         return ucast_recv_buf_sizeForUdp;
      }

      public void setUcast_recv_buf_sizeForUdp(String ucast_recv_buf_sizeForUdp)
      {
         this.ucast_recv_buf_sizeForUdp = ucast_recv_buf_sizeForUdp;
      }

      public String getUcast_send_buf_sizeForUdp()
      {
         return ucast_send_buf_sizeForUdp;
      }

      public void setUcast_send_buf_sizeForUdp(String ucast_send_buf_sizeForUdp)
      {
         this.ucast_send_buf_sizeForUdp = ucast_send_buf_sizeForUdp;
      }

      public boolean isUp_threadForFd()
      {
         return up_threadForFd;
      }

      public void setUp_threadForFd(boolean up_threadForFd)
      {
         this.up_threadForFd = up_threadForFd;
      }

      public boolean isUp_threadForPing()
      {
         return up_threadForPing;
      }

      public void setUp_threadForPing(boolean up_threadForPing)
      {
         this.up_threadForPing = up_threadForPing;
      }

      public String getSecdesired_avg_gossipForPbCastStable()
      {
         return secdesired_avg_gossipForPbCastStable;
      }

      public void setSecdesired_avg_gossipForPbCastStable(String secdesired_avg_gossipForPbCastStable)
      {
         this.secdesired_avg_gossipForPbCastStable = secdesired_avg_gossipForPbCastStable;
      }

      public boolean isSecdown_threadForPbCastNakAck()
      {
         return secdown_threadForPbCastNakAck;
      }

      public void setSecdown_threadForPbCastNakAck(boolean secdown_threadForPbCastNakAck)
      {
         this.secdown_threadForPbCastNakAck = secdown_threadForPbCastNakAck;
      }

      public boolean isSecdown_threadForPbCastStable()
      {
         return secdown_threadForPbCastStable;
      }

      public void setSecdown_threadForPbCastStable(boolean secdown_threadForPbCastStable)
      {
         this.secdown_threadForPbCastStable = secdown_threadForPbCastStable;
      }

      public boolean isSecdown_threadForUniCast()
      {
         return secdown_threadForUniCast;
      }

      public void setSecdown_threadForUniCast(boolean secdown_threadForUniCast)
      {
         this.secdown_threadForUniCast = secdown_threadForUniCast;
      }

      public boolean isSecdown_threadForVerifySuspect()
      {
         return secdown_threadForVerifySuspect;
      }

      public void setSecdown_threadForVerifySuspect(boolean secdown_threadForVerifySuspect)
      {
         this.secdown_threadForVerifySuspect = secdown_threadForVerifySuspect;
      }

      public String getSecgc_lagForPbCastNakAck()
      {
         return secgc_lagForPbCastNakAck;
      }

      public void setSecgc_lagForPbCastNakAck(String secgc_lagForPbCastNakAck)
      {
         this.secgc_lagForPbCastNakAck = secgc_lagForPbCastNakAck;
      }

      public String getSecmax_xmit_sizeForPbCastNakAck()
      {
         return secmax_xmit_sizeForPbCastNakAck;
      }

      public void setSecmax_xmit_sizeForPbCastNakAck(String secmax_xmit_sizeForPbCastNakAck)
      {
         this.secmax_xmit_sizeForPbCastNakAck = secmax_xmit_sizeForPbCastNakAck;
      }

      public String getSecmin_thresholdForUniCast()
      {
         return secmin_thresholdForUniCast;
      }

      public void setSecmin_thresholdForUniCast(String secmin_thresholdForUniCast)
      {
         this.secmin_thresholdForUniCast = secmin_thresholdForUniCast;
      }

      public String getSecretransmit_timeoutForPbCastNakAck()
      {
         return secretransmit_timeoutForPbCastNakAck;
      }

      public void setSecretransmit_timeoutForPbCastNakAck(String secretransmit_timeoutForPbCastNakAck)
      {
         this.secretransmit_timeoutForPbCastNakAck = secretransmit_timeoutForPbCastNakAck;
      }

      public String getSectimeoutForUniCast()
      {
         return sectimeoutForUniCast;
      }

      public void setSectimeoutForUniCast(String sectimeoutForUniCast)
      {
         this.sectimeoutForUniCast = sectimeoutForUniCast;
      }

      public String getSectimeoutForVerifySuspect()
      {
         return sectimeoutForVerifySuspect;
      }

      public void setSectimeoutForVerifySuspect(String sectimeoutForVerifySuspect)
      {
         this.sectimeoutForVerifySuspect = sectimeoutForVerifySuspect;
      }

      public boolean isSecup_threadForPbCastNakAck()
      {
         return secup_threadForPbCastNakAck;
      }

      public void setSecup_threadForPbCastNakAck(boolean secup_threadForPbCastNakAck)
      {
         this.secup_threadForPbCastNakAck = secup_threadForPbCastNakAck;
      }

      public boolean isSecup_threadForPbCastStable()
      {
         return secup_threadForPbCastStable;
      }

      public void setSecup_threadForPbCastStable(boolean secup_threadForPbCastStable)
      {
         this.secup_threadForPbCastStable = secup_threadForPbCastStable;
      }

      public boolean isSecup_threadForVerifySuspect()
      {
         return secup_threadForVerifySuspect;
      }

      public void setSecup_threadForVerifySuspect(boolean secup_threadForVerifySuspect)
      {
         this.secup_threadForVerifySuspect = secup_threadForVerifySuspect;
      }

      public String getSecwindow_sizeForUniCast()
      {
         return secwindow_sizeForUniCast;
      }

      public void setSecwindow_sizeForUniCast(String secwindow_sizeForUniCast)
      {
         this.secwindow_sizeForUniCast = secwindow_sizeForUniCast;
      }

      public boolean isThirdshunForPbCastGms()
      {
         return thirdshunForPbCastGms;
      }

      public void setThirdshunForPbCastGms(boolean thirdshunForPbCastGms)
      {
         this.thirdshunForPbCastGms = thirdshunForPbCastGms;
      }

      public boolean isThirddown_threadForFrag()
      {
         return thirddown_threadForFrag;
      }

      public void setThirddown_threadForFrag(boolean thirddown_threadForFrag)
      {
         this.thirddown_threadForFrag = thirddown_threadForFrag;
      }

      public boolean isThirddown_threadForPbCastStateTransfer()
      {
         return thirddown_threadForPbCastStateTransfer;
      }

      public void setThirddown_threadForPbCastStateTransfer(boolean thirddown_threadForPbCastStateTransfer)
      {
         this.thirddown_threadForPbCastStateTransfer = thirddown_threadForPbCastStateTransfer;
      }

      public String getThirdfrag_sizeForFrag()
      {
         return thirdfrag_sizeForFrag;
      }

      public void setThirdfrag_sizeForFrag(String thirdfrag_sizeForFrag)
      {
         this.thirdfrag_sizeForFrag = thirdfrag_sizeForFrag;
      }

      public String getThirdjoin_retry_timeoutForPbCastGms()
      {
         return thirdjoin_retry_timeoutForPbCastGms;
      }

      public void setThirdjoin_retry_timeoutForPbCastGms(String thirdjoin_retry_timeoutForPbCastGms)
      {
         this.thirdjoin_retry_timeoutForPbCastGms = thirdjoin_retry_timeoutForPbCastGms;
      }

      public String getThirdjoin_timeoutForPbCastGms()
      {
         return thirdjoin_timeoutForPbCastGms;
      }

      public void setThirdjoin_timeoutForPbCastGms(String thirdjoin_timeoutForPbCastGms)
      {
         this.thirdjoin_timeoutForPbCastGms = thirdjoin_timeoutForPbCastGms;
      }

      public boolean isThirdprint_local_addrForPbCastGms()
      {
         return thirdprint_local_addrForPbCastGms;
      }

      public void setThirdprint_local_addrForPbCastGms(boolean thirdprint_local_addrForPbCastGms)
      {
         this.thirdprint_local_addrForPbCastGms = thirdprint_local_addrForPbCastGms;
      }

      public boolean isThirdup_threadForFrag()
      {
         return thirdup_threadForFrag;
      }

      public void setThirdup_threadForFrag(boolean thirdup_threadForFrag)
      {
         this.thirdup_threadForFrag = thirdup_threadForFrag;
      }

      public boolean isThirdup_threadForPbCastStateTransfer()
      {
         return thirdup_threadForPbCastStateTransfer;
      }

      public void setThirdup_threadForPbCastStateTransfer(boolean thirdup_threadForPbCastStateTransfer)
      {
         this.thirdup_threadForPbCastStateTransfer = thirdup_threadForPbCastStateTransfer;
      }

      public boolean isIp_mcastForUdp()
      {
         return ip_mcastForUdp;
      }

      public void setIp_mcastForUdp(boolean ip_mcastForUdp)
      {
         this.ip_mcastForUdp = ip_mcastForUdp;
      }

      public boolean isLoopbackForUdp()
      {
         return loopbackForUdp;
      }

      public void setLoopbackForUdp(boolean loopbackForUdp)
      {
         this.loopbackForUdp = loopbackForUdp;
      }

   }

   /**
    * Contains the eviction policy configuration related params
    * @author Gurkaner
    *
    */
   public class EvictionConfigInternal
   {

   }

   public CacheLoaderConfigInternal getCacheLoaderConfig()
   {
      return cacheLoaderConfig;
   }

   public void setCacheLoaderConfig(CacheLoaderConfigInternal cacheLoaderConfig)
   {
      this.cacheLoaderConfig = cacheLoaderConfig;
   }

   public ClusterConfigInternal getClusterConfig()
   {
      return clusterConfig;
   }

   public void setClusterConfig(ClusterConfigInternal clusterConfig)
   {
      this.clusterConfig = clusterConfig;
   }

   public EvictionConfigInternal getEvictionPolicyConfig()
   {
      return evictionPolicyConfig;
   }

   public void setEvictionPolicyConfig(EvictionConfigInternal evictionPolicyConfig)
   {
      this.evictionPolicyConfig = evictionPolicyConfig;
   }

   public String getCacheMode()
   {
      return cacheMode;
   }

   public void setCacheMode(String cacheMode)
   {
      this.cacheMode = cacheMode;
   }

   public String getCacheName()
   {
      return cacheName;
   }

   public void setCacheName(String cacheName)
   {
      this.cacheName = cacheName;
   }

   public String getClusterName()
   {
      return clusterName;
   }

   public void setClusterName(String clusterName)
   {
      this.clusterName = clusterName;
   }

   public String getEvictionPolicyClass()
   {
      return evictionPolicyClass;
   }

   public void setEvictionPolicyClass(String evictionPolicyClass)
   {
      this.evictionPolicyClass = evictionPolicyClass;
   }

   public boolean isFetchStateOnStartup()
   {
      return fetchStateOnStartup;
   }

   public void setFetchStateOnStartup(boolean fetchStateOnStartup)
   {
      this.fetchStateOnStartup = fetchStateOnStartup;
   }

   public String getInitialStateRetrievalTimeOut()
   {
      return initialStateRetrievalTimeOut;
   }

   public void setInitialStateRetrievalTimeOut(String initialStateRetrievalTimeOut)
   {
      this.initialStateRetrievalTimeOut = initialStateRetrievalTimeOut;
   }

   public String getIsolationLevel()
   {
      return isolationLevel;
   }

   public void setIsolationLevel(String isolationLevel)
   {
      this.isolationLevel = isolationLevel;
   }

   public String getLockAcquisitionTimeOut()
   {
      return lockAcquisitionTimeOut;
   }

   public void setLockAcquisitionTimeOut(String lockAcquisitionTimeOut)
   {
      this.lockAcquisitionTimeOut = lockAcquisitionTimeOut;
   }

   public String getReplQueueInterval()
   {
      return replQueueInterval;
   }

   public void setReplQueueInterval(String replQueueInterval)
   {
      this.replQueueInterval = replQueueInterval;
   }

   public String getReplQueueMaxElements()
   {
      return replQueueMaxElements;
   }

   public void setReplQueueMaxElements(String replQueueMaxElements)
   {
      this.replQueueMaxElements = replQueueMaxElements;
   }

   public String getSyncReplicationTimeOut()
   {
      return syncReplicationTimeOut;
   }

   public void setSyncReplicationTimeOut(String syncReplicationTimeOut)
   {
      this.syncReplicationTimeOut = syncReplicationTimeOut;
   }

   public String getTransManagerLookUpClass()
   {
      return transManagerLookUpClass;
   }

   public void setTransManagerLookUpClass(String transManagerLookUpClass)
   {
      this.transManagerLookUpClass = transManagerLookUpClass;
   }

   public boolean isUseReplQueue()
   {
      return useReplQueue;
   }

   public void setUseReplQueue(boolean useReplQueue)
   {
      this.useReplQueue = useReplQueue;
   }

   public boolean isUseMarshalling()
   {
      return useMarshalling;
   }

   public void setUseMarshalling(boolean useMarshalling)
   {
      this.useMarshalling = useMarshalling;
   }

   public String getDirectoryName()
   {
      return directoryName;
   }

   public void setDirectoryName(String directoryName)
   {
      this.directoryName = directoryName;
   }
}