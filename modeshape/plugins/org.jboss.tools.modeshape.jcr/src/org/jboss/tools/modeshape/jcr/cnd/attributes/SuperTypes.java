/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.jboss.tools.modeshape.jcr.cnd.attributes;

import java.util.List;

import org.jboss.tools.modeshape.jcr.Utils;

/**
 * 
 */
public final class SuperTypes extends ListAttributeState<String> {

    public static final String NOTATION_PREFIX = ">"; //$NON-NLS-1$

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.jcr.cnd.attributes.ListAttributeState#getCndNotationPrefix(org.jboss.tools.modeshape.jcr.cnd.CndElement.NotationType)
     */
    @Override
    protected String getCndNotationPrefix( NotationType notationType ) {
        return NOTATION_PREFIX;
    }

    public String[] toArray() {
        List<String> superTypes = getSupportedItems();

        if (Utils.isEmpty(superTypes)) {
            return Utils.EMPTY_STRING_ARRAY;
        }

        String[] result = new String[superTypes.size()];
        int i = 0;

        for (String superType : superTypes) {
            result[i++] = superType;
        }

        return result;
    }

}
