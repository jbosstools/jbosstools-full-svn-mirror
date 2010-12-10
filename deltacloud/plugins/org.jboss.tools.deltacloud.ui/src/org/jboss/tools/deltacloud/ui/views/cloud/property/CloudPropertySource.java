/*******************************************************************************
 * Copyright (c) 2010 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Incorporated - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.deltacloud.ui.views.cloud.property;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.ui.views.CVMessages;

public class CloudPropertySource implements IPropertySource {

	private static final String PROPERTY_NAME = "deltacloud.views.cloud.name"; //$NON-NLS-1$
	private static final String PROPERTY_URL = "deltacloud.views.cloud.id"; //$NON-NLS-1$
	private static final String PROPERTY_USERNAME = "deltacloud.views.cloud.username"; //$NON-NLS-1$
	private static final String PROPERTY_NAME_TITLE = "PropertyName.title"; //$NON-NLS-1$
	private static final String PROPERTY_URL_TITLE = "PropertyURL.title"; //$NON-NLS-1$
	private static final String PROPERTY_USERNAME_TITLE = "PropertyUsername.title"; //$NON-NLS-1$
	
	private IPropertyDescriptor[] propertyDescriptors;
	private DeltaCloud cloud;
	
	public CloudPropertySource(Object o) {
		cloud = (DeltaCloud)o;
	}
	
	@Override
	public Object getEditableValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		if (propertyDescriptors == null) {
			PropertyDescriptor nameDescriptor = new PropertyDescriptor(PROPERTY_NAME, 
					CVMessages.getString(PROPERTY_NAME_TITLE));
			PropertyDescriptor urlDescriptor = new PropertyDescriptor(PROPERTY_URL, 
					CVMessages.getString(PROPERTY_URL_TITLE));
			PropertyDescriptor userDescriptor = new PropertyDescriptor(PROPERTY_USERNAME, 
					CVMessages.getString(PROPERTY_USERNAME_TITLE));
						
			propertyDescriptors = new IPropertyDescriptor[] {
					nameDescriptor,
					urlDescriptor,
					userDescriptor,
			};
		}
		return propertyDescriptors;
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (id.equals(PROPERTY_NAME))
			return cloud.getName();
		if (id.equals(PROPERTY_URL))
			return cloud.getURL();
		if (id.equals(PROPERTY_USERNAME))
			return cloud.getUsername();
		return null;
	}

	@Override
	public boolean isPropertySet(Object id) {
		return false;
	}


	@Override
	public void resetPropertyValue(Object id) {
		// do nothing
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		// TODO Auto-generated method stub

	}

}
