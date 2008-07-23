package org.jboss.tools.flow.editor.command;

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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.jboss.tools.flow.editor.core.AbstractConnectionWrapper;
import org.jboss.tools.flow.editor.core.ContainerWrapper;
import org.jboss.tools.flow.editor.core.NodeWrapper;

/**
 * A command for deleting an element.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public class DeleteElementCommand extends Command {

    private NodeWrapper child;
    private ContainerWrapper parent;
    
    private List<NodeWrapper> incomingElementWrappers = new ArrayList<NodeWrapper>();
    private List<NodeWrapper> outgoingElementWrappers = new ArrayList<NodeWrapper>();
    private List<AbstractConnectionWrapper> incomingConnections = new ArrayList<AbstractConnectionWrapper>();
    private List<AbstractConnectionWrapper> outgoingConnections = new ArrayList<AbstractConnectionWrapper>();
    
    
    private void deleteConnections(NodeWrapper element) {
    	for (AbstractConnectionWrapper connection: element.getIncomingConnections()) {
    		incomingElementWrappers.add(connection.getSource());
    		incomingConnections.add(connection);
    	}
    	for (AbstractConnectionWrapper connection: element.getOutgoingConnections()) {
    		outgoingElementWrappers.add(connection.getTarget());
    		outgoingConnections.add(connection);
    	} 
    	for (AbstractConnectionWrapper connection: incomingConnections) {
    		connection.disconnect();
    	}
    	for (AbstractConnectionWrapper connection: outgoingConnections) {
    		connection.disconnect();
    	}
    }

    public void execute() {
        deleteConnections(child);
        parent.removeElement(child);
    }

    private void restoreConnections() {
    	int i = 0;
    	for (AbstractConnectionWrapper connection: incomingConnections) {
    		connection.connect((NodeWrapper) incomingElementWrappers.get(i), child);
    		i++;
    	}
    	i = 0;
    	for (AbstractConnectionWrapper connection: outgoingConnections) {
    		connection.connect(child, (NodeWrapper) outgoingElementWrappers.get(i));
    		i++;
    	}
    	incomingConnections.clear();
    	incomingElementWrappers.clear();
    	outgoingConnections.clear();
    	outgoingElementWrappers.clear();
    }
    
    public void setChild(NodeWrapper child) {
        this.child = child;
    }

    public void setParent(ContainerWrapper parent) {
        this.parent = parent;
    }

    public void undo() {
        parent.addElement(child);
        restoreConnections();
    }

}
