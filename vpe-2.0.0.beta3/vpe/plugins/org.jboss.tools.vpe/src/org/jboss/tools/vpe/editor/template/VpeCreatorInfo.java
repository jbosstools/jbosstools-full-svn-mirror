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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class VpeCreatorInfo {
	private Node visualNode;
	private List childrenInfoList;
	private Set dependencySet;

	VpeCreatorInfo(Node visualNode) {
		this.visualNode = visualNode;
	}
	
	/**
	 * Returns the built element of the visual tree.
	 * @return The built element of the visual tree.
	 */
	Node getVisualNode() {
		return visualNode;
	}
	
	/**
	 * Adds information on links between visual container and source nodes.
	 * @param info The information on links between visual container and source nodes. 
	 */
	void addChildrenInfo(VpeChildrenInfo info) {
		List infoList = getInfoList();
		infoList.add(info);
	}

	/**
	 * Returns list information� on links between visual container and source nodes.
	 * @return List information� on links between visual container and source nodes.
	 */
	List getChildrenInfoList() {
		return childrenInfoList;
	}

	Set getDependencySet() {
		return dependencySet;
	}
	
	void addDependencySet(Set dependencySet) {
		if (dependencySet != null) {
			if (this.dependencySet == null) {
				this.dependencySet = new HashSet();
			}
			this.dependencySet.addAll(dependencySet);
		}
	}
	
	private List getInfoList() {
		if (childrenInfoList == null) {
			childrenInfoList = new ArrayList();
		}
		return childrenInfoList;
	}
}
