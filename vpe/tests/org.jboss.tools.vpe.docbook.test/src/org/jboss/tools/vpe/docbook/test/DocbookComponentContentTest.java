/*******************************************************************************
 * Copyright (c) 2007-2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.docbook.test;

import org.jboss.tools.vpe.ui.test.ComponentContentTest;

/**
 * Tests for the context that was generated by Docbook templates
 * 
 * @author Denis Vinnichek (dvinnichek)
 */
public class DocbookComponentContentTest extends ComponentContentTest {
	
	private static final String DOC_BOOK_EDITOR_ID = "org.jboss.tools.jst.jsp.jspeditor.DocBookEditor"; //$NON-NLS-1$

	/**
	 * The Constructor
	 * 
	 * @param name
	 *            a test case name
	 */
	public DocbookComponentContentTest(String name) {
		super(name);
		setCheckWarning(false);
	}

	public void testXref() throws Throwable {
		performContentTest("components/xref/xref.xml"); //$NON-NLS-1$
	}

	public void testLiteral() throws Throwable {
		performContentTest("components/literal/literal.xml"); //$NON-NLS-1$
	}

	@Override
	protected String getTestProjectName() {
		return DocbookAllTests.IMPORT_PROJECT_NAME;
	}

	@Override
	protected String getEditorID() {
		return DOC_BOOK_EDITOR_ID;
	}

}
