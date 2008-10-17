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

import org.eclipse.swt.custom.ST;
import org.eclipse.swt.graphics.Point;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.mapping.AttributeData;
import org.jboss.tools.vpe.editor.mapping.NodeData;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.mapping.VpeElementData;
import org.jboss.tools.vpe.editor.mapping.VpeElementMapping;
import org.jboss.tools.vpe.editor.mapping.VpeNodeMapping;
import org.jboss.tools.vpe.editor.selection.VpeSelectionController;
import org.jboss.tools.vpe.editor.util.SelectionUtil;
import org.jboss.tools.vpe.editor.util.TextUtil;
import org.jboss.tools.vpe.editor.util.VpeDebugUtil;
import org.mozilla.interfaces.nsIDOMKeyEvent;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsISelection;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * @author S.Dzmitrovich
 * 
 */
public class KeyEventManager implements IKeyEventHandler {

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	/**
	 * source editor
	 */
	private StructuredTextEditor sourceEditor;

	/**
	 * mapping
	 */
	private VpeDomMapping domMapping;

	/**
	 * page context
	 */
	private VpePageContext pageContext;
	
	/**
	 * selection controller
	 */
	private VpeSelectionController selectionController;
	
	public KeyEventManager(StructuredTextEditor sourceEditor,
			VpeDomMapping domMapping, VpePageContext pageContext, VpeSelectionController selectionController) {
		this.sourceEditor = sourceEditor;
		this.domMapping = domMapping;
		this.pageContext = pageContext;
		setSelectionController(selectionController);
	}

	final public boolean handleKeyPress(nsIDOMKeyEvent keyEvent) {

		long keyCode = keyEvent.getKeyCode();

		if (keyCode == nsIDOMKeyEvent.DOM_VK_ENTER) {
			return handleEnter(keyEvent);

		} else if ((keyCode == nsIDOMKeyEvent.DOM_VK_LEFT)
				&& (!keyEvent.getShiftKey())) {
			return handleLeft(keyEvent);

		} else if ((keyCode == nsIDOMKeyEvent.DOM_VK_UP)
				&& (!keyEvent.getShiftKey())) {
			return handleUp(keyEvent);

		} else if ((keyCode == nsIDOMKeyEvent.DOM_VK_RIGHT)
				&& (!keyEvent.getShiftKey())) {
			return handleRight(keyEvent);

		} else if ((keyCode == nsIDOMKeyEvent.DOM_VK_DOWN)
				&& (!keyEvent.getShiftKey())) {
			return handleDown(keyEvent);

		} else if ((keyCode == nsIDOMKeyEvent.DOM_VK_HOME)
				&& (!keyEvent.getShiftKey())) {
			return handleHome(keyEvent);

		} else if ((keyCode == nsIDOMKeyEvent.DOM_VK_END)
				&& (!keyEvent.getShiftKey())) {
			return handleEnd(keyEvent);

		} else if ((keyCode == nsIDOMKeyEvent.DOM_VK_BACK_SPACE)
				&& (!keyEvent.getShiftKey())) {
			return handleLeftDelete(keyEvent);

		} else if ((keyCode == nsIDOMKeyEvent.DOM_VK_DELETE)
				&& (!keyEvent.getShiftKey())) {
			return handleRightDelete(keyEvent);

		} else if ((keyCode == nsIDOMKeyEvent.DOM_VK_PAGE_UP)
				&& (!keyEvent.getShiftKey())) {
			return handlePageUp(keyEvent);

		} else if (keyEvent.getCharCode() != 0) {
			return handleCharacter(keyEvent);

		} else if ((keyEvent.getKeyCode() == nsIDOMKeyEvent.DOM_VK_INSERT)
				&& keyEvent.getShiftKey()) {
			return handleInsert(keyEvent);
		}

		return false;
	}

