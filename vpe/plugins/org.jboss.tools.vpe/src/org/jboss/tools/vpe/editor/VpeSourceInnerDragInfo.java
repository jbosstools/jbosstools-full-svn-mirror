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

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class VpeSourceInnerDragInfo {
	private Node node;
	private int offset;
	private int length;
	
	public VpeSourceInnerDragInfo(Element node) {
		this.node = node;
	}
	
	public VpeSourceInnerDragInfo(Node node, int offset, int length) {
		this.node = node;
		this.offset = offset;
		this.length = length;
	}
	
	public Node getNode() {
		return node;
	}
	
	public int getOffset() {
		return offset;
	}
	
	public int getLength() {
		return length;
	}
}
