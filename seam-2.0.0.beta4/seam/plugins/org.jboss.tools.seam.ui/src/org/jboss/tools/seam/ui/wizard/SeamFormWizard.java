/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.seam.ui.wizard;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.INewWizard;
import org.jboss.tools.seam.internal.core.project.facet.ISeamFacetDataModelProperties;

/**
 * 
 * @author eskimo
 *
 */
public class SeamFormWizard extends SeamBaseWizard implements INewWizard {

	/**
	 * 
	 */
	public SeamFormWizard() {
		super(CREATE_SEAM_FORM);
		setWindowTitle("New Seam Form");
		setDefaultPageImageDescriptor(ImageDescriptor.createFromFile(SeamActionWizard.class, "SeamFormWizBan.png"));
		addPage(new SeamFormWizardPage1());
	}

	private static IUndoableOperation CREATE_SEAM_FORM = new SeamFormCreateOperation();
	
	public static class SeamFormCreateOperation extends SeamBaseOperation {
		
		public SeamFormCreateOperation() {
			super("Form creating operation");
		}
		
		public File getBeanFile(Map<String, Object> vars)  {
			return new File(getSeamFolder(vars),"src/FormActionJavaBean.java");
		}
		
		public File getTestClassFile(Map<String, Object> vars) {
			return new File(getSeamFolder(vars),"test/FormTest.java");
		}
		
		public File getTestngXmlFile(Map<String, Object> vars) {
			return new File(getSeamFolder(vars),"test/testng.xml");
		}
		
		public File getPageXhtml(Map<String, Object> vars) {
			return new File(getSeamFolder(vars),"view/form.xhtml");
		}

		/* (non-Javadoc)
		 * @see org.jboss.tools.seam.ui.wizard.SeamBaseOperation#getFileMappings(java.util.Map)
		 */
		@Override
		public List<String[]> getFileMappings(Map<String, Object> vars) {
			if("war".equals(vars.get(ISeamFacetDataModelProperties.JBOSS_AS_DEPLOY_AS)))
				return FORM_WAR_MAPPING;
			else
				return FORM_EAR_MAPPING;
		}
		
		public static final List<String[]> FORM_WAR_MAPPING = new ArrayList<String[]>();
		
		public static final List<String[]> FORM_EAR_MAPPING = new ArrayList<String[]>();
		
		static {
			FORM_WAR_MAPPING.add(new String[]{
					"${" + ISeamFacetDataModelProperties.JBOSS_SEAM_HOME + "}/seam-gen/src/FormActionJavaBean.java",
					"${" + IParameter.SEAM_PROJECT_LOCATION_PATH + "}/src/model/${" + ISeamFacetDataModelProperties.SESION_BEAN_PACKAGE_PATH + "}/${" + IParameter.SEAM_LOCAL_INTERFACE_NAME +"}.java"});
			FORM_WAR_MAPPING.add(new String[]{
					"${" + ISeamFacetDataModelProperties.JBOSS_SEAM_HOME + "}/seam-gen/test/FormTest.java",
					"${" + IParameter.SEAM_TEST_PROJECT_LOCATION_PATH + "}/test-src/${" + ISeamFacetDataModelProperties.TEST_CASES_PACKAGE_PATH + "}/${"+ IParameter.SEAM_LOCAL_INTERFACE_NAME +"}Test.java"});
			FORM_WAR_MAPPING.add(new String[]{
					"${" + ISeamFacetDataModelProperties.JBOSS_SEAM_HOME + "}/seam-gen/test/testng.xml",
					"${" + IParameter.SEAM_TEST_PROJECT_LOCATION_PATH + "}/test-src/${" + ISeamFacetDataModelProperties.TEST_CASES_PACKAGE_PATH + "}/${"+ IParameter.SEAM_LOCAL_INTERFACE_NAME +"}Test.xml"});
			FORM_WAR_MAPPING.add(new String[]{
					"${" + ISeamFacetDataModelProperties.JBOSS_SEAM_HOME + "}/seam-gen/view/form.xhtml",
					"${" + IParameter.SEAM_PROJECT_WEBCONTENT_PATH + "}/${" + IParameter.SEAM_PAGE_NAME +"}.xhtml"});	
		
			// initialize ear files mapping
			FORM_EAR_MAPPING.add(new String[]{
					"${" + ISeamFacetDataModelProperties.JBOSS_SEAM_HOME + "}/seam-gen/src/FormActionBean.java",
					"${" + IParameter.SEAM_EJB_PROJECT_LOCATION_PATH + "}/ejbModule/${" + ISeamFacetDataModelProperties.SESION_BEAN_PACKAGE_PATH + "}/${" + IParameter.SEAM_BEAN_NAME +"}.java"});
			FORM_EAR_MAPPING.add(new String[]{
					"${" + ISeamFacetDataModelProperties.JBOSS_SEAM_HOME + "}/seam-gen/src/FormAction.java",
					"${" + IParameter.SEAM_EJB_PROJECT_LOCATION_PATH + "}/ejbModule/${" + ISeamFacetDataModelProperties.SESION_BEAN_PACKAGE_PATH + "}/${" + IParameter.SEAM_LOCAL_INTERFACE_NAME +"}.java"});
			FORM_EAR_MAPPING.add(new String[]{
					"${" + ISeamFacetDataModelProperties.JBOSS_SEAM_HOME + "}/seam-gen/test/FormTest.java",
					"${" + IParameter.SEAM_TEST_PROJECT_LOCATION_PATH + "}/test-src/${" + ISeamFacetDataModelProperties.TEST_CASES_PACKAGE_PATH + "}/${"+ IParameter.SEAM_LOCAL_INTERFACE_NAME +"}Test.java"});
			FORM_EAR_MAPPING.add(new String[]{
					"${" + ISeamFacetDataModelProperties.JBOSS_SEAM_HOME + "}/seam-gen/test/testng.xml",
					"${" + IParameter.SEAM_TEST_PROJECT_LOCATION_PATH + "}/test-src/${" + ISeamFacetDataModelProperties.TEST_CASES_PACKAGE_PATH + "}/${"+ IParameter.SEAM_LOCAL_INTERFACE_NAME +"}Test.xml"});
			FORM_EAR_MAPPING.add(FORM_WAR_MAPPING.get(3));
		}
	};
}
