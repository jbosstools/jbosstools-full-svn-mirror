package org.jboss.tools.runtime;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.runtime.messages"; //$NON-NLS-1$
	public static String JBossRuntimeStartup_JBoss_Application_Server_6_0;
	public static String JBossRuntimeStartup_Cannot_create_new_JBoss_Server;
	public static String JBossRuntimeStartup_Cannott_create_new_DTP_Connection_Profile;
	public static String JBossRuntimeStartup_Cannott_create_new_HSQL_DB_Driver;
	public static String JBossRuntimeStartup_JBoss_Application_Server_3_2;
	public static String JBossRuntimeStartup_JBoss_Application_Server_4_0;
	public static String JBossRuntimeStartup_JBoss_Application_Server_4_2;
	public static String JBossRuntimeStartup_JBoss_Application_Server_5_0;
	public static String JBossRuntimeStartup_JBoss_Application_Server_5_1;
	public static String JBossRuntimeStartup_JBoss_EAP_Server_4_3;
	public static String JBossRuntimeStartup_JBoss_EAP_Server_5_0;
	public static String JBossRuntimeStartup_Runtime;
	public static String JBossRuntimeStartup_The_JBoss_AS_Hypersonic_embedded_database;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
