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
package org.jboss.tools.vpe.editor.util;

import java.util.HashSet;
import java.util.Set;

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.mapping.VpeNodeMapping;
import org.jboss.tools.vpe.editor.proxy.VpeProxyUtil;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class SourceDomUtil {
	private static final Set<String> templatesNamespacesWithRendered=new HashSet<String>();
	static {
		templatesNamespacesWithRendered.add("h:"); //$NON-NLS-1$
		templatesNamespacesWithRendered.add("a4j:"); //$NON-NLS-1$
		templatesNamespacesWithRendered.add("rich:"); //$NON-NLS-1$
		templatesNamespacesWithRendered.add("seam:"); //$NON-NLS-1$
	}
	
	static public Node getAncestorNode(Node sourceNode, String tagName) {
		if (tagName == null)
			return null;
		Node element = sourceNode;
		while (true) {
			if (tagName.equalsIgnoreCase(element.getNodeName()))
				return element;
			element = element.getParentNode();
			if (element == null)
				break;
		}
		return null;
	}

	/**
	 * Finds first n-th parent of <code>sourceNode</code> that has a linked
	 * non-null nodeMaping in <code>domMapping</code>.
	 * 
	 * @param domMapping
	 * @param sourceNode
	 * @return first n-th parent of <code>sourceNode</code> that has a linked
	 *         non-null nodeMaping in <code>domMapping</code> or
	 *         <code>null</code> if there is not any.
	 */
	public static Node getParentHavingDomMapping(final Node sourceNode,
			final VpeDomMapping domMapping) {
		VpeNodeMapping nodeMapping = null;
		Node parent = sourceNode;
		do {
			parent = parent.getParentNode();
			nodeMapping = domMapping.getNodeMapping(parent);
		} while (nodeMapping == null && parent != null);

		return parent;
	}

	/**
	 * @author mareshkau FIX for JBIDE-4179 check if node have rendered=false
	 *         attribute, checks el expresion to
	 * @return true if rendered="false"
	 */
	public static boolean isRenderedAttrEqFalse(VpePageContext pageContext,
			Element sourceNode) {
		boolean result = false;
		final String attrName = "rendered"; //$NON-NLS-1$
		Element tempElement = sourceNode;
		if (sourceNode.hasAttribute(attrName)) {
			if (ElService.getInstance().isELNode(pageContext, sourceNode)) {
				tempElement = (Element) VpeProxyUtil
						.createProxyForELExpressionNode(pageContext, sourceNode);
			}
			if ("false".equals(tempElement.getAttribute(attrName))) { //$NON-NLS-1$
				String templateName = VpeTemplateManager.getInstance().getTemplateName(pageContext, sourceNode);
				String [] templatePrefix = templateName.split(":"); //$NON-NLS-1$
				if(templatePrefix.length>1 && templatesNamespacesWithRendered.contains(templatePrefix[0]+":")) { //$NON-NLS-1$
					result = true;
				}
			}
		}
		return result;
	}
}
