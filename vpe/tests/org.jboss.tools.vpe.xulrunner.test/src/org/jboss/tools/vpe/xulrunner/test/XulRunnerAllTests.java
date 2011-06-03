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
package org.jboss.tools.vpe.xulrunner.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class XulRunnerAllTests {
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for " + XulRunnerAllTests.class.getName());
		suite.addTestSuite(XulRunnerBrowserTest.class);
		suite.addTestSuite(DOMCreatingTest.class);
		suite.addTestSuite(XPCOMTest.class);
		suite.addTestSuite(Nullplugin_JBIDE8792.class);		
		return suite;
	}
}
