package org.jboss.tools.flow.common.wrapper;

import org.eclipse.core.runtime.IAdaptable;
import org.jboss.tools.flow.common.model.Element;


public interface Wrapper extends IAdaptable {
	
	int ADD_INCOMING_CONNECTION = 1;
	int REMOVE_INCOMING_CONNECTION = 2;
	int ADD_OUTGOING_CONNECTION = 3;
	int REMOVE_OUTGOING_CONNECTION = 4;
	int CHANGE_VISUAL = 5;
	int ADD_ELEMENT = 6;
	int REMOVE_ELEMENT = 7;

    

	void setElement(Element element);
	Element getElement();
	
    void addListener(ModelListener listener);
    void removeListener(ModelListener listener);


}
