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

package org.jboss.tools.vpe.editor.template.expression;

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.w3c.dom.Node;

public class VpeFunctionIsLastChild extends VpeFunction {

	public VpeValue exec(VpePageContext pageContext, Node sourceNode) {
		return new VpeValue(!hasNextElementNode(sourceNode));
	}

	private boolean hasNextElementNode(Node sourceNode) {

		Node nextNode = sourceNode.getNextSibling();
		if (nextNode == null) {
			return false;
		} else if (nextNode.getNodeType() == Node.ELEMENT_NODE) {
			return true;
		} else {
			return hasNextElementNode(nextNode);
		}
	}
}
