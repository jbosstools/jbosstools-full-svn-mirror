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
package org.jboss.tools.smooks.ui.editors;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchActionConstants;

/**
 * @author Dart Peng
 * 
 * @CreateTime Jul 23, 2008
 */
public class SmooksGraphViewerContextMenuProvider extends ContextMenuProvider {

	protected ActionRegistry actionRegistry;

	/**
	 * @return the actionRegistry
	 */
	public ActionRegistry getActionRegistry() {
		return actionRegistry;
	}

	/**
	 * @param actionRegistry
	 *            the actionRegistry to set
	 */
	public void setActionRegistry(ActionRegistry actionRegistry) {
		this.actionRegistry = actionRegistry;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ContextMenuProvider#buildContextMenu(org.eclipse.jface
	 *      .action.IMenuManager)
	 */
	@Override
	public void buildContextMenu(IMenuManager menu) {
		GEFActionConstants.addStandardActionGroups(menu);
		IAction action;
		// TODO don't enable redo/undo now 
//		action = getActionRegistry().getAction(GEFActionConstants.UNDO);
//		if (action != null)
//			menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);
//
//		action = getActionRegistry().getAction(GEFActionConstants.REDO);
//		if (action != null)
//			menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

		action = getActionRegistry()
				.getAction(IWorkbenchActionConstants.DELETE);
		if (action != null)
//			if (action.isEnabled())
				menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
	}

	public SmooksGraphViewerContextMenuProvider(EditPartViewer viewer,
			ActionRegistry actionRegistry) {
		super(viewer);
		this.actionRegistry = actionRegistry;
	}

}
