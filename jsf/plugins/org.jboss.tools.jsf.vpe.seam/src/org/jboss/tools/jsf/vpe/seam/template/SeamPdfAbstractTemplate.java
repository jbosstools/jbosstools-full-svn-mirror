/******************************************************************************* 
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jsf.vpe.seam.template;

/**
 * @author yzhishko
 */

import org.jboss.tools.jsf.vpe.seam.template.util.SeamUtil;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeAbstractTemplate;
import org.jboss.tools.vpe.editor.util.HTML;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public abstract class SeamPdfAbstractTemplate extends VpeAbstractTemplate {

	@Override
	public Node getNodeForUpdate(VpePageContext pageContext, Node sourceNode,
			nsIDOMNode visualNode, Object data) {
		Node[] footers = SeamUtil.getChildsByName(pageContext, sourceNode,
				"p:footer"); //$NON-NLS-1$
		if (footers != null && footers.length != 0) {
			Node parentPdfDocumentNode = SeamUtil.getParentByName(pageContext,
					sourceNode, "p:document"); //$NON-NLS-1$
			if (parentPdfDocumentNode != null) {
				return parentPdfDocumentNode;
			}
		}
		return super
				.getNodeForUpdate(pageContext, sourceNode, visualNode, data);
	}

	protected final void copySizeAttrs(nsIDOMElement visualElement,
			Element sourceElement) {
		if (sourceElement.hasAttribute(HTML.ATTR_WIDTH)) {
			String width = sourceElement.getAttribute(HTML.ATTR_WIDTH);			
			if (visualElement.hasAttribute(HTML.ATTR_STYLE)) {
				String styleAttrValue = visualElement.getAttribute(HTML.ATTR_STYLE);
				visualElement.setAttribute(HTML.ATTR_STYLE, styleAttrValue
						+ "; width:" + width); //$NON-NLS-1$				
			} else {
				visualElement.setAttribute(HTML.ATTR_STYLE, "width:" + width); //$NON-NLS-1$
			}
		}
		if (sourceElement.hasAttribute(HTML.ATTR_HEIGHT)) {
			String height = sourceElement.getAttribute(HTML.ATTR_HEIGHT);			
			if (visualElement.hasAttribute(HTML.ATTR_STYLE)) {
				String styleAttrValue = visualElement.getAttribute(HTML.ATTR_STYLE);
				visualElement.setAttribute(HTML.ATTR_STYLE, styleAttrValue
						+ "; height:" + height); //$NON-NLS-1$
			} else {
				visualElement.setAttribute(HTML.ATTR_STYLE, "height:" + height); //$NON-NLS-1$
			}
		}
	}

}
