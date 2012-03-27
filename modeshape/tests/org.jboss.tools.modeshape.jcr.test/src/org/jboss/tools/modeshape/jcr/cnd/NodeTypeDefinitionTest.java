/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.jboss.tools.modeshape.jcr.cnd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Collections;

import org.jboss.tools.modeshape.jcr.Listener;
import org.jboss.tools.modeshape.jcr.Utils;
import org.jboss.tools.modeshape.jcr.cnd.NodeTypeDefinition.PropertyName;
import org.jboss.tools.modeshape.jcr.cnd.attributes.AttributeState.Value;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 */
public class NodeTypeDefinitionTest {

    private ChildNodeDefinition childNodeDefinition;
    private NodeTypeDefinition nodeTypeDefinition;
    private PropertyDefinition propertyDefinition;

    @Before
    public void beforeEach() {
        this.childNodeDefinition = new ChildNodeDefinition();
        this.nodeTypeDefinition = new NodeTypeDefinition();
        this.propertyDefinition = new PropertyDefinition();
    }

    @Test
    public void shouldNotHaveChildNodeDefinitionsAfterConstruction() {
        assertEquals(0, this.nodeTypeDefinition.getChildNodeDefinitions().size());
    }

    @Test
    public void shouldNotHavePropertyDefinitionsAfterConstruction() {
        assertEquals(0, this.nodeTypeDefinition.getPropertyDefinitions().size());
    }

    @Test
    public void shouldNotHaveSuperTypesAfterConstruction() {
        assertEquals(0, this.nodeTypeDefinition.getDeclaredSupertypeNames().length);
    }

