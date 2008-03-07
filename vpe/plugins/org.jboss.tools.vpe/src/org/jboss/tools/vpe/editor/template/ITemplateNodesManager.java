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

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.mapping.VpeElementData;
import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Node;

/**
 * Interface for editing of attributes of a template
 * 
 * @author Sergey Dzmitrovich
 * 
 */
public interface ITemplateNodesManager {

	/**
	 * @param pageContext -
	 *            context of vpe
	 * @param visualNode -
	 *            selected visual node
	 * 
	 * @return true if selected attribute is editable
	 */
	public boolean isNodeEditable(VpePageContext pageContext,
			nsIDOMNode visualNode, VpeElementData elementData);

	/**
	 * 
	 * @param pageContext
	 * @param node
	 * @param elementData
	 * @return
	 */
	public boolean isNodeEditable(VpePageContext pageContext, Node node,
			VpeElementData elementData);

	/**
	 * get visual element of attribute from source element
	 * 
	 * @param pageContext
	 * @param attr
	 * @param data
	 * @return
	 */
	public nsIDOMNode getVisualNode(VpePageContext pageContext, Node node,
			VpeElementData elementData);

	/**
	 * get source element of attribute from visual element
	 * 
	 * @param pageContext
	 * @param visualNode
	 * @param data
	 * @return
	 */
	public Node getSourceNode(VpePageContext pageContext,
			nsIDOMNode visualNode, VpeElementData elementData);

	/**
	 * get sourceNode by offset
	 * 
	 * @param sourceNode
	 * @param offset
	 * @return
	 */
	public Node getFocusedNode(Node sourceNode, VpeElementData elementData,
			int offset);

	/**
	 * open bundle
	 * 
	 * @param pageContext
	 * @param visualNod
	 * @return
	 */
	boolean openBundle(VpePageContext pageContext, nsIDOMNode visualNode,
			VpeElementData elementData);
}
