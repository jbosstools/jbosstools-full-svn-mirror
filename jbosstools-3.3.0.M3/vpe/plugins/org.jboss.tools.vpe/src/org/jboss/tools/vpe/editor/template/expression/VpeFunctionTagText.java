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
package org.jboss.tools.vpe.editor.template.expression;

import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMText;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.jboss.tools.vpe.editor.context.VpePageContext;

public class VpeFunctionTagText extends VpeFunction {

	@SuppressWarnings("nls")
	public VpeValue exec(VpePageContext pageContext, Node sourceNode) {
		String tagText = "";
		
		if (sourceNode.hasChildNodes()) {
			NodeList children = sourceNode.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				if (child instanceof IDOMText) {
					String text = ((IDOMText)child).getData();
					text = text.replace('\r', ' ').replace('\n', ' ').trim();
					if (text.length() > 0) {
						if (tagText.length() > 0) {
							text = " " + text;
						}
						tagText += text;
					}
				} else if (child instanceof IDOMElement) {
					if (child.getNodeName().toLowerCase().equals("br")) {
						tagText += "\n";
					}
				}
			}
		}
		return new VpeValue(tagText);
	}
	
	String[] getSignatures() {
		return new String[] {VpeExpressionBuilder.SIGNATURE_ANY_ATTR};
	}
}
