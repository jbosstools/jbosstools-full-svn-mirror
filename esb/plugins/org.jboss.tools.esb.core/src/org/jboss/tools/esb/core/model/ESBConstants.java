/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.esb.core.model;

/**
 * @author Viacheslav Kabanovich
 */
public interface ESBConstants {
	public String VERSION_101 = "1.0.1.xsd"; //$NON-NLS-1$
	public String VERSION_110 = "1.1.0.xsd"; //$NON-NLS-1$
	public String VERSION_120 = "1.2.0.xsd"; //$NON-NLS-1$
	public String VERSION_130 = "1.3.0.xsd"; //$NON-NLS-1$
	public String VERSION_131 = "1.3.1.xsd"; //$NON-NLS-1$
	
	public String SCHEMA_PREFIX = "http://anonsvn.labs.jboss.com/labs/jbossesb/trunk/product/etc/schemas/xml/jbossesb-"; //$NON-NLS-1$ 
	public String SCHEMA_101 = SCHEMA_PREFIX + VERSION_101;
	public String SCHEMA_110 = SCHEMA_PREFIX + VERSION_110;
	public String SCHEMA_120 = SCHEMA_PREFIX + VERSION_120;
	public String SCHEMA_130 = SCHEMA_PREFIX + VERSION_130;
	public String SCHEMA_131 = SCHEMA_PREFIX + VERSION_131;

	public String NEW_SCHEMA_PREFIX = "http://anonsvn.jboss.org/repos/labs/labs/jbossesb/trunk/product/etc/schemas/xml/jbossesb-"; //$NON-NLS-1$
	public String NEW_SCHEMA_101 = NEW_SCHEMA_PREFIX + VERSION_101;
	public String NEW_SCHEMA_110 = NEW_SCHEMA_PREFIX + VERSION_110;
	public String NEW_SCHEMA_120 = NEW_SCHEMA_PREFIX + VERSION_120;
	public String NEW_SCHEMA_130 = NEW_SCHEMA_PREFIX + VERSION_130;
	public String NEW_SCHEMA_131 = NEW_SCHEMA_PREFIX + VERSION_131;

	public String SUFF_101 = "101"; //$NON-NLS-1$
	public String SUFF_110 = "110"; //$NON-NLS-1$
	public String SUFF_120 = "120"; //$NON-NLS-1$
	public String SUFF_130 = "130"; //$NON-NLS-1$
	public String SUFF_131 = "131"; //$NON-NLS-1$

	public String[] KNOWN_SUFFIXES = {
		SUFF_101,
		SUFF_110,
		SUFF_120,
		SUFF_130,
		SUFF_131,
	};
	public String ENT_ESB_FILE = "FileESB"; //$NON-NLS-1$
	public String ENT_ESB_FILE_101 = ENT_ESB_FILE + SUFF_101;
	public String ENT_ESB_FILE_110 = ENT_ESB_FILE + SUFF_110;
	public String ENT_ESB_FILE_120 = ENT_ESB_FILE + SUFF_120;
	public String ENT_ESB_FILE_130 = ENT_ESB_FILE + SUFF_130;
	public String ENT_ESB_FILE_131 = ENT_ESB_FILE + SUFF_131;
	
	public String ENT_ESB_PROPERTY = "ESBProperty"; //$NON-NLS-1$
	
	//prefix
	public String ENT_ESB_PROVIDERS = "ESBProviders"; //$NON-NLS-1$

	public String ENT_ESB_SECURITY = "ESBSecurity101P"; //$NON-NLS-1$
	public String ENT_ESB_PROVIDER = "ESBBusProvider101"; //$NON-NLS-1$
	public String ENT_ESB_JBR_PROVIDER = "ESBJBRProvider101"; //$NON-NLS-1$
	public String ENT_ESB_JBR_PROVIDER_120 = "ESBJBRProvider120"; //$NON-NLS-1$
	public String ENT_ESB_SCHEDULE_PROVIDER = "ESBScheduleProvider101"; //$NON-NLS-1$
	public String ENT_ESB_JMS_PROVIDER = "ESBJMSProvider101"; //$NON-NLS-1$
	public String ENT_ESB_JCA_PROVIDER = "ESBJCAProvider101"; //$NON-NLS-1$
	public String ENT_ESB_FS_PROVIDER = "ESBFSProvider101"; //$NON-NLS-1$
	public String ENT_ESB_FTP_PROVIDER = "ESBFTPProvider101"; //$NON-NLS-1$
	public String ENT_ESB_SQL_PROVIDER = "ESBSQLProvider101"; //$NON-NLS-1$
	public String ENT_ESB_HIBERNATE_PROVIDER = "ESBHibernateProvider101"; //$NON-NLS-1$
	public String ENT_ESB_HTTP_PROVIDER = "ESBHTTPProvider110"; //$NON-NLS-1$
	public String ENT_ESB_HTTP_PROVIDER_120 = "ESBHTTPProvider120"; //$NON-NLS-1$
	public String ENT_ESB_CAMEL_PROVIDER_130 = "ESBCamelProvider130"; //$NON-NLS-1$

