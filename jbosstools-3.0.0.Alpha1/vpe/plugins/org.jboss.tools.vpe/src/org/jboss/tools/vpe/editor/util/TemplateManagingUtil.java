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

import org.eclipse.swt.graphics.Point;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.mapping.VpeElementMapping;
import org.jboss.tools.vpe.editor.mapping.VpeNodeMapping;
import org.jboss.tools.vpe.editor.template.VpeTemplate;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMHTMLInputElement;
import org.mozilla.interfaces.nsIDOMNSHTMLInputElement;
import org.mozilla.interfaces.nsIDOMNSHTMLTextAreaElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMRange;
import org.mozilla.interfaces.nsISelection;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @deprecated
 * @author S. Dzmitrovich
 *
 */
public class TemplateManagingUtil {

	/**
	 * name of "view" tag
	 */
	private static final String VIEW_TAGNAME = "view"; //$NON-NLS-1$

	/**
	 * name of "locale" attribute
	 */
	private static final String LOCALE_ATTRNAME = "locale"; //$NON-NLS-1$

	/**
	 * get template of selected element
	 * 
	 * @param pageContext
	 * @return
	 */
	public static VpeTemplate getTemplateByVisualSelection(
			VpePageContext pageContext, nsIDOMNode selectedNode) {
		// get element mapping

		VpeElementMapping elementMapping = getElementMappingByVisualSelection(
				pageContext, selectedNode);

		if (elementMapping != null)
			return elementMapping.getTemplate();

		return null;
	}

	/**
	 * 
	 * @param pageContext
	 * @param selectedNode
	 */
	public static VpeElementMapping getElementMappingByVisualSelection(
			VpePageContext pageContext, nsIDOMNode selectedNode) {

		// get element mapping
		VpeElementMapping elementMapping = pageContext.getDomMapping()
				.getNearElementMapping(selectedNode);

		if (elementMapping != null)
			return elementMapping;

		/*
		 * next code is necessary for get template some jsf elements (which have
		 * escape="false" attribute). It's necessary for the current
		 * implementation of escape="false" attribute's process. When or if the
		 * implementation of escape="false" attribute's process will changed,
		 * you will must review next code
		 */
		VpeNodeMapping nodeMapping = pageContext.getDomMapping()
				.getNearNodeMapping(selectedNode);

		if (nodeMapping != null) {

			/*
			 * get node. This node is not ascribe (may be) to DOM model of page,
			 * because "value" attribute is parsed (if escape ="false")
			 * separately and is built in vpe. But it has correct offset
			 * information
			 */
			IDOMNode mappingNode = (IDOMNode) nodeMapping.getSourceNode();

			// get document
			IDOMDocument sourceDocument = getSourceDocument(pageContext);

			// get source node by ofsset
			Node sourceNode = getSourceNodeByPosition(sourceDocument,
					mappingNode.getStartOffset());
			// find elementMapping by source node
			if (sourceNode != null) {
				VpeElementMapping mapping = pageContext.getDomMapping()
						.getNearElementMapping(sourceNode);
				if (mapping != null)
					return mapping;
			}

		}

		return null;

	}

	/**
	 * get template by source Selection
	 * 
	 * @param pageContext
	 * @param focus
	 * @param anchor
	 * @return
	 */
	public static VpeTemplate getTemplateBySourceSelection(
			VpePageContext pageContext, int focus, int anchor) {

		VpeElementMapping elementMapping = getElementMappingBySourceSelection(
				pageContext, focus, anchor);

		if (elementMapping != null)
			return elementMapping.getTemplate();

		return null;
	}

	/**
	 * 
	 * @param pageContext
	 * @param selectedRange
	 * @return
	 */
	public static VpeElementMapping getElementMappingBySourceSelection(
			VpePageContext pageContext, Point selectedRange) {

		return getElementMappingBySourceSelection(pageContext, selectedRange.x,
				selectedRange.x + selectedRange.y);
	}

