package org.jboss.tools.flow.common.wrapper;

import org.eclipse.draw2d.geometry.Rectangle;
import org.jboss.tools.flow.common.model.Connection;
import org.jboss.tools.flow.common.model.Node;
import org.jboss.tools.flow.common.strategy.AcceptsIncomingConnectionStrategy;
import org.jboss.tools.flow.common.strategy.AcceptsOutgoingConnectionStrategy;

public class DefaultNodeWrapper extends AbstractNodeWrapper {
	
	private AcceptsIncomingConnectionStrategy incomingConnectionStrategy;
	private AcceptsOutgoingConnectionStrategy outgoingConnectionStrategy;

	public void setNode(Node node) {
		setElement(node);
	}

	public Node getNode() {
		return (Node) getElement();
	}

	public String getId() {
		long id = getNode().getId();
		return id == -1 ? null : getNode().getId() + "";
	}

	public String getName() {
		return getNode().getName();
	}

	public void internalSetName(String name) {
		getNode().setName(name);
		notifyListeners(CHANGE_VISUAL);
	}

	protected void internalSetConstraint(Rectangle constraint) {
		Node node = getNode();
		node.setMetaData("x", constraint.x);
		node.setMetaData("y", constraint.y);
		node.setMetaData("width", constraint.width);
		node.setMetaData("height", constraint.height);
	}

	public Rectangle internalGetConstraint() {
		Node node = getNode();
		Integer x = (Integer) node.getMetaData("x");
		Integer y = (Integer) node.getMetaData("y");
		Integer width = (Integer) node.getMetaData("width");
		Integer height = (Integer) node.getMetaData("height");
		return new Rectangle(x == null ? 0 : x, y == null ? 0 : y,
				width == null ? -1 : width, height == null ? -1 : height);
	}
	
	public void setAcceptsIncomingConnectionStrategy(AcceptsIncomingConnectionStrategy strategy) {
		this.incomingConnectionStrategy = strategy;
	}

	public void setAcceptsOutgoingConnectionStrategy(AcceptsOutgoingConnectionStrategy strategy) {
		this.outgoingConnectionStrategy = strategy;
	}

	public boolean acceptsIncomingConnection(
			ConnectionWrapper connectionWrapper, NodeWrapper sourceWrapper) {
		if (connectionWrapper == null || sourceWrapper == null) {
			return false;
		} else if (incomingConnectionStrategy != null) {
			return incomingConnectionStrategy.acceptsIncomingConnection(
					(Connection)connectionWrapper.getElement(), 
					(Node)sourceWrapper.getElement());
		} else {
			return true;
		}
	}

	public boolean acceptsOutgoingConnection(
			ConnectionWrapper connectionWrapper, NodeWrapper targetWrapper) {
		if (connectionWrapper == null || targetWrapper == null) {
			return false;
		} else if (outgoingConnectionStrategy != null) {
			return outgoingConnectionStrategy.acceptsOutgoingConnection(
					(Connection)connectionWrapper.getElement(), 
					(Node)targetWrapper.getElement());
		} else {
			return true;
		}
	}
	
	protected void internalAddIncomingConnection(AbstractConnectionWrapper connection) {
		getNode().addIncomingConnection("DROOLS_DEFAULT",
			((DefaultConnectionWrapper) connection).getConnection());
	}
	
	protected void internalRemoveIncomingConnection(AbstractConnectionWrapper connection) {
		getNode().removeIncomingConnection("DROOLS_DEFAULT",
			((DefaultConnectionWrapper) connection).getConnection());
	}
	
	protected void internalAddOutgoingConnection(AbstractConnectionWrapper connection) {
		getNode().addOutgoingConnection("DROOLS_DEFAULT",
			((DefaultConnectionWrapper) connection).getConnection());
	}
	
	protected void internalRemoveOutgoingConnection(AbstractConnectionWrapper connection) {
		getNode().removeOutgoingConnection("DROOLS_DEFAULT",
			((DefaultConnectionWrapper) connection).getConnection());
	}

}
