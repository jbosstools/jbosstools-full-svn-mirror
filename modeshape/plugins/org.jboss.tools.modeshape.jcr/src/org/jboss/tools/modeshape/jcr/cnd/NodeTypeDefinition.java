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

import javax.jcr.nodetype.NodeDefinition;
import javax.jcr.nodetype.NodeDefinitionTemplate;
import javax.jcr.nodetype.NodeTypeTemplate;
import javax.jcr.nodetype.PropertyDefinitionTemplate;

import org.eclipse.osgi.util.NLS;
import org.jboss.tools.modeshape.jcr.Messages;
import org.jboss.tools.modeshape.jcr.Utils;
import org.jboss.tools.modeshape.jcr.cnd.CndNotationPreferences.Preference;
import org.jboss.tools.modeshape.jcr.cnd.attributes.AttributeState;
import org.jboss.tools.modeshape.jcr.cnd.attributes.AttributeState.Value;
import org.jboss.tools.modeshape.jcr.cnd.attributes.NodeTypeAttributes;
import org.jboss.tools.modeshape.jcr.cnd.attributes.SuperTypes;

/**
 * Represents a CND node type definition.
 */
public class NodeTypeDefinition implements CndElement, Comparable, NodeTypeTemplate {

    /**
     * Then node type name prefix used in the CND notation.
     */
    public static final String NAME_NOTATION_PREFIX = "["; //$NON-NLS-1$

    /**
     * Then node type name suffix used in the CND notation.
     */
    public static final String NAME_NOTATION_SUFFIX = "]"; //$NON-NLS-1$

    /**
     * The node type attributes (never <code>null</code>).
     */
    private final NodeTypeAttributes attributes;

    /**
     * The collection of child node definitions (can be <code>null</code>).
     */
    private List<ChildNodeDefinition> childNodesDefinitions;

    /**
     * The collection of property change listeners (never <code>null</code>).
     */
    private final CopyOnWriteArrayList<PropertyChangeListener> listeners;

    /**
     * The node type name (never <code>null</code> but can have a <code>null</code> or empty value).
     */
    private final QualifiedName name;

    /**
     * The collection of property definitions (can be <code>null</code>).
     */
    private List<PropertyDefinition> propertyDefinitions;

    /**
     * The super types (never <code>null</code>).
     */
    private final SuperTypes superTypes;

    /**
     * Constructs a node type definition having default values for all attributes and properties.
     */
    public NodeTypeDefinition() {
        this.attributes = new NodeTypeAttributes();
        this.name = new QualifiedName();
        this.superTypes = new SuperTypes();
        this.listeners = new CopyOnWriteArrayList<PropertyChangeListener>();
    }

    /**
     * If added, a property change event is broadcast to all registered listeners.
     * 
     * @param childNodeDefinitionBeingAdded the child node definition being added (cannot be <code>null</code>)
     * @return <code>true</code> if successfully added
     */
    public boolean addChildNodeDefinition( final ChildNodeDefinition childNodeDefinitionBeingAdded ) {
        Utils.verifyIsNotNull(childNodeDefinitionBeingAdded, "childNodeDefinitionBeingAdded"); //$NON-NLS-1$

        if (this.childNodesDefinitions == null) {
            this.childNodesDefinitions = new ArrayList<ChildNodeDefinition>();
        }

        if (this.childNodesDefinitions.add(childNodeDefinitionBeingAdded)) {
            notifyChangeListeners(PropertyName.CHILD_NODES, null, childNodeDefinitionBeingAdded);
            return true; // added
        }

        return false; // not added
    }

    /**
     * @param newListener the listener being added (cannot be <code>null</code>)
     * @return <code>true</code> if successfully added
     */
    public boolean addListener( final PropertyChangeListener newListener ) {
        Utils.verifyIsNotNull(newListener, "newListener"); //$NON-NLS-1$
        return this.listeners.addIfAbsent(newListener);
    }

