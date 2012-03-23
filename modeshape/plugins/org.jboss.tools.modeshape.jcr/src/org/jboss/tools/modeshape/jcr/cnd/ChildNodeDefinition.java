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
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.jcr.nodetype.NodeDefinitionTemplate;
import javax.jcr.nodetype.NodeType;

import org.eclipse.osgi.util.NLS;
import org.jboss.tools.modeshape.jcr.Messages;
import org.jboss.tools.modeshape.jcr.Utils;
import org.jboss.tools.modeshape.jcr.cnd.CndNotationPreferences.Preference;
import org.jboss.tools.modeshape.jcr.cnd.attributes.AttributeState.Value;
import org.jboss.tools.modeshape.jcr.cnd.attributes.DefaultType;
import org.jboss.tools.modeshape.jcr.cnd.attributes.NodeAttributes;
import org.jboss.tools.modeshape.jcr.cnd.attributes.OnParentVersion;
import org.jboss.tools.modeshape.jcr.cnd.attributes.RequiredTypes;

/**
 * 
 */
public class ChildNodeDefinition implements CndElement, Comparable, NodeDefinitionTemplate {

    /**
     * The prefix used in CND notation before the property definition.
     */
    public static final String NOTATION_PREFIX = "+"; //$NON-NLS-1$

    /**
     * @param childNodeBeingCopied the child node definition being copied (cannot be <code>null</code>)
     * @return the copy (never <code>null</code>)
     */
    public static ChildNodeDefinition copy( final ChildNodeDefinition childNodeBeingCopied ) {
        final ChildNodeDefinition copy = new ChildNodeDefinition();

        // name
        copy.setName(childNodeBeingCopied.getName());

        // attributes
        copy.attributes.getAutocreated().set(childNodeBeingCopied.attributes.getAutocreated().get());
        copy.attributes.getMandatory().set(childNodeBeingCopied.attributes.getMandatory().get());
        copy.attributes.getProtected().set(childNodeBeingCopied.attributes.getProtected().get());
        copy.attributes.getSameNameSiblings().set(childNodeBeingCopied.attributes.getSameNameSiblings().get());
        copy.attributes.setOnParentVersion(childNodeBeingCopied.attributes.getOnParentVersion());

        // default type
        copy.defaultType.set(childNodeBeingCopied.defaultType.get());
        copy.defaultType.setDefaultType(childNodeBeingCopied.getDefaultPrimaryTypeName());

        // required types
        copy.requiredTypes.set(childNodeBeingCopied.requiredTypes.get());

        for (final String requiredTypeName : childNodeBeingCopied.getRequiredPrimaryTypeNames()) {
            copy.addRequiredType(requiredTypeName);
        }

        return copy;
    }

    /**
     * The node attributes (never <code>null</code>).
     */
    private final NodeAttributes attributes;

    /**
     * The node default type (never <code>null</code>).
     */
    private final DefaultType defaultType;

    /**
     * The registered property change listeners (never <code>null</code>).
     */
    private final CopyOnWriteArrayList<PropertyChangeListener> listeners;

    /**
     * The node identifier (can be <code>null</code> or empty).
     */
    private final QualifiedName name;

    /**
     * The node required types (never <code>null</code>).
     */
    private final RequiredTypes requiredTypes;

