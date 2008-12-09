/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.mapping;

import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Node;

/**
 * 
 * @author Sergey Dzmitrovich
 * 
 *         Keep information about output Node. Set up a correspondence source
 *         node and visual node
 * 
 * 
 */
public class NodeData {

	final public static int NODE = 0;

	final public static int ATTRIBUTE = 1;

	/**
	 * source presentation of attribute
	 */
	protected Node sourceNode;

	/**
	 * visual presentation of attribute
	 */
	protected nsIDOMNode visualNode;

	/**
	 * mark if editable
	 */
	protected boolean isEditable;

	public NodeData(Node sourceNode, nsIDOMNode visualNode, boolean isEditable) {
		this.sourceNode = sourceNode;
		this.visualNode = visualNode;
		this.isEditable = isEditable;

	}

	public NodeData(Node sourceNode, nsIDOMNode visualNode) {
		this.sourceNode = sourceNode;
		this.visualNode = visualNode;
		this.isEditable = true;

	}

	public NodeData() {
		this.visualNode = null;
		this.sourceNode = null;
		this.isEditable = true;

	}

	public Node getSourceNode() {
		return sourceNode;
	}

	public void setSourceNode(Node sourceNode) {
		this.sourceNode = sourceNode;
	}

	public nsIDOMNode getVisualNode() {
		return visualNode;
	}

	public void setVisualNode(nsIDOMNode visualNode) {
		this.visualNode = visualNode;
	}

	public boolean isEditable() {
		return isEditable;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	public int getType() {
		return NODE;
	}

}
