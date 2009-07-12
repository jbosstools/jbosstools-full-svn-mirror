/**
 * 
 */
package org.jboss.tools.smooks.gef.tree.model;

import org.eclipse.core.runtime.Assert;

/**
 * @author DartPeng
 *
 */
public class TreeNodeConnection {
	
	private TreeNodeModel sourceNode;
	
	private TreeNodeModel targetNode;
	
	public TreeNodeConnection(){
		
	}

	public TreeNodeConnection(TreeNodeModel sourceNode,TreeNodeModel targetNode){
		this.sourceNode = sourceNode;
		this.targetNode = targetNode;
		Assert.isNotNull(sourceNode);
		Assert.isNotNull(targetNode);
	}
	
	public void connect(){
		connectSource();
		connectTarget();
	}
	
	public void disconnect(){
		disconnectSource();
		disconnectTarget();
	}
	
	public void connectSource(){
		sourceNode.addSourceConnection(this);
	}
	
	public void connectTarget(){
		targetNode.addTargetConnection(this);
	}
	
	public void disconnectSource(){
		sourceNode.removeSourceConnection(this);
	}
	
	public void disconnectTarget(){
		targetNode.removeTargetConnection(this);
	}
	
	public TreeNodeModel getSourceNode() {
		return sourceNode;
	}

	public void setSourceNode(TreeNodeModel sourceNode) {
		this.sourceNode = sourceNode;
	}

	public TreeNodeModel getTargetNode() {
		return targetNode;
	}

	public void setTargetNode(TreeNodeModel targetNode) {
		this.targetNode = targetNode;
	}
}
