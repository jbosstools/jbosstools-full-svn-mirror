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
package org.jboss.tools.smooks.graphical.editors.commands;

import org.eclipse.gef.commands.Command;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;

/**
 * @author Dart
 *
 */
public class DeleteSmooksGraphicalModelCommand extends Command {
	
	private AbstractSmooksGraphicalModel graphModel;
	
	private AbstractSmooksGraphicalModel parentModel;
	
	private int oldIndex = -1;
	
	public DeleteSmooksGraphicalModelCommand(AbstractSmooksGraphicalModel graphModel){
		this.graphModel = graphModel;
		this.parentModel = graphModel.getParent();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	@Override
	public boolean canExecute() {
		if(graphModel != null && parentModel != null){
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		oldIndex = parentModel.getChildrenWithoutDynamic().indexOf(graphModel);
		parentModel.removeChild(graphModel);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
	@Override
	public void redo() {
		super.redo();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		if(oldIndex != -1 && parentModel != null){
			parentModel.addChild(oldIndex, graphModel);
		}
		super.undo();
	}

}
