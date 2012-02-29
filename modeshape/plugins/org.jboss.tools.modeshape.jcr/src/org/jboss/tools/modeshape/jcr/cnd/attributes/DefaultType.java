/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.jboss.tools.modeshape.jcr.cnd.attributes;

import org.jboss.tools.modeshape.jcr.Utils;
import org.jboss.tools.modeshape.jcr.cnd.LocalName;

public class DefaultType extends AttributeState {

    public static final String NOTATION = "="; //$NON-NLS-1$

    private final LocalName defaultType = new LocalName();

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

        if (Utils.isEmpty(this.defaultType.get())) {
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
        return NOTATION;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.jcr.cnd.attributes.AttributeState#getCompressedCndNotation()
     */
    @Override
    protected String getCompressedCndNotation() {
        return NOTATION;
    }

    public String getDefaultType() {
        return this.defaultType.get();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.jcr.cnd.attributes.AttributeState#getLongCndNotation()
     */
    @Override
    protected String getLongCndNotation() {
        return NOTATION;
    }

    /**
     * {@inheritDoc}
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

    public boolean setDefaultType( String newDefaultType ) {
        if (this.defaultType.set(newDefaultType)) {
            if (Utils.isEmpty(newDefaultType) && !isVariant()) {
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
            notation += ' ' + this.defaultType.toCndNotation(notationType);
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