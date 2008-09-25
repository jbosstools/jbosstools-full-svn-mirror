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
package org.jboss.tools.seam.ui.pages.editor.edit;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Handle;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.handles.ResizeHandle;
import org.eclipse.gef.requests.CreateRequest;
import org.jboss.tools.seam.ui.pages.editor.commands.SetConstraintCommand;
import org.jboss.tools.seam.ui.pages.editor.ecore.pages.Page;
import org.jboss.tools.seam.ui.pages.editor.ecore.pages.PagesElement;
import org.jboss.tools.seam.ui.pages.editor.ecore.pages.PageException;
import org.jboss.tools.seam.ui.pages.editor.figures.ExceptionFeedbackFigure;
import org.jboss.tools.seam.ui.pages.editor.figures.NodeFigure;
import org.jboss.tools.seam.ui.pages.editor.figures.PageFeedbackFigure;

/**
 * 
 * @author eskimo(dgolovin@exadel.com)
 *
 */
public class PagesXYLayoutEditPolicy extends XYLayoutEditPolicy {

	/**
	 * 
	 */
	protected Command createAddCommand(EditPart childEditPart, Object constraint) {
		SetConstraintCommand setConstraint = new SetConstraintCommand();
		setConstraint.setLocation((Rectangle) constraint);
		setConstraint.setPart((PagesElement) childEditPart.getModel());
		setConstraint.setLabel("Reparent");
		setConstraint.setDebugLabel("LogicXYEP setConstraint");//$NON-NLS-1$*/
		return setConstraint;
	}

	protected Command createChangeConstraintCommand(EditPart child,
			Object constraint) {
		SetConstraintCommand locationCommand = new SetConstraintCommand();
		locationCommand.setShell(child.getViewer().getControl().getShell());
		locationCommand.setPart((PagesElement) child.getModel());
		locationCommand.setLocation((Rectangle) constraint);
		return locationCommand;
	}

	protected EditPolicy createChildEditPolicy(EditPart child) {
		return new CustomPolicy();
	}

	protected Command getCreateCommand(CreateRequest request) {
		return null;
	}

	protected Command getDeleteDependantCommand(Request request) {
		return null;
	}

	protected Command getOrphanChildrenCommand(Request request) {
		return null;
	}

	class CustomPolicy extends NonResizableEditPolicy {
		protected IFigure createDragSourceFeedbackFigure() {
			PagesEditPart part = (PagesEditPart) getHost();
			IFigure child = getCustomFeedbackFigure(part.getModel());
			addFeedback(child);
			Rectangle childBounds = part.getFigure().getBounds().getCopy();
			IFigure walker = part.getFigure().getParent();

			while (walker != ((GraphicalEditPart) part.getParent()).getFigure()) {
				walker.translateToParent(childBounds);
				walker = walker.getParent();
			}

			child.setBounds(childBounds);

			return child;
		}

		protected IFigure getFeedbackLayer() {
			return getLayer(LayerConstants.SCALED_FEEDBACK_LAYER);
		}

		protected IFigure getCustomFeedbackFigure(Object modelPart) {
			IFigure figure;

			if (modelPart instanceof Page)
				figure = new PageFeedbackFigure();
			else if (modelPart instanceof PageException)
				figure = new ExceptionFeedbackFigure();
			else {
				figure = new RectangleFigure();
				((RectangleFigure) figure).setXOR(true);
				((RectangleFigure) figure).setFill(true);
				figure.setBackgroundColor(NodeFigure.ghostFillColor);
				figure.setForegroundColor(ColorConstants.white);
			}

			return figure;
		}

		protected List createSelectionHandles() {
			List<Handle> list = new ArrayList<Handle>();
			list.add(createHandle((GraphicalEditPart) getHost(),
					PositionConstants.SOUTH_EAST));
			list.add(createHandle((GraphicalEditPart) getHost(),
					PositionConstants.SOUTH_WEST));
			list.add(createHandle((GraphicalEditPart) getHost(),
					PositionConstants.NORTH_WEST));
			list.add(createHandle((GraphicalEditPart) getHost(),
					PositionConstants.NORTH_EAST));

			return list;
		}

		Handle createHandle(GraphicalEditPart owner, int direction) {
			ResizeHandle handle = new ResizeHandle(owner, direction);
			handle.setCursor(SharedCursors.SIZEALL);
			return handle;
		}
	}
}