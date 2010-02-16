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
package org.jboss.tools.vpe.ui.test;

import java.io.File;
import java.util.List;

import junit.extensions.TestSetup;
import junit.framework.TestSuite;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.test.util.ResourcesUtils;
import org.jboss.tools.tests.ImportBean;

/**
 * @author Max Areshkau
 * 
 * Class for setup-tear down junit tests(import project 
 * into workspace and remove project from workspace)
 */
public class VpeTestSetup extends TestSetup {
	
	/**
	 * Contains test project names, which will be imported 
	 * in setUp method and removed in tear down method
	 */
	private List<ImportBean> testProjectNames;
	
	private static final String CONTENT_OUTLINE_VIEW_ID = "org.eclipse.ui.views.ContentOutline"; //$NON-NLS-1$
	
	public VpeTestSetup(TestSuite test, List<ImportBean> testProjectNames) {
		super(test);
		setTestProjects(testProjectNames);
	}

	/* (non-Javadoc)
	 * @see junit.extensions.TestSetup#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		for (ImportBean importBean : getTestProjects()) {			
			if (ResourcesPlugin.getWorkspace().getRoot().findMember(importBean.getImportProjectName()) == null) {
				ResourcesUtils.importProjectIntoWorkspace((importBean.getImportProjectPath()
						+ File.separator+importBean.getImportProjectName()),importBean.getImportProjectName());
			}		
		}
		//added by Maksim Areshkau, Fix for https://jira.jboss.org/jira/browse/JBIDE-5820 https://jira.jboss.org/jira/browse/JBIDE-5821
        //remove this code when we will move on wtp 3.2
		IViewReference[] iviewReferences= PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences();
        for (IViewReference iViewReference : iviewReferences) {
        	if(VpeTestSetup.CONTENT_OUTLINE_VIEW_ID.equalsIgnoreCase(iViewReference.getId())){
        		 PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().hideView(iViewReference);
        	}
		}
	}

	/* (non-Javadoc)
	 * @see junit.extensions.TestSetup#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		
		for(ImportBean importBean:getTestProjects()) {
			TestUtil.removeProject(importBean.getImportProjectName());
		}
		super.tearDown();
	}

	/**
	 * @return the testProjectNames
	 */
	private List<ImportBean> getTestProjects() {
		return testProjectNames;
	}

	/**
	 * @param testProjectNames the testProjectNames to set
	 */
	private void setTestProjects(List<ImportBean> testProjectNames) {
		this.testProjectNames = testProjectNames;
	}

}
