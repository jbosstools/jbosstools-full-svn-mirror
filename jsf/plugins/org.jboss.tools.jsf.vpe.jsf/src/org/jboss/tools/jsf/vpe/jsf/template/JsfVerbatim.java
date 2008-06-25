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

import java.util.ArrayList;

import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeAbstractTemplate;
import org.jboss.tools.vpe.editor.template.VpeChildrenInfo;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.editor.util.TemplateManagingUtil;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public class JsfVerbatim extends VpeAbstractTemplate {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#create(org.jboss.tools.vpe.editor.context.VpePageContext,
	 *      org.w3c.dom.Node, org.mozilla.interfaces.nsIDOMDocument)
	 */
	public VpeCreationData create(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument) {

		Element element = (Element) sourceNode;

		// create span
		nsIDOMElement span = visualDocument.createElement(HTML.TAG_SPAN);

		// get children
		NodeList list = element.getChildNodes();

		// creation data
		VpeCreationData creationData = new VpeCreationData(span);

		// for each child
		for (int i = 0; i < list.getLength(); i++) {

			Node child = list.item(i);

			// create span for child
			nsIDOMElement childSpan = visualDocument
					.createElement(HTML.TAG_SPAN);
			span.appendChild(childSpan);

			// if child is text or not html tag
			if ((child.getNodeType() == Node.ELEMENT_NODE && child.getPrefix() != null)
					|| (child.getNodeType() == Node.TEXT_NODE)) {

				// create children info and add to creationData
				VpeChildrenInfo childSpanInfo = new VpeChildrenInfo(childSpan);
				childSpanInfo.addSourceChild(child);
				creationData.addChildrenInfo(childSpanInfo);

			} else {
				// get text by positions and add to span
				String text = TemplateManagingUtil.getSourceText(pageContext,
						((IDOMNode) child).getStartOffset(), ((IDOMNode) child)
								.getEndOffset() - 1);
				span.appendChild(visualDocument.createTextNode(text));
			}

		}

		// for case when all children are html tags
		if ((list.getLength() != 0)
				&& (creationData.getChildrenInfoList() == null)) {
			// set empty children info list to visualDomBuilder doesn't
			// search children himself
			creationData.setChildrenInfoList(new ArrayList<VpeChildrenInfo>());

		}

		return creationData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.vpe.editor.template.VpeAbstractTemplate#isRecreateAtAttrChange(org.jboss.tools.vpe.editor.context.VpePageContext,
	 *      org.w3c.dom.Element, org.mozilla.interfaces.nsIDOMDocument,
	 *      org.mozilla.interfaces.nsIDOMElement, java.lang.Object,
	 *      java.lang.String, java.lang.String)
	 */
	public boolean isRecreateAtAttrChange(VpePageContext pageContext,
			Element sourceElement, nsIDOMDocument visualDocument,
			nsIDOMElement visualNode, Object data, String name, String value) {
		return true;
	}

}
