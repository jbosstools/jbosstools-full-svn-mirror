/*******************************************************************************
 * Copyright (c) 2006 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.jboss.tools.jmx.ui.internal.adapters;

import java.util.ArrayList;
import java.util.List;

import javax.management.MBeanInfo;
import javax.management.ObjectName;


import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.jboss.tools.jmx.ui.Messages;

public class MBeanInfoPropertySourceAdapter implements IPropertySource {

    private ObjectName on;

    private MBeanInfo info;

    public MBeanInfoPropertySourceAdapter(ObjectName on, MBeanInfo info) {
        this.on = on;
        this.info = info;
    }

    public Object getEditableValue() {
        return null;
    }

    public IPropertyDescriptor[] getPropertyDescriptors() {
        List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
        // General properties
        addDescriptor(
                "objectname", Messages.objectName, Messages.general, descriptors); //$NON-NLS-1$
        addDescriptor(
                "description", Messages.description, Messages.general, descriptors); //$NON-NLS-1$
        addDescriptor(
                "classname", Messages.className, Messages.general, descriptors); //$NON-NLS-1$
        return descriptors.toArray(new IPropertyDescriptor[descriptors.size()]);
    }

    private void addDescriptor(String id, String displayName, String category,
            List<PropertyDescriptor> descriptors) {
        PropertyDescriptor descriptor = new PropertyDescriptor(id, displayName);
        descriptor.setCategory(category);
        descriptors.add(descriptor);
    }

    public Object getPropertyValue(Object id) {
        if ("objectname".equals(id)) { //$NON-NLS-1$
            return on.getCanonicalName();
        }
        if ("description".equals(id)) { //$NON-NLS-1$
            return info.getDescription();
        }
        if ("classname".equals(id)) { //$NON-NLS-1$
            return info.getClassName();
        }
        return null;
    }

    public boolean isPropertySet(Object id) {
        return false;
    }

    public void resetPropertyValue(Object id) {
    }

    public void setPropertyValue(Object id, Object value) {
    }

}
