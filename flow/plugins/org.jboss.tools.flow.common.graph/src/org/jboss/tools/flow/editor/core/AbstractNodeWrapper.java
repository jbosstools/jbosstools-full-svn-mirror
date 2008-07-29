package org.jboss.tools.flow.editor.core;

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
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * Default wrapper of a model element.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public abstract class AbstractNodeWrapper extends AbstractWrapper implements NodeWrapper {

    protected static IPropertyDescriptor[] descriptors;

    public static final String NAME = "Name";
    static {
        descriptors = new IPropertyDescriptor[] {
            new TextPropertyDescriptor(NAME, "Name"),
        };
    }
    
    private ContainerWrapper parent;
    private transient Rectangle constraint;
    private List<AbstractConnectionWrapper> incomingConnections = new ArrayList<AbstractConnectionWrapper>();
    private List<AbstractConnectionWrapper> outgoingConnections = new ArrayList<AbstractConnectionWrapper>();
    private transient List<ModelListener> listeners = new ArrayList<ModelListener>();
    
	public void setConstraint(Rectangle constraint) {
	    this.constraint = constraint;
		internalSetConstraint(constraint);
		notifyListeners(CHANGE_VISUAL);
	}
	
	protected abstract void internalSetConstraint(Rectangle constraint);
	
	public Rectangle getConstraint() {
	    if (constraint == null) {
	        constraint = internalGetConstraint();
	    }
	    return constraint;
	}
	
	protected abstract Rectangle internalGetConstraint();

	public void setParent(ContainerWrapper parent) {
		this.parent = parent;
	}

	public ContainerWrapper getParent() {
		return parent;
	}

	public List<AbstractConnectionWrapper> getOutgoingConnections() {
		return Collections.unmodifiableList(outgoingConnections);
	}

	public List<AbstractConnectionWrapper> getIncomingConnections() {
		return Collections.unmodifiableList(incomingConnections);
	}

	public void addIncomingConnection(AbstractConnectionWrapper connection) {
	    localAddIncomingConnection(connection);
		internalAddIncomingConnection(connection);
		notifyListeners(CHANGE_INCOMING_CONNECTIONS);
	}
	
	public void localAddIncomingConnection(AbstractConnectionWrapper connection) {
	    incomingConnections.add(connection);
	}

	protected void internalAddIncomingConnection(AbstractConnectionWrapper connection) {
	}

	public void removeIncomingConnection(AbstractConnectionWrapper connection) {
		incomingConnections.remove(connection);
		internalRemoveIncomingConnection(connection);
		notifyListeners(CHANGE_INCOMING_CONNECTIONS);
	}

	protected void internalRemoveIncomingConnection(AbstractConnectionWrapper connection) {
	}

	public void addOutgoingConnection(AbstractConnectionWrapper connection) {
	    localAddOutgoingConnection(connection);
		internalAddOutgoingConnection(connection);
		notifyListeners(CHANGE_OUTGOING_CONNECTIONS);
	}

    public void localAddOutgoingConnection(AbstractConnectionWrapper connection) {
        outgoingConnections.add(connection);
    }

	protected void internalAddOutgoingConnection(AbstractConnectionWrapper connection) {
	}

	public void removeOutgoingConnection(AbstractConnectionWrapper connection) {
		outgoingConnections.remove(connection);
		internalRemoveOutgoingConnection(connection);
		notifyListeners(CHANGE_OUTGOING_CONNECTIONS);
	}

	protected void internalRemoveOutgoingConnection(AbstractConnectionWrapper connection) {
	}

	public void setName(String name) {
		internalSetName(name);
		notifyListeners(CHANGE_VISUAL);
	}

	protected void internalSetName(String name) {
	}

	public void addListener(ModelListener listener) {
		listeners.add(listener);
	}

	public void removeListener(ModelListener listener) {
		listeners.remove(listener);
	}

	protected void notifyListeners(int change) {
		ModelEvent event = new ModelEvent(change);
		for (ModelListener listener: listeners) {
			listener.modelChanged(event);
		}
	}

}
