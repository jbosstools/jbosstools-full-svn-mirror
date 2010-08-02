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

import org.jboss.tools.vpe.ui.test.VpeTestSetup;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Class for testing all RichFaces components
 * 
 * @author Yahor Radtsevich (yradtsevich)
 * 
 */

public class SpringAllTests {

	/*
	 * Import project name
	 */
	public static final String IMPORT_PROJECT_NAME = "SpringTest"; //$NON-NLS-1$
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Tests for Vpe Spring components"); //$NON-NLS-1$
		// $JUnit-BEGIN$
		suite.addTestSuite(SpringComponentTest.class);
		suite.addTestSuite(SpringComponentContentTest.class);
		//cleanUpTests();
		// $JUnit-END$
		return new VpeTestSetup(suite);
	}
}
