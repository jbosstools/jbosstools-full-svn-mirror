package org.jboss.tools.flow.jpdl4.graph.wrapper;

import org.jboss.tools.flow.editor.core.DefaultNodeWrapper;
import org.jboss.tools.flow.editor.strategy.AcceptsOutgoingConnectionStrategy;
import org.jboss.tools.flow.jpdl4.core.EndState;
import org.jboss.tools.flow.jpdl4.graph.strategy.EndStateAcceptsOutgoingConnectionStrategy;

public class EndStateWrapper extends DefaultNodeWrapper {

    public EndStateWrapper() {
    	EndState element = new EndState();
        setElement(element);
        setName("End");
        AcceptsOutgoingConnectionStrategy strategy = new EndStateAcceptsOutgoingConnectionStrategy();
        strategy.setNode(element);
        setAcceptsOutgoingConnectionStrategy(strategy);
    }
    
}
