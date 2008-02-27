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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.ui.test.TestUtil;
import org.jboss.tools.vpe.ui.test.VpeTest;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;

/**
 * Test JBIDE-1720
 * 
 * @author Dzmitry Sakovich (dsakovich@exadel.com)
 * 
 */
public class JBIDE1720Test extends VpeTest {

	public static final String IMPORT_PROJECT_NAME = "jsfTest";

	private static final String TEST_PAGE_NAME1 = "JBIDE/1720/JBIDE-1720-selectOneRadio.jsp";
	private static final String TEST_PAGE_NAME2 = "JBIDE/1720/JBIDE-1720-selectOneListBox.jsp";
	private static final String TEST_PAGE_NAME3 = "JBIDE/1720/JBIDE-1720-selectOneMenu.jsp";

	public JBIDE1720Test(String name) {
		super(name);
	}

	// test method for JBIDE 1720 selectOneRadio component
	public void testJBIDE_1720_selectOneRadio() throws Throwable {
		// wait
		TestUtil.waitForJobs();
		// set exception
		setException(null);

		// get test page path
		IFile file = (IFile) TestUtil.getComponentPath(TEST_PAGE_NAME1,
				IMPORT_PROJECT_NAME);

		assertNotNull("Could not open specified file " + file.getFullPath(),
				file);

		IEditorInput input = new FileEditorInput(file);

		assertNotNull("Editor input is null", input);
		// open and get editor
		JSPMultiPageEditor part = openEditor(input);

		// get dom document
		nsIDOMDocument document = getVpeVisualDocument(part);
		nsIDOMElement element = document.getDocumentElement();

		// check that element is not null
		assertNotNull(element);

		// get root node
		nsIDOMNode node = (nsIDOMNode) element
				.queryInterface(nsIDOMNode.NS_IDOMNODE_IID);

		List<nsIDOMNode> elements = new ArrayList<nsIDOMNode>();

		// find "input" elements
		TestUtil.findElementsByName(node, elements, HTML.TAG_INPUT);

		assertEquals(5, elements.size());

		for (int i = 0; i < elements.size(); i++) {

			nsIDOMElement inputElement = (nsIDOMElement) elements.get(i)
					.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
			assertNotNull(inputElement);
			String dir = inputElement.getAttribute(HTML.ATTR_DIR);
			assertNotNull(dir);
			assertEquals("rtl", dir);

			String disabled = inputElement.getAttribute(HTML.ATTR_DISABLED);
			assertNotNull(disabled);
			assertEquals("disabled", disabled);
		}

		elements.clear();

		// find "label" elements
		TestUtil.findElementsByName(node, elements, HTML.TAG_LABEL);

		assertEquals(5, elements.size());

		for (int i = 0; i < elements.size(); i++) {

			nsIDOMElement inputElement = (nsIDOMElement) elements.get(i)
					.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
			assertNotNull(inputElement);
			String style = inputElement.getAttribute(HTML.ATTR_CLASS);
			assertNotNull(style);
			assertEquals("myStyle1", style);

		}

		if (getException() != null) {
			throw getException();
		}
	}

