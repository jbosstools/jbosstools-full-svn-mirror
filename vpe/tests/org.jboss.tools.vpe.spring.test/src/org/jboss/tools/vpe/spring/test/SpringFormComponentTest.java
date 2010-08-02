/*******************************************************************************
  * Copyright (c) 2007-2010 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.vpe.spring.test;

import org.eclipse.core.resources.IFile;
import org.jboss.tools.vpe.ui.test.TestUtil;
import org.jboss.tools.vpe.ui.test.VpeTest;

/**
 * Class for testing form spring components
 * 
 * @author Yahor Radtsevich (yradtsevich)
 * 
 */
public class SpringFormComponentTest extends VpeTest {

	// import project name
	static final String IMPORT_PROJECT_NAME = "SpringTest"; //$NON-NLS-1$

	public SpringFormComponentTest(String name) {
		super(name);
	}

	/*
	 * Spring Form test cases
	 */
	
	public void testCheckbox() throws Throwable {
		performTestForVpeComponent((IFile)TestUtil.getResource(
				"src/main/webapp/WEB-INF/jsp/checkbox.jsp", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}
	
	public void testCheckboxes() throws Throwable {
		performTestForVpeComponent((IFile)TestUtil.getResource(
				"src/main/webapp/WEB-INF/jsp/checkboxes.jsp", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}
	
	public void testRadiobutton() throws Throwable {
		performTestForVpeComponent((IFile)TestUtil.getResource(
				"src/main/webapp/WEB-INF/jsp/radiobutton.jsp", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testForm() throws Throwable {
		performTestForVpeComponent((IFile)TestUtil.getResource(
				"src/main/webapp/WEB-INF/jsp/form.jsp", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testErrors() throws Throwable {
		performTestForVpeComponent((IFile)TestUtil.getResource(
				"src/main/webapp/WEB-INF/jsp/errors.jsp", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}
}
