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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.common.componentcore.datamodel.FacetInstallDataModelProvider;
import org.jboss.tools.seam.core.SeamCorePlugin;

/**
 * Data model provider for Seam facet wizard page
 * @author eskimo
 *
 */
public class SeamFacetInstallDataModelProvider extends
		FacetInstallDataModelProvider implements ISeamFacetDataModelProperties {
	
	@Override
	public IStatus validate(String name) {
		// TODO Auto-generated method stub
		return super.validate(name);
	}

	public static final Map<String,String[]> SEAM_LIBRARIES= new HashMap<String,String[]>();
	
	static {
		SEAM_LIBRARIES.put("1.2",new String[] {
		});
	}
	/**
	 * Returns set of facet properties for facet wizard page
	 */
	@Override
	public Set getPropertyNames() {
		Set<String> names = super.getPropertyNames();
		
		// General group
		names.add(ISeamFacetDataModelProperties.JBOSS_AS_HOME);
		names.add(ISeamFacetDataModelProperties.JBOSS_SEAM_HOME);
		names.add(ISeamFacetDataModelProperties.JBOSS_AS_DEPLOY_AS);
		
		// Database group
		names.add(ISeamFacetDataModelProperties.DB_TYPE);
		names.add(ISeamFacetDataModelProperties.HIBERNATE_DIALECT);
		names.add(ISeamFacetDataModelProperties.JDBC_DRIVER_CLASS_NAME);
		names.add(ISeamFacetDataModelProperties.JDBC_URL_FOR_DB);

		names.add(ISeamFacetDataModelProperties.DB_USER_NAME);
		names.add(ISeamFacetDataModelProperties.DB_USER_PASSWORD);
		names.add(ISeamFacetDataModelProperties.DB_SCHEMA_NAME);
		names.add(ISeamFacetDataModelProperties.DB_CATALOG_NAME);

		names.add(ISeamFacetDataModelProperties.DB_ALREADY_EXISTS);
		names.add(ISeamFacetDataModelProperties.RECREATE_TABLES_AND_DATA_ON_DEPLOY);

		names.add(ISeamFacetDataModelProperties.JDBC_DRIVER_JAR_PATH);

		// Code generation group
		names.add(ISeamFacetDataModelProperties.SESION_BEAN_PACKAGE_NAME);
		names.add(ISeamFacetDataModelProperties.ENTITY_BEAN_PACKAGE_NAME);
		names.add(ISeamFacetDataModelProperties.TEST_CASES_PACKAGE_NAME);
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
	 */
	public Object getDefaultProperty(String propertyName) {
		if(JBOSS_AS_HOME.equals(propertyName)) {
			return "Jboss_AS_HOME";
		}else if(JBOSS_AS_DEPLOY_AS.equals(propertyName)) {
			return "Jboos_DEPLOY_AS";
		}else if (propertyName.equals(FACET_ID)) {
				return ISeamCoreConstants.SEAM_CORE_FACET_ID;
		}
		return super.getDefaultProperty(propertyName);
	}
	
	public static File getTemplatesFolder() throws IOException {
		return new File(FileLocator.resolve(Platform.getBundle(SeamCorePlugin.PLUGIN_ID).getEntry("/templates")).getPath());
	}
}
