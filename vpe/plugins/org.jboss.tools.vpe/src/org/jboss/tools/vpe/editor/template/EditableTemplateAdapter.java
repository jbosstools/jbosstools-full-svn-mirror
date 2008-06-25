/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.template;

import java.util.List;

import org.eclipse.swt.graphics.Point;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.mapping.VpeAttributeData;
import org.jboss.tools.vpe.editor.mapping.VpeElementData;
import org.jboss.tools.vpe.editor.mapping.VpeElementMapping;
import org.jboss.tools.vpe.editor.selection.VpeSelectionController;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.editor.util.TemplateManagingUtil;
import org.jboss.tools.vpe.editor.util.TextUtil;
import org.jboss.tools.vpe.editor.util.VisualDomUtil;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMKeyEvent;
import org.mozilla.interfaces.nsIDOMMouseEvent;
import org.mozilla.interfaces.nsIDOMNSUIEvent;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsISelection;
import org.mozilla.interfaces.nsISelectionController;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Abstract class to handle keyEvent.
 * 
 * Default implementation of ITemplateKeyEventHandler. In result of work
 * handleKeyPress() call one from handle* methods.
 * 
 * You must override some handle* methods if you want change work of your
 * handler *
 * 
 * Default implementation of ITemplateNodesManager.
 * 
 * @author Sergey Dzmitrovich
 * 
 */
