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

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.common.componentcore.datamodel.FacetInstallDataModelProvider;
import org.eclipse.wst.common.frameworks.datamodel.DataModelPropertyDescriptor;
import org.jboss.tools.seam.core.SeamCorePlugin;

/**
 * Data model provider for Seam facet wizard page
 * 
 * @author eskimo
 * 
 */
public class SeamFacetInstallDataModelProvider extends
		FacetInstallDataModelProvider implements ISeamFacetDataModelProperties {

	private static final String EMPTY_STRING = "";

	/**
	 * Returns set of facet properties for facet wizard page
	 * 
	 * @return set of property names
	 */
	public Set getPropertyNames() {
		Set<String> names = super.getPropertyNames();

		// General group
		names.add(ISeamFacetDataModelProperties.JBOSS_AS_HOME);
		names.add(ISeamFacetDataModelProperties.JBOSS_SEAM_HOME);
		names.add(ISeamFacetDataModelProperties.JBOSS_AS_DEPLOY_AS);
		names.add(ISeamFacetDataModelProperties.JBOSS_AS_TARGET_SERVER);
		names.add(ISeamFacetDataModelProperties.JBOSS_AS_TARGET_RUNTIME);

		// Database group
		names.add(ISeamFacetDataModelProperties.DB_TYPE);
		names.add(ISeamFacetDataModelProperties.HIBERNATE_DIALECT);
		names.add(ISeamFacetDataModelProperties.JDBC_DRIVER_CLASS_NAME);
		names.add(ISeamFacetDataModelProperties.JDBC_URL_FOR_DB);

		names.add(ISeamFacetDataModelProperties.DB_USER_NAME);
		names.add(ISeamFacetDataModelProperties.DB_USER_PASSWORD);
		names.add(ISeamFacetDataModelProperties.DB_SCHEMA_NAME);
		names.add(ISeamFacetDataModelProperties.DB_DEFAULT_SCHEMA_NAME);
		names.add(ISeamFacetDataModelProperties.DB_CATALOG_NAME);
		names.add(ISeamFacetDataModelProperties.DB_DEFAULT_CATALOG_NAME);
		names.add(ISeamFacetDataModelProperties.DB_ALREADY_EXISTS);
		names.add(ISeamFacetDataModelProperties.RECREATE_TABLES_AND_DATA_ON_DEPLOY);

		names.add(ISeamFacetDataModelProperties.JDBC_DRIVER_JAR_PATH);

		// Code generation group
		names.add(ISeamFacetDataModelProperties.SESSION_BEAN_SOURCE_FOLDER);
		names.add(ISeamFacetDataModelProperties.SESSION_BEAN_PACKAGE_NAME);
		names.add(ISeamFacetDataModelProperties.ENTITY_BEAN_SOURCE_FOLDER);
		names.add(ISeamFacetDataModelProperties.ENTITY_BEAN_PACKAGE_NAME);
		names.add(ISeamFacetDataModelProperties.TEST_SOURCE_FOLDER);
		names.add(ISeamFacetDataModelProperties.TEST_CASES_PACKAGE_NAME);
		names.add(ISeamFacetDataModelProperties.TEST_CREATING);
		names.add(ISeamFacetDataModelProperties.WEB_CONTENTS_FOLDER);
		names.add(ISeamFacetDataModelProperties.SEAM_PROJECT_NAME);
		names.add(ISeamFacetDataModelProperties.SEAM_CONNECTION_PROFILE);
		names.add(ISeamFacetDataModelProperties.SEAM_EAR_PROJECT);
		names.add(ISeamFacetDataModelProperties.SEAM_EJB_PROJECT);
		names.add(ISeamFacetDataModelProperties.SEAM_TEST_PROJECT);
		names.add(ISeamFacetDataModelProperties.SEAM_RUNTIME_NAME);
		names.add(ISeamFacetDataModelProperties.HIBERNATE_HBM2DDL_AUTO);

		return names;
	}

	/**
	 * Returns default value for a given property
	 * 
	 * @param propertyName name of property which default value requested
	 * @return default value 
	 */
	public Object getDefaultProperty(String propertyName) {
		if (JBOSS_AS_HOME.equals(propertyName)) {
			return "Jboss_AS_HOME"; //$NON-NLS-1$
		} else if (JBOSS_AS_DEPLOY_AS.equals(propertyName)) {
			return "Jboos_DEPLOY_AS"; //$NON-NLS-1$
		} else if (propertyName.equals(FACET_ID)) {
			return ISeamFacetDataModelProperties.SEAM_FACET_ID;
		} else if (SEAM_TEST_PROJECT.equals(propertyName)) {
			return EMPTY_STRING;
		} else if (SEAM_EJB_PROJECT.equals(propertyName)) {
			return EMPTY_STRING;
		} else if (SEAM_EAR_PROJECT.equals(propertyName)) {
			return EMPTY_STRING;
		} else if (DB_DEFAULT_CATALOG_NAME.equals(propertyName)) {
			return EMPTY_STRING;
		} else if (DB_DEFAULT_SCHEMA_NAME.equals(propertyName)) {
			return EMPTY_STRING;
		} else if(JBOSS_AS_TARGET_RUNTIME.equals(propertyName)) {
			return null;
		}
		return super.getDefaultProperty(propertyName);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.wst.common.componentcore.datamodel.FacetInstallDataModelProvider#getValidPropertyDescriptors(java.lang.String)
	 */
	public DataModelPropertyDescriptor[] getValidPropertyDescriptors(String propertyName) {
		if (ISeamFacetDataModelProperties.JBOSS_AS_TARGET_SERVER.equals(propertyName)) {
			String runtimeName = (String)getProperty(ISeamFacetDataModelProperties.JBOSS_AS_TARGET_RUNTIME);
			if(runtimeName!=null) {
				return SeamFacetProjectCreationDataModelProvider.getServerPropertyDescriptors(runtimeName);
			}
		}
		return super.getValidPropertyDescriptors(propertyName);
	}

	/**
	 * Calculate path to templates folder
	 *  
	 * @return path to templates
	 * @throws IOException if templates folder not found
	 */
	public static File getTemplatesFolder() throws IOException {
		return new File(FileLocator.resolve(
				Platform.getBundle(SeamCorePlugin.PLUGIN_ID).getEntry(
						"/templates")).getPath()); //$NON-NLS-1$
	}
}
