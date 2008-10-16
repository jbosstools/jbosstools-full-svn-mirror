package org.jboss.tools.flow.common.wrapper;

import org.jboss.tools.flow.common.model.Element;

public abstract class AbstractWrapper implements Wrapper {

    private Element element;

    public void setElement(Element element) {
        this.element = element;
    }

    public Element getElement() {
        return element;
    }
    
    @SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
    	if (adapter == Element.class) {
    		return element;
    	}
    	return null;
    }

}
