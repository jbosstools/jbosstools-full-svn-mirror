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

import java.io.Serializable;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import org.hibernate.EntityMode;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;

/**
 * @author leon
 */
public class Values {
    
    private Values() {
    }
    
    public static String getClassName(Object obj) {
        if (obj == null) {
            return "<null>";
        }
        Class clazz = Hibernate.getClass(obj);
        return clazz.getName();
    }
    
    public static String getSimpleClassName(Object obj) {
        return getSimpleClassName(getClassName(obj));
    }
    
    public static String getSimpleClassName(String className) {
        int idx = className.lastIndexOf(".");
        if (idx != -1) {
            className = className.substring(idx + 1, className.length());
        }
        return className;
    }
    
    public static boolean isDisplayableByToString(Object obj) {
        if (obj == null) {
            return true;
        }
        Class clazz = obj.getClass();
        return clazz.isPrimitive() || 
                Locale.class.isAssignableFrom(clazz) || 
                String.class.isAssignableFrom(clazz) ||
                Currency.class.isAssignableFrom(clazz) || 
                Date.class.isAssignableFrom(clazz) || 
                Number.class.isAssignableFrom(clazz) ||
                Boolean.class.isAssignableFrom(clazz) || 
                Character.class.isAssignableFrom(clazz) || 
                Byte.class.isAssignableFrom(clazz);
    }
    
    public static String toString(Object obj, SessionFactory sf) {
        if (obj == null) {
            return "<html><b><nobr>&lt;null&gt;</nobr></b></html>";
        }
        if (isDisplayableByToString(obj)) {
            return "<html><nobr><b>" + String.valueOf(obj) + "</b></nobr></html>";
        } else {
            String id = null;
            if (sf != null) {
                ClassMetadata metaData = sf.getClassMetadata(Hibernate.getClass(obj));
                if (metaData != null) {
                    Serializable sid = metaData.getIdentifier(obj, EntityMode.POJO);
                    if (sid != null) {
                        id = sid.toString().trim();
                    }
                }
            }
            String name = getSimpleClassName(obj);
            if (id != null) {
                name += " [<b>" + id + "]</b>";
            } else {
                name += "(instance <b>" + System.identityHashCode(obj) + "</b>)";
            }
            return "<html><nobr>" + name + "</nobr></html>";
        }
    }
    
}
