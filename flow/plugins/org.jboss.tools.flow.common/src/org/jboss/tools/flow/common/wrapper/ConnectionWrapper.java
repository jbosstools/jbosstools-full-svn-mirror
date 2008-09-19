package org.jboss.tools.flow.common.wrapper;

public interface ConnectionWrapper extends Wrapper {

	NodeWrapper getSource();
	NodeWrapper getTarget();
	void disconnect();
	void connect(NodeWrapper source, NodeWrapper target);

}
