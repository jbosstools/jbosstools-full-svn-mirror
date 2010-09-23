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

import java.util.Map;

import org.jboss.tools.jst.jsp.editor.ITextFormatter;
import org.jboss.tools.jst.jsp.selection.SourceSelection;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionException;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public abstract class VpeAbstractCreator implements VpeCreator {

	/**
	 * Creates a node of the visual tree on the node of the source tree.
	 * This visual node should not have the parent node
     * This visual node can have child nodes.
	 * @param pageContext Contains the information on edited page.
	 * @param sourceNode The current node of the source tree.
	 * @param visualDocument The document of the visual tree.
	 * @param visualElement The current element of the visual tree.
	 * @param visualNodeMap Is used for a storage padding information.
	 * @return The information on the created node of the visual tree. 
	 */
	public abstract VpeCreatorInfo create(VpePageContext pageContext, Node sourceNode, nsIDOMDocument visualDocument, nsIDOMElement visualElement, Map visualNodeMap)throws VpeExpressionException ;

	/**
	 * Is invoked after construction of all child nodes of the current visual node.
	 * @param pageContext Contains the information on edited page.
	 * @param sourceElement The current element of the source tree.
	 * @param visualDocument The document of the visual tree.
	 * @param visualParent The future parent of the current visual element.
	 * @param visualElement The current element of the visual tree.
	 * @param visualNodeMap Is used for a storage padding information.
	 */
	public void validate(VpePageContext pageContext, Element sourceElement, nsIDOMDocument visualDocument, nsIDOMElement visualParent, nsIDOMElement visualElement, Map visualNodeMap) {
	}

	/**
	 * Informs on remove of an element of the visual tree.
	 * @param pageContext Contains the information on edited page.
	 * @param sourceElement The current element of the source tree.
	 * @param visualNodeMap Is used for a storage padding information.
	 */
	public void removeElement(VpePageContext pageContext, Element sourceElement, Map visualNodeMap) {
	}

	/**
	 * Is used for refresh the current element of visual tree.
	 * @param pageContext Contains the information on edited page.
	 * @param sourceElement The current element of the source tree.
	 * @param visualNodeMap Is used for a storage padding information.
	 */
	public void refreshElement(VpePageContext pageContext, Element sourceElement, Map visualNodeMap) {
	}

	/**
	 * Sets value of attribute of the current visual element.
	 * Is invoked at change of attribute of an source element.
	 * @param pageContext Contains the information on edited page.
	 * @param sourceElement The current element of the source tree.
	 * @param visualNodeMap Is used for a storage padding information.
	 * @param name Attribute name.
	 * @param value Attribute value.
	 */
	public void setAttribute(VpePageContext pageContext, Element sourceElement, Map visualNodeMap, String name, String value) {
	}

	/**
	 * Informs on remove of attribute of the current source element.
	 * @param pageContext Contains the information on edited page.
	 * @param sourceElement The current element of the source tree.
	 * @param visualNodeMap Is used for a storage padding information.
	 * @param name Attribute name.
	 */
	public void removeAttribute(VpePageContext pageContext, Element sourceElement, Map visualNodeMap, String name) {
	}

	/**
	 * Returns <code>true</code> if it is required to re-create an element at a modification of attribute, <code>false</code> otherwise.
	 * @param pageContext Contains the information on edited page.
	 * @param sourceElement The current element of the source tree.
	 * @param visualDocument The document of the visual tree.
	 * @param visualNode The current node of the visual tree.
	 * @param data The saved data in a method <code>create</code>. 
	 * @param name Attribute name.
	 * @param value Attribute value.
	 * @return <code>true</code> if it is required to re-create an element at a modification of attribute, <code>false</code> otherwise.
	 */
	public boolean isRecreateAtAttrChange(VpePageContext pageContext, Element sourceElement, nsIDOMDocument visualDocument, nsIDOMNode visualNode, Object data, String name, String value) {
		return false;
	}

	/**
	 * Processes key press in the visual editor.
	 * @param pageContext Contains the information on edited page.
	 * @param sourceDocument The document of the source tree.
	 * @param sourceNode The current node of the source tree.
	 * @param data The saved data in a method <code>create</code>. 
	 * @param charCode The code of char. 
	 * @param selection The current selection in source editor.
	 * @param formatter Formatter of text.
	 * @return <code>true</code> if the key is treated, <code>false</code> otherwise.
	 */
	public boolean nonctrlKeyPressHandler(VpePageContext pageContext, Document sourceDocument, Node sourceNode, Object data, long charCode, SourceSelection selection, ITextFormatter formatter) {
		return false;
	}

	public Node getNodeForUpdate(VpePageContext pageContext, Node sourceNode, nsIDOMNode visualNode, Map visualNodeMap) {
		return null;
	}
}
