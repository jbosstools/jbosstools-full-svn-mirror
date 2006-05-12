/*
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
