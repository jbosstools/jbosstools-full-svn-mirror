package org.jboss.tools.flow.common.properties;

import java.util.Arrays;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.flow.common.wrapper.Wrapper;

public class WrapperPropertySource implements IPropertySource {
	
	private Wrapper wrapper;
	private IPropertySource elementPropertySource;
	
	public WrapperPropertySource(Wrapper wrapper) {
		this.wrapper = wrapper;
		if (wrapper != null && wrapper.getElement() != null) {
			Object object = wrapper.getElement().getMetaData("propertySource");
			if (object != null && object instanceof IPropertySource) {
				elementPropertySource = (IPropertySource)object;
			}
		}
	}

	public Object getEditableValue() {
		if (elementPropertySource != null) {
			return elementPropertySource.getEditableValue();
		}
		return null;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		if (elementPropertySource != null) {
			return elementPropertySource.getPropertyDescriptors();
		}
		return new IPropertyDescriptor[0];
	}

	public Object getPropertyValue(Object id) {
		if (elementPropertySource != null) {
			return elementPropertySource.getPropertyValue(id);
		}
		return null;
	}

	public boolean isPropertySet(Object id) {
		if (elementPropertySource != null) {
			return elementPropertySource.isPropertySet(id);
		}
		return false;
	}

	public void resetPropertyValue(Object id) {
		if (elementPropertySource != null) {
			elementPropertySource.resetPropertyValue(id);
		}
	}

	public void setPropertyValue(Object id, Object value) {
		if (elementPropertySource != null) {
			elementPropertySource.setPropertyValue(id, value);
			wrapper.notifyListeners(Wrapper.CHANGE_PROPERTY, id);
		}
	}
	
	protected Wrapper getWrapper() {
		return wrapper;
	}
	
	protected IPropertyDescriptor[] merge(IPropertyDescriptor[] first, IPropertyDescriptor[] second) {
		List<IPropertyDescriptor> result = Arrays.asList(first);
		result.addAll(Arrays.asList(second));
		return (IPropertyDescriptor[])result.toArray();
	}

}
