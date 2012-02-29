/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.jboss.tools.modeshape.jcr.cnd.attributes;

import java.util.Collection;

import org.jboss.tools.modeshape.jcr.Utils;

/**
 * 
 */
public final class DefaultValues extends ListAttributeState<String> {

    public static final String NOTATION_PREFIX = "="; //$NON-NLS-1$

    public javax.jcr.Value[] asJcrValues() {
        Collection<String> defaultValues = getSupportedItems();

        if (Utils.isEmpty(defaultValues)) {
            return new javax.jcr.Value[0];
        }

        javax.jcr.Value[] jcrValues = new javax.jcr.Value[defaultValues.size()];
        int i = 0;

        for (String defaultValue : defaultValues) {
            jcrValues[i++] = new PropertyValue(PropertyType.STRING.asJcrValue(), defaultValue);
        }

        return jcrValues;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.jcr.cnd.attributes.ListAttributeState#getCndNotationPrefix(org.jboss.tools.modeshape.jcr.cnd.CndElement.NotationType)
     */
    @Override
    protected String getCndNotationPrefix( NotationType notationType ) {
        return NOTATION_PREFIX;
    }

}
