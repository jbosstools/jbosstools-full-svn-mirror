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
package org.jboss.tools.vpe.html.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.tools.vpe.base.test.VpeTestSetup;


/**
 * Class for testing all Html components
 * 
 * @author sdzmitrovich
 * 
 */

public class HtmlAllTests {
	
	// import project name
	public static final String IMPORT_PROJECT_NAME = "htmlTest"; //$NON-NLS-1$
	
	public static Test suite() {

		TestSuite suite = new TestSuite("Tests for Vpe HTML components"); //$NON-NLS-1$
		// $JUnit-BEGIN$
		suite.addTest(HtmlAllImportantTests.suite());
		suite.addTestSuite(HtmlComponentContentTest.class);
		//$JUnit-END$
		return new VpeTestSetup(suite);
	}

}
