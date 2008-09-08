package org.jboss.tools.smooks.ui.gef.commands;

import org.eclipse.gef.commands.Command;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataConnectionModel;
import org.jboss.tools.smooks.ui.gef.model.IConnectableModel;

/**
 * l��ɾ������
 * 
 * @author wangpeng created 2007-4-5 14:51:58
 * 
 */
public class DeleteConnectionCommand extends Command {

	private AbstractStructuredDataConnectionModel connectionModel;
	private IConnectableModel sourceNode;
	private IConnectableModel targetNode;

	public void execute() {

		sourceNode.removeSourceConnection(connectionModel);
		targetNode.removeTargetConnection(connectionModel);
	}

	public void undo() {
		sourceNode.addSourceConnection(connectionModel);
		targetNode.addTargetConnection(connectionModel);
	}

	/**
	 * @return ���� connectionModel��
	 */
	public AbstractStructuredDataConnectionModel getConnectionModel() {
		return connectionModel;
	}

	/**
	 * @param connectionModel
	 *            Ҫ���õ� connectionModel��
	 */
	public void setConnectionModel(
			AbstractStructuredDataConnectionModel connectionModel) {
		this.connectionModel = connectionModel;
	}

	/**
	 * @return ���� sourceNode��
	 */
	public IConnectableModel getSourceNode() {
		return sourceNode;
	}

	/**
	 * @param sourceNode
	 *            Ҫ���õ� sourceNode��
	 */
	public void setSourceNode(IConnectableModel sourceNode) {
		this.sourceNode = sourceNode;
	}

	/**
	 * @return ���� targetNode��
	 */
	public IConnectableModel getTargetNode() {
		return targetNode;
	}

	/**
	 * @param targetNode
	 *            Ҫ���õ� targetNode��
	 */
	public void setTargetNode(IConnectableModel targetNode) {
		this.targetNode = targetNode;
	}
}