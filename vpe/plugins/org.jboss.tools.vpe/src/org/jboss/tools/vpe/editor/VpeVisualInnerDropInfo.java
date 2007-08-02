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

import org.w3c.dom.Node;

public class VpeVisualInnerDropInfo {
	private Node dropContainer;
	private int dropOffset;
	private int mouseX;
	private int mouseY;

	public VpeVisualInnerDropInfo(Node dropContainer, int dropOffset, int mouseX, int mouseY) {
		this.dropContainer = dropContainer;
		this.dropOffset = dropOffset;
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		
	}
	
	public Node getDropContainer() {
		return dropContainer;
	}
	
	public int getDropOffset() {
		return dropOffset;
	}
	public void setDropOffset(int dropOffset) {
		this.dropOffset = dropOffset;
	}
	
	public int getMouseX() {
		return mouseX;
	}
	
	public int getMouseY() {
		return mouseY;
	}
	
	public void Release() {
		if (dropContainer != null) {
			dropContainer = null;
		}
	}
}