	/**
	 * Default handling of a pressing a character event
	 * 
	 * 
	 * @param keyEvent
	 *            - event
	 * @return whether handled event
	 */
	protected boolean handleCharacter(nsIDOMKeyEvent keyEvent) {

		VpeNodeMapping selectedNodeMapping = SelectionUtil
				.getNodeMappingBySourceSelection(getSourceEditor(),
						getDomMapping());

		if (selectedNodeMapping == null)
			return false;

		boolean editable = false;

		String enteredChar = TextUtil.getChar(keyEvent);

		// if selected node is element
		if (selectedNodeMapping instanceof VpeElementMapping) {

			VpeElementMapping elementMapping = (VpeElementMapping) selectedNodeMapping;

			VpeElementData elementData = elementMapping.getElementData();

			VpeTemplate template = elementMapping.getTemplate();

			nsIDOMNode visualNode = SelectionUtil
					.getLastSelectedNode(getPageContext());

			NodeData nodeData = template.getNodeData(visualNode, elementData,
					domMapping);

			if (nodeData != null) {

				editable = nodeData.isEditable();

				if (editable && nodeData.getType() == NodeData.ATTRIBUTE
						&& nodeData.getSourceNode() == null) {

					Node newNode = createAttribute(
							(Element) selectedNodeMapping.getSourceNode(),
							((AttributeData) nodeData).getAttributeName(),
							enteredChar);
					nodeData.setSourceNode(newNode);

					SelectionUtil.setSourceSelection(pageContext, newNode,
							enteredChar.length());
					return true;

				}

			}
			// if template can't give necessary information
			else {

				editable = false;

			}
		}
		// if node is simple text
		else {
			editable = true;
		}

		if (editable) {

			Point range = getSourceEditor().getTextViewer().getSelectedRange();

			getSourceEditor().getTextViewer().getTextWidget().replaceTextRange(
					range.x, range.y, enteredChar);

			getSourceEditor().getTextViewer().getTextWidget().setSelection(
					range.x + enteredChar.length());

		}

		return true;
	}

	/**
	 * Default implementation of a handling of a pressing the "delete" event
	 * 
	 * Override this method for a handling of a pressing the "delete" event
	 * 
	 * @param keyEvent
	 *            - event
	 * @return whether handled event
	 */
	protected boolean handleRightDelete(nsIDOMKeyEvent keyEvent) {

		VpeNodeMapping selectedNodeMapping = SelectionUtil
				.getNodeMappingBySourceSelection(getSourceEditor(),
						getDomMapping());

		if (selectedNodeMapping == null)
			return false;

		boolean editable = false;

		// if selected node is element
		if (selectedNodeMapping instanceof VpeElementMapping) {

			VpeElementMapping elementMapping = (VpeElementMapping) selectedNodeMapping;

			VpeElementData elementData = elementMapping.getElementData();

			VpeTemplate template = elementMapping.getTemplate();

			nsIDOMNode visualNode = SelectionUtil
					.getLastSelectedNode(getPageContext());

			NodeData nodeData = template.getNodeData(visualNode, elementData,
					domMapping);

			if (nodeData != null) {
				editable = nodeData.isEditable();

				if (editable && nodeData.getType() == NodeData.ATTRIBUTE
						&& nodeData.getSourceNode() == null) {

					Node newNode = createAttribute(
							(Element) selectedNodeMapping.getSourceNode(),
							((AttributeData) nodeData).getAttributeName(),
							EMPTY_STRING);

					nodeData.setSourceNode(newNode);

					SelectionUtil.setSourceSelection(pageContext, newNode, 0);

					return true;

				}

			}
			// if template can't give necessary information
			else {

				editable = false;

			}
		}
		// if node is simple text
		else {
			editable = true;
		}

		if (editable) {

			sourceEditor.getTextViewer().getTextWidget().invokeAction(
					ST.DELETE_NEXT);

		}

		return true;

	}

	/**
	 * Default handling of a pressing the "backspace" event
	 * 
	 * Override this method to handle the "backspace" event
	 * 
	 * @param keyEvent
	 *            - event
	 * @return whether handled event
	 */
	protected boolean handleLeftDelete(nsIDOMKeyEvent keyEvent) {
		VpeNodeMapping selectedNodeMapping = SelectionUtil
				.getNodeMappingBySourceSelection(getSourceEditor(),
						getDomMapping());

		if (selectedNodeMapping == null)
			return false;

		boolean editable = false;

		// if selected node is element
		if (selectedNodeMapping instanceof VpeElementMapping) {

			VpeElementMapping elementMapping = (VpeElementMapping) selectedNodeMapping;

			VpeElementData elementData = elementMapping.getElementData();

			VpeTemplate template = elementMapping.getTemplate();

			nsIDOMNode visualNode = SelectionUtil
					.getLastSelectedNode(getPageContext());

			NodeData nodeData = template.getNodeData(visualNode, elementData,
					domMapping);

			if (nodeData != null) {
				editable = nodeData.isEditable();

				if (editable && nodeData.getType() == NodeData.ATTRIBUTE
						&& nodeData.getSourceNode() == null) {

					Node newNode = createAttribute(
							(Element) selectedNodeMapping.getSourceNode(),
							((AttributeData) nodeData).getAttributeName(),
							EMPTY_STRING);

					nodeData.setSourceNode(newNode);

					SelectionUtil.setSourceSelection(pageContext, newNode, 0);

					return true;

				}

			}
			// if template can't give necessary information
			else {

				editable = false;

			}
		}
		// if node is simple text
		else {
			editable = true;
		}

		if (editable) {

			sourceEditor.getTextViewer().getTextWidget().invokeAction(
					ST.DELETE_PREVIOUS);

		}

		return true;

	}