	// test method for JBIDE 1720 selectOneMenu component
	public void testJBIDE_1720_selectOneMenu() throws Throwable {
		// wait
		TestUtil.waitForJobs();
		// set exception
		setException(null);

		// get test page path
		IFile file = (IFile) TestUtil.getComponentPath(TEST_PAGE_NAME3,
				IMPORT_PROJECT_NAME);

		assertNotNull("Could not open specified file " + file.getFullPath(),
				file);

		IEditorInput input = new FileEditorInput(file);

		assertNotNull("Editor input is null", input);
		// open and get editor
		JSPMultiPageEditor part = openEditor(input);

		// get dom document
		nsIDOMDocument document = getVpeVisualDocument(part);
		nsIDOMElement element = document.getDocumentElement();

		// check that element is not null
		assertNotNull(element);

		// get root node
		nsIDOMNode node = (nsIDOMNode) element
				.queryInterface(nsIDOMNode.NS_IDOMNODE_IID);

		List<nsIDOMNode> elements = new ArrayList<nsIDOMNode>();

		// find "select" elements
		TestUtil.findElementsByName(node, elements, HTML.TAG_SELECT);

		assertEquals(1, elements.size());

		nsIDOMElement select = (nsIDOMElement) elements.get(0).queryInterface(
				nsIDOMElement.NS_IDOMELEMENT_IID);

		String size = select.getAttribute(HTML.ATTR_SIZE);
		assertNotNull("Attribute size is not exist.", size);
		assertEquals("1", size);

		String styleClass = select.getAttribute(HTML.ATTR_CLASS);
		assertNotNull("Attribute class is not exist.", styleClass);
		assertEquals("myStyle2", styleClass);

		String style = select.getAttribute(HTML.ATTR_STYLE);
		assertNotNull("Attribute style is not exist.", style);
		assertEquals("font-size: large;", style);

		String dir = select.getAttribute(HTML.ATTR_DIR);
		assertNotNull("Attribute dir is not exist.", dir);
		assertEquals("rtl", dir);

		elements.clear();

		// find "option" elements
		TestUtil.findElementsByName(node, elements, HTML.TAG_OPTION);

		assertEquals(5, elements.size());

		for (int i = 0; i < elements.size(); i++) {

			nsIDOMElement inputElement = (nsIDOMElement) elements.get(i)
					.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
			assertNotNull(inputElement);
			String attr = inputElement.getAttribute(HTML.ATTR_CLASS);
			assertNotNull("Attribute class is not exist in option tag", attr);
			assertEquals("myStyle1", attr);

		}

		if (getException() != null) {
			throw getException();
		}
	}

	// test method for JBIDE 1720 selectOneListBox component
	public void testJBIDE_1720_selectOneListBox() throws Throwable {
		// wait
		TestUtil.waitForJobs();
		// set exception
		setException(null);

		// get test page path
		IFile file = (IFile) TestUtil.getComponentPath(TEST_PAGE_NAME2,
				IMPORT_PROJECT_NAME);

		assertNotNull("Could not open specified file " + file.getFullPath(),
				file);

		IEditorInput input = new FileEditorInput(file);

		assertNotNull("Editor input is null", input);
		// open and get editor
		JSPMultiPageEditor part = openEditor(input);

		// get dom document
		nsIDOMDocument document = getVpeVisualDocument(part);
		nsIDOMElement element = document.getDocumentElement();

		// check that element is not null
		assertNotNull(element);

		// get root node
		nsIDOMNode node = (nsIDOMNode) element
				.queryInterface(nsIDOMNode.NS_IDOMNODE_IID);

		List<nsIDOMNode> elements = new ArrayList<nsIDOMNode>();

		// find "select" elements
		TestUtil.findElementsByName(node, elements, HTML.TAG_SELECT);

		assertEquals(1, elements.size());

		nsIDOMElement select = (nsIDOMElement) elements.get(0).queryInterface(
				nsIDOMElement.NS_IDOMELEMENT_IID);

		String size = select.getAttribute(HTML.ATTR_SIZE);
		assertNotNull("Attribute size is not exist.", size);
		assertEquals("3", size);

		String styleClass = select.getAttribute(HTML.ATTR_CLASS);
		assertNotNull("Attribute class is not exist.", styleClass);
		assertEquals("myStyle2", styleClass);

		String style = select.getAttribute(HTML.ATTR_STYLE);
		assertNotNull("Attribute style is not exist.", style);
		assertEquals("font-size: large;", style);

		String disabled = select.getAttribute(HTML.ATTR_DISABLED);
		assertNotNull("Attribute disabled is not exist.", disabled);
		assertEquals("disabled", disabled);

		elements.clear();

		// find "option" elements
		TestUtil.findElementsByName(node, elements, HTML.TAG_OPTION);

		assertEquals(5, elements.size());

		for (int i = 0; i < elements.size(); i++) {

			nsIDOMElement inputElement = (nsIDOMElement) elements.get(i)
					.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
			assertNotNull(inputElement);
			String attr = inputElement.getAttribute(HTML.ATTR_CLASS);
			assertNotNull("Attribute class is not exist in option tag", attr);
			assertEquals("myStyle", attr);

		}

		if (getException() != null) {
			throw getException();
		}
	}

}
