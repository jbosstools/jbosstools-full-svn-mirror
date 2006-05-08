package org.hibernate.netbeans.console.output.result;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import org.hibernate.EntityMode;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.Type;

/**
 * @author leon
 */
public class MappedObjectResultItem extends ResultItem {
    
    private Object object;
    
    private String className;
    
    private String id;
    
    private String simpleClassName;
    
    private String displayName;
    
    private String[] propertyNames;
    
    private ClassMetadata metaData;

    private SessionFactory sessionFactory;

    public MappedObjectResultItem(SessionFactory sessionFactory, ClassMetadata metaData, Object object) {
        this.sessionFactory = sessionFactory;
        this.metaData = metaData;
        this.object = object;
    }
    
    private String getId(Object obj) {
        Serializable sid = metaData.getIdentifier(obj, EntityMode.POJO);
        if (sid != null) {
            return sid.toString().trim();
        }
        return null;
    }

    private String getId() {
        if (id == null) {
            id = getId(object);
        }
        return id;
    }
    
    public String getDisplayName() {
        if (displayName == null) {
            String id = getId();
            displayName = getSimpleClassName() + " [" + id + "]";
        }
        return displayName;
    }
    
    public String getClassName() {
        if (className == null) {
            className = Values.getSimpleClassName(object);
        }
        return className;
    }
    
    public String getSimpleClassName() {
        if (simpleClassName == null) {
            simpleClassName = Values.getSimpleClassName(object);
        }
        return simpleClassName;
    }
    
    public int getPropertyCount() {
        String[] pn = getPropertyNames();
        return pn.length;
    }
    
    private String[] getPropertyNames() {
        if (propertyNames == null) {
            propertyNames = metaData.getPropertyNames();
            Arrays.sort(propertyNames);
        }
        return propertyNames;
    }
    
    public String getPropertyValueAsString(int i) {
        Object value = getPropertyValue(i);
        if (value instanceof Collection) {
            return "Collection"; // TODO
        } else {
            return Values.toString(value, sessionFactory);
        }
    }
    
    public boolean hasMoreDetails(int i) {
        String name = getPropertyNames()[i];
        Type t = metaData.getPropertyType(name);
        if (t == null) {
            return false;
        }
        Object value = getPropertyValue(i);
        if (t.isAssociationType() || t.isCollectionType()) {
            return value != null;
        }
        if (value != null && (t.isComponentType() || t.isEntityType())) {
            return true;
        }
        return false;
    }
    
    public String getPropertyName(int i) {
        return getPropertyNames()[i];
    }

    public Object getPropertyValue(int i) {
        return metaData.getPropertyValue(object, getPropertyNames()[i], EntityMode.POJO);

    }

}