	/**
	 * Default handling of a pressing the "end" event - always return false.
	 * 
	 * Override this method to handle of a pressing the "end" event
	 * 
	 * @param keyEvent
	 *            - event
	 * @return whether handled event
	 */
	protected boolean handleEnd(nsIDOMKeyEvent keyEvent) {
		return false;
	}

	/**
	 * Default handling of a pressing the "home" event - always return false.
	 * 
	 * Override this method to handle of a pressing the "home" event
	 * 
	 * @param keyEvent
	 *            - event
	 * @return whether handled event
	 */
	protected boolean handleHome(nsIDOMKeyEvent keyEvent) {
		return false;
	}

	/**
	 * Default handling of a pressing the "down" event - always return false.
	 * 
	 * Override this method to handle of a pressing the "down" event
	 * 
	 * @param keyEvent
	 *            - event
	 * @return whether handled event
	 */
	protected boolean handleDown(nsIDOMKeyEvent keyEvent) {
		return false;
	}

	/**
	 * Default handling of a pressing the "right" event - always return false.
	 * 
	 * Override this method to handle of a pressing the "right" event
	 * 
	 * @param keyEvent
	 *            - event
	 * @return whether handled event
	 */
	protected boolean handleRight(nsIDOMKeyEvent keyEvent) {
		return false;
	}

	/**
	 * Default handling of a pressing the "up" event - always return false.
	 * 
	 * Override this method to handle of a pressing the "up" event
	 * 
	 * @param keyEvent
	 *            - event
	 * @return whether handled event
	 */
	protected boolean handleUp(nsIDOMKeyEvent keyEvent) {
		return false;
	}

	/**
	 * Default handling of a pressing the "left" event - always return false.
	 * 
	 * Override this method to handle of a pressing the "left" event
	 * 
	 * @param keyEvent
	 *            - event
	 * @return whether handled event
	 */
	protected boolean handleLeft(nsIDOMKeyEvent keyEvent) {
		return false;

	}

	/**
	 * Default handling of a pressing the "enter" event - always return false.
	 * 
	 * Override to handling of a pressing the "enter" event
	 * 
	 * @param keyEvent
	 *            - event
	 * @return whether handled event
	 */
	protected boolean handleEnter(nsIDOMKeyEvent keyEvent) {
		return true;
	}

	/**
	 * 
	 * @param keyEvent
	 * @return
	 */
	protected boolean handleInsert(nsIDOMKeyEvent keyEvent) {
		return false;
	}

	/**
	 * Default handling of a pressing the "page up" event - always return false.
	 * 
	 * Override this method for a handling of a pressing the "page up" event
	 * 
	 * @param keyEvent
	 *            - event
	 * @return whether handled event
	 */
	protected boolean handlePageUp(nsIDOMKeyEvent keyEvent) {
		return false;
	}

	protected StructuredTextEditor getSourceEditor() {
		return sourceEditor;
	}

	protected VpeDomMapping getDomMapping() {
		return domMapping;
	}

	protected VpePageContext getPageContext() {
		return pageContext;
	}

	protected Node createAttribute(Element sourceElement, String attributeName,
			String value) {

		if ((sourceElement != null) && (attributeName != null)) {
			sourceElement.setAttribute(attributeName, value != null ? value
					: ""); //$NON-NLS-1$

			return sourceElement.getAttributeNode(attributeName);
		}
		return null;

	}

	/**
	 * @return the selectionController
	 */
	private VpeSelectionController getSelectionController() {
		return selectionController;
	}

	/**
	 * @param selectionController the selectionController to set
	 */
	private void setSelectionController(VpeSelectionController selectionController) {
		this.selectionController = selectionController;
	}
}
