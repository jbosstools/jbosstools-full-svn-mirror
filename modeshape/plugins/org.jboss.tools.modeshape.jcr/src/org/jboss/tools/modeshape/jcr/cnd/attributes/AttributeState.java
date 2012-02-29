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

/**
 * 
 */
public abstract class AttributeState implements CndElement {

    public static final char VARIANT_CHAR = '?';

    public static final String VARIANT_STRING = Character.toString(VARIANT_CHAR);

    private Value state;

    public AttributeState() {
        this.state = Value.IS_NOT;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object obj ) {
        if ((obj == null) || !getClass().equals(obj.getClass())) {
            return false;
        }

        AttributeState that = (AttributeState)obj;
        return (this.state == that.state);
    }

    public Value get() {
        return this.state;
    }

    protected abstract String getCompactCndNotation();

    protected abstract String getCompressedCndNotation();

    protected abstract String getLongCndNotation();

    protected boolean hasCndNotation() {
        return !isNot();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public boolean is() {
        return (this.state == Value.IS);
    }

    public boolean isNot() {
        return (this.state == Value.IS_NOT);
    }

    public boolean isVariant() {
        return (this.state == Value.VARIANT);
    }

    public boolean set( Value newState ) {
        if (this.state != newState) {
            this.state = newState;
            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.jcr.cnd.CndElement#toCndNotation(org.jboss.tools.modeshape.jcr.cnd.CndElement.NotationType)
     */
    @Override
    public String toCndNotation( NotationType notationType ) {
        if (hasCndNotation()) {
            String notation = Utils.EMPTY_STRING;

            if (NotationType.LONG == notationType) {
                notation = getLongCndNotation();
            } else if (NotationType.COMPRESSED == notationType) {
                notation = getCompressedCndNotation();
            } else if (NotationType.COMPACT == notationType) {
                notation = getCompactCndNotation();
            }

            if (isVariant()) {
                return toVariantCndNotation(notation);
            }

            return notation;
        }

        return Utils.EMPTY_STRING;
    }

    protected String toVariantCndNotation( String cndNotation ) {
        if (Utils.isEmpty(cndNotation)) {
            return String.valueOf(AttributeState.VARIANT_CHAR);
        }

        return cndNotation + AttributeState.VARIANT_CHAR;
    }

    public enum Value {
        IS,
        IS_NOT,
        VARIANT
    }
}
