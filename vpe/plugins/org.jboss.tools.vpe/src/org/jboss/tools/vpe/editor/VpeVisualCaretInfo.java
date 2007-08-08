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

import org.eclipse.swt.graphics.Point;
import org.w3c.dom.Node;

import org.mozilla.interfaces.nsIDOMNode;

public class VpeVisualCaretInfo {
	private VpeSelectionBuilder selectionBuilder;
	private nsIDOMNode rangeParent;
	private int rangeOffset;

	public VpeVisualCaretInfo(VpeSelectionBuilder selectionBuilder, nsIDOMNode rangeParent, int rangeOffset) {
		this.selectionBuilder = selectionBuilder;
		this.rangeParent = rangeParent;
		this.rangeOffset = rangeOffset;
		
	}
	
	public boolean exist() {
		return rangeParent != null;
	}
	
	public void showCaret() {
		selectionBuilder.showVisualDragCaret(rangeParent, rangeOffset);
	}
	
	public void hideCaret() {
		selectionBuilder.hideVisualDragCaret();
	}
	
	public int getSourcePosition() {
		return selectionBuilder.getSourcePosition(rangeParent, rangeOffset);
	}

	Point getSourceSelectionRange() {
		return selectionBuilder.getSourceSelectionRangeAtVisualNode(rangeParent, rangeOffset);
	}
}
