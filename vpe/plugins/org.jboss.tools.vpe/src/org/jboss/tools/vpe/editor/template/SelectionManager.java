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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.graphics.Point;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.xml.core.internal.document.AttrImpl;
import org.eclipse.wst.xml.core.internal.document.NodeImpl;
import org.eclipse.wst.xml.core.internal.document.TextImpl;
import org.jboss.tools.jst.jsp.selection.SelectionHelper;
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
	
	private static Pattern START_WHITESPACE = Pattern.compile("(?<=^)(\\s+)"); //$NON-NLS-1$
	private static Pattern END_WHITESPACE = Pattern.compile("(\\s+)(?=$)"); //$NON-NLS-1$

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
		boolean leftToRightSelection = anchorOffset <= focusOffset; 
		if (selectionData.isNodeEditable()) {
			Point sourceSelectionRange;
			if (leftToRightSelection) {
				 sourceSelectionRange = SelectionUtil.getSourceSelectionRange(
						 visualNode, anchorOffset, focusOffset, selectionData.getSourceNode());
			} else {
				sourceSelectionRange = SelectionUtil.getSourceSelectionRange(
						visualNode, focusOffset, anchorOffset, selectionData.getSourceNode());
			}
			selectionOffset = sourceSelectionRange.x;
			selectionLength = sourceSelectionRange.y - sourceSelectionRange.x;
		} else {
			selectionOffset = 0;
			selectionLength = NodesManagingUtil.getNodeLength(selectionData.getSourceNode());
		}
		/*************** Apply selection to views *****************************/
		SelectionUtil.setSourceSelection(getPageContext(), 
				selectionData.getSourceNode(), selectionOffset, selectionLength);
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
		setUpdateSelectionEventFlag(true);
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
			VpeTemplate template = elementMapping.getTemplate();
			nsIDOMNode targetVisualNode = template.getVisualNodeBySourcePosition(
					elementMapping, range, getDomMapping());
			NodeData nodeData = template.getNodeData(targetVisualNode,
					elementMapping.getElementData(), getDomMapping());
			// we can restore cursor position only if we have nodeData and
			// range.y==0
			if (nodeData != null) {
				// restore cursor position in source document
				restoreVisualCursorPosition(template, nodeData, range);
			}
		}
	}

	/**
	 * Restores visual cursor position in visual part of editor
	 * 
	 * @param template - current template in scope of which we are editing data
	 *            	!IMPORTANT for current implementation in should be a text node
	 * @param nodeData - contains mapping before sourceNode(it's can be an attribute)
	 *            	and visual node, attribute
	 * @param selectionRange - a <code>Point</code> with x as the offset 
	 * 				and y as the length of the current selection or <code>null</code> 
	 */
	private void restoreVisualCursorPosition(VpeTemplate template,
			NodeData nodeData, Point selectionRange) {
		if (nodeData != null) {
			nsIDOMNode visualNode = nodeData.getVisualNode();
			Node sourceNode = nodeData.getSourceNode();
			
			if ((selectionRange != null) && (sourceNode != null) && (visualNode != null) 
					&& (visualNode.getNodeType() == nsIDOMNode.TEXT_NODE)) {
				
				int focusOffcetReferenceToSourceNode = selectionRange.x
						- NodesManagingUtil.getStartOffsetNode(sourceNode);
				int anchorOffcetReferenceToSourceNode = selectionRange.x + selectionRange.y
						- NodesManagingUtil.getStartOffsetNode(sourceNode);
				NodeImpl sourceTextImpl = (NodeImpl) sourceNode;
				int visualNodeFocusOffcet = TextUtil.visualPosition(
						sourceTextImpl.getValueSource(),
						focusOffcetReferenceToSourceNode);
				int visualNodeAnchorOffcet = TextUtil.visualPosition(
						sourceTextImpl.getValueSource(),
						anchorOffcetReferenceToSourceNode);
				int length = visualNode.getNodeValue().length();
				
				if (visualNodeFocusOffcet > length || visualNodeAnchorOffcet > length) {
					return;
				}
				
				nsISelection selection = selectionController.getSelection(
						nsISelectionController.SELECTION_NORMAL);
				if ((visualNodeFocusOffcet == visualNodeAnchorOffcet)) {
					/*
					 * Nothing is selected.
					 */
					selection.collapse(visualNode, visualNodeFocusOffcet);
				} else {
					/*
					 * https://issues.jboss.org/browse/JBIDE-11137
					 * Detect selection direction
					 */
					int selectionStartOffset;
					int selectionEndOffset;
					if (visualNodeFocusOffcet <= visualNodeAnchorOffcet) {
						selectionStartOffset = visualNodeFocusOffcet;
						selectionEndOffset = visualNodeAnchorOffcet;
					} else {
						selectionStartOffset = visualNodeAnchorOffcet;
						selectionEndOffset = visualNodeFocusOffcet;
					}
					int sourceStartSel = selectionRange.x;
					int sourceSelLength =  selectionRange.y;
					int caretOffset = SelectionHelper.getCaretOffset(sourceEditor);
					TextImpl txt = null;
					AttrImpl attr = null;
					String wholeText = null;
					boolean sourceIsCorrect = false;
					int txtStartOffset= -1;
					int txtEndOffset = -1;
					int wsStartLength = 0;
					int wsEndLength = 0;
					int goodX = 0;
					int goodY = 0;
					if (sourceNode instanceof TextImpl) {
						txt = (TextImpl) sourceNode;
						wholeText = txt.getSource();
						txtStartOffset = txt.getStartOffset();
						txtEndOffset = txt.getEndOffset();
						sourceIsCorrect = true;
					} else if (sourceNode instanceof AttrImpl) {
						attr = (AttrImpl) sourceNode;
						wholeText = attr.getValueSource();
						txtStartOffset = attr.getValueRegionStartOffset();
						txtEndOffset = txtStartOffset + attr.getValueRegionText().length();
						sourceIsCorrect = true;
					}
					/*
					 * Currently only text and attribute nodes are supported.
					 */
					if (sourceIsCorrect) {
						Matcher matcher = START_WHITESPACE.matcher(wholeText);
						if (matcher.find()) {
							wsStartLength = matcher.group(1).length();
						} 
						matcher = END_WHITESPACE.matcher(wholeText);
						if (matcher.find()) {
							wsEndLength = matcher.group(1).length();
						}
						/*
						 * Good coordinates -- is coordinates of the text itself
						 * WITHOUT whitespace characters at the beginning
						 * and at the end.
						 */
						goodX = txtStartOffset + wsStartLength;
						goodY = txtEndOffset - wsEndLength;
						boolean skip = false;
						int allowedLength = 0;  
						if ((caretOffset >= txtStartOffset) && (caretOffset <= (txtStartOffset + wsStartLength))) {
							allowedLength = wsStartLength - (caretOffset - txtStartOffset); 
							if (sourceSelLength <= allowedLength) {
								skip = true;
							}
						} else if ((caretOffset <= txtEndOffset) && (caretOffset >= (txtEndOffset - wsEndLength))
								&& (sourceSelLength <= wsEndLength)) {
							allowedLength = wsStartLength - (txtEndOffset - caretOffset); 
							if (sourceSelLength <= allowedLength) {
								skip = true;
							}
						}
						if (skip) {
							/*
							 * Nothing is selected.
							 */
							selection.collapse(visualNode, selectionStartOffset);
						} else {
							// Source text selection start offset
							int newX = selectionRange.x;
							// Source text selection length
							int newY = selectionRange.y;
							if (newX < goodX) {
								newY = newY - (goodX - newX);
								if (newY < 0) {
									newY = 0;
								}
								newX = goodX;
							}
							// if new (selection offset+selection length) > than 
							// allowed text end coordinate.
							if ((newX + newY) > goodY) {
								// correct the new selection length:
								// no more that allowed text end coordinate.
								newY = goodY - newX;
							}
							Point newSrcRange = new Point(newX, newY);
							int srcStartOffset = newSrcRange.x - NodesManagingUtil.getStartOffsetNode(sourceNode);
							int srcEndOffset = srcStartOffset + newSrcRange.y;
							int visualStartOffset = TextUtil.visualPosition(sourceTextImpl.getValueSource(), srcStartOffset);
							int visualEndOffset = TextUtil.visualPosition(sourceTextImpl.getValueSource(), srcEndOffset);
							if (visualStartOffset > length || visualEndOffset > length) {
								return;
							}
							/*
							 * Detect selection direction
							 */
							boolean toLeft = caretOffset <= newSrcRange.x;
							boolean toRight = caretOffset >= newSrcRange.x + newSrcRange.y;
							int min = Math.min(visualStartOffset, visualEndOffset);
							int max = Math.max(visualStartOffset, visualEndOffset);
							if (toRight) {
								selection.collapse(visualNode, min);
								selection.extend(visualNode, max);
							} else if (toLeft) {
								selection.collapse(visualNode, max);
								selection.extend(visualNode, min);
							}
						}
					}
				}
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
