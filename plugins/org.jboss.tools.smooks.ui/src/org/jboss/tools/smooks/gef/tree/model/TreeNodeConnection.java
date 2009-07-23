/**
 * 
 */
package org.jboss.tools.smooks.gef.tree.model;

import org.eclipse.core.runtime.Assert;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;

/**
 * @author DartPeng
 * 
 */
public class TreeNodeConnection {

	private AbstractSmooksGraphicalModel sourceNode;

	private AbstractSmooksGraphicalModel targetNode;

	public TreeNodeConnection() {

	}

	public TreeNodeConnection(AbstractSmooksGraphicalModel sourceNode, AbstractSmooksGraphicalModel targetNode) {
		this.sourceNode = sourceNode;
		this.targetNode = targetNode;
		Assert.isNotNull(sourceNode);
		Assert.isNotNull(targetNode);
	}

	public void connect() {
		connectSource();
		connectTarget();
	}

	public void disconnect() {
		disconnectSource();
		disconnectTarget();
	}

	public void connectSource() {
		sourceNode.addSourceConnection(this);
	}

	public void connectTarget() {
		targetNode.addTargetConnection(this);
	}

	public void disconnectSource() {
		sourceNode.removeSourceConnection(this);
	}

	public void disconnectTarget() {
		targetNode.removeTargetConnection(this);
	}

	public AbstractSmooksGraphicalModel getSourceNode() {
		return sourceNode;
	}

	public void setSourceNode(AbstractSmooksGraphicalModel sourceNode) {
		this.sourceNode = sourceNode;
	}

	public AbstractSmooksGraphicalModel getTargetNode() {
		return targetNode;
	}

	public void setTargetNode(AbstractSmooksGraphicalModel targetNode) {
		this.targetNode = targetNode;
	}
}
