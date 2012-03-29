/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.jboss.tools.modeshape.jcr;

import org.jboss.tools.modeshape.jcr.cnd.CndElement;

/**
 * 
 */
public class LocalName implements CndElement, Comparable<LocalName> {

    private Mode mode = Mode.UNQOUTED;

    private String value;

    /**
     * Constructs an empty name in unquoated mode.
     */
    public LocalName() {
        this.mode = Mode.UNQOUTED;
    }

    /**
     * @param initialValue the initial name value (can be <code>null</code> or empty)
     */
    public LocalName( final String initialValue ) {
        this.value = initialValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo( final LocalName that ) {
        if (equals(that) || Utils.equivalent(get(), that.get())) {
            return 0;
        }

        // both can't be empty
        if (Utils.isEmpty(this.value)) {
            return -1;
        }

        if (Utils.isEmpty(that.value)) {
            return 1;
        }

        return this.value.compareTo(that.value);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( final Object obj ) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || !getClass().equals(obj.getClass())) {
            return false;
        }

        final LocalName that = (LocalName)obj;
        return Utils.equals(this.value, that.value);
    }

    /**
     * @return the name (can be <code>null</code> or empty)
     */
    public String get() {
        return this.value;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Utils.hashCode(this.value);
    }

    /**
     * @return <code>true</code> if double quotes should be used in CND notation
     */
    public boolean isDoubleQuoted() {
        return (this.mode == Mode.DOUBLE_QUOTED);
    }

    /**
     * @return <code>true</code> if single quotes should be used in CND notation
     */
    public boolean isSingleQuoted() {
        return (this.mode == Mode.SINGLE_QUOTED);
    }

    /**
     * @return <code>true</code> if no quotes should be used in CND notation
     */
    public boolean isUnquoted() {
        return (this.mode == Mode.UNQOUTED);
    }

    /**
     * @param newValue the new name value (can be <code>null</code> or empty)
     * @return <code>true</code> if the name was changed
     */
    public boolean set( String newValue ) {
        if (!Utils.isEmpty(newValue)) {
            newValue = newValue.trim();
        }

        if (!Utils.equals(this.value, newValue)) {
            this.value = newValue;
            return true;
        }

        return false;
    }

    /**
     * @param newMode the new mode (cannot be <code>null</code>)
     * @return <code>true</code> if the mode was changed
     */
    public boolean setMode( final Mode newMode ) {
        Utils.verifyIsNotNull(newMode, "newMode"); //$NON-NLS-1$

        if (this.mode != newMode) {
            this.mode = newMode;
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
    public String toCndNotation( final NotationType notationType ) {
        final StringBuilder builder = new StringBuilder();
        Mode notationMode = this.mode;

        if (!Utils.isEmpty(this.value) && this.value.contains(Utils.SPACE_STRING) && (this.mode == Mode.UNQOUTED)) {
            notationMode = Mode.SINGLE_QUOTED;
        }

        builder.append(notationMode);
        builder.append((this.value == null) ? Utils.EMPTY_STRING : this.value);
        builder.append(notationMode);

        return builder.toString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (Utils.isEmpty(this.value)) {
            return Utils.EMPTY_STRING;
        }

        return this.value;
    }

    /**
     * The quotation preference when using the name in CND notation.
     */
    public enum Mode {

        /**
         * The name is surrounded by double quotes in the CND notation.
         */
        DOUBLE_QUOTED("\""), //$NON-NLS-1$

        /**
         * The name is surrounded by single quotes in the CND notation.
         */
        SINGLE_QUOTED("'"), //$NON-NLS-1$

        /**
         * The name is not surrounded by quotes in the CND notation.
         */
        UNQOUTED(Utils.EMPTY_STRING);

        private final String delim;

        private Mode( final String delim ) {
            this.delim = delim;
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return this.delim;
        }
    }
}
