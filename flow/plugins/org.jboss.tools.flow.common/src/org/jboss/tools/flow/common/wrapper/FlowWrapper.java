package org.jboss.tools.flow.common.wrapper;

public interface FlowWrapper extends ContainerWrapper {

	NodeWrapper getElement(String id);

	Object getRouterLayout();

}