    /**
     * If added, a property change event is broadcast to all registered listeners.
     * 
     * @param properyDefinitionBeingAdded the property definition being added (cannot be <code>null</code>)
     * @return <code>true</code> if successfully added
     */
    public boolean addPropertyDefinition( final PropertyDefinition properyDefinitionBeingAdded ) {
        Utils.verifyIsNotNull(properyDefinitionBeingAdded, "properyDefinitionBeingAdded"); //$NON-NLS-1$

        if (this.propertyDefinitions == null) {
            this.propertyDefinitions = new ArrayList<PropertyDefinition>();
        }

        if (this.propertyDefinitions.add(properyDefinitionBeingAdded)) {
            notifyChangeListeners(PropertyName.PROPERTY_DEFINITIONS, null, properyDefinitionBeingAdded);
            return true; // added
        }

        return false; // not added
    }

    /**
     * If added, a property change event is broadcast to all registered listeners.
     * 
     * @param superTypeBeingAdded the super type name being added (cannot be <code>null</code> or empty)
     * @return <code>true</code> if successfully added
     */
    public boolean addSuperType( final String superTypeBeingAdded ) {
        Utils.verifyIsNotEmpty(superTypeBeingAdded, "superTypeBeingAdded"); //$NON-NLS-1$

        if (this.superTypes.add(QualifiedName.parse(superTypeBeingAdded))) {
            notifyChangeListeners(PropertyName.SUPERTYPES, null, superTypeBeingAdded);
            return true; // added
        }

        return false; // not added
    }

    /**
     * Changes the attribute state of the specified property. Can be used to change the state of the abstract, mixin, orderable,
     * primary item, queryable, and super types properties.
     * 
     * @param propertyName the property whose state is being changed (cannot be <code>null</code>)
     * @param newState the new state (cannot be <code>null</code>)
     * @return <code>true</code> if the attribute state was changed
     */
    public boolean changeState( final PropertyName propertyName,
                                final Value newState ) {
        Utils.verifyIsNotNull(propertyName, "propertyName"); //$NON-NLS-1$
        Utils.verifyIsNotNull(newState, "newState"); //$NON-NLS-1$

        Object oldValue = null;
        final Object newValue = newState;
        boolean changed = false;

        if (PropertyName.ABSTRACT == propertyName) {
            oldValue = this.attributes.getAbstract().get();
            changed = this.attributes.getAbstract().set(newState);
        } else if (PropertyName.MIXIN == propertyName) {
            oldValue = this.attributes.getMixin().get();
            changed = this.attributes.getMixin().set(newState);
        } else if (PropertyName.ORDERABLE == propertyName) {
            oldValue = this.attributes.getOrderable().get();
            changed = this.attributes.getOrderable().set(newState);
        } else if (PropertyName.PRIMARY_ITEM == propertyName) {
            oldValue = this.attributes.getPrimaryItem().get();
            changed = this.attributes.getPrimaryItem().set(newState);
        } else if (PropertyName.QUERYABLE == propertyName) {
            oldValue = this.attributes.getQueryable().get();
            changed = this.attributes.getQueryable().set(newState);
        } else if (PropertyName.SUPERTYPES == propertyName) {
            oldValue = this.superTypes.get();
            changed = this.superTypes.set(newState);
        }

        if (changed) {
            notifyChangeListeners(propertyName, oldValue, newValue);
            return true; // changed state
        }

        return false; // did not change state
    }

    /**
     * If cleared, a property change event is broadcast to all registered listeners.
     * 
     * @return <code>true</code> if there was at least one child node before clearing
     */
    public boolean clearChildNodeDefinitions() {
        if (this.childNodesDefinitions == null) {
            return false; // nothing to clear
        }

        final List<ChildNodeDefinition> childNodes = new ArrayList<ChildNodeDefinition>(getChildNodeDefinitions());
        final boolean cleared = !this.childNodesDefinitions.isEmpty();
        this.childNodesDefinitions = null;

        if (cleared) {
            notifyChangeListeners(PropertyName.CHILD_NODES, childNodes, null);
        }

        return cleared;
    }

