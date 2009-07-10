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

package org.jboss.tools.vpe.editor.template;

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.util.HTML;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMText;
import org.w3c.dom.Node;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public class InvisibleTemplate extends VpeAbstractTemplate {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.vpe.editor.template.VpeTemplate#create(org.jboss.tools
	 * .vpe.editor.context.VpePageContext, org.w3c.dom.Node,
	 * org.mozilla.interfaces.nsIDOMDocument) #0037FF #ACBECE
	 */
	public VpeCreationData create(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument) {

		nsIDOMElement div = visualDocument.createElement(HTML.TAG_SPAN);

		div.setAttribute(HTML.TAG_STYLE,
				"border: solid 1px #0037FF;color:#0037FF;font-size:12px;"); //$NON-NLS-1$

		nsIDOMText text = visualDocument.createTextNode(sourceNode
				.getNodeName());

		div.appendChild(text);

		return new VpeCreationData(div);
	}

}
