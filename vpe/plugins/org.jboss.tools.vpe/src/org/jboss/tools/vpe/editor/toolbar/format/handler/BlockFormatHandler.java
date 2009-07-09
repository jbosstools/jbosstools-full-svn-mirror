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
package org.jboss.tools.vpe.editor.toolbar.format.handler;

import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.eclipse.wst.xml.core.internal.document.ElementImpl;
import org.w3c.dom.Node;

import org.jboss.tools.vpe.editor.toolbar.format.BlockFormatController;
import org.jboss.tools.vpe.editor.toolbar.format.FormatControllerManager;
import org.jboss.tools.vpe.editor.util.HTML;

/**
 * @author Igels
 */
public class BlockFormatHandler extends FormatHandler {

    public BlockFormatHandler() {
	super();
    }

    public BlockFormatHandler(FormatControllerManager manager) {
	super(manager);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.tools.vpe.editor.toolbar.format.handler.FormatHandler#run()
     */
    protected void run() {
	BlockFormatController formatController = (BlockFormatController) controller;
	String tagName = formatController.getTagName();
	if (tagName == null || tagName.trim().length() == 0) {
	    return;
	}

	boolean normal = false;
	if ("normal".equals(tagName)) { //$NON-NLS-1$
	    normal = true;
	}

	Node selectedNode = manager.getCurrentSelectedNodeInfo().getNode();

	StructuredTextViewer viewer = manager.getVpeController()
		.getPageContext().getSourceBuilder().getStructuredTextViewer();

	Node replacedNode = null;
	//Node parentNode = selectedNode.getParentNode();
	Node formatNode = getCurrentNodeWithoutFormatingTag(selectedNode);
	Node parentNode = formatNode.getParentNode();
	if (isBlockFormatNode(selectedNode)) {
	    replacedNode = selectedNode;
	} else if (isBlockFormatNode(parentNode)) {
	    replacedNode = parentNode;
	}
	if (replacedNode instanceof ElementImpl) {
	    // Replase old node
	    if (normal) {
		stripElement((ElementImpl) replacedNode, selectedNode, viewer);
	    } else {
		replaseElementName((ElementImpl) replacedNode, tagName, viewer,
			selectedNode);
	    }
	} else if (!normal) {
	    insertNewElementAroundNode(tagName, formatNode, viewer, true);
	}
    }

    private boolean isBlockFormatNode(Node node) {
	return node != null
		&& BlockFormatController.TAGS.get(node.getNodeName()
			.toLowerCase()) != null;
    }

    private Node getCurrentNodeWithoutFormatingTag(Node node) {
	Node currentNode = node;
	 while (currentNode.getParentNode() != null
			&& (currentNode.getParentNode().getNodeName().equalsIgnoreCase(HTML.TAG_B)
				|| currentNode.getParentNode().getNodeName()
					.equalsIgnoreCase(HTML.TAG_I) || currentNode.getParentNode()
				.getNodeName().equalsIgnoreCase(HTML.TAG_U))) {
	    currentNode = currentNode.getParentNode();
	}
	return currentNode;
    }

}