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
package org.jboss.tools.jsf.vpe.jsf.test;

import org.jboss.tools.vpe.ui.test.ComponentContentTest;

/**
 * Class for testing all jsf components
 * 
 * @author sdzmitrovich
 * 
 */
public class JsfComponentContentTest extends ComponentContentTest {

	public JsfComponentContentTest(String name) {
		super(name);
		setCheckWarning(false);
	}

	public void testCommandButton() throws Throwable {
		performContentTest("components/commandButton.jsp"); //$NON-NLS-1$
	}

	public void testCommandLink() throws Throwable {
		performContentTest("components/commandLink.jsp"); //$NON-NLS-1$
	}

	public void testDataTable() throws Throwable {
		performContentTest("components/dataTable.jsp"); //$NON-NLS-1$
	}

	public void testForm() throws Throwable {
		performContentTest("components/form.jsp"); //$NON-NLS-1$
	}

	public void testGraphicImage() throws Throwable {
		performContentTest("components/graphicImage.jsp"); //$NON-NLS-1$
	}

	public void testInputHidden() throws Throwable {
		assertTrue("it is necessery to add body of the test", false); //$NON-NLS-1$
	}

	public void testInputSecret() throws Throwable {
		performContentTest("components/inputSecret.jsp"); //$NON-NLS-1$
	}

	public void testInputText() throws Throwable {
		performContentTest("components/inputText.jsp"); //$NON-NLS-1$

	}

	public void testInputTextArea() throws Throwable {
		performContentTest("components/inputTextArea.jsp"); //$NON-NLS-1$
	}

	public void testMessage() throws Throwable {
		performContentTest("components/message.jsp"); //$NON-NLS-1$
	}

	public void testMessages() throws Throwable {
		performContentTest("components/messages.jsp"); //$NON-NLS-1$
	}

	public void testOutputFormat() throws Throwable {
		performContentTest("components/outputFormat.jsp"); //$NON-NLS-1$
	}

	public void testOutputLabel() throws Throwable {
		performContentTest("components/outputLabel.jsp"); //$NON-NLS-1$
	}

	public void testOutputLink() throws Throwable {
		assertTrue("it is necessery to add body of the test", false); //$NON-NLS-1$
	}

	public void testOutputText() throws Throwable {
		performContentTest("components/outputText.jsp"); //$NON-NLS-1$
	}

	public void testPanelGrid() throws Throwable {
		performContentTest("components/panelGrid.jsp"); //$NON-NLS-1$
	}

	public void testPanelGroup() throws Throwable {
		performContentTest("components/panelGroup.jsp"); //$NON-NLS-1$
	}

	public void testSelectBooleanCheckbox() throws Throwable {
		performContentTest("components/selectBooleanCheckbox.jsp"); //$NON-NLS-1$
	}

	public void testSelectManyCheckbox() throws Throwable {
		performContentTest("components/selectManyCheckbox.jsp"); //$NON-NLS-1$
	}

	public void testSelectManyListbox() throws Throwable {
		performContentTest("components/selectManyListbox.jsp"); //$NON-NLS-1$
	}

	public void testSelectManyMenu() throws Throwable {
		performContentTest("components/selectManyMenu.jsp"); //$NON-NLS-1$
	}

	public void testSelectOneListbox() throws Throwable {
		performContentTest("components/selectOneListbox.jsp"); //$NON-NLS-1$
	}

	public void testSelectOneMenu() throws Throwable {
		performContentTest("components/selectOneMenu.jsp"); //$NON-NLS-1$
	}

	public void testSelectOneRadio() throws Throwable {
		performContentTest("components/selectOneRadio.jsp"); //$NON-NLS-1$
	}

	/*
	 * JSF Core test cases
	 */

	public void testActionListener() throws Throwable {
		assertTrue("it is necessery to add body of the test", false); //$NON-NLS-1$
	}

	public void testAttribute() throws Throwable {
		assertTrue("it is necessery to add body of the test", false); //$NON-NLS-1$
	}

	public void testConvertDateTime() throws Throwable {
		assertTrue("it is necessery to add body of the test", false); //$NON-NLS-1$
	}

	public void testConvertNumber() throws Throwable {
		assertTrue("it is necessery to add body of the test", false); //$NON-NLS-1$
	}

	public void testConverter() throws Throwable {
		assertTrue("it is necessery to add body of the test", false); //$NON-NLS-1$
	}

	public void testFacet() throws Throwable {
		performContentTest("components/facet.jsp"); //$NON-NLS-1$
	}

	public void testLoadBundle() throws Throwable {
		assertTrue("it is necessery to add body of the test", false); //$NON-NLS-1$
	}

	public void testParam() throws Throwable {
		assertTrue("it is necessery to add body of the test", false); //$NON-NLS-1$
	}

	public void testPhaseListener() throws Throwable {
		assertTrue("it is necessery to add body of the test", false); //$NON-NLS-1$
	}

	public void testSelectItem() throws Throwable {
		performContentTest("components/selectItem.jsp"); //$NON-NLS-1$
	}

	public void testSelectItems() throws Throwable {
		performContentTest("components/selectItems.jsp"); //$NON-NLS-1$
	}

	public void testSetPropertyActionListener() throws Throwable {
		assertTrue("it is necessery to add body of the test", false); //$NON-NLS-1$
	}

	public void testSubview() throws Throwable {
		performContentTest("components/subview.jsp"); //$NON-NLS-1$

	}

	public void testValidateDoubleRange() throws Throwable {
		assertTrue("it is necessery to add body of the test", false); //$NON-NLS-1$
	}

	public void testValidateLength() throws Throwable {
		assertTrue("it is necessery to add body of the test", false); //$NON-NLS-1$
	}

	public void testValidateLongRange() throws Throwable {
		assertTrue("it is necessery to add body of the test", false); //$NON-NLS-1$
	}

	public void testValidator() throws Throwable {
		assertTrue("it is necessery to add body of the test", false); //$NON-NLS-1$
	}

	public void testValueChangeListener() throws Throwable {
		assertTrue("it is necessery to add body of the test", false); //$NON-NLS-1$
	}

	public void testVerbatim() throws Throwable {
		performContentTest("components/verbatim.jsp"); //$NON-NLS-1$
	}

	public void testView() throws Throwable {
		performContentTest("components/view.jsp"); //$NON-NLS-1$
	}

	protected String getTestProjectName() {
		return JsfAllTests.IMPORT_PROJECT_NAME;
	}

}
