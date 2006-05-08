package org.hibernate.netbeans.console.output.result;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;

/**
 * @author leon
 */
public class ArrayResultItem extends ResultItem {
    
    private final static int MAX_DISPLAYED_ELEMENTS = 10;
    
    private Object[] elements;
    
    private String propertyNames[];
    
    private String displayName;
    
    private final static Pattern FROM_PATTERN = Pattern.compile("select(.*)from(.*)", 
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    
    public ArrayResultItem(String hql, Object[] results) {
        this.elements = results;
        if (hql != null) {
            Matcher m = FROM_PATTERN.matcher(hql);
            if (m.matches()) {
                String from = m.group(1);
                StringTokenizer tok = new StringTokenizer(from, " ,");
                if (tok.countTokens() == elements.length) {
                    propertyNames = new String[elements.length];
                    for (int i = 0; i < elements.length; i++) {
                        propertyNames[i] = tok.nextToken().trim();
                    }
                }
            }
        }
    }

    public String getClassName() {
        return "Object[]";
    }

    public String getDisplayName() {
        if (displayName == null) {
            StringBuffer buff = new StringBuffer("[");
            for (int i = 0; i < Math.min(MAX_DISPLAYED_ELEMENTS - 1, elements.length); i++) {
                if (i > 0) {
                    buff.append(", ");
                }
                buff.append(Values.getSimpleClassName(elements[i]));
            }
            if (elements.length > MAX_DISPLAYED_ELEMENTS) {
                buff.append(", ..., ");
                buff.append(Values.getSimpleClassName(elements[elements.length - 1]));
            } else if (elements.length == MAX_DISPLAYED_ELEMENTS) {
                buff.append(", ");
                buff.append(Values.getSimpleClassName(elements[MAX_DISPLAYED_ELEMENTS - 1]));
            }
            buff.append("]");
            displayName = buff.toString();
        }
        return displayName;
    }

    public String getPropertyName(int i) {
        if (propertyNames != null) {
            return propertyNames[i];
        }
        String name = "Element " + (i + 1);
        Object value = getPropertyValue(i);
        if (value != null) {
            name += " (" + Hibernate.getClass(value).getName() + ")";
        }
        return name;
    }

    public int getPropertyCount() {
        return elements.length;
    }

    public Object getPropertyValue(int i) {
        return elements[i];
    }

    public String getPropertyValueAsString(int i) {
        Object value = elements[i];
        return Values.toString(value, null);
    }

    public String getSimpleClassName() {
        return "Object[]";
    }

    public boolean hasMoreDetails(int i) {
        Object value = elements[i];
        if (value == null) {
            return false;
        }
        return !Values.isDisplayableByToString(value);
    }

}
