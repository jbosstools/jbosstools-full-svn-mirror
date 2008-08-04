/*******************************************************************************
 * Copyright (c) 2007-2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jsf.vpe.jsf.template;

import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.jboss.tools.jsf.vpe.jsf.template.util.ComponentUtil;
import org.jboss.tools.jsf.vpe.jsf.template.util.JSF;
import org.jboss.tools.jsf.vpe.jsf.template.util.NodeProxyUtil;
import org.jboss.tools.jsf.vpe.jsf.template.util.model.VpeElementProxyData;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.mapping.AttributeData;
import org.jboss.tools.vpe.editor.template.VpeChildrenInfo;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.jboss.tools.vpe.editor.util.Constants;
import org.jboss.tools.vpe.editor.util.HTML;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMText;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author dmaliarevich
 * 
 */
public class JsfOptionSelectItemTemplate extends AbstractOutputJsfTemplate /* VpeAbstractTemplate */{

	public static final String EMPTY_STRING = ""; //$NON-NLS-1$

	/* "escape" attribute of f:selectItem */

	private String escape;
	private String disabled;
	private String enabledClass;
	private String disabledClass;

	/**
	 * 
	 */
	public JsfOptionSelectItemTemplate() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.vpe.editor.template.VpeTemplate#create(org.jboss.tools
	 * .vpe.editor.context.VpePageContext, org.w3c.dom.Node,
	 * org.mozilla.interfaces.nsIDOMDocument)
	 */
	public VpeCreationData create(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument) {

		readParentAttributes(sourceNode.getParentNode());
		readAttributes(sourceNode);
		Element element = (Element) sourceNode;
		boolean disabledItem = ComponentUtil.string2boolean(ComponentUtil
				.getAttribute(element, JSF.ATTR_ITEM_DISABLED));
		nsIDOMElement option = visualDocument.createElement(HTML.TAG_OPTION);

		if (disabledItem)

			option.setAttribute(HTML.ATTR_DISABLED, Constants.TRUE);
		VpeCreationData creationData = new VpeCreationData(option);

		if (attrPresents(disabled) && Constants.TRUE.equalsIgnoreCase(disabled)) {
			option.setAttribute(HTML.ATTR_CLASS, disabledClass);
		} else if (attrPresents(enabledClass)) {
			option.setAttribute(HTML.ATTR_CLASS, enabledClass);
		}

		processOutputAttribute(pageContext, visualDocument, element, option,
				creationData);

		return creationData;
	}

	protected void processOutputAttribute(VpePageContext pageContext,
			nsIDOMDocument visualDocument, Element sourceElement,
			nsIDOMElement targetVisualElement, VpeCreationData creationData) {

		VpeElementProxyData elementData = new VpeElementProxyData();

		Attr outputAttr = getOutputAttributeNode(sourceElement);

		if (outputAttr != null) {

			// prepare value
			String newValue = outputAttr.getValue();

			// if escape then contents of value (or other attribute) is only
			// text
			if (!sourceElement.hasAttribute(JSF.ATTR_ESCAPE)
					|| Constants.TRUE.equalsIgnoreCase(sourceElement //$NON-NLS-1$
							.getAttribute(JSF.ATTR_ESCAPE))) {

				String value = outputAttr.getNodeValue();

				nsIDOMText text;
				// if bundleValue differ from value then will be represent
				// bundleValue, but text will be not edit
				boolean isEditable = value.equals(newValue);

				text = visualDocument.createTextNode(newValue);
				// add attribute for ability of editing

				elementData.addNodeData(new AttributeData(outputAttr,
						targetVisualElement, isEditable));

				targetVisualElement.appendChild(text);

			}
			// then text can be html code
			else {

				// create info
				VpeChildrenInfo targetVisualInfo = new VpeChildrenInfo(
						targetVisualElement);

				// get atribute's offset
				int offset = ((IDOMAttr) outputAttr)
						.getValueRegionStartOffset();

				// reparse attribute's value
				NodeList list = NodeProxyUtil.reparseAttributeValue(
						elementData, newValue, offset + 1);

				// add children to info
				for (int i = 0; i < list.getLength(); i++) {

					Node child = list.item(i);

					// add info to creation data
					targetVisualInfo.addSourceChild(child);
				}

				elementData.addNodeData(new AttributeData(outputAttr,
						targetVisualElement, false));

				creationData.addChildrenInfo(targetVisualInfo);

			}

		}

		creationData.setElementData(elementData);
	}

	/**
	 * Checks is attribute presents.
	 * 
	 * @param attr
	 *            the attribute
	 * 
	 * @return true, if successful
	 */
	private boolean attrPresents(String attr) {
		return ((null != attr) && (!Constants.EMPTY.equals(attr)));
	}

	/**
	 * Read attributes from the h:SelectManyCheckbox element.
	 * 
	 * @param sourceNode
	 *            the source node
	 */
	private void readParentAttributes(Node sourceNode) {
		if (null == sourceNode) {
			return;
		}
		Element source = (Element) sourceNode;
		disabled = source.getAttribute(HTML.ATTR_DISABLED);
		enabledClass = source.getAttribute(JSF.ATTR_ENABLED_CLASS);
		disabledClass = source.getAttribute(JSF.ATTR_DISABLED_CLASS);
	}

	/**
	 * Read attributes from the source element.
	 * 
	 * @param sourceNode
	 *            the source node
	 */
	private void readAttributes(Node sourceNode) {
		if (null == sourceNode) {
			return;
		}
		Element source = (Element) sourceNode;
		escape = source.getAttribute(JSF.ATTR_ESCAPE);
	}

	@Override
	public Attr getOutputAttributeNode(Element element) {

		if (element.hasAttribute(JSF.ATTR_ITEM_LABEL))
			return element.getAttributeNode(JSF.ATTR_ITEM_LABEL);
		return null;
	}

}
