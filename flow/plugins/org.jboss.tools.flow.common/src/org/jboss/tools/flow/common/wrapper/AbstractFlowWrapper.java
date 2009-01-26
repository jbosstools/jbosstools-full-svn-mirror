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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A wrapper for a process element.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public abstract class AbstractFlowWrapper extends AbstractWrapper implements FlowWrapper {

    private Map<String, NodeWrapper> elementMap = new HashMap<String, NodeWrapper>();
    private List<NodeWrapper> elementList = new ArrayList<NodeWrapper>();
    private transient List<ModelListener> listeners = new ArrayList<ModelListener>();
    
    public abstract Integer getRouterLayout();
    
    public void setRouterLayout(Integer routerLayout) {
    	internalSetRouterLayout(routerLayout);
    	notifyListeners(CHANGE_VISUAL, null);
    }
    
    protected void internalSetRouterLayout(Integer routerLayout) {
    }

    public List<NodeWrapper> getElements() {
        return Collections.unmodifiableList(
            new ArrayList<NodeWrapper>(elementList));
    }
    
    public NodeWrapper getElement(String id) {
        return (NodeWrapper) elementMap.get(id);
    }
    
    public void addElement(NodeWrapper element) {
    	if (!acceptsElement(element)) return;
        internalAddElement(element);
		localAddElement(element);
		notifyListeners(ADD_ELEMENT, element);
    }
    
    public void localAddElement(NodeWrapper element) {
        elementMap.put(element.getId(), element);
        elementList.add(element);
        element.setParent(this);
    }
    
    public boolean acceptsElement(NodeWrapper element) {
    	return true;
    }
    
    protected abstract void internalAddElement(NodeWrapper element);
    
    public void localRemoveElement(NodeWrapper element) {
        elementMap.remove(element.getId());
        elementList.remove(element);    	
    }
    
    public void removeElement(NodeWrapper element) {
    	localRemoveElement(element);
        internalRemoveElement(element);
        notifyListeners(REMOVE_ELEMENT, element);
    }
    
    protected abstract void internalRemoveElement(NodeWrapper element);
    
    public FlowWrapper getFlowWrapper() {
        return this;
    }
    
    public void addListener(ModelListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ModelListener listener) {
        listeners.remove(listener);
    }
    
    protected void notifyListeners(int change, NodeWrapper wrapper) {
        ModelEvent event = new ModelEvent(change, wrapper);
        for (ModelListener listener: listeners) {
        	listener.modelChanged(event);
        }
    }
    
}