	/**
	 * get element mapping by source Selection
	 * 
	 * @param pageContext
	 * @param focus
	 * @param anchor
	 * @return
	 */
	public static VpeElementMapping getElementMappingBySourceSelection(
			VpePageContext pageContext, int focus, int anchor) {

		// get document
		IDOMDocument sourceDocument = getSourceDocument(pageContext);

		/*
		 * implementation of IDOMModel's method getIndexedRegion(...) has one
		 * feature : if cursor is situated at the border of elements then this
		 * method return next element. For example ... <h:inputText ../><h:outputText/>... -
		 * if cursor will be situated at the right border of "h:inputText"
		 * element then getIndexedRegion() return "h:outputText" element. So for
		 * focus position we choose smaller value
		 */

		if (anchor < focus) {
			focus = anchor;
			anchor = focus;
		}

		// get source node by offset
		Node focusNode = getSourceNodeByPosition(sourceDocument, focus);

		// if focus node also contain anchor point (selected only 1 element)
		if ((focusNode != null) && isNodeContainPosition(focusNode, anchor)) {

			return pageContext.getDomMapping().getNearElementMapping(focusNode);

		}
		return null;

	}

	/**
	 * get element mapping by source Selection
	 * 
	 * @param pageContext
	 * @param focus
	 * @param anchor
	 * @return
	 */
	public static VpeElementMapping getElementMappingBySourcePosition(
			VpePageContext pageContext, int focus) {

		// get document
		IDOMDocument sourceDocument = getSourceDocument(pageContext);

		// get source node by offset
		Node focusNode = getSourceNodeByPosition(sourceDocument, focus);

		// if focus node also contain anchor point (selected only 1 element)
		if ((focusNode != null)) {

			VpeElementMapping elementMapping = pageContext.getDomMapping()
					.getNearElementMapping(focusNode);

			return elementMapping;

		}
		return null;

	}

	private static boolean isNodeContainPosition(Node node, int position) {

		if ((((IDOMNode) node).getStartOffset() <= position)
				&& (((IDOMNode) node).getEndOffset() >= position))
			return true;

		return false;
	}

	/**
	 * get source node by position
	 * 
	 * @param pageContext
	 * @param position
	 * @return
	 */
	private static Node getSourceNodeByPosition(IDOMDocument document,
			int position) {

		// get source node by position
		IDOMNode node = (IDOMNode) document.getModel().getIndexedRegion(
				position);

		return node;
	}

	private static IDOMDocument getSourceDocument(VpePageContext pageContext) {

		return (IDOMDocument) pageContext.getSourceBuilder()
				.getSourceDocument();
	}

	/**
	 * get selected element
	 * 
	 * @param pageContext
	 * @return
	 */
	public static nsIDOMElement getLastSelectedVisualNode(
			VpePageContext pageContext) {

		return pageContext.getEditPart().getController().getXulRunnerEditor()
				.getLastSelectedElement();
	}