	static String PREACTION_PREFIX = "ESBPreAction"; //$NON-NLS-1$
	
	public String[] PROVIDERS_101 = {
		ENT_ESB_PROVIDER,
		ENT_ESB_JBR_PROVIDER, ENT_ESB_JBR_PROVIDER_120,
		ENT_ESB_SCHEDULE_PROVIDER,
		ENT_ESB_JMS_PROVIDER,
		ENT_ESB_JCA_PROVIDER,
		ENT_ESB_FS_PROVIDER,
		ENT_ESB_FTP_PROVIDER,
		ENT_ESB_SQL_PROVIDER,
		ENT_ESB_HIBERNATE_PROVIDER,
		ENT_ESB_HTTP_PROVIDER, ENT_ESB_HTTP_PROVIDER_120,
		ENT_ESB_CAMEL_PROVIDER_130,
	};

	
	public String ENT_ESB_BUS = "ESBBus"; //$NON-NLS-1$
	public String ENT_ESB_FS_BUS = "ESBFSBus101"; //$NON-NLS-1$
	public String ENT_ESB_FTP_BUS = "ESBFTPBus101"; //$NON-NLS-1$
	public String ENT_ESB_JBR_BUS = "ESBJBRBus101"; //$NON-NLS-1$
	public String ENT_ESB_JMS_BUS = "ESBJMSBus101"; //$NON-NLS-1$
	public String ENT_ESB_HIBERNATE_BUS = "ESBHibernateBus101"; //$NON-NLS-1$
	public String ENT_ESB_SQL_BUS = "ESBSQLBus101"; //$NON-NLS-1$
	public String ENT_ESB_HTTP_BUS = "ESBHTTPBus110"; //$NON-NLS-1$
	public String ENT_ESB_HTTP_BUS_120 = "ESBHTTPBus120"; //$NON-NLS-1$
	public String ENT_ESB_CAMEL_BUS_130 = "ESBCamelBus130"; //$NON-NLS-1$

	public String[] BUSES_101 = {
		ENT_ESB_BUS,
		ENT_ESB_FS_BUS,
		ENT_ESB_FTP_BUS,
		ENT_ESB_JBR_BUS,
		ENT_ESB_JMS_BUS,
		ENT_ESB_HIBERNATE_BUS,
		ENT_ESB_SQL_BUS,
		ENT_ESB_HTTP_BUS, ENT_ESB_HTTP_BUS_120,
		ENT_ESB_CAMEL_BUS_130,
	};		

	//prefix
	public String ENT_ESB_LISTENERS = "ESBListeners"; //$NON-NLS-1$

	public String ENT_ESB_LISTENER = "ESBListener101"; //$NON-NLS-1$
	public String ENT_ESB_LISTENER_120 = "ESBListener120"; //$NON-NLS-1$
	public String ENT_ESB_SH_LISTENER = "ESBScheduledListener101"; //$NON-NLS-1$
	public String ENT_ESB_GATEWAY = "ESBJCAGateway101"; //$NON-NLS-1$
	public String ENT_ESB_GATEWAY_120 = "ESBJCAGateway120"; //$NON-NLS-1$
	public String ENT_ESB_JMS_LISTENER = "ESBJMSListener101"; //$NON-NLS-1$
	public String ENT_ESB_JMS_LISTENER_130 = "ESBJMSListener130"; //$NON-NLS-1$
	public String ENT_ESB_FS_LISTENER = "ESBFSListener101"; //$NON-NLS-1$
	public String ENT_ESB_FTP_LISTENER = "ESBFTPListener101"; //$NON-NLS-1$
	public String ENT_ESB_FTP_LISTENER_120 = "ESBFTPListener120"; //$NON-NLS-1$
	public String ENT_ESB_SQL_LISTENER = "ESBSQLListener101"; //$NON-NLS-1$
	public String ENT_ESB_SQL_LISTENER_120 = "ESBSQLListener120"; //$NON-NLS-1$
	public String ENT_ESB_HIB_LISTENER = "ESBHibernateListener101"; //$NON-NLS-1$
	public String ENT_ESB_JBR_LISTENER = "ESBJBRListener101"; //$NON-NLS-1$
	public String ENT_ESB_JBR_LISTENER_120 = "ESBJBRListener120"; //$NON-NLS-1$
	public String ENT_ESB_GROOVY_LISTENER = "ESBGroovyListener101"; //$NON-NLS-1$
	public String ENT_ESB_HTTP_LISTENER = "ESBHTTPListener110"; //$NON-NLS-1$
	public String ENT_ESB_HTTP_GATEWAY = "ESBHTTPGateway120"; //$NON-NLS-1$
	public String ENT_ESB_UDP_LISTENER = "ESBUDPListener110"; //$NON-NLS-1$
	public String ENT_ESB_CAMEL_GATEWAY = "ESBCamelGateway130"; //$NON-NLS-1$
	
