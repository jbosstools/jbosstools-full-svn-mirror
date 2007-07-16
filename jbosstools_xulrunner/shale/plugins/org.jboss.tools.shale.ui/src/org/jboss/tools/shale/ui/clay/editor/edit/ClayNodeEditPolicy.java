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
package org.jboss.tools.shale.ui.clay.editor.edit;

import org.eclipse.draw2d.*;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import org.jboss.tools.shale.ui.clay.editor.figures.*;
import org.jboss.tools.shale.ui.clay.editor.model.commands.ConnectionCommand;

public class ClayNodeEditPolicy
	extends org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy {

	protected Connection createDummyConnection(Request req) {
		PolylineConnection conn = FigureFactory.createNewLink(null);
		return conn;
	}

	protected Command getConnectionCompleteCommand(
			CreateConnectionRequest request) {
		ConnectionCommand command = (ConnectionCommand) request
				.getStartCommand();
		command.setTarget((ClayEditPart) getClayEditPart());
		ConnectionAnchor ctor = getClayEditPart().getTargetConnectionAnchor(
				request);
		if (ctor == null)
			return null;
		command.setTargetTerminal(getClayEditPart()
				.mapConnectionAnchorToTerminal(ctor));
		return command;
	}

	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		if (getClayEditPart() instanceof ClayComponentEditPart) {
			if (((ClayComponentEditPart) getClayEditPart()).getDefinitionModel()
					.isAnotherFile()
					|| !((ClayComponentEditPart) getClayEditPart())
							.getDefinitionModel().isConfirmed())
				return null;
		}
		ConnectionCommand command = new ConnectionCommand();
		command.setLink(null);
		command.setSource((ClayEditPart) getClayEditPart());
		ConnectionAnchor ctor = getClayEditPart().getSourceConnectionAnchor(
				request);
		command.setSourceTerminal(getClayEditPart()
				.mapConnectionAnchorToTerminal(ctor));
		request.setStartCommand(command);
		return command;
	}

	protected ClayEditPart getClayEditPart() {
		return (ClayEditPart) getHost();
	}

	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		return null;
	}

	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		return null;
	}

	protected NodeFigure getNodeFigure() {
		return (NodeFigure) ((GraphicalEditPart) getHost()).getFigure();
	}

}