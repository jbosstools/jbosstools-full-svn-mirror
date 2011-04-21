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

import org.eclipse.swt.graphics.Rectangle;
import org.jboss.tools.vpe.editor.mozilla.MozillaEditor;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.editor.util.VpeStyleUtil;
import org.jboss.tools.vpe.xulrunner.editor.XulRunnerVpeUtils;
import org.mozilla.interfaces.nsIDOMCSSStyleDeclaration;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMElementCSSInlineStyle;
import org.mozilla.interfaces.nsIDOMMouseEvent;
import org.mozilla.interfaces.nsIDOMNode;

/**
 * Class responsible for showing the drag icon and the node being dragged. 
 * 
 * @author Yahor Radtsevich (yradtsevich)
 */
public class DraggablePattern {
	private static final String DRAGGING_OPACITY = "0.5";		  //$NON-NLS-1$
	private static final String DRAG_ICON_ID = "dragIcon";		  //$NON-NLS-1$
	private static final String DRAG_ICON_FILE = "dragIcon.gif";  //$NON-NLS-1$
	private static final String DEFAULT_DISPLAY = "";			  //$NON-NLS-1$
	private static final String NONE_DISPLAY = "none";			  //$NON-NLS-1$
	private static final String ABSOLUTE_POSITION = "absolute";	  //$NON-NLS-1$
	private static final String IMPORTANT_PRIORITY = "important"; //$NON-NLS-1$
	private static final String DEFAULT_PRIORITY = "";			  //$NON-NLS-1$
	private static final String DRAG_ICON_STYLE
			= "display:none; position: absolute; cursor: move";	  //$NON-NLS-1$

	private int offsetX;
	private int offsetY;
	private boolean sessionStarted;
	private nsIDOMNode nodeCopy;
	private nsIDOMNode node;
	private nsIDOMCSSStyleDeclaration nodeCopyStyle;
	private final MozillaEditor mozillaEditor;

	public DraggablePattern(MozillaEditor mozillaEditor) {
		sessionStarted = false;
		this.mozillaEditor = mozillaEditor;
	}

	public void showDragIcon(nsIDOMNode node) {
		this.node = node;
		Rectangle bounds = XulRunnerVpeUtils.getElementBounds(node);
		DragIcon dragIcon = getDragIcon();
		dragIcon.setStyleProperty(HTML.STYLE_PARAMETER_DISPLAY,
				DEFAULT_DISPLAY, DEFAULT_PRIORITY);
		dragIcon.setStyleProperty(HTML.STYLE_PARAMETER_LEFT,
				VpeStyleUtil.toPxPosition(bounds.x), DEFAULT_PRIORITY);
		dragIcon.setStyleProperty(HTML.STYLE_PARAMETER_TOP,
				VpeStyleUtil.toPxPosition(bounds.y - 20), DEFAULT_PRIORITY);
	}

	public void hideDragIcon() {
		this.node = null;
		DragIcon dragIcon = getDragIcon();
		dragIcon.setStyleProperty(HTML.STYLE_PARAMETER_DISPLAY,
				NONE_DISPLAY, DEFAULT_PRIORITY);
	}

	public DragIcon getDragIcon() {
		nsIDOMElement dragIconElement = mozillaEditor.getDomDocument()
				.getElementById(DRAG_ICON_ID);
		if (dragIconElement == null) {
			dragIconElement = mozillaEditor.getDomDocument()
					.createElement(HTML.TAG_IMG);
			DndUtil.setTemporaryDndElement(dragIconElement, true);
			mozillaEditor.getDomDocument().getElementsByTagName(HTML.TAG_BODY)
					.item(0).appendChild(dragIconElement);
			dragIconElement.setAttribute(HTML.ATTR_ID, DRAG_ICON_ID);
			dragIconElement.setAttribute(HTML.ATTR_SRC,
					VpeStyleUtil.getAbsoluteResourcePathUrl(DRAG_ICON_FILE));
			dragIconElement.setAttribute(HTML.ATTR_STYLE, DRAG_ICON_STYLE);
		}
		return new DragIcon(dragIconElement); 
	}

	public boolean isDragIconClicked(nsIDOMMouseEvent mouseEvent) {
		nsIDOMNode targetNode = (nsIDOMNode) mouseEvent.getTarget()
				.queryInterface(nsIDOMNode.NS_IDOMNODE_IID);
		return getDragIcon().getElement().equals(targetNode);
	}

