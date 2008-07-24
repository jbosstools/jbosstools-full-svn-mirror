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
package org.jboss.tools.vpe.editor;

import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;

public class VpeVisualInnerDragInfo {
	private nsIDOMNode node;
	private int offset;
	private int length;
	
	public VpeVisualInnerDragInfo(nsIDOMElement node) {
		this.node = node;
	}
	
	public VpeVisualInnerDragInfo(nsIDOMNode node, int offset, int length) {
		this.node = node;
		this.offset = offset;
		this.length = length;
	}
	
	public nsIDOMNode getNode() {
		return node;
	}
	
	public int getOffset() {
		return offset;
	}
	
	public int getLength() {
		return length;
	}
	
	public void Release() {
		if (node != null) {
			node = null;
		}
	}
}
