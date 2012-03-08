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
public class NamespaceMapping implements CndElement {

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
        URI
    }

    /**
     * The prefix used in CND notation before the namespace mapping.
     */
    public static final String NOTATION_PREFIX = "<"; //$NON-NLS-1$

    /**
     * The delimeter used to separate the prefix from the URI.
     */
    public static final String NOTATION_DELIMITER = "="; //$NON-NLS-1$

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
    public NamespaceMapping( String initialPrefix,
                             String initialUri ) {
        this();
        this.prefix.set(initialPrefix);
        this.uri.set(initialUri);
    }

    /**
     * @param newListener the listener being registered (cannot be <code>null</code>)
     * @return <code>true</code> if registered
     */
    public boolean addListener( PropertyChangeListener newListener ) {
        Utils.isNotNull(newListener, "newListener"); //$NON-NLS-1$
        return this.listeners.addIfAbsent(newListener);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object obj ) {
        if ((obj != null) && getClass().equals(obj.getClass())) {
            NamespaceMapping that = (NamespaceMapping)obj;
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

        PropertyChangeEvent event = new PropertyChangeEvent(this, property.toString(), oldValue, newValue);

        for (final Object listener : this.listeners.toArray()) {
            try {
                ((PropertyChangeListener)listener).propertyChange(event);
            } catch (Exception e) {
                // TODO log this
                this.listeners.remove(listener);
            }
        }
    }

    /**
     * @param listener the listener being unregistered (cannot be <code>null</code>)
     * @return <code>true</code> if removed
     */
    public boolean removeListener( PropertyChangeListener listener ) {
        Utils.isNotNull(listener, "listener"); //$NON-NLS-1$
        return this.listeners.remove(listener);
    }

    /**
     * @param newPrefix the new prefix value (can be <code>null</code> or empty)
     * @return true if the prefix was changed
     */
    public boolean setPrefix( String newPrefix ) {
        Object oldValue = this.prefix.get();
        boolean changed = this.prefix.set(newPrefix);

        if (changed) {
            notifyChangeListeners(PropertyName.PREFIX, oldValue, newPrefix);
        }

        return changed;
    }

    /**
     * @param newUri then new URI value (can be <code>null</code> or empty)
     * @return <code>true</code> if the URI was changed
     */
    public boolean setUri( String newUri ) {
        Object oldValue = this.uri.get();
        boolean changed = this.uri.set(newUri);

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
    public String toCndNotation( NotationType notationType ) {
        StringBuilder builder = new StringBuilder();
        builder.append(NOTATION_PREFIX);
        builder.append(this.prefix.toCndNotation(notationType));
        builder.append(NOTATION_DELIMITER);
        builder.append(this.uri.toCndNotation(notationType));
        builder.append(NOTATION_SUFFIX);

        return builder.toString();
    }

}