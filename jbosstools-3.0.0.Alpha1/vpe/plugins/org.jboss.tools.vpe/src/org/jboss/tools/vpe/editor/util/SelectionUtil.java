package org.jboss.tools.vpe.editor.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.mapping.VpeNodeMapping;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMRange;
import org.mozilla.interfaces.nsISelection;
import org.w3c.dom.Node;

/**
 * Util class for selection manupalating
 * @author S.Dzmitrovich
 * 
 */
public class SelectionUtil {

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$
	private static List<String> NO_INPUT_TYPES;

	static {
		NO_INPUT_TYPES = new ArrayList<String>();

		NO_INPUT_TYPES.add(HTML.VALUE_TEXT_TYPE);
		NO_INPUT_TYPES.add(HTML.VALUE_PASSWORD_TYPE);
		NO_INPUT_TYPES.add(EMPTY_STRING);
	}

	/**
	 * get selected visual node from nsiDelection
	 * 
	 * @param selection
	 * @return
	 */
	public static nsIDOMNode getSelectedNode(nsISelection selection) {

		if (selection.getAnchorNode() == selection.getFocusNode()) {
			if (selection.getFocusNode() != null) {
				if ((selection.getFocusNode().getNodeType() != nsIDOMNode.TEXT_NODE)
						&& (selection.getFocusOffset() != 0)) {

					return selection.getFocusNode().getChildNodes().item(
							selection.getFocusOffset() - 1);
				} else
					return selection.getFocusNode();

			}
		} else {
			nsIDOMRange range = selection.getRangeAt(0);
			nsIDOMNode visualAncestor = range.getCommonAncestorContainer();
			return visualAncestor;
		}
		return null;
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

		pageContext.getSourceBuilder().getStructuredTextViewer()
				.setSelectedRange(start + offset, length);
		pageContext.getSourceBuilder().getStructuredTextViewer().revealRange(
				start + offset, length);

	}

	public static void setSourceSelection(VpePageContext pageContext,
			Node node, int offset) {

		int start = NodesManagingUtil.getStartOffsetNode(node);

		pageContext.getSourceBuilder().getStructuredTextViewer()
				.getTextWidget().setSelection(start + offset);

	}

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
			focus = anchor;
			anchor = focus;
		}

		// get source node by offset
		Node focusNode = getSourceNodeByPosition(model, focus);

		// if focus node also contain anchor point (selected only 1 element)
		if ((focusNode != null) && isNodeContainsPosition(focusNode, anchor)) {

			return NodesManagingUtil.getNodeMapping(domMapping, focusNode);

		}
		return null;

	}

	/**
	 * if position belong to node return true
	 * 
	 * @param node
	 * @param position
	 * @return
	 */
	private static boolean isNodeContainsPosition(Node node, int position) {

		if ((((IDOMNode) node).getStartOffset() <= position)
				&& (((IDOMNode) node).getEndOffset() >= position))
			return true;

		return false;
	}

	public static Node getSourceNodeByPosition(IStructuredModel model,
			int position) {

		// get source node by position
		Node node = (Node) model.getIndexedRegion(position);

		return node;
	}
	/**
	 * Returns selection range for visual part of editor
	 * Focus offset and anchor offset can be not equals to source focus offset and anchor offset
	 * 
	 * @param selection -selection in visual part of editor
	 * 
	 * @return selection range for visual part of editor
	 */
	public static Point getVisualSelectionRange(nsISelection selection) {
		nsIDOMNode focusedNode = getSelectedNode(selection);

		Point range = new Point(0, 0);

		if (focusedNode != null) {

			range.x = selection.getFocusOffset();
			range.y = selection.getAnchorOffset() - selection.getFocusOffset();

		}
		return range;
	}
	/**
	 * Return source editor part selection range, range returns relatively to start of text in source,
	 * not for start of document  
	 * 
	 * @param selection
	 * @return source editor selection range
	 */
	public static Point getSourceSelectionRange(nsISelection selection, Node sourceNode) {
		
		nsIDOMNode focusedNode = getSelectedNode(selection);
		//gets visual selection range
		Point sourceRange = new Point(0, 0);
		//converts to source selection
		sourceRange.x = TextUtil.sourcePosition(sourceNode.getNodeValue(), focusedNode.getNodeValue(), selection.getFocusOffset());
		sourceRange.y = TextUtil.sourcePosition(sourceNode.getNodeValue(), focusedNode.getNodeValue(), selection.getAnchorOffset())-sourceRange.x;
		return sourceRange;
	}
	
	public static VpeNodeMapping getNodeMappingBySourceSelection(
			StructuredTextEditor sourceEditor, VpeDomMapping domMapping) {

		Point range = sourceEditor.getTextViewer().getSelectedRange();

		IDocument document = sourceEditor.getTextViewer().getDocument();
		
		IStructuredModel model =null;
		
		try {
		model = StructuredModelManager.getModelManager()
				.getExistingModelForRead(document);

		int anchor = range.x;
		int focus = range.x + range.y;

		/*
		 * implementation of IDOMModel's method getIndexedRegion(...) has one
		 * feature : if cursor is situated at the border of elements then this
		 * method return next element. For example ... <h:inputText
		 * ../><h:outputText/>... - if cursor will be situated at the right
		 * border of "h:inputText" element then getIndexedRegion() return
		 * "h:outputText" element. So for focus position we choose smaller value
		 */

		if (anchor < focus) {
			focus = anchor;
			anchor = focus;
		}

		// get source node by offset
		Node focusNode = getSourceNodeByPosition(model, focus);

		// if focus node also contain anchor point (selected only 1 element)
		if ((focusNode != null) && isNodeContainsPosition(focusNode, anchor)) {

			return NodesManagingUtil.getNodeMapping(domMapping, focusNode);

		}
		} finally {
			
			if(model!=null) {
				
				model.releaseFromRead();
			}
		}
		
		return null;

	}

	public static nsIDOMNode getLastSelectedNode(VpePageContext pageContext) {

		return pageContext.getVisualBuilder().getXulRunnerEditor()
				.getLastSelectedNode();

	}
	/**
	 * Returns sourceSelectionRange
	 * @param sourceEditor
	 * @return sourceSelectionRange
	 */
	static public Point getSourceSelectionRange(StructuredTextEditor sourceEditor) {
		ITextViewer textViewer = sourceEditor.getTextViewer();

		if (textViewer != null)
			return textViewer.getSelectedRange();

		return null;
	}

}
