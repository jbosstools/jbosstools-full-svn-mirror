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

import org.drools.workflow.core.node.SubProcessNode;
import org.jboss.tools.flow.editor.core.ConnectionWrapper;
import org.jboss.tools.flow.editor.core.NodeWrapper;

/**
 * Wrapper for a SubProcess node.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public class SubProcessWrapper extends BaseNodeWrapper {

	private static final long serialVersionUID = 1L;
	
    public SubProcessWrapper() {
        setNode(new SubProcessNode());
        getSubProcessNode().setName("SubProcess");
    }
    
    public SubProcessNode getSubProcessNode() {
        return (SubProcessNode) getNode();
    }
    
    public boolean acceptsIncomingConnection(ConnectionWrapper connection, NodeWrapper source) {
        return super.acceptsIncomingConnection(connection, source)
        	&& getIncomingConnections().isEmpty();
    }

    public boolean acceptsOutgoingConnection(ConnectionWrapper connection, NodeWrapper target) {
        return super.acceptsOutgoingConnection(connection, target)
        	&& getOutgoingConnections().isEmpty();
    }
    
}
