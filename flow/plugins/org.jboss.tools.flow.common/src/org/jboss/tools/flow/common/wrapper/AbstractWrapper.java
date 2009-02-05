package org.jboss.tools.flow.common.wrapper;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.flow.common.model.Element;

public abstract class AbstractWrapper implements Wrapper {

    private Element element;
    private transient List<ModelListener> listeners = new ArrayList<ModelListener>();
    
    public void setElement(Element element) {
        this.element = element;
    }

    public Element getElement() {
        return element;
    }
    
	public void addListener(ModelListener listener) {
		listeners.add(listener);
	}

	public void removeListener(ModelListener listener) {
		listeners.remove(listener);
	}

	protected void notifyListeners(int change, Object object) {
		ModelEvent event = new ModelEvent(change, object);
		for (ModelListener listener: listeners) {
			listener.modelChanged(event);
		}
	}

   @SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
    	if (adapter == Element.class) {
    		return element;
    	}
    	return null;
    }

}
