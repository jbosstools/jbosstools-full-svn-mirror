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
package org.jboss.tools.vpe.editor.mapping;

import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Node;

public class VpeNodeMapping {
	public static final int EMPTY_MAPPING = 0;
	public static final int TEXT_MAPPING = 1;
	public static final int ELEMENT_MAPPING = 2;
	public static final int COMMENT_MAPPING = 3;
	public static final int DOCUMENT_MAPPING = 4;
	
	private Node sourceNode;
	private nsIDOMNode visualNode;
	
	public VpeNodeMapping(Node sourceNode, nsIDOMNode visualNode) {
		this.sourceNode = sourceNode;
		this.visualNode = visualNode;
	}

	public int getType() {
		if (sourceNode != null) {
			switch (sourceNode.getNodeType()) {
			case Node.TEXT_NODE:
				return TEXT_MAPPING;
			case Node.ELEMENT_NODE:
				return ELEMENT_MAPPING;
			case Node.COMMENT_NODE:
				return COMMENT_MAPPING;
			case Node.DOCUMENT_NODE:
				return DOCUMENT_MAPPING;
			}
			return COMMENT_MAPPING;
		}
		return EMPTY_MAPPING;
	}
	
	public Node getSourceNode() {
		return sourceNode;
	}
	
	public nsIDOMNode getVisualNode() {
		return visualNode;
	}
}
