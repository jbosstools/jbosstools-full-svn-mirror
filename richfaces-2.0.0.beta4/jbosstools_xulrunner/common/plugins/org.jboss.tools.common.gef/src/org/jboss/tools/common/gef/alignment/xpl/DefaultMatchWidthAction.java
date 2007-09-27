/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Exadel, Inc.
 *     Red Hat, Inc. 
 *******************************************************************************/package org.jboss.tools.common.gef.alignment.xpl;
import java.util.List;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.draw2d.geometry.PrecisionDimension;
import org.eclipse.draw2d.geometry.PrecisionRectangle;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.internal.InternalImages;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.SelectionAction;

import org.jboss.tools.common.gef.action.IDiagramSelectionProvider;

/**
 * An action that matches the width of all selected EditPart's Figures to the
 * width of the Primary Selection EditPart's Figure.
 */
public abstract class DefaultMatchWidthAction
	extends SelectionAction
{

/**
 * Constructs a <code>MatchWidthAction</code> and associates it with the given
 * part.
 * 
 * @param part
 *            The workbench part associated with this MatchWidthAction
 */
public DefaultMatchWidthAction(IWorkbenchPart part) {
	super(part);
	setText(GEFMessages.MatchWidthAction_Label);
	setHoverImageDescriptor(InternalImages.DESC_MATCH_WIDTH);
	setDisabledImageDescriptor(InternalImages.DESC_MATCH_WIDTH_DIS);
	setToolTipText(GEFMessages.MatchWidthAction_Tooltip);
	setId(GEFActionConstants.MATCH_WIDTH);
}


/**
 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
 */
protected boolean calculateEnabled() {
	Command cmd = createMatchSizeCommand(getSelectedObjects());
	if (cmd == null)
		return false;
	return cmd.canExecute();
}

/**
 * Create a command to resize the selected objects.
 * 
 * @param objects
 *            The objects to be resized.
 * @return The command to resize the selected objects.
 */
private Command createMatchSizeCommand(List objects) {
	if (objects.isEmpty())
		return null;
	if (!(objects.get(0) instanceof GraphicalEditPart))
		return null;

	GraphicalEditPart primarySelection = getPrimarySelectionEditPart(getSelectedObjects());

	if (primarySelection == null) 
		return null;
	
	GraphicalEditPart part = null;
	ChangeBoundsRequest request = null;
	PrecisionDimension preciseDimension = null;
	PrecisionRectangle precisePartBounds = null;
	Command cmd = null;
	CompoundCommand command = new CompoundCommand();
	
	PrecisionRectangle precisePrimaryBounds = new PrecisionRectangle(primarySelection
			.getFigure().getBounds().getCopy());
	primarySelection.getFigure().translateToAbsolute(precisePrimaryBounds);
	
	for (int i = 0; i < objects.size(); i++) {
		part = (GraphicalEditPart)objects.get(i);
		if (!part.equals(primarySelection)) {
			request = new ChangeBoundsRequest(RequestConstants.REQ_RESIZE);
			
			precisePartBounds = new PrecisionRectangle(part.getFigure().getBounds().getCopy());
			part.getFigure().translateToAbsolute(precisePartBounds);
			
			preciseDimension = new PrecisionDimension();
			preciseDimension.preciseWidth = getPreciseWidthDelta(precisePartBounds, 
					precisePrimaryBounds);
			preciseDimension.preciseHeight = getPreciseHeightDelta(precisePartBounds, 
					precisePrimaryBounds);
			preciseDimension.updateInts();
			
			request.setSizeDelta(preciseDimension);
			
			cmd = part.getCommand(request);
			if (cmd != null) 
				command.add(cmd);			
		}
	}
	
	return command;
}

/**
 * Returns the height delta between the two bounds. Separated into a method so
 * that it can be overriden to return 0 in the case of a width-only action.
 * 
 * @param precisePartBounds
 *            the precise bounds of the EditPart's Figure to be matched
 * @param precisePrimaryBounds
 *            the precise bounds of the Primary Selection EditPart's Figure
 * @return the delta between the two heights to be used in the Request.
 */
protected double getPreciseHeightDelta(PrecisionRectangle precisePartBounds, 
		PrecisionRectangle precisePrimaryBounds) {	
	return precisePrimaryBounds.preciseHeight - precisePartBounds.preciseHeight;
}

private GraphicalEditPart getPrimarySelectionEditPart(List editParts) {
	GraphicalEditPart part = null;
	for (int i = 0; i < editParts.size(); i++) {
		part = (GraphicalEditPart)editParts.get(i);
		if (part.getSelected() == EditPart.SELECTED_PRIMARY)
			return part;
	}
	return null;
}

/**
 * Returns the width delta between the two bounds. Separated into a method so
 * that it can be overriden to return 0 in the case of a height-only action.
 * 
 * @param precisePartBounds
 *            the precise bounds of the EditPart's Figure to be matched
 * @param precisePrimaryBounds
 *            the precise bounds of the Primary Selection EditPart's Figure
 * @return the delta between the two widths to be used in the Request.
 */
protected double getPreciseWidthDelta(PrecisionRectangle precisePartBounds, 
		PrecisionRectangle precisePrimaryBounds) {
	return precisePrimaryBounds.preciseWidth - precisePartBounds.preciseWidth;
}

/**
 * Executes this action, cycling through the selected EditParts in the Action's
 * viewer, and matching the size of the selected EditPart's Figures to that of
 * the Primary Selection's Figure.
 */
public void run() {
	execute(createMatchSizeCommand(getSelectedObjects()));
}
public abstract void update();

}