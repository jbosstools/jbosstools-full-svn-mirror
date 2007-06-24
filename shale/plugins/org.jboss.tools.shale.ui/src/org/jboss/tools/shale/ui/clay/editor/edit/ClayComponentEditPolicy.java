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

import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ReconnectRequest;
import org.jboss.tools.shale.ui.clay.editor.model.IComponent;

public class ClayComponentEditPolicy extends ClayElementEditPolicy {

/*
	private ClayComponentEditPart getDefinitionEditPart(){
		return (ClayComponentEditPart)getHost();
	}
*/

	public Command getCommand(Request request) {
		if (RequestConstants.REQ_CONNECTION_END.equals(request.getType()))
			return getConnectionEndCommand();
		else if (RequestConstants.REQ_RECONNECT_SOURCE
				.equals(request.getType())) {
			return getReconnectionSourceCommand((ReconnectRequest) request);
		} else if (RequestConstants.REQ_RECONNECT_TARGET.equals(request
				.getType()))
			return getReconnectionTargetCommand((ReconnectRequest) request);

		return super.getCommand(request);
	}

	protected Command getConnectionEndCommand() {
		return null;
	}

	protected Command getReconnectionSourceCommand(ReconnectRequest request){
		return null;
	}

	protected Command getReconnectionTargetCommand(ReconnectRequest request){
		return null;
	}

	static class ConnectionEndCommand extends org.eclipse.gef.commands.Command {
		IComponent child = null;

		public ConnectionEndCommand() {
			super("ConnectionEndCommand");
		}

		public void setChild(IComponent child) {
			this.child = child;
		}

		public void execute() {
		}

		public boolean canUndo() {
			return false;
		}
	}
	
}