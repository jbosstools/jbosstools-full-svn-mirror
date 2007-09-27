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

import org.jboss.tools.vpe.editor.toolbar.format.IFormatItemSelector;
import org.jboss.tools.vpe.editor.toolbar.format.ToolItemFormatController;

/**
 * @author Igels
 */
public abstract class SimpleTagHandler extends FormatHandler implements IFormatItemSelector {

	/**
	 * Constructor
	 */
	public SimpleTagHandler() {
		super();
	}

	/**
	 * @see org.jboss.tools.vpe.editor.toolbar.format.handler.FormatHandler#run()
	 */
	protected void run() {
		StructuredTextViewer viewer = manager.getVpeController().getPageContext().getSourceBuilder().getStructuredTextViewer();
		Node selectedNode = manager.getCurrentSelectedNodeInfo().getNode();
		if(selectedNode==null) {
			return;
		}
		if(equalsWrappingTagName(selectedNode.getNodeName())) {
			stripElement((ElementImpl)selectedNode, selectedNode, viewer);
		} else if(isParentWrappingTag()) {
			Node parentNode = selectedNode.getParentNode();
			stripElement((ElementImpl)parentNode, selectedNode, viewer);
		} else {
			insertNewElementAroundNode(getWrappingTagName(), selectedNode, viewer, false);
		}
	}

	private boolean isParentWrappingTag() {
		Node selectedNode = manager.getCurrentSelectedNodeInfo().getNode();
		Node parentNode = selectedNode.getParentNode();
//		if(selectedNode instanceof IDOMText) {
//		} 
		if(parentNode!=null) {
			return equalsWrappingTagName(parentNode.getNodeName());
		}
		return false;
	}

	/**
	 * @return
	 */
	abstract protected boolean equalsWrappingTagName(String tagName);


	/**
	 * @return
	 */
	abstract protected String getWrappingTagName();

	/**
	 * @see org.jboss.tools.vpe.editor.toolbar.format.IFormatItemSelector#setToolbarItemEnabled(boolean)
	 */
	public void setToolbarItemEnabled(boolean enabled) {
		controller.setToolbarItemEnabled(enabled);
		if(enabled) {
			setToolbarItemSelection();
		}
	}

	/**
	 * @see org.jboss.tools.vpe.editor.toolbar.format.IFormatItemSelector#setToolbarItemSelection()
	 */
	public void setToolbarItemSelection() {
		((ToolItemFormatController)controller).setToolbarItemSelection(isParentWrappingTag());		
	}
}