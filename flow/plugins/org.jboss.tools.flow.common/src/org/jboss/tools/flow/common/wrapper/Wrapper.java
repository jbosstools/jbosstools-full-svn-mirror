package org.jboss.tools.flow.common.wrapper;

public interface Wrapper {
	
	void setElement(Object element);
	Object getElement();
	
    void addListener(ModelListener listener);
    void removeListener(ModelListener listener);


}