    /**
     * Constructs an instance set to all defaults.
     */
    public ChildNodeDefinition() {
        this.attributes = new NodeAttributes();
        this.name = new QualifiedName();
        this.defaultType = new DefaultType();
        this.requiredTypes = new RequiredTypes();
        this.listeners = new CopyOnWriteArrayList<PropertyChangeListener>();
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
     * If added, broadcasts a {@link PropertyChangeEvent} with an old value of <code>null</code> and a new value equal to
     * <code>requiredTypeBeingAdded</code>.
     * 
     * @param requiredTypeBeingAdded the required type being added (cannot be <code>null</code>)
     * @return <code>true</code> if added
     */
    public boolean addRequiredType( final String requiredTypeBeingAdded ) {
        if (this.requiredTypes.add(QualifiedName.parse(requiredTypeBeingAdded))) {
            notifyChangeListeners(PropertyName.REQUIRED_TYPES, null, requiredTypeBeingAdded);
            return true; // added
        }

        return false; // not added
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.nodetype.NodeDefinition#allowsSameNameSiblings()
     */
    @Override
    public boolean allowsSameNameSiblings() {
        return this.attributes.getSameNameSiblings().is();
    }

    /**
     * If state was changed, a property change event is broadcast to all registered listeners. Can be used to change the
     * autocreated, default type, mandatory, on parent version variant, protected, required types, and same name siblings
     * properties.
     * 
     * @param propertyName the property whose attribute state is being changed (cannot be <code>null</code>)
     * @param newState the new attribute state (cannot be <code>null</code>)
     * @return <code>true</code> if the attribute state was changed
     */
    public boolean changeState( final PropertyName propertyName,
                                final Value newState ) {
        Utils.verifyIsNotNull(propertyName, "propertyName"); //$NON-NLS-1$
        Utils.verifyIsNotNull(newState, "newState"); //$NON-NLS-1$

        Object oldValue = null;
        Object newValue = newState;
        boolean changed = false;

        if (PropertyName.AUTOCREATED == propertyName) {
            oldValue = this.attributes.getAutocreated().get();
            changed = this.attributes.getAutocreated().set(newState);
        } else if (PropertyName.DEFAULT_TYPE == propertyName) {
            oldValue = this.defaultType.get();
            changed = this.defaultType.set(newState);
        } else if (PropertyName.MANDATORY == propertyName) {
            oldValue = this.attributes.getMandatory().get();
            changed = this.attributes.getMandatory().set(newState);
        } else if (PropertyName.ON_PARENT_VERSION == propertyName) {
            if (Value.VARIANT == newState) {
                oldValue = this.attributes.getOnParentVersion();
                newValue = OnParentVersion.VARIANT;
                changed = this.attributes.setOnParentVersion(OnParentVersion.VARIANT);
            }
        } else if (PropertyName.PROTECTED == propertyName) {
            oldValue = this.attributes.getProtected().get();
            changed = this.attributes.getProtected().set(newState);
        } else if (PropertyName.REQUIRED_TYPES == propertyName) {
            oldValue = this.requiredTypes.get();
            changed = this.requiredTypes.set(newState);
        } else if (PropertyName.SAME_NAME_SIBLINGS == propertyName) {
            oldValue = this.attributes.getSameNameSiblings().get();
            changed = this.attributes.getSameNameSiblings().set(newState);
        }

        if (changed) {
            notifyChangeListeners(propertyName, oldValue, newValue);
            return true; // changed state
        }

        return false; // did not change state
    }

    /**
     * If at least one required type was removed, broadcasts a {@link PropertyChangeEvent} with an old value equal to the old
     * required types collection and a new value of <code>null</code>.
     * 
     * @return <code>true</code> if at least one required type was removed
     */
    public boolean clearRequiredTypes() {
        final String[] oldValue = this.requiredTypes.toArray();

        if (this.requiredTypes.clear()) {
            notifyChangeListeners(PropertyName.REQUIRED_TYPES, oldValue, null);
            return true; // cleared
        }

        return false; // nothing to clear
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo( final Object object ) {
        final ChildNodeDefinition that = (ChildNodeDefinition)object;
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

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( final Object obj ) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || !getClass().equals(obj.getClass())) {
            return false;
        }

        final ChildNodeDefinition that = (ChildNodeDefinition)obj;

        if (!this.attributes.equals(that.attributes)) {
            return false;
        }

        if (!Utils.equals(getName(), that.getName())) {
            return false;
        }

        if (!Utils.equals(getDefaultPrimaryTypeName(), that.getDefaultPrimaryTypeName())) {
            return false;
        }

        return this.requiredTypes.equals(that.requiredTypes);
    }

    /**
     * @param notationType the notation type being requested (cannot be <code>null</code>)
     * @return the CND notation (never <code>null</code>)
     */
    public String getAttributesCndNotation( final NotationType notationType ) {
        final String cndNotation = this.attributes.toCndNotation(notationType);

        if (cndNotation == null) {
            return Utils.EMPTY_STRING;
        }

        return cndNotation;
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.nodetype.ItemDefinition#getDeclaringNodeType()
     * @throws UnsupportedOperationException if method is called
     */
    @Override
    public NodeType getDeclaringNodeType() {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.nodetype.NodeDefinition#getDefaultPrimaryType()
     * @throws UnsupportedOperationException if method is called
     */
    @Override
    public NodeType getDefaultPrimaryType() {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.nodetype.NodeDefinition#getDefaultPrimaryTypeName()
     */
    @Override
    public String getDefaultPrimaryTypeName() {
        final String primaryType = this.defaultType.getDefaultTypeName();

        // per API should return null if empty
        if (Utils.isEmpty(primaryType)) {
            return null;
        }

        return primaryType;
    }

    /**
     * @return defaultType the default type (never <code>null</code>)
     */
    public DefaultType getDefaultType() {
        return this.defaultType;
    }

    private String getFormatDelimiter() {
        return CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.CHILD_NODE_PROPERTY_DELIMITER);
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.nodetype.ItemDefinition#getName()
     */
    @Override
    public String getName() {
        // API requires null to be returned
        if (Utils.isEmpty(this.name.get())) {
            return null;
        }

        return this.name.get();
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.nodetype.ItemDefinition#getOnParentVersion()
     */
    @Override
    public int getOnParentVersion() {
        return this.attributes.getOnParentVersion().asJcrValue();
    }

    private String getPrefixEndDelimiter() {
        return CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.CHILD_NODE_DEFINITION_END_PREFIX_DELIMITER);
    }

    /**
     * @return the qualified name (never <code>null</code>)
     */
    public QualifiedName getQualifiedName() {
        return this.name;
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.nodetype.NodeDefinition#getRequiredPrimaryTypeNames()
     */
    @Override
    public String[] getRequiredPrimaryTypeNames() {
        return this.requiredTypes.toArray();
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.nodetype.NodeDefinition#getRequiredPrimaryTypes()
     * @throws UnsupportedOperationException if method is called
     */
    @Override
    public NodeType[] getRequiredPrimaryTypes() {
        throw new UnsupportedOperationException();
    }

    /**
     * @return the list of required types (never <code>null</code> but can be empty)
     */
    public List<QualifiedName> getRequiredTypes() {
        return this.requiredTypes.getSupportedItems();
    }

    /**
     * @param notationType the notation type being requested (cannot be <code>null</code>)
     * @return the CND notation (never <code>null</code>)
     */
    public String getRequiredTypesCndNotation( final NotationType notationType ) {
        final String cndNotation = this.requiredTypes.toCndNotation(notationType);

        if (cndNotation == null) {
            return Utils.EMPTY_STRING;
        }

        return cndNotation;
    }

    /**
     * Can be used to find the attribute state of the autocreated, default type, mandatory, protected, required types, same name
     * siblings, and on parent version properties.
     * 
     * @param propertyName the property whose attribute state is being requested (cannot be <code>null</code>)
     * @return the attribute state (never <code>null</code>)
     * @throws IllegalArgumentException if a property that does not have an attribute state is specified
     */
    public Value getState( final PropertyName propertyName ) {
        Utils.verifyIsNotNull(propertyName, "propertyName"); //$NON-NLS-1$

        if (PropertyName.AUTOCREATED == propertyName) {
            return this.attributes.getAutocreated().get();
        }

        if (PropertyName.DEFAULT_TYPE == propertyName) {
            return this.defaultType.get();
        }

        if (PropertyName.MANDATORY == propertyName) {
            return this.attributes.getMandatory().get();
        }

        if (PropertyName.PROTECTED == propertyName) {
            return this.attributes.getProtected().get();
        }

        if (PropertyName.REQUIRED_TYPES == propertyName) {
            return this.requiredTypes.get();
        }

        if (PropertyName.SAME_NAME_SIBLINGS == propertyName) {
            return this.attributes.getSameNameSiblings().get();
        }

        if (PropertyName.ON_PARENT_VERSION == propertyName) {
            if (isVariant(PropertyName.ON_PARENT_VERSION)) {
                return Value.VARIANT;
            }

            return Value.IS;
        }

        throw new IllegalArgumentException(NLS.bind(Messages.invalidGetStateRequest, propertyName));
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Utils.hashCode(this.attributes, this.name, this.defaultType, this.requiredTypes);
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.nodetype.ItemDefinition#isAutoCreated()
     */
    @Override
    public boolean isAutoCreated() {
        return this.attributes.getAutocreated().is();
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.nodetype.ItemDefinition#isMandatory()
     */
    @Override
    public boolean isMandatory() {
        return this.attributes.getMandatory().is();
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.nodetype.ItemDefinition#isProtected()
     */
    @Override
    public boolean isProtected() {
        return this.attributes.getProtected().is();
    }

    /**
     * @param propertyName the property being checked (cannot be <code>null</code>)
     * @return <code>true</code> if property is a variant
     */
    public boolean isVariant( final PropertyName propertyName ) {
        Utils.verifyIsNotNull(propertyName, "propertyName"); //$NON-NLS-1$

        if (PropertyName.ON_PARENT_VERSION == propertyName) {
            return (this.attributes.getOnParentVersion() == OnParentVersion.VARIANT);
        }

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
     * @param listener the listener who no longer will receive property change events (cannot be <code>null</code>)
     * @return <code>true</code> if successfully removed
     */
    public boolean removeListener( final PropertyChangeListener listener ) {
        Utils.verifyIsNotNull(listener, "listener"); //$NON-NLS-1$
        return this.listeners.remove(listener);
    }

    /**
     * If required type is removed, broadcasts a {@link PropertyChangeEvent} with an old value of
     * <code>requiredTypeBeingRemoved</code> and a new value of <code>null</code>.
     * 
     * @param requiredTypeBeingRemoved the required type being removed (cannot be <code>null</code>)
     * @return <code>true</code> if removed
     */
    public boolean removeRequiredType( final String requiredTypeBeingRemoved ) {
        final QualifiedName qname = QualifiedName.parse(requiredTypeBeingRemoved);

        if (this.requiredTypes.remove(qname)) {
            notifyChangeListeners(PropertyName.REQUIRED_TYPES, requiredTypeBeingRemoved, null);
            return true; // removed
        }

        return false; // not removed
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.nodetype.NodeDefinitionTemplate#setAutoCreated(boolean)
     */
    @Override
    public void setAutoCreated( final boolean newAutocreated ) {
        final Value newState = (newAutocreated ? Value.IS : Value.IS_NOT);
        changeState(PropertyName.AUTOCREATED, newState);
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.nodetype.NodeDefinitionTemplate#setDefaultPrimaryTypeName(java.lang.String)
     */
    @Override
    public void setDefaultPrimaryTypeName( final String newTypeName ) {
        final String oldValue = this.defaultType.getDefaultTypeName();

        if (this.defaultType.setDefaultType(newTypeName)) {
            notifyChangeListeners(PropertyName.DEFAULT_TYPE, oldValue, newTypeName);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.nodetype.NodeDefinitionTemplate#setMandatory(boolean)
     */
    @Override
    public void setMandatory( final boolean newMandatory ) {
        final Value newState = (newMandatory ? Value.IS : Value.IS_NOT);
        changeState(PropertyName.MANDATORY, newState);
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.nodetype.NodeDefinitionTemplate#setName(java.lang.String)
     */
    @Override
    public void setName( final String newName ) {
        final Object oldValue = getName();

        if (this.name.set(newName)) {
            notifyChangeListeners(PropertyName.NAME, oldValue, newName);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.nodetype.NodeDefinitionTemplate#setOnParentVersion(int)
     */
    @Override
    public void setOnParentVersion( final int newOpv ) {
        final OnParentVersion oldValue = this.attributes.getOnParentVersion();

        if (this.attributes.setOnParentVersion(OnParentVersion.findUsingJcrValue(newOpv))) {
            notifyChangeListeners(PropertyName.ON_PARENT_VERSION, oldValue, newOpv);
        }
    }

    /**
     * If changed, a property change event is broadcast to all registered listeners.
     * 
     * @param newOpv the new OPV value (cannot be <code>null</code>)
     * @return <code>true</code> if successfully changed
     */
    public boolean setOnParentVersion( final String newOpv ) {
        final OnParentVersion oldValue = this.attributes.getOnParentVersion();

        if (this.attributes.setOnParentVersion(OnParentVersion.find(newOpv))) {
            notifyChangeListeners(PropertyName.ON_PARENT_VERSION, oldValue, newOpv);
            return true; // value changed
        }

        return false; // value not changed
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.nodetype.NodeDefinitionTemplate#setProtected(boolean)
     */
    @Override
    public void setProtected( final boolean newProtected ) {
        final Value newState = (newProtected ? Value.IS : Value.IS_NOT);
        changeState(PropertyName.PROTECTED, newState);
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.nodetype.NodeDefinitionTemplate#setRequiredPrimaryTypeNames(java.lang.String[])
     */
    @Override
    public void setRequiredPrimaryTypeNames( final String[] newTypeNames ) {
        final String[] items = this.requiredTypes.toArray();
        boolean changed = this.requiredTypes.clear();

        if (!Utils.isEmpty(newTypeNames)) {
            for (final String typeName : newTypeNames) {
                if (this.requiredTypes.add(QualifiedName.parse(typeName))) {
                    changed = true;
                }
            }
        }

        if (changed) {
            notifyChangeListeners(PropertyName.REQUIRED_TYPES, items, this.requiredTypes.getSupportedItems());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.nodetype.NodeDefinitionTemplate#setSameNameSiblings(boolean)
     */
    @Override
    public void setSameNameSiblings( final boolean newAllowSameNameSiblings ) {
        final Value newState = (newAllowSameNameSiblings ? Value.IS : Value.IS_NOT);
        changeState(PropertyName.SAME_NAME_SIBLINGS, newState);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.jcr.cnd.CndElement#toCndNotation(org.jboss.tools.modeshape.jcr.cnd.CndElement.NotationType)
     */
    @Override
    public String toCndNotation( final NotationType notationType ) {
        final StringBuilder builder = new StringBuilder(NOTATION_PREFIX);
        builder.append(getPrefixEndDelimiter());

        final String DELIM = getFormatDelimiter();

        builder.append(this.name.toCndNotation(notationType));
        Utils.build(builder, true, DELIM, this.requiredTypes.toCndNotation(notationType));
        Utils.build(builder, true, DELIM, this.defaultType.toCndNotation(notationType));
        Utils.build(builder, true, DELIM, this.attributes.toCndNotation(notationType));

        return builder.toString();
    }

    /**
     * The property names whose <code>toString()</code> is used in {@link PropertyChangeEvent}s.
     */
    public enum PropertyName {
        /**
         * The autocreated indicator.
         */
        AUTOCREATED,

        /**
         * The default type.
         */
        DEFAULT_TYPE,

        /**
         * The mandatory indicator.
         */
        MANDATORY,

        /**
         * The property name.
         */
        NAME,

        /**
         * The on parent version value.
         */
        ON_PARENT_VERSION,

        /**
         * The protected indicator.
         */
        PROTECTED,

        /**
         * The collection of required types.
         */
        REQUIRED_TYPES,

        /**
         * The supports same name siblings indicator.
         */
        SAME_NAME_SIBLINGS;

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