	/**
	 * 
	 * @param selection
	 * @return
	 */
	public static nsIDOMNode getSelectedNode(nsISelection selection) {
	
		if (selection.getFocusNode() == selection.getAnchorNode()) {
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
	 * get start offset of node
	 * 
	 * @param node
	 * @return
	 */
	public static int getStartOffsetNode(Node node) {

		if (node instanceof IDOMAttr) {
			return ((IDOMAttr) node).getValueRegionStartOffset() + 1;
		} else if (node instanceof IndexedRegion) {
			return ((IndexedRegion) node).getStartOffset();
		}
		return 0;
	}

	/**
	 * 
	 * @param node
	 * @return
	 */
	public static int getLengthNode(Node node) {

		if (node instanceof IDOMAttr) {
			return ((IDOMAttr) node).getValueSource().length();
		} else if (node instanceof IndexedRegion) {
			return ((IndexedRegion) node).getEndOffset()
					- ((IndexedRegion) node).getStartOffset();
		}
		return 0;
	}

	/**
	 * get end offset of node
	 * 
	 * @param node
	 * @return
	 */
	public static int getEndOffsetNode(Node node) {

		if (node instanceof IndexedRegion) {
			return ((IndexedRegion) node).getEndOffset();
		}
		return 0;
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

		int start = getStartOffsetNode(node);

		pageContext.getSourceBuilder().getStructuredTextViewer()
				.setSelectedRange(start + offset, length);
		pageContext.getSourceBuilder().getStructuredTextViewer().revealRange(
				start + offset, length);

	}

	/**
	 * 
	 * @param pageContext
	 * @return
	 */
	public static nsISelection getCurrentSelection(VpePageContext pageContext) {
		return pageContext.getEditPart().getController().getXulRunnerEditor()
				.getSelection();
	}

	/**
	 * 
	 * @param pageContext
	 * @param sourceElement
	 * @return
	 */
	public static String getPageLocale(VpePageContext pageContext,
			Node sourceNode) {

		while (sourceNode != null) {

			if (VIEW_TAGNAME.equals(sourceNode.getLocalName())) {
				break;
			}
			sourceNode = sourceNode.getParentNode();
		}

		if ((sourceNode == null) || !(sourceNode instanceof Element)
				|| !(((Element) sourceNode).hasAttribute(LOCALE_ATTRNAME)))
			return null;

		String locale = ((Element) sourceNode).getAttribute(LOCALE_ATTRNAME);

		return locale;

	}

	public static Point getSelectionRangeFromInputElement(nsIDOMNode node) {

		Point range = new Point(0, 0);

		if (node != null)
			if (HTML.TAG_INPUT.equalsIgnoreCase(node.getLocalName())) {

				nsIDOMNSHTMLInputElement inputElement = (nsIDOMNSHTMLInputElement) node
						.queryInterface(nsIDOMNSHTMLInputElement.NS_IDOMNSHTMLINPUTELEMENT_IID);

				range.x = inputElement.getSelectionStart();
				range.y = inputElement.getSelectionEnd()
						- inputElement.getSelectionStart();
			} else if (HTML.TAG_TEXTAREA.equalsIgnoreCase(node.getLocalName())) {

				nsIDOMNSHTMLTextAreaElement textAreaElement = (nsIDOMNSHTMLTextAreaElement) node
						.queryInterface(nsIDOMNSHTMLTextAreaElement.NS_IDOMNSHTMLTEXTAREAELEMENT_IID);
				range.x = textAreaElement.getSelectionStart();
				range.y = textAreaElement.getSelectionEnd()
						- textAreaElement.getSelectionStart();

			}

		return range;

	}

	/**
	 * 
	 * @param node
	 * @param range
	 */
	public static void setSelectionRangeInInputElement(nsIDOMNode node,
			Point range) {

		if ((node != null) && (range != null))
			if (HTML.TAG_INPUT.equalsIgnoreCase(node.getLocalName())) {
				
				nsIDOMHTMLInputElement inputElement = (nsIDOMHTMLInputElement) node
						.queryInterface(nsIDOMHTMLInputElement.NS_IDOMHTMLINPUTELEMENT_IID);
				System.out.print("\ninput:"+inputElement.getType());
//				nsIDOMNSHTMLInputElement inputElement = (nsIDOMNSHTMLInputElement) node
//						.queryInterface(nsIDOMNSHTMLInputElement.NS_IDOMNSHTMLINPUTELEMENT_IID);
//				inputElement.setSelectionRange(range.x, range.x + range.y);
			} else if (HTML.TAG_TEXTAREA.equalsIgnoreCase(node.getLocalName())) {
				nsIDOMNSHTMLTextAreaElement textAreaElement = (nsIDOMNSHTMLTextAreaElement) node
						.queryInterface(nsIDOMNSHTMLTextAreaElement.NS_IDOMNSHTMLTEXTAREAELEMENT_IID);
				textAreaElement.setSelectionRange(range.x, range.x + range.y);
			}
	}

	/**
	 * 
	 * @param pageContext
	 * @param startPosition
	 * @param endPosition
	 * @return
	 */
	public static String getSourceText(VpePageContext pageContext,
			int startPosition, int endPosition) {

		return pageContext.getSourceBuilder().getStructuredTextViewer()
				.getTextWidget().getText(startPosition, endPosition);
	}
}
