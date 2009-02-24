package org.jboss.tools.flow.common.wrapper;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
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
	
	public void notifyListeners(int change, Object object) {
		ModelEvent event = new ModelEvent(change, object);
		for (ModelListener listener: listeners) {
			listener.modelChanged(event);
		}
	}
	
	public void notifyListeners(int change) {
		notifyListeners(change, null);
	}

	public Object getEditableValue() {
		if (getPropertySource() != null) {
			getPropertySource().getEditableValue();
		}
		return null;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		if (getPropertySource() != null) {
			return getPropertySource().getPropertyDescriptors();
		}
		return null;
	}

	public Object getPropertyValue(Object id) {
		if (getPropertySource() != null) {
			return getPropertySource().getPropertyValue(id);
		}
		return null;
	}

	public boolean isPropertySet(Object id) {
		if (getPropertySource() != null) {
			return getPropertySource().isPropertySet(id);
		}
		return false;
	}

	public void resetPropertyValue(Object id) {
		if (getPropertySource() != null) {
			getPropertySource().resetPropertyValue(id);
		}
	}

	public void setPropertyValue(Object id, Object value) {
		if (getPropertySource() != null) {
			getPropertySource().setPropertyValue(id, value);
			notifyListeners(CHANGE_PROPERTY, id);
		}		
	}
	
	protected abstract IPropertySource getPropertySource(); 	

   @SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
    	if (adapter == Element.class) {
    		return element;
    	}
    	return null;
    }

}
