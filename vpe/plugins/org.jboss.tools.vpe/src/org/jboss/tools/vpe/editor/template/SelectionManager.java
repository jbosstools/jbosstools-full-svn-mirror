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

import org.eclipse.swt.graphics.Point;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.xml.core.internal.document.NodeImpl;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.mapping.NodeData;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.mapping.VpeElementMapping;
import org.jboss.tools.vpe.editor.mapping.VpeNodeMapping;
import org.jboss.tools.vpe.editor.selection.VpeSelectionController;
import org.jboss.tools.vpe.editor.util.NodesManagingUtil;
import org.jboss.tools.vpe.editor.util.SelectionUtil;
import org.jboss.tools.vpe.editor.util.TextUtil;
import org.jboss.tools.vpe.editor.util.VisualDomUtil;
import org.mozilla.interfaces.nsIDOMMouseEvent;
import org.mozilla.interfaces.nsIDOMNSUIEvent;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsISelection;
import org.mozilla.interfaces.nsISelectionController;
import org.w3c.dom.Node;

/**
 * 
 * @author S. Dzmitrovich
 * 
 */
public class SelectionManager implements ISelectionManager {

	/**
	 * pageContext keeps information about page
	 */
	private VpePageContext pageContext;

	/**
	 * source editor
	 */
	private StructuredTextEditor sourceEditor;

	/**
	 * selection
	 */
	private VpeSelectionController selectionController;

	public SelectionManager(VpePageContext pageContext,
			StructuredTextEditor sourceEditor,
			VpeSelectionController selectionController) {
		this.pageContext = pageContext;
		this.sourceEditor = sourceEditor;
		this.selectionController = selectionController;
	}

	public final void setSelection(nsISelection selection) {

		nsIDOMNode selectedVisualNode = SelectionUtil
				.getSelectedNode(selection);

		if (selectedVisualNode == null)
			return;

		VpeNodeMapping nodeMapping = NodesManagingUtil.getNodeMapping(
				getDomMapping(), selectedVisualNode);

		if (nodeMapping == null)
			return;

		// visual node which will be selected
		nsIDOMNode targetVisualNode;
		// source node which will be selected
		Node targetSourceNode;

		boolean isNodeEditable;

		// if mapping is elementMapping
		if (nodeMapping instanceof VpeElementMapping) {

			VpeElementMapping elementMapping = (VpeElementMapping) nodeMapping;

			VpeTemplate template = elementMapping.getTemplate();

			NodeData nodeData = template.getNodeData(selectedVisualNode,
					elementMapping.getElementData(), getDomMapping());

			if (nodeData != null) {

				isNodeEditable = nodeData.isEditable();
				if (nodeData.getSourceNode() != null) {
					targetSourceNode = nodeData.getSourceNode();

				} else {

					isNodeEditable = false;
					targetSourceNode = elementMapping.getSourceNode();

				}

				targetVisualNode = nodeData.getVisualNode();
			} else {

				targetVisualNode = elementMapping.getVisualNode();
				targetSourceNode = elementMapping.getSourceNode();
				isNodeEditable = false;
			}

		} else {
			// here we processed text node
			targetVisualNode = nodeMapping.getVisualNode();
			targetSourceNode = nodeMapping.getSourceNode();
			isNodeEditable = true;

		}
		
		if (targetVisualNode.getNodeType() != nsIDOMNode.TEXT_NODE
				&& SelectionUtil.getLastSelectedNode(getPageContext()) == targetVisualNode)
			return;
			
		int focusOffset;
		int length;

		if (isNodeEditable) {

			Point sourceSelectionRange = SelectionUtil.getSourceSelectionRange(
					selection, targetSourceNode);

			focusOffset = sourceSelectionRange.x;
			length = sourceSelectionRange.y;

		} else {

			focusOffset = 0;
			length = NodesManagingUtil.getNodeLength(targetSourceNode);

		}

		// set source selection
		SelectionUtil.setSourceSelection(getPageContext(), targetSourceNode,
				focusOffset, length);

		// paint visual selection
		getPageContext().getVisualBuilder().setSelectionRectangle(
				targetVisualNode);

	}

	final public void setSelection(nsIDOMMouseEvent mouseEvent) {

		// get visual node by event
		nsIDOMNode visualNode = VisualDomUtil.getTargetNode(mouseEvent);

		// get element mapping
		VpeNodeMapping nodeMapping = NodesManagingUtil.getNodeMapping(
				getDomMapping(), visualNode);

		if (nodeMapping == null)
			return;

		// visual node which will be selected
		nsIDOMNode targetVisualNode;
		// source node which will be selected
		Node targetSourceNode;

		boolean isNodeEditable;

		// if mapping is elementMapping
		if (nodeMapping instanceof VpeElementMapping) {

			VpeElementMapping elementMapping = (VpeElementMapping) nodeMapping;

			VpeTemplate template = elementMapping.getTemplate();

			NodeData nodeData = template.getNodeData(visualNode, elementMapping
					.getElementData(), getDomMapping());

			if (nodeData != null) {

				isNodeEditable = nodeData.isEditable();

				if (nodeData.getSourceNode() != null) {

					targetSourceNode = nodeData.getSourceNode();

				} else {

					isNodeEditable = false;
					targetSourceNode = elementMapping.getSourceNode();

				}

				targetVisualNode = nodeData.getVisualNode();

			} else {

				targetVisualNode = elementMapping.getVisualNode();
				targetSourceNode = elementMapping.getSourceNode();
				isNodeEditable = false;

			}

		}

		else {

			targetVisualNode = nodeMapping.getVisualNode();
			targetSourceNode = nodeMapping.getSourceNode();
			isNodeEditable = true;

		}

		// get nsIDOMNSUIEvent event
		nsIDOMNSUIEvent nsuiEvent = (nsIDOMNSUIEvent) mouseEvent
				.queryInterface(nsIDOMNSUIEvent.NS_IDOMNSUIEVENT_IID);

		int selectionOffset;
		int selectionLength;

		if (isNodeEditable) {
			selectionOffset = nsuiEvent.getRangeOffset();
			selectionLength = 0;
		} else {

			selectionOffset = 0;
			selectionLength = NodesManagingUtil.getNodeLength(targetSourceNode);

		}

		SelectionUtil.setSourceSelection(getPageContext(), targetSourceNode,
				selectionOffset, selectionLength);

		// paint selection rectangle
		getPageContext().getVisualBuilder().setSelectionRectangle(
				targetVisualNode);

	}

