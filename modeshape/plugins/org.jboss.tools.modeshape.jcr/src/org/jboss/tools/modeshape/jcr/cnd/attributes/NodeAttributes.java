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
import org.jboss.tools.modeshape.jcr.cnd.CndNotationPreferences;
import org.jboss.tools.modeshape.jcr.cnd.CndNotationPreferences.Preference;
import org.jboss.tools.modeshape.jcr.cnd.attributes.AttributeState.Value;

/**
 * 
 */
public class NodeAttributes implements CndElement {

    public static final char DEFAULT_FORMAT_DELIMITER = ' ';

    private Autocreated autocreated;

    private Mandatory mandatory;

    private Protected notDeletable;

    private OnParentVersion opv;

    private SameNameSiblings sns;

    public NodeAttributes() {
        this.autocreated = new Autocreated();
        this.mandatory = new Mandatory();
        this.notDeletable = new Protected();
        this.opv = OnParentVersion.DEFAULT_VALUE;
        this.sns = new SameNameSiblings();
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

        if (this == obj) {
            return true;
        }

        NodeAttributes that = (NodeAttributes)obj;

        return (this.autocreated.equals(that.autocreated) && this.mandatory.equals(that.mandatory)
                && this.notDeletable.equals(that.notDeletable) && (this.opv == that.opv) && this.sns.equals(that.sns));
    }

    public Autocreated getAutocreated() {
        return this.autocreated;
    }

    private String getFormatDelimiter() {
        return CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.CHILD_NODE_ATTRIBUTES_DELIMITER);
    }

    public Mandatory getMandatory() {
        return this.mandatory;
    }

    public OnParentVersion getOnParentVersion() {
        return this.opv;
    }

    public Protected getProtected() {
        return this.notDeletable;
    }

    public SameNameSiblings getSameNameSiblings() {
        return this.sns;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Utils.hashCode(this.autocreated, this.mandatory, this.notDeletable, this.opv, this.sns);
    }

    public boolean setAutocreated( Value newState ) {
        return this.autocreated.set(newState);
    }

    public boolean setMandatory( Value newState ) {
        return this.mandatory.set(newState);
    }

    public boolean setOnParentVersion( OnParentVersion newOpv ) {
        if (this.opv != newOpv) {
            this.opv = newOpv;
            return true;
        }

        return false;
    }

    public boolean setProtected( Value newState ) {
        return this.notDeletable.set(newState);
    }

    public boolean setSameNameSibling( Value newState ) {
        return this.sns.set(newState);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.jcr.cnd.CndElement#toCndNotation(org.jboss.tools.modeshape.jcr.cnd.CndElement.NotationType)
     */
    @Override
    public String toCndNotation( NotationType notationType ) {
        String delim = getFormatDelimiter();
        StringBuilder builder = new StringBuilder();
        boolean addDelim = false;

        { // autocreated
            if (addDelim) {
                builder.append(delim);
            }

            String notation = this.autocreated.toCndNotation(notationType);

            if (!Utils.isEmpty(notation)) {
                builder.append(notation);
                addDelim = true;
            }
        }

        { // mandatory
            if (addDelim) {
                builder.append(delim);
            }

            String notation = this.mandatory.toCndNotation(notationType);

            if (!Utils.isEmpty(notation)) {
                builder.append(notation);
                addDelim = true;
            }
        }

        { // protected
            if (addDelim) {
                builder.append(delim);
            }

            String notation = this.notDeletable.toCndNotation(notationType);

            if (!Utils.isEmpty(notation)) {
                builder.append(notation);
                addDelim = true;
            }
        }

        { // on parent value
            if (addDelim) {
                builder.append(delim);
            }

            String notation = this.opv.toCndNotation(notationType);

            if (!Utils.isEmpty(notation)) {
                builder.append(notation);
                addDelim = true;
            }
        }

        { // same name siblings
            if (addDelim) {
                builder.append(delim);
            }

            String notation = this.sns.toCndNotation(notationType);

            if (!Utils.isEmpty(notation)) {
                builder.append(notation);
                addDelim = true;
            }
        }

        return builder.toString().trim();
    }
}
