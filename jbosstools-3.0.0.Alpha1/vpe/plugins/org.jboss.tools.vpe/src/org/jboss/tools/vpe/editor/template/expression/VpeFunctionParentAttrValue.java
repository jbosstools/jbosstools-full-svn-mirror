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

import org.w3c.dom.Node;

import org.jboss.tools.vpe.editor.context.VpePageContext;

public class VpeFunctionParentAttrValue extends VpeFunction {

	public VpeValue exec(VpePageContext pageContext, Node sourceNode) {
		String prm = getParameter(0).exec(pageContext, sourceNode).stringValue();
		Node parentNode = sourceNode.getParentNode();
		if (parentNode != null) {
			String a = getParameter(0).exec(pageContext, parentNode).stringValue();
			Node attr = parentNode.getAttributes().getNamedItem(a);
			if (attr != null) {
				return new VpeValue(attr.getNodeValue());
			}
		}
		return new VpeValue("");
	}
}
