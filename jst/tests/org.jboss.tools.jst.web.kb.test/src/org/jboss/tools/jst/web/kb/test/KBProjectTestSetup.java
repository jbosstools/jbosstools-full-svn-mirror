/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.jst.web.kb.test;

import junit.framework.Test;

import org.eclipse.core.resources.IProject;
import org.jboss.tools.common.model.util.test.XProjectImportTestSetUp;
import org.jboss.tools.jst.jsp.test.TestUtil;

/**
 * @author Alexey Kazakov
 */
public class KBProjectTestSetup extends XProjectImportTestSetUp {

	public KBProjectTestSetup(Test test, String bundleName, String projectPath, String projectName) {
		super(test,bundleName, projectPath, projectName);
	}

	public KBProjectTestSetup(Test test, String bundleName, String[] projectPaths, String[] projectNames) {
		super(test, bundleName, projectPaths, projectNames);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.common.model.util.test.XProjectImportTestSetUp#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		for (IProject project : projects) {
			TestUtil._waitForValidation(project);
		}
	}
}