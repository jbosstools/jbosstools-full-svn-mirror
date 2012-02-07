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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Point;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.xml.core.internal.document.NodeImpl;
import org.jboss.tools.jst.jsp.util.NodesManagingUtil;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.mapping.NodeData;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.mapping.VpeElementMapping;
import org.jboss.tools.vpe.editor.mapping.VpeNodeMapping;
import org.jboss.tools.vpe.editor.selection.VpeSelectionController;
import org.jboss.tools.vpe.editor.util.SelectionUtil;
import org.jboss.tools.vpe.editor.util.TextUtil;
import org.jboss.tools.vpe.editor.util.VpeNodesManagingUtil;
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
	 * Flag indicates that the source and visual selection is updating. 
	 */
	public static boolean updateSelectionEventFlag = false;
	
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

	@Override
	public boolean isUpdateSelectionEventPerformed() {
		return updateSelectionEventFlag;
	}

	@Override
	public void setUpdateSelectionEventFlag(boolean isPerforming) {
		updateSelectionEventFlag = isPerforming;
	}

	public final void setSelection(nsIDOMNode visualNode, int focusOffset, int anchorOffset) {
		setUpdateSelectionEventFlag(true);
		if (visualNode == null) {
			return;
		}

		SelectionData selectionData = getSelectionData(visualNode);
		if (selectionData == null) {
			return;
		}
		
		if (selectionData.getVisualNode().getNodeType() != nsIDOMNode.TEXT_NODE
				&& SelectionUtil.getSelectedNode(getPageContext())
						== selectionData.getVisualNode()) {
			return;
		}
		/*************** Calculate selection range ****************************/
		int selectionOffset;
		int selectionLength;
		if (selectionData.isNodeEditable()) {
			Point sourceSelectionRange = SelectionUtil.getSourceSelectionRange(
					visualNode, focusOffset, anchorOffset, selectionData.getSourceNode());
			selectionOffset = sourceSelectionRange.x;
			selectionLength = sourceSelectionRange.y;
		} else {
			selectionOffset = 0;
			selectionLength = NodesManagingUtil.getNodeLength(selectionData.getSourceNode());
		}
		/*************** Apply selection to views *****************************/
		SelectionUtil.setSourceSelection(getPageContext(),
				selectionData.getSourceNode(),
				selectionOffset, selectionLength);
		refreshVisualNodeSelection();
		setUpdateSelectionEventFlag(false);
	}

	private SelectionData getSelectionData(nsIDOMNode visualNode) {
		// get element mapping
		VpeNodeMapping nodeMapping = VpeNodesManagingUtil.getNodeMapping(
				getDomMapping(), visualNode);

		if (nodeMapping == null) {
			return null;
		}
		
		nsIDOMNode targetVisualNode; // visual node which will be selected
		Node targetSourceNode; // source node which will be selected
		boolean isNodeEditable;
		if (nodeMapping instanceof VpeElementMapping) {
			VpeElementMapping elementMapping = (VpeElementMapping) nodeMapping;
			NodeData nodeData = elementMapping.getTemplate().getNodeData(
					visualNode, elementMapping.getElementData(), getDomMapping());

			if (nodeData != null) {
				targetVisualNode = nodeData.getVisualNode();
				if (nodeData.getSourceNode() != null) {
					targetSourceNode = nodeData.getSourceNode();
					isNodeEditable = nodeData.isEditable();				
				} else {
					targetSourceNode = elementMapping.getSourceNode();
					isNodeEditable = false;
				}
			} else {
				targetVisualNode = elementMapping.getVisualNode();
				targetSourceNode = elementMapping.getSourceNode();				
				isNodeEditable = false;
			}
		} else {
			targetVisualNode = nodeMapping.getVisualNode();
			targetSourceNode = nodeMapping.getSourceNode();
			isNodeEditable = true;
		}
		SelectionData selectionData = new SelectionData(
				targetVisualNode, targetSourceNode, isNodeEditable);
		return selectionData;
	}

	/**
	 * Synchronize visual selection and source selection (move
	 * source selection to visual selection).
	 */
	final public void refreshVisualSelection() {
		refreshVisualNodeSelection();
		refreshVisualTextSelection();
	}
	
	/**
	 * Draws selection rectangle in the Visual Part according to source selection.
	 */
	private void refreshVisualNodeSelection() {
		// checks for null, for case when we close editor and background
		// update job is running
		if (getSourceEditor().getTextViewer() == null) {
			return;
		}
		
		Point range = SelectionUtil.getSourceSelectionRange(getSourceEditor());
		if (range == null) {
			return;
		}
		
		List<VpeNodeMapping> nodeMappings = SelectionUtil
				.getNodeMappingsBySourceSelection(getSourceEditor(),	getDomMapping());
		if (nodeMappings == null) {
			return;
		}
		
		// visual node which will be selected
		List<nsIDOMNode> targetVisualNodes = new ArrayList<nsIDOMNode>();
		for (VpeNodeMapping nodeMapping : nodeMappings) {
			if (nodeMapping instanceof VpeElementMapping) {
				VpeElementMapping elementMapping = (VpeElementMapping) nodeMapping;
				targetVisualNodes.add(elementMapping.getTemplate()
						.getVisualNodeBySourcePosition(elementMapping,
								range, getDomMapping()));
			} else {
				targetVisualNodes.add(nodeMapping.getVisualNode());
			}
		}
		getPageContext().getVisualBuilder().setSelectionRectangle(targetVisualNodes);
	}
	
	/**
	 * Selects text in the Visual Part Visual Part according to source selection.
	 */
	private void refreshVisualTextSelection() {
		setUpdateSelectionEventFlag(true);
		// checks for null, for case when we close editor and background
		// update job is running
		if (getSourceEditor().getTextViewer() == null) {
			return;
		}
		
		Point range = SelectionUtil.getSourceSelectionRange(getSourceEditor());
		if (range == null) {
			return;
		}
		
		VpeNodeMapping nodeMapping = SelectionUtil
				.getNodeMappingBySourceSelection(getSourceEditor(),	getDomMapping());
		if (nodeMapping == null) {
			return;
		}
		
		SelectionUtil.clearSelection(selectionController); 
		
		// visual node which will be selected
		
		if (nodeMapping instanceof VpeElementMapping) {
			VpeElementMapping elementMapping = (VpeElementMapping) nodeMapping;
			nsIDOMNode targetVisualNode = elementMapping.getTemplate()
					.getVisualNodeBySourcePosition(
							elementMapping, range, getDomMapping());
			NodeData nodeData = elementMapping.getTemplate().getNodeData(targetVisualNode,
					elementMapping.getElementData(), getDomMapping());
			// we can restore cursor position only if we have nodeData and
			// range.y==0
			if (nodeData != null) {
				// restore cursor position in source document
				restoreVisualCursorPosition(elementMapping.getTemplate(), nodeData, range);
			}
		}
		setUpdateSelectionEventFlag(false);
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
			NodeData nodeData, Point selectionRange) {

		nsIDOMNode visualNode = nodeData.getVisualNode();

		if (visualNode != null
				&& visualNode.getNodeType() == nsIDOMNode.TEXT_NODE
				&& nodeData.getSourceNode() != null) {

			Node targetSourceNode = nodeData.getSourceNode();

			int focusOffcetReferenceToSourceNode = selectionRange.x
					- NodesManagingUtil.getStartOffsetNode(targetSourceNode);

			int anchorOffcetReferenceToSourceNode = selectionRange.x + selectionRange.y
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

class SelectionData {
	private nsIDOMNode visualNode;
	private Node sourceNode;
	private boolean isNodeEditable;
	
	public SelectionData(nsIDOMNode visualNode, Node sourceNode,
			boolean isNodeEditable) {
		super();
		this.visualNode = visualNode;
		this.sourceNode = sourceNode;
		this.isNodeEditable = isNodeEditable;
	}
	
	public nsIDOMNode getVisualNode() {
		return visualNode;
	}
	public void setVisualNode(nsIDOMNode visualNode) {
		this.visualNode = visualNode;
	}
	public Node getSourceNode() {
		return sourceNode;
	}
	public void setSourceNode(Node sourceNode) {
		this.sourceNode = sourceNode;
	}
	public boolean isNodeEditable() {
		return isNodeEditable;
	}
	public void setNodeEditable(boolean isNodeEditable) {
		this.isNodeEditable = isNodeEditable;
	}
}
