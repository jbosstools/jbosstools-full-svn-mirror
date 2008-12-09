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
import org.w3c.dom.NodeList;

import org.jboss.tools.vpe.editor.context.VpePageContext;

public class VpeFunctionHasContent extends VpeFunction {

	public VpeValue exec(VpePageContext pageContext, Node sourceNode) {
		boolean value = false;
		if (sourceNode != null){
			NodeList children = sourceNode.getChildNodes();
			if (children != null) {
				int len = children.getLength();
				if (len > 1) {
					value = true;
				} else if (len == 1) {
					Node child = children.item(0); 
					value = child.getNodeType() != Node.TEXT_NODE || child.getNodeValue().trim().length() > 0;
				}
			}
		}
		return new VpeValue(value);
	}
}
