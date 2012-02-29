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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jboss.tools.modeshape.jcr.Utils;
import org.jboss.tools.modeshape.jcr.cnd.CndNotationPreferences.Preference;

/**
 * The <code>CompactNodeTypeDefinition</code> class represents one CND file.
 */
public class CompactNodeTypeDefinition implements CndElement {

    /**
     * The property names whose <code>toString()</code> is used in {@link PropertyChangeEvent}s.
     */
    public enum PropertyName {

        /**
         * The namespace mappings property.
         */
        NAMESPACE_MAPPINGS,

        /**
         * The node type definitions property.
         */
        NODE_TYPE_DEFINITIONS
    }

    /**
     * The registered property change listeners (never <code>null</code>).
     */
    private final CopyOnWriteArrayList<PropertyChangeListener> listeners;

    /**
     * The namespace mappings (can be <code>null</code>).
     */
    private List<NamespaceMapping> namespaceMappings;

    /**
     * The node type definitions (can be <code>null</code>).
     */
    private List<NodeTypeDefinition> nodeTypeDefinitions;

    /**
     * Constructs an instance with no namespace mappings or node type definitions.
     */
    public CompactNodeTypeDefinition() {
        this.listeners = new CopyOnWriteArrayList<PropertyChangeListener>();
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
     * If added, broadcasts a {@link PropertyChangeEvent} with an old value of <code>null</code> and a new value equal to
     * <code>namespaceMappingToAdd</code>.
     * 
     * @param namespaceMappingToAdd the namespace mapping being added (cannot be <code>null</code>)
     * @return <code>true</code> if added
     */
    public boolean addNamespaceMapping( NamespaceMapping namespaceMappingToAdd ) {
        Utils.isNotNull(namespaceMappingToAdd, "namespaceMappingToAdd"); //$NON-NLS-1$

        if (this.namespaceMappings == null) {
            this.namespaceMappings = new ArrayList<NamespaceMapping>();
        }

        if (this.namespaceMappings.add(namespaceMappingToAdd)) {
            notifyChangeListeners(PropertyName.NAMESPACE_MAPPINGS, null, namespaceMappingToAdd);
            return true; // added
        }

        return false; // not added
    }

    /**
     * If added, broadcasts a {@link PropertyChangeEvent} with an old value of <code>null</code> and a new value of
     * <code>nodeTypeDefinitionToAdd</code>.
     * 
     * @param nodeTypeDefinitionToAdd the node type definition being added (cannot be <code>null</code>)
     * @return <code>true</code> if added
     */
    public boolean addNodeTypeDefinition( NodeTypeDefinition nodeTypeDefinitionToAdd ) {
        Utils.isNotNull(nodeTypeDefinitionToAdd, "nodeTypeDefinitionToAdd"); //$NON-NLS-1$

        if (this.nodeTypeDefinitions == null) {
            this.nodeTypeDefinitions = new ArrayList<NodeTypeDefinition>();
        }

        if (this.nodeTypeDefinitions.add(nodeTypeDefinitionToAdd)) {
            notifyChangeListeners(PropertyName.NODE_TYPE_DEFINITIONS, null, nodeTypeDefinitionToAdd);
            return true; // added
        }

        return false; // not added
    }

    /**
     * If at least one namespace mapping was removed, broadcasts a {@link PropertyChangeEvent} with an old value equal to the old
     * namespace mappings collection and a new value of <code>null</code>.
     * 
     * @return <code>true</code> if at least one namespace mapping was removed
     */
    public boolean clearNamespaceMappings() {
        if (this.namespaceMappings == null) {
            return false; // nothing to clear
        }

        boolean wasCleared = false;

        if (!this.namespaceMappings.isEmpty()) {
            Object oldValue = new ArrayList<NamespaceMapping>(this.namespaceMappings);
            wasCleared = true;
            this.namespaceMappings.clear();
            notifyChangeListeners(PropertyName.NAMESPACE_MAPPINGS, oldValue, null);
        }

        this.namespaceMappings = null;
        return wasCleared;
    }

    /**
     * If at least one node type definition was removed, broadcasts a {@link PropertyChangeEvent} with an old value equal to the old
     * node type definitions collection and a new value of <code>null</code>.
     * 
     * @return <code>true</code> if at least one namespace mapping was removed
     */
    public boolean clearNodeTypeDefinitions() {
        if (this.nodeTypeDefinitions == null) {
            return false; // nothing to clear
        }

        boolean wasCleared = false;

        if (!this.nodeTypeDefinitions.isEmpty()) {
            Object oldValue = new ArrayList<NodeTypeDefinition>(nodeTypeDefinitions);
            wasCleared = true;
            this.nodeTypeDefinitions.clear();
            notifyChangeListeners(PropertyName.NODE_TYPE_DEFINITIONS, oldValue, null);
        }

        this.nodeTypeDefinitions = null;
        return wasCleared;
    }

    private String getEndNamespaceMappingSectionDelimiter() {
        return CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.NAMESPACE_MAPPING_SECTION_END_DELIMITER);
    }

