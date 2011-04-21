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

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import org.jboss.tools.vpe.editor.context.VpePageContext;

public class VpeFunctionTagString extends VpeFunction {

	@SuppressWarnings("nls")
	public VpeValue exec(VpePageContext pageContext, Node sourceNode) {
		String tagString = " <" + sourceNode.getNodeName();
		NamedNodeMap attrs = sourceNode.getAttributes();
		if (attrs != null) {
			tagString += attrs.getLength() > 0 ? " " : "";
			for (int i = 0; i < attrs.getLength(); i++) {
				Node attr = attrs.item(i);
				tagString += 
					attr.getNodeName() + "=\"" + attr.getNodeValue() + "\"" +
					(i < (attrs.getLength() - 1) ? " " : ""); 
			}
		}
		tagString += (sourceNode.hasChildNodes() ? "" : "/") + "> ";
		return new VpeValue(tagString);
	}
	
	String[] getSignatures() {
		return new String[] {VpeExpressionBuilder.SIGNATURE_ANY_ATTR};
	}
}
