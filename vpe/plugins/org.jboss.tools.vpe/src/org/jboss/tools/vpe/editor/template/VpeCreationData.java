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

import org.jboss.tools.vpe.editor.mapping.VpeElementData;
import org.jboss.tools.vpe.editor.util.VpeDebugUtil;
import org.jboss.tools.vpe.editor.util.XmlUtil;
import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Node;

public class VpeCreationData {
	private nsIDOMNode node;
	private List<VpeChildrenInfo> childrenInfoList;
	private List<Node> illegalChildren;

	/**
	 * @deprecated - You must use elementData. If VpeElementData has not
	 *             necessary functionality you must extend its
	 */
	private Object data;
	private VpeElementData elementData;

	public VpeCreationData(nsIDOMNode node) {
		this.node = node;
	}
	
	public VpeCreationData(nsIDOMNode node, boolean initializeChildren) {
		this.node = node;
		if (initializeChildren)
			this.childrenInfoList = new ArrayList<VpeChildrenInfo>();
	} 

	public nsIDOMNode getNode() {
		return node;
	}

	public void addChildrenInfo(VpeChildrenInfo info) {
		if (childrenInfoList == null) {
			childrenInfoList = new ArrayList<VpeChildrenInfo>();
		}
		childrenInfoList.add(info);
	}

	public void setChildrenInfoList(List<VpeChildrenInfo> childrenInfoList) {
		this.childrenInfoList = childrenInfoList;
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

	/**
	 * @deprecated - You must use elementData. If VpeElementData has not
	 *             necessary functionality you must extend its
	 * @param data
	 */
	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * @deprecated - You must use elementData. If VpeElementData has not
	 *             necessary functionality you must extend its
	 * @return
	 */
	public Object getData() {
		return data;
	}

	/**
	 * get element data
	 * 
	 * @return
	 */
	public VpeElementData getElementData() {
		return elementData;
	}

	/**
	 * set element data
	 * 
	 * @param elementData
	 */
	public void setElementData(VpeElementData elementData) {
		this.elementData = elementData;
	}

}
