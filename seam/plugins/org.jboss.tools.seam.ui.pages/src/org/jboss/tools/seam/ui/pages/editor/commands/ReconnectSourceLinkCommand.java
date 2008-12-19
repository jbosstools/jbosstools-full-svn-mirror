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
package org.jboss.tools.seam.ui.pages.editor.commands;

import org.eclipse.gef.commands.Command;
import org.jboss.tools.seam.pages.xml.model.handlers.PageAdopt;
import org.jboss.tools.seam.ui.pages.editor.dnd.DndHelper;
import org.jboss.tools.seam.ui.pages.editor.ecore.pages.Link;


public class ReconnectSourceLinkCommand	extends Command{
	
		static Link child = null;
	
	public ReconnectSourceLinkCommand(){
		super("ReconnectSourceLinkCommand");
	}
	
	public void setLink(Link child) {
		ReconnectSourceLinkCommand.child= child;
	}

	public boolean canExecute() {
		PageAdopt.move_case = true;
		try {
			return DndHelper.isDropEnabled(child.getData());
		} finally {
			PageAdopt.move_case = false;
		}
	}
	
	public void execute() {
		if(child != null) {
			PageAdopt.move_case = true;
			try {
				DndHelper.drop(child.getData());
			} finally {
				PageAdopt.move_case = false;
			}
		}
		child = null;
	}
	
	public boolean canUndo() {
		return false;
	}
}