    @Test
    public void shouldNotHaveNameAfterConstruction() {
        assertTrue(Utils.isEmpty(this.nodeTypeDefinition.getName()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowNullChildNodeDefinitionToBeAdded() {
        this.nodeTypeDefinition.addChildNodeDefinition(null);
    }

    @Test
    public void shouldAddChildNodeDefinition() {
        assertTrue(this.nodeTypeDefinition.addChildNodeDefinition(this.childNodeDefinition));
        assertEquals(this.childNodeDefinition, this.nodeTypeDefinition.getChildNodeDefinitions().iterator().next());
        assertEquals(this.childNodeDefinition, this.nodeTypeDefinition.getDeclaredChildNodeDefinitions()[0]);
    }

    @Test
    public void shouldReceiveEventAfterAddingChildNodeDefinition() {
        Listener l = new Listener();
        assertTrue(this.nodeTypeDefinition.addListener(l));

        assertTrue(this.nodeTypeDefinition.addChildNodeDefinition(this.childNodeDefinition));

        assertEquals(1, l.getCount());
        assertEquals(PropertyName.CHILD_NODES.toString(), l.getPropertyName());
        assertEquals(this.childNodeDefinition, l.getNewValue());
        assertNull(l.getOldValue());
    }

    @Test
    public void shouldNotReceiveEventAfterUnregistering() {
        Listener l = new Listener();
        assertTrue(this.nodeTypeDefinition.addListener(l));
        assertTrue(this.nodeTypeDefinition.removeListener(l));

        assertTrue(this.nodeTypeDefinition.addChildNodeDefinition(this.childNodeDefinition));
        assertEquals(0, l.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowNullPropertyDefinitionToBeAdded() {
        this.nodeTypeDefinition.addPropertyDefinition(null);
    }

    @Test
    public void shouldAddPropertyDefinition() {
        assertTrue(this.nodeTypeDefinition.addPropertyDefinition(this.propertyDefinition));
        assertEquals(this.propertyDefinition, this.nodeTypeDefinition.getPropertyDefinitions().iterator().next());
        assertEquals(this.propertyDefinition, this.nodeTypeDefinition.getDeclaredPropertyDefinitions()[0]);
    }

    @Test
    public void shouldReceiveEventAfterAddingPropertyDefinition() {
        Listener l = new Listener();
        assertTrue(this.nodeTypeDefinition.addListener(l));

        assertTrue(this.nodeTypeDefinition.addPropertyDefinition(this.propertyDefinition));

        assertEquals(1, l.getCount());
        assertEquals(PropertyName.PROPERTY_DEFINITIONS.toString(), l.getPropertyName());
        assertEquals(this.propertyDefinition, l.getNewValue());
        assertNull(l.getOldValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowNullSuperTypeToBeAdded() {
        this.nodeTypeDefinition.addSuperType(null);
    }

    @Test
    public void shouldAddSuperType() {
        final String SUPER_TYPE = "superType"; //$NON-NLS-1$
        assertTrue(this.nodeTypeDefinition.addSuperType(SUPER_TYPE));
        assertEquals(SUPER_TYPE, this.nodeTypeDefinition.getDeclaredSupertypeNames()[0]);
    }

    @Test
    public void shouldNotAllowDuplicateSuperType() {
        final String SUPER_TYPE = "superType"; //$NON-NLS-1$
        assertTrue(this.nodeTypeDefinition.addSuperType(SUPER_TYPE));
        assertFalse(this.nodeTypeDefinition.addSuperType(SUPER_TYPE));
    }

    @Test
    public void shouldReceiveEventAfterAddingSuperType() {
        Listener l = new Listener();
        assertTrue(this.nodeTypeDefinition.addListener(l));

        final String SUPER_TYPE = "superType"; //$NON-NLS-1$
        assertTrue(this.nodeTypeDefinition.addSuperType(SUPER_TYPE));

        assertEquals(1, l.getCount());
        assertEquals(PropertyName.SUPERTYPES.toString(), l.getPropertyName());
        assertEquals(SUPER_TYPE, l.getNewValue());
        assertNull(l.getOldValue());
    }

    @Test
    public void shouldChangeAbstractPropertyState() {
        assertTrue(this.nodeTypeDefinition.changeState(PropertyName.ABSTRACT, Value.IS));
        assertEquals(this.nodeTypeDefinition.getState(PropertyName.ABSTRACT), Value.IS);
        assertTrue(this.nodeTypeDefinition.isAbstract());

        assertTrue(this.nodeTypeDefinition.changeState(PropertyName.ABSTRACT, Value.VARIANT));
        assertEquals(this.nodeTypeDefinition.getState(PropertyName.ABSTRACT), Value.VARIANT);
        assertTrue(this.nodeTypeDefinition.isVariant(PropertyName.ABSTRACT));

        assertTrue(this.nodeTypeDefinition.changeState(PropertyName.ABSTRACT, Value.IS_NOT));
        assertEquals(this.nodeTypeDefinition.getState(PropertyName.ABSTRACT), Value.IS_NOT);
        assertFalse(this.nodeTypeDefinition.isAbstract());
    }

    @Test
    public void shouldChangeMixinPropertyState() {
        assertTrue(this.nodeTypeDefinition.changeState(PropertyName.MIXIN, Value.IS));
        assertEquals(this.nodeTypeDefinition.getState(PropertyName.MIXIN), Value.IS);
        assertTrue(this.nodeTypeDefinition.isMixin());

        assertTrue(this.nodeTypeDefinition.changeState(PropertyName.MIXIN, Value.VARIANT));
        assertEquals(this.nodeTypeDefinition.getState(PropertyName.MIXIN), Value.VARIANT);
        assertTrue(this.nodeTypeDefinition.isVariant(PropertyName.MIXIN));

        assertTrue(this.nodeTypeDefinition.changeState(PropertyName.MIXIN, Value.IS_NOT));
        assertEquals(this.nodeTypeDefinition.getState(PropertyName.MIXIN), Value.IS_NOT);
        assertFalse(this.nodeTypeDefinition.isMixin());
    }

    @Test
    public void shouldChangePrimaryItemPropertyStateToVariantOnly() {
        assertFalse(this.nodeTypeDefinition.changeState(PropertyName.PRIMARY_ITEM, Value.IS));

        assertTrue(this.nodeTypeDefinition.changeState(PropertyName.PRIMARY_ITEM, Value.VARIANT));
        assertEquals(this.nodeTypeDefinition.getState(PropertyName.PRIMARY_ITEM), Value.VARIANT);
        assertTrue(this.nodeTypeDefinition.isVariant(PropertyName.PRIMARY_ITEM));

        assertFalse(this.nodeTypeDefinition.changeState(PropertyName.PRIMARY_ITEM, Value.IS_NOT));
    }

    @Test
    public void shouldChangeQueryablePropertyState() {
        assertTrue(this.nodeTypeDefinition.changeState(PropertyName.QUERYABLE, Value.IS));
        assertEquals(this.nodeTypeDefinition.getState(PropertyName.QUERYABLE), Value.IS);
        assertTrue(this.nodeTypeDefinition.isQueryable());

        assertTrue(this.nodeTypeDefinition.changeState(PropertyName.QUERYABLE, Value.VARIANT));
        assertEquals(this.nodeTypeDefinition.getState(PropertyName.QUERYABLE), Value.VARIANT);
        assertTrue(this.nodeTypeDefinition.isVariant(PropertyName.QUERYABLE));

        assertTrue(this.nodeTypeDefinition.changeState(PropertyName.QUERYABLE, Value.IS_NOT));
        assertEquals(this.nodeTypeDefinition.getState(PropertyName.QUERYABLE), Value.IS_NOT);
        assertFalse(this.nodeTypeDefinition.isQueryable());
    }

    @Test
    public void shouldChangeSuperTypesPropertyStateToVariantOnly() {
        assertFalse(this.nodeTypeDefinition.changeState(PropertyName.SUPERTYPES, Value.IS));

        assertTrue(this.nodeTypeDefinition.changeState(PropertyName.SUPERTYPES, Value.VARIANT));
        assertEquals(this.nodeTypeDefinition.getState(PropertyName.SUPERTYPES), Value.VARIANT);
        assertTrue(this.nodeTypeDefinition.isVariant(PropertyName.SUPERTYPES));

        assertFalse(this.nodeTypeDefinition.changeState(PropertyName.SUPERTYPES, Value.IS_NOT));
    }

    @Test
    public void shouldClearChildNodeDefinitions() {
        assertTrue(this.nodeTypeDefinition.addChildNodeDefinition(this.childNodeDefinition));
        assertTrue(this.nodeTypeDefinition.clearChildNodeDefinitions());
        assertEquals(0, this.nodeTypeDefinition.getChildNodeDefinitions().size());
        assertEquals(0, this.nodeTypeDefinition.getDeclaredChildNodeDefinitions().length);
    }

    @Test
    public void shouldNotClearChildNodeDefinitionsWhenEmpty() {
        assertFalse(this.nodeTypeDefinition.clearChildNodeDefinitions());
    }

    @Test
    public void shouldReceiveEventAfterClearingChildNodeDefinitions() {
        assertTrue(this.nodeTypeDefinition.addChildNodeDefinition(this.childNodeDefinition));
        Collection<ChildNodeDefinition> oldValue = Collections.singletonList(this.childNodeDefinition);

        Listener l = new Listener();
        assertTrue(this.nodeTypeDefinition.addListener(l));

        assertTrue(this.nodeTypeDefinition.clearChildNodeDefinitions());
        assertEquals(1, l.getCount());
        assertEquals(PropertyName.CHILD_NODES.toString(), l.getPropertyName());
        assertNull(l.getNewValue());
        assertEquals(oldValue, l.getOldValue());
    }

    @Test
    public void shouldClearPropertyDefinitions() {
        assertTrue(this.nodeTypeDefinition.addPropertyDefinition(this.propertyDefinition));
        assertTrue(this.nodeTypeDefinition.clearPropertyDefinitions());
        assertEquals(0, this.nodeTypeDefinition.getPropertyDefinitions().size());
        assertEquals(0, this.nodeTypeDefinition.getDeclaredPropertyDefinitions().length);
    }

    @Test
    public void shouldNotClearPropertyDefinitionsWhenEmpty() {
        assertFalse(this.nodeTypeDefinition.clearPropertyDefinitions());
    }

    @Test
    public void shouldReceiveEventAfterClearingPropertyDefinitions() {
        assertTrue(this.nodeTypeDefinition.addPropertyDefinition(this.propertyDefinition));
        Collection<PropertyDefinition> oldValue = Collections.singletonList(this.propertyDefinition);

        Listener l = new Listener();
        assertTrue(this.nodeTypeDefinition.addListener(l));

        assertTrue(this.nodeTypeDefinition.clearPropertyDefinitions());
        assertEquals(1, l.getCount());
        assertEquals(PropertyName.PROPERTY_DEFINITIONS.toString(), l.getPropertyName());
        assertNull(l.getNewValue());
        assertEquals(oldValue, l.getOldValue());
    }

    @Test
    public void shouldClearSuperTypes() {
        assertTrue(this.nodeTypeDefinition.addSuperType("superType")); //$NON-NLS-1$
        assertTrue(this.nodeTypeDefinition.clearSuperTypes());
        assertEquals(0, this.nodeTypeDefinition.getDeclaredSupertypeNames().length);
    }

    @Test
    public void shouldNotClearSuperTypesWhenEmpty() {
        assertFalse(this.nodeTypeDefinition.clearSuperTypes());
    }

    @Test
    public void shouldReceiveEventAfterClearingSuperTypes() {
        final String SUPER_TYPE = "superType"; //$NON-NLS-1$
        assertTrue(this.nodeTypeDefinition.addSuperType(SUPER_TYPE));
        Collection<QualifiedName> oldValue = this.nodeTypeDefinition.getSupertypes();

        Listener l = new Listener();
        assertTrue(this.nodeTypeDefinition.addListener(l));

        assertTrue(this.nodeTypeDefinition.clearSuperTypes());
        assertEquals(1, l.getCount());
        assertEquals(PropertyName.SUPERTYPES.toString(), l.getPropertyName());
        assertNull(l.getNewValue());
        assertEquals(oldValue, l.getOldValue());
    }

    @Test
    public void shouldRemoveChildNodeDefinition() {
        assertTrue(this.nodeTypeDefinition.addChildNodeDefinition(this.childNodeDefinition));
        assertTrue(this.nodeTypeDefinition.removeChildNodeDefinition(this.childNodeDefinition));
        assertEquals(0, this.nodeTypeDefinition.getChildNodeDefinitions().size());
        assertEquals(0, this.nodeTypeDefinition.getDeclaredChildNodeDefinitions().length);
    }

    @Test
    public void shouldNotRemoveChildNodeDefinitionThatDoesNotExist() {
        assertFalse(this.nodeTypeDefinition.removeChildNodeDefinition(this.childNodeDefinition));
    }

    @Test
    public void shouldReceiveEventAfterRemovingChildNodeDefinition() {
        assertTrue(this.nodeTypeDefinition.addChildNodeDefinition(this.childNodeDefinition));

        Listener l = new Listener();
        assertTrue(this.nodeTypeDefinition.addListener(l));

        assertTrue(this.nodeTypeDefinition.removeChildNodeDefinition(this.childNodeDefinition));
        assertEquals(1, l.getCount());
        assertEquals(PropertyName.CHILD_NODES.toString(), l.getPropertyName());
        assertEquals(this.childNodeDefinition, l.getOldValue());
        assertNull(l.getNewValue());
    }

    @Test
    public void shouldRemovePropertyDefinition() {
        assertTrue(this.nodeTypeDefinition.addPropertyDefinition(this.propertyDefinition));
        assertTrue(this.nodeTypeDefinition.removePropertyDefinition(this.propertyDefinition));
        assertEquals(0, this.nodeTypeDefinition.getPropertyDefinitions().size());
        assertEquals(0, this.nodeTypeDefinition.getDeclaredPropertyDefinitions().length);
    }

    @Test
    public void shouldNotRemovePropertyDefinitionThatDoesNotExist() {
        assertFalse(this.nodeTypeDefinition.removePropertyDefinition(this.propertyDefinition));
    }

    @Test
    public void shouldReceiveEventAfterRemovingPropertyDefinition() {
        assertTrue(this.nodeTypeDefinition.addPropertyDefinition(this.propertyDefinition));

        Listener l = new Listener();
        assertTrue(this.nodeTypeDefinition.addListener(l));

        assertTrue(this.nodeTypeDefinition.removePropertyDefinition(this.propertyDefinition));
        assertEquals(1, l.getCount());
        assertEquals(PropertyName.PROPERTY_DEFINITIONS.toString(), l.getPropertyName());
        assertEquals(this.propertyDefinition, l.getOldValue());
        assertNull(l.getNewValue());
    }

    @Test
    public void shouldRemoveSuperType() {
        final String SUPER_TYPE = "superType"; //$NON-NLS-1$
        assertTrue(this.nodeTypeDefinition.addSuperType(SUPER_TYPE));
        assertTrue(this.nodeTypeDefinition.removeSuperType(SUPER_TYPE));
        assertEquals(0, this.nodeTypeDefinition.getDeclaredSupertypeNames().length);
    }

    @Test
    public void shouldNotRemoveSuperTypeThatDoesNotExist() {
        assertFalse(this.nodeTypeDefinition.removeSuperType("superType")); //$NON-NLS-1$
    }

    @Test
    public void shouldReceiveEventAfterRemovingSuperType() {
        final String SUPER_TYPE = "superType"; //$NON-NLS-1$
        assertTrue(this.nodeTypeDefinition.addSuperType(SUPER_TYPE));

        Listener l = new Listener();
        assertTrue(this.nodeTypeDefinition.addListener(l));

        assertTrue(this.nodeTypeDefinition.removeSuperType(SUPER_TYPE));
        assertEquals(1, l.getCount());
        assertEquals(PropertyName.SUPERTYPES.toString(), l.getPropertyName());
        assertEquals(SUPER_TYPE, l.getOldValue());
        assertNull(l.getNewValue());
    }

    @Test
    public void shouldSetAbstractProperty() {
        this.nodeTypeDefinition.setAbstract(true);
        assertTrue(this.nodeTypeDefinition.isAbstract());
        assertEquals(this.nodeTypeDefinition.getState(PropertyName.ABSTRACT), Value.IS);

        this.nodeTypeDefinition.setAbstract(false);
        assertFalse(this.nodeTypeDefinition.isAbstract());
        assertEquals(this.nodeTypeDefinition.getState(PropertyName.ABSTRACT), Value.IS_NOT);
    }

    @Test
    public void shouldSetMixinProperty() {
        this.nodeTypeDefinition.setMixin(true);
        assertTrue(this.nodeTypeDefinition.isMixin());
        assertEquals(this.nodeTypeDefinition.getState(PropertyName.MIXIN), Value.IS);

        this.nodeTypeDefinition.setMixin(false);
        assertFalse(this.nodeTypeDefinition.isMixin());
        assertEquals(this.nodeTypeDefinition.getState(PropertyName.MIXIN), Value.IS_NOT);
    }

    @Test
    public void shouldSetNameWithNull() {
        this.nodeTypeDefinition.setName(null);
        assertTrue(Utils.isEmpty(this.nodeTypeDefinition.getName()));
    }

    @Test
    public void shouldSetNameWithEmptyString() {
        this.nodeTypeDefinition.setName(Utils.EMPTY_STRING);
        assertTrue(Utils.isEmpty(this.nodeTypeDefinition.getName()));
    }

    @Test
    public void shouldSetName() {
        final String NEW_NAME = "newName"; //$NON-NLS-1$
        this.nodeTypeDefinition.setName(NEW_NAME);
        assertEquals(NEW_NAME, this.nodeTypeDefinition.getName());        
    }

    @Test
    public void shouldReceiveEventAfterChangingName() {
        Listener l = new Listener();
        assertTrue(this.nodeTypeDefinition.addListener(l));

        final String OLD_NAME = this.nodeTypeDefinition.getName();
        final String NEW_NAME = "newName"; //$NON-NLS-1$
        this.nodeTypeDefinition.setName(NEW_NAME);

        assertEquals(1, l.getCount());
        assertEquals(PropertyName.NAME.toString(), l.getPropertyName());
        assertEquals(NEW_NAME, l.getNewValue());
        assertEquals(OLD_NAME, l.getOldValue());
    }

    @Test
    public void shouldSetOrderableProperty() {
        this.nodeTypeDefinition.setOrderableChildNodes(true);
        assertTrue(this.nodeTypeDefinition.hasOrderableChildNodes());
        assertEquals(this.nodeTypeDefinition.getState(PropertyName.ORDERABLE), Value.IS);

        this.nodeTypeDefinition.setOrderableChildNodes(false);
        assertFalse(this.nodeTypeDefinition.hasOrderableChildNodes());
        assertEquals(this.nodeTypeDefinition.getState(PropertyName.ORDERABLE), Value.IS_NOT);
    }

    @Test
    public void shouldSetPrimaryItemNameUsingNull() {
        this.nodeTypeDefinition.setPrimaryItemName(null);
        assertTrue(Utils.isEmpty(this.nodeTypeDefinition.getPrimaryItemName()));
    }

    @Test
    public void shouldSetPrimaryItemNameUsingEmptyValue() {
        this.nodeTypeDefinition.setPrimaryItemName(Utils.EMPTY_STRING);
        assertTrue(Utils.isEmpty(this.nodeTypeDefinition.getPrimaryItemName()));
    }

    @Test
    public void shouldSetPrimaryItemName() {
        final String PRIMARY_ITEM = "primaryItem"; //$NON-NLS-1$
        this.nodeTypeDefinition.setPrimaryItemName(PRIMARY_ITEM);
        assertEquals(PRIMARY_ITEM, this.nodeTypeDefinition.getPrimaryItemName());
    }

    @Test
    public void shouldReceiveEventAfterChangingPrimaryItemName() {
        Listener l = new Listener();
        assertTrue(this.nodeTypeDefinition.addListener(l));

        final String PRIMARY_ITEM = "primaryItem"; //$NON-NLS-1$
        this.nodeTypeDefinition.setPrimaryItemName(PRIMARY_ITEM);

        assertEquals(1, l.getCount());
        assertEquals(PropertyName.PRIMARY_ITEM.toString(), l.getPropertyName());
        assertEquals(PRIMARY_ITEM, l.getNewValue());
        assertNull(l.getOldValue());
    }

    @Test
    public void shouldSetQueryableProperty() {
        this.nodeTypeDefinition.setQueryable(true);
        assertTrue(this.nodeTypeDefinition.isQueryable());
        assertEquals(this.nodeTypeDefinition.getState(PropertyName.QUERYABLE), Value.IS);

        this.nodeTypeDefinition.setQueryable(false);
        assertFalse(this.nodeTypeDefinition.isQueryable());
        assertEquals(this.nodeTypeDefinition.getState(PropertyName.QUERYABLE), Value.IS_NOT);
    }

    @Test
    public void shouldSetSuperTypesWithEmptyArray() {
        assertTrue(this.nodeTypeDefinition.addSuperType("superType")); //$NON-NLS-1$
        this.nodeTypeDefinition.setDeclaredSuperTypeNames(new String[0]);
        assertEquals(0, this.nodeTypeDefinition.getDeclaredSupertypeNames().length);
    }

    @Test
    public void shouldSetSuperTypesWithNull() {
        assertTrue(this.nodeTypeDefinition.addSuperType("superType")); //$NON-NLS-1$
        this.nodeTypeDefinition.setDeclaredSuperTypeNames(null);
        assertEquals(0, this.nodeTypeDefinition.getDeclaredSupertypeNames().length);
    }

    @Test
    public void shouldSetSuperTypeNames() {
        final String SUPER_TYPE1 = "superType1"; //$NON-NLS-1$
        final String SUPER_TYPE2 = "superType2"; //$NON-NLS-1$
        final String SUPER_TYPE3 = "superType3"; //$NON-NLS-1$
        final String[] SUPER_TYPES = new String[] { SUPER_TYPE1, SUPER_TYPE2, SUPER_TYPE3 };
        this.nodeTypeDefinition.setDeclaredSuperTypeNames(SUPER_TYPES);

        String[] superTypeNames = this.nodeTypeDefinition.getDeclaredSupertypeNames();
        assertEquals(SUPER_TYPE1, superTypeNames[0]);
        assertEquals(SUPER_TYPE2, superTypeNames[1]);
        assertEquals(SUPER_TYPE3, superTypeNames[2]);
    }

    @Test
    public void copiesShouldBeEqualAndHaveSameHashCode() {
        NodeTypeDefinition thatNodeTypeDefinition = NodeTypeDefinition.copy(this.nodeTypeDefinition);
        assertEquals(this.nodeTypeDefinition, thatNodeTypeDefinition);
        assertEquals(this.nodeTypeDefinition.hashCode(), thatNodeTypeDefinition.hashCode());
        
        this.nodeTypeDefinition.setName("newName"); //$NON-NLS-1$
        thatNodeTypeDefinition = NodeTypeDefinition.copy(this.nodeTypeDefinition);
        assertEquals(this.nodeTypeDefinition, thatNodeTypeDefinition);
        assertEquals(this.nodeTypeDefinition.hashCode(), thatNodeTypeDefinition.hashCode());
        
        this.nodeTypeDefinition.setAbstract(!this.nodeTypeDefinition.isAbstract());
        thatNodeTypeDefinition = NodeTypeDefinition.copy(this.nodeTypeDefinition);
        assertEquals(this.nodeTypeDefinition, thatNodeTypeDefinition);
        assertEquals(this.nodeTypeDefinition.hashCode(), thatNodeTypeDefinition.hashCode());
        
        this.nodeTypeDefinition.setMixin(!this.nodeTypeDefinition.isMixin());
        thatNodeTypeDefinition = NodeTypeDefinition.copy(this.nodeTypeDefinition);
        assertEquals(this.nodeTypeDefinition, thatNodeTypeDefinition);
        assertEquals(this.nodeTypeDefinition.hashCode(), thatNodeTypeDefinition.hashCode());
        
        this.nodeTypeDefinition.setOrderableChildNodes(!this.nodeTypeDefinition.hasOrderableChildNodes());
        thatNodeTypeDefinition = NodeTypeDefinition.copy(this.nodeTypeDefinition);
        assertEquals(this.nodeTypeDefinition, thatNodeTypeDefinition);
        assertEquals(this.nodeTypeDefinition.hashCode(), thatNodeTypeDefinition.hashCode());
        
        this.nodeTypeDefinition.setQueryable(!this.nodeTypeDefinition.isQueryable());
        thatNodeTypeDefinition = NodeTypeDefinition.copy(this.nodeTypeDefinition);
        assertEquals(this.nodeTypeDefinition, thatNodeTypeDefinition);
        assertEquals(this.nodeTypeDefinition.hashCode(), thatNodeTypeDefinition.hashCode());
        
        this.nodeTypeDefinition.setPrimaryItemName("newPrimaryItem"); //$NON-NLS-1$
        thatNodeTypeDefinition = NodeTypeDefinition.copy(this.nodeTypeDefinition);
        assertEquals(this.nodeTypeDefinition, thatNodeTypeDefinition);
        assertEquals(this.nodeTypeDefinition.hashCode(), thatNodeTypeDefinition.hashCode());

        this.nodeTypeDefinition.addSuperType(Constants.QUALIFIED_NAME1.get());
        thatNodeTypeDefinition = NodeTypeDefinition.copy(this.nodeTypeDefinition);
        assertEquals(this.nodeTypeDefinition, thatNodeTypeDefinition);
        assertEquals(this.nodeTypeDefinition.hashCode(), thatNodeTypeDefinition.hashCode());

        this.nodeTypeDefinition.addChildNodeDefinition(new ChildNodeDefinition());
        thatNodeTypeDefinition = NodeTypeDefinition.copy(this.nodeTypeDefinition);
        assertEquals(this.nodeTypeDefinition, thatNodeTypeDefinition);
        assertEquals(this.nodeTypeDefinition.hashCode(), thatNodeTypeDefinition.hashCode());

        this.nodeTypeDefinition.addPropertyDefinition(new PropertyDefinition());
        thatNodeTypeDefinition = NodeTypeDefinition.copy(this.nodeTypeDefinition);
        assertEquals(this.nodeTypeDefinition, thatNodeTypeDefinition);
        assertEquals(this.nodeTypeDefinition.hashCode(), thatNodeTypeDefinition.hashCode());
    }
}