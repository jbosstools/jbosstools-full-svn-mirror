package org.jboss.tools.flow.editor.core;

import org.jboss.tools.flow.common.core.Flow;
import org.jboss.tools.flow.common.core.Node;
import org.jboss.tools.flow.editor.core.AbstractFlowWrapper;
import org.jboss.tools.flow.editor.core.NodeWrapper;

public class DefaultFlowWrapper extends AbstractFlowWrapper {

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
    
}
