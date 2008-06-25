package org.jboss.tools.vpe.editor.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.IDocument;
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
 * 
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

	public static Point getSelectionRange(nsISelection selection) {
		nsIDOMNode focusedNode = getSelectedNode(selection);

		Point range = new Point(0, 0);

		if (focusedNode != null) {

			range.x = selection.getFocusOffset();
			range.y = selection.getAnchorOffset() - selection.getFocusOffset();

		}
		return range;
	}

	public static VpeNodeMapping getNodeMappingBySourceSelection(
			StructuredTextEditor sourceEditor, VpeDomMapping domMapping) {

		Point range = sourceEditor.getTextViewer().getSelectedRange();

		IDocument document = sourceEditor.getTextViewer().getDocument();

		IStructuredModel model = StructuredModelManager.getModelManager()
				.getExistingModelForEdit(document);

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
		return null;

	}

	public static nsIDOMNode getLastSelectedNode(VpePageContext pageContext) {

		return pageContext.getVisualBuilder().getXulRunnerEditor()
				.getLastSelectedNode();

	}

	static public Point getSourceSelection(StructuredTextEditor sourceEditor) {
		return sourceEditor.getTextViewer().getSelectedRange();
	}

}
