/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.jboss.tools.modeshape.jcr.cnd;

import org.jboss.tools.modeshape.jcr.Utils;

/**
 * 
 */
public class LocalName implements CndElement {

    public enum Mode {

        /**
         * The name is not surrounded by quotes in the CND notation.
         */
        UNQOUTED(Utils.EMPTY_STRING),

        /**
         * The name is surrounded by single quotes in the CND notation.
         */
        SINGLE_QUOTED("'"), //$NON-NLS-1$

        /**
         * The name is surrounded by double quotes in the CND notation.
         */
        DOUBLE_QUOTED("\""); //$NON-NLS-1$

        private final String delim;

        private Mode( String delim ) {
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
    public LocalName( String initialValue ) {
        this.value = initialValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object obj ) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || !getClass().equals(obj.getClass())) {
            return false;
        }

        LocalName that = (LocalName)obj;
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
     * @return <code>true</code> if the model was changed
     */
    public boolean setMode( Mode newMode ) {
        Utils.isNotNull(newMode, "newMode"); //$NON-NLS-1$

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
    public String toCndNotation( NotationType notationType ) {
        StringBuilder builder = new StringBuilder();
        Mode notationMode = this.mode;

        if (!Utils.isEmpty(this.value) && this.value.contains(" ") && (this.mode == Mode.UNQOUTED)) { //$NON-NLS-1$
            notationMode = Mode.SINGLE_QUOTED;
        }

        builder.append(notationMode);
        builder.append((this.value == null) ? Utils.EMPTY_STRING : this.value);
        builder.append(notationMode);

        return builder.toString();
    }

}
