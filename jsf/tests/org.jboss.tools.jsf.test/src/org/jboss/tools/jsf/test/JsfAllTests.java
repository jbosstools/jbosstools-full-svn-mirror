/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jsf.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class JsfAllTests {
//	public static final String PLUGIN_ID = "org.jboss.tools.jsf";

	public static Test suite() {
		TestSuite suite = new TestSuite("Test model loading for JSF projects");
		
		suite.addTestSuite(JSFModelTest.class);
		suite.addTestSuite(ModelFormat_2_0_0_Test.class);
		
		return suite;
	}

}
