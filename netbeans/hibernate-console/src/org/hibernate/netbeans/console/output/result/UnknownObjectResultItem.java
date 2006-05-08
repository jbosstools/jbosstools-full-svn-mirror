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