public abstract class EditableTemplateAdapter extends VpeAbstractTemplate
		implements ITemplateKeyEventHandler, ITemplateNodesManager,
		ITemplateSelectionManager {

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplateKeyEventHandler#handleKeyPress(org.jboss.tools.vpe.editor.context.VpePageContext,
	 *      org.mozilla.interfaces.nsIDOMKeyEvent)
	 */
	final public boolean handleKeyPress(VpePageContext pageContext,
			nsIDOMKeyEvent keyEvent) {

		long keyCode = keyEvent.getKeyCode();

		if (keyCode == nsIDOMKeyEvent.DOM_VK_ENTER) {
			return handleEnter(pageContext, keyEvent);

		} else if ((keyCode == nsIDOMKeyEvent.DOM_VK_LEFT)
				&& (!keyEvent.getShiftKey())) {
			return handleLeft(pageContext, keyEvent);

		} else if ((keyCode == nsIDOMKeyEvent.DOM_VK_UP)
				&& (!keyEvent.getShiftKey())) {
			return handleUp(pageContext, keyEvent);

		} else if ((keyCode == nsIDOMKeyEvent.DOM_VK_RIGHT)
				&& (!keyEvent.getShiftKey())) {
			return handleRight(pageContext, keyEvent);

		} else if ((keyCode == nsIDOMKeyEvent.DOM_VK_DOWN)
				&& (!keyEvent.getShiftKey())) {
			return handleDown(pageContext, keyEvent);

		} else if ((keyCode == nsIDOMKeyEvent.DOM_VK_HOME)
				&& (!keyEvent.getShiftKey())) {
			return handleHome(pageContext, keyEvent);

		} else if ((keyCode == nsIDOMKeyEvent.DOM_VK_END)
				&& (!keyEvent.getShiftKey())) {
			return handleEnd(pageContext, keyEvent);

		} else if ((keyCode == nsIDOMKeyEvent.DOM_VK_BACK_SPACE)
				&& (!keyEvent.getShiftKey())) {
			return handleLeftDelete(pageContext, keyEvent);

		} else if ((keyCode == nsIDOMKeyEvent.DOM_VK_DELETE)
				&& (!keyEvent.getShiftKey())) {
			return handleRightDelete(pageContext, keyEvent);

		} else if ((keyCode == nsIDOMKeyEvent.DOM_VK_PAGE_UP)
				&& (!keyEvent.getShiftKey())) {
			return handlePageUp(pageContext, keyEvent);

		} else if (keyEvent.getCharCode() != 0) {
			return handleCharacter(pageContext, keyEvent);

		} else if ((keyEvent.getKeyCode() == nsIDOMKeyEvent.DOM_VK_INSERT)
				&& keyEvent.getShiftKey()) {
			return handleInsert(pageContext, keyEvent);
		}

		return false;
	}

	/**
	 * Default handling of a pressing the "insert" event - always return false.
	 * 
	 * @param pageContext -
	 *            context of vpe
	 * @param keyEvent -
	 *            event
	 * @return whether handled event
	 */
	protected boolean handleInsert(VpePageContext pageContext,
			nsIDOMKeyEvent keyEvent) {
		return false;
	}

	/**
	 * Default handling of a pressing a character event
	 * 
	 * 
	 * @param pageContext -
	 *            context of vpe
	 * @param keyEvent -
	 *            event
	 * @return whether handled event
	 */
	protected boolean handleCharacter(VpePageContext pageContext,
			nsIDOMKeyEvent keyEvent) {

		// get visual node which is focused
		nsIDOMNode visualNode = getCurrentSelectedVisualNode(pageContext);

		VpeElementMapping elementMapping = getElmentMapping(pageContext,
				visualNode);

		if (elementMapping == null || elementMapping.getElementData() == null) {
			return false;
		}

		VpeElementData elementData = elementMapping.getElementData();

		// if node editable
		if (isNodeEditable(visualNode, elementData)) {

			VpeAttributeData attributeData = getAttributeData(visualNode,
					elementMapping.getElementData());

			Node node;
			Point selectedRange;
			// get source node
			if ((attributeData.getSourceAttr() == null)
					&& (attributeData.getAttributeName() != null)) {

				node = createAttribute(
						(Element) elementMapping.getSourceNode(), attributeData
								.getAttributeName(), EMPTY_STRING);
				selectedRange = new Point(0, 0);
			} else {

				node = getTargetSourceNodeByVisualNode(pageContext, visualNode,
						elementMapping);
				selectedRange = getSelectionRange(TemplateManagingUtil
						.getCurrentSelection(pageContext));
			}

			if (node == null || selectedRange == null)
				return false;

			// get focus and anchor offsets
			int focusOffset = selectedRange.x;
			int anchorOffset = selectedRange.x + selectedRange.y;

			// initialization offset and length selected string
			int startOffset = 0;
			int length = 0;

			// set start offset and length selected string
			if (focusOffset < anchorOffset) {
				startOffset = focusOffset;
				length = anchorOffset - focusOffset;
			} else {
				startOffset = anchorOffset;
				length = focusOffset - anchorOffset;
			}

			// get inserted string
			long charCode = keyEvent.getCharCode();
			char[] s = new char[1];
			s[0] = (char) charCode;
			String str = new String(s);
			if (TextUtil.containsKey(s[0])) {
				str = TextUtil.getValue(s[0]);
			}

			// get value
			String oldValue = node.getNodeValue();

			// create new value
			String newValue = oldValue.substring(0, startOffset) + str
					+ oldValue.substring(startOffset + length);

			node.setNodeValue(newValue);

			// set selection
			TemplateManagingUtil.setSourceSelection(pageContext, node,
					startOffset + 1, 0);

		}
		return true;
	}

	/**
	 * Default handling of a pressing the "page up" event - always return false.
	 * 
	 * Override this method for a handling of a pressing the "page up" event
	 * 
	 * @param pageContext -
	 *            context of vpe
	 * @param keyEvent -
	 *            event
	 * @return whether handled event
	 */
	protected boolean handlePageUp(VpePageContext pageContext,
			nsIDOMKeyEvent keyEvent) {
		return false;
	}

	/**
	 * Default implementation of a handling of a pressing the "delete" event
	 * 
	 * Override this method for a handling of a pressing the "delete" event
	 * 
	 * @param pageContext -
	 *            context of vpe
	 * @param keyEvent -
	 *            event
	 * @return whether handled event
	 */
	protected boolean handleRightDelete(VpePageContext pageContext,
			nsIDOMKeyEvent keyEvent) {

		// get visual node which is focused
		nsIDOMNode visualNode = getCurrentSelectedVisualNode(pageContext);

		VpeElementMapping elementMapping = getElmentMapping(pageContext,
				visualNode);

		if (elementMapping == null || elementMapping.getElementData() == null) {
			return false;
		}

		VpeElementData elementData = elementMapping.getElementData();

		// if node editable
		if (isNodeEditable(visualNode, elementData)) {

			VpeAttributeData attributeData = getAttributeData(visualNode,
					elementMapping.getElementData());

			Node node;
			Point selectedRange;
			// get source node
			if ((attributeData.getSourceAttr() == null)
					&& (attributeData.getAttributeName() != null)) {

				node = createAttribute(
						(Element) elementMapping.getSourceNode(), attributeData
								.getAttributeName(), EMPTY_STRING);
				selectedRange = new Point(0, 0);
			} else {

				node = getTargetSourceNodeByVisualNode(pageContext, visualNode,
						elementMapping);
				selectedRange = getSelectionRange(TemplateManagingUtil
						.getCurrentSelection(pageContext));
			}

			if (node == null || selectedRange == null)
				return false;

			// get focus and anchor offsets

			int focusOffset = selectedRange.x;
			int anchorOffset = selectedRange.x + selectedRange.y;

			// initialization offset and length selected string
			int startOffset = 0;
			int length = 0;

			// set start offset and length selected string
			// if text was not selected then will be deleted next character
			if (focusOffset == anchorOffset) {

				// if offset is end of text will do nothing
				if (focusOffset == node.getNodeValue().length()) {
					TemplateManagingUtil.setSourceSelection(pageContext, node,
							focusOffset, 0);
					return true;
				}

				startOffset = focusOffset;
				length = 1;
			}
			// if some text was selected
			else if (focusOffset < anchorOffset) {
				startOffset = focusOffset;
				length = anchorOffset - focusOffset;
			} else {
				startOffset = anchorOffset;
				length = focusOffset - anchorOffset;
			}

			// get old value
			String oldValue = node.getNodeValue();

			// create new value
			String newValue = oldValue.substring(0, startOffset)
					+ oldValue.substring(startOffset + length);

			// set new value
			node.setNodeValue(newValue);

			// set new selection
			TemplateManagingUtil.setSourceSelection(pageContext, node,
					startOffset, 0);

		}

		return true;
	}

	/**
	 * Default handling of a pressing the "backspace" event
	 * 
	 * Override this method to handle the "backspace" event
	 * 
	 * @param pageContext -
	 *            context of vpe
	 * @param keyEvent -
	 *            event
	 * @return whether handled event
	 */
	protected boolean handleLeftDelete(VpePageContext pageContext,
			nsIDOMKeyEvent keyEvent) {

		// get visual node which is focused
		nsIDOMNode visualNode = getCurrentSelectedVisualNode(pageContext);

		VpeElementMapping elementMapping = getElmentMapping(pageContext,
				visualNode);

		if (elementMapping == null || elementMapping.getElementData() == null) {
			return false;
		}

		VpeElementData elementData = elementMapping.getElementData();

		// if node editable
		if (isNodeEditable(visualNode, elementData)) {

			VpeAttributeData attributeData = getAttributeData(visualNode,
					elementMapping.getElementData());

			Node node;
			Point selectedRange;
			// get source node
			if ((attributeData.getSourceAttr() == null)
					&& (attributeData.getAttributeName() != null)) {

				node = createAttribute(
						(Element) elementMapping.getSourceNode(), attributeData
								.getAttributeName(), EMPTY_STRING);
				selectedRange = new Point(0, 0);
			} else {

				node = getTargetSourceNodeByVisualNode(pageContext, visualNode,
						elementMapping);
				selectedRange = getSelectionRange(TemplateManagingUtil
						.getCurrentSelection(pageContext));
			}
			if (node == null || selectedRange == null)
				return false;

			// get focus and anchor offsets

			int focusOffset = selectedRange.x;
			int anchorOffset = selectedRange.x + selectedRange.y;

			// initialization offset and length selected string
			int startOffset = 0;
			int length = 0;

			// set start offset and length selected string
			// if text was not selected then will be deleted previous character
			if (focusOffset == anchorOffset) {
				// if offset is start of text then will do nothing
				if (focusOffset == 0) {

					TemplateManagingUtil.setSourceSelection(pageContext, node,
							0, 0);
					return true;
				}
				// set start offset to previous character
				startOffset = focusOffset - 1;
				length = 1;
			}
			// if some text was selected
			else if (focusOffset < anchorOffset) {
				startOffset = focusOffset;
				length = anchorOffset - focusOffset;
			} else {
				startOffset = anchorOffset;
				length = focusOffset - anchorOffset;
			}

			// get old value
			String oldValue = node.getNodeValue();

			// create new value
			String newValue = oldValue.substring(0, startOffset)
					+ oldValue.substring(startOffset + length);

			// set new value
			node.setNodeValue(newValue);

			// set new selection
			TemplateManagingUtil.setSourceSelection(pageContext, node,
					startOffset, 0);

		}

		return true;
	}

	/**
	 * Default handling of a pressing the "end" event - always return false.
	 * 
	 * Override this method to handle of a pressing the "end" event
	 * 
	 * @param pageContext -
	 *            context of vpe
	 * @param keyEvent -
	 *            event
	 * @return whether handled event
	 */
	protected boolean handleEnd(VpePageContext pageContext,
			nsIDOMKeyEvent keyEvent) {
		return false;
	}

	/**
	 * Default handling of a pressing the "home" event - always return false.
	 * 
	 * Override this method to handle of a pressing the "home" event
	 * 
	 * @param pageContext -
	 *            context of vpe
	 * @param keyEvent -
	 *            event
	 * @return whether handled event
	 */
	protected boolean handleHome(VpePageContext pageContext,
			nsIDOMKeyEvent keyEvent) {
		return false;
	}

	/**
	 * Default handling of a pressing the "down" event - always return false.
	 * 
	 * Override this method to handle of a pressing the "down" event
	 * 
	 * @param pageContext -
	 *            context of vpe
	 * @param keyEvent -
	 *            event
	 * @return whether handled event
	 */
	protected boolean handleDown(VpePageContext pageContext,
			nsIDOMKeyEvent keyEvent) {
		return false;
	}

	/**
	 * Default handling of a pressing the "right" event - always return false.
	 * 
	 * Override this method to handle of a pressing the "right" event
	 * 
	 * @param pageContext -
	 *            context of vpe
	 * @param keyEvent -
	 *            event
	 * @return whether handled event
	 */
	protected boolean handleRight(VpePageContext pageContext,
			nsIDOMKeyEvent keyEvent) {

		// get visual node which is focused
		nsIDOMNode visualNode = getCurrentSelectedVisualNode(pageContext);

		VpeElementMapping elementMapping = getElmentMapping(pageContext,
				visualNode);

		if (elementMapping == null || elementMapping.getElementData() == null) {
			return false;
		}

		VpeElementData elementData = elementMapping.getElementData();

		// get source node
		Node node = getTargetSourceNodeByVisualNode(pageContext, visualNode,
				elementMapping);

		// get focus and anchor offsets
		Point selectedRange = getSelectionRange(TemplateManagingUtil
				.getCurrentSelection(pageContext));

		if (node == null || selectedRange == null)
			return false;
		
		int focusOffset = selectedRange.x;

		// if node editable
		if (isNodeEditable(visualNode, elementData)) {

			if (focusOffset != node.getNodeValue().length()) {
				TemplateManagingUtil.setSourceSelection(pageContext, node,
						focusOffset + 1, 0);
			} else
				TemplateManagingUtil.setSourceSelection(pageContext, node,
						focusOffset, 0);
		}
		return true;

	}

	/**
	 * Default handling of a pressing the "up" event - always return false.
	 * 
	 * Override this method to handle of a pressing the "up" event
	 * 
	 * @param pageContext -
	 *            context of vpe
	 * @param keyEvent -
	 *            event
	 * @return whether handled event
	 */
	protected boolean handleUp(VpePageContext pageContext,
			nsIDOMKeyEvent keyEvent) {
		return false;
	}

	/**
	 * Default handling of a pressing the "left" event - always return false.
	 * 
	 * Override this method to handle of a pressing the "left" event
	 * 
	 * @param pageContext -
	 *            context of vpe
	 * @param keyEvent -
	 *            event
	 * @return whether handled event
	 */
	protected boolean handleLeft(VpePageContext pageContext,
			nsIDOMKeyEvent keyEvent) {

		// get visual node which is focused
		nsIDOMNode visualNode = getCurrentSelectedVisualNode(pageContext);

		VpeElementMapping elementMapping = getElmentMapping(pageContext,
				visualNode);

		if (elementMapping == null || elementMapping.getElementData() == null) {
			return false;
		}

		VpeElementData elementData = elementMapping.getElementData();

		// if node editable
		if (isNodeEditable(visualNode, elementData)) {

			// get source node
			Node node = getTargetSourceNodeByVisualNode(pageContext,
					visualNode, elementMapping);

			// get focus and anchor offsets
			Point selectedRange = getSelectionRange(TemplateManagingUtil
					.getCurrentSelection(pageContext));

			if (node == null || selectedRange == null)
				return false;
			
			int focusOffset = selectedRange.x;

			if (focusOffset != 0) {
				TemplateManagingUtil.setSourceSelection(pageContext, node,
						focusOffset - 1, 0);
			} else {
				TemplateManagingUtil
						.setSourceSelection(pageContext, node, 0, 0);
			}
		}
		return true;
	}

	/**
	 * Default handling of a pressing the "enter" event - always return false.
	 * 
	 * Override to handling of a pressing the "enter" event
	 * 
	 * @param pageContext -
	 *            context of vpe
	 * @param keyEvent -
	 *            event
	 * @return whether handled event
	 */
	protected boolean handleEnter(VpePageContext pageContext,
			nsIDOMKeyEvent keyEvent) {
		return true;
	}

	/**
	 * 
	 * @param pageContext
	 * @param visualNode
	 * @param elementData
	 * @return
	 */
	public boolean isNodeEditable(nsIDOMNode visualNode,
			VpeElementData elementData) {

		VpeAttributeData attributeData = getAttributeData(visualNode,
				elementData);

		if (attributeData != null)
			return attributeData.isEditable();

		return false;
	}

	/**
	 * 
	 */
	public boolean isNodeEditable(Node node, VpeElementData elementData) {

		VpeAttributeData attributeData = getAttributeData(node, elementData);

		if (attributeData != null) {
			return attributeData.isEditable();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.vpe.editor.template.ITemplateSelectionManager#setSelection(org.jboss.tools.vpe.editor.context.VpePageContext,
	 *      org.mozilla.interfaces.nsISelection)
	 */
	final public void setSelection(VpePageContext pageContext,
			nsISelection selection) {

		nsIDOMNode selectedVisualNode = TemplateManagingUtil
				.getSelectedNode(selection);

		if (selectedVisualNode == null)
			return;

		VpeElementMapping elementMapping = getElmentMapping(pageContext,
				selectedVisualNode);

		// get target visual node
		nsIDOMNode targetVisualNode = getTargetVisualNodeByVisualNode(
				pageContext, selectedVisualNode, elementMapping);

		// get target souce node
		Node targetSourceNode = getTargetSourceNodeByVisualNode(pageContext,
				selectedVisualNode, elementMapping);

		int focusOffset;
		int length;

		if (isNodeEditable(targetVisualNode, elementMapping.getElementData())) {

			Point range = getSelectionRange(selection);

			focusOffset = range.x;
			length = range.y;

		} else {

			focusOffset = 0;
			length = TemplateManagingUtil.getLengthNode(targetSourceNode);

		}

		// set source selection
		TemplateManagingUtil.setSourceSelection(pageContext, targetSourceNode,
				focusOffset, length);

		if ((HTML.TAG_INPUT.equalsIgnoreCase(targetVisualNode.getLocalName()))
				|| (HTML.TAG_TEXTAREA.equalsIgnoreCase(targetVisualNode
						.getLocalName()))) {

			TemplateManagingUtil.setSelectionRangeInInputElement(
					targetVisualNode, new Point(focusOffset, length));

			selection.collapse(targetVisualNode.getParentNode(),
					(int) VisualDomUtil.getOffset(targetVisualNode));

			selection.extend(targetVisualNode.getParentNode(),
					(int) VisualDomUtil.getOffset(targetVisualNode) + 1);
		}

		// setSelectionRange(selection,
		// targetVisualNode, new Point(focusOffset, length));

		// check for text node
		if (targetVisualNode.getNodeType() != nsIDOMNode.ELEMENT_NODE)
			targetVisualNode = targetVisualNode.getParentNode();

		// paint visual selection
		pageContext.getVisualBuilder().setSelectionRectangle(
				(nsIDOMElement) targetVisualNode
						.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.vpe.editor.template.ITemplateSelectionManager#setSelectionBySource(org.jboss.tools.vpe.editor.context.VpePageContext,
	 *      org.jboss.tools.vpe.editor.selection.VpeSelectionController, int,
	 *      int)
	 */
	final public void setSelectionBySource(VpePageContext pageContext,
			VpeSelectionController selectionController, int focus, int anchor) {

		// get element mapping
		VpeElementMapping elementMapping = TemplateManagingUtil
				.getElementMappingBySourceSelection(pageContext, focus, anchor);

		// get focused attribute
		Node focusNode = getTargetSourceNodeBySourcePosition(pageContext,
				focus, anchor);

		int visualFocus = 0;
		int visualAnchor = 0;

		if (isNodeEditable(focusNode, elementMapping.getElementData())) {

			String text = focusNode.getNodeValue();
			int start = TemplateManagingUtil.getStartOffsetNode(focusNode);
			focus = focus - start;
			anchor = anchor - start;
			visualFocus = TextUtil.visualInnerPosition(text, focus);
			visualAnchor = TextUtil.visualInnerPosition(text, anchor);

		}

		nsIDOMNode visualNode = getTargetVisualNodeBySourceNode(focusNode,
				elementMapping);

		if (visualNode == null)
			return;

		setSelectionRange(selectionController
				.getSelection(nsISelectionController.SELECTION_NORMAL),
				visualNode, new Point(visualFocus, visualAnchor - visualFocus));

		// check for text node
		if (visualNode.getNodeType() != nsIDOMNode.ELEMENT_NODE) {
			visualNode = visualNode.getParentNode();
		}

		pageContext.getVisualBuilder().setSelectionRectangle(
				(nsIDOMElement) visualNode
						.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.vpe.editor.template.ITemplateSelectionManager#setSelectionByMouse(org.mozilla.interfaces.nsIDOMMouseEvent)
	 */
	final public void setSelectionByMouse(VpePageContext pageContext,
			VpeSelectionController selectionController,
			nsIDOMMouseEvent mouseEvent) {

		// get visual node by event
		nsIDOMNode visualNode = VisualDomUtil.getTargetNode(mouseEvent);

		// get element mapping
		VpeElementMapping elementMapping = getElmentMapping(pageContext,
				visualNode);

		if (elementMapping == null)
			return;

		Node targetSourceNode = getTargetSourceNodeByVisualNode(pageContext,
				visualNode, elementMapping);

		nsIDOMNode targetVisualNode = getTargetVisualNodeByVisualNode(
				pageContext, visualNode, elementMapping);

		// get nsIDOMNSUIEvent event
		nsIDOMNSUIEvent nsuiEvent = (nsIDOMNSUIEvent) mouseEvent
				.queryInterface(nsIDOMNSUIEvent.NS_IDOMNSUIEVENT_IID);

		int selectionOffset;
		int selectionLength;

		if (isNodeEditable(targetVisualNode, elementMapping.getElementData())) {
			selectionOffset = nsuiEvent.getRangeOffset();
			selectionLength = 0;
		} else {

			selectionOffset = 0;
			selectionLength = TemplateManagingUtil
					.getLengthNode(targetSourceNode);

		}

		TemplateManagingUtil.setSourceSelection(pageContext, targetSourceNode,
				selectionOffset, selectionLength);

		// setSelectionRange(selectionController
		// .getSelection(nsISelectionController.SELECTION_NORMAL),
		// targetVisualNode, new Point(selectionOffset, selectionLength));

		if ((HTML.TAG_INPUT.equalsIgnoreCase(targetVisualNode.getLocalName()))
				|| (HTML.TAG_TEXTAREA.equalsIgnoreCase(targetVisualNode
						.getLocalName()))) {

			TemplateManagingUtil.setSelectionRangeInInputElement(
					targetVisualNode, new Point(selectionOffset,
							selectionLength));

			 (selectionController.getSelection(nsISelectionController.SELECTION_NORMAL)).collapse(targetVisualNode.getParentNode(),(int)
			 VisualDomUtil.getOffset(targetVisualNode));

			 (selectionController
					.getSelection(nsISelectionController.SELECTION_NORMAL))
					.extend(targetVisualNode.getParentNode(),
							(int) VisualDomUtil.getOffset(targetVisualNode) + 1);
		}

		// check for text node
		if (targetVisualNode.getNodeType() != nsIDOMNode.ELEMENT_NODE) {
			targetVisualNode = targetVisualNode.getParentNode();
		}

		// paint selection rectangle
		pageContext.getVisualBuilder().setSelectionRectangle(
				(nsIDOMElement) targetVisualNode
						.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID));

	}

	/**
	 * 
	 */
	public boolean openBundle(VpePageContext pageContext,
			nsIDOMNode visualNode, VpeElementMapping elementMapping) {

		VpeAttributeData attributeData = getAttributeData(
				getTargetVisualNodeByVisualNode(pageContext, visualNode,
						elementMapping), elementMapping.getElementData());

		// so as nsIDOMMouseEvent doesn't give simple selected nsIDOMText as
		// target, but nsiSelection can give simple "text"
		// TODO may be, there is a better way to get selected simple nsIDOMText
		if (attributeData == null) {

			// get visual node which is focused
			nsIDOMNode tempNode = getCurrentSelectedVisualNode(pageContext);
			attributeData = getAttributeData(getTargetVisualNodeByVisualNode(
					pageContext, tempNode, elementMapping), elementMapping
					.getElementData());

		}

		if ((attributeData == null) || (attributeData.getSourceAttr() == null))
			return false;

		return pageContext.getBundle().openBundle(
				attributeData.getSourceAttr().getNodeValue(),
				TemplateManagingUtil.getPageLocale(pageContext, attributeData
						.getSourceAttr()));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.vpe.editor.template.ITemplateNodesManager#getTargetSourceNodeBySourcePosition(org.jboss.tools.vpe.editor.context.VpePageContext,
	 *      int, int)
	 */
	public Node getTargetSourceNodeBySourcePosition(VpePageContext pageContext,
			int focusPosition, int anchorPosition) {

		// get element mapping by position
		VpeElementMapping elementMapping = TemplateManagingUtil
				.getElementMappingBySourceSelection(pageContext, focusPosition,
						anchorPosition);

		// find focus attribute by position
		Node focusAttribute = findElementAttributeByPosition(elementMapping
				.getElementData(), focusPosition);

		// fond anchor attribute by position
		Node anchorAttribute = findElementAttributeByPosition(elementMapping
				.getElementData(), anchorPosition);

		// if anchor and focus attributes are equal return focused attribute
		if (focusAttribute == anchorAttribute)
			return focusAttribute;

		// else return all element
		return elementMapping.getSourceNode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.vpe.editor.template.ITemplateNodesManager#getTargetVisualNodeBySourceNode(org.w3c.dom.Node,
	 *      org.jboss.tools.vpe.editor.mapping.VpeElementMapping)
	 */
	public nsIDOMNode getTargetVisualNodeBySourceNode(Node sourceNode,
			VpeElementMapping elementMapping) {

		// if element is not null
		if (elementMapping != null) {

			// get attributeData
			VpeAttributeData attributeData = getAttributeData(sourceNode,
					elementMapping.getElementData());

			// attributeData is found
			if (attributeData != null)
				return attributeData.getVisualAttr();
			else
				return elementMapping.getVisualNode();

		}

		return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.vpe.editor.template.ITemplateNodesManager#getTargetVisualNodeByVisualNode(org.mozilla.interfaces.nsIDOMNode,
	 *      org.jboss.tools.vpe.editor.mapping.VpeElementMapping)
	 */
	public nsIDOMNode getTargetVisualNodeByVisualNode(
			VpePageContext pageContext, nsIDOMNode visualNode,
			VpeElementMapping elementMapping) {

		// if element is not null
		if (elementMapping != null) {

			// get attributeData
			VpeAttributeData attributeData = getAttributeData(visualNode,
					elementMapping.getElementData());

			// attributeData is found
			if (attributeData != null)
				return attributeData.getVisualAttr();
			else
				return elementMapping.getVisualNode();

		}

		return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.vpe.editor.template.ITemplateNodesManager#getTargetSourceNodeByVisualNode(org.mozilla.interfaces.nsIDOMNode,
	 *      org.jboss.tools.vpe.editor.mapping.VpeElementMapping)
	 */
	public Node getTargetSourceNodeByVisualNode(VpePageContext pageContext,
			nsIDOMNode visualNode, VpeElementMapping elementMapping) {

		// if element is not null
		if (elementMapping != null) {

			// get attributeData
			VpeAttributeData attributeData = getAttributeData(visualNode,
					elementMapping.getElementData());

			// attributeData is found
			if ((attributeData != null)
					&& (attributeData.getSourceAttr() != null))
				return attributeData.getSourceAttr();
			else
				return elementMapping.getSourceNode();

		}

		return null;

	}

	/**
	 * 
	 * @param elementData
	 * @param offset
	 * @return
	 */
	private Node findElementAttributeByPosition(VpeElementData elementData,
			int position) {

		// if data are correct
		if ((elementData != null) && (elementData.getAttributesData() != null)) {

			List<VpeAttributeData> attributesMapping = elementData
					.getAttributesData();

			// for each defined attribute
			for (VpeAttributeData attributeData : attributesMapping) {

				// if position is in attribute's bound
				if ((position >= (TemplateManagingUtil
						.getStartOffsetNode(attributeData.getSourceAttr())))
						&& (position <= (TemplateManagingUtil
								.getEndOffsetNode(attributeData.getSourceAttr()))))
					return attributeData.getSourceAttr();
			}
		}

		return null;
	}

	/**
	 * 
	 * @param pageContext
	 * @param visualNode
	 * @param elementData
	 * @return
	 */
	protected VpeAttributeData getAttributeData(nsIDOMNode visualNode,
			VpeElementData elementData) {

		// if input data is correct
		if ((visualNode != null) && (elementData != null)
				&& (elementData.getAttributesData() != null)) {

			List<VpeAttributeData> attributesMapping = elementData
					.getAttributesData();

			for (VpeAttributeData attributeData : attributesMapping) {

				// if visual nodes equals
				if (visualNode.equals(attributeData.getVisualAttr()))
					return attributeData;
			}
		}

		return null;

	}

	/**
	 * 
	 * @param pageContext
	 * @param node
	 * @param elementData
	 * @return
	 */
	protected VpeAttributeData getAttributeData(Node node,
			VpeElementData elementData) {

		// if input data is correct
		if ((node != null) && (elementData != null)
				&& (elementData.getAttributesData() != null)) {

			List<VpeAttributeData> attributesMapping = elementData
					.getAttributesData();

			for (VpeAttributeData attributeData : attributesMapping) {

				// if source nodes equals
				if (node.equals(attributeData.getSourceAttr()))
					return attributeData;
			}
		}
		return null;

	}

	/**
	 * 
	 * @param pageContext
	 * @param node
	 * @return
	 */
	protected VpeElementMapping getElmentMapping(VpePageContext pageContext,
			nsIDOMNode node) {

		return pageContext.getDomMapping().getNearElementMapping(node);

	}

	/**
	 * 
	 * @param pageContext
	 * @return
	 */
	protected nsIDOMNode getCurrentSelectedVisualNode(VpePageContext pageContext) {

		nsISelection selection = TemplateManagingUtil
				.getCurrentSelection(pageContext);

		if (selection.getFocusNode() != null)
			return TemplateManagingUtil.getSelectedNode(selection);
		else
			return TemplateManagingUtil.getLastSelectedVisualNode(pageContext);

	}

	/**
	 * 
	 * @param pageContext
	 * @return
	 */
	protected Point getSelectionRange(nsISelection selection) {

		nsIDOMNode focusedNode = TemplateManagingUtil
				.getSelectedNode(selection);

		Point range = null;

		if (focusedNode != null) {
			
			range = new Point(0,0);
			if ((HTML.TAG_INPUT.equalsIgnoreCase(focusedNode.getLocalName()))
					|| (HTML.TAG_TEXTAREA.equalsIgnoreCase(focusedNode
							.getLocalName()))) {

				range = TemplateManagingUtil
						.getSelectionRangeFromInputElement(focusedNode);

			} else {

				;
				range.x = selection.getFocusOffset();
				range.y = selection.getAnchorOffset()
						- selection.getFocusOffset();

			}
		}
		return range;
	}

	/**
	 * 
	 * @param selection
	 * @param node
	 * @param range
	 */
	protected void setSelectionRange(nsISelection selection, nsIDOMNode node,
			Point range) {

		if (node.getNodeType() == nsIDOMNode.TEXT_NODE) {
			selection.collapse(node, range.x);

			// if(visualFocus!=visualAnchor)
			// selection.extend(visualNode, visualAnchor );
		} else {

			if ((HTML.TAG_INPUT.equalsIgnoreCase(node.getLocalName()))
					|| (HTML.TAG_TEXTAREA.equalsIgnoreCase(node.getLocalName()))) {
				TemplateManagingUtil.setSelectionRangeInInputElement(node,
						range);

			}
			selection.collapse(node, 0);
		}

	}

	/**
	 * 
	 * @param sourceElement
	 * @param attributeName
	 * @param value
	 */
	protected Node createAttribute(Element sourceElement, String attributeName,
			String value) {

		if ((sourceElement != null) && (attributeName != null)) {
			sourceElement.setAttribute(attributeName, value != null ? value
					: ""); //$NON-NLS-1$

			return sourceElement.getAttributeNode(attributeName);
		}
		return null;

	}
}
