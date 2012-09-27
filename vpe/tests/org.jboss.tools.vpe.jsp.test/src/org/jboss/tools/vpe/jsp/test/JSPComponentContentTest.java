/*******************************************************************************
 * Copyright (c) 2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.jsp.test;

import org.jboss.tools.vpe.base.test.ComponentContentTest;

public class JSPComponentContentTest extends ComponentContentTest {

	public JSPComponentContentTest(String name) {
		super(name);
		setCheckWarning(false);
	}

	public void testDeclaration() throws Throwable {
		performInvisibleTagTest("components/declaration.jsp", "declaration"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void testExpression() throws Throwable {
		performInvisibleTagTest("components/expression.jsp", "expression"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void testScriptlet() throws Throwable {
		performInvisibleTagTest("components/scriptlet.jsp", "scriptlet"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void testDirectiveAttribute() throws Throwable {
		performInvisibleTagTestByFullPath("WebContent/WEB-INF/tags/catalog.tag", "DirectiveAttribute"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void testDirectiveInclude() throws Throwable {
		performContentTest("components/directive_include_absolute.jsp"); //$NON-NLS-1$
		performContentTest("components/directive_include_relative.jsp"); //$NON-NLS-1$
	}
	
	public void testInclude() throws Throwable {
		performContentTest("components/include_absolute.jsp"); //$NON-NLS-1$
		performContentTest("components/include_relative.jsp"); //$NON-NLS-1$
	}
	
	public void testDirectivePage() throws Throwable {
		performInvisibleTagTest("components/directive_page.jsp", "directive_page"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void testDirectiveTag() throws Throwable {
		performInvisibleTagTest("components/directive_tag.jsp", "directive_tag"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void testDirectiveTaglib() throws Throwable {
		performInvisibleTagTest("components/directive_taglib.jsp", "directive_taglib"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void testDirectiveVariable() throws Throwable {
		performInvisibleTagTestByFullPath("WebContent/WEB-INF/tags/catalog.tag", "DirectiveVariable"); //$NON-NLS-1$ //$NON-NLS-2$
	}	
	
	public void testAttribute() throws Throwable {
		performInvisibleTagTest("components/attribute.jsp", "attribute"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void testBody() throws Throwable {
		performContentTest("components/body.jsp"); //$NON-NLS-1$
	}
	
	public void testElement() throws Throwable {
		performInvisibleTagTest("components/element.jsp", "element"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public void testDoBody() throws Throwable {
		performInvisibleTagTestByFullPath("WebContent/WEB-INF/tags/double.tag", "DoBody"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void testForward() throws Throwable {
		performInvisibleTagTest("components/forward.jsp", "forward"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void testGetProperty() throws Throwable {
		performInvisibleTagTest("components/get_property.jsp", "get_property"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void testInvoke() throws Throwable {
		performInvisibleTagTestByFullPath("WebContent/WEB-INF/tags/catalog.tag", "Invoke"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void testOutput() throws Throwable {
		performInvisibleTagTest("components/output.jsp", "output"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void testPlugin() throws Throwable {
		performInvisibleTagTest("components/plugin.jsp", "plugin"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void testRoot() throws Throwable {
		performInvisibleTagTest("components/root.jsp", "root"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void testSetProperty() throws Throwable {
		performInvisibleTagTest("components/set_property.jsp", "set_property"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public void testText() throws Throwable {
		performContentTest("components/text.jsp"); //$NON-NLS-1$
	}
	
	public void testUseBean() throws Throwable {
		performInvisibleTagTest("components/useBean.jsp", "useBean"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	@Override
	protected String getTestProjectName() {
		return JSPAllTests.IMPORT_PROJECT_NAME;
	}

}
