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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A wrapper for a process element.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public abstract class AbstractFlowWrapper extends AbstractWrapper implements ContainerWrapper {

	public static final int CHANGE_ELEMENTS = 1;
	public static final int CHANGE_VISUAL = 2;

    public static final Integer ROUTER_LAYOUT_MANUAL = new Integer(0);
    public static final Integer ROUTER_LAYOUT_MANHATTAN = new Integer(1);
    public static final Integer ROUTER_LAYOUT_SHORTEST_PATH = new Integer(2);

    private Map<String, NodeWrapper> elements = new HashMap<String, NodeWrapper>();
    private transient List<ModelListener> listeners = new ArrayList<ModelListener>();
    
    public abstract Integer getRouterLayout();
    
    public void setRouterLayout(Integer routerLayout) {
    	internalSetRouterLayout(routerLayout);
    	notifyListeners(CHANGE_VISUAL);
    }
    
    protected void internalSetRouterLayout(Integer routerLayout) {
    }

    public List<NodeWrapper> getElements() {
        return Collections.unmodifiableList(
            new ArrayList<NodeWrapper>(elements.values()));
    }
    
    public NodeWrapper getElement(String id) {
        return (NodeWrapper) elements.get(id);
    }
    
    public void addElement(NodeWrapper element) {
    	if (!acceptsElement(element)) return;
        internalAddElement(element);
		localAddElement(element);
		notifyListeners(CHANGE_ELEMENTS);
    }
    
    public void localAddElement(NodeWrapper element) {
        elements.put(element.getId(), element);
    }
    
    public boolean acceptsElement(NodeWrapper element) {
    	return true;
    }
    
    protected abstract void internalAddElement(NodeWrapper element);
    
    public void removeElement(NodeWrapper element) {
        elements.remove(element.getId());
        notifyListeners(CHANGE_ELEMENTS);
        internalRemoveElement(element);
    }
    
    protected abstract void internalRemoveElement(NodeWrapper element);
    
    public AbstractFlowWrapper getProcessWrapper() {
        return this;
    }
    
    public void addListener(ModelListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ModelListener listener) {
        listeners.remove(listener);
    }
    
    public void notifyListeners(int change) {
        ModelEvent event = new ModelEvent(change);
        for (ModelListener listener: listeners) {
        	listener.modelChanged(event);
        }
    }
    
}