    /**
     * If cleared, a property change event is broadcast to all registered listeners.
     * 
     * @return <code>true</code> if there was at least one property definition before clearing
     */
    public boolean clearPropertyDefinitions() {
        if (this.propertyDefinitions == null) {
            return false; // nothing to clear
        }

        final List<PropertyDefinition> propDefns = new ArrayList<PropertyDefinition>(getPropertyDefinitions());
        final boolean cleared = !this.propertyDefinitions.isEmpty();
        this.propertyDefinitions = null;

        if (cleared) {
            notifyChangeListeners(PropertyName.PROPERTY_DEFINITIONS, propDefns, null);
        }

        return cleared;
    }

    /**
     * If cleared, a property change event is broadcast to all registered listeners.
     * 
     * @return <code>true</code> if there was at least one super type before clearing
     */
    public boolean clearSuperTypes() {
        final List<QualifiedName> types = new ArrayList<QualifiedName>(this.superTypes.getSupportedItems());

        if (this.superTypes.clear()) {
            notifyChangeListeners(PropertyName.SUPERTYPES, types, null);
            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo( final Object object ) {
        final NodeTypeDefinition that = (NodeTypeDefinition)object;
        final String thisName = getName();
        final String thatName = that.getName();

        if (Utils.isEmpty(thisName)) {
            if (Utils.isEmpty(thatName)) {
                return 0;
            }

            // thatName is not empty
            return 1;
        }

        // thisName is not empty
        if (thatName == null) {
            return 1;
        }

        // thisName and thatName are not empty
        return thisName.compareTo(thatName);
    }

    private String getChildNodeDefinitionDelimiter() {
        return CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.CHILD_NODE_DEFINITION_DELIMITER);
    }

    /**
     * @return the child node definitions (never <code>null</code>)
     */
    public List<ChildNodeDefinition> getChildNodeDefinitions() {
        if (this.childNodesDefinitions == null) {
            return Collections.emptyList();
        }

        return this.childNodesDefinitions;
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.nodetype.NodeTypeDefinition#getDeclaredChildNodeDefinitions()
     */
    @Override
    public NodeDefinition[] getDeclaredChildNodeDefinitions() {
        final List<ChildNodeDefinition> childNodes = getChildNodeDefinitions();
        return childNodes.toArray(new NodeDefinition[childNodes.size()]);
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.nodetype.NodeTypeDefinition#getDeclaredPropertyDefinitions()
     */
    @Override
    public PropertyDefinition[] getDeclaredPropertyDefinitions() {
        final List<PropertyDefinition> propDefns = getPropertyDefinitions();
        return propDefns.toArray(new PropertyDefinition[propDefns.size()]);
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.nodetype.NodeTypeDefinition#getDeclaredSupertypeNames()
     */
    @Override
    public String[] getDeclaredSupertypeNames() {
        return this.superTypes.toArray();
    }

    private String getEndAttributesDelimiter() {
        return CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.NODE_TYPE_DEFINITION_ATTRIBUTES_END_DELIMITER);
    }

    private String getEndChildNodeDefinitionsDelimiter() {
        return CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.CHILD_NODE_DEFINITION_SECTION_END_DELIMITER);
    }

    private String getEndNameDelimiter() {
        return CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.NODE_TYPE_DEFINITION_NAME_END_DELIMITER);
    }

    private String getEndPropertyDefinitionsDelimiter() {
        return CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.PROPERTY_DEFINITION_SECTION_END_DELIMITER);
    }