	public nsIDOMNode getNode() {
		return node;
	}

	public void startSession(int mouseStartX, int mouseStartY) {
		if (sessionStarted) {
			new IllegalStateException(
					"Session is already started."); //$NON-NLS-1$
		}
		if (node == null) {
			new IllegalStateException(
			"No node to drag."); //$NON-NLS-1$
		}

		Rectangle nodeBounds = XulRunnerVpeUtils.getElementBounds(node);
		offsetX = nodeBounds.x - mouseStartX;
		offsetY = nodeBounds.y - mouseStartY;
		
		nodeCopy = node.cloneNode(true);
		if (nodeCopy.getNodeType() == nsIDOMNode.ELEMENT_NODE) {
			nsIDOMElement elementCopy = (nsIDOMElement) nodeCopy.queryInterface(
					nsIDOMElement.NS_IDOMELEMENT_IID);
			DndUtil.setTemporaryDndElement(elementCopy, true);
		}
		nodeCopyStyle = ((nsIDOMElementCSSInlineStyle)
				nodeCopy.queryInterface(
						nsIDOMElementCSSInlineStyle
								.NS_IDOMELEMENTCSSINLINESTYLE_IID)).getStyle();

		nodeCopyStyle.setProperty(HTML.STYLE_PARAMETER_POSITION,
				ABSOLUTE_POSITION, IMPORTANT_PRIORITY);
		nodeCopyStyle.setProperty(HTML.STYLE_PARAMETER_OPACITY,
				DRAGGING_OPACITY, IMPORTANT_PRIORITY);
		setVisible(false);
		node.getParentNode().appendChild(nodeCopy);
		move(mouseStartX, mouseStartY);
		setVisible(true);
		sessionStarted = true;
	}

	public void closeSession() {
		if (!sessionStarted) {
			new IllegalStateException(
					"Session is already closed."); //$NON-NLS-1$
		}
		
		nsIDOMNode parent = nodeCopy.getParentNode();
		if (parent != null) {
			parent.removeChild(nodeCopy);
		}
		
		hideDragIcon();
		nodeCopy = null;
		nodeCopyStyle = null;
		sessionStarted = false;
	}

	public void setVisible(boolean visible) {
		nodeCopyStyle.setProperty(HTML.STYLE_PARAMETER_DISPLAY,
				visible ? DEFAULT_DISPLAY : NONE_DISPLAY, IMPORTANT_PRIORITY);
		DragIcon dragIcon = getDragIcon();
		dragIcon.setStyleProperty(HTML.STYLE_PARAMETER_DISPLAY,
				visible ? DEFAULT_DISPLAY : NONE_DISPLAY, DEFAULT_PRIORITY);
	}

	public void move(int mouseX, int mouseY) {
		nodeCopyStyle.setProperty(HTML.STYLE_PARAMETER_LEFT,
				VpeStyleUtil.toPxPosition(offsetX + mouseX),
				IMPORTANT_PRIORITY);
		nodeCopyStyle.setProperty(HTML.STYLE_PARAMETER_TOP,
				VpeStyleUtil.toPxPosition(offsetY + mouseY),
				IMPORTANT_PRIORITY);

		DragIcon dragIcon = getDragIcon();
		dragIcon.setStyleProperty(HTML.STYLE_PARAMETER_LEFT,
				VpeStyleUtil.toPxPosition(offsetX + mouseX),
				DEFAULT_PRIORITY);
		dragIcon.setStyleProperty(HTML.STYLE_PARAMETER_TOP,
				VpeStyleUtil.toPxPosition(offsetY + mouseY - 20),
				DEFAULT_PRIORITY);
	}

	public class DragIcon {
		private final nsIDOMElement element;
		private final nsIDOMCSSStyleDeclaration style;

		public DragIcon(nsIDOMElement element) {
			this.element = element;
			style = VpeStyleUtil.getStyle(element);
		}

		public nsIDOMElement getElement() {
			return element;
		}

		public nsIDOMCSSStyleDeclaration getStyle() {
			return style;
		}

		public void setStyleProperty(String propertyName, String value,
				String priority) {
			style.setProperty(propertyName, value, priority);
		}
	}
}
