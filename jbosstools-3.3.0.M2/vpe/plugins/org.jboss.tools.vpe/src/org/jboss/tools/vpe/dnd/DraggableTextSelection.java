/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.dnd;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.jboss.tools.vpe.editor.util.VisualDomUtil;
import org.jboss.tools.vpe.xulrunner.util.XulRunnerVpeUtils;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMText;

/**
 * @author Yahor Radtsevich (yradtsevich)
 *
 */
public class DraggableTextSelection extends AbstractDraggableFragment {
	private final nsIDOMText selectionContainer;
	private final int startOffset;
	private final int endOffset;

	/**
	 * NOTE: selectionContainer must contain really selected text. It will
	 * not work if real selection range do not math offsets.
	 */
	public DraggableTextSelection(nsIDOMText selectionContainer,
			int startOffset, int endOffset) {
		this.selectionContainer = selectionContainer;
		this.startOffset = startOffset;
		this.endOffset = endOffset;
	}
	

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.dnd.AbstractDraggableFragment#cloneFragmentAsElement()
	 */
	@Override
	public nsIDOMElement cloneFragmentAsElement() {
		nsIDOMDocument document = selectionContainer.getOwnerDocument();
		
		nsIDOMElement cloneContainer = VisualDomUtil.createBorderlessContainer(
				document);
		nsIDOMNode selectionContainerParent = selectionContainer.getParentNode();
		selectionContainerParent.appendChild(cloneContainer);
		String cloneText = selectionContainer.getData().substring(startOffset, endOffset);
		nsIDOMText clone = document.createTextNode(cloneText);
		cloneContainer.appendChild(clone);
		
		return cloneContainer;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.dnd.IDraggableFragment#getPosition()
	 */
	public Point getPosition() {
		Rectangle bounds = XulRunnerVpeUtils.getTextSelectionBounds(selectionContainer); 
		return new Point(bounds.x, bounds.y);
	}
}