    private String getEndSuperTypesDelimiter() {
        return CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.SUPER_TYPES_END_DELIMITER);
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.nodetype.NodeTypeDefinition#getName()
     */
    @Override
    public String getName() {
        return this.name.get();
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.nodetype.NodeTypeTemplate#getNodeDefinitionTemplates()
     */
    @Override
    public List getNodeDefinitionTemplates() {
        return new ArrayList<NodeDefinitionTemplate>(getChildNodeDefinitions());
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.nodetype.NodeTypeDefinition#getPrimaryItemName()
     */
    @Override
    public String getPrimaryItemName() {
        final String primaryItem = this.attributes.getPrimaryItem().getPrimaryItem();

        // API states to return null
        if (Utils.isEmpty(primaryItem)) {
            return null;
        }

        return primaryItem;
    }

    private String getPropertyDefinitionDelimiter() {
        return CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.PROPERTY_DEFINITION_DELIMITER);
    }

    /**
     * @return the property definitions (never <code>null</code>)
     */
    public List<PropertyDefinition> getPropertyDefinitions() {
        if (this.propertyDefinitions == null) {
            return Collections.emptyList();
        }

        return this.propertyDefinitions;
    }

    private String getPropertyDefinitionStartDelimiter() {
        return CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.PROPERTY_DEFINITION_START_DELIMITER);
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.nodetype.NodeTypeTemplate#getPropertyDefinitionTemplates()
     */
    @Override
    public List getPropertyDefinitionTemplates() {
        return new ArrayList<PropertyDefinitionTemplate>(getPropertyDefinitions());
    }

    private String getStartChildNodeDefinitionDelimiter() {
        return CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.CHILD_NODE_DEFINITION_START_DELIMITER);
    }

    /**
     * Can be used to find the attribute state of the abstract, mixin, orderable, primary item, queryable, and super types
     * properties.
     * 
     * @param propertyName the property whose attribute state is being requested (cannot be <code>null</code>)
     * @return the attribute state (never <code>null</code>)
     * @throws IllegalArgumentException if a property that does not have an attribute state is specified
     */
    public Value getState( final PropertyName propertyName ) {
        Utils.verifyIsNotNull(propertyName, "propertyName"); //$NON-NLS-1$

        if (PropertyName.ABSTRACT == propertyName) {
            return this.attributes.getAbstract().get();
        }

        if (PropertyName.MIXIN == propertyName) {
            return this.attributes.getMixin().get();
        }

        if (PropertyName.ORDERABLE == propertyName) {
            return this.attributes.getOrderable().get();
        }

        if (PropertyName.PRIMARY_ITEM == propertyName) {
            return this.attributes.getPrimaryItem().get();
        }

        if (PropertyName.QUERYABLE == propertyName) {
            return this.attributes.getQueryable().get();
        }

        if (PropertyName.SUPERTYPES == propertyName) {
            return this.superTypes.get();
        }

        throw new IllegalArgumentException(NLS.bind(Messages.invalidGetStateRequest, propertyName));
    }

