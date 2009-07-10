/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.vpe.editor.menu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMDataType;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.core.internal.contentmodel.util.CMDescriptionBuilder;
import org.eclipse.wst.xml.core.internal.contentmodel.util.DOMContentBuilder;
import org.eclipse.wst.xml.core.internal.contentmodel.util.DOMContentBuilderImpl;
import org.eclipse.wst.xml.core.internal.contentmodel.util.DOMNamespaceHelper;
import org.eclipse.wst.xml.ui.internal.actions.EditDoctypeAction;
import org.eclipse.wst.xml.ui.internal.actions.NodeAction;
import org.eclipse.wst.xml.ui.internal.util.XMLCommonResources;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;

import org.jboss.tools.vpe.VpePlugin;

/**
 * 
 * @author eskimo(dgolovin@exadel.com)
 */
public abstract class AbstractActionManager extends BaseActionManager {
	protected Viewer viewer;

	public AbstractActionManager(IStructuredModel model, ModelQuery modelQuery,
			Viewer viewer) {
		super(model, modelQuery);
		this.viewer = viewer;
	}

	/**
	 * This method is abstract since currently, the sed editor is required to
	 * perform formating and we don't want to create a dependency on the sed
	 * editor.
	 */
	public abstract void reformat(Node parent, boolean deep);

	protected Action createAddAttributeAction(Element parent,
			CMAttributeDeclaration ad) {
		return null;
	}

	protected Action createAddCDataSectionAction(Node parent, int index) {
		return new AddNodeAction(Node.CDATA_SECTION_NODE, parent, index);
	}

	protected Action createAddPCDataAction(Node parent, CMDataType dataType,
			int index) {
		Action action = null;
		if (dataType == null) {
			action = new AddNodeAction(Node.TEXT_NODE, parent, index);
		} else {
			action = new AddNodeAction(dataType, parent, index);
		}
		return action;
	}

	protected Action createAddCommentAction(Node parent, int index) {
		return new AddNodeAction(Node.COMMENT_NODE, parent, index);
	}

	protected Action createAddDoctypeAction(Document document, int index) {
		return new EditDoctypeAction(model, document, model.getBaseLocation(),
				XMLCommonResources.getInstance().getString(
						"_UI_MENU_ADD_DTD_INFORMATION")); //$NON-NLS-1$
	}

	protected Action createAddElementAction(Node parent,
			CMElementDeclaration ed, int index, int type) {
		Action action = null;
		if (ed == null) {
			return null;
		} else {
			action = new AddNodeAction(ed, parent, index, type);
		}
		return action;
	}

	protected Action createAddProcessingInstructionAction(Node parent, int index) {
		return null;
	}

	protected Action createAddSchemaInfoAction(Element element) {
		return null;
	}

	protected Action createEditAttributeAction(Attr attr,
			CMAttributeDeclaration ad) {
		return null;
		// return new EditAttributeAction(this, attr.getOwnerElement(), attr,
		// XMLCommonResources.getInstance().getString("_UI_MENU_EDIT_ATTRIBUTE"),
		// XMLCommonResources.getInstance().getString("_UI_MENU_EDIT_ATTRIBUTE_TITLE"));
		// //$NON-NLS-1$ //$NON-NLS-2$
	}

	protected Action createEditDoctypeAction(DocumentType doctype) {
		return new EditDoctypeAction(model, doctype, model.getBaseLocation(),
				XMLCommonResources.getInstance().getString(
						"_UI_MENU_EDIT_DOCTYPE")); //$NON-NLS-1$
	}

	protected Action createEditProcessingInstructionAction(
			ProcessingInstruction pi) {
		return null;
	}

	protected Action createEditSchemaInfoAction(Element element) {
		return null;
	}

	protected Action createRenameAction(Node node) {
		Action result = null;
		return result;
	}

	protected Action createReplaceAction(Node parent, CMNode cmnode,
			int startIndex, int endIndex) {
		return new ReplaceNodeAction(parent, cmnode, startIndex, endIndex);
	}

	protected Action createDeleteAction(List selection) {
		DeleteAction deleteAction = new DeleteAction(selection);
		deleteAction.setEnabled(selection.size() > 0);
		return deleteAction;
	}

	/**
	 * AddNodeAction
	 */
	public class AddNodeAction extends NodeAction {
		protected String description;

		protected String undoDescription;

		protected int nodeType;

		protected CMNode cmnode;

		protected int index;

		protected Node parent;

		protected int type = 2;

		public AddNodeAction(CMNode cmnode, Node parent, int index, int type) {
			this(cmnode, parent, index);
			this.type = type;
		}

		public AddNodeAction(CMNode cmnode, Node parent, int index) {
			this.cmnode = cmnode;
			this.parent = parent;
			this.index = index;

			String text = getLabel(parent, cmnode);
			setText(text);
			description = text;
			undoDescription = XMLCommonResources.getInstance().getString(
					"_UI_MENU_ADD") + " " + text; //$NON-NLS-1$ //$NON-NLS-2$
		}

		public void setAround(int type) {
			this.type = type;
		}

		public AddNodeAction(int nodeType, Node parent, int index) {
			this.nodeType = nodeType;
			this.index = index;
			this.parent = parent;

			switch (nodeType) {
			case Node.COMMENT_NODE: {
				description = XMLCommonResources.getInstance().getString(
						"_UI_MENU_COMMENT"); //$NON-NLS-1$
				undoDescription = XMLCommonResources.getInstance().getString(
						"_UI_MENU_ADD_COMMENT"); //$NON-NLS-1$
				break;
			}
			case Node.PROCESSING_INSTRUCTION_NODE: {
				description = XMLCommonResources.getInstance().getString(
						"_UI_MENU_PROCESSING_INSTRUCTION"); //$NON-NLS-1$
				undoDescription = XMLCommonResources.getInstance().getString(
						"_UI_MENU_ADD_PROCESSING_INSTRUCTION"); //$NON-NLS-1$
				break;
			}
			case Node.CDATA_SECTION_NODE: {
				description = XMLCommonResources.getInstance().getString(
						"_UI_MENU_CDATA_SECTION"); //$NON-NLS-1$
				undoDescription = XMLCommonResources.getInstance().getString(
						"_UI_MENU_ADD_CDATA_SECTION"); //$NON-NLS-1$
				break;
			}
			case Node.TEXT_NODE: {
				description = XMLCommonResources.getInstance().getString(
						"_UI_MENU_PCDATA"); //$NON-NLS-1$
				undoDescription = XMLCommonResources.getInstance().getString(
						"_UI_MENU_ADD_PCDATA"); //$NON-NLS-1$
				break;
			}
			}
			setText(description);
		}

		public String getUndoDescription() {
			return undoDescription;
		}

		public void run() {
			if (textNodeSplitter != null)
				textNodeSplitter.nodeSplit(type);
			beginNodeAction(this);
			if (cmnode != null) {
				addNodeForCMNode();
			} else {
				addNodeForNodeType();
			}
			endNodeAction(this);
		}

		protected void addNodeForCMNode() {
			if (parent != null) {
				insert(parent, cmnode, index, type);
			}
		}

		protected void addNodeForNodeType() {
			Document document = parent.getNodeType() == Node.DOCUMENT_NODE ? (Document) parent
					: parent.getOwnerDocument();
			Node newChildNode = null;
			boolean format = true;
			switch (nodeType) {
			case Node.COMMENT_NODE: {
				newChildNode = document.createComment(XMLCommonResources
						.getInstance().getString("_UI_COMMENT_VALUE")); //$NON-NLS-1$
				break;
			}
			case Node.PROCESSING_INSTRUCTION_NODE: {
				newChildNode = document
						.createProcessingInstruction(
								XMLCommonResources.getInstance().getString(
										"_UI_PI_TARGET_VALUE"), XMLCommonResources.getInstance().getString("_UI_PI_DATA_VALUE")); //$NON-NLS-1$ //$NON-NLS-2$
				break;
			}
			case Node.CDATA_SECTION_NODE: {
				newChildNode = document.createCDATASection(""); //$NON-NLS-1$
				break;
			}
			case Node.TEXT_NODE: {
				format = false;
				newChildNode = document.createTextNode(parent.getNodeName());
				break;
			}
			}

			if (newChildNode != null) {
				List list = new Vector(1);
				list.add(newChildNode);
				insertNodesAtIndex(type, parent, list, index, format);
			}
		}
	}

	/**
	 * ReplaceNodeAction
	 */
	public class ReplaceNodeAction extends NodeAction {
		protected Node parent;

		protected CMNode cmnode;

		protected int startIndex;

		protected int endIndex;

		protected String description;

		public ReplaceNodeAction(Node parent, CMNode cmnode, int startIndex,
				int endIndex) {
			this.parent = parent;
			this.cmnode = cmnode;
			this.startIndex = startIndex;
			this.endIndex = endIndex;

			setText(getLabel(parent, cmnode));
		}

		public String getUndoDescription() {
			String result = XMLCommonResources.getInstance().getString(
					"_UI_LABEL_UNDO_REPLACE_DESCRIPTION"); //$NON-NLS-1$
			result += " " + getLabel(parent, cmnode); //$NON-NLS-1$
			return result;
		}

		public void run() {
			beginNodeAction(this);

			if (parent != null && cmnode != null) {
				remove(parent, startIndex, endIndex);
				insert(parent, cmnode, startIndex, 2);
			}
			endNodeAction(this);
		}
	}

	/**
	 * 
	 */
	public String getLabel(Node parent, CMNode cmnode) {
		String result = "?" + cmnode + "?"; //$NON-NLS-1$ //$NON-NLS-2$
		if (cmnode != null) {
			result = (String) cmnode.getProperty("description"); //$NON-NLS-1$
			if (result == null) {
				if (cmnode.getNodeType() == CMNode.GROUP) {
					CMDescriptionBuilder descriptionBuilder = new CMDescriptionBuilder();
					result = descriptionBuilder.buildDescription(cmnode);
				} else {
					result = DOMNamespaceHelper.computeName(cmnode, parent,
							null);
				}
			}
		}
		return result;
	}

	public DOMContentBuilder createDOMContentBuilder(Document document) {
		DOMContentBuilderImpl builder = new DOMContentBuilderImpl(document);
		return builder;
	}

	public void beginNodeAction(NodeAction action) {
		model.beginRecording(action, action.getUndoDescription());
	}

	public void endNodeAction(NodeAction action) {
		model.endRecording(action);
	}

	/**
	 * DeleteAction
	 */
	public class DeleteAction extends NodeAction {
		protected List list;

		public DeleteAction(Node node) {
			setText(XMLCommonResources.getInstance().getString(
					"_UI_MENU_REMOVE")); //$NON-NLS-1$
			list = new Vector();
			list.add(node);
		}

		public DeleteAction(List list) {
			setText(XMLCommonResources.getInstance().getString(
					"_UI_MENU_REMOVE")); //$NON-NLS-1$
			this.list = list;
		}

		public void run() {
			beginNodeAction(this);

			for (Iterator i = list.iterator(); i.hasNext();) {
				Node node = (Node) i.next();
				if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
					Attr attr = (Attr) node;
					attr.getOwnerElement().removeAttributeNode(attr);
				} else {
					Node parent = node.getParentNode();
					if (parent != null) {
						Node previousSibling = node.getPreviousSibling();
						if (previousSibling != null
								&& isWhitespaceTextNode(previousSibling)) {
							parent.removeChild(previousSibling);
						}
						parent.removeChild(node);
					}
				}
			}

			endNodeAction(this);
		}

