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
public class NodeTypeAttributes implements CndElement {

    private Mixin mixin;

    private Abstract notConcrete;

    private Orderable orderable;

    private PrimaryItem primaryItem;

    private Queryable queryable;

    public NodeTypeAttributes() {
        this.orderable = new Orderable();
        this.mixin = new Mixin();
        this.notConcrete = new Abstract();
        this.primaryItem = new PrimaryItem();
        this.queryable = new Queryable();
    }

    /**
     * @param initialOrderable the initial orderable value (can be <code>null</code>)
     * @param initialMixin the initial mixin value (can be <code>null</code>)
     * @param initialAbstract the initial abstract value (can be <code>null</code>)
     * @param initialQueryable the initial queryable value (can be <code>null</code>)
     * @param initialPrimaryItem the initial primary item value (can be <code>null</code>)
     */
    public NodeTypeAttributes( Orderable initialOrderable,
                               Mixin initialMixin,
                               Abstract initialAbstract,
                               Queryable initialQueryable,
                               PrimaryItem initialPrimaryItem ) {
        this();

        if (!this.orderable.equals(initialOrderable)) {
            this.orderable = initialOrderable;
        }

        if (!this.mixin.equals(initialMixin)) {
            this.mixin = initialMixin;
        }

        if (!this.notConcrete.equals(initialOrderable)) {
            this.notConcrete = initialAbstract;
        }

        if (!this.queryable.equals(initialQueryable)) {
            this.queryable = initialQueryable;
        }

        if (!this.primaryItem.equals(initialPrimaryItem)) {
            this.primaryItem = initialPrimaryItem;
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

        NodeTypeAttributes that = (NodeTypeAttributes)obj;

        return (this.mixin.equals(that.mixin) && this.notConcrete.equals(that.notConcrete) && this.orderable.equals(that.orderable)
                && this.queryable.equals(that.queryable) && this.primaryItem.equals(that.primaryItem));
    }

    public Abstract getAbstract() {
        return this.notConcrete;
    }

    private String getFormatDelimiter() {
        return CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.NODE_TYPE_DEFINITION_ATTRIBUTES_DELIMITER);
    }

    public Mixin getMixin() {
        return this.mixin;
    }

    public Orderable getOrderable() {
        return this.orderable;
    }

    public PrimaryItem getPrimaryItem() {
        return this.primaryItem;
    }

    public Queryable getQueryable() {
        return this.queryable;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Utils.hashCode(this.mixin, this.notConcrete, this.orderable, this.queryable, this.primaryItem);
    }

    public boolean isPrimaryItemVariant() {
        return this.primaryItem.isVariant();
    }

    public boolean setAbstract( AttributeState.Value newState ) {
        return this.notConcrete.set(newState);
    }

    public boolean setMixin( AttributeState.Value newState ) {
        return this.mixin.set(newState);
    }

    public boolean setOrderable( AttributeState.Value newState ) {
        return this.orderable.set(newState);
    }

    public boolean setPrimaryItem( AttributeState.Value newState ) {
        return this.primaryItem.set(newState);
    }

    public boolean setPrimaryItem( String newPrimaryItem ) {
        return this.primaryItem.setPrimaryItem(newPrimaryItem);
    }

    public boolean setQueryable( AttributeState.Value newState ) {
        return this.queryable.set(newState);
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

        boolean addDelim = Utils.build(builder, false, DELIM, this.orderable.toCndNotation(notationType));
        addDelim = Utils.build(builder, addDelim, DELIM, this.mixin.toCndNotation(notationType));
        addDelim = Utils.build(builder, addDelim, DELIM, this.notConcrete.toCndNotation(notationType));
        addDelim = Utils.build(builder, addDelim, DELIM, this.queryable.toCndNotation(notationType));
        addDelim = Utils.build(builder, addDelim, DELIM, this.primaryItem.toCndNotation(notationType));

        return builder.toString().trim();
    }

}