    /**
     * @return the qualified names of the supertypes (never <code>null</code> but can be empty)
     */
    public List<QualifiedName> getSupertypes() {
        return this.superTypes.getSupportedItems();
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.nodetype.NodeTypeDefinition#hasOrderableChildNodes()
     */
    @Override
    public boolean hasOrderableChildNodes() {
        return this.attributes.getOrderable().is();
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.nodetype.NodeTypeDefinition#isAbstract()
     */
    @Override
    public boolean isAbstract() {
        return this.attributes.getAbstract().is();
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.nodetype.NodeTypeDefinition#isMixin()
     */
    @Override
    public boolean isMixin() {
        return this.attributes.getMixin().is();
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.nodetype.NodeTypeDefinition#isQueryable()
     */
    @Override
    public boolean isQueryable() {
        return this.attributes.getQueryable().is();
    }

    /**
     * @param propertyName the property being checked (cannot be <code>null</code>)
     * @return <code>true</code> if property is a variant
     */
    public boolean isVariant( final PropertyName propertyName ) {
        Utils.verifyIsNotNull(propertyName, "propertyName"); //$NON-NLS-1$
        return (getState(propertyName) == Value.VARIANT);
    }

    /**
     * @param property the property that was changed (never <code>null</code>)
     * @param oldValue the old value (can be <code>null</code>)
     * @param newValue the new value (can be <code>null</code>)
     */
    private void notifyChangeListeners( final PropertyName property,
                                        final Object oldValue,
                                        final Object newValue ) {
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
     * If removed, a property change event is broadcast to all registered listeners.
     * 
     * @param childNodeDefinitionBeingRemoved the child node definition being removed (cannot be <code>null</code>)
     * @return <code>true</code> if successfully removed
     */
    public boolean removeChildNodeDefinition( final ChildNodeDefinition childNodeDefinitionBeingRemoved ) {
        Utils.verifyIsNotNull(childNodeDefinitionBeingRemoved, "childNodeDefinitionBeingRemoved"); //$NON-NLS-1$

        if (this.childNodesDefinitions == null) {
            return false;
        }

        if (this.childNodesDefinitions.remove(childNodeDefinitionBeingRemoved)) {
            notifyChangeListeners(PropertyName.CHILD_NODES, childNodeDefinitionBeingRemoved, null);

            if (this.childNodesDefinitions.isEmpty()) {
                this.childNodesDefinitions = null;
            }

            return true; // removed
        }

        return false; // not removed
    }

    /**
     * @param listener the listener who no longer will receive property change events (cannot be <code>null</code>)
     * @return <code>true</code> if successfully removed
     */
    public boolean removeListener( final PropertyChangeListener listener ) {
        Utils.verifyIsNotNull(listener, "listener"); //$NON-NLS-1$
        return this.listeners.remove(listener);
    }

    /**
     * If removed, a property change event is broadcast to all registered listeners.
     * 
     * @param propertyDefinitionBeingRemoved the property definition being removed (cannot be <code>null</code>)
     * @return <code>true</code> if successfully removed
     */
    public boolean removePropertyDefinition( final PropertyDefinition propertyDefinitionBeingRemoved ) {
        Utils.verifyIsNotNull(propertyDefinitionBeingRemoved, "propertyDefinitionBeingRemoved"); //$NON-NLS-1$

        if (this.propertyDefinitions == null) {
            return false;
        }

        if (this.propertyDefinitions.remove(propertyDefinitionBeingRemoved)) {
            notifyChangeListeners(PropertyName.PROPERTY_DEFINITIONS, propertyDefinitionBeingRemoved, null);

            if (this.propertyDefinitions.isEmpty()) {
                this.propertyDefinitions = null;
            }

            return true; // removed
        }

        return false; // not removed
    }

    /**
     * If removed, a property change event is broadcast to all registered listeners.
     * 
     * @param superTypeBeingRemoved the super type name being removed (cannot be <code>null</code>)
     * @return <code>true</code> if successfully removed
     */
    public boolean removeSuperType( final String superTypeBeingRemoved ) {
        if (this.superTypes.remove(QualifiedName.parse(superTypeBeingRemoved))) {
            notifyChangeListeners(PropertyName.SUPERTYPES, superTypeBeingRemoved, null);
            return true; // removed
        }

        return false; // not removed
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.nodetype.NodeTypeTemplate#setAbstract(boolean)
     */
    @Override
    public void setAbstract( final boolean newAbstract ) {
        final AttributeState.Value newState = (newAbstract ? AttributeState.Value.IS : AttributeState.Value.IS_NOT);
        changeState(PropertyName.ABSTRACT, newState);
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.nodetype.NodeTypeTemplate#setDeclaredSuperTypeNames(java.lang.String[])
     */
    @Override
    public void setDeclaredSuperTypeNames( final String[] newSuperTypes ) {
        final List<QualifiedName> oldValue = this.superTypes.getSupportedItems();
        boolean changed = this.superTypes.clear();

        if (!Utils.isEmpty(newSuperTypes)) {
            for (final String superType : newSuperTypes) {
                if (this.superTypes.add(QualifiedName.parse(superType))) {
                    changed = true;
                }
            }
        }

        if (changed) {
            notifyChangeListeners(PropertyName.SUPERTYPES, oldValue, this.superTypes.getSupportedItems());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.nodetype.NodeTypeTemplate#setMixin(boolean)
     */
    @Override
    public void setMixin( final boolean newMixin ) {
        final AttributeState.Value newState = (newMixin ? AttributeState.Value.IS : AttributeState.Value.IS_NOT);
        changeState(PropertyName.MIXIN, newState);
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.nodetype.NodeTypeTemplate#setName(java.lang.String)
     */
    @Override
    public void setName( final String newName ) {
        final Object oldValue = this.name.get();

        if (this.name.set(newName)) {
            notifyChangeListeners(PropertyName.NAME, oldValue, newName);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.nodetype.NodeTypeTemplate#setOrderableChildNodes(boolean)
     */
    @Override
    public void setOrderableChildNodes( final boolean newOrderable ) {
        final AttributeState.Value newState = (newOrderable ? AttributeState.Value.IS : AttributeState.Value.IS_NOT);
        changeState(PropertyName.ORDERABLE, newState);
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.nodetype.NodeTypeTemplate#setPrimaryItemName(java.lang.String)
     */
    @Override
    public void setPrimaryItemName( final String newPrimaryItem ) {
        final String oldName = this.attributes.getPrimaryItem().getPrimaryItem();

        if (this.attributes.getPrimaryItem().setPrimaryItem(newPrimaryItem)) {
            notifyChangeListeners(PropertyName.PRIMARY_ITEM, oldName, newPrimaryItem);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.nodetype.NodeTypeTemplate#setQueryable(boolean)
     */
    @Override
    public void setQueryable( final boolean newQueryable ) {
        final AttributeState.Value newState = (newQueryable ? AttributeState.Value.IS : AttributeState.Value.IS_NOT);
        changeState(PropertyName.QUERYABLE, newState);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.jcr.cnd.CndElement#toCndNotation(org.jboss.tools.modeshape.jcr.cnd.CndElement.NotationType)
     */
    @Override
    public String toCndNotation( final NotationType notationType ) {
        final StringBuilder builder = new StringBuilder();

        { // name
            builder.append(NAME_NOTATION_PREFIX)
                   .append(this.name.toCndNotation(notationType))
                   .append(NAME_NOTATION_SUFFIX)
                   .append(getEndNameDelimiter());
        }

        { // super types
            final String notation = this.superTypes.toCndNotation(notationType);

            if (!Utils.isEmpty(notation)) {
                builder.append(notation).append(getEndSuperTypesDelimiter());
            }
        }

        { // attributes
            final String notation = this.attributes.toCndNotation(notationType);

            if (!Utils.isEmpty(notation)) {
                builder.append(notation);
            }

            builder.append(getEndAttributesDelimiter());
        }

        { // property definitions
            if (!Utils.isEmpty(this.propertyDefinitions)) {
                for (final PropertyDefinition propDefn : this.propertyDefinitions) {
                    builder.append(getPropertyDefinitionStartDelimiter());
                    builder.append(propDefn.toCndNotation(notationType));
                    builder.append(getPropertyDefinitionDelimiter());
                }

                builder.append(getEndPropertyDefinitionsDelimiter());
            }
        }

        { // child node definitions
            if (!Utils.isEmpty(this.childNodesDefinitions)) {
                for (final ChildNodeDefinition childNodeDefn : this.childNodesDefinitions) {
                    builder.append(getStartChildNodeDefinitionDelimiter());
                    builder.append(childNodeDefn.toCndNotation(notationType));
                    builder.append(getChildNodeDefinitionDelimiter());
                }

                builder.append(getEndChildNodeDefinitionsDelimiter());
            }
        }

        return builder.toString();
    }

    /**
     * The property names whose <code>toString()</code> is used in {@link PropertyChangeEvent}s.
     */
    public enum PropertyName {

        /**
         * The abstract attribute.
         */
        ABSTRACT,

        /**
         * The collection for child node definitions.
         */
        CHILD_NODES,

        /**
         * The mixin attribute.
         */
        MIXIN,

        /**
         * The node type name.
         */
        NAME,

        /**
         * The orderable attribute.
         */
        ORDERABLE,

        /**
         * The primary item attribute.
         */
        PRIMARY_ITEM,

        /**
         * The collection of property definitions.
         */
        PROPERTY_DEFINITIONS,

        /**
         * The queryable attribute.
         */
        QUERYABLE,

        /**
         * The collection for super types.
         */
        SUPERTYPES
    }

}
