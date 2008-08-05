package org.jboss.tools.flow.editor.core;

import org.jboss.tools.flow.common.model.Flow;
import org.jboss.tools.flow.common.model.Node;
import org.jboss.tools.flow.editor.core.AbstractFlowWrapper;
import org.jboss.tools.flow.editor.core.NodeWrapper;
import org.jboss.tools.flow.editor.strategy.AcceptsElementStrategy;

public class DefaultFlowWrapper extends AbstractFlowWrapper {
	
	private AcceptsElementStrategy acceptsElementStrategy;

    public Integer getRouterLayout() {
        Integer routerLayout = (Integer) ((Flow)getElement()).getMetaData("routerLayout");
        if (routerLayout == null) {
            return ROUTER_LAYOUT_MANUAL;
        }
        return routerLayout;
    }
    
    public void internalSetRouterLayout(Integer routerLayout) {
    	((Flow)getElement()).setMetaData("routerLayout", routerLayout);
    }
    
    protected void internalAddElement(NodeWrapper element) {
        Node node = (Node)element.getElement();
        long id = 0;
        for (Node n: ((Flow)getElement()).getNodes()) {
            if (n.getId() > id) {
                id = n.getId();
            }
        }
        node.setId(++id);
        ((Flow)getElement()).addNode(node); 
    }

    protected void internalRemoveElement(NodeWrapper element) {
    	((Flow)getElement()).removeNode((Node)element.getElement()); 
    }
    
    public boolean acceptsElement(NodeWrapper wrapper) {
    	if (wrapper == null) {
    		return false;
    	} else if (acceptsElementStrategy != null) {
    		return acceptsElementStrategy.acceptsElement(wrapper.getElement());
    	} else {
    		return true;
    	}
    }
    
    public void setAcceptsElementStrategy(AcceptsElementStrategy strategy) {
    	this.acceptsElementStrategy = strategy;
    }
    
}
