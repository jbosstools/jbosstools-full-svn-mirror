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

import org.w3c.dom.Node;

public class VpeCreationData {
	private Node node;
	private List<VpeChildrenInfo> childrenInfoList;
	private List<Node> illegalChildren;
	private Object data;

	public VpeCreationData(Node node) {
		this.node = node;
	}

	public Node getNode() {
		return node;
	}
	
	public void addChildrenInfo(VpeChildrenInfo info) {
		if (childrenInfoList == null) {
			childrenInfoList = new ArrayList<VpeChildrenInfo>();
		}
		childrenInfoList.add(info);
	}
	
	public List<VpeChildrenInfo> getChildrenInfoList() {
		return childrenInfoList;
	}
	
	public void addIllegalChild(Node child) {
		if (illegalChildren == null) {
			illegalChildren = new ArrayList<Node>();
		}
		illegalChildren.add(child);
	}
	
	public List<Node> getIllegalChildren() {
		return illegalChildren;
	}
	
	public void setData(Object data) {
		this.data = data;
	}
	
	public Object getData() {
		return data;
	}
}
