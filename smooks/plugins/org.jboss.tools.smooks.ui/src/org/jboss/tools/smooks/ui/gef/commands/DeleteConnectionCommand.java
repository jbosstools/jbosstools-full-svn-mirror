package org.jboss.tools.smooks.ui.gef.commands;

import org.eclipse.gef.commands.Command;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataConnectionModel;
import org.jboss.tools.smooks.ui.gef.model.IConnectableModel;

/**
 * 
 */
public class DeleteConnectionCommand extends Command {

	private AbstractStructuredDataConnectionModel connectionModel;
	private IConnectableModel sourceNode;
	private IConnectableModel targetNode;

	public void execute() {
		if (sourceNode != null && targetNode != null) {
			sourceNode.removeSourceConnection(connectionModel);
			targetNode.removeTargetConnection(connectionModel);
		}
	}

	public void undo() {
		sourceNode.addSourceConnection(connectionModel);
		targetNode.addTargetConnection(connectionModel);
	}

	/**
	 */
	public AbstractStructuredDataConnectionModel getConnectionModel() {
		return connectionModel;
	}

	/**
	 */
	public void setConnectionModel(
			AbstractStructuredDataConnectionModel connectionModel) {
		this.connectionModel = connectionModel;
		if (connectionModel != null) {
			setSourceNode(connectionModel.getSource());
			setTargetNode(connectionModel.getTarget());
		}
	}

	/**
	 */
	public IConnectableModel getSourceNode() {
		return sourceNode;
	}

	/**
	 */
	public void setSourceNode(IConnectableModel sourceNode) {
		this.sourceNode = sourceNode;
	}

	/**
	 */
	public IConnectableModel getTargetNode() {
		return targetNode;
	}

	/**
	 */
	public void setTargetNode(IConnectableModel targetNode) {
		this.targetNode = targetNode;
	}
}