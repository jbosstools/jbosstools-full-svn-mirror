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

import org.mozilla.interfaces.nsIDOMNode;

public class VpeCreatorInfo {
	private nsIDOMNode visualNode;
	private List<VpeChildrenInfo> childrenInfoList;
	private Set dependencySet;

	VpeCreatorInfo(nsIDOMNode visualNode) {
		this.visualNode = visualNode;
	}
	
	/**
	 * Returns the built element of the visual tree.
	 * @return The built element of the visual tree.
	 */
	nsIDOMNode getVisualNode() {
		return visualNode;
	}
	
	/**
	 * Adds information on links between visual container and source nodes.
	 * @param info The information on links between visual container and source nodes. 
	 */
	void addChildrenInfo(VpeChildrenInfo info) {
		List<VpeChildrenInfo> infoList = getInfoList();
		infoList.add(info);
	}

	/**
	 * Returns list information� on links between visual container and source nodes.
	 * @return List information� on links between visual container and source nodes.
	 */
	List<VpeChildrenInfo> getChildrenInfoList() {
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
	
	private List<VpeChildrenInfo> getInfoList() {
		if (childrenInfoList == null) {
			childrenInfoList = new ArrayList<VpeChildrenInfo>();
		}
		return childrenInfoList;
	}
}
