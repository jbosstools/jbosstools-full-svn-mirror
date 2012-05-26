/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.menu.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.IUndoManager;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.vpe.editor.menu.VpeMenuUtil;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Action to "strip tag" - replace the node by its children.
 * 
 * @author yradtsevich
 * (based on the implementation of MenuCreationHelper)
 */
public class StripTagAction extends Action {
	final Node node;
	final VpeMenuUtil menuUtil = new VpeMenuUtil();

	public StripTagAction() {
		this.node = menuUtil.getSelectedNode();
		init();
	}
	public StripTagAction(final Node node) {
		this.node = node;
		init();
	}

	private void init() {
		setText(VpeUIMessages.STRIP_TAG_MENU_ITEM);		
	}

	@Override
	public void run() {
		final Node parent = node.getParentNode();
		if (parent != null) {
			final IUndoManager undoManager = menuUtil.getSourceEditor()
					.getTextViewer().getUndoManager();
			try {
				undoManager.beginCompoundChange();
				moveNodeChildrenInto(parent);
				parent.removeChild(node);
			} finally {
				undoManager.endCompoundChange();
			}
		}
	}

	private void moveNodeChildrenInto(final Node parent) {
		final NodeList children = node.getChildNodes();
		final int childrenLength = children.getLength();
		for (int i = 0; i < childrenLength; i++) {
			final Node child = children.item(0);
			node.removeChild(child);
			parent.insertBefore(child, node);
		}
	}

	@Override
	public boolean isEnabled() {
		if (node == null 
				|| node.getNodeType() != Node.ELEMENT_NODE) {
			return false;
		} else {
			return true;
		}
	}
}
