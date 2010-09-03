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
package org.jboss.tools.deltacloud.ui.views;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.jboss.tools.deltacloud.core.DeltaCloudImage;

public class ImagePropertySource implements IPropertySource {

	private static final String PROPERTY_NAME = "deltacloud.views.image.name"; //$NON-NLS-1$
	private static final String PROPERTY_ID = "deltacloud.views.image.id"; //$NON-NLS-1$
	private static final String PROPERTY_ARCH = "deltacloud.views.image.arch"; //$NON-NLS-1$
	private static final String PROPERTY_DESCRIPTION = "deltacloud.views.image.description"; //$NON-NLS-1$
	private static final String PROPERTY_NAME_TITLE = "PropertyName.title"; //$NON-NLS-1$
	private static final String PROPERTY_ID_TITLE = "PropertyId.title"; //$NON-NLS-1$
	private static final String PROPERTY_ARCH_TITLE = "PropertyArch.title"; //$NON-NLS-1$
	private static final String PROPERTY_DESCRIPTION_TITLE = "PropertyDescription.title"; //$NON-NLS-1$
	
	private IPropertyDescriptor[] propertyDescriptors;
	private DeltaCloudImage image;
	
	public ImagePropertySource(Object o) {
		image = (DeltaCloudImage)o;
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
			PropertyDescriptor idDescriptor = new PropertyDescriptor(PROPERTY_ID, 
					CVMessages.getString(PROPERTY_ID_TITLE));
			PropertyDescriptor archDescriptor = new PropertyDescriptor(PROPERTY_ARCH, 
					CVMessages.getString(PROPERTY_ARCH_TITLE));
			PropertyDescriptor descDescriptor = new PropertyDescriptor(PROPERTY_DESCRIPTION, 
					CVMessages.getString(PROPERTY_DESCRIPTION_TITLE));
			
			propertyDescriptors = new IPropertyDescriptor[] {
					nameDescriptor,
					idDescriptor,
					archDescriptor,
					descDescriptor,
			};
		}
		return propertyDescriptors;
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (id.equals(PROPERTY_NAME))
			return image.getName();
		if (id.equals(PROPERTY_ID))
			return image.getId();
		if (id.equals(PROPERTY_ARCH))
			return image.getArchitecture();
		if (id.equals(PROPERTY_DESCRIPTION))
			return image.getDescription();
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