    private String getEndNodeTypeDefinitionSectionDelimiter() {
        return CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.NODE_TYPE_DEFINITION_SECTION_END_DELIMITER);
    }

    private String getNamespaceMappingDelimiter() {
        return CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.NAMESPACE_MAPPING_DELIMITER);
    }

    /**
     * @return the namespace mappings (never <code>null</code>)
     */
    public List<NamespaceMapping> getNamespaceMappings() {
        if (this.namespaceMappings == null) {
            return Collections.emptyList();
        }

        return this.namespaceMappings;
    }

    private String getNodeTypeDefinitionDelimiter() {
        return CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.NODE_TYPE_DEFINITION_DELIMITER);
    }

    /**
     * @return the node type definitions (never <code>null</code>)
     */
    public List<NodeTypeDefinition> getNodeTypeDefinitions() {
        if (this.nodeTypeDefinitions == null) {
            return Collections.emptyList();
        }

        return this.nodeTypeDefinitions;
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
     * If namespace mapping is removed, broadcasts a {@link PropertyChangeEvent} with an old value of
     * <code>namespaceMappingToRemove</code> and a new value of <code>null</code>.
     * 
     * @param namespaceMappingToRemove the namespace mapping being removed (cannot be <code>null</code>)
     * @return <code>true</code> if removed
     */
    public boolean removeNamespaceMapping( NamespaceMapping namespaceMappingToRemove ) {
        Utils.isNotNull(namespaceMappingToRemove, "namespaceMappingToRemove"); //$NON-NLS-1$

        if ((this.namespaceMappings != null) && this.namespaceMappings.remove(namespaceMappingToRemove)) {
            notifyChangeListeners(PropertyName.NAMESPACE_MAPPINGS, namespaceMappingToRemove, null);

            if (this.namespaceMappings.isEmpty()) {
                this.namespaceMappings = null;
            }

            return true; // removed
        }

        return false; // not removed
    }

    /**
     * If node type definition is removed, broadcasts a {@link PropertyChangeEvent} with an old value of
     * <code>nodeTypeDefinitionToRemove</code> and a new value of <code>null</code>.
     * 
     * @param nodeTypeDefinitionToRemove the node type definition being removed (cannot be <code>null</code>)
     * @return <code>true</code> if removed
     */
    public boolean removeNodeTypeDefinition( NodeTypeDefinition nodeTypeDefinitionToRemove ) {
        Utils.isNotNull(nodeTypeDefinitionToRemove, "nodeTypeDefinitionToRemove"); //$NON-NLS-1$

        if ((this.nodeTypeDefinitions != null) && this.nodeTypeDefinitions.remove(nodeTypeDefinitionToRemove)) {
            notifyChangeListeners(PropertyName.NODE_TYPE_DEFINITIONS, nodeTypeDefinitionToRemove, null);

            if (this.nodeTypeDefinitions.isEmpty()) {
                this.nodeTypeDefinitions = null;
            }

            return true; // removed
        }

        return false; // not removed
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.jcr.cnd.CndElement#toCndNotation(org.jboss.tools.modeshape.jcr.cnd.CndElement.NotationType)
     */
    @Override
    public String toCndNotation( NotationType notationType ) {
        StringBuilder builder = new StringBuilder();
        boolean addDelim = false;

        { // namespace mappings
            if (!Utils.isEmpty(this.namespaceMappings)) {
                final String DELIM = getNamespaceMappingDelimiter();

                for (NamespaceMapping namespaceMapping : this.namespaceMappings) {
                    addDelim = Utils.build(builder, addDelim, DELIM, namespaceMapping.toCndNotation(notationType));
                }

                builder.append(getEndNamespaceMappingSectionDelimiter());
            }
        }

        { // node type definitions
            if (!Utils.isEmpty(this.nodeTypeDefinitions)) {
                final String DELIM = getNodeTypeDefinitionDelimiter();

                for (NodeTypeDefinition nodeTypeDefinition : this.nodeTypeDefinitions) {
                    addDelim = Utils.build(builder, addDelim, DELIM, nodeTypeDefinition.toCndNotation(notationType));
                }

                builder.append(getEndNodeTypeDefinitionSectionDelimiter());
            }
        }

        return builder.toString();
    }

}
