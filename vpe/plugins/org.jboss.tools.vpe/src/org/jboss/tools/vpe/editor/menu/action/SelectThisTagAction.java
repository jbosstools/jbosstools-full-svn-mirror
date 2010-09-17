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
import org.jboss.tools.jst.jsp.util.NodesManagingUtil;
import org.jboss.tools.vpe.editor.menu.VpeMenuUtil;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.w3c.dom.Node;

/**
 * Action to select {@link #node}.
 * 
 * @author yradtsevich
 * (based on the implementation of MenuCreationHelper)
 */
public class SelectThisTagAction extends Action {
	final Node node;
	final VpeMenuUtil menuUtil = new VpeMenuUtil();

	public SelectThisTagAction() {
		this.node = menuUtil.getSelectedNode();
		init();
	}
	public SelectThisTagAction(final Node node) {
		this.node = node;
		init();
	}

	private void init() {
		setText(VpeUIMessages.SELECT_THIS_TAG_MENU_ITEM);		
	}

	@Override
	public void run() {
		final int start = NodesManagingUtil.getStartOffsetNode(node);
		final int end = NodesManagingUtil.getEndOffsetNode(node);
		menuUtil.getSourceEditor().getTextViewer().setSelectedRange(
				start, end - start);
	}

	@Override
	public boolean isEnabled() {
		return node != null;
	}
}
