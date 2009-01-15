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
	public String SCHEMA_101 = "http://anonsvn.labs.jboss.com/labs/jbossesb/trunk/product/etc/schemas/xml/jbossesb-1.0.1.xsd"; //$NON-NLS-1$
	
	public String SUFF_101 = "101"; //$NON-NLS-1$

	public String ENT_ESB_FILE = "FileESB"; //$NON-NLS-1$
	public String ENT_ESB_FILE_101 = ENT_ESB_FILE + SUFF_101;
	
	public String ENT_ESB_PROPERTY = "ESBProperty"; //$NON-NLS-1$
	
	public String ENT_ESB_PROVIDERS = "ESBProviders101"; //$NON-NLS-1$

	public String ENT_ESB_SECURITY = "ESBSecurity101P"; //$NON-NLS-1$
	public String ENT_ESB_PROVIDER = "ESBBusProvider101"; //$NON-NLS-1$
	public String ENT_ESB_JBR_PROVIDER = "ESBJBRProvider101"; //$NON-NLS-1$
	public String ENT_ESB_SCHEDULE_PROVIDER = "ESBScheduleProvider101"; //$NON-NLS-1$
	public String ENT_ESB_JMS_PROVIDER = "ESBJMSProvider101"; //$NON-NLS-1$
	public String ENT_ESB_JCA_PROVIDER = "ESBJCAProvider101"; //$NON-NLS-1$
	public String ENT_ESB_FS_PROVIDER = "ESBFSProvider101"; //$NON-NLS-1$
	public String ENT_ESB_FTP_PROVIDER = "ESBFTPProvider101"; //$NON-NLS-1$
	public String ENT_ESB_SQL_PROVIDER = "ESBSQLProvider101"; //$NON-NLS-1$
	public String ENT_ESB_HIBERNATE_PROVIDER = "ESBHibernateProvider101"; //$NON-NLS-1$

	static String PREACTION_PREFIX = "ESBPreAction";
	
	public String[] PROVIDERS_101 = {
		ENT_ESB_PROVIDER,
		ENT_ESB_JBR_PROVIDER,
		ENT_ESB_SCHEDULE_PROVIDER,
		ENT_ESB_JMS_PROVIDER,
		ENT_ESB_JCA_PROVIDER,
		ENT_ESB_FS_PROVIDER,
		ENT_ESB_FTP_PROVIDER,
		ENT_ESB_SQL_PROVIDER,
		ENT_ESB_HIBERNATE_PROVIDER,
	};

	
	public String ENT_ESB_BUS = "ESBBus"; //$NON-NLS-1$
	public String ENT_ESB_FS_BUS = "ESBFSBus101"; //$NON-NLS-1$
	public String ENT_ESB_FTP_BUS = "ESBFTPBus101"; //$NON-NLS-1$
	public String ENT_ESB_JBR_BUS = "ESBJBRBus101"; //$NON-NLS-1$
	public String ENT_ESB_JMS_BUS = "ESBJMSBus101"; //$NON-NLS-1$
	public String ENT_ESB_HIBERNATE_BUS = "ESBHibernateBus101"; //$NON-NLS-1$
	public String ENT_ESB_SQL_BUS = "ESBSQLBus101"; //$NON-NLS-1$

	public String[] BUSES_101 = {
		ENT_ESB_BUS,
		ENT_ESB_FS_BUS,
		ENT_ESB_FTP_BUS,
		ENT_ESB_JBR_BUS,
		ENT_ESB_JMS_BUS,
		ENT_ESB_HIBERNATE_BUS,
		ENT_ESB_SQL_BUS,
	};		

	public String ENT_ESB_LISTENERS = "ESBListeners101"; //$NON-NLS-1$

	public String ENT_ESB_LISTENER = "ESBListener101"; //$NON-NLS-1$
	public String ENT_ESB_SH_LISTENER = "ESBScheduledListener101"; //$NON-NLS-1$
	public String ENT_ESB_GATEWAY = "ESBJCAGateway101"; //$NON-NLS-1$
	public String ENT_ESB_JMS_LISTENER = "ESBJMSListener101"; //$NON-NLS-1$
	public String ENT_ESB_FS_LISTENER = "ESBFSListener101"; //$NON-NLS-1$
	public String ENT_ESB_FTP_LISTENER = "ESBFTPListener101"; //$NON-NLS-1$
	public String ENT_ESB_SQL_LISTENER = "ESBSQLListener101"; //$NON-NLS-1$
	public String ENT_ESB_HIB_LISTENER = "ESBHibernateListener101"; //$NON-NLS-1$
	public String ENT_ESB_JBR_LISTENER = "ESBJBRListener101"; //$NON-NLS-1$
	public String ENT_ESB_GROOVY_LISTENER = "ESBGroovyListener101"; //$NON-NLS-1$
	
	public String[] LISTENERS_101 = {
		ENT_ESB_LISTENER,
		ENT_ESB_SH_LISTENER,
		ENT_ESB_GATEWAY,
		ENT_ESB_JMS_LISTENER,
		ENT_ESB_FS_LISTENER,
		ENT_ESB_FTP_LISTENER,
		ENT_ESB_SQL_LISTENER,
		ENT_ESB_HIB_LISTENER,
		ENT_ESB_JBR_LISTENER,
		ENT_ESB_GROOVY_LISTENER,
	};

	public String ENT_ESB_SERVICES = "ESBServices101"; //$NON-NLS-1$
	public String ENT_ESB_SERVICE = "ESBService101"; //$NON-NLS-1$
	
	public String ENT_ESB_ACTIONS = "ESBActions101"; //$NON-NLS-1$
	public String ENT_ESB_ACTION = "ESBAction101"; //$NON-NLS-1$

	public String ENT_ESB_SIMPLE_SCHEDULE = "ESBSimpleSchedule101"; //$NON-NLS-1$
	public String ENT_ESB_CRON_SCHEDULE = "ESBCronSchedule101"; //$NON-NLS-1$

	public String ENT_ESB_ROUTE_TO = "ESBPreRouteTo"; //$NON-NLS-1$	

	public String ATTR_NAME = "name"; //$NON-NLS-1$
	public String ATTR_VALUE = "value"; //$NON-NLS-1$
	public String ATTR_PROPERTY_VALUE_PRESENTATION = "value presentation";
	public String ATTR_BUS_ID = "id"; //$NON-NLS-1$
	public String ATTR_BUS_ID_REF = "channel id ref"; //$NON-NLS-1$

}
