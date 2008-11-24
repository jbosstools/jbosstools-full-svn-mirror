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

import org.jboss.tools.vpe.ui.test.ComponentContentTest;

/**
 * Class for testing all jsf components
 * 
 * @author sdzmitrovich
 * 
 */
public class HtmlComponentContentTest extends ComponentContentTest {

	public HtmlComponentContentTest(String name) {
		super(name);
		setCheckWarning(false);
	}

	/*
	 * test for text html tags
	 */

	public void testAbbr() throws Throwable {
		performContentTest("components/text/abbr.html"); //$NON-NLS-1$
	}

	public void testAcronym() throws Throwable {
		performContentTest("components/text/acronym.html"); //$NON-NLS-1$
	}

	public void testB() throws Throwable {
		performContentTest("components/text/b.html"); //$NON-NLS-1$
	}

	public void testBig() throws Throwable {
		performContentTest("components/text/big.html"); //$NON-NLS-1$
	}

	public void testBlockquote() throws Throwable {
		performContentTest("components/text/blockquote.html"); //$NON-NLS-1$
	}

	public void testBr() throws Throwable {
		performContentTest("components/text/br.html"); //$NON-NLS-1$
	}

	public void testCite() throws Throwable {
		performContentTest("components/text/cite.html"); //$NON-NLS-1$
	}

	public void testCode() throws Throwable {
		performContentTest("components/text/code.html"); //$NON-NLS-1$
	}

	public void testDel() throws Throwable {
		performContentTest("components/text/del.html"); //$NON-NLS-1$
	}

	public void testDfn() throws Throwable {
		performContentTest("components/text/dfn.html"); //$NON-NLS-1$
	}

	public void testEm() throws Throwable {
		performContentTest("components/text/em.html"); //$NON-NLS-1$
	}

	public void testHr() throws Throwable {
		performContentTest("components/text/hr.html"); //$NON-NLS-1$
	}

	public void testI() throws Throwable {
		performContentTest("components/text/i.html"); //$NON-NLS-1$
	}

	public void testIns() throws Throwable {
		performContentTest("components/text/ins.html"); //$NON-NLS-1$
	}

	public void testKbd() throws Throwable {
		performContentTest("components/text/kbd.html"); //$NON-NLS-1$
	}

	public void testP() throws Throwable {
		performContentTest("components/text/p.html"); //$NON-NLS-1$
	}

	public void testPre() throws Throwable {
		performContentTest("components/text/pre.html"); //$NON-NLS-1$
	}

	public void testQ() throws Throwable {
		performContentTest("components/text/q.html"); //$NON-NLS-1$
	}

	public void testSamp() throws Throwable {
		performContentTest("components/text/samp.html"); //$NON-NLS-1$
	}

	public void testSmall() throws Throwable {
		performContentTest("components/text/small.html"); //$NON-NLS-1$
	}

	public void testStrong() throws Throwable {
		performContentTest("components/text/strong.html"); //$NON-NLS-1$
	}

	public void testSub() throws Throwable {
		performContentTest("components/text/sub.html"); //$NON-NLS-1$
	}

	public void testSup() throws Throwable {
		performContentTest("components/text/sup.html"); //$NON-NLS-1$
	}

	public void testTt() throws Throwable {
		performContentTest("components/text/tt.html"); //$NON-NLS-1$
	}

	public void testVar() throws Throwable {
		performContentTest("components/text/var.html"); //$NON-NLS-1$
	}

	protected String getTestProjectName() {
		return HtmlAllTests.IMPORT_PROJECT_NAME;
	}

}
