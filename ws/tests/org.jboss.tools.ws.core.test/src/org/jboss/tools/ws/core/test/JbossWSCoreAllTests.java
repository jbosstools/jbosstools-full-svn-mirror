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
package org.jboss.tools.ws.core.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jboss.tools.ws.core.test.classpath.JbossWSRuntimeManagerTest;
import org.jboss.tools.ws.core.test.command.JavaFirstCommandTest;
import org.jboss.tools.ws.core.test.command.WSClientCommandTest;

public class JbossWSCoreAllTests extends TestCase {
	public static final String PLUGIN_ID = "org.jboss.tools.common.test";
	public static Test suite ()
	{
		TestSuite suite = new TestSuite(JbossWSCoreAllTests.class.getName());
		suite.addTestSuite(JbossWSRuntimeManagerTest.class);
		suite.addTestSuite(JavaFirstCommandTest.class);
		suite.addTestSuite(WSClientCommandTest.class);
	
		return suite;
	}
}