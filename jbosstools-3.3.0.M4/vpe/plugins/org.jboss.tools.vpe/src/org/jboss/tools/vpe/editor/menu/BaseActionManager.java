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
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMDataType;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQueryAction;
import org.eclipse.wst.xml.core.internal.contentmodel.modelqueryimpl.ModelQueryActionHelper;
import org.eclipse.wst.xml.core.internal.contentmodel.modelqueryimpl.ModelQueryImpl;
import org.eclipse.wst.xml.ui.internal.actions.MenuBuilder;
import org.eclipse.wst.xml.ui.internal.util.XMLCommonResources;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;

public abstract class BaseActionManager {
	protected ModelQuery modelQuery;

	protected MenuBuilder menuBuilder = new MenuBuilder();

	protected IStructuredModel model;

	protected static ITextNodeSplitter textNodeSplitter;

	public static final String INSERT_AROUND_MENU = VpeUIMessages.BaseActionManager_InsertAround;
	public static final String INSERT_BEFORE_MENU = VpeUIMessages.BaseActionManager_InsertBefore;
	public static final String INSERT_AFTER_MENU = VpeUIMessages.BaseActionManager_InsertAfter;
	public static final String REPLACE_TAG_MENU = VpeUIMessages.BaseActionManager_ReplaceWith;
	public static final String INSERT_TAG_MENU = VpeUIMessages.BaseActionManager_InsertTag;

	static public void setTextNodeSplitter(ITextNodeSplitter splitter) {
		textNodeSplitter = splitter;
	}

	protected BaseActionManager(IStructuredModel model, ModelQuery modelQuery) {
		this.model = model;
		this.modelQuery = modelQuery;
	}

	abstract protected Action createAddAttributeAction(Element parent, CMAttributeDeclaration ad);

	abstract protected Action createAddCDataSectionAction(Node parent, int index);

	abstract protected Action createAddPCDataAction(Node parent, CMDataType dataType, int index);

	abstract protected Action createAddCommentAction(Node parent, int index);

	abstract protected Action createAddDoctypeAction(Document parent, int index);

	abstract protected Action createAddElementAction(Node parent, CMElementDeclaration ed, int index, int type);

	abstract protected Action createAddProcessingInstructionAction(Node parent, int index);

	abstract protected Action createAddSchemaInfoAction(Element element);

	abstract protected Action createEditAttributeAction(Attr attribute, CMAttributeDeclaration ad);

	abstract protected Action createEditDoctypeAction(DocumentType doctype);

	abstract protected Action createEditProcessingInstructionAction(ProcessingInstruction pi);

	abstract protected Action createEditSchemaInfoAction(Element element);

	abstract protected Action createRenameAction(Node node);

	abstract protected Action createReplaceAction(Node parent, CMNode cmnode, int startIndex, int endIndex);

	abstract protected Action createDeleteAction(List selection);

	public void contributeActionsForVpe(IMenuManager menu, List selection) {
		int editMode = modelQuery.getEditMode();
		int includeOptions = (editMode == ModelQuery.EDIT_MODE_CONSTRAINED_STRICT) ? ModelQuery.INCLUDE_CHILD_NODES
				| ModelQuery.INCLUDE_SEQUENCE_GROUPS
				: ModelQuery.INCLUDE_CHILD_NODES;
		int validityChecking = (editMode == ModelQuery.EDIT_MODE_CONSTRAINED_STRICT) ? ModelQuery.VALIDITY_STRICT
				: ModelQuery.VALIDITY_NONE;

		List implicitlySelectedNodeList = null;

		if (selection.size() > 0) {
			implicitlySelectedNodeList = getSelectedNodes(selection, true);
			if (selection.size() == 1) {
				Node node = (Node) selection.get(0);

				// contribute add before actions
				contributeAddSiblingActions(menu, node, true);
			}

			// contribute replace actions
			contributeReplaceActions(menu, implicitlySelectedNodeList, includeOptions, validityChecking);
		} else {
			IMenuManager addTagMenu = new MyMenuManager(INSERT_TAG_MENU, true);
			menu.add(addTagMenu);
		}
	}

	public void contributeDeleteActionForVpe(IMenuManager menu, List selection) {
		int editMode = modelQuery.getEditMode();
		int includeOptions = (editMode == ModelQuery.EDIT_MODE_CONSTRAINED_STRICT) ? ModelQuery.INCLUDE_CHILD_NODES
				| ModelQuery.INCLUDE_SEQUENCE_GROUPS
				: ModelQuery.INCLUDE_CHILD_NODES;
		int validityChecking = (editMode == ModelQuery.EDIT_MODE_CONSTRAINED_STRICT) ? ModelQuery.VALIDITY_STRICT
				: ModelQuery.VALIDITY_NONE;

		List implicitlySelectedNodeList = null;

		if (selection.size() > 0) {
			implicitlySelectedNodeList = getSelectedNodes(selection, true);

			menu.add(new Separator());
			// contribute delete actions
			contributeDeleteActions(menu, implicitlySelectedNodeList, includeOptions, validityChecking);
		}
	}

	protected void contributeAction(IMenuManager menu, Action action) {
		if (action != null) {
			menu.add(action);
		}
	}

	protected void contributeEditActions(IMenuManager menu, Node node) {
		contributeEditGrammarInformationActions(menu, node);

		if (node.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE) {
			contributeAction(
					menu,
					createEditProcessingInstructionAction((ProcessingInstruction) node));
		} else if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
			contributeAction(menu, createEditAttributeAction((Attr) node, null));
		}
	}

	protected void contributeEditGrammarInformationActions(IMenuManager menu,
			Node node) {
		Document document = node.getNodeType() == Node.DOCUMENT_NODE ? (Document) node
				: node.getOwnerDocument();

		DocumentType doctype = getDoctype(node);
		if (doctype == null) {
			contributeAction(menu, createAddDoctypeAction(document, -1));
		}

		if (node.getNodeType() == Node.DOCUMENT_TYPE_NODE) {
			contributeAction(menu, createEditDoctypeAction((DocumentType) node));
		}

		if (doctype == null && getRootElement(document) != null) {
			contributeAction(menu,
					createEditSchemaInfoAction(getRootElement(document)));
		}
	}

	protected void contributeAddChildActions(IMenuManager menu, Node node,
			int includeOptions, int validityChecking) {
		int nodeType = node.getNodeType();

		if (nodeType == Node.ELEMENT_NODE) {
			// 'Add Child...' and 'Add Attribute...' actions
			//
			Element element = (Element) node;

			IMenuManager addAttributeMenu = new MyMenuManager(
					XMLCommonResources.getInstance().getString(
							"_UI_MENU_ADD_ATTRIBUTE")); //$NON-NLS-1$
			IMenuManager addChildMenu = new MyMenuManager(XMLCommonResources
					.getInstance().getString("_UI_MENU_ADD_CHILD")); //$NON-NLS-1$
			menu.add(addAttributeMenu);
			menu.add(addChildMenu);

			CMElementDeclaration ed = modelQuery
					.getCMElementDeclaration(element);
			if (ed != null) {
				// add insert attribute actions
				//
				List modelQueryActionList = new ArrayList();
				modelQuery
						.getInsertActions(element, ed, -1,
								ModelQuery.INCLUDE_ATTRIBUTES, validityChecking,
								modelQueryActionList);
				addActionHelper(addAttributeMenu, modelQueryActionList, 2);

				// add insert child node actions
				//
				modelQueryActionList = new ArrayList();
				modelQuery.getInsertActions(element, ed, -1, includeOptions, validityChecking,
						modelQueryActionList);
				addActionHelper(addChildMenu, modelQueryActionList, 2);
			}

			// add PI and COMMENT
			contributePIAndCommentActions(addChildMenu, element, ed, -1);

			// add PCDATA, CDATA_SECTION
			contributeTextNodeActions(addChildMenu, element, ed, -1);

			// add NEW ELEMENT
			contributeUnconstrainedAddElementAction(addChildMenu, element, ed,
					-1);

			// add ATTRIBUTE
			contributeUnconstrainedAttributeActions(addAttributeMenu, element,
					ed);
		}
	}

	protected void contributeAddSiblingActions(IMenuManager menu, Node node,
			boolean visible) {
		IMenuManager addAroundMenu = new MyMenuManager(INSERT_AROUND_MENU, visible);
		IMenuManager addBeforeMenu = new MyMenuManager(INSERT_BEFORE_MENU, visible);
		IMenuManager addAfterMenu = new MyMenuManager(INSERT_AFTER_MENU, visible);
		menu.add(addAroundMenu);
		menu.add(addBeforeMenu);
		menu.add(addAfterMenu);
	}

	protected void contributeAddDocumentChildActions(IMenuManager menu,
			Document document) {
		IMenuManager addChildMenu = new MyMenuManager(XMLCommonResources
				.getInstance().getString("_UI_MENU_ADD_CHILD")); //$NON-NLS-1$
		menu.add(addChildMenu);

		// add PI and COMMENT
		contributePIAndCommentActions(addChildMenu, document, -1);

		// add NEW ELEMENT
		contributeUnconstrainedAddElementAction(addChildMenu, document, -1);
	}

	protected void contributeReplaceActions(IMenuManager menu,
			List selectedNodeList, int includeOptions, int validityChecking) {
		// 'Replace With...' actions
		//                        
		//Fix for JBIDE-3428
		IMenuManager replaceWithMenu = new MyMenuManager(BaseActionManager.REPLACE_TAG_MENU);
		menu.add(replaceWithMenu);

		if (modelQuery.getEditMode() == ModelQuery.EDIT_MODE_CONSTRAINED_STRICT
				&& selectedNodeList.size() > 0) {
			
			Node node = (Node) selectedNodeList.get(0);
			Node parentNode = node.getParentNode();
			if (parentNode != null
					&& parentNode.getNodeType() == Node.ELEMENT_NODE) {
				Element parentElement = (Element) parentNode;
				CMElementDeclaration parentED = modelQuery
						.getCMElementDeclaration(parentElement);
				if (parentED != null) {
					List replaceActionList = new Vector();
					modelQuery.getReplaceActions(parentElement, parentED,
							selectedNodeList, includeOptions, validityChecking,
							replaceActionList);
					addActionHelper(replaceWithMenu, replaceActionList, 2);
				}
			}
		}
	}

	public static DocumentType getDoctype(Node node) {
		Document document = (node.getNodeType() == Node.DOCUMENT_NODE) ? (Document) node
				: node.getOwnerDocument();
		return document.getDoctype();
	}

	protected void contributeDeleteActions(IMenuManager menu, List list,
			int includeOptions, int validityChecking) {
		boolean canRemove = modelQuery.canRemove(list, validityChecking);

		// a delete action with an empty list will produce a disabled menu item
		//
		List resultList = canRemove ? list : Collections.EMPTY_LIST;

		contributeAction(menu, createDeleteAction(resultList));
	}

	protected void contributePIAndCommentActions(IMenuManager menu,
			Element parentElement, CMElementDeclaration parentEd, int index) {
		if (parentEd == null || isCommentAllowed(parentEd)) {
			contributeAction(menu, createAddCommentAction(parentElement, index));
			contributeAction(menu, createAddProcessingInstructionAction(
					parentElement, index));
		}
	}

	protected void contributeTextNodeActions(IMenuManager menu,
			Element parentElement, CMElementDeclaration parentEd, int index) {
		if (parentEd == null || isTextAllowed(parentEd)) {
			CMDataType dataType = parentEd != null ? parentEd.getDataType()
					: null;
			contributeAction(menu, createAddPCDataAction(parentElement,
					dataType, index));
			contributeAction(menu, createAddCDataSectionAction(parentElement,
					index));
		}
	}

	protected void contributePIAndCommentActions(IMenuManager menu,
			Document document, int index) {
		// test to make sure that the index isn't before the XML declaration
		// 
		contributeAction(menu, createAddCommentAction(document, index));
		contributeAction(menu, createAddProcessingInstructionAction(document,
				index));
	}

	protected void contributeUnconstrainedAddElementAction(IMenuManager menu,
			Element parentElement, CMElementDeclaration parentEd, int index) {
		if (isUnconstrainedActionAllowed()) {
			if (parentEd == null
					|| parentEd.getProperty("isInferred") == Boolean.TRUE  //$NON-NLS-1$
					|| ( modelQuery.getEditMode() != ModelQuery.EDIT_MODE_CONSTRAINED_STRICT 
							&& isElementAllowed(parentEd) )) {
				contributeAction(menu, createAddElementAction(parentElement,
						null, index, 2));
			}
		}
	}

	protected void contributeUnconstrainedAddElementAction(IMenuManager menu,
			Document document, int index) {
		if (isUnconstrainedActionAllowed()) {
			if (getRootElement(document) == null) {
				int xmlDeclarationIndex = -1;
				int doctypeIndex = -1;
				NodeList nodeList = document.getChildNodes();
				int nodeListLength = nodeList.getLength();
				for (int i = 0; i < nodeListLength; i++) {
					Node node = nodeList.item(i);
					int nodeType = node.getNodeType();
					if (nodeType == Node.DOCUMENT_TYPE_NODE) {
						doctypeIndex = i;
						break;
					} else if (nodeType == Node.PROCESSING_INSTRUCTION_NODE) {
						ProcessingInstruction pi = (ProcessingInstruction) node;
						if (pi.getTarget().equalsIgnoreCase("xml") && xmlDeclarationIndex == -1) { //$NON-NLS-1$
							xmlDeclarationIndex = i;
						}
					}
				}

				if ((xmlDeclarationIndex == -1 || index > xmlDeclarationIndex)
						&& (doctypeIndex == -1 || index > doctypeIndex)) {
					contributeAction(menu, createAddElementAction(document,
							null, index, 2));
				}
			}
		}
	}

	protected void contributeUnconstrainedAttributeActions(IMenuManager menu,
			Element parentElement, CMElementDeclaration parentEd) {
		if (isUnconstrainedActionAllowed()) {
			if (parentEd == null
					|| parentEd.getProperty("isInferred") == Boolean.TRUE //$NON-NLS-1$
					|| modelQuery.getEditMode() != ModelQuery.EDIT_MODE_CONSTRAINED_STRICT) {
				contributeAction(menu, createAddAttributeAction(parentElement,
						null));
			}
		}
	}

	protected void addActionHelper(IMenuManager menu,
			List modelQueryActionList, int type) {
		List actionList = new Vector();
		for (Iterator i = modelQueryActionList.iterator(); i.hasNext();) {
			ModelQueryAction action = (ModelQueryAction) i.next();
			if (action.getCMNode() != null) {
				int cmNodeType = action.getCMNode().getNodeType();
				if (action.getKind() == ModelQueryAction.INSERT) {
					switch (cmNodeType) {
					case CMNode.ATTRIBUTE_DECLARATION: {
						actionList.add(createAddAttributeAction(
								(Element) action.getParent(),
								(CMAttributeDeclaration) action.getCMNode()));
						break;
					}
					case CMNode.ELEMENT_DECLARATION: {
						actionList.add(createAddElementAction(action
								.getParent(), (CMElementDeclaration) action
								.getCMNode(), action.getStartIndex(), type));
						break;
					}
					}
				} else if (action.getKind() == ModelQueryAction.REPLACE) {
					if (action.getParent() != null
							&& action.getCMNode() != null) {
						actionList.add(createReplaceAction(action.getParent(),
								action.getCMNode(), action.getStartIndex(),
								action.getEndIndex()));
					}
				}
			}
		}
		menuBuilder.populateMenu(menu, actionList, false);
	}

	protected boolean isCommentAllowed(CMElementDeclaration parentEd) {
		int contentType = parentEd.getContentType();
		return contentType == CMElementDeclaration.ELEMENT
				|| contentType == CMElementDeclaration.MIXED
				|| contentType == CMElementDeclaration.PCDATA
				|| contentType == CMElementDeclaration.ANY;
	}

	protected boolean isElementAllowed(CMElementDeclaration parentEd) {
		int contentType = parentEd.getContentType();
		return contentType == CMElementDeclaration.ELEMENT
				|| contentType == CMElementDeclaration.MIXED
				|| contentType == CMElementDeclaration.ANY;
	}

	protected boolean isTextAllowed(CMElementDeclaration parentEd) {
		int contentType = parentEd.getContentType();
		return contentType == CMElementDeclaration.MIXED
				|| contentType == CMElementDeclaration.PCDATA
				|| contentType == CMElementDeclaration.ANY;
	}

	protected boolean isUnconstrainedActionAllowed() {
		return true;
	}

	public int getIndex(Node parentNode, Node child) {
		NodeList nodeList = parentNode.getChildNodes();
		int index = -1;
		int size = nodeList.getLength();
		for (int i = 0; i < size; i++) {
			if (nodeList.item(i) == child) {
				index = i;
				break;
			}
		}
		return index;
	}

	public Node getRefChildNodeAtIndex(Node parent, int index) {
		NodeList nodeList = parent.getChildNodes();
		Node refChild = (index >= 0 && index < nodeList.getLength()) ? nodeList
				.item(index) : null;
		return refChild;
	}

	protected boolean isWhitespaceTextNode(Node node) {
		return (node != null) && (node.getNodeType() == Node.TEXT_NODE)
				&& (node.getNodeValue().trim().length() == 0);
	}

	protected Element getRootElement(Document document) {
		Element result = null;
		NodeList nodeList = document.getChildNodes();
		int nodeListLength = nodeList.getLength();
		for (int i = 0; i < nodeListLength; i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				result = (Element) node;
				break;
			}
		}
		return result;
	}

	protected List getSelectedNodes(List list, boolean includeTextNodes) {
		List result = new ArrayList(0);
		for (Iterator i = list.iterator(); i.hasNext();) {
			Object object = i.next();
			if (object instanceof Node) {
				Node node = (Node) object;
				if (node.getNodeType() == Node.TEXT_NODE) {
					if (includeTextNodes) {
						result.add(object);
					}
				} else {
					result.add(node);
				}
			}
		}
		return result;
	}

	/**
	 * MyMenuManager
	 */
	public static class MyMenuManager extends MenuManager {
		protected String title;

		private boolean visualForever = false;

		public MyMenuManager(String s, boolean visualForever) {
			this(s);
			this.visualForever = visualForever;
		}

		public MyMenuManager(String s) {
			super(s);
			title = s;
		}

		public boolean isVisible() {
			if (visualForever)
				return true;
			else
				return super.isVisible();
		}

		public boolean isEnabled() {
			return !isEmpty();
		}

		public String toString() {
			return title;
		}
	}

	static HashSet noContainerTags = new HashSet();
	static {
		noContainerTags.add("basefont"); //$NON-NLS-1$
		noContainerTags.add("bgsound"); //$NON-NLS-1$
		noContainerTags.add("br"); //$NON-NLS-1$
		noContainerTags.add("img"); //$NON-NLS-1$
		noContainerTags.add("input"); //$NON-NLS-1$
		noContainerTags.add("isindex"); //$NON-NLS-1$
		noContainerTags.add("script"); //$NON-NLS-1$
		noContainerTags.add("wbr"); //$NON-NLS-1$
	}

	public static class ActionHelper extends ModelQueryActionHelper {
		public ActionHelper(ModelQueryImpl query) {
			super(query);
		}

		public List modifyActionList(List actionList) {
			CMNode nImpl;
			for (int i = actionList.size() - 1; i >= 0; i--) {
				nImpl = (CMNode) ((ModelQueryActionHelper.Action) actionList
						.get(i)).getCMNode();
				if (noContainerTags.contains(nImpl.getNodeName().toLowerCase())) {
					actionList.remove(i);
				}
			}
			return actionList;
		}
	}
}