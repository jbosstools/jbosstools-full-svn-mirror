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

import org.jboss.tools.vpe.html.test.jbide.JBIDE3280Test;
import org.jboss.tools.vpe.ui.test.VpeTestSetup;


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
		suite.addTestSuite(JBIDE3280Test.class);
		suite.addTestSuite(HtmlComponentTest.class);
		suite.addTestSuite(HtmlComponentContentTest.class);

		return new VpeTestSetup(suite);
	}

}
