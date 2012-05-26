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
import org.jboss.tools.vpe.editor.toolbar.format.IFormatItemSelector;
import org.jboss.tools.vpe.editor.toolbar.format.ToolItemFormatController;
import org.jboss.tools.vpe.editor.util.VpeStyleUtil;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

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
		Node parentNode = null;
		Node selectedNode = manager.getCurrentSelectedNodeInfo().getNode();
		if(selectedNode==null) {
			return;
		}
		if(equalsWrappingTagName(selectedNode.getNodeName())) {
			stripElement((ElementImpl)selectedNode, selectedNode, viewer);
		} else if((parentNode = getParentWrappingTagNode()) != null) {
		    	if (getWrappingTagName().equalsIgnoreCase(parentNode.getNodeName()))
			stripElement((ElementImpl)parentNode, selectedNode, viewer);
		} else {
			insertNewElementAroundNode(getWrappingTagName(), selectedNode, viewer, false);
		}
	}

	/**
	 * Get parent wrapping tag
	 * @return node
	 */
	private Node getParentWrappingTagNode() {
		Node parentNode = manager.getCurrentSelectedNodeInfo().getNode();
		for (int i = 0; i < manager.getHandlerFactory().getCountHandlers(); i++) {
		    parentNode = parentNode.getParentNode();
		    if (parentNode == null)
			return null;
		    if (parentNode instanceof ElementImpl) {
			ElementImpl element = (ElementImpl) parentNode;
			String attr = element.getAttribute("style"); //$NON-NLS-1$
			if (attr != null) {
			    attr = attr.replaceAll(" ", ""); //$NON-NLS-1$ //$NON-NLS-2$
			    if(equalsWrappingTagStyle(attr)) {
				return parentNode;
			    }
			}
		    }
		    if(equalsWrappingTagName(parentNode.getNodeName())) {
			return parentNode;
		    }
		}
		return null;
	}
	
	/**
	 * Parent is format tag
	 * 
	 * @return 
	 */
	private boolean isParentWrappingTag() {
		Node parentNode = manager.getCurrentSelectedNodeInfo().getNode();
		for (int i = 0; i < manager.getHandlerFactory().getCountHandlers(); i++) {
		    parentNode = parentNode.getParentNode();
		    if (parentNode == null)
			return false;
		    if(equalsWrappingTagName(parentNode.getNodeName())) {
			return true;
		    }
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
	 * @return
	 */
	abstract protected String getWrappingTagStyle();
	
	/**
	 * @return
	 */
	abstract protected String getWrappingTagStyleValue();

	/**
	 * @return
	 */
	abstract protected boolean equalsWrappingTagStyle(String tagStyle);
	
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