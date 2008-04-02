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
import org.jboss.tools.vpe.editor.mapping.VpeElementMapping;
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
	public boolean isNodeEditable(nsIDOMNode visualNode,
			VpeElementData elementData);

	/**
	 * 
	 * @param pageContext
	 * @param node
	 * @param elementData
	 * @return
	 */
	public boolean isNodeEditable(Node node, VpeElementData elementData);

	public nsIDOMNode getTargetVisualNodeBySourceNode(Node sourceNode,
			VpeElementMapping elementMapping);

	public nsIDOMNode getTargetVisualNodeByVisualNode(
			VpePageContext pageContext, nsIDOMNode visualNode,
			VpeElementMapping elementMapping);

	public Node getTargetSourceNodeByVisualNode(VpePageContext pageContext,
			nsIDOMNode visualNode, VpeElementMapping elementMapping);

	public Node getTargetSourceNodeBySourcePosition(VpePageContext pageContext,
			int focusOffset, int anchorOffset);

	/**
	 * open bundle
	 * 
	 * @param pageContext
	 * @param visualNod
	 * @return
	 */
	boolean openBundle(VpePageContext pageContext, nsIDOMNode visualNode,
			VpeElementMapping elementMapping);
}
