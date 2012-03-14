/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.jboss.tools.modeshape.jcr.cnd.attributes;

import org.jboss.tools.modeshape.jcr.Utils;
import org.jboss.tools.modeshape.jcr.cnd.CndNotationPreferences;
import org.jboss.tools.modeshape.jcr.cnd.CndNotationPreferences.Preference;
import org.jboss.tools.modeshape.jcr.cnd.LocalName;

/**
 * The child node definitions default type property.
 */
public class DefaultType extends AttributeState {

    /**
     * The CND notation for each notation type.
     */
    public static final String NOTATION = "="; //$NON-NLS-1$

    private final LocalName defaultType = new LocalName();

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.jcr.cnd.attributes.AttributeState#get()
     */
    @Override
    public Value get() {
        final Value state = super.get();

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
        return getLongCndNotation();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.jcr.cnd.attributes.AttributeState#getCompressedCndNotation()
     */
    @Override
    protected String getCompressedCndNotation() {
        return getLongCndNotation();
    }

    /**
     * @return the default type (can be <code>null</code> or empty)
     */
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
        if (isVariant()) {
            return getPrefix();
        }

        String defaultType = getDefaultType();

        if (Utils.isEmpty(defaultType)) {
            return Utils.EMPTY_STRING;
        }
 
        return getPrefix() + defaultType;
    }

    private String getPrefix() {
        String delim = CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.DEFAULT_TYPE_END_PREFIX_DELIMITER);

        if (Utils.isEmpty(delim)) {
            return NOTATION;
        }

        return (NOTATION + delim);
    }

    /**
     * {@inheritDoc} <strong>Can only be used to change to a variant state. Use {@link DefaultType#setDefaultType(String)} to set to
     * other states</strong>
     * 
     * @see org.jboss.tools.modeshape.jcr.cnd.attributes.AttributeState#set(org.jboss.tools.modeshape.jcr.cnd.attributes.AttributeState.Value)
     */
    @Override
    public boolean set( final Value newState ) {
        if (newState == Value.VARIANT) {
            if (super.set(Value.VARIANT)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param newDefaultType the proposed new value for the default type (can be <code>null</code> or empty)
     * @return <code>true</code> if changed
     */
    public boolean setDefaultType( final String newDefaultType ) {
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
     * @see org.jboss.tools.modeshape.jcr.cnd.attributes.AttributeState#toVariantCndNotation(java.lang.String)
     */
    @Override
    protected String toVariantCndNotation( final String cndNotation ) {
        return cndNotation + AttributeState.VARIANT_CHAR;
    }
}