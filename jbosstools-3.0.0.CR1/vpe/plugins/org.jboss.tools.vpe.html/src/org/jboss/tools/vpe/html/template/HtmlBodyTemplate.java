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
package org.jboss.tools.vpe.html.template;

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeAbstractTemplate;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.editor.util.VpeStyleUtil;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNamedNodeMap;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;
import org.mozilla.xpcom.XPCOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @author ezheleznyakov@exadel.com
 */
public class HtmlBodyTemplate extends VpeAbstractTemplate {

	public VpeCreationData create(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument) {
		final nsIDOMElement body = getBody(visualDocument.getDocumentElement());

		nsIDOMNamedNodeMap attrsMap = body.getAttributes();
		long len = attrsMap.getLength();
		int j = 0;
		for (int i = 0; i < len; i++) {
			nsIDOMNode attr = attrsMap.item(j);
			if (HTML.ATTR_ID.equalsIgnoreCase(attr.getNodeName())) {
				j++;
			} else {
				body.removeAttribute(attr.getNodeName());
			}
		}

		final nsIDOMElement div = visualDocument.createElement(HTML.TAG_DIV);
		final NamedNodeMap sourceNodeAttributes = sourceNode.getAttributes();
		for (int i = 0; i < sourceNodeAttributes.getLength(); i++) {
			final Node sourceNodeAttribute = sourceNodeAttributes.item(i);
			final String name = sourceNodeAttribute.getNodeName();
			String value = sourceNodeAttribute.getNodeValue();
			if(HTML.ATTR_ID.equalsIgnoreCase(name)) {
				div.setAttribute(HTML.ATTR_ID, value);
			} else {
				// all full path for 'url'
				if (VpeStyleUtil.ATTRIBUTE_STYLE.equalsIgnoreCase(name))
					value = VpeStyleUtil.addFullPathIntoURLValue(value, pageContext
							.getEditPart().getEditorInput());
				if (VpeStyleUtil.PARAMETR_BACKGROND.equalsIgnoreCase(name))
					value = VpeStyleUtil.addFullPathIntoBackgroundValue(value,
							pageContext.getEditPart().getEditorInput());
				//FIX FOR JBIDE-1568, added by Max Areshkau
				try{
					body.setAttribute(name, value);
				}catch(XPCOMException ex ) {
					//jsut ignore it
				}
			}			
		}

		return new VpeCreationData(div);
	}

	/**
	 * Finds {@code BODY}-element
	 * 
	 * @param node a visual node
	 * @return the nearest child of {@code node} named {@code 'BODY'}
	 */
	private nsIDOMElement getBody(nsIDOMNode node) {

		final nsIDOMNodeList nodeChildren = node.getChildNodes();
		for (int i = 0; i < nodeChildren.getLength(); i++) {
			final nsIDOMNode nodeChild = nodeChildren.item(i);
			if (HTML.TAG_BODY.equalsIgnoreCase(nodeChild
					.getNodeName())) {
				return (nsIDOMElement) nodeChild
						.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
			} else {
				nsIDOMElement body = getBody(nodeChild);
				if (body != null) {
					return body;
				}
			}
		}
		
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isRecreateAtAttrChange(VpePageContext pageContext,
			Element sourceElement, nsIDOMDocument visualDocument,
			nsIDOMElement visualNode, Object data, String name, String value) {
		return true;
	}
}