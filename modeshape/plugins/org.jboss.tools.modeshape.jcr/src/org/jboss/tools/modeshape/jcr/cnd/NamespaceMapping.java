/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.jboss.tools.modeshape.jcr.cnd;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jboss.tools.modeshape.jcr.Utils;

/**
 * The <code>NamespaceMapping</code> class represents a namespace. Each namespace mapping includes a prefix and a URI.
 */
public class NamespaceMapping implements CndElement, Comparable {

    /**
     * @param namespaceMappingToCopy the namespace mapping being copied (cannot be <code>null</code>)
     * @return a new namespace mapping exactly equal to the one that was copied (never <code>null</code>)
     */
    public static NamespaceMapping copy( NamespaceMapping namespaceMappingToCopy ) {
        return new NamespaceMapping(namespaceMappingToCopy.getPrefix(), namespaceMappingToCopy.getUri());
    }

    /**
     * The delimeter used to separate the prefix from the URI.
     */
    public static final String NOTATION_DELIMITER = "="; //$NON-NLS-1$

    /**
     * The prefix used in CND notation before the namespace mapping.
     */
    public static final String NOTATION_PREFIX = "<"; //$NON-NLS-1$

    /**
     * The suffix used in CND notation after the namespace mapping.
     */
    public static final String NOTATION_SUFFIX = ">"; //$NON-NLS-1$

    /**
     * The registered property change listeners (never <code>null</code>).
     */
    private final CopyOnWriteArrayList<PropertyChangeListener> listeners;

    /**
     * The namespace prefix (can be <code>null</code> ore empty).
     */
    private final LocalName prefix;

    /**
     * The namespace URI (can be <code>null</code> or empty).
     */
    private final LocalName uri;

    /**
     * Constructs an instance with no prefix and no URI.
     */
    public NamespaceMapping() {
        this.listeners = new CopyOnWriteArrayList<PropertyChangeListener>();
        this.prefix = new LocalName();
        this.uri = new LocalName();
    }

    /**
     * Constructs an instance with the specified prefix and URI.
     * 
     * @param initialPrefix the initial prefix (can be <code>null</code> or empty)
     * @param initialUri the initial URI (can be <code>null</code> or empty)
     */
    public NamespaceMapping( final String initialPrefix,
                             final String initialUri ) {
        this();
        this.prefix.set(initialPrefix);
        this.uri.set(initialUri);
    }

    /**
     * @param newListener the listener being registered (cannot be <code>null</code>)
     * @return <code>true</code> if registered
     */
    public boolean addListener( final PropertyChangeListener newListener ) {
        Utils.verifyIsNotNull(newListener, "newListener"); //$NON-NLS-1$
        return this.listeners.addIfAbsent(newListener);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo( final Object object ) {
        final NamespaceMapping that = (NamespaceMapping)object;
        final String thisPrefix = getPrefix();
        final String thatPrefix = that.getPrefix();

        if (Utils.isEmpty(thisPrefix)) {
            if (Utils.isEmpty(thatPrefix)) {
                return 0;
            }

            // thatName is not empty
            return 1;
        }

        // thisName is not empty
        if (thatPrefix == null) {
            return 1;
        }

        // thisName and thatName are not empty
        return thisPrefix.compareTo(thatPrefix);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( final Object obj ) {
        if ((obj != null) && getClass().equals(obj.getClass())) {
            final NamespaceMapping that = (NamespaceMapping)obj;
            return (this.prefix.equals(that.prefix) && this.uri.equals(that.uri));
        }

        return false;
    }

    /**
     * @return the prefix (can be <code>null</code> or empty)
     */
    public String getPrefix() {
        return this.prefix.get();
    }

    /**
     * @return the URI (can be <code>null</code> or empty)
     */
    public String getUri() {
        return this.uri.get();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Utils.hashCode(this.prefix, this.uri);
    }

    /**
     * @param property the property that was changed (never <code>null</code>)
     * @param oldValue the old value (can be <code>null</code>)
     * @param newValue the new value (can be <code>null</code>)
     */
    private void notifyChangeListeners( final PropertyName property,
                                        final Object oldValue,
                                        final Object newValue ) {
        assert (property != null) : "property is null"; //$NON-NLS-1$

        final PropertyChangeEvent event = new PropertyChangeEvent(this, property.toString(), oldValue, newValue);

        for (final Object listener : this.listeners.toArray()) {
            try {
                ((PropertyChangeListener)listener).propertyChange(event);
            } catch (final Exception e) {
                // TODO log this
                this.listeners.remove(listener);
            }
        }
    }

    /**
     * @param listener the listener being unregistered (cannot be <code>null</code>)
     * @return <code>true</code> if removed
     */
    public boolean removeListener( final PropertyChangeListener listener ) {
        Utils.verifyIsNotNull(listener, "listener"); //$NON-NLS-1$
        return this.listeners.remove(listener);
    }

    /**
     * @param newPrefix the new prefix value (can be <code>null</code> or empty)
     * @return true if the prefix was changed
     */
    public boolean setPrefix( final String newPrefix ) {
        final Object oldValue = this.prefix.get();
        final boolean changed = this.prefix.set(newPrefix);

        if (changed) {
            notifyChangeListeners(PropertyName.PREFIX, oldValue, newPrefix);
        }

        return changed;
    }

    /**
     * @param newUri then new URI value (can be <code>null</code> or empty)
     * @return <code>true</code> if the URI was changed
     */
    public boolean setUri( final String newUri ) {
        final Object oldValue = this.uri.get();
        final boolean changed = this.uri.set(newUri);

        if (changed) {
            notifyChangeListeners(PropertyName.URI, oldValue, newUri);
        }

        return changed;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.jcr.cnd.CndElement#toCndNotation(org.jboss.tools.modeshape.jcr.cnd.CndElement.NotationType)
     */
    @Override
    public String toCndNotation( final NotationType notationType ) {
        final StringBuilder builder = new StringBuilder();
        builder.append(NOTATION_PREFIX);
        builder.append(this.prefix.toCndNotation(notationType));
        builder.append(NOTATION_DELIMITER);
        builder.append(this.uri.toCndNotation(notationType));
        builder.append(NOTATION_SUFFIX);

        return builder.toString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(this.prefix);
        builder.append(NOTATION_DELIMITER);
        builder.append(this.uri);

        return builder.toString();
    }

    /**
     * The property names whose <code>toString()</code> is used in {@link PropertyChangeEvent}s.
     */
    public enum PropertyName {

        /**
         * The namespace prefix.
         */
        PREFIX,

        /**
         * The namespace URI.
         */
        URI;

        /**
         * {@inheritDoc}
         * 
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return (getClass().getName() + super.toString());
        }
    }
}
