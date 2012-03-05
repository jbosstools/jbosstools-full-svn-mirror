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
 * Property attributes of a property definitions.
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

    /**
     * Constructs a property definition's attribute collecdtion all set to there default values.
     */
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

    /**
     * @return the autocreated attribute (never <code>null</code>)
     */
    public Autocreated getAutocreated() {
        return this.autocreated;
    }

    private String getFormatDelimiter() {
        return CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.PROPERTY_DEFINITION_ATTRIBUTES_DELIMITER);
    }

    /**
     * @return the mandatory attribute (never <code>null</code>)
     */
    public Mandatory getMandatory() {
        return this.mandatory;
    }

    /**
     * @return the multiple attribute (never <code>null</code>)
     */
    public Multiple getMultiple() {
        return this.multiple;
    }

    /**
     * @return the no full text search attribute (never <code>null</code>)
     */
    public NoFullText getNoFullText() {
        return this.noFullText;
    }

    /**
     * @return the no query order attribute (never <code>null</code>)
     */
    public NoQueryOrder getNoQueryOrder() {
        return this.noQueryOrder;
    }

    /**
     * @return the on parent version attribute (never <code>null</code>)
     */
    public OnParentVersion getOnParentVersion() {
        return this.opv;
    }

    /**
     * @return the protected attribute (never <code>null</code>)
     */
    public Protected getProtected() {
        return this.notDeletable;
    }

    /**
     * @return the query operators attribute (never <code>null</code>)
     */
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

    /**
     * @param newState the proposed new state of the autocreated attribute (cannot be <code>null</code>)
     * @return <code>true</code> if the state was changed
     */
    public boolean setAutocreated( AttributeState.Value newState ) {
        return this.autocreated.set(newState);
    }

    /**
     * @param newState the proposed new state of the mandatory attribute (cannot be <code>null</code>)
     * @return <code>true</code> if the state was changed
     */
    public boolean setMandatory( AttributeState.Value newState ) {
        return this.mandatory.set(newState);
    }

    /**
     * @param newState the proposed new state of the multiple attribute (cannot be <code>null</code>)
     * @return <code>true</code> if the state was changed
     */
    public boolean setMultiple( AttributeState.Value newState ) {
        return this.multiple.set(newState);
    }

    /**
     * @param newState the proposed new state of the no full text search attribute (cannot be <code>null</code>)
     * @return <code>true</code> if the state was changed
     */
    public boolean setNoFullText( AttributeState.Value newState ) {
        return this.noFullText.set(newState);
    }

    /**
     * @param newState the proposed new state of the no query order attribute (cannot be <code>null</code>)
     * @return <code>true</code> if the state was changed
     */
    public boolean setNoQueryOrder( AttributeState.Value newState ) {
        return this.noQueryOrder.set(newState);
    }

    /**
     * @param newOpv the proposed new value of the on parent version attribute (cannot be <code>null</code>)
     * @return <code>true</code> if the value was changed
     */
    public boolean setOnParentVersion( OnParentVersion newOpv ) {
        if (this.opv != newOpv) {
            this.opv = newOpv;
            return true;
        }

        return false;
    }

    /**
     * @param newState the proposed new state of the protected attribute (cannot be <code>null</code>)
     * @return <code>true</code> if the state was changed
     */
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
