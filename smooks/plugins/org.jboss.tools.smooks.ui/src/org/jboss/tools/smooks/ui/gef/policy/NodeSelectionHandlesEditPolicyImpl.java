package org.jboss.tools.smooks.ui.gef.policy;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy;
import org.eclipse.gef.handles.MoveHandle;
import org.eclipse.gef.handles.MoveHandleLocator;
import org.jboss.tools.smooks.ui.gef.editparts.StructuredDataContentEditPart;

public class NodeSelectionHandlesEditPolicyImpl extends	SelectionHandlesEditPolicy {

	protected List createSelectionHandles() {
		
		List list = new ArrayList();
		EditPart editPart = getHost();
		
		if ( editPart instanceof GraphicalEditPart ) {
			GraphicalEditPart graphicalEditPart = (GraphicalEditPart)editPart;
			IFigure figure = (graphicalEditPart instanceof StructuredDataContentEditPart ) ? 
					((StructuredDataContentEditPart)graphicalEditPart).getSelectionFigure() : graphicalEditPart.getFigure();
					
			MoveHandleLocator loc = new MoveHandleLocator(figure);
			MoveHandle moveHandle = new MoveHandle(graphicalEditPart, loc);
			list.add(moveHandle);
		}
		return list;
	}
}