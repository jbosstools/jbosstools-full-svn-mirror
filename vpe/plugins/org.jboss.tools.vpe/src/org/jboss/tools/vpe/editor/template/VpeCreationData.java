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

import org.jboss.tools.common.model.util.XMLUtil;
import org.jboss.tools.vpe.editor.util.VpeDebugUtil;
import org.jboss.tools.vpe.editor.util.XmlUtil;
import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Node;

public class VpeCreationData {
	private nsIDOMNode node;
	private List<VpeChildrenInfo> childrenInfoList;
	private List<Node> illegalChildren;
	private Object data;

	public VpeCreationData(nsIDOMNode node) {
		this.node = node;
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
	
	/**
	 * Added method for creation copy which will
	 * placed in cash to improve perfomance of VPE
	 * Added by Max Areshkau JBIDE-675. 
	 * Here copyed only nsI****
	 * @return
	 */
	public VpeCreationData createHashCopy() {
		nsIDOMNode node=null;
		if(this.node!=null) {
		node = XmlUtil.createClone(this.node);
	    } else {
	    	VpeDebugUtil.debugInfo("Node is Null");
	    }
		if(node.getNodeType()!=nsIDOMNode.ELEMENT_NODE) {
			VpeDebugUtil.debugInfo("It's Not Element");
		}
		
		VpeCreationData data = new VpeCreationData(node);
		if(this.childrenInfoList!=null) {
		data.childrenInfoList= new ArrayList<VpeChildrenInfo>();
		for (VpeChildrenInfo childrenInfo : this.childrenInfoList) {
			data.childrenInfoList.add(childrenInfo.createCashCopy());
		}
		}
		data.illegalChildren =this.illegalChildren;
		data.data = this.data;
		return data;
	}
}
