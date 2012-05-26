/*******************************************************************************
  * Copyright (c) 2007-2009 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.vpe.editor.template;

import static org.jboss.tools.vpe.xulrunner.util.XPCOM.queryInterface;

import org.jboss.tools.vpe.editor.context.VpePageContext;
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
 * This abstract class is intended to provide a common way to implement templates
 * that have to render their contents into the {@code BODY}-element of the page.
 * 
 * In most cases only the method {@code #getTargetAttributeName(String)} should be
 * implemented.
 * 
 * @author ezheleznyakov@exadel.com
 * @author yradtsevich
 */
public abstract class VpeAbstractBodyTemplate extends VpeAbstractTemplate {
	
	/**
	 * This method should return target attribute name for the given 
	 * source attribute name.
	 *
	 * @return target attribute name, or {@code null} if the attribute
	 * have to be omitted.
	 */
	abstract protected String getTargetAttributeName(String sourceAttributeName);
	
	public VpeCreationData create(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument) {
		final nsIDOMElement body = getBody(visualDocument.getDocumentElement());
		cleanBody(body);

		final nsIDOMElement div = visualDocument.createElement(HTML.TAG_DIV);
		final NamedNodeMap sourceNodeAttributes = sourceNode.getAttributes();
		for (int i = 0; i < sourceNodeAttributes.getLength(); i++) {
			final Node sourceAttribute = sourceNodeAttributes.item(i);
			final String sourceAttributeName = sourceAttribute.getNodeName();
			String attributeValue = sourceAttribute.getNodeValue();
			final String targetAttributeName = getTargetAttributeName(sourceAttributeName);
			
			if (targetAttributeName != null) {
				if(HTML.ATTR_ID.equalsIgnoreCase(targetAttributeName)) {
					div.setAttribute(HTML.ATTR_ID, attributeValue);
				} else {
					if (HTML.ATTR_BACKGROUND.equalsIgnoreCase(targetAttributeName)
						|| HTML.ATTR_STYLE.equalsIgnoreCase(targetAttributeName)) {
						/*
						 * https://issues.jboss.org/browse/JBIDE-9975
						 * Simply set the background style, its correction will be made in 
						 * VpeVisualDomBuilder.correctVisualAttribute(..) method. 
						 * Wrong URLs won't be set by XULRunner itself.
						 */
						div.setAttribute(targetAttributeName, attributeValue);
					}
					//FIX FOR JBIDE-1568, added by Max Areshkau
					try {
						body.setAttribute(targetAttributeName, attributeValue);
					} catch(XPCOMException ex ) {
						// Ignored
					}
				}
			}
		}

		return new VpeCreationData(div);
	}

	/**
	 * Deletes all attributes except ID from the {@code body}
	 * 
	 * @param body BODY-element
	 */
	protected void cleanBody(final nsIDOMElement body) {
		nsIDOMNamedNodeMap attributes = body.getAttributes();

		long len = attributes.getLength();
		int j = 0;
		for (int i = 0; i < len; i++) {
			nsIDOMNode attr = attributes.item(j);
			if (HTML.ATTR_ID.equalsIgnoreCase(attr.getNodeName())) {
				j++;
			} else {
				body.removeAttribute(attr.getNodeName());
			}
		}
	}

	/**
	 * Finds {@code BODY}-element
	 * 
	 * @param node a visual node
	 * @return the nearest child of {@code node} named {@code 'BODY'}
	 */
	protected static nsIDOMElement getBody(nsIDOMNode node) {

		final nsIDOMNodeList nodeChildren = node.getChildNodes();
		for (int i = 0; i < nodeChildren.getLength(); i++) {
			final nsIDOMNode nodeChild = nodeChildren.item(i);
			if (HTML.TAG_BODY.equalsIgnoreCase(nodeChild
					.getNodeName())) {
				return queryInterface(nodeChild, nsIDOMElement.class);
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
	@Override
	public boolean recreateAtAttrChange(VpePageContext pageContext,
			Element sourceElement, nsIDOMDocument visualDocument,
			nsIDOMElement visualNode, Object data, String name, String value) {
		return true;
	}

	/**
	 * Cleans the visual {@code BODY}-element before the source element is removed.
	 */
	@Override
	public void beforeRemove(VpePageContext pageContext, Node sourceNode,
			nsIDOMNode visualNode, Object data) {
		final nsIDOMElement body = getBody(visualNode.getOwnerDocument().getDocumentElement());
		cleanBody(body);
	}
}
