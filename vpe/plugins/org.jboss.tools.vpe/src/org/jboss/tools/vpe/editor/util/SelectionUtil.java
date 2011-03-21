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

package org.jboss.tools.vpe.editor.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Point;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.xml.core.internal.document.NodeImpl;
import org.jboss.tools.jst.jsp.util.NodesManagingUtil;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.mapping.VpeNodeMapping;
import org.jboss.tools.vpe.editor.selection.VpeSelectionController;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMRange;
import org.mozilla.interfaces.nsISelection;
import org.mozilla.interfaces.nsISelectionController;
import org.w3c.dom.Node;

/**
 * Utility class for selection manipulating.
 * 
 * @author S.Dzmitrovich
 */
public class SelectionUtil {

	/**
	 * get selected visual node from nsISelection
	 * 
	 * @param selection
	 * @return
	 */
	public static nsIDOMNode getSelectedNode(nsISelection selection) {
		nsIDOMNode selectedNode = null;
		nsIDOMNode focusNode = selection.getFocusNode();
		if (selection.getAnchorNode() == focusNode) {
			if (focusNode != null) {
				if ((focusNode.getNodeType() != nsIDOMNode.TEXT_NODE)
						&& (selection.getFocusOffset() != 0)) {

					selectedNode = focusNode.getChildNodes().item(
							selection.getFocusOffset() - 1);
				} else
					selectedNode = selection.getFocusNode();
			}
		} else {
			nsIDOMRange range = selection.getRangeAt(0);
			selectedNode = range.getCommonAncestorContainer();
		}
		return selectedNode;
	}

	/**
	 * select node completely
	 * 
	 * @param pageContext
	 * @param node
	 */
	public static void setSourceSelection(VpePageContext pageContext, Node node) {

		int start = NodesManagingUtil.getStartOffsetNode(node);
		int length = NodesManagingUtil.getNodeLength(node);

		pageContext.getSourceBuilder().getStructuredTextViewer()
				.setSelectedRange(start, length);
		pageContext.getSourceBuilder().getStructuredTextViewer().revealRange(
				start, length);
	}

	/**
	 * 
	 * @param pageContext
	 * @param node
	 * @param offset
	 * @param length
	 */
	public static void setSourceSelection(VpePageContext pageContext,
			Node node, int offset, int length) {
		int start = NodesManagingUtil.getStartOffsetNode(node);
		setSourceSelection(pageContext, start + offset, length);
	}

	/**
	 * 
	 * @param pageContext
	 * @param node
	 * @param offset
	 */
	public static void setSourceSelection(VpePageContext pageContext,
			Node node, int offset) {
		int start = NodesManagingUtil.getStartOffsetNode(node);
		pageContext.getSourceBuilder().getStructuredTextViewer()
				.getTextWidget().setSelection(start + offset);
	}

	/**
	 * 
	 * @param pageContext
	 * @param offset
	 * @param length
	 */
	public static void setSourceSelection(VpePageContext pageContext,
			int offset, int length) {
		pageContext.getSourceBuilder().getStructuredTextViewer()
				.setSelectedRange(offset, length);
		pageContext.getSourceBuilder().getStructuredTextViewer().revealRange(
				offset, length);
	}

	/**
	 * @deprecated This method causes JBIDE-4713. Use
	 * {@link #getNodeMappingBySourceSelection(StructuredTextEditor, VpeDomMapping)}
	 * instead.
	 */
	public static VpeNodeMapping getNodeMappingBySourceSelection(
			IStructuredModel model, VpeDomMapping domMapping, int focus,
			int anchor) {
		/*
		 * implementation of IDOMModel's method getIndexedRegion(...) has one
		 * feature : if cursor is situated at the border of elements then this
		 * method return next element. For example ... <h:inputText
		 * ../><h:outputText/>... - if cursor will be situated at the right
		 * border of "h:inputText" element then getIndexedRegion() return
		 * "h:outputText" element. So for focus position we choose smaller value
		 */
		if (anchor < focus) {
			int tmp = focus;
			focus = anchor;
			anchor = tmp;
		}

		// get source node by offset
		Node focusNode = getSourceNodeByPosition(model, focus);

		// if focus node also contain anchor point (selected only 1 element)
		if (focusNode != null
				&& NodesManagingUtil.isNodeContainsPosition(focusNode, anchor)) {
			return VpeNodesManagingUtil.getNodeMapping(domMapping, focusNode);
		}
		return null;
	}

