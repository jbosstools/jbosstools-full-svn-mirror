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


package org.hibernate.netbeans.console.output.result;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hibernate.Hibernate;
import org.openide.ErrorManager;

/**
 * @author leon
 */
public class UnknownObjectResultItem extends ResultItem {
    
    private Object object;
    
    private String className;
    
    private String displayName;
    
    private PropertyDescriptor[] propertyDescriptors;

    private String simpleClassName;

    private final static Set<String> IGNORED_PROPS;
    
    static {
        IGNORED_PROPS = new HashSet<String>();
        IGNORED_PROPS.add("class");
    }
    
    private final static Object[] NO_ARGS = new Object[0];
    
    private final static String ERROR_READING = "<Error while getting value>";
    
    public UnknownObjectResultItem(Object object) {
        this.object = object;
        if (Values.isDisplayableByToString(object)) {
            propertyDescriptors = new PropertyDescriptor[0];
        } else {
            try {
                BeanInfo bi = Introspector.getBeanInfo(Hibernate.getClass(object), Introspector.IGNORE_ALL_BEANINFO);
                PropertyDescriptor[] pds = bi.getPropertyDescriptors();
                List<PropertyDescriptor> l = new ArrayList<PropertyDescriptor>();
                for (int i = 0; i < pds.length; i++) {
                    PropertyDescriptor pd = pds[i];
                    if (pd.getReadMethod() != null && !IGNORED_PROPS.contains(pd.getName())) {
                        l.add(pd);
                    }
                    propertyDescriptors = l.toArray(new PropertyDescriptor[l.size()]);
                }
            } catch (IntrospectionException ex) {
                ErrorManager.getDefault().notify(ErrorManager.INFORMATIONAL, ex);
                propertyDescriptors = new PropertyDescriptor[0];
            }
        }
    }

    public String getClassName() {
        if (className == null) {
            className = Values.getClassName(object);
        }
        return className;
    }

    public String getDisplayName() {
        if (displayName == null) {
            displayName = Values.getSimpleClassName(object);
        }
        return displayName;
    }

    public String getPropertyName(int i) {
        return propertyDescriptors.length == 0 ? "Value" : propertyDescriptors[i].getName();
    }

    public int getPropertyCount() {
        return propertyDescriptors.length == 0 ? 1: propertyDescriptors.length;
    }

    public Object getPropertyValue(int i) {
        if (propertyDescriptors.length == 0) {
            return object;
        } else {
            try {
                return propertyDescriptors[i].getReadMethod().invoke(object, NO_ARGS);
            } catch (IllegalArgumentException ex) {
                ErrorManager.getDefault().notify(ErrorManager.INFORMATIONAL, ex);
                return ERROR_READING;
            } catch (IllegalAccessException ex) {
                ErrorManager.getDefault().notify(ErrorManager.INFORMATIONAL, ex);
                return ERROR_READING;
            } catch (InvocationTargetException ex) {
                ErrorManager.getDefault().notify(ErrorManager.INFORMATIONAL, ex);
                return ERROR_READING;
            }
        }
    }

    public String getPropertyValueAsString(int i) {
        return Values.toString(getPropertyValue(i), null);
    }

    public String getSimpleClassName() {
        if (simpleClassName == null) {
            simpleClassName = Values.getSimpleClassName(object);
        }
        return simpleClassName;
    }

    public boolean hasMoreDetails(int i) {
        return !Values.isDisplayableByToString(getPropertyValue(i));
    }
    
}
