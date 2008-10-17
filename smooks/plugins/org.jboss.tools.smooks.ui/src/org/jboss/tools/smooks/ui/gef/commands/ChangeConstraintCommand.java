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
package org.jboss.tools.smooks.ui.gef.commands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.jboss.tools.smooks.ui.gef.model.IGraphicalModel;

/**
 * @deprecated
 * @author Dart Peng
 * 
 */
public class ChangeConstraintCommand extends Command {
	Rectangle constraint = null;

	IGraphicalModel graphicalModel = null;
	
	/**
	 * @return the graphicalModel
	 */
	public IGraphicalModel getGraphicalModel() {
		return graphicalModel;
	}

	/**
	 * @param graphicalModel the graphicalModel to set
	 */
	public void setGraphicalModel(IGraphicalModel graphicalModel) {
		this.graphicalModel = graphicalModel;
	}

	Rectangle prevConstraint = null;

	/**
	 * @return the constraint
	 */
	public Rectangle getConstraint() {
		return constraint;
	}

	/**
	 * @param constraint
	 *            the constraint to set
	 */
	public void setConstraint(Rectangle constraint) {
		this.constraint = constraint;
	}

	@Override
	public void execute() {
		if (constraint != null && graphicalModel != null) {
			graphicalModel.setConstraint(constraint);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
	@Override
	public void redo() {
		// TODO Auto-generated method stub
		super.redo();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		// TODO Auto-generated method stub
		super.undo();
	}
	
	

}
