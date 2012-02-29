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
public final class RequiredTypes extends ListAttributeState<String> {

    public static final String NOTATION_PREFIX = "("; //$NON-NLS-1$;

    public static final String NOTATION_SUFFIX = ")"; //$NON-NLS-1$

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.jcr.cnd.attributes.ListAttributeState#getCndNotationPrefix(org.jboss.tools.modeshape.jcr.cnd.CndElement.NotationType)
     */
    @Override
    protected String getCndNotationPrefix( NotationType notationType ) {
        return NOTATION_PREFIX;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.jcr.cnd.attributes.ListAttributeState#getCndNotationSuffix(org.jboss.tools.modeshape.jcr.cnd.CndElement.NotationType)
     */
    @Override
    protected String getCndNotationSuffix( NotationType notationType ) {
        return NOTATION_SUFFIX;
    }

    public String[] toArray() {
        List<String> typeNames = getSupportedItems();

        if (Utils.isEmpty(typeNames)) {
            return Utils.EMPTY_STRING_ARRAY;
        }

        String[] result = new String[typeNames.size()];
        int i = 0;

        for (String typeName : typeNames) {
            result[i++] = typeName;
        }

        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.jboss.tools.modeshape.jcr.cnd.attributes.ListAttributeState#getDelimiter()
     */
    @Override
    protected String getDelimiter() {
        return Utils.EMPTY_STRING;
    }

}