	/**
	 * Syncronization visual selection and source selection, actually moves
	 * source selection to visual selection
	 */
	final public void refreshVisualSelection() {

		// checks for null, for case when we close editor and background
		// update job is running
		if (getSourceEditor().getTextViewer() == null) {

			return;
		}

		Point range = SelectionUtil
				.getSourceSelectionRange(getSourceEditor());

		if (range == null)
			return;

		int focusOffcetInSourceDocument = range.x;

		int anchorOffcetInSourceDocument = focusOffcetInSourceDocument
				+ range.y;
		VpeNodeMapping nodeMapping = SelectionUtil
				.getNodeMappingBySourceSelection(getSourceEditor(),
						getDomMapping());

		if (nodeMapping == null) {
			return;
		}

		// visual node which will be selected
		nsIDOMNode targetVisualNode;

		// int visualNodeOffcet =
		// TextUtil.visualPosition(((Node)targetSourceNode
		// ).getNodeValue(),offcetReferenceToSourceNode);

		// if mapping is elementMapping
		
		SelectionUtil.clearSelection(selectionController); 
		
		if (nodeMapping instanceof VpeElementMapping) {

			VpeElementMapping elementMapping = (VpeElementMapping) nodeMapping;

			VpeTemplate template = elementMapping.getTemplate();

			targetVisualNode = template.getVisualNodeBySourcePosition(
					elementMapping, focusOffcetInSourceDocument,
					anchorOffcetInSourceDocument, getDomMapping());

			NodeData nodeData = template.getNodeData(targetVisualNode,
					elementMapping.getElementData(), getDomMapping());
			// we can restore cursor position only if we have nodeData and
			// range.y==0
			if (nodeData != null) {
				// restore cursor position in source document
				restoreVisualCursorPosition(template, nodeData,
						focusOffcetInSourceDocument,
						anchorOffcetInSourceDocument);
			}
		} else {

			targetVisualNode = nodeMapping.getVisualNode();
			// restore cursor position for source node
//				restoreVisualCursorPositionForTextNode(targetVisualNode,
//						focusOffcetInSourceDocument, model);
		}
		// here we restore only highlight
		getPageContext().getVisualBuilder().setSelectionRectangle(
				targetVisualNode);
	}

	/**
	 * Restores visual cursor position in visual part of editor
	 * 
	 * @param template
	 *            - current template in scope of which we are editing data
	 *            !IMPORTANT for current implementation in should be a text node
	 * @param nodeData
	 *            -contains mapping before sourceNode(it's can be an attribute)
	 *            and visual node, attribute
	 */
	private void restoreVisualCursorPosition(VpeTemplate template,
			NodeData nodeData, int focusOffcetInSourceDocument,
			int anchorOffsetrInSourceDocument) {

		nsIDOMNode visualNode = nodeData.getVisualNode();

		if (visualNode != null
				&& visualNode.getNodeType() == nsIDOMNode.TEXT_NODE
				&& nodeData.getSourceNode() != null) {

			Node targetSourceNode = nodeData.getSourceNode();

			int focusOffcetReferenceToSourceNode = focusOffcetInSourceDocument
					- NodesManagingUtil.getStartOffsetNode(targetSourceNode);

			int anchorOffcetReferenceToSourceNode = anchorOffsetrInSourceDocument
					- NodesManagingUtil.getStartOffsetNode(targetSourceNode);

			int length = visualNode.getNodeValue().length();
//			if (focusOffcetReferenceToSourceNode > length
//					|| anchorOffcetReferenceToSourceNode > length)
//				return;
			NodeImpl sourceTextImpl = (NodeImpl) targetSourceNode;
			int visualNodeFocusOffcet = TextUtil.visualPosition(
					sourceTextImpl.getValueSource(),
					focusOffcetReferenceToSourceNode);

			int visualNodeAnchorOffcet = TextUtil.visualPosition(
					sourceTextImpl.getValueSource(),
					anchorOffcetReferenceToSourceNode);
			if (visualNodeFocusOffcet > length
					|| visualNodeAnchorOffcet > length)
					return;
			nsISelection selection = selectionController.getSelection(
					nsISelectionController.SELECTION_NORMAL);

			selection.collapse(
					visualNode, visualNodeFocusOffcet);

			if (visualNodeAnchorOffcet != visualNodeFocusOffcet) {
				selection.extend(
						visualNode, visualNodeAnchorOffcet);
			}

		}

	}


	protected VpePageContext getPageContext() {
		return pageContext;
	}

	protected VpeDomMapping getDomMapping() {
		return pageContext.getDomMapping();
	}

	protected StructuredTextEditor getSourceEditor() {
		return sourceEditor;
	}

}
