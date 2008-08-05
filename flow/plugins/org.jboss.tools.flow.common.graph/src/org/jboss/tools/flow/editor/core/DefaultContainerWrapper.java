package org.jboss.tools.flow.editor.core;

import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.jboss.tools.flow.common.core.Container;
import org.jboss.tools.flow.common.core.Flow;
import org.jboss.tools.flow.common.core.Node;

public class DefaultContainerWrapper extends AbstractContainerWrapper {
	
	protected void internalAddElement(NodeWrapper element) {
        Node node = (Node)element.getElement();
        List<Node> nodes = ((Flow)getFlowWrapper().getElement()).getNodes();
        long id = 0;
        for (Node n: nodes) {
            if (n.getId() > id) {
                id = n.getId();
            }
        }
        node.setId(++id);
        ((Container)getParent().getElement()).addNode(node); 
	}


	protected void internalRemoveElement(NodeWrapper element) {
		((Container)getParent().getElement()).removeNode((Node)element.getElement()); 
	}


	protected Rectangle internalGetConstraint() {
		Node node = getNode();
		Integer x = (Integer) node.getMetaData("x");
		Integer y = (Integer) node.getMetaData("y");
		Integer width = (Integer) node.getMetaData("width");
		Integer height = (Integer) node.getMetaData("height");
		return new Rectangle(x == null ? 0 : x, y == null ? 0 : y,
				width == null ? -1 : width, height == null ? -1 : height);
	}


	protected void internalSetConstraint(Rectangle constraint) {
		Node node = getNode();
		node.setMetaData("x", constraint.x);
		node.setMetaData("y", constraint.y);
		node.setMetaData("width", constraint.width);
		node.setMetaData("height", constraint.height);
	}


	public String getId() {
		long id = getNode().getId();
		return id == -1 ? null : getNode().getId() + "";
	}

	public String getName() {
		return getNode().getName();
	}

	public Node getNode() {
		return (Node)getElement();
	}
	
	public boolean acceptsElement(NodeWrapper element) {
		return getParent().acceptsElement(element);
	}

}