	private static Node getSourceNodeByPosition(IStructuredModel model,
			int position) {
		// if we state at the end of text node, model will return
		// for us next node or null if on page exists only text node,
		// but we still in the end of text node, so we should check
		// this situation

		// get source node by position
		// see jbide-3163
		IndexedRegion node = model.getIndexedRegion(position);
		IndexedRegion possibleNode = position >= 1 ? model
				.getIndexedRegion(position - 1) : null;
		if (node == null && position >= 1) {
			node = possibleNode;
		} else if ((node != null)
				&& (((Node) node).getNodeType() != Node.TEXT_NODE)
				&& (node.getStartOffset() == position) && (position >= 1)) {
			// check for such situation #text<h1></h1>
			node = possibleNode;
		} else if ((node != null)
				&& (((Node) node).getNodeType() != Node.TEXT_NODE)
				&& (possibleNode != null)
				&& ((Node) possibleNode).getNodeType() == Node.TEXT_NODE) {
			node = possibleNode;
		}

		return (Node) node;
	}

	/**
	 * Returns selection range for visual part of editor Focus offset and anchor
	 * offset can be not equals to source focus offset and anchor offset
	 * 
	 * @param selection
	 *            the selection in visual part of editor
	 * @return selection range for visual part of editor
	 */
	public static Point getVisualSelectionRange(nsISelection selection) {
		Point range = new Point(0, 0);
		nsIDOMNode focusedNode = getSelectedNode(selection);
		if (focusedNode != null) {
			range.x = selection.getFocusOffset();
			range.y = selection.getAnchorOffset() - selection.getFocusOffset();
		}
		return range;
	}

	/**
	 * Return source editor part selection range, range returns relatively to
	 * start of text in source, not for start of document
	 * 
	 * @param selection
	 *            the selection in visual part of editor
	 * @return source editor selection range
	 */
	public static Point getSourceSelectionRange(nsIDOMNode focusedNode,
			int focusOffset, int anchorOffset, Node sourceNode) {
		// gets visual selection range
		Point sourceRange = new Point(0, 0);
		// converts to source selection
		if ((sourceNode != null) && (sourceNode.getNodeValue() != null)) {
			//fix for JBIDE-3650
			NodeImpl nodeImpl = (NodeImpl) sourceNode;
			sourceRange.x = TextUtil.sourcePosition(nodeImpl.getValueSource(),
					focusedNode.getNodeValue(), focusOffset);
			sourceRange.y = TextUtil.sourcePosition(nodeImpl.getValueSource(),
					focusedNode.getNodeValue(), anchorOffset)
					- sourceRange.x;
		}
		return sourceRange;
	}

	
	public static VpeNodeMapping getNodeMappingBySourceSelection(
			StructuredTextEditor sourceEditor, VpeDomMapping domMapping) {
		List<VpeNodeMapping> mappings
				= getNodeMappingsBySourceSelection(sourceEditor, domMapping);
		if (!mappings.isEmpty()) {
			return mappings.get(0);
		} else {
			return null;
		}
	}
	public static List<VpeNodeMapping> getNodeMappingsBySourceSelection(
			StructuredTextEditor sourceEditor, VpeDomMapping domMapping) {
		List<VpeNodeMapping> nodeMappings = new ArrayList<VpeNodeMapping>();
		for (Node node : getFocusNodesBySourceSelection(sourceEditor, domMapping)) {
			VpeNodeMapping nodeMapping = VpeNodesManagingUtil.getNodeMapping(domMapping, node);
			if (nodeMapping != null) {
				nodeMappings.add(nodeMapping);
			}
		}
		return nodeMappings;
	}
	
