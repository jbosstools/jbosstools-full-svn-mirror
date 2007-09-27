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
package org.jboss.tools.shale.ui.clay.editor.action;

import java.util.List;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.gef.commands.Command;
import org.jboss.tools.common.gef.action.DiagramPasteAction;

final public class ClayPasteAction extends DiagramPasteAction {

	public ClayPasteAction(IWorkbenchPart editor) {
		super(editor);
	}

	protected Command createCommand(List objects) {
		return ClayCommandFactory.createPasteCommand(objects);
	}

}


