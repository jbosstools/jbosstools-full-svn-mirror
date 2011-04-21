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
package org.jboss.tools.vpe.editor.template;

import org.eclipse.jface.text.IRegion;
import org.jboss.tools.jst.jsp.editor.ITextFormatter;
import org.jboss.tools.vpe.editor.VpeSourceInnerDragInfo;
import org.jboss.tools.vpe.editor.VpeSourceInnerDropInfo;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.mapping.NodeData;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.mapping.VpeElementData;
import org.jboss.tools.vpe.editor.mapping.VpeElementMapping;
import org.jboss.tools.vpe.editor.selection.VpeSourceSelection;
import org.jboss.tools.vpe.editor.template.textformating.TextFormatingData;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMText;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public interface VpeTemplate {

	/**
	 * Initiates template after its creating
	 * 
	 * @param templateElement
	 *            <code>Element</code> with a name "vpe:template" from the
	 *            template file
	 * @param caseSensitive
	 *            The case sensitive of an element of a source file
	 */
	void init(Element templateElement, boolean caseSensitive);

	/**
	 * Creates a node of the visual tree on the node of the source tree. This
	 * visual node should not have the parent node This visual node can have
	 * child nodes.
	 * 
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param sourceNode
	 *            The current node of the source tree.
	 * @param visualDocument
	 *            The document of the visual tree.
	 * @return The information on the created node of the visual tree.
	 */
	VpeCreationData create(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument);

	/**
	 * Is invoked after construction of all child nodes of the current visual
	 * node.
	 * 
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param sourceNode
	 *            The current node of the source tree.
	 * @param visualDocument
	 *            The document of the visual tree.
	 * @param data
	 *            Object <code>VpeCreationData</code>, built by a method
	 *            <code>create</code>
	 */
	void validate(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument, VpeCreationData data);

	/**
	 * Processes keyboard input (without the pressed key Ctrl)
	 * 
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param sourceDocument
	 *            The document of the source tree.
	 * @param sourceNode
	 *            The current node of the source tree.
	 * @param visualNode
	 *            The current node of the visual tree.
	 * @param data
	 *            The arbitrary data, built by a method <code>create</code>
	 * @param charCode
	 *            Code of the pressed key
	 * @param selection
	 *            The current selection
	 * @param formatter
	 *            Interface for formatting the source text
	 * @return <code>true</code> if the key is processed
	 */
	boolean nonctrlKeyPressHandler(VpePageContext pageContext,
			Document sourceDocument, Node sourceNode, nsIDOMNode visualNode,
			Object data, long charCode, VpeSourceSelection selection,
			ITextFormatter formatter);

	/**
	 * Sets value of attribute of the current visual element. Is invoked at
	 * change of attribute of an source element.
	 * 
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param sourceElement
	 *            The current element of the source tree.
	 * @param visualDocument
	 *            The document of the visual tree.
	 * @param visualNode
	 *            The current node of the visual tree.
	 * @param data
	 *            The arbitrary data, built by a method <code>create</code>
	 * @param name
	 *            Attribute name.
	 * @param value
	 *            Attribute value.
	 */
	void setAttribute(VpePageContext pageContext, Element sourceElement,
			nsIDOMDocument visualDocument, nsIDOMNode visualNode, Object data, String name,
			String value);

	/**
	 * Informs on remove of attribute of the current source element.
	 * 
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param sourceElement
	 *            The current element of the source tree.
	 * @param visualDocument
	 *            The document of the visual tree.
	 * @param visualNode
	 *            The current node of the visual tree.
	 * @param data
	 *            The arbitrary data, built by a method <code>create</code>
	 * @param name
	 *            Attribute name.
	 */
	void removeAttribute(VpePageContext pageContext, Element sourceElement,
			nsIDOMDocument visualDocument, nsIDOMNode visualNode, Object data, String name);

	/**
	 * Is invoked before removal of the visiblis node from the tree
	 * 
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param sourceNode
	 *            The current node of the source tree.
	 * @param visualNode
	 *            The current node of the visual tree.
	 * @param data
	 *            The arbitrary data, built by a method <code>create</code>
	 */
	void beforeRemove(VpePageContext pageContext, Node sourceNode,
			nsIDOMNode visualNode, Object data);

	/**
	 * At a modification of the node of an source tree, the method update for
	 * this node is invoked. Template can indicate other node for update
	 * 
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param sourceNode
	 *            The current node of the source tree.
	 * @param visualNode
	 *            The current node of the visual tree.
	 * @param data
	 *            The arbitrary data, built by a method <code>create</code>
	 * @return For this node of an source tree the method update is invoked. If
	 *         null, that is invoked update for current source node
	 */
	Node getNodeForUpdate(VpePageContext pageContext, Node sourceNode,
			nsIDOMNode visualNode, Object data);

	/**
	 * Is invoked at resize of an element visual tree
	 * 
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param sourceElement
	 *            The current element of the source tree.
	 * @param visualDocument
	 *            The document of the visual tree.
	 * @param visualElement
	 *            The current element of the visual tree.
	 * @param data
	 *            The arbitrary data, built by a method <code>create</code>
	 * @param constrains
	 *            Code of resizer:<br>
	 *            top-left: 1<br>
	 *            top: 2<br>
	 *            top-right: 4<br>
	 *            left: 8<br>
	 *            right: 16<br>
	 *            bottomleft: 32<br>
	 *            bottom: 64<br>
	 *            bottom-right: 128<br>
	 * @param top
	 *            Element top
	 * @param left
	 *            Element left
	 * @param width
	 *            Element width
	 * @param height
	 *            Element height
	 */
	void resize(VpePageContext pageContext, Element sourceElement,
			nsIDOMDocument visualDocument, nsIDOMElement visualElement, Object data,
			int constrains, int top, int left, int width, int height);

	/**
	 * Checks a capability of drag of visual element
	 * 
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param sourceElement
	 *            The current element of the source tree.
	 * @param visualDocument
	 *            The document of the visual tree.
	 * @param visualElement
	 *            The current element of the visual tree.
	 * @param data
	 *            The arbitrary data, built by a method <code>create</code>
	 * @return <code>true</code> The element can be dragged
	 */
	boolean canInnerDrag(VpePageContext pageContext, Element sourceElement,
			nsIDOMDocument visualDocument, nsIDOMElement visualElement, Object data);

	/**
	 * Checks a capability to drop an element in the container
	 * 
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param container
	 *            Element-Container
	 * @param sourceDragNode
	 *            Node for drop
	 * @return <code>true</code> The node can be dropped
	 */
	boolean canInnerDrop(VpePageContext pageContext, Node container,
			Node sourceDragNode);

	/**
	 * Is invoked at drop of an element visual tree
	 * 
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param dragInfo
	 *            The information on the dragged element
	 * @param dropInfo
	 *            The information on the drop container
	 */
	void innerDrop(VpePageContext pageContext, VpeSourceInnerDragInfo dragInfo,
			VpeSourceInnerDropInfo dropInfo);

	/**
	 * Returns <code>VpeTagDescription</code>
	 * 
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param sourceElement
	 *            The current element of the source tree.
	 * @param visualDocument
	 *            The document of the visual tree.
	 * @param visualElement
	 *            The current element of the visual tree.
	 * @param data
	 *            The arbitrary data, built by a method <code>create</code>
	 * @return <code>VpeTagDescription</code>
	 */
	VpeTagDescription getTagDescription(VpePageContext pageContext,
			Element sourceElement, nsIDOMDocument visualDocument,
			nsIDOMElement visualElement, Object data);

	/**
	 * Checks, whether it is necessary to re-create an element at change of
	 * attribute
	 * 
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param sourceElement
	 *            The current element of the source tree.
	 * @param visualDocument
	 *            The document of the visual tree.
	 * @param visualNode
	 *            The current node of the visual tree.
	 * @param data
	 *            The arbitrary data, built by a method <code>create</code>
	 * @param name
	 *            Atrribute name
	 * @param value
	 *            Attribute value
	 * @return <code>true</code> if it is required to re-create an element at
	 *         a modification of attribute, <code>false</code> otherwise.
	 */
	boolean recreateAtAttrChange(VpePageContext pageContext,
			Element sourceElement, nsIDOMDocument visualDocument, nsIDOMElement visualNde,
			Object data, String name, String value);

	/**
	 * @return <code>true</code> if the element can have children
	 */
	boolean hasChildren();

	/**
	 * @return <code>true</code> if the element is case sensitive
	 */
	boolean isCaseSensitive();

	/**
	 * 
	 * @return
	 */
	boolean hasImaginaryBorder();

	/**
	 * Returns the data for formatting an element of source tree
	 * 
	 * @return <code>TextFormatingData</code>
	 */
	TextFormatingData getTextFormattingData();

	/**
	 * Returns a list of attributes of an element of the source tree, the values
	 * which one are mapped in the visiblis editor
	 * @deprecated
	 * @return attrubute name array
	 */
	String[] getOutputAttributeNames();

	/**
	 * Is invoked at a change of bundle values
	 * 
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param sourceElement
	 *            The current element of the source tree.
	 * @param data
	 *            The arbitrary data, built by a method <code>create</code>
	 */
	void refreshBundleValues(VpePageContext pageContext, Element sourceElement,
			Object data);

//	/**
//	 * Opens editor of source file for include-element
//	 * 
//	 * @param pageContext
//	 *            Contains the information on edited page.
//	 * @param sourceElement
//	 *            The current element of the source tree.
//	 * @param data
//	 *            The arbitrary data, built by a method <code>create</code>
//	 */
//	void openIncludeEditor(VpePageContext pageContext, Element sourceElement,
//			Object data);

	/**
	 * @deprecated
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param sourceElement
	 *            The current element of the source tree.
	 * @param data
	 *            The arbitrary data, built by a method <code>create</code>
	 */
	void setSourceAttributeValue(VpePageContext pageContext,
			Element sourceElement, Object data);

	/**
	 * If the value of attribute of an element of an source tree is mapped by
	 * the way of text node of a visual tree, this method returns the text
	 * node, otherwise - null
	 * @deprecated
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param sourceElement
	 *            The current element of the source tree.
	 * @param data
	 *            The arbitrary data, built by a method <code>create</code>
	 * @return Text node or null
	 */
	nsIDOMText getOutputTextNode(VpePageContext pageContext, Element sourceElement,
			Object data);

	/**
	 * @deprecated
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param sourceElement
	 *            The current element of the source tree.
	 * @param offset
	 * @param length
	 * @param data
	 */
	void setSourceAttributeSelection(VpePageContext pageContext,
			Element sourceElement, int offset, int length, Object data);

	/**
	 * @deprecated
	 * @return
	 */
	boolean isOutputAttributes();

	/**
	 * @deprecated
	 */
	int getType();

	/**
	 * @deprecated
	 * @return
	 */
	VpeAnyData getAnyData();

	/**
	 * The unfilled element of an source tree can be mapped in the visiblis
	 * editor with the default contents This method fills default contents
	 * 
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param sourceContainer
	 *            The current element of the source tree.
	 * @param visualContainer
	 *            The current element of the visual tree.
	 * @param visualDocument
	 *            The document of the visual tree.
	 */
	void setPseudoContent(VpePageContext pageContext, Node sourceContainer,
			nsIDOMNode visualContainer, nsIDOMDocument visualDocument);

	boolean containsText();
	
	public boolean canModify();
	
	public void setModify(boolean modify);
	
	/**
	 * open bundle
	 * 
	 * @param pageContext
	 * @param visualNod
	 * @return
	 */
	public boolean openBundle(VpePageContext pageContext,
			nsIDOMNode visualNode, VpeElementMapping elementMapping);

	/**
	 * 
	 * @param node
	 * @param elementData
	 * @param domMapping
	 * @return
	 */
	public NodeData getNodeData(nsIDOMNode node, VpeElementData elementData,
			VpeDomMapping domMapping);

	/**
	 * 
	 * @param elementMapping
	 * @param focusPosition
	 * @param anchorPosition
	 * @param domMapping
	 * @return
	 */
	public nsIDOMNode getVisualNodeBySourcePosition(
			VpeElementMapping elementMapping, int focusPosition,
			int anchorPosition, VpeDomMapping domMapping);
	
	public boolean isInvisible();
	
	/**
	 * Calculates and returns sourceRegion, on which we should
	 * make openOn action
	 * @param sourceNode - source node from element mapping
	 * @param domNode
	 * @return sourceRegion on which we should make openOn action
	 */
	public IRegion getSourceRegionForOpenOn(VpePageContext pageContext, Node sourceNode,nsIDOMNode domNode);
	
}