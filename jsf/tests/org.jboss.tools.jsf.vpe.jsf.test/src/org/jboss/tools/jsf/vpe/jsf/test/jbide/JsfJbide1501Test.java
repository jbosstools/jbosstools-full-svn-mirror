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
import org.jboss.tools.jsf.vpe.jsf.test.JsfTestPlugin;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.ui.test.TestUtil;
import org.jboss.tools.vpe.ui.test.VpeTest;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNamedNodeMap;
import org.mozilla.interfaces.nsIDOMNode;

/**
 * Class for testing all jsf bugs
 * 
 * @author sdzmitrovich
 * 
 */
public class JsfJbide1501Test extends VpeTest {

	private static final String IMPORT_PROJECT_NAME = "jsfTest";

	public JsfJbide1501Test(String name) {
		super(name, IMPORT_PROJECT_NAME, JsfTestPlugin.getPluginResourcePath());
	}

	/**
	 * test for http://jira.jboss.com/jira/browse/JBIDE-1467
	 * 
	 * the cause of bug :
	 * 
	 * 1. <select ... > tag ( which vpe template forms when process
	 * "selectManyMenu" and "selectManyListbox" jsf tags ) didn't have
	 * multiple="multiple" attribute
	 * 
	 * 2. <select ... > tag ( which vpe template forms when process
	 * "selectOneListbox" and "selectManyListbox" jsf tags ) sometimes had
	 * incorrect "size" attribute
	 * 
	 * DISCRIPTION: test consist of two part
	 * 
	 * 1. The first page (JBIDE-1501_multiple.jsp) has "selectManyMenu" and
	 * "selectManyListbox" jsf tags. And test checks that all "select" elements
	 * have "multiple" attribute
	 * 
	 * 2. The second page (JBIDE-1501_size.jsp) has "selectOneListbox" and
	 * "selectManyListbox" jsf tags and they was formed so that all "select"
	 * elements must have size="2" attribute . And test checks "size" attribute
	 */
	public void testJbide() throws Throwable {

		// wait
		TestUtil.waitForJobs();
		// set exception
		setException(null);

		// _____1st Part____//

		// get test page path
		IFile file = (IFile) TestUtil.getComponentPath(
				"JBIDE/1501/JBIDE-1501_multiple.jsp", getImportProjectName());

		IEditorInput input = new FileEditorInput(file);

		// open and get editor
		JSPMultiPageEditor part = openEditor(input);

		// get dom document
		nsIDOMDocument document = getVpeVisualDocument(part);
		assertNotNull(document);

		// get dom element
		nsIDOMElement element = document.getDocumentElement();
		assertNotNull(element);

		// get root node
		nsIDOMNode node = (nsIDOMNode) element
				.queryInterface(nsIDOMNode.NS_IDOMNODE_IID);

		List<nsIDOMNode> elements = new ArrayList<nsIDOMNode>();

		// find "select" elements
		TestUtil.findElementsByName(node, elements, HTML.TAG_SELECT);

		for (nsIDOMNode select : elements) {

			// get attributes
			nsIDOMNamedNodeMap attributes = select.getAttributes();

			// "select" must have attributes
			assertNotNull(attributes);

			// select must have "multiple" attribute
			assertNotNull(attributes.getNamedItem(HTML.ATTR_MULTIPLE));

		}

		// _____2nd Part____//

		// get test page path
		file = (IFile) TestUtil.getComponentPath(
				"JBIDE/1501/JBIDE-1501_size.jsp", getImportProjectName());

		input = new FileEditorInput(file);

		// open and get editor
		part = openEditor(input);

		// get dom document
		document = getVpeVisualDocument(part);
		assertNotNull(document);

		// get dom element
		element = document.getDocumentElement();
		assertNotNull(element);

		// get root node
		node = (nsIDOMNode) element.queryInterface(nsIDOMNode.NS_IDOMNODE_IID);

		elements = new ArrayList<nsIDOMNode>();

		// find "select" elements
		TestUtil.findElementsByName(node, elements, HTML.TAG_SELECT);

		for (nsIDOMNode select : elements) {

			// get attributes
			nsIDOMNamedNodeMap attributes = select.getAttributes();

			// "select" must have attributes
			assertNotNull(attributes);

			// select must have "size" attribute
			nsIDOMNode size = attributes.getNamedItem(HTML.ATTR_SIZE);
			assertNotNull(size);

			//
			assertEquals(2, Integer.parseInt(size.getNodeValue()));

		}

		// check exception
		if (getException() != null) {
			throw getException();
		}

	}

}
