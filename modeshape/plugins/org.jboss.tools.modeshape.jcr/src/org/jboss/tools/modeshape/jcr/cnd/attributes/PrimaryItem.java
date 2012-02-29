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
import org.jboss.tools.modeshape.jcr.cnd.LocalName;

public class PrimaryItem extends AttributeState {

    public static final String[] NOTATION = new String[] { "primaryitem", "!", "!" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    private final LocalName primaryItem = new LocalName();

    /**
     * {@inheritDoc}
     *
     * @see org.jboss.tools.modeshape.jcr.cnd.attributes.AttributeState#get()
     */
    @Override
    public Value get() {
        Value state = super.get();

        if (state == Value.VARIANT) {
            return Value.VARIANT;
        }

        if (Utils.isEmpty(this.primaryItem.get())) {
            return Value.IS_NOT;
        }

        return Value.IS;
    }

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

    public String getPrimaryItem() {
        return this.primaryItem.get();
    }

    /**
     * {@inheritDoc}
     * Only can be used to set to variant state.
     *
     * @see org.jboss.tools.modeshape.jcr.cnd.attributes.AttributeState#set(org.jboss.tools.modeshape.jcr.cnd.attributes.AttributeState.Value)
     */
    @Override
    public boolean set( Value newState ) {
        if (newState == Value.VARIANT) {
            if (super.set(Value.VARIANT)) {
                return true;
            }
        }

        return false;
    }

    public boolean setPrimaryItem( String newPrimaryItem ) {
        if (this.primaryItem.set(newPrimaryItem)) {
            if (Utils.isEmpty(newPrimaryItem) && !isVariant()) {
                super.set(Value.IS_NOT);
            } else {
                super.set(Value.IS);
            }

            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.jcr.cnd.attributes.AttributeState#toCndNotation(org.jboss.tools.modeshape.jcr.cnd.CndElement.NotationType)
     */
    @Override
    public String toCndNotation( NotationType notationType ) {
        String notation = super.toCndNotation(notationType);

        if (!isVariant() && is()) {
            notation += ' ' + this.primaryItem.toCndNotation(notationType);
        }

        return notation;
    }
    
    /**
     * {@inheritDoc}
     *
     * @see org.jboss.tools.modeshape.jcr.cnd.attributes.AttributeState#toVariantCndNotation(java.lang.String)
     */
    @Override
    protected String toVariantCndNotation( String cndNotation ) {
        return cndNotation + ' ' + AttributeState.VARIANT_CHAR;
    }
}