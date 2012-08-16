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

public class VpeFunctionHasChildren extends VpeFunction {

	public VpeValue exec(VpePageContext pageContext, Node sourceNode)  throws VpeExpressionException {
		boolean includeTextNodes = "true".equalsIgnoreCase( //$NON-NLS-1$
				getParameter(0).exec(pageContext, sourceNode).stringValue());
		
		boolean status = false;
		if (sourceNode != null) {
			if (sourceNode.hasChildNodes()) {
				NodeList list = sourceNode.getChildNodes();
				if (!includeTextNodes && (list.getLength() == 1)
						&& ((Node) list.item(0)).getNodeType() == Node.TEXT_NODE) {
					status = false;
				} else
					status = true;
			}
		}
		return new VpeValue(status);
	}
}
