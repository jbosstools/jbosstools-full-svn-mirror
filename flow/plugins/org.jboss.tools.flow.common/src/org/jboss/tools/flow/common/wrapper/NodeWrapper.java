package org.jboss.tools.flow.common.wrapper;

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

import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Wrapper of a model element.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public interface NodeWrapper extends Wrapper {
	
	static final int CHANGE_INCOMING_CONNECTIONS = 1;
	static final int CHANGE_OUTGOING_CONNECTIONS = 2;
	static final int CHANGE_VISUAL = 3;
    
    String getId();
    String getName();
    void setName(String name);
    
    void setConstraint(Rectangle constraint);
    Rectangle getConstraint();
    
    void setParent(ContainerWrapper parent);
    ContainerWrapper getParent();
    
    List<ConnectionWrapper> getOutgoingConnections();
    List<ConnectionWrapper> getIncomingConnections();
    void addIncomingConnection(AbstractConnectionWrapper connection);
    void localAddIncomingConnection(AbstractConnectionWrapper connection);
    void removeIncomingConnection(AbstractConnectionWrapper connection);
    void addOutgoingConnection(AbstractConnectionWrapper connection);
    void localAddOutgoingConnection(AbstractConnectionWrapper connection);
    void removeOutgoingConnection(AbstractConnectionWrapper connection);
    boolean acceptsIncomingConnection(ConnectionWrapper connection, NodeWrapper source);
    boolean acceptsOutgoingConnection(ConnectionWrapper connection, NodeWrapper target);
    
    void addListener(ModelListener listener);
    void removeListener(ModelListener listener);

}
