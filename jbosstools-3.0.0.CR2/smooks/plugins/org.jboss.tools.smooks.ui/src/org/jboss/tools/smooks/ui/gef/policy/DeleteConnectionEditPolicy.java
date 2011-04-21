package org.jboss.tools.smooks.ui.gef.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import org.jboss.tools.smooks.ui.gef.commands.DeleteConnectionCommand;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;

public class DeleteConnectionEditPolicy extends ComponentEditPolicy {
	
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		DeleteConnectionCommand delete = new DeleteConnectionCommand();
		LineConnectionModel connxModel = (LineConnectionModel)getHost().getModel();
		delete.setConnectionModel(connxModel);
		delete.setSourceNode(connxModel.getSource());
		delete.setTargetNode(connxModel.getTarget());
		return delete;
	}
}