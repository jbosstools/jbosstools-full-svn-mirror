package org.hibernate.netbeans.console.output.result;

import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;

/**
 * @author leon
 */
public abstract class ResultItem {
    
    protected ResultItem() {
    }
    
    public static ResultItem create(SessionFactory sf, String hql, Object result) {
        if (result instanceof Object[]) {
            return new ArrayResultItem(hql, (Object[]) result);
        } else if (result != null) {
            ClassMetadata metaData = sf.getClassMetadata(result.getClass());
            if (metaData != null) {
                return new MappedObjectResultItem(sf, metaData, result);
            } else {
                return new UnknownObjectResultItem(result);
            }
        } else {
            return new UnknownObjectResultItem(result);
        }
    }
    
    public abstract String getClassName();

    public abstract String getDisplayName();

    public abstract String getPropertyName(int i);
    
    public abstract int getPropertyCount();

    public abstract Object getPropertyValue(int i);

    public abstract String getPropertyValueAsString(int i);

    public abstract String getSimpleClassName();

    public abstract boolean hasMoreDetails(int i);
    
}
