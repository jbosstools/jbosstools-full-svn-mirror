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

package org.jboss.tools.jsf.vpe.jsf.test.jbide;

import org.eclipse.core.resources.IFile;
import org.jboss.tools.vpe.ui.test.TestUtil;
import org.jboss.tools.vpe.ui.test.VpeTest;

/**
 * Test case for 
 * 
 * @author mareshkau
 *
 */
public class JBIDE2297Test extends VpeTest{

	public static final String IMPORT_PROJECT_NAME = "jsfTest"; //$NON-NLS-1$

	private static final String TEST_PAGE_NAME = "JBIDE/2297/JBIDE-2297.xhtml"; //$NON-NLS-1$

	public JBIDE2297Test(String name) {
		super(name);
	}
	
	public void testJBIDE2297() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(TEST_PAGE_NAME,IMPORT_PROJECT_NAME)); 
	}
}
