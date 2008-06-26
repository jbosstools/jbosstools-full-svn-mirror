package org.jboss.tools.vpe.editor.template;

import org.eclipse.swt.graphics.Point;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.mapping.NodeData;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.mapping.VpeElementMapping;
import org.jboss.tools.vpe.editor.mapping.VpeNodeMapping;
import org.jboss.tools.vpe.editor.selection.VpeSelectionController;
import org.jboss.tools.vpe.editor.util.NodesManagingUtil;
import org.jboss.tools.vpe.editor.util.SelectionUtil;
import org.jboss.tools.vpe.editor.util.VisualDomUtil;
import org.mozilla.interfaces.nsIDOMMouseEvent;
import org.mozilla.interfaces.nsIDOMNSUIEvent;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsISelection;
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

	final public void setSelection(nsISelection selection) {

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
		if (nodeMapping.getType() == VpeNodeMapping.ELEMENT_MAPPING) {

			VpeElementMapping elementMapping = (VpeElementMapping) nodeMapping;

			VpeTemplate template = elementMapping.getTemplate();

			NodeData nodeData = template.getNodeData(selectedVisualNode,
					elementMapping.getElementData(), getDomMapping());

			if (nodeData != null) {

				isNodeEditable = nodeData.isEditable();
				if (nodeData.getSourceNode() != null) {
					targetSourceNode = nodeData.getSourceNode();

				} else {

					targetSourceNode = elementMapping.getSourceNode();

				}

				targetVisualNode = nodeData.getVisualNode();
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

		int focusOffset;
		int length;

		if (isNodeEditable) {

			Point range = SelectionUtil.getSelectionRange(selection);

			focusOffset = range.x;
			length = range.y;

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
		if (nodeMapping.getType() == VpeNodeMapping.ELEMENT_MAPPING) {

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

	final public void refreshVisualSelection() {

		Point range = SelectionUtil.getSourceSelection(getSourceEditor());

		int focus = range.x;

		int anchor = focus + range.y;

		// get element mapping
		VpeNodeMapping nodeMapping = SelectionUtil
				.getNodeMappingBySourceSelection(NodesManagingUtil
						.getStructuredModel(getSourceEditor()),
						getDomMapping(), focus, anchor);

		if (nodeMapping == null)
			return;

		// visual node which will be selected
		nsIDOMNode targetVisualNode;

		// if mapping is elementMapping
		if (nodeMapping.getType() == VpeNodeMapping.ELEMENT_MAPPING) {

			VpeElementMapping elementMapping = (VpeElementMapping) nodeMapping;

			VpeTemplate template = elementMapping.getTemplate();

			targetVisualNode = template.getVisualNodeByBySourcePosition(
					elementMapping, focus, anchor, getDomMapping());

		} else {

			targetVisualNode = nodeMapping.getVisualNode();

		}

		getPageContext().getVisualBuilder().setSelectionRectangle(
				targetVisualNode);

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
