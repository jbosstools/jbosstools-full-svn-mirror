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
package org.jboss.tools.vpe.editor.template;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Describes links between visual container and source nodes 
 */

public class VpeChildrenInfo {
	private Element visualParent;
	private List sourceChildren;

	public VpeChildrenInfo(Element visualParent) {
		this.visualParent = visualParent;
	}
	
	/**
	 * Returns the container of the visual tree for adding new nodes.
	 * @return The container of the visual tree for adding new nodes.
	 */
	public Element getVisualParent() {
		return visualParent;
	}
	
	/**
	 * Returs <code>List</code> of nodes of the source tree for creating new visual nodes.
	 * @return <code>List</code> of nodes of the source tree for creating new visual nodes.
	 */
	public List getSourceChildren() {
		return sourceChildren;
	}
	
	/**
	 * Adds the node in a list of nodes
	 * @param child The node of the source tree.
	 */
	public void addSourceChild(Node child) {
		List children = getChildren();
		children.add(child);
	}
	
	private List getChildren() {
		if (sourceChildren == null) {
			sourceChildren = new ArrayList();
		}
		return sourceChildren;
	}
}
