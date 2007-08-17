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

package org.jboss.tools.seam.internal.core.project.facet;

import org.eclipse.wst.common.project.facet.core.IActionConfigFactory;

/**
 * Seam facet properties
 * @author eskimo
 *
 */
public interface ISeamFacetDataModelProperties extends IActionConfigFactory {
	
	public static final String SEAM_PROJECT_NAME = "project.name";
	
	public static final String SEAM_PROJECT_INSTANCE =  "seam.project.instance";
	
	public static final String SEAM_CONNECTION_PROFILE = "seam.project.connection.profile";
	
	public static final String JBOSS_AS_HOME = "jboss.home";
	
	public static final String JBOSS_AS_DEPLOY_AS = "seam.project.deployment.type";
	
	public static final String DB_TYPE = "database.type";
	
	public static final String HIBERNATE_DIALECT = "hibernate.dialect";
	
	public static final String JDBC_DRIVER_CLASS_NAME = "hibernate.connection.driver_class";
	
	public static final String JDBC_URL_FOR_DB = "hibernate.connection.url";
	
	public static final String DB_USER_NAME = "hibernate.connection.username";
	
	public static final String DB_USERP_PASSWORD = "hibernate.connection.password";
	
	public static final String DB_SCHEMA_NAME = "schema.property";
	
	public static final String DB_CATALOG_NAME  = "catalog.property";
	
	public static final String DB_ALREADY_EXISTS = "database.exists";
	
	public static final String RECREATE_TABLES_AND_DATA_ON_DEPLOY = "database.drop";
	
	public static final String JDBC_DRIVER_JAR_PATH = "driver.file";
	
	public static final String SESION_BEAN_PACKAGE_NAME = "action.package";
	
	public static final String SESION_BEAN_PACKAGE_PATH = "action.package.path";
	
	public static final String ENTITY_BEAN_PACKAGE_NAME = "model.package";
	
	public static final String ENTITY_BEAN_PACKAGE_PATH = "model.package.path";
	
	public static final String TEST_CASES_PACKAGE_NAME = "test.package";
	
	public static final String TEST_CASES_PACKAGE_PATH = "test.package.path";

	public static final String JBOSS_SEAM_HOME = "seam.jbossas.home.folder";
	
	public static final String WEB_CONTENTS_FOLDER = "seam.project.web.root.folder";
	
}
