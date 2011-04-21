package org.jboss.tools.flow.common.wrapper;

public interface FlowWrapper extends ContainerWrapper {

	int CHANGE_ELEMENTS = 1;
	int CHANGE_VISUAL = 2;

    Integer ROUTER_LAYOUT_MANUAL = new Integer(0);
    Integer ROUTER_LAYOUT_MANHATTAN = new Integer(1);
    Integer ROUTER_LAYOUT_SHORTEST_PATH = new Integer(2);


	NodeWrapper getElement(String id);

	Object getRouterLayout();

}
