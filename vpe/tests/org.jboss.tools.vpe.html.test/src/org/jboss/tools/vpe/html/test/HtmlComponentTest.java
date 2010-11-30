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

import org.eclipse.core.resources.IFile;
import org.jboss.tools.vpe.base.test.TestUtil;
import org.jboss.tools.vpe.base.test.VpeTest;

/**
 * Class for testing all html components
 * 
 * @author Sergey Dzmitrovich
 * 
 */
public class HtmlComponentTest extends VpeTest {
	// import project name
	public static final String IMPORT_PROJECT_NAME = "htmlTest"; //$NON-NLS-1$
	
	public HtmlComponentTest(String name) {
		super(name);
		setCheckWarning(false);
	}

	/*
	 * test for block html tags
	 */

	public void testDiv() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/block/div.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testDl() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/block/dl.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testLists() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/block/li.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testSpan() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/block/span.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	/*
	 * test for core html tags
	 */

	public void testA() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/core/a.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testAddress() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/core/address.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testBody() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/core/body.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testImg() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/core/img.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testLink() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/core/link.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testMap() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/core/map.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testObject() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/core/object.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testStyle() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/core/style.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testTitle() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/core/title.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	/*
	 * test for form html tags
	 */

	public void testButton() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/form/button.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testFieldset() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/form/fieldset.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testForm() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/form/form.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testInput() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/form/input.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testLabel() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/form/label.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testSelect() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/form/select.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testTextArea() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/form/textArea.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	/*
	 * test for frames html tags
	 */

	public void testFrameset() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/frames/frameset.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testIframe() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/frames/iframe.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	/*
	 * test for scripts html tags
	 */

	public void testScript() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/scripts/script.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	/*
	 * test for table html tags
	 */

	public void testComplexTable() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/table/complex_table.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testTable() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/table/table.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	/*
	 * test for text html tags
	 */

	public void testAbbr() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/text/abbr.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testAcronym() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/text/acronym.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testB() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/text/b.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testBig() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/text/big.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testBlockquote() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/text/blockquote.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testBr() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/text/br.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testCite() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/text/cite.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testCode() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/text/code.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testDel() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/text/del.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testDfn() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/text/dfn.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testEm() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/text/em.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testHr() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/text/hr.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testI() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/text/i.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testIns() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/text/ins.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testKbd() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/text/kbd.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testP() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/text/p.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testPre() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/text/pre.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testQ() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/text/q.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testSamp() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/text/samp.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testSmall() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/text/small.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testStrong() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/text/strong.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testSub() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/text/sub.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testSup() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/text/sup.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testTt() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/text/tt.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

	public void testVar() throws Throwable {
		performTestForVpeComponent((IFile) TestUtil.getComponentPath(
				"components/text/var.html", IMPORT_PROJECT_NAME)); //$NON-NLS-1$
	}

}
