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
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.common.componentcore.datamodel.FacetInstallDataModelProvider;
import org.eclipse.wst.common.componentcore.datamodel.properties.IFacetDataModelProperties;
import org.eclipse.wst.common.frameworks.datamodel.DataModelPropertyDescriptor;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.jboss.tools.common.zip.UnzipOperation;
import org.jboss.tools.seam.core.SeamCorePlugin;
import org.jboss.tools.seam.core.project.facet.SeamProjectPreferences;
import org.jboss.tools.seam.core.project.facet.SeamRuntime;
import org.jboss.tools.seam.core.project.facet.SeamRuntimeManager;
import org.jboss.tools.seam.core.project.facet.SeamVersion;
import org.osgi.framework.Bundle;

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
		Set<String> names = super.getPropertyNames();//super.getPropertyNames();

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
		names.add(ISeamFacetDataModelProperties.TEST_PROJECT_CREATING);
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
		names.add(ISeamFacetDataModelProperties.CREATE_EAR_PROJECTS);
		names.add(CONFIGURE_DEFAULT_SEAM_RUNTIME);
		names.add(CONFIGURE_WAR_PROJECT);
		names.add(ISeamFacetDataModelProperties.SEAM_RUNTIME_LIBRARIES_COPYING);
		names.add(ISeamFacetDataModelProperties.SEAM_TEMPLATES_AND_LIBRARIES_COPYING);
		names.add(ISeamFacetDataModelProperties.SEAM_LIBRARY_PROVIDER);
		
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
//			return "Jboss_AS_HOME"; //$NON-NLS-1$
			return null;
		} else if (JBOSS_AS_DEPLOY_AS.equals(propertyName)) {
//			return "Jboos_DEPLOY_AS"; //$NON-NLS-1$
			return null;
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
		else if(JBOSS_AS_TARGET_RUNTIME.equals(propertyName)) {
			return null;
		} else if (CONFIGURE_DEFAULT_SEAM_RUNTIME.equals(propertyName)) {
			return Boolean.TRUE;
		} else if (CONFIGURE_WAR_PROJECT.equals(propertyName)) {
			return Boolean.TRUE;
		} else if (SEAM_RUNTIME_LIBRARIES_COPYING.equals(propertyName)) {
			return Boolean.TRUE;
		} else if (SEAM_TEMPLATES_AND_LIBRARIES_COPYING.equals(propertyName)) {
			return Boolean.TRUE;
		} else if (SEAM_LIBRARY_PROVIDER.equals(propertyName)) {
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
		Bundle bundle = SeamCorePlugin.getDefault().getBundle();
		String version = bundle.getVersion().toString();
		IPath stateLocation = Platform.getStateLocation(bundle);
		File templatesDir = FileLocator.getBundleFile(bundle);
		if(templatesDir.isFile()) {
			File toCopy = new File(stateLocation.toFile(),version);
			if(!toCopy.exists()) {
				toCopy.mkdirs();
				UnzipOperation unZip = new UnzipOperation(templatesDir.getAbsolutePath());
				unZip.execute(toCopy,"templates.*");
			}
			templatesDir = toCopy;
		}
		return new File(templatesDir,"templates");

	}

	/**
	 * Returns default seam runtime name.
	 * @param seamModel
	 * @return
	 */
	public static String getSeamRuntimeDefaultValue(IDataModel seamModel) {
		String seamFacetVersion = seamModel.getProperty(IFacetDataModelProperties.FACET_VERSION_STR).toString();
		SeamVersion seamVersion = SeamVersion.parseFromString(seamFacetVersion); 

		SeamRuntime defaultRuntime = SeamRuntimeManager.getInstance().getDefaultRuntime(seamVersion);
		if(defaultRuntime==null) {
			return "";
		}
		return defaultRuntime.getName();
	}

	public boolean propertySet(String propertyName, Object propertyValue) {
		if (IFacetDataModelProperties.FACET_PROJECT_NAME.equals(propertyName)) {
			setProperty(ISeamFacetDataModelProperties.SEAM_PROJECT_NAME, propertyValue);
			setProperty(IFacetDataModelProperties.FACET_PROJECT_NAME, propertyValue);
		}
		return super.propertySet(propertyName, propertyValue);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelProvider#validate(java.lang.String)
	 */
	@Override
    public IStatus validate(String name) {
		IStatus status = OK_STATUS;
		Map<String, IStatus> errors = null;
		if(name.equals(SEAM_RUNTIME_NAME)) {
			String seamRuntimeName = getStringProperty(SEAM_RUNTIME_NAME);
			errors = SeamValidatorFactory.SEAM_RUNTIME_NAME_VALIDATOR.validate(seamRuntimeName, null);
		} else if(name.equals(SEAM_CONNECTION_PROFILE)) {
			String connectionName = getStringProperty(SEAM_CONNECTION_PROFILE);
			errors = SeamValidatorFactory.CONNECTION_PROFILE_VALIDATOR.validate(connectionName, null);
		} else if(name.equals(ENTITY_BEAN_PACKAGE_NAME)) {
			String packageName = getStringProperty(ENTITY_BEAN_PACKAGE_NAME);
			errors = SeamValidatorFactory.PACKAGE_NAME_VALIDATOR.validate(packageName, null);
		} else if(name.equals(SESSION_BEAN_PACKAGE_NAME)) {
			String packageName = getStringProperty(SESSION_BEAN_PACKAGE_NAME);
			errors = SeamValidatorFactory.PACKAGE_NAME_VALIDATOR.validate(packageName, null);
		}
		if(errors!=null && !errors.isEmpty()) {
			status = errors.values().iterator().next();
		}
    	return status;
    }

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelProvider#init()
	 */
	@Override
	public void init() {
		super.init();

		model.setProperty(ISeamFacetDataModelProperties.DB_TYPE, SeamProjectPreferences.getStringPreference(SeamProjectPreferences.HIBERNATE_DEFAULT_DB_TYPE));
		model.setProperty(ISeamFacetDataModelProperties.SEAM_CONNECTION_PROFILE, SeamProjectPreferences.getStringPreference(SeamProjectPreferences.SEAM_DEFAULT_CONNECTION_PROFILE));
		model.setProperty(ISeamFacetDataModelProperties.DB_DEFAULT_SCHEMA_NAME, "");
		model.setProperty(ISeamFacetDataModelProperties.DB_DEFAULT_CATALOG_NAME, "");
		model.setProperty(ISeamFacetDataModelProperties.DB_ALREADY_EXISTS, false);
		model.setProperty(ISeamFacetDataModelProperties.RECREATE_TABLES_AND_DATA_ON_DEPLOY, false);
//		SESSION_BEAN_PACKAGE_NAME
//		ENTITY_BEAN_PACKAGE_NAME
//		TEST_CASES_PACKAGE_NAME
//		jBossSeamHome
//		jBossAsDeployAs
	}
}