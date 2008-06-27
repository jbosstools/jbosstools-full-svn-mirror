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
package org.jboss.tools.jsf.vpe.jsf.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.tools.jsf.vpe.jsf.template.util.ComponentUtil;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeAbstractTemplate;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.jboss.tools.vpe.editor.util.HTML;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;
import org.mozilla.xpcom.XPCOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author Dzmitry Sakovich (dsakovich@exadel.com)
 * 
 * template for selectOneMenu select item
 * 
 */
public class JsfSelectOneMenuTemplate extends VpeAbstractTemplate {

	/**
	 * "size" attribute
	 */

	private static final String ATTR_DISABLED_CLASS = "disabledClass";
	private static final String ATTR_ENABLED_CLASS = "enabledClass";

	/**
	 * list of visible children
	 */
	private static List<String> CHILDREN_LIST = new ArrayList<String>();

	static {
		CHILDREN_LIST.add("selectItem"); //$NON-NLS-1$
		CHILDREN_LIST.add("selectItems"); //$NON-NLS-1$
	}

	/**
	 * list of copied attributes
	 */
	private static Map<String, String> ATTR_LIST_COPY = new HashMap<String, String>();

	static {
		ATTR_LIST_COPY.put("style", "style"); //$NON-NLS-1$
		ATTR_LIST_COPY.put("styleClass", "class"); //$NON-NLS-1$
		ATTR_LIST_COPY.put("disabled", "disabled"); //$NON-NLS-1$
		ATTR_LIST_COPY.put("dir", "dir"); //$NON-NLS-1$

	}

	/**
	 * 
	 */
	public JsfSelectOneMenuTemplate() {

		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#create(org.jboss.tools.vpe.editor.context.VpePageContext,
	 *      org.w3c.dom.Node, org.mozilla.interfaces.nsIDOMDocument)
	 */
	public VpeCreationData create(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument) {

		// create select element
		nsIDOMElement select = visualDocument.createElement(HTML.TAG_SELECT);
		select.setAttribute(HTML.ATTR_SIZE, "1");
		Element element = (Element) sourceNode;

		// import attributes from source
		Set<String> jsfAttributes = ATTR_LIST_COPY.keySet();

		for (String attributeName : jsfAttributes) {

			// get attribute
			String attr = element.getAttribute(attributeName);

			// add attribute to "select"
			if (attr != null) {
				if (attributeName.equalsIgnoreCase(HTML.ATTR_DISABLED)) {
					if (attr.equalsIgnoreCase("true")) {
						select.setAttribute(ATTR_LIST_COPY.get(attributeName),
								"disabled");
						
					}
					continue;
				}
				select.setAttribute(ATTR_LIST_COPY.get(attributeName), attr);
			}

		}

		return new VpeCreationData(select);
	}

	/**
	 * 
	 */
	public void removeAttribute(VpePageContext pageContext,
			Element sourceElement, nsIDOMDocument visualDocument,
			nsIDOMNode visualNode, Object data, String name) {

		// get DOMElement(root element is select)
		nsIDOMElement select = (nsIDOMElement) visualNode
				.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);

		// remove attribute
		select.removeAttribute(name);
	}

	@Override
	public void validate(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument, VpeCreationData data) {
		if (data.getNode() != null) {
			applyChildAttributes((Element) sourceNode, data.getNode());
		}
	}

	private void applyChildAttributes(Element sourceElement, nsIDOMNode node) {
		boolean disabled = false;
		try {
			nsIDOMNodeList list = node.getChildNodes();
			nsIDOMElement element = (nsIDOMElement) node
					.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
			disabled = ComponentUtil.string2boolean(ComponentUtil.getAttribute(
					sourceElement, HTML.ATTR_DISABLED));
			if (node.getNodeName().equalsIgnoreCase(HTML.TAG_OPTION)) {
				element.setAttribute(HTML.ATTR_CLASS, disabled ? ComponentUtil
						.getAttribute(sourceElement, ATTR_DISABLED_CLASS)
						: ComponentUtil.getAttribute(sourceElement,
								ATTR_ENABLED_CLASS));
			}
			for (int i = 0; i < list.getLength(); i++) {
				applyChildAttributes(sourceElement, list.item(i));
			}
		} catch (XPCOMException e) {
			// Ignore
			return;
		}
	}
}
