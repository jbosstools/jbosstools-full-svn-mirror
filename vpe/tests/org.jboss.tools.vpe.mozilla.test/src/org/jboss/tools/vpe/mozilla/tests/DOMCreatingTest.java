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
package org.jboss.tools.vpe.mozilla.tests;

import org.eclipse.ui.PlatformUI;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMDocument;
import org.jboss.tools.vpe.mozilla.browser.MozillaBrowser;
import org.jboss.tools.vpe.mozilla.view.MozillaView;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * This class used for testing mozilla interfaces.
 * 
 * @author Max Areshkau
 * 
 */
public class DOMCreatingTest extends MozillaBrowserTest {

	/**
	 * Contains brouser instamce
	 */
	private MozillaBrowser mozillaBrowser;

	/**
	 * Tests possability add and remove dom elements.
	 * 
	 */
	public void testAddRemovingDOMElements() {
		Element root = mozillaBrowser.getDOMDocumentElement();
		nsIDOMDocument domDocument = mozillaBrowser.getDOMDocument();
		Element child = domDocument.createElement("test-element");
		root.appendChild(child);
		assertTrue("We doen't have elements to remove", root.getChildNodes()
				.getLength() > 0);
		for (int i = root.getChildNodes().getLength() - 1; i >= 0; i--) {
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
	public void testMozillaCreatingDOM() throws Exception {
		String chieldName = "H";
		String attrName = "color";
		String attrValue = "TEST_VALUE";
		assertNotNull(mozillaBrowser);
		nsIDOMDocument domDocument = mozillaBrowser.getDOMDocument();
		Element root = mozillaBrowser.getDOMDocumentElement();

		for (int i = root.getChildNodes().getLength() - 1; i >= 0; i--) {
			root.removeChild(root.getChildNodes().item(i));
		}
		// checks of creating elements with attributes and chield nodes
		Element child = domDocument.createElement("test-element");
		for (int i = 0; i < 4; i++) {
			child.appendChild(domDocument.createElement(chieldName + i));
		}
		for (int i = 0; i < 3; i++) {
			child.setAttribute(attrName + i, attrValue + i);
		}
		Attr attr = domDocument.createAttribute(attrName + 3);
		attr.setValue(attrValue + 3);
		child.setAttributeNode(attr);
		// append child element to root element
		root.appendChild(child);
		Node toCheck = root.getChildNodes().item(0);
		assertEquals("We haven't add child element", toCheck.getNodeName(),
				child.getNodeName());

		assertEquals("Number of child nodes do not coincide", 4,child
				.getChildNodes().getLength());
		NodeList nodeList = child.getChildNodes();
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
		Text text = domDocument.createTextNode("TEST");
		root.appendChild(text);
		assertEquals("Dom element hasn't been created", "TEST", text
				.getNodeValue());
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		waitForJobs();
		MozillaView mozilla
			= ((MozillaView) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().showView(VIEW_ID));
		waitForJobs();
		delay(3000);
		
		mozillaBrowser = mozilla.getBrowser();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
}
