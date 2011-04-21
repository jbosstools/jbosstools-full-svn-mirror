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
package org.jboss.tools.jsf.vpe.seam.test;

import org.eclipse.core.resources.IFile;
import org.jboss.tools.vpe.ui.test.TestUtil;
import org.jboss.tools.vpe.ui.test.VpeTest;

/**
 * Class for testing all Seam components
 * 
 * @author dsakovich@exadel.com
 * 
 */
public class SeamComponentTest extends VpeTest {

    // import project name
    public static final String IMPORT_PROJECT_NAME = "SeamTest";

    public SeamComponentTest(String name) {
	super(name);
	setCheckWarning(false);
    }

    public void testButton() throws Throwable {
	performTestForVpeComponent((IFile) TestUtil.getComponentPath(
		"components/button.xhtml", IMPORT_PROJECT_NAME)); // $NON-NLS-1$
    }

    public void testDecorate() throws Throwable {
	performTestForVpeComponent((IFile) TestUtil.getComponentPath(
		"components/decorate.xhtml", IMPORT_PROJECT_NAME)); // $NON-NLS-1$
    }

    public void testDiv() throws Throwable {
	performTestForVpeComponent((IFile) TestUtil.getComponentPath(
		"components/div.xhtml", IMPORT_PROJECT_NAME)); // $NON-NLS-1$
    }

    public void testFormattedText() throws Throwable {
	performTestForVpeComponent((IFile) TestUtil.getComponentPath(
		"components/formattedText.xhtml", IMPORT_PROJECT_NAME)); // $NON-NLS-1$
    }

    public void testSpan() throws Throwable {
	performTestForVpeComponent((IFile) TestUtil.getComponentPath(
		"components/span.xhtml", IMPORT_PROJECT_NAME)); // $NON-NLS-1$
    }

    public void testLabel() throws Throwable {
	performTestForVpeComponent((IFile) TestUtil.getComponentPath(
		"components/label.xhtml", IMPORT_PROJECT_NAME)); // $NON-NLS-1$
    }

    public void testLink() throws Throwable {
	performTestForVpeComponent((IFile) TestUtil.getComponentPath(
		"components/link.xhtml", IMPORT_PROJECT_NAME)); // $NON-NLS-1$
    }

    public void testMessage() throws Throwable {
	performTestForVpeComponent((IFile) TestUtil.getComponentPath(
		"components/message.xhtml", IMPORT_PROJECT_NAME)); // $NON-NLS-1$
    }

    public void testAllComponentsOnSinglePage() throws Throwable {
	performTestForVpeComponent((IFile) TestUtil.getComponentPath(
		"components/seamtest.xhtml", IMPORT_PROJECT_NAME)); // $NON-NLS-1$
    }

}
