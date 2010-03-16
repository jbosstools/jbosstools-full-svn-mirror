/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.hibernate.eclipse.console.views.properties;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource2;
import org.hibernate.console.stubs.SessionStub;

public class EntityPropertySource implements IPropertySource2
{
	private Object reflectedObj;
	private IPropertyDescriptor[] propertyDescriptors;
	private final SessionStub sessionStub;

	public EntityPropertySource(final Object obj, final SessionStub sessionStub) {
		this.sessionStub = sessionStub;
		reflectedObj = obj;
	}

	public Object getEditableValue() {
		return ""; //$NON-NLS-1$
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		if (propertyDescriptors == null) {
			propertyDescriptors = sessionStub.getPropertyDescriptors(reflectedObj);
		}
		return propertyDescriptors;
	}

	public Object getPropertyValue(Object id) {
		return sessionStub.getPropertyValue(reflectedObj, id);
	}

	public boolean isPropertySet(Object id) {
		return false; // we can not decide this at the given point.
	}

	public void resetPropertyValue(Object id) {

	}

	public void setPropertyValue(Object id, Object value) {
		// lets not support editing in the raw properties view - to flakey ui.
		//classMetadata.setPropertyValue(reflectedObject, (String) id, value, EntityMode.POJO);
	}

	public boolean isPropertyResettable(Object id) {
		return false;
	}

}