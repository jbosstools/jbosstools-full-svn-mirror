package org.jboss.tools.flow.common.wrapper;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.flow.common.model.Element;


public interface Wrapper extends IAdaptable, IPropertySource, Element {
	
	int CHANGE_VISUAL = 5;
	int ADD_ELEMENT = 6;
	int REMOVE_ELEMENT = 7;
	int CHANGE_PROPERTY = 8;

    

	void setElement(Element element);
	Element getElement();
	
	void addChild(Object type, Element element);
	void removeChild(Object type, Element element);
	List<Element> getChildren(Object type);
	
    void addListener(ModelListener listener);
    void removeListener(ModelListener listener);
    void notifyListeners(int changeId, Object changeDiscriminator, Object changedObject, Object oldValue, Object newValue);

}
