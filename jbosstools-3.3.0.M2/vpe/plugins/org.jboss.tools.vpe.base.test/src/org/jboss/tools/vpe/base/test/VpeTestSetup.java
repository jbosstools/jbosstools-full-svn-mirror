/******************************************************************************* 
* Copyright (c) 2007-2010 Red Hat, Inc.
* Distributed under license by Red Hat, Inc. All rights reserved.
* This program is made available under the terms of the
* Eclipse Public License v1.0 which accompanies this distribution,
* and is available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*     Red Hat, Inc. - initial API and implementation
******************************************************************************/
package org.jboss.tools.vpe.base.test;

import junit.extensions.TestSetup;
import junit.framework.TestSuite;

/**
 * @author Max Areshkau
 * @author Yahor Radtsevich (yradtsevich)
 * 
 * Class for tear down JUnit tests (remove projects from workspace)
 * 
 */
public class VpeTestSetup extends TestSetup {

	
	public VpeTestSetup(TestSuite test) {
		super(test);
	}

	/* (non-Javadoc)
	 * @see junit.extensions.TestSetup#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
	}

	/* (non-Javadoc)
	 * @see junit.extensions.TestSetup#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		//ProjectsLoader.removeAllProjects();
	}
}
