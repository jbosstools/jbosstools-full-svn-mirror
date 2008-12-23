package org.jboss.tools.smooks.ui.gef.policy;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import org.jboss.tools.smooks.ui.gef.commandprocessor.CommandProcessorFactory;
import org.jboss.tools.smooks.ui.gef.commands.CreateConnectionCommand;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataModel;

public class CustomGraphicalNodeEditPolicy extends GraphicalNodeEditPolicy {

	protected Command getConnectionCompleteCommand(
			CreateConnectionRequest request) {

		CreateConnectionCommand command = (CreateConnectionCommand) request
				.getStartCommand();
		if (command == null) {
			return null;
		}
		command.setTarget(getHost().getModel());
		try {
			boolean cando = CommandProcessorFactory.getInstance().processGEFCommand(command,
					getHost());
			if(!cando) return null;
		} catch (CoreException e) {
			// ignore
		}
		return command;
	}

	protected EditPart findTheEditPart(Object model, GraphicalViewer viewer) {
		EditPart rootEditPart = viewer.getContents();
		EditPart resultEditPart = null;
		List childrenEditPartList = rootEditPart.getChildren();
		for (Iterator iterator = childrenEditPartList.iterator(); iterator
				.hasNext();) {
			EditPart childEditPart = (EditPart) iterator.next();
			Object cm = childEditPart.getModel();
			if (cm instanceof AbstractStructuredDataModel) {
				if (((AbstractStructuredDataModel) cm).getReferenceEntityModel() == model) {
					resultEditPart = childEditPart;
					break;
				}
			}
		}

		return resultEditPart;
	}

	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		CreateConnectionCommand command = new CreateConnectionCommand();
		command.setSource(getHost().getModel());
		request.setStartCommand(command);
		try {
			boolean cando = CommandProcessorFactory.getInstance().processGEFCommand(command,
					getHost());
			if(!cando) return null;
		} catch (CoreException e) {
			// ignore
		}
		return command;
	}

	protected ConnectionAnchor getTargetConnectionAnchor(
			CreateConnectionRequest request) {
		Object model = request.getExtendedData().get("Model");
		EditPartViewer viewer = this.getHost().getViewer();
		EditPart target = findTheEditPart(model, (GraphicalViewer) viewer);
		if (target == null)
			return null;
		return target instanceof NodeEditPart ? ((NodeEditPart) target)
				.getTargetConnectionAnchor(request) : null;
	}

	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		return null;
	}

	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		return null;
	}
}