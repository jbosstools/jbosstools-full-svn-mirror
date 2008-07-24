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

import org.drools.workflow.core.node.StartNode;
import org.jboss.tools.flow.editor.core.AbstractConnectionWrapper;
import org.jboss.tools.flow.editor.core.NodeWrapper;

/**
 * Wrapper for a start node.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public class StartNodeWrapper extends BaseNodeWrapper {

    private static final long serialVersionUID = 1L;

    public StartNodeWrapper() {
        setNode(new StartNode());
        setName("Start");
    }
    
    public StartNode getStartNode() {
        return (StartNode) getNode();
    }
    
    public boolean acceptsIncomingConnection(AbstractConnectionWrapper connection, NodeWrapper source) {
        return false;
    }

    public boolean acceptsOutgoingConnection(AbstractConnectionWrapper connection, NodeWrapper target) {
        return super.acceptsOutgoingConnection(connection, target)
        	&& getOutgoingConnections().isEmpty();
    }
}
