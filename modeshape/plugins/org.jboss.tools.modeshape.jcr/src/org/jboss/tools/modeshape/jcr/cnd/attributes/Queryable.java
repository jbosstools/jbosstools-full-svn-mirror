/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.jboss.tools.modeshape.jcr.cnd.attributes;

import org.jboss.tools.modeshape.jcr.Utils;
import org.jboss.tools.modeshape.jcr.cnd.CndElement;

public class Queryable extends AttributeState {

    public static final String[] QUERY_NOTATION = new String[] { "query", "q", "q" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    public static final String[] NO_QUERY_NOTATION = new String[] { "noquery", "nq", "nq" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    public Queryable() {
        set(Value.VARIANT);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.jcr.cnd.attributes.AttributeState#hasCndNotation()
     */
    @Override
    protected boolean hasCndNotation() {
        return !isVariant();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.jcr.cnd.attributes.AttributeState#getCompactCndNotation()
     */
    @Override
    protected String getCompactCndNotation() {
        if (is()) {
            return QUERY_NOTATION[CndElement.NotationType.COMPACT_INDEX];
        }

        if (isNot()) {
            return NO_QUERY_NOTATION[CndElement.NotationType.COMPACT_INDEX];
        }

        return Utils.EMPTY_STRING;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.jcr.cnd.attributes.AttributeState#getCompressedCndNotation()
     */
    @Override
    protected String getCompressedCndNotation() {
        if (is()) {
            return QUERY_NOTATION[CndElement.NotationType.COMPRESSED_INDEX];
        }

        if (isNot()) {
            return NO_QUERY_NOTATION[CndElement.NotationType.COMPRESSED_INDEX];
        }

        return Utils.EMPTY_STRING;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.jcr.cnd.attributes.AttributeState#getLongCndNotation()
     */
    @Override
    protected String getLongCndNotation() {
        if (is()) {
            return QUERY_NOTATION[CndElement.NotationType.LONG_INDEX];
        }

        if (isNot()) {
            return NO_QUERY_NOTATION[CndElement.NotationType.LONG_INDEX];
        }

        return Utils.EMPTY_STRING;
    }

}