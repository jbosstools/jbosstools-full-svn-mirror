/**
 * 
 */
package org.jboss.tools.smooks.gef.tree.editpolicy;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import org.jboss.tools.smooks.gef.tree.editparts.CreateConnectionCommand;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeModel;

/**
 * @author DartPeng
 *
 */
public class TreeNodeGraphicalNodeEditPolicy extends GraphicalNodeEditPolicy {

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getConnectionCompleteCommand(org.eclipse.gef.requests.CreateConnectionRequest)
	 */
	@Override
	protected Command getConnectionCompleteCommand(
			CreateConnectionRequest request) {
		Command command = request.getStartCommand();
		if(command != null && command instanceof CreateConnectionCommand){
			Object targetModel = request.getTargetEditPart().getModel();
			if(targetModel instanceof TreeNodeModel){
				if(!((TreeNodeModel)targetModel).isLinkable()) return null;
				((CreateConnectionCommand)command).setTarget((TreeNodeModel)targetModel);
				return command;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getConnectionCreateCommand(org.eclipse.gef.requests.CreateConnectionRequest)
	 */
	@Override
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		EditPart sourceEditpart = request.getSourceEditPart();
		Object model = null;
		if(sourceEditpart == null){
			sourceEditpart = getHost();
		}
		model = sourceEditpart.getModel();
		if(model != null && model instanceof TreeNodeModel){
			if(!((TreeNodeModel)model).isLinkable()) return null;
			CreateConnectionCommand command = new CreateConnectionCommand();
			command.setSource((TreeNodeModel)model);
			request.setStartCommand(command);
			return command;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getReconnectSourceCommand(org.eclipse.gef.requests.ReconnectRequest)
	 */
	@Override
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getReconnectTargetCommand(org.eclipse.gef.requests.ReconnectRequest)
	 */
	@Override
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
