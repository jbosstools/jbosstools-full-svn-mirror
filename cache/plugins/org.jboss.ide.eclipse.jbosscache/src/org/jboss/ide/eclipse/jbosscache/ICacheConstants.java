/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache;

/**
 * This interface contains the constants using all over the project
 * @author Gurkaner
 */
public interface ICacheConstants
{

   /**Seperator*/
   String SEPERATOR = "/";

   /**Plug-in Unique Id*/
   String CACHE_PLUGIN_UNIQUE_ID = "org.jboss.ide.eclipse.jbosscache";

   /**Cache Configuration Tags and Attributes*/
   String SERVER = "server";

   String CLASSPATH = "classpath";

   String CODEBASE = "codebase";

   String ARCHIVES = "archives";

   String MBEAN = "mbean";

   String CODE = "code";

   String NAME = "name";

   String DEPENDS = "depends";

   String ATTRIBUTE = "attribute";

   String CONFIG = "config";

   String UDP = "UDP";

   String MCAST_ADDR = "mcast_addr";

   String MCAST_PORT = "mcast_port";

   String IP_TTL = "ip_ttl";

   String IP_MCAST = "ip_mcast";

   String MCAST_SEND_BUF_SIZE = "mcast_send_buf_size";

   String MCAST_RECV_BUF_SIZE = "mcast_recv_buf_size";

   String UCAST_SEND_BUF_SIZE = "ucast_send_buf_size";

   String UCAST_RECV_BUF_SIZE = "ucast_recv_buf_size";

   String LOOPBACK = "loopback";

   String PING = "PING";

   String TIMEOUT = "timeout";

   String NUM_INITIAL_MEMBERS = "num_initial_members";

   String UP_THREAD = "up_thread";

   String DOWN_THREAD = "down_thread";

   String MIN_INTERVAL = "min_interval";

   String MAX_INTERVAL = "max_interval";

   String MERGE2 = "MERGE2";

   String FD = "FD";

   String SHUN = "shun";

   String FD_SOCK = "FD_SOCK";

   String NACKACK = "pbcast.NAKACK";

   String GC_LAG = "gc_lag";

   String RETRANSMIT_TIMEOUT = "retransmit_timeout";

   String MAX_XMIT_SIZE = "max_xmit_size";

   String WINDOW_SIZE = "window_size";

   String MIN_THRESHOLD = "min_threshold";

   String UNICAST = "UNICAST";

   String STABLE = "pbcast.STABLE";

   String DESIRED_AVERAGE_GOSSIP = "desired_avg_gossip";

   String FRAG = "FRAG";

   String FRAG_SIZE = "frag_size";

   String GMS = "pbcast.GMS";

   String JOIN_TIMEOUT = "join_timeout";

   String JOIN_RETRY_TIMEOUT = "join_retry_timeout";

   String PRINT_LOCAL_ADDR = "print_local_addr";

   String STATE_TRANSFER = "pbcast.STATE_TRANSFER";

   String REPLACE = "replace";

   String VERIFY_SUSPECT = "VERIFY_SUSPECT";

   /**View Id Constants*/
   String CACHE_CONTENT_VIEW_ID = "org.jboss.ide.eclipse.jbosscache.views.CacheContentView";

   String CACHE_QUERY_EDITOR_ID = "org.jboss.ide.eclipse.jbosscache.editor.CacheQueryEditor";

   String CACHE_NODE_CONTENT_VIEW_ID = "org.jboss.ide.eclipse.jbosscache.view.NodeContentView";

   String CACHE_GRAPH_VIEW_ID = "org.jboss.ide.eclipse.jbosscache.views.CacheGraphView";

   ////////////////End of View Id Constants////////////////////////

   /**Perspective Id Constants*/
   String CACHE_PERSPECTIVE_ID = "org.jboss.ide.eclipse.jbosscache.perspective2";

   /**Cache Loader Class */
   String CACHE_LOADER_CLASSES[] = new String[]
   {"org.jboss.cache.loader.JDBCCacheLoader", "org.jboss.cache.loader.FileCacheLoader",
         "org.jboss.cache.loader.BdbjeCacheLoader","None"};

   /**JDBC Drivers*/
   String JDBC_DRIVERS[] = new String[]
   {"oracle.jdbc.OracleDriver"};

   /**JDBC URL Pattern*/
   String JDBC_URL_PATTERN[] = new String[]
   {"jdbc:oracle:thin:@{host}:port:name"};

   /**TRANSACTION_MANAGER_LOOK_UP CLASSES*/
   String TRANSACTION_MANAGER_LOOKUP_CLASSES[] = new String[]
   {"None","org.jboss.cache.GenericTransactionManagerLookup","org.jboss.cache.DummyTransactionManagerLookup", "org.jboss.cache.JBossTransactionManagerLookup","org.jboss.cache.BatchModeTransactionManagerLookup"};

   /**CACHE MODES*/
   String CACHE_MODES[] = new String[]
   {"LOCAL", "REPL_SYNC", "REPL_ASYNC","INVALIDATION_ASYNC","INVALIDATION_SYNC"};

   /**TRANSACTION ISOLATION LEVELS*/
   String ISOLATION_LEVELS[] = new String[]
   {"NONE", "READ_UNCOMMITTED", "READ_COMMITTED", "REPEATABLE_READ", "SERIALIZABLE"};

   /**IMAGE PATHS*/
   String PLUGIN_IMAGE_PATH = "icons/";

   String PLUGIN_IMAGE_PATH_ELCL16 = "icons/elcl16/";

   String PLUGIN_IMAGE_PATH_DLCL16 = "icons/dlcl16/";

   /**IMAGES*/
   String IMAGE_KEY_ECLIPSE_GIF = "sample.gif";

   String IMAGE_KEY_RUN_EXC = "run_exc.gif";

   String IMAGE_KEY_TERM_SBOOK = "term_sbook.gif";

   String IMAGE_KEY_NEW_CON = "new_con.gif";

   String IMAGE_KEY_NEWPACK_WIZ = "newpack_wiz.gif";

   String IMAGE_KEY_DELETE_EDIT = "delete_edit.gif";

   String IMAGE_KEY_SEARCH_REF_OBJ = "search_ref_obj.gif";

   String IMAGE_KEY_WATCHLIST_VIEW = "watchlist_view.gif";

   String IMAGE_KEY_EXTERNAL_JAR = "external_jar.gif";

   String IMAGE_KEY_EDIT_CON = "edtsrclkup_co.gif";

   String IMAGE_KEY_CLASS_OBJ = "class_obj.gif";

   String IMAGE_KEY_FIELD_PUBLIC_OBJ = "field_public_obj.gif";

   String IMAGE_KEY_IMPORT_WIZ = "import_wiz.gif";

   String IMAGE_KEY_EXPORT_WIZ = "export_wiz.gif";

   String IMAGE_KEY_FILE_OBJ = "file_obj.gif";
   
   String IMAGE_KEY_SERVER = "server.gif";
   
   String IMAGE_KEY_SERVER_RUNNING = "serverrunning.gif";
   
   String IMAGE_KEY_SERVER_NOT_RUNNING = "servernotrunning.gif";
   
   String IMAGE_KEY_SERVER_NAVIGATOR = "servernavigator.gif";
   
   String IMAGE_KEY_SERVER_START_ACTION = "startserveraction.gif";
   
   String IMAGE_KEY_SERVER_SHUT_DOWN_ACTOIN = "shutdowservernaction.gif";
   
   String IMAGE_KEY_REMOTE_CONNECT_ACTION = "disconnect_co.gif";
   
   String IMAGE_KEY_REFRESH_NAV_ACTION = "refresh_nav.gif";
   
   String IMAGE_KEY_JBOSS_GIF = "jboss.gif";
   
   String IMAGE_KEY_DB16_GIF = "jdbc_16.gif";
   
   
   

   /**Resource Bundle Keys*/
   String CONFIGURATION_MONITOR_TASK_NAME = "configuration.monitor.task.name";

   String CONFIGURATION_MONITOR_SUBTASK_CLUSTERCONFIG_NAME = "configuration.monitor.subtask.clusterconfig.name";

   String CONFIGURATION_MONITOR_SUBTASK_CACHE_LOADER_CONFIG_NAME = "configuration.monitor.subtask.cacheloaderconfig.name";

   String CONFIGURATION_ERROR_MESSAGE_CACHEEXIST = "configuration.error.message.cache.exist.message";

   String CONFIGURATION_ERROR_MESSAGE_CACHEEXIST_TITLE = "configuration.error.message.cache.exist.title";

   String TREECACHEVIEW_NEW_CONFIGURATION_ACTION = "TreeCacheView_New_Configuration_Action";

   String TREECACHEVIEW_EDIT_CONFIGURATION_ACTION = "TreeCacheView_Edit_Configuration_Action";

   String TREECACHEVIEW_DELETE_CONFIGURATION_ACTION = "TreeCacheView_Delete_Configuration_Action";

   String TREECACHEVIEW_DELETE_CONFIGURATION_DIALOG_TITLE = "TreeCacheView_Delete_Configuration_Dialog_Title";

   String TREECACHEVIEW_DELETE_CONFIGURATION_DIALOG_MESSAGE = "TreeCacheView_Delete_Configuration_Dialog_Message";

   String TREECACHEVIEW_CONNECT_ACTION = "TreeCacheView_Connect_Action";

   String TREECACHEVIEW_DISCONNECT_ACTION = "TreeCacheView_Disconnect_Action";

   String TREECACHEVIEW_ADD_NODE_ACTION = "TreeCacheView_Add_Node_Action";

   String TREECACHEVIEW_DELETE_NODE_ACTION = "TreeCacheView_Delete_Node_Action";

   String TREECACHEVIEW_SHOW_NODE_CONTENT_ACTION = "TreeCacheView_Show_Node_Content_Action";

   String TREECACHEVIEW_SHOW_OBJECT_GRAPH_ACTION = "TreeCacheView_Show_Object_Graph_Action";

   String TREECACHEVIEW_CONNECT_ACTION_MESSAGE_DIALOG_TITLE = "TreeCacheView_Connect_Action_Message_Dialog_Title";

   String TREECACHEVIEW_CONNECT_ACTION_MESSAGE_DIALOG_MESSAGE = "TreeCacheView_Connect_Action_Message_Dialog_Message";

   String TREECACHEVIEW_CONNECT_ACTION_ERROR_DIALOG_TITLE = "TreeCacheView_Connect_Action_Error_Dialog_Title";

   String TREECACHEVIEW_CONNECT_ACTION_ERROR_DIALOG_MESSAGE = "TreeCacheView_Connect_Action_Error_Dialog_Message";

   String WIZARDPAGE_STANDARD_CONFIGURATION_PAGE_ADD_JAR_TEXT = "Wizard_Page_Standard_Configuration_Page_Add_Jar_Text";

   String WIZARDPAGE_STANDARD_CONFIGURATION_PAGE_ADD_JAR_BUTTON = "Wizard_Page_Standard_Configuration_Page_Add_Jar_Button";

   String WIZARDPAGE_STANDARD_CONFIGURATION_PAGE_REMOVE_JAR_BUTTON = "Wizard_Page_Standard_Configuration_Page_Remove_Jar_Button";

   String JBOSS_CACHE_PLUGIN_CACHE_SAVING_PARTICIPANT_LOGICAL_FILE_NAME = "JBoss_Cache_Plugin_Cache_Saving_Participant_Logicial_File_Name";

   String NODECONTENTVIEW_CLEAR_TABLE_ACTION = "Node_Content_View_Clear_Table_Action";

   /**File Extension Type*/
   String XML_FILEEXTENSION = ".xml";

   /**Table Column Information used in NodeContentView, these are defined in RB with given keys*/
   String NODE_CONTENT_VIEW_TABLE_COLUMN_PARENT = "Node_Content_View_Table_Column_Parent";

   String NODE_CONTENT_VIEW_TABLE_COLUMN_FQN = "Node_Content_View_Table_Column_Fqn";

   String NODE_CONTENT_VIEW_TABLE_COLUMN_KEY = "Node_Content_View_Table_Column_Key";

   String NODE_CONTENT_VIEW_TABLE_COLUMN_VALUE = "Node_Content_View_Table_Column_Value";

   /**Type of the JBoss Cache*/
   String JBOSS_CACHE_TREE_CACHE = "TreeCache";

   String JBOSS_CACHE_TREE_CACHE_AOP = "TreeCacheAOP";

   String JBOSS_INTERNAL = "__JBossInternal__";

   String JBOSS_CLASS_INTERNAL = "__jboss:internal:class__";

   String JBOSS_INTERNAL_REF_MAP = JBOSS_INTERNAL + "/__RefMap__";

   /**Reference Instance Key*/
   String REFERENCED_CACHE_KEY = "refFqn_";

   String[] CACHE_TYPE_MODE = new String[]
   {JBOSS_CACHE_TREE_CACHE, JBOSS_CACHE_TREE_CACHE_AOP};

}//end of interface