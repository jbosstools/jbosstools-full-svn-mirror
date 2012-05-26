/******************************************************************************* 
* Copyright (c) 2007-2010 Red Hat, Inc.
* Distributed under license by Red Hat, Inc. All rights reserved.
* This program is made available under the terms of the
* Eclipse Public License v1.0 which accompanies this distribution,
* and is available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*     Red Hat, Inc. - initial API and implementation
******************************************************************************/
package org.jboss.tools.vpe.ui.test;

import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;

import junit.extensions.TestSetup;
import junit.framework.TestSuite;

/**
 * @author Max Areshkau
 * @author Yahor Radtsevich (yradtsevich)
 * 
 * Class for tear down JUnit tests (remove projects from workspace)
 * 
 */
public class VpeTestSetup extends TestSetup {
	private static final String CONTENT_OUTLINE_VIEW_ID = "org.eclipse.ui.views.ContentOutline";
	
	public VpeTestSetup(TestSuite test) {
		super(test);
	}

	/* (non-Javadoc)
	 * @see junit.extensions.TestSetup#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
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
		ProjectsLoader.removeAllProjects();
	}
}
