/******************************************************************************* 
 * Copyright (c) 2009 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.cdi.ui.test;

import org.eclipse.jdt.internal.core.JavaModelManager;
import org.jboss.tools.cdi.ui.test.wizard.NewCDIWizardTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Alexey Kazakov
 */
public class CDIUIAllTests {

	public static Test suite() {
		// it could be done here because it is not needed to be enabled back
		JavaModelManager.getIndexManager().disable();
		
		TestSuite suite = new TestSuite("CDI UI Tests");
		suite.addTestSuite(CATest.class);
		suite.addTestSuite(NewCDIWizardTest.class);
		return suite;
	}
}