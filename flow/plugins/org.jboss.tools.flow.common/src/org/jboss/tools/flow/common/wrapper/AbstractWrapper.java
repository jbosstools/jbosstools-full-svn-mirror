package org.jboss.tools.flow.common.wrapper;

public abstract class AbstractWrapper implements Wrapper {

    private Object element;

    public void setElement(Object element) {
        this.element = element;
    }

    public Object getElement() {
        return element;
    }

}
