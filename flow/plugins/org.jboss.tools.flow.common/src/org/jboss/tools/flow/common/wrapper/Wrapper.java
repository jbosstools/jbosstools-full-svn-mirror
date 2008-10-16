package org.jboss.tools.flow.common.wrapper;

import org.eclipse.core.runtime.IAdaptable;
import org.jboss.tools.flow.common.model.Element;


public interface Wrapper extends IAdaptable {
	
	void setElement(Element element);
	Element getElement();
	
    void addListener(ModelListener listener);
    void removeListener(ModelListener listener);


}