	public String[] LISTENERS_101 = {
		ENT_ESB_LISTENER, ENT_ESB_LISTENER_120,
		ENT_ESB_SH_LISTENER,
		ENT_ESB_GATEWAY, ENT_ESB_GATEWAY_120,
		ENT_ESB_JMS_LISTENER, ENT_ESB_JMS_LISTENER_130,
		ENT_ESB_FS_LISTENER,
		ENT_ESB_FTP_LISTENER, ENT_ESB_FTP_LISTENER_120,
		ENT_ESB_SQL_LISTENER, ENT_ESB_SQL_LISTENER_120,
		ENT_ESB_HIB_LISTENER,
		ENT_ESB_JBR_LISTENER, ENT_ESB_JBR_LISTENER_120,
		ENT_ESB_GROOVY_LISTENER,
		ENT_ESB_HTTP_LISTENER, ENT_ESB_HTTP_GATEWAY,
		ENT_ESB_UDP_LISTENER,
		ENT_ESB_CAMEL_GATEWAY,
	};

	//prefix
	public String ENT_ESB_SERVICES = "ESBServices"; //$NON-NLS-1$
	//prefix
	public String ENT_ESB_SERVICE = "ESBService"; //$NON-NLS-1$
	public String ENT_ESB_SERVICE_101 = ENT_ESB_SERVICE + SUFF_101;
	public String ENT_ESB_SERVICE_110 = ENT_ESB_SERVICE + SUFF_110;
	public String ENT_ESB_SERVICE_120 = ENT_ESB_SERVICE + SUFF_120;
	public String ENT_ESB_SERVICE_130 = ENT_ESB_SERVICE + SUFF_130;
	public String ENT_ESB_SERVICE_131 = ENT_ESB_SERVICE + SUFF_131;

	//prefix
	public String ENT_ESB_ACTIONS = "ESBActions"; //$NON-NLS-1$
	public String ENT_ESB_ACTIONS_101 = ENT_ESB_ACTIONS + SUFF_101;
	public String ENT_ESB_ACTIONS_110 = ENT_ESB_ACTIONS + SUFF_110;
	public String ENT_ESB_ACTIONS_120 = ENT_ESB_ACTIONS + SUFF_120;
	public String ENT_ESB_ACTIONS_130 = ENT_ESB_ACTIONS + SUFF_130;
	public String ENT_ESB_ACTION = "ESBAction101"; //$NON-NLS-1$
	public String ENT_ESB_ACTION_120 = "ESBAction120"; //$NON-NLS-1$

	public String ENT_ESB_SIMPLE_SCHEDULE = "ESBSimpleSchedule101"; //$NON-NLS-1$
	public String ENT_ESB_CRON_SCHEDULE = "ESBCronSchedule101"; //$NON-NLS-1$

	public String ENT_ESB_ROUTE_TO = "ESBPreRouteTo"; //$NON-NLS-1$	
	public String ENT_ESB_OBJECT_PATH = "ESBPreObjectPath"; //$NON-NLS-1$
	public String ENT_ESB_SEND_TO = "ESBPreSendTo"; //$NON-NLS-1$	

	public String ENT_ESB_NOTIFICATION = "ESBPreNotificationList"; //$NON-NLS-1$
	public String ENT_ESB_NOTIFICATION_120 = ENT_ESB_NOTIFICATION + SUFF_120;
	public String ENT_ESB_TARGET = "ESBPreTarget"; //$NON-NLS-1$
	public String TARGET_NOTIFY_CONSOLE = "NotifyConsole"; //$NON-NLS-1$
	public String TARGET_NOTIFY_FILES = "NotifyFiles"; //$NON-NLS-1$
	public String TARGET_NOTIFY_SQL_TABLE = "NotifySQLTable"; //$NON-NLS-1$

	public String ATTR_NAME = "name"; //$NON-NLS-1$
	public String ATTR_VALUE = "value"; //$NON-NLS-1$
	public String ATTR_PROPERTY_VALUE_PRESENTATION = "value presentation"; //$NON-NLS-1$
	public String ATTR_BUS_ID = "id"; //$NON-NLS-1$
	public String ATTR_BUS_ID_REF = "channel id ref"; //$NON-NLS-1$
	public String ATTR_SCHEDULE_ID = "schedule id"; //$NON-NLS-1$
	public String ATTR_SCHEDULE_ID_REF = "schedule id ref"; //$NON-NLS-1$

	public String ATTR_MESSAGE_FLOW_PRIORITY = "message flow priority"; //$NON-NLS-1$

	public String XML_ATTR_PROTECTED_METHODS = "protected-methods"; //$NON-NLS-1$
}
