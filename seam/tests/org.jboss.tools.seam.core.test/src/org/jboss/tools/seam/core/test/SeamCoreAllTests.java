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
package org.jboss.tools.seam.core.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.tools.seam.core.test.project.facet.SeamFacetInstallDelegateTest;
import org.jboss.tools.seam.core.test.project.facet.SeamRuntimeListConverterTest;
import org.jboss.tools.seam.core.test.project.facet.SeamRuntimeManagerTest;
/**
 * @author V.Kabanovich
 *
 */
public class SeamCoreAllTests {
	public static final String PLUGIN_ID = "org.jboss.tools.common.model";
	//
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.setName("All tests for " + PLUGIN_ID);
		suite.addTestSuite(ScannerTest.class);
		suite.addTestSuite(SeamEARTest.class);
		suite.addTestSuite(SeamRuntimeListConverterTest.class);
		suite.addTestSuite(SeamRuntimeManagerTest.class);
		suite.addTestSuite(SeamFacetInstallDelegateTest.class);
		suite.addTest(SeamValidatorsAllTests.suite());

		// Seam 2 tests can't run on hudson for now ;(
		//suite.addTestSuite(Seam2FacetInstallDelegateTest.class);
		
		return suite;
	}
}
