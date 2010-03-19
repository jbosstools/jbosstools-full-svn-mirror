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
import org.jboss.tools.vpe.xulrunner.editor.XulRunnerVpeUtils;
import org.mozilla.interfaces.nsIDOMCSSStyleDeclaration;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;

/**
 * @author Yahor Radtsevich (yradtsevich)
 *
 */
public class DropableArea {
	private boolean visible;
	private EnumSet<DropSpot> dropSpots;
	private final nsIDOMDocument document;
	private nsIDOMNode node;
	private nsIDOMElement domArea;
	private static final String AREA_COLOR = "rgba(166, 202, 240, 0.5)"; //$NON-NLS-1$
	private DropSpot hightlightedSpot;
	
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
	
	/**
	 * @param dropSpots cannot be null
	 */
	public void setDropSpots(EnumSet<DropSpot> dropSpots) {
		this.dropSpots = dropSpots;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public void setHighlightedSpot(int mouseX, int mouseY) {
		this.hightlightedSpot = getHighlightedSpot(mouseX, mouseY);
	}
	
	public DropSpot getHighlightedSpot(int mouseX, int mouseY) {
		if (node == null) {
			return null;
		}
		
		Rectangle bounds = XulRunnerVpeUtils.getElementBounds(node);
		if (dropSpots.contains(DropSpot.BEFORE)
				&& bounds.x <= mouseX
				&& mouseX < bounds.x + bounds.width / 5) {
			return DropSpot.BEFORE; 
		} else if (dropSpots.contains(DropSpot.AFTER)
				&& bounds.x + bounds.width * 4 / 5 <= mouseX
				&& mouseX < bounds.x + bounds.width) {
			return DropSpot.AFTER;
		} else if (dropSpots.contains(DropSpot.BEGIN)
				&& bounds.y <= mouseY
				&& mouseY < bounds.y + bounds.height / 5) {
			return DropSpot.BEGIN;
		} else if (dropSpots.contains(DropSpot.END)
				&& bounds.y + bounds.height * 4 / 5 <= mouseY
				&& mouseY < bounds.y + bounds.height) {
			return DropSpot.END;
		} else {
			return null;
		}
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
		
		if (dropSpots.contains(DropSpot.BEFORE)) {
			line = createVerticalLine(bounds.height, getColor(DropSpot.BEFORE));
			style = VpeStyleUtil.getStyle(line);
			style.setProperty(HTML.STYLE_PARAMETER_LEFT,
					VpeStyleUtil.toPxPosition(-6), HTML.STYLE_PRIORITY_DEFAULT);
			domArea.appendChild(line);
		}
		
		if (dropSpots.contains(DropSpot.AFTER)) {
			line = createVerticalLine(bounds.height, getColor(DropSpot.AFTER));
			style = VpeStyleUtil.getStyle(line);
			style.setProperty(HTML.STYLE_PARAMETER_RIGHT,
					VpeStyleUtil.toPxPosition(-6), HTML.STYLE_PRIORITY_DEFAULT);
			domArea.appendChild(line);
		}
		
		if (dropSpots.contains(DropSpot.BEGIN)) {
			line = createHorizontalLine(bounds.width - 4,
					getColor(DropSpot.BEGIN));
			style = VpeStyleUtil.getStyle(line);
			style.setProperty(HTML.STYLE_PARAMETER_LEFT,
					VpeStyleUtil.toPxPosition(2), HTML.STYLE_PRIORITY_DEFAULT);
			style.setProperty(HTML.STYLE_PARAMETER_TOP,
					VpeStyleUtil.toPxPosition(1), HTML.STYLE_PRIORITY_DEFAULT);
			domArea.appendChild(line);
		}
		
		if (dropSpots.contains(DropSpot.END)) {
			line = createHorizontalLine(bounds.width - 4,
					getColor(DropSpot.END));
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
	
	private String getColor(DropSpot dropSpot) {
		if (dropSpot == hightlightedSpot) {
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