	private static List<Node> getFocusNodesBySourceSelection(
			StructuredTextEditor sourceEditor, VpeDomMapping domMapping) {
		Point range = sourceEditor.getTextViewer().getSelectedRange();

		IDocument document = sourceEditor.getTextViewer().getDocument();

		List<Node> focusNodes = new ArrayList<Node>();
		IStructuredModel model = null;
		try {
			// gets source model for read, model should be released see
			// JBIDE-2219
			model = StructuredModelManager.getModelManager()
					.getExistingModelForRead(document);
			
			//fix for JBIDE-3805, mareshkau			
			if(model == null) {
				return Collections.emptyList();
			}

			/*
			 * implementation of IDOMModel's method getIndexedRegion(...) has
			 * one feature : if cursor is situated at the border of elements
			 * then this method return next element. For example ...
			 * <h:inputText ../><h:outputText/>... - if cursor will be situated
			 * at the right border of "h:inputText" element then
			 * getIndexedRegion() return "h:outputText" element. So for focus
			 * position we choose smaller value
			 */
			int selectionStart = Math.min(range.x, range.x + range.y);
			if (range.y == 0) {
				/* if selection length is 0 (text cursor only), than use
				 * custom implementation to determine node. See JBIDE-3163. */
				Node focusNode = getSourceNodeByPosition(model, selectionStart);
				if (focusNode != null) {
					focusNodes.add(focusNode);
				}
			} else {
				// fixed JBIDE-3388: Incorrect selection after Copy/Cut actions
				ISelection selection = sourceEditor.getSelectionProvider().getSelection();
				if (selection instanceof IStructuredSelection) {
					Iterator iterator = ((IStructuredSelection) selection).iterator();
					while (iterator.hasNext()) {
						Object selectionElement = iterator.next();
						if (selectionElement instanceof Node) {
							focusNodes.add((Node) selectionElement);
						}
					}
				}
			}
		} finally {
			if (model != null) {
				model.releaseFromRead();
			}
		}

		return focusNodes;
	}

	public static Node getNodeBySourcePosition(
			StructuredTextEditor sourceEditor, int position) {

		IDocument document = sourceEditor.getTextViewer().getDocument();

		IStructuredModel model = null;

		Node node = null;
		try {
			model = StructuredModelManager.getModelManager()
					.getExistingModelForRead(document);

			node = (Node) model.getIndexedRegion(position);

		} finally {
			if (model != null) {
				model.releaseFromRead();
			}
		}

		return node;
	}

	/**
	 * Method is used to select the last selected node.
	 * 
	 * @param pageContext
	 *            VpePageContext object
	 * @return nsIDOMNode the last selected node
	 */
	public static nsIDOMNode getSelectedNode(VpePageContext pageContext) {
		nsIDOMNode domNode=null;
		List<nsIDOMNode> selectedNodes = pageContext.getVisualBuilder().getXulRunnerEditor()
		.getSelectedNodes();
		if(selectedNodes!=null&&selectedNodes.size()>0){
			domNode=selectedNodes.get(0);
		}
		return domNode;
	}

	/**
	 * Returns sourceSelectionRange
	 * 
	 * @param sourceEditor
	 *            StructuredTextEditor object
	 * @return sourceSelectionRange
	 */
	public static Point getSourceSelectionRange(
			StructuredTextEditor sourceEditor) {
		ITextViewer textViewer = sourceEditor.getTextViewer();
		if (textViewer != null) {
			return textViewer.getSelectedRange();
		}
		return null;
	}

	/**
	 * 
	 * @param selectionController
	 *            VpeSelectionController object
	 */
	public static void clearSelection(VpeSelectionController selectionController) {
		selectionController.getSelection(
				nsISelectionController.SELECTION_NORMAL).removeAllRanges();
	}
}