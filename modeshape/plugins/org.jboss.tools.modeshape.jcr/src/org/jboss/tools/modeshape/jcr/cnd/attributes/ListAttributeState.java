/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.jboss.tools.modeshape.jcr.cnd.attributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.jboss.tools.modeshape.jcr.Utils;
import org.jboss.tools.modeshape.jcr.cnd.CndElement;
import org.jboss.tools.modeshape.jcr.cnd.CndNotationPreferences;
import org.jboss.tools.modeshape.jcr.cnd.CndNotationPreferences.Preference;

/**
 * @param <E> the class of the list items
 */
public abstract class ListAttributeState<E extends Comparable> extends AttributeState {

    /**
     * A list of supported items (can be <code>null</code>).
     */
    private List<E> supported;

    /**
     * @param item the item being added (cannot be <code>null</code>)
     * @return <code>true</code> if successfully added
     */
    public boolean add( final E item ) {
        Utils.isNotNull(item, "item"); //$NON-NLS-1$

        if (this.supported == null) {
            this.supported = new ArrayList<E>();
        }

        boolean added = false;

        if (!this.supported.contains(item)) {
            added = this.supported.add(item);
        }

        if (added && !is()) {
            super.set(Value.IS);
        }

        return added;
    }

    /**
     * @return <code>true</code> if at least one item was cleared
     */
    public boolean clear() {
        boolean cleared = false;

        if (this.supported != null) {
            cleared = !this.supported.isEmpty();
            this.supported = null;
        }

        if (!isNot()) {
            super.set(Value.IS_NOT);
        }

        return cleared;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( final Object obj ) {
        if ((obj == null) || !getClass().equals(obj.getClass())) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        final ListAttributeState that = (ListAttributeState)obj;
        final List<E> thatSupportedItems = that.getSupportedItems();
        final List<E> thisSupportedItems = getSupportedItems();

        if (Utils.isEmpty(thisSupportedItems)) {
            return Utils.isEmpty(thatSupportedItems);
        }

        if (Utils.isEmpty(thatSupportedItems)) {
            return false;
        }

        if (thisSupportedItems.size() != thatSupportedItems.size()) {
            return false;
        }

        return thisSupportedItems.containsAll(thatSupportedItems);
    }

    /**
     * @param notationType the notation type whose CND notation prefix is being requested (cannot be <code>null</code>)
     * @return the CND notation prefix (can be <code>null</code> or empty)
     */
    protected abstract String getCndNotationPrefix( NotationType notationType );

    /**
     * @param notationType the notation type whose CND notation suffix is being requested (cannot be <code>null</code>)
     * @return the CND notation suffix (can be <code>null</code> or empty)
     */
    protected String getCndNotationSuffix( final NotationType notationType ) {
        return Utils.EMPTY_STRING;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.jcr.cnd.attributes.AttributeState#getCompactCndNotation()
     */
    @Override
    protected String getCompactCndNotation() {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.jcr.cnd.attributes.AttributeState#getCompressedCndNotation()
     */
    @Override
    protected String getCompressedCndNotation() {
        throw new UnsupportedOperationException();
    }

    /**
     * @return the delimiter used after the list prefix (never <code>null</code> but can be empty)
     */
    protected String getListPrefixEndDelimiter() {
        return CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_PREFIX_END_DELIMITER);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.jcr.cnd.attributes.AttributeState#getLongCndNotation()
     */
    @Override
    protected String getLongCndNotation() {
        throw new UnsupportedOperationException();
    }

    /**
     * @return the collection of supported items (never <code>null</code>)
     */
    public List<E> getSupportedItems() {
        if (this.supported == null) {
            return Collections.emptyList();
        }

        return Collections.unmodifiableList(this.supported);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.jcr.cnd.attributes.AttributeState#hasCndNotation()
     */
    @Override
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
        return Utils.hashCode(getSupportedItems());
    }

    /**
     * @param item the item being removed (cannot be <code>null</code>)
     * @return <code>true</code> if successfully removed
     */
    public boolean remove( final E item ) {
        Utils.isNotNull(item, "item"); //$NON-NLS-1$

        if (this.supported == null) {
            return false;
        }

        final boolean removed = this.supported.remove(item);

        if (this.supported.isEmpty()) {
            this.supported = null;

            if (is()) {
                super.set(Value.IS_NOT);
            }
        }

        return removed;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.jcr.cnd.attributes.AttributeState#set(org.jboss.tools.modeshape.jcr.cnd.attributes.AttributeState.Value)
     */
    @Override
    public final boolean set( final Value newState ) {
        if (Value.VARIANT == newState) {
            clear();
            return super.set(Value.VARIANT);
        }

        return false; // other states set by adding and removing supported items
    }

    /**
     * @param notationType the CND notation type to use (cannot be <code>null</code>)
     * @return the CND notation (never <code>null</code> but can be empty)
     */
    protected String supportedItemsCndNotation( final NotationType notationType ) {
        final List<E> items = new ArrayList<E>(getSupportedItems());
        Collections.sort(items);

        if (items.isEmpty()) {
            return Utils.EMPTY_STRING;
        }

        if (items.size() == 1) {
            final E firstItem = items.iterator().next();

            if (firstItem instanceof CndElement) {
                return ((CndElement)firstItem).toCndNotation(notationType);
            }

            return firstItem.toString();
        }

        final StringBuilder builder = new StringBuilder();

        for (final Iterator<E> itr = items.iterator(); itr.hasNext();) {
            final E item = itr.next();

            if (item instanceof CndElement) {
                builder.append(((CndElement)item).toCndNotation(notationType));
            } else {
                builder.append(item.toString());
            }

            if (itr.hasNext()) {
                builder.append(Preference.ATTRIBUTE_LIST_ELEMENT_DELIMITER);
            }
        }

        return builder.toString();
    }

    /**
     * @param item the item being checked (cannot be <code>null</code>)
     * @return <code>true</code> if item is contained in list
     */
    public boolean supports( final E item ) {
        Utils.isNotNull(item, "item"); //$NON-NLS-1$
        return this.supported.contains(item);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.jcr.cnd.CndElement#toCndNotation(org.jboss.tools.modeshape.jcr.cnd.CndElement.NotationType)
     */
    @Override
    public String toCndNotation( final NotationType notationType ) {
        if (hasCndNotation()) {
            final StringBuilder builder = new StringBuilder();

            if (!Utils.isEmpty(getCndNotationPrefix(notationType))) {
                builder.append(getCndNotationPrefix(notationType));
            }

            final String delim = getListPrefixEndDelimiter();

            if (!Utils.isEmpty(delim)) {
                builder.append(delim);
            }

            if (isVariant()) {
                builder.append(AttributeState.VARIANT_CHAR);
            } else {
                // add the delimited list
                builder.append(CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_QUOTE_CHAR));
                builder.append(supportedItemsCndNotation(notationType));
                builder.append(CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_QUOTE_CHAR));
            }

            if (!Utils.isEmpty(getCndNotationSuffix(notationType))) {
                builder.append(getCndNotationSuffix(notationType));
            }

            return builder.toString();
        }

        return Utils.EMPTY_STRING;
    }
}
