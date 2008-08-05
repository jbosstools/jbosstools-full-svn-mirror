package org.jboss.tools.process.ruleflow.editor.core;

/*
 * Copyright 2005 JBoss Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.drools.workflow.core.Node;
import org.eclipse.draw2d.geometry.Rectangle;
import org.jboss.tools.flow.editor.core.AbstractNodeWrapper;
import org.jboss.tools.flow.editor.core.ConnectionWrapper;
import org.jboss.tools.flow.editor.core.NodeWrapper;

/**
 * Wrapper for a node.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public abstract class BaseNodeWrapper extends AbstractNodeWrapper {
	
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
        return new Rectangle(
            x == null ? 0 : x,
            y == null ? 0 : y,
            width == null ? -1 : width,
            height == null ? -1 : height);
    }

    public boolean acceptsIncomingConnection(ConnectionWrapper connection, NodeWrapper source) {
        return source == null
    		|| ((BaseNodeWrapper) source).getNode().getNodeContainer() == getNode().getNodeContainer();
    }

    public boolean acceptsOutgoingConnection(ConnectionWrapper connection, NodeWrapper target) {
        return target == null
    		|| ((BaseNodeWrapper) target).getNode().getNodeContainer() == getNode().getNodeContainer();
    }
    
}