		public String getUndoDescription() {
			return XMLCommonResources.getInstance().getString("DELETE"); //$NON-NLS-1$
		}
	}

	public void fillContextMenuForVpe(IMenuManager menuManager,
		ISelection selection) {
		List selectionList = new ArrayList();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection es = (IStructuredSelection) selection;
			for (Iterator i = es.iterator(); i.hasNext();) {
				selectionList.add(i.next());
			}
		}

		contributeActionsForVpe(menuManager, selectionList);
	}

	public void addContextMenuForVpe(IMenuManager menuManager,
			ISelection selection) {
		List selectionList = new ArrayList();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection es = (IStructuredSelection) selection;
			for (Iterator i = es.iterator(); i.hasNext();) {
				selectionList.add(i.next());
			}
		}

		contributeDeleteActionForVpe(menuManager, selectionList);
	}

	public void remove(Node parent, int startIndex, int endIndex) {
		NodeList nodeList = parent.getChildNodes();
		for (int i = endIndex; i >= startIndex; i--) {
			Node node = nodeList.item(i);
			if (node != null) {
				parent.removeChild(node);
			}
		}
	}

	public void insert(Node parent, CMNode cmnode, int index, int type) {
		Document document = parent.getNodeType() == Node.DOCUMENT_NODE ? (Document) parent
				: parent.getOwnerDocument();
		DOMContentBuilder builder = createDOMContentBuilder(document);
		builder.setBuildPolicy(DOMContentBuilder.BUILD_ONLY_REQUIRED_CONTENT);
		builder.build(parent, cmnode);
		List list = builder.getResult();

		if (list.size() > 0
				&& ((Node) list.get(0)).getNodeName().equals("HTML")) { //$NON-NLS-1$
			Node node = (Node) list.get(0);
			Node child = node.getFirstChild();
			while (child != null) {
				node.removeChild(child);
				child = node.getFirstChild();
			}
		}
		insertNodesAtIndex(type, parent, list, index);
	}

	public void insertNodesAtIndex(int type, Node parent, List list, int index) {
		insertNodesAtIndex(type, parent, list, index, true);
	}

	public void insertNodesAtIndex(int type, Node parent, List list, int index,
			boolean format) {
		NodeList nodeList = parent.getChildNodes();
		if (index == -1) {
			index = nodeList.getLength();
		}
		Node refChild = (index < nodeList.getLength()) ? nodeList.item(index)
				: null;

		int prevIndex = index - 1;
		Node prevChild = (prevIndex < nodeList.getLength()) ? nodeList
				.item(prevIndex) : null;
		int nextIndex = index + 1;
		Node nextChild = (nextIndex < nodeList.getLength()) ? nodeList
				.item(nextIndex) : null;

		if (type == 1 && refChild != null) {
			parent.removeChild(refChild);
		} else {
			if (isWhitespaceTextNode(prevChild)) {
				refChild = prevChild;
			}
		}

		for (Iterator i = list.iterator(); i.hasNext();) {
			Node newNode = (Node) i.next();

			if (type == 1 && refChild != null) {
				newNode.appendChild(refChild);
			}
			if (newNode.getNodeType() == Node.ATTRIBUTE_NODE) {
				Element parentElement = (Element) parent;
				parentElement.setAttributeNode((Attr) newNode);
			} else {
				if (type == 1 && refChild != null) {
                    parent.insertBefore(newNode, nextChild);
                 //Added by estherbin
                 //Fix:http://jira.jboss.com/jira/browse/JBIDE-2275
                } else if (type == 2) {
                    parent.insertBefore(newNode, refChild);
                } else if (type == ITextNodeSplitter.INSERT_AFTER) {
                    parent.appendChild(newNode);
                } else {
                    parent.insertBefore(newNode, refChild);
                }
			}
		}

		boolean formatDeep = false;
		for (Iterator i = list.iterator(); i.hasNext();) {
			Node newNode = (Node) i.next();
			if (newNode.getNodeType() == Node.ELEMENT_NODE) {
				formatDeep = true;
			}

			if (format) {
				reformat(newNode, formatDeep);
			}
		}

		setViewerSelection(list);
	}

	public void setViewerSelection(List list) {
		if (viewer != null) {
			viewer.setSelection(new StructuredSelection(list), true);
		}
	}

	public void setViewerSelection(Node node) {
		if (viewer != null) {
			viewer.setSelection(new StructuredSelection(node), true);
		}
	}

	public Shell getWorkbenchWindowShell() {
		return XMLCommonResources.getInstance().getWorkbench()
				.getActiveWorkbenchWindow().getShell();
	}

	public IStructuredModel getModel() {
		return model;
	}

}
