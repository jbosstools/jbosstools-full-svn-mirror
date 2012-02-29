/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.jboss.tools.modeshape.jcr.cnd.attributes;

import org.jboss.tools.modeshape.jcr.cnd.CndElement;

public class SameNameSiblings extends AttributeState {

    public static final String[] NOTATION = new String[] { "sns", "*", "*" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.jcr.cnd.attributes.AttributeState#getCompactCndNotation()
     */
    @Override
    protected String getCompactCndNotation() {
        return NOTATION[CndElement.NotationType.COMPACT_INDEX];
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.jcr.cnd.attributes.AttributeState#getCompressedCndNotation()
     */
    @Override
    protected String getCompressedCndNotation() {
        return NOTATION[CndElement.NotationType.COMPRESSED_INDEX];
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.jcr.cnd.attributes.AttributeState#getLongCndNotation()
     */
    @Override
    protected String getLongCndNotation() {
        return NOTATION[CndElement.NotationType.LONG_INDEX];
    }

}