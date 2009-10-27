/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.graphical.actions;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;
import org.jboss.tools.smooks.graphical.editors.SmooksGraphicalEditorPart;
import org.jboss.tools.smooks.graphical.editors.editparts.IAutoLayout;

/**
 * @author Dart
 *
 */
public class AutoLayoutAction extends SelectionAction{

	public static final String ID = "_smooks_auto_layout";

	public AutoLayoutAction(IWorkbenchPart part, int style) {
		super(part, style);
	}

	public AutoLayoutAction(IWorkbenchPart part) {
		super(part);
	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#init()
	 */
	@Override
	protected void init() {
		super.init();
		this.setText("Auto Layout");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	@Override
	protected boolean calculateEnabled() {
		IWorkbenchPart part = this.getWorkbenchPart();
		if(part instanceof SmooksGraphicalEditorPart){
			IAutoLayout layout = ((SmooksGraphicalEditorPart)part).getAutoLayout();
			return (layout != null);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		IWorkbenchPart part = this.getWorkbenchPart();
		if(part instanceof SmooksGraphicalEditorPart){
			((SmooksGraphicalEditorPart)part).autoLayout(true);
		}
	}
	
}
