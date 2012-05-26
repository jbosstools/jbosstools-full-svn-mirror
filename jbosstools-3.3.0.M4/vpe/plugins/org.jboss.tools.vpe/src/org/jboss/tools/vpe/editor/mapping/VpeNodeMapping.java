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

/**
 * Stores a relation between Node in source model and nsIDOMNode in in visual model
 * @see VpeDomMapping
 */
public class VpeNodeMapping {
	Node sourceNode;
	nsIDOMNode visualNode;
	
	public VpeNodeMapping(Node sourceNode, nsIDOMNode visualNode) {
		this.sourceNode = sourceNode;
		this.visualNode = visualNode;
	}

	public Node getSourceNode() {
		return sourceNode;
	}
	
	public nsIDOMNode getVisualNode() {
		return visualNode;
	}
}
