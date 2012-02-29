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

/**
 * 
 */
public class PropertyAttributes implements CndElement {

    private Autocreated autocreated;

    private Mandatory mandatory;

    private Multiple multiple;

    private NoFullText noFullText;

    private NoQueryOrder noQueryOrder;

    private Protected notDeletable; // protected

    private OnParentVersion opv;

    private QueryOperators queryOps;

    public PropertyAttributes() {
        this.autocreated = new Autocreated();
        this.mandatory = new Mandatory();
        this.multiple = new Multiple();
        this.notDeletable = new Protected();
        this.opv = OnParentVersion.DEFAULT_VALUE;
        this.noFullText = new NoFullText();
        this.noQueryOrder = new NoQueryOrder();
        this.queryOps = new QueryOperators();
    }

    /**
     * @param initialAutocreated the initial autocreated value (can be <code>null</code>)
     * @param initialMandatory the initial mandatory value (can be <code>null</code>)
     * @param initialMultiple the initial multiple values value (can be <code>null</code>)
     * @param initialProtected the initial protected value (can be <code>null</code>)
     * @param initialOpv the initial on-parent-value value (can be <code>null</code>)
     * @param initialNoFullText the initial no full text search support value (can be <code>null</code>)
     * @param initialNoQueryOrder the initial no query order support value (can be <code>null</code>)
     * @param initialQueryOps the initial query operator support value (can be <code>null</code>)
     */
    public PropertyAttributes( Autocreated initialAutocreated,
                               Mandatory initialMandatory,
                               Multiple initialMultiple,
                               Protected initialProtected,
                               OnParentVersion initialOpv,
                               NoFullText initialNoFullText,
                               NoQueryOrder initialNoQueryOrder,
                               QueryOperators initialQueryOps ) {
        this();

        if (!this.autocreated.equals(initialAutocreated)) {
            this.autocreated = initialAutocreated;
        }

        if (!this.mandatory.equals(initialMandatory)) {
            this.mandatory = initialMandatory;
        }

        if (!this.multiple.equals(initialMultiple)) {
            this.multiple = initialMultiple;
        }

        if (!this.notDeletable.equals(initialProtected)) {
            this.notDeletable = initialProtected;
        }

        if (initialOpv != this.opv) {
            this.opv = initialOpv;
        }

        if (!this.noFullText.equals(initialNoFullText)) {
            this.noFullText = initialNoFullText;
        }

        if (!this.noQueryOrder.equals(initialNoQueryOrder)) {
            this.noQueryOrder = initialNoQueryOrder;
        }

        if (!this.queryOps.equals(initialQueryOps)) {
            this.queryOps = initialQueryOps;
        }
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

        PropertyAttributes that = (PropertyAttributes)obj;

        return (this.autocreated.equals(that.autocreated) && this.mandatory.equals(that.mandatory)
                && this.multiple.equals(that.multiple) && this.noFullText.equals(that.noFullText)
                && this.noQueryOrder.equals(that.noQueryOrder) && this.notDeletable.equals(that.notDeletable)
                && (this.opv == that.opv) && this.queryOps.equals(that.queryOps));
    }

    public Autocreated getAutocreated() {
        return this.autocreated;
    }

    private String getFormatDelimiter() {
        return CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.PROPERTY_DEFINITION_ATTRIBUTES_DELIMITER);
    }

    public Mandatory getMandatory() {
        return this.mandatory;
    }

    public Multiple getMultiple() {
        return this.multiple;
    }

    public NoFullText getNoFullText() {
        return this.noFullText;
    }

    public NoQueryOrder getNoQueryOrder() {
        return this.noQueryOrder;
    }

    public OnParentVersion getOnParentVersion() {
        return this.opv;
    }

    public Protected getProtected() {
        return this.notDeletable;
    }

    public QueryOperators getQueryOps() {
        return this.queryOps;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Utils.hashCode(0, this.autocreated, this.mandatory, this.multiple, this.noFullText, this.noQueryOrder,
                              this.notDeletable, this.opv, this.queryOps);
    }

    public boolean setAutocreated( AttributeState.Value newState ) {
        return this.autocreated.set(newState);
    }

    public boolean setMandatory( AttributeState.Value newState ) {
        return this.mandatory.set(newState);
    }

    public boolean setMultiple( AttributeState.Value newState ) {
        return this.multiple.set(newState);
    }

    public boolean setNoFullText( AttributeState.Value newState ) {
        return this.noFullText.set(newState);
    }

    public boolean setNoQueryOrder( AttributeState.Value newState ) {
        return this.noQueryOrder.set(newState);
    }

    public boolean setOnParentVersion( OnParentVersion newOpv ) {
        if (this.opv != newOpv) {
            this.opv = newOpv;
            return true;
        }

        return false;
    }

    public boolean setProtected( AttributeState.Value newState ) {
        return this.notDeletable.set(newState);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.jcr.cnd.CndElement#toCndNotation(org.jboss.tools.modeshape.jcr.cnd.CndElement.NotationType)
     */
    @Override
    public String toCndNotation( NotationType notationType ) {
        final String DELIM = getFormatDelimiter();
        StringBuilder builder = new StringBuilder();

        boolean addDelim = Utils.build(builder, false, DELIM, this.autocreated.toCndNotation(notationType));
        addDelim = Utils.build(builder, addDelim, DELIM, this.mandatory.toCndNotation(notationType));
        addDelim = Utils.build(builder, addDelim, DELIM, this.notDeletable.toCndNotation(notationType));
        addDelim = Utils.build(builder, addDelim, DELIM, this.multiple.toCndNotation(notationType));
        addDelim = Utils.build(builder, addDelim, DELIM, this.opv.toCndNotation(notationType));
        addDelim = Utils.build(builder, addDelim, DELIM, this.noFullText.toCndNotation(notationType));
        addDelim = Utils.build(builder, addDelim, DELIM, this.noQueryOrder.toCndNotation(notationType));
        addDelim = Utils.build(builder, addDelim, DELIM, this.queryOps.toCndNotation(notationType));

        return builder.toString().trim();
    }
}
