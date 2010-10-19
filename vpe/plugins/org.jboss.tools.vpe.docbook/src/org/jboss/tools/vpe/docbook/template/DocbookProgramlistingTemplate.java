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

package org.jboss.tools.vpe.docbook.template;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeAbstractTemplate;
import org.jboss.tools.vpe.editor.template.VpeChildrenInfo;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.jboss.tools.vpe.editor.util.HTML;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMText;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Used for processing CDATA sections.
 * 
 * @author dvinnichek
 * 
 */
public class DocbookProgramlistingTemplate extends VpeAbstractTemplate {

	@Override
	public VpeCreationData create(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument) {

		final nsIDOMElement newElement = visualDocument
				.createElement(HTML.TAG_PRE);
		final VpeCreationData creationData = new VpeCreationData(newElement);
		
		NodeList childNodes = sourceNode.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node childNode = childNodes.item(i);
			if (childNode.getNodeType() == Node.CDATA_SECTION_NODE) {
				String cdataText = childNode.getNodeValue();
				newElement.appendChild(visualDocument.createTextNode(cdataText));
			} else {
				nsIDOMElement spanElement = visualDocument.createElement(HTML.TAG_SPAN);
				newElement.appendChild(spanElement);
				VpeChildrenInfo info = new VpeChildrenInfo(spanElement);
				info.addSourceChild(childNode);
				creationData.addChildrenInfo(info);
			}
		}
		
		return creationData;
	}

}
