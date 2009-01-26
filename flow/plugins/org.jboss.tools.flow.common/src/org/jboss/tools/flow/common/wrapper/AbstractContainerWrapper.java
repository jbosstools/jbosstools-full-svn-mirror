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

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractContainerWrapper extends AbstractNodeWrapper implements ContainerWrapper {

    List<NodeWrapper> elements = new ArrayList<NodeWrapper>();
    
    public void addElement(NodeWrapper element) {
        internalAddElement(element);
        localAddElement(element);
        notifyListeners(ADD_ELEMENT, element);
    }
    
    public void localAddElement(NodeWrapper element) {
        elements.add(element);
        element.setParent(this);
    }
    
    protected abstract void internalAddElement(NodeWrapper element);
    
    public void removeElement(NodeWrapper element) {
        internalRemoveElement(element);
        elements.remove(element);
        element.setParent(null);
        notifyListeners(REMOVE_ELEMENT, element);
    }
    
    protected abstract void internalRemoveElement(NodeWrapper element);
    
    public List<NodeWrapper> getElements() {
        return elements;
    }
    
    public FlowWrapper getFlowWrapper() {
        return getParent().getFlowWrapper();
    }

    public boolean acceptsElement(NodeWrapper element) {
    	return true;
    }
    
	public boolean acceptsIncomingConnection(
			ConnectionWrapper connection, NodeWrapper source) {
		return true;
	}

	public boolean acceptsOutgoingConnection(
			ConnectionWrapper connection, NodeWrapper target) {
		return true;
	}

}
