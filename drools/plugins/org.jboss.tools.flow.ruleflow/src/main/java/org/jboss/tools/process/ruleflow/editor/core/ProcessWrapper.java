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

import org.drools.ruleflow.core.RuleFlowProcess;
import org.drools.workflow.core.Node;
import org.jboss.tools.flow.editor.core.AbstractRootWrapper;
import org.jboss.tools.flow.editor.core.NodeWrapper;

/**
 * Wrapper for a RuleFlow process.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public class ProcessWrapper extends AbstractRootWrapper {

	private static final long serialVersionUID = 400L;

    public ProcessWrapper() {
        setElement(new RuleFlowProcess());
    }

    public RuleFlowProcess getRuleFlowProcess() {
        return (RuleFlowProcess) getElement();
    }
    
    public String getName() {
        return getRuleFlowProcess().getName();
    }
    
    public void setName(String name) {
        getRuleFlowProcess().setName(name);
    }
    
    public Integer getRouterLayout() {
        Integer routerLayout = (Integer) getRuleFlowProcess().getMetaData("routerLayout");
        if (routerLayout == null) {
            return ROUTER_LAYOUT_MANUAL;
        }
        return routerLayout;
    }
    
    public void internalSetRouterLayout(Integer routerLayout) {
        getRuleFlowProcess().setMetaData("routerLayout", routerLayout);
    }
    
    protected void internalAddElement(NodeWrapper element) {
        Node node = ((BaseNodeWrapper) element).getNode();
        long id = 0;
        for (Node n: getRuleFlowProcess().getNodes()) {
            if (n.getId() > id) {
                id = n.getId();
            }
        }
        node.setId(++id);
        getRuleFlowProcess().addNode(node); 
    }

    protected void internalRemoveElement(NodeWrapper element) {
        getRuleFlowProcess().removeNode(((BaseNodeWrapper) element).getNode()); 
    }
    
}
