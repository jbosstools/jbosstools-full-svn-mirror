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
package org.jboss.tools.cdi.seam.solder.core.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.jdt.internal.core.JavaModelManager;
import org.eclipse.wst.validation.ValidationFramework;
import org.jboss.tools.cdi.core.test.tck.validation.ValidationExceptionTest;
/**
 * @author Viacheslav Kabanovich
 */
public class CDISeamSolderCoreAllTests {

	public static Test suite() {
		// it could be done here because it is not needed to be enabled back
		JavaModelManager.getIndexManager().disable();

		ValidationFramework.getDefault().suspendAllValidation(true);

		ValidationExceptionTest.initLogger();

		TestSuite suiteAll = new TestSuite("CDI Solder Core Tests");
		SeamSolderTestSetup suite = new SeamSolderTestSetup(suiteAll);

		suiteAll.addTestSuite(GenericBeanTest.class);
		suiteAll.addTestSuite(GenericBeanValidationTest.class);
		suiteAll.addTestSuite(BeanNamingTest.class);
		suiteAll.addTestSuite(VetoTest.class);
		suiteAll.addTestSuite(ExactTest.class);
		suiteAll.addTestSuite(MessageLoggerTest.class);
		suiteAll.addTestSuite(ServiceHandlerTest.class);
		suiteAll.addTestSuite(DefaultBeanTest.class);
		suiteAll.addTestSuite(UnwrapsTest.class);

		suiteAll.addTestSuite(ValidationExceptionTest.class); // This test should be added last!

		return suite;
	}
}