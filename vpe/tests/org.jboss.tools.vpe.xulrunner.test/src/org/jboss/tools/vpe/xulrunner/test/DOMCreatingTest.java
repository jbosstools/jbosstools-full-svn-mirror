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
package org.jboss.tools.vpe.xulrunner.test;

import org.mozilla.interfaces.nsIDOMAttr;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;
import org.mozilla.interfaces.nsIDOMText;

/**
 * This class used for testing mozilla interfaces.
 * 
 * @author Max Areshkau
 * 
 */
public class DOMCreatingTest extends XulRunnerAbstractTest {
	/**
	 * Tests possability add and remove dom elements.
	 * 
	 */
	public void testAddRemovingDOMElements() {
	    	nsIDOMDocument domDocument = xulRunnerEditor.getDOMDocument();
		nsIDOMElement root = domDocument.getDocumentElement();
		nsIDOMElement child = domDocument.createElement("test-element");
		root.appendChild(child);
		assertTrue("We doen't have elements to remove", root.getChildNodes()
				.getLength() > 0);
		for (long i = root.getChildNodes().getLength() - 1; i >= 0; i--) {
			root.removeChild(root.getChildNodes().item(i));
		}
		root.appendChild(child);
		assertTrue("We haven't remove some elements", root.getChildNodes()
				.getLength() == 1);
	}

	/**
	 * Tests that DOM can be created
	 * 
	 * @throws Exception
	 */
	public void testXulRunnerCreatingDOM() throws Exception {
		String chieldName = "H";
		String attrName = "color";
		String attrValue = "TEST_VALUE";
		assertNotNull(xulRunnerEditor);
		nsIDOMDocument domDocument = xulRunnerEditor.getDOMDocument();
		nsIDOMElement root = domDocument.getDocumentElement();

		for (long i = root.getChildNodes().getLength() - 1; i >= 0; i--) {
			root.removeChild(root.getChildNodes().item(i));
		}
		// checks of creating elements with attributes and chield nodes
		nsIDOMElement child = domDocument.createElement("test-element");
		for (int i = 0; i < 4; i++) {
			child.appendChild(domDocument.createElement(chieldName + i));
		}
		for (int i = 0; i < 3; i++) {
			child.setAttribute(attrName + i, attrValue + i);
		}
		nsIDOMAttr attr = domDocument.createAttribute(attrName + 3);
		attr.setValue(attrValue + 3);
		child.setAttributeNode(attr);
		// append child element to root element
		root.appendChild(child);
		nsIDOMNode toCheck = root.getChildNodes().item(0);
		assertEquals("We haven't add child element", toCheck.getNodeName(),
				child.getNodeName());

		assertEquals("Number of child nodes do not coincide", 4,child
				.getChildNodes().getLength());
		nsIDOMNodeList nodeList = child.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			assertEquals("Child node doesn't concide", nodeList.item(i)
					.getNodeName(), chieldName + i);
		}

		for (int i = 0; i < toCheck.getAttributes().getLength(); i++) {
			assertEquals("Attribute names doesn't coinside", toCheck.getAttributes()
					.item(i).getNodeName(), attrName + i);
			assertEquals("Attribute values doesn't coinside", toCheck.getAttributes()
					.item(i).getNodeValue(), attrValue + i);
		}
		nsIDOMText text = domDocument.createTextNode("TEST");
		root.appendChild(text);
		assertEquals("Dom element hasn't been created", "TEST", text
				.getNodeValue());
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
}
