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

import java.util.EnumSet;

import org.eclipse.swt.graphics.Rectangle;
import org.jboss.tools.vpe.editor.mozilla.MozillaEditor;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.editor.util.VpeStyleUtil;
import org.jboss.tools.vpe.xulrunner.util.XulRunnerVpeUtils;
import org.mozilla.interfaces.nsIDOMCSSStyleDeclaration;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;

/**
 * Class responsible for showing the place where the node being dragged
 * could be dropped.
 * 
 * @author Yahor Radtsevich (yradtsevich)
 */
public class DropableArea {
	private boolean visible;
	private EnumSet<DropTarget> dropTargets;
	private nsIDOMDocument document;
	private nsIDOMNode node;
	private nsIDOMElement domArea;
	private static final String AREA_COLOR = "rgba(166, 202, 240, 0.5)"; //$NON-NLS-1$
	private DropTarget highlightedDropTarget;
	
	/**
	 * 
	 * @param document cannot be null
	 */
	public DropableArea(nsIDOMDocument document) {
		this.document = document;
	}
	
	/**
	 * @param node cannot be null
	 */
	public void setNode(nsIDOMNode node) {
		this.node = node;
	}
	public nsIDOMNode getNode() {
		return node;
	}
	
	/**
	 * @param dropTargets cannot be null
	 */
	public void setDropTargets(EnumSet<DropTarget> dropTargets) {
		this.dropTargets = dropTargets;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public void setHighlightedDropTarget(int mouseX, int mouseY) {
		if (node == null) {
			highlightedDropTarget = null;
		}
		
		Rectangle bounds = XulRunnerVpeUtils.getElementBounds(node);
		if (dropTargets.contains(DropTarget.BEFORE)
				&& bounds.x <= mouseX
				&& mouseX < bounds.x + bounds.width / 5) {
			highlightedDropTarget = DropTarget.BEFORE; 
		} else if (dropTargets.contains(DropTarget.AFTER)
				&& bounds.x + bounds.width * 4 / 5 <= mouseX
				&& mouseX < bounds.x + bounds.width) {
			highlightedDropTarget = DropTarget.AFTER;
		} else if (dropTargets.contains(DropTarget.BEGIN)
				&& bounds.y <= mouseY
				&& mouseY < bounds.y + bounds.height / 5) {
			highlightedDropTarget = DropTarget.BEGIN;
		} else if (dropTargets.contains(DropTarget.END)
				&& bounds.y + bounds.height * 4 / 5 <= mouseY
				&& mouseY < bounds.y + bounds.height) {
			highlightedDropTarget = DropTarget.END;
		} else {
			highlightedDropTarget = null;
		}
	}

	public DropTarget getHighlightedDropTarget() {
		return highlightedDropTarget;
	}

	public void redraw() {
		if (!visible || node == null) {
			if (domArea != null) {
				domArea.getParentNode().removeChild(domArea);
				domArea = null;
			}
			return;
		}
		
		Rectangle bounds = XulRunnerVpeUtils.getElementBounds(node);
		
		nsIDOMElement oldDomArea = domArea;
		domArea = createRect(bounds, AREA_COLOR);
		nsIDOMElement contentArea
				= document.getElementById(MozillaEditor.CONTENT_AREA_ID);
		contentArea.appendChild(domArea);
		
		nsIDOMCSSStyleDeclaration style;
		nsIDOMElement line;
		
		if (dropTargets.contains(DropTarget.BEFORE)) {
			line = createVerticalLine(bounds.height, getColor(DropTarget.BEFORE));
			style = VpeStyleUtil.getStyle(line);
			style.setProperty(HTML.STYLE_PARAMETER_LEFT,
					VpeStyleUtil.toPxPosition(-6), HTML.STYLE_PRIORITY_DEFAULT);
			domArea.appendChild(line);
		}
		
		if (dropTargets.contains(DropTarget.AFTER)) {
			line = createVerticalLine(bounds.height, getColor(DropTarget.AFTER));
			style = VpeStyleUtil.getStyle(line);
			style.setProperty(HTML.STYLE_PARAMETER_RIGHT,
					VpeStyleUtil.toPxPosition(-6), HTML.STYLE_PRIORITY_DEFAULT);
			domArea.appendChild(line);
		}
		
		if (dropTargets.contains(DropTarget.BEGIN)) {
			line = createHorizontalLine(bounds.width - 4,
					getColor(DropTarget.BEGIN));
			style = VpeStyleUtil.getStyle(line);
			style.setProperty(HTML.STYLE_PARAMETER_LEFT,
					VpeStyleUtil.toPxPosition(2), HTML.STYLE_PRIORITY_DEFAULT);
			style.setProperty(HTML.STYLE_PARAMETER_TOP,
					VpeStyleUtil.toPxPosition(1), HTML.STYLE_PRIORITY_DEFAULT);
			domArea.appendChild(line);
		}
		
		if (dropTargets.contains(DropTarget.END)) {
			line = createHorizontalLine(bounds.width - 4,
					getColor(DropTarget.END));
			style = VpeStyleUtil.getStyle(line);
			style.setProperty(HTML.STYLE_PARAMETER_LEFT,
					VpeStyleUtil.toPxPosition(2), HTML.STYLE_PRIORITY_DEFAULT);
			style.setProperty(HTML.STYLE_PARAMETER_BOTTOM,
					VpeStyleUtil.toPxPosition(1), HTML.STYLE_PRIORITY_DEFAULT);
			domArea.appendChild(line);
		}
		
		if (oldDomArea != null) {
			contentArea.removeChild(oldDomArea);
		}
	}

	public void dispose() {
		setVisible(false);
		redraw();

		document = null;
		node = null;
		domArea = null;
	}

	private String getColor(DropTarget dropTarget) {
		if (dropTarget == highlightedDropTarget) {
			return "red";
		} else {
			return "black";
		}
	}

	private nsIDOMElement createRect(Rectangle coords, String color) {
		nsIDOMElement rect = createElement(HTML.TAG_DIV);
		
		nsIDOMCSSStyleDeclaration style = VpeStyleUtil.getStyle(rect);
		style.setProperty(HTML.STYLE_PARAMETER_POSITION, 
				HTML.STYLE_VALUE_ABSOLUTE,
				HTML.STYLE_PRIORITY_DEFAULT);
		style.setProperty(HTML.STYLE_PARAMETER_LEFT, 
				VpeStyleUtil.toPxPosition(coords.x),
				HTML.STYLE_PRIORITY_DEFAULT);
		style.setProperty(HTML.STYLE_PARAMETER_TOP, 
				VpeStyleUtil.toPxPosition(coords.y),
				HTML.STYLE_PRIORITY_DEFAULT);
		style.setProperty(HTML.STYLE_PARAMETER_HEIGHT, 
				VpeStyleUtil.toPxPosition(coords.height),
				HTML.STYLE_PRIORITY_DEFAULT);
		style.setProperty(HTML.STYLE_PARAMETER_WIDTH, 
				VpeStyleUtil.toPxPosition(coords.width),
				HTML.STYLE_PRIORITY_DEFAULT);
		style.setProperty(HTML.STYLE_PARAMETER_BACKGROUND_COLOR, 
				color,
				HTML.STYLE_PRIORITY_DEFAULT);

		return rect;
	}
	
	private void drawRect(nsIDOMElement container,
			Rectangle coords, String color) {
		container.appendChild(createRect(coords, color));
	}

	private nsIDOMElement createHorizontalLine(int width, String color) {
		nsIDOMElement line = createElement(HTML.TAG_DIV);
		nsIDOMCSSStyleDeclaration style = VpeStyleUtil.getStyle(line);
		style.setProperty(HTML.STYLE_PARAMETER_POSITION,
				HTML.STYLE_VALUE_ABSOLUTE, HTML.STYLE_PRIORITY_DEFAULT);
		style.setProperty(HTML.STYLE_PARAMETER_WIDTH,
				VpeStyleUtil.toPxPosition(width), HTML.STYLE_PRIORITY_DEFAULT);
		style.setProperty(HTML.STYLE_PARAMETER_HEIGHT,
				VpeStyleUtil.toPxPosition(6), HTML.STYLE_PRIORITY_DEFAULT);
		for (int i = 0; i < 2; i++) {
			drawRect(line, new Rectangle(i, i, 1, 6 - 2 * i), color);
		}
		drawRect(line, new Rectangle(2, 2, width - 2 * 2, 2), color);
		for (int i = 0; i < 2; i++) {
			drawRect(line, new Rectangle(width - 1 - i, i, 1, 6 - 2 * i),
					color);
		}
		
		return line;
	}
	
	private nsIDOMElement createVerticalLine(int height, String color) {
		nsIDOMElement line = createElement(HTML.TAG_DIV);
		nsIDOMCSSStyleDeclaration style = VpeStyleUtil.getStyle(line);
		style.setProperty(HTML.STYLE_PARAMETER_POSITION,
				HTML.STYLE_VALUE_ABSOLUTE, HTML.STYLE_PRIORITY_DEFAULT);
		style.setProperty(HTML.STYLE_PARAMETER_WIDTH,
				VpeStyleUtil.toPxPosition(6), HTML.STYLE_PRIORITY_DEFAULT);
		style.setProperty(HTML.STYLE_PARAMETER_HEIGHT,
				VpeStyleUtil.toPxPosition(height), HTML.STYLE_PRIORITY_DEFAULT);
		for (int i = 0; i < 2; i++) {
			drawRect(line, new Rectangle(i, i, 6 - 2 * i, 1), color);
		}
		drawRect(line, new Rectangle(2, 2, 2, height - 2 * 2), color);
		for (int i = 0; i < 2; i++) {
			drawRect(line, new Rectangle(i, height - 1 - i, 6 - 2 * i, 1),
					color);
		}
		
		return line;
	}
	
	private nsIDOMElement createElement(String tagName) {
		nsIDOMElement element = document.createElement(tagName);
		DndUtil.setTemporaryDndElement(element, true);
		return element;
	}
}
