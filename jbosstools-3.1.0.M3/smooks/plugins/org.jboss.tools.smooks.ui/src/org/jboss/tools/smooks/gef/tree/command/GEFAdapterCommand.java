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
package org.jboss.tools.smooks.gef.tree.command;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.gef.commands.Command;

/**
 * @author Dart (dpeng@redhat.com)
 * 
 */
public class GEFAdapterCommand extends Command {

	protected org.eclipse.emf.common.command.Command emfCommand;

	protected EditingDomain domain;

	public GEFAdapterCommand(EditingDomain domain, org.eclipse.emf.common.command.Command emfCommand) {
		this.emfCommand = emfCommand;
		this.domain = domain;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	@Override
	public boolean canExecute() {
		if (emfCommand != null) {
			return emfCommand.canExecute();
		}
		return super.canExecute();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	@Override
	public boolean canUndo() {
		if (emfCommand != null && domain != null) {
			if (domain.getCommandStack().getUndoCommand().equals(emfCommand)) {
				return emfCommand.canUndo();
			}
		}
		return super.canUndo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#dispose()
	 */
	@Override
	public void dispose() {
		if (emfCommand != null) {
			emfCommand.dispose();
			return;
		}
		super.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		if (emfCommand != null && domain != null) {
			domain.getCommandStack().execute(emfCommand);
			return;
		}
		super.execute();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#getDebugLabel()
	 */
	@Override
	public String getDebugLabel() {
		return super.getDebugLabel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#getLabel()
	 */
	@Override
	public String getLabel() {
		if (emfCommand != null) {
			return emfCommand.getLabel();
		}
		return super.getLabel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
	@Override
	public void redo() {
		if (emfCommand != null && domain != null) {
			if (domain.getCommandStack().getRedoCommand().equals(emfCommand)) {
				domain.getCommandStack().redo();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#setDebugLabel(java.lang.String)
	 */
	@Override
	public void setDebugLabel(String label) {
		super.setDebugLabel(label);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#setLabel(java.lang.String)
	 */
	@Override
	public void setLabel(String label) {
		super.setLabel(label);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		if (emfCommand != null && domain != null) {
			org.eclipse.emf.common.command.Command ccc = domain.getCommandStack().getUndoCommand();
			if (domain.getCommandStack().getUndoCommand().equals(emfCommand)) {
				try {
					domain.getCommandStack().undo();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
			}
		}
		super.undo();
	}

}
