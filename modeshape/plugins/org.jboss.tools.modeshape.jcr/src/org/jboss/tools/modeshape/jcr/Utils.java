package org.jboss.tools.modeshape.jcr;

import java.util.Collection;

import org.eclipse.osgi.util.NLS;
import org.modeshape.common.util.HashCode;

/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */

/**
 * Commonly used utilities and constants.
 */
public final class Utils {

    public static final String EMPTY_STRING = ""; //$NON-NLS-1$

    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    public static boolean build( StringBuilder builder,
                                 boolean addDelimiter,
                                 String delimiter,
                                 String text ) {
        if (!Utils.isEmpty(text)) {
            if (addDelimiter) {
                builder.append(delimiter);
            }

            builder.append(text);
            return true;
        }

        return false;
    }

    public static boolean equals( Object thisObject,
                                  Object thatObject ) {
        if (thisObject == null) {
            return (thatObject == null);
        }

        if (thatObject == null) {
            return false;
        }

        return thisObject.equals(thatObject);
    }

    public static boolean equivalent( Collection<?> thisCollection,
                                      Collection<?> thatCollection ) {
        if (isEmpty(thisCollection)) {
            return isEmpty(thatCollection);
        }

        if (isEmpty(thatCollection)) {
            return false;
        }

        if (thisCollection.size() != thatCollection.size()) {
            return false;
        }

        return thisCollection.containsAll(thatCollection);
    }

    public static boolean equivalent( String thisString,
                                      String thatString ) {
        if (isEmpty(thisString)) {
            return isEmpty(thatString);
        }

        if (isEmpty(thatString)) {
            return false;
        }

        return thisString.equals(thatString);
    }

    public static final int hashCode( Object... x ) {
        return HashCode.compute(x);
    }

    public static boolean isEmpty( Object[] items ) {
        return ((items == null) || (items.length == 0));
    }

    public static boolean isEmpty( Collection<?> items ) {
        return ((items == null) || items.isEmpty());
    }

    public static boolean isEmpty( String text ) {
        return ((text == null) || text.isEmpty());
    }

    public static void isNotNull( Object objectBeingChecked,
                                  String name ) {
        assert ((name != null) && !name.isEmpty()) : "name is null"; //$NON-NLS-1$

        if (objectBeingChecked == null) {
            throw new IllegalArgumentException(NLS.bind(Messages.objectIsNull, name));
        }
    }

    public static String[] toUpperCase(String[] items) {
        Utils.isNotNull(items, "items"); //$NON-NLS-1$

        String result[] = new String[items.length];
        int i = 0;

        for (String item : items) {
            result[i++] = item.toUpperCase();
        }

        return result;
    }

    public static void verifyIsNotEmpty( String objectBeingChecked,
                                         String name ) {
        assert ((name != null) && !name.isEmpty()) : "name is null"; //$NON-NLS-1$

        if (isEmpty(objectBeingChecked)) {
            throw new IllegalArgumentException(NLS.bind(Messages.stringIsEmpty, name));
        }
    }

    /**
     * Don't allow construction.
     */
    private Utils() {
        // nothing to do
    }

}
