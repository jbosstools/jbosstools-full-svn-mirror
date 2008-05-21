package org.hibernate.eclipse.launch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.hibernate.eclipse.console.model.impl.ExporterFactory;
import org.hibernate.eclipse.console.model.impl.ExporterProperty;

public class ExporterFactoryPropertySource implements IPropertySource {

	private final ExporterFactory factory;

	public ExporterFactoryPropertySource(ExporterFactory factory) {
		this.factory = factory;
	}

	public Object getEditableValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		List descriptors = new ArrayList();
		
		Map values = factory.getProperties();
		
		// get the values we explicitly have
		Iterator iterator = values.keySet().iterator();
		while ( iterator.hasNext() ) {
			String key = (String) iterator.next();
			
			ExporterProperty element = factory.getExporterProperty(key);
			
			if(element!=null) {
				descriptors.add(new TextPropertyDescriptor(element.getName(), element.getDescription()==null?element.getName():element.getDescription()));
			} else {
				descriptors.add(new TextPropertyDescriptor(key,key));
			}
		}
				
/* removed "default" show of properties since it gets confusing in the ui. 			
  Set set = factory.getDefaultExporterProperties().keySet();
			for (Iterator iter = set.iterator(); iter.hasNext();) {
				String key = (String) iter.next();
				if(!values.containsKey( key )) {
					ExporterProperty element = factory.getExporterProperty( key );

					if(element!=null) {
						descriptors.add(new TextPropertyDescriptor(element.getName(), element.getDescription()==null?element.getName():element.getDescription()));
					}  
				}			
			}*/
	
		
		return (IPropertyDescriptor[]) descriptors.toArray(new IPropertyDescriptor[0]);
	}

	public Object getPropertyValue(Object id) {
		String propertyValue = factory.getPropertyValue( id.toString() );
		if(propertyValue==null) {
			return "";
		} else {
			return propertyValue;
		}		
	}

	public boolean isPropertySet(Object id) {
		return false;
	}

	public void resetPropertyValue(Object id) {
	
	}

	public void setPropertyValue(Object id, Object value) {
		factory.setProperty( id.toString(), value.toString() );
	}

}
