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

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.mapping.VpeNodeMapping;
import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Node;

public class SourceDomUtil {
	static public Node getAncestorNode(Node sourceNode, String tagName){
		if(tagName == null)return null;
		Node element = sourceNode;
		while(true){
			if(tagName.equalsIgnoreCase(element.getNodeName())) return element;
			element = element.getParentNode();
			if(element == null) break;
		}
		return null;
	}
	
	
	/** Finds first n-th parent of <code>sourceNode</code> that has 
	 * a linked non-null nodeMaping in <code>domMapping</code>.
	 * 
	 * @param domMapping 
	 * @param sourceNode
	 * @return first n-th parent of <code>sourceNode</code> that has 
	 * 		a linked non-null nodeMaping in <code>domMapping</code>
	 * 		or <code>null</code> if there is not any. */
	public static Node getParentHavingDomMapping(final Node sourceNode, final VpeDomMapping domMapping) {
		VpeNodeMapping nodeMapping = null;
		Node parent = sourceNode;
		do {
			parent = parent.getParentNode();
			nodeMapping = domMapping.getNodeMapping(parent);
		} while (nodeMapping == null && parent != null);

		return parent;
	}
}
