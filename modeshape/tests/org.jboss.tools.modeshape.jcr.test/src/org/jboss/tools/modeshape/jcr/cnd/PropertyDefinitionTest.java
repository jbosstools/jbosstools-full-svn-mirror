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
import org.jboss.tools.modeshape.jcr.cnd.PropertyDefinition.PropertyName;
import org.jboss.tools.modeshape.jcr.cnd.attributes.AttributeState.Value;
import org.jboss.tools.modeshape.jcr.cnd.attributes.OnParentVersion;
import org.jboss.tools.modeshape.jcr.cnd.attributes.PropertyType;
import org.jboss.tools.modeshape.jcr.cnd.attributes.PropertyValue;
import org.jboss.tools.modeshape.jcr.cnd.attributes.QueryOperators.QueryOperator;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 */
public class PropertyDefinitionTest {

    private PropertyDefinition propDefn;

    @Before
    public void beforeEach() {
        this.propDefn = new PropertyDefinition();
    }

    @Test
    public void shouldNotHaveDefaultValuesAfterConstruction() {
        assertEquals(0, this.propDefn.getDefaultValues().length);
    }

    @Test
    public void shouldNotHaveValueConstraintsAfterConstruction() {
        assertEquals(0, this.propDefn.getValueConstraints().length);
    }

    @Test
    public void shouldNotHaveNameAfterConstruction() {
        assertTrue(Utils.isEmpty(this.propDefn.getName()));
    }

    @Test
    public void shouldAddDefaultValue() {
        final String VALUE = "value"; //$NON-NLS-1$
        assertTrue(this.propDefn.addDefaultValue(VALUE));
        assertEquals(1, this.propDefn.getDefaultValues().length);
        assertEquals(VALUE, this.propDefn.getDefaultValuesAsStrings().iterator().next());
    }

    @Test
    public void shouldRemoveDefaultValue() {
        final String VALUE = "value"; //$NON-NLS-1$
        assertTrue(this.propDefn.addDefaultValue(VALUE));
        assertTrue(this.propDefn.removeDefaultValue(VALUE));
        assertEquals(0, this.propDefn.getDefaultValues().length);
    }

    @Test
    public void shouldNotRemoveDefaultValueThatDoesNotExist() {
        assertFalse(this.propDefn.removeDefaultValue("defaultValueDoesNotExist")); //$NON-NLS-1$
    }

    @Test
    public void shouldReceiveEventAfterRemovingDefaultValue() {
        final String VALUE = "value"; //$NON-NLS-1$
        assertTrue(this.propDefn.addDefaultValue(VALUE));

        Listener l = new Listener();
        assertTrue(this.propDefn.addListener(l));

        assertTrue(this.propDefn.removeDefaultValue(VALUE));
        assertEquals(1, l.getCount());
        assertEquals(PropertyName.DEFAULT_VALUES.toString(), l.getPropertyName());
        assertEquals(VALUE, l.getOldValue());
        assertNull(l.getNewValue());
    }

    @Test
    public void shouldReceiveEventAfterAddingDefaultValue() {
        Listener l = new Listener();
        assertTrue(this.propDefn.addListener(l));

        final String VALUE = "value"; //$NON-NLS-1$
        assertTrue(this.propDefn.addDefaultValue(VALUE));

        assertEquals(1, l.getCount());
        assertEquals(PropertyName.DEFAULT_VALUES.toString(), l.getPropertyName());
        assertEquals(VALUE, l.getNewValue());
        assertNull(l.getOldValue());
    }

    @Test
    public void shouldNotReceiveEventAfterUnregistering() {
        Listener l = new Listener();
        assertTrue(this.propDefn.addListener(l));
        assertTrue(this.propDefn.removeListener(l));

        final String VALUE = "value"; //$NON-NLS-1$
        assertTrue(this.propDefn.addDefaultValue(VALUE));

        assertEquals(0, l.getCount());
    }

    @Test
    public void shouldNotAddSameDefaultValue() {
        final String VALUE = "value"; //$NON-NLS-1$
        assertTrue(this.propDefn.addDefaultValue(VALUE));
        assertFalse(this.propDefn.addDefaultValue(VALUE));
    }

    @Test
    public void shouldNotUnregisterAnUnregisteredListener() {
        Listener l = new Listener();
        assertFalse(this.propDefn.removeListener(l));
    }

    @Test
    public void shouldNotRegisterAnAlreadyRegisteredListener() {
        Listener l = new Listener();
        assertTrue(this.propDefn.addListener(l));
        assertFalse(this.propDefn.addListener(l));
    }

    @Test
    public void shouldAddValueConstraint() {
        final String CONSTRAINT = "constraint"; //$NON-NLS-1$
        assertTrue(this.propDefn.addValueConstraint(CONSTRAINT));
        assertEquals(1, this.propDefn.getValueConstraints().length);
        assertEquals(CONSTRAINT, this.propDefn.getValueConstraints()[0]);
    }

    @Test
    public void shouldReceiveEventAfterAddingValueConstraint() {
        Listener l = new Listener();
        assertTrue(this.propDefn.addListener(l));

        final String CONSTRAINT = "constraint"; //$NON-NLS-1$
        assertTrue(this.propDefn.addValueConstraint(CONSTRAINT));

        assertEquals(1, l.getCount());
        assertEquals(PropertyName.VALUE_CONSTRAINTS.toString(), l.getPropertyName());
        assertEquals(CONSTRAINT, l.getNewValue());
        assertNull(l.getOldValue());
    }

    @Test
    public void shouldNotAddSameValueConstraint() {
        final String CONSTRAINT = "constraint"; //$NON-NLS-1$
        assertTrue(this.propDefn.addValueConstraint(CONSTRAINT));
        assertFalse(this.propDefn.addValueConstraint(CONSTRAINT));
    }

    @Test
    public void shouldRemoveValueConstraint() {
        final String CONSTRAINT = "constraint"; //$NON-NLS-1$
        assertTrue(this.propDefn.addValueConstraint(CONSTRAINT));
        assertTrue(this.propDefn.removeValueConstraint(CONSTRAINT));
        assertEquals(0, this.propDefn.getValueConstraints().length);
    }

    @Test
    public void shouldNotRemoveValueConstraintThatDoesNotExist() {
        assertFalse(this.propDefn.removeValueConstraint("valueConstraintdoesNotExist")); //$NON-NLS-1$
    }

    @Test
    public void shouldReceiveEventAfterRemovingValueConstraint() {
        final String CONSTRAINT = "constraint"; //$NON-NLS-1$
        assertTrue(this.propDefn.addValueConstraint(CONSTRAINT));

        Listener l = new Listener();
        assertTrue(this.propDefn.addListener(l));

        assertTrue(this.propDefn.removeValueConstraint(CONSTRAINT));
        assertEquals(1, l.getCount());
        assertEquals(PropertyName.VALUE_CONSTRAINTS.toString(), l.getPropertyName());
        assertEquals(CONSTRAINT, l.getOldValue());
        assertNull(l.getNewValue());
    }

    @Test
    public void shouldChangeAutocreatedPropertyState() {
        assertTrue(this.propDefn.changeState(PropertyName.AUTOCREATED, Value.IS));
        assertEquals(this.propDefn.getState(PropertyName.AUTOCREATED), Value.IS);
        assertTrue(this.propDefn.isAutoCreated());

        assertTrue(this.propDefn.changeState(PropertyName.AUTOCREATED, Value.VARIANT));
        assertEquals(this.propDefn.getState(PropertyName.AUTOCREATED), Value.VARIANT);
        assertTrue(this.propDefn.isVariant(PropertyName.AUTOCREATED));

        assertTrue(this.propDefn.changeState(PropertyName.AUTOCREATED, Value.IS_NOT));
        assertEquals(this.propDefn.getState(PropertyName.AUTOCREATED), Value.IS_NOT);
        assertFalse(this.propDefn.isAutoCreated());
    }

    @Test
    public void shouldChangeDefaultValuesPropertyToVariantStateOnly() {
        assertFalse(this.propDefn.changeState(PropertyName.DEFAULT_VALUES, Value.IS));

        assertTrue(this.propDefn.changeState(PropertyName.DEFAULT_VALUES, Value.VARIANT));
        assertEquals(this.propDefn.getState(PropertyName.DEFAULT_VALUES), Value.VARIANT);
        assertTrue(this.propDefn.isVariant(PropertyName.DEFAULT_VALUES));

        assertFalse(this.propDefn.changeState(PropertyName.DEFAULT_VALUES, Value.IS_NOT));
    }

    @Test
    public void shouldChangeMandatoryPropertyState() {
        assertTrue(this.propDefn.changeState(PropertyName.MANDATORY, Value.IS));
        assertEquals(this.propDefn.getState(PropertyName.MANDATORY), Value.IS);
        assertTrue(this.propDefn.isMandatory());

        assertTrue(this.propDefn.changeState(PropertyName.MANDATORY, Value.VARIANT));
        assertEquals(this.propDefn.getState(PropertyName.MANDATORY), Value.VARIANT);
        assertTrue(this.propDefn.isVariant(PropertyName.MANDATORY));

        assertTrue(this.propDefn.changeState(PropertyName.MANDATORY, Value.IS_NOT));
        assertEquals(this.propDefn.getState(PropertyName.MANDATORY), Value.IS_NOT);
        assertFalse(this.propDefn.isMandatory());
    }

    @Test
    public void shouldChangeMultiplePropertyState() {
        assertTrue(this.propDefn.changeState(PropertyName.MULTIPLE, Value.IS));
        assertEquals(this.propDefn.getState(PropertyName.MULTIPLE), Value.IS);
        assertTrue(this.propDefn.isMultiple());

        assertTrue(this.propDefn.changeState(PropertyName.MULTIPLE, Value.VARIANT));
        assertEquals(this.propDefn.getState(PropertyName.MULTIPLE), Value.VARIANT);
        assertTrue(this.propDefn.isVariant(PropertyName.MULTIPLE));

        assertTrue(this.propDefn.changeState(PropertyName.MULTIPLE, Value.IS_NOT));
        assertEquals(this.propDefn.getState(PropertyName.MULTIPLE), Value.IS_NOT);
        assertFalse(this.propDefn.isMultiple());
    }

    @Test
    public void shouldChangeNoFullTextPropertyState() {
        assertTrue(this.propDefn.changeState(PropertyName.NO_FULL_TEXT, Value.IS));
        assertEquals(this.propDefn.getState(PropertyName.NO_FULL_TEXT), Value.IS);
        assertFalse(this.propDefn.isFullTextSearchable());

        assertTrue(this.propDefn.changeState(PropertyName.NO_FULL_TEXT, Value.VARIANT));
        assertEquals(this.propDefn.getState(PropertyName.NO_FULL_TEXT), Value.VARIANT);
        assertTrue(this.propDefn.isVariant(PropertyName.NO_FULL_TEXT));

        assertTrue(this.propDefn.changeState(PropertyName.NO_FULL_TEXT, Value.IS_NOT));
        assertEquals(this.propDefn.getState(PropertyName.NO_FULL_TEXT), Value.IS_NOT);
        assertTrue(this.propDefn.isFullTextSearchable());
    }

    @Test
    public void shouldChangeNoQueryOrderPropertyState() {
        assertTrue(this.propDefn.changeState(PropertyName.NO_QUERY_ORDER, Value.IS));
        assertEquals(this.propDefn.getState(PropertyName.NO_QUERY_ORDER), Value.IS);
        assertFalse(this.propDefn.isQueryOrderable());

        assertTrue(this.propDefn.changeState(PropertyName.NO_QUERY_ORDER, Value.VARIANT));
        assertEquals(this.propDefn.getState(PropertyName.NO_QUERY_ORDER), Value.VARIANT);
        assertTrue(this.propDefn.isVariant(PropertyName.NO_QUERY_ORDER));

        assertTrue(this.propDefn.changeState(PropertyName.NO_QUERY_ORDER, Value.IS_NOT));
        assertEquals(this.propDefn.getState(PropertyName.NO_QUERY_ORDER), Value.IS_NOT);
        assertTrue(this.propDefn.isQueryOrderable());
    }

    @Test
    public void shouldChangeOnParentVersionPropertyToVariantStateOnly() {
        assertFalse(this.propDefn.changeState(PropertyName.ON_PARENT_VERSION, Value.IS));

        assertTrue(this.propDefn.changeState(PropertyName.ON_PARENT_VERSION, Value.VARIANT));
        assertTrue(this.propDefn.isVariant(PropertyName.ON_PARENT_VERSION));
        assertTrue(this.propDefn.isVariant(PropertyName.ON_PARENT_VERSION));

        assertFalse(this.propDefn.changeState(PropertyName.ON_PARENT_VERSION, Value.IS_NOT));
    }

    @Test
    public void shouldChangeProtectedPropertyState() {
        assertTrue(this.propDefn.changeState(PropertyName.PROTECTED, Value.IS));
        assertEquals(this.propDefn.getState(PropertyName.PROTECTED), Value.IS);
        assertTrue(this.propDefn.isProtected());

        assertTrue(this.propDefn.changeState(PropertyName.PROTECTED, Value.VARIANT));
        assertEquals(this.propDefn.getState(PropertyName.PROTECTED), Value.VARIANT);
        assertTrue(this.propDefn.isVariant(PropertyName.PROTECTED));

        assertTrue(this.propDefn.changeState(PropertyName.PROTECTED, Value.IS_NOT));
        assertEquals(this.propDefn.getState(PropertyName.PROTECTED), Value.IS_NOT);
        assertFalse(this.propDefn.isProtected());
    }

    @Test
    public void shouldChangeQueryOpsPropertyToVariantStateOnly() {
        assertFalse(this.propDefn.changeState(PropertyName.QUERY_OPS, Value.IS));

        assertTrue(this.propDefn.changeState(PropertyName.QUERY_OPS, Value.VARIANT));
        assertTrue(this.propDefn.isVariant(PropertyName.QUERY_OPS));
        assertTrue(this.propDefn.isVariant(PropertyName.QUERY_OPS));

        assertFalse(this.propDefn.changeState(PropertyName.QUERY_OPS, Value.IS_NOT));
    }

    @Test
    public void shouldChangeTypePropertyToVariantStateOnly() {
        assertFalse(this.propDefn.changeState(PropertyName.TYPE, Value.IS));

        assertTrue(this.propDefn.changeState(PropertyName.TYPE, Value.VARIANT));
        assertTrue(this.propDefn.isVariant(PropertyName.TYPE));
        assertTrue(this.propDefn.isVariant(PropertyName.TYPE));

        assertFalse(this.propDefn.changeState(PropertyName.TYPE, Value.IS_NOT));
    }

    @Test
    public void shouldChangeValueConstraintsPropertyToVariantStateOnly() {
        assertFalse(this.propDefn.changeState(PropertyName.VALUE_CONSTRAINTS, Value.IS));

        assertTrue(this.propDefn.changeState(PropertyName.VALUE_CONSTRAINTS, Value.VARIANT));
        assertEquals(this.propDefn.getState(PropertyName.VALUE_CONSTRAINTS), Value.VARIANT);
        assertTrue(this.propDefn.isVariant(PropertyName.VALUE_CONSTRAINTS));

        assertFalse(this.propDefn.changeState(PropertyName.VALUE_CONSTRAINTS, Value.IS_NOT));
    }

    @Test
    public void shouldClearDefaultValues() {
        assertTrue(this.propDefn.addDefaultValue("value")); //$NON-NLS-1$
        assertTrue(this.propDefn.clearDefaultValues());
        assertEquals(0, this.propDefn.getDefaultValues().length);
    }

    @Test
    public void shouldNotClearDefaultValuesWhenEmpty() {
        assertFalse(this.propDefn.clearDefaultValues());
    }

    @Test
    public void shouldReceiveEventAfterClearingDefaultValues() {
        final String VALUE = "value"; //$NON-NLS-1$
        assertTrue(this.propDefn.addDefaultValue(VALUE));
        Collection<String> oldValue = Collections.singletonList(VALUE);

        Listener l = new Listener();
        assertTrue(this.propDefn.addListener(l));

        assertTrue(this.propDefn.clearDefaultValues());
        assertEquals(1, l.getCount());
        assertEquals(PropertyName.DEFAULT_VALUES.toString(), l.getPropertyName());
        assertNull(l.getNewValue());
        assertEquals(oldValue, l.getOldValue());
    }

    @Test
    public void shouldClearValueConstraints() {
        assertTrue(this.propDefn.addValueConstraint("value")); //$NON-NLS-1$
        assertTrue(this.propDefn.clearValueConstraints());
        assertEquals(0, this.propDefn.getValueConstraints().length);
    }

    @Test
    public void shouldNotClearValueConstraintsWhenEmpty() {
        assertFalse(this.propDefn.clearValueConstraints());
    }

    @Test
    public void shouldReceiveEventAfterClearingValueConstraints() {
        final String CONSTRAINT = "constraint"; //$NON-NLS-1$
        assertTrue(this.propDefn.addValueConstraint(CONSTRAINT));
        Collection<String> oldValue = Collections.singletonList(CONSTRAINT);

        Listener l = new Listener();
        assertTrue(this.propDefn.addListener(l));

        assertTrue(this.propDefn.clearValueConstraints());
        assertEquals(1, l.getCount());
        assertEquals(PropertyName.VALUE_CONSTRAINTS.toString(), l.getPropertyName());
        assertNull(l.getNewValue());
        assertEquals(oldValue, l.getOldValue());
    }

    @Test
    public void shouldSetAutoCreated() {
        this.propDefn.setAutoCreated(true);
        assertTrue(this.propDefn.isAutoCreated());
        assertTrue(this.propDefn.getState(PropertyName.AUTOCREATED) == Value.IS);

        this.propDefn.setAutoCreated(false);
        assertFalse(this.propDefn.isAutoCreated());
        assertTrue(this.propDefn.getState(PropertyName.AUTOCREATED) == Value.IS_NOT);
    }

    @Test
    public void shouldSetFullTextSearchable() {
        this.propDefn.setFullTextSearchable(true);
        assertTrue(this.propDefn.isFullTextSearchable());
        assertTrue(this.propDefn.getState(PropertyName.NO_FULL_TEXT) == Value.IS_NOT);

        this.propDefn.setFullTextSearchable(false);
        assertFalse(this.propDefn.isFullTextSearchable());
        assertTrue(this.propDefn.getState(PropertyName.NO_FULL_TEXT) == Value.IS);
    }

    @Test
    public void shouldSetMandatory() {
        this.propDefn.setMandatory(true);
        assertTrue(this.propDefn.isMandatory());
        assertTrue(this.propDefn.getState(PropertyName.MANDATORY) == Value.IS);

        this.propDefn.setMandatory(false);
        assertFalse(this.propDefn.isMandatory());
        assertTrue(this.propDefn.getState(PropertyName.MANDATORY) == Value.IS_NOT);
    }

    @Test
    public void shouldSetMultiple() {
        this.propDefn.setMultiple(true);
        assertTrue(this.propDefn.isMultiple());
        assertTrue(this.propDefn.getState(PropertyName.MULTIPLE) == Value.IS);

        this.propDefn.setMultiple(false);
        assertFalse(this.propDefn.isMultiple());
        assertTrue(this.propDefn.getState(PropertyName.MULTIPLE) == Value.IS_NOT);
    }

    @Test
    public void shouldSetProtected() {
        this.propDefn.setProtected(true);
        assertTrue(this.propDefn.isProtected());
        assertTrue(this.propDefn.getState(PropertyName.PROTECTED) == Value.IS);

        this.propDefn.setProtected(false);
        assertFalse(this.propDefn.isProtected());
        assertTrue(this.propDefn.getState(PropertyName.PROTECTED) == Value.IS_NOT);
    }

    @Test
    public void shouldSetQueryOrderable() {
        this.propDefn.setQueryOrderable(true);
        assertTrue(this.propDefn.isQueryOrderable());
        assertTrue(this.propDefn.getState(PropertyName.NO_QUERY_ORDER) == Value.IS_NOT);

        this.propDefn.setQueryOrderable(false);
        assertFalse(this.propDefn.isQueryOrderable());
        assertTrue(this.propDefn.getState(PropertyName.NO_QUERY_ORDER) == Value.IS);
    }

    @Test
    public void shouldSetName() {
        final String NAME = "name"; //$NON-NLS-1$
        this.propDefn.setName(NAME);
        assertEquals(NAME, this.propDefn.getName());

    }

    @Test
    public void shouldAllowNullEmptyName() {
        this.propDefn.setName(null);
        this.propDefn.setName(""); //$NON-NLS-1$

    }

    @Test
    public void shouldReceiveEventWhenNameIsChanged() {
        Listener l = new Listener();
        assertTrue(this.propDefn.addListener(l));

        final String NAME = "name"; //$NON-NLS-1$
        this.propDefn.setName(NAME);

        assertEquals(NAME, this.propDefn.getName());
        assertEquals(1, l.getCount());
        assertEquals(PropertyName.NAME.toString(), l.getPropertyName());
        assertEquals(NAME, l.getNewValue());
        assertNull(l.getOldValue());
    }

    @Test
    public void shouldNotReceiveEventWhenNameIsNotChanged() {
        final String NAME = "name"; //$NON-NLS-1$
        this.propDefn.setName(NAME);

        Listener l = new Listener();
        assertTrue(this.propDefn.addListener(l));

        this.propDefn.setName(NAME); // same value
        assertEquals(0, l.getCount());
    }

    @Test
    public void onParentVersionShouldBeSetToDefaultAfterConstruction() {
        assertTrue(this.propDefn.getOnParentVersion() == OnParentVersion.DEFAULT_VALUE.asJcrValue());
    }

    @Test
    public void onParentVersionDefaultValueShouldBeCopy() {
        assertEquals(OnParentVersion.COPY, OnParentVersion.DEFAULT_VALUE);
    }

    @Test
    public void shouldSetOnParentVersionUsingInt() {
        for (OnParentVersion opv : OnParentVersion.values()) {
            if (opv != OnParentVersion.VARIANT) {
                this.propDefn.setOnParentVersion(opv.asJcrValue());
                assertEquals(opv.asJcrValue(), this.propDefn.getOnParentVersion());
            }
        }
    }

    @Test
    public void shouldSetOnParentVersionUsingString() {
        for (OnParentVersion opv : OnParentVersion.values()) {
            if (opv != OnParentVersion.VARIANT) {
                assertTrue(this.propDefn.setOnParentVersion(opv.toString()));
                assertEquals(opv.asJcrValue(), this.propDefn.getOnParentVersion());
            }
        }
    }

    @Test
    public void shouldReceiveEventWhenOnParentVersionIsChanged() {
        Listener l = new Listener();
        assertTrue(this.propDefn.addListener(l));

        final int OPV = OnParentVersion.COMPUTE.asJcrValue();
        this.propDefn.setOnParentVersion(OPV);

        assertEquals(1, l.getCount());
        assertEquals(PropertyName.ON_PARENT_VERSION.toString(), l.getPropertyName());
        assertEquals(OPV, l.getNewValue());
        assertEquals(OnParentVersion.DEFAULT_VALUE, l.getOldValue());
    }

    @Test
    public void propertyTypeShouldBeSetToDefaultAfterConstruction() {
        assertTrue(this.propDefn.getType() == PropertyType.DEFAULT_VALUE);
        assertEquals(PropertyType.DEFAULT_VALUE.asJcrValue(), this.propDefn.getRequiredType());
    }

    @Test
    public void propertyTypeDefaultValueShouldBeString() {
        assertEquals(PropertyType.STRING, PropertyType.DEFAULT_VALUE);
    }

    @Test
    public void shouldSetPropertyTypeUsingInt() {
        for (PropertyType type : PropertyType.values()) {
            if (type != PropertyType.VARIANT) {
                this.propDefn.setRequiredType(type.asJcrValue());
                assertEquals(type.asJcrValue(), this.propDefn.getRequiredType());
            }
        }
    }

    @Test
    public void shouldSetPropertyType() {
        for (PropertyType type : PropertyType.values()) {
            if (type != PropertyType.VARIANT) {
                assertTrue(this.propDefn.setType(type));
                assertEquals(type.asJcrValue(), this.propDefn.getRequiredType());
            }
        }
    }

    @Test
    public void shouldReceiveEventWhenPropertyTypeIsChanged() {
        Listener l = new Listener();
        assertTrue(this.propDefn.addListener(l));

        final PropertyType TYPE = PropertyType.BINARY;
        this.propDefn.setRequiredType(TYPE.asJcrValue());

        assertEquals(1, l.getCount());
        assertEquals(PropertyName.TYPE.toString(), l.getPropertyName());
        assertEquals(TYPE, l.getNewValue());
        assertEquals(PropertyType.DEFAULT_VALUE, l.getOldValue());
    }

    @Test
    public void shouldSetValueContraintsWithNull() {
        this.propDefn.setValueConstraints(null);
        assertEquals(0, this.propDefn.getValueConstraints().length);
    }

    @Test
    public void shouldSetValueContraintsWithEmptyArray() {
        this.propDefn.setValueConstraints(new String[0]);
        assertEquals(0, this.propDefn.getValueConstraints().length);
    }

    @Test
    public void shouldSetValueContraints() {
        assertTrue(this.propDefn.addValueConstraint("constraintBeingOverridden")); //$NON-NLS-1$

        final String CONSTRAINT1 = "constraint1"; //$NON-NLS-1$
        final String CONSTRAINT2 = "constraint2"; //$NON-NLS-1$
        final String CONSTRAINT3 = "constraint3"; //$NON-NLS-1$
        final String[] NEW_CONSTRAINTS = new String[] { CONSTRAINT1, CONSTRAINT2, CONSTRAINT3 };
        this.propDefn.setValueConstraints(NEW_CONSTRAINTS);

        String[] constraints = this.propDefn.getValueConstraints();
        assertEquals(NEW_CONSTRAINTS.length, constraints.length);

        for (int i = 0; i < NEW_CONSTRAINTS.length; ++i) {
            assertEquals(NEW_CONSTRAINTS[i], constraints[i]);
        }
    }

    @Test
    public void shouldSetDefaultValuesWithNull() {
        this.propDefn.setDefaultValues(null);
        assertEquals(0, this.propDefn.getDefaultValues().length);
    }

    @Test
    public void shouldSetDefaultValuesWithEmptyArray() {
        this.propDefn.setDefaultValues(new javax.jcr.Value[0]);
        assertEquals(0, this.propDefn.getDefaultValues().length);
    }

    @Test
    public void shouldSetDefaultValues() {
        assertTrue(this.propDefn.addDefaultValue("defaultValueBeingOverridden")); //$NON-NLS-1$

        final PropertyValue VALUE1 = new PropertyValue(PropertyType.STRING.asJcrValue(), "value1"); //$NON-NLS-1$
        final PropertyValue VALUE2 = new PropertyValue(PropertyType.STRING.asJcrValue(), "value2"); //$NON-NLS-1$
        final PropertyValue VALUE3 = new PropertyValue(PropertyType.STRING.asJcrValue(), "value3"); //$NON-NLS-1$
        final PropertyValue[] NEW_VALUES = new PropertyValue[] { VALUE1, VALUE2, VALUE3 };
        this.propDefn.setDefaultValues(NEW_VALUES);

        javax.jcr.Value[] defaultValues = this.propDefn.getDefaultValues();
        assertEquals(NEW_VALUES.length, defaultValues.length);

        for (int i = 0; i < NEW_VALUES.length; ++i) {
            assertEquals(NEW_VALUES[i], defaultValues[i]);
        }

        assertEquals(Value.IS, this.propDefn.getState(PropertyName.DEFAULT_VALUES));
    }

    @Test
    public void shouldSetQueryOperatorsWithNull() {
        this.propDefn.setAvailableQueryOperators(null);
        assertEquals(0, this.propDefn.getAvailableQueryOperators().length);
    }

    @Test
    public void shouldSetQueryOperatorsWithEmptyArray() {
        this.propDefn.setAvailableQueryOperators(new String[0]);
        assertEquals(0, this.propDefn.getAvailableQueryOperators().length);
    }

    @Test
    public void shouldAddQueryOperator() {
        final QueryOperator OP = QueryOperator.GREATER_THAN_EQUALS;
        assertTrue(this.propDefn.addQueryOperator(OP));
        assertEquals(1, this.propDefn.getAvailableQueryOperators().length);
        assertEquals(OP.toString(), this.propDefn.getAvailableQueryOperators()[0]);
    }

    @Test
    public void shouldRemoveQueryOperator() {
        final QueryOperator OP = QueryOperator.GREATER_THAN_EQUALS;
        assertTrue(this.propDefn.addQueryOperator(OP));
        assertTrue(this.propDefn.removeQueryOperator(OP));
    }

    @Test
    public void shouldNotRemoveQueryOperatorThatDoesNotExist() {
        assertFalse(this.propDefn.removeQueryOperator(QueryOperator.GREATER_THAN_EQUALS));
    }

    @Test
    public void shouldNotAddDuplicateQueryOperator() {
        final QueryOperator OP = QueryOperator.GREATER_THAN_EQUALS;
        assertTrue(this.propDefn.addQueryOperator(OP));
        assertFalse(this.propDefn.addQueryOperator(OP));
    }

    @Test
    public void shouldReceiveEventWhenQueryOperatorIsAdded() {
        Listener l = new Listener();
        assertTrue(this.propDefn.addListener(l));

        final QueryOperator OP = QueryOperator.GREATER_THAN_EQUALS;
        assertTrue(this.propDefn.addQueryOperator(OP));

        assertEquals(1, l.getCount());
        assertEquals(PropertyName.QUERY_OPS.toString(), l.getPropertyName());
        assertEquals(OP, l.getNewValue());
        assertNull(l.getOldValue());
    }

    @Test
    public void shouldReceiveEventWhenQueryOperatorIsRemoved() {
        final QueryOperator OP = QueryOperator.GREATER_THAN_EQUALS;
        assertTrue(this.propDefn.addQueryOperator(OP));

        Listener l = new Listener();
        assertTrue(this.propDefn.addListener(l));

        assertTrue(this.propDefn.removeQueryOperator(OP));
        assertEquals(1, l.getCount());
        assertEquals(PropertyName.QUERY_OPS.toString(), l.getPropertyName());
        assertNull(l.getNewValue());
        assertEquals(OP, l.getOldValue());
    }

    @Test
    public void shouldSetQueryOperators() {
        assertTrue(this.propDefn.addQueryOperator(QueryOperator.GREATER_THAN_EQUALS));

        final String OP1 = QueryOperator.EQUALS.toString();
        final String OP2 = QueryOperator.LESS_THAN.toString();
        final String OP3 = QueryOperator.GREATER_THAN.toString();
        final String[] NEW_OPERATORS = new String[] { OP1, OP2, OP3 };
        this.propDefn.setAvailableQueryOperators(NEW_OPERATORS);

        String[] queryOperators = this.propDefn.getAvailableQueryOperators();
        assertEquals(NEW_OPERATORS.length, queryOperators.length);

        for (int i = 0; i < NEW_OPERATORS.length; ++i) {
            assertEquals(NEW_OPERATORS[i], queryOperators[i]);
        }
    }

    @Test
    public void shouldNotSetInvalidQueryOperators() {
        final String GOOD_OP = QueryOperator.EQUALS.toString();
        final String BAD_OP = "badOp"; //$NON-NLS-1$
        final String[] NEW_OPERATORS = new String[] { GOOD_OP, BAD_OP };
        this.propDefn.setAvailableQueryOperators(NEW_OPERATORS);

        String[] queryOperators = this.propDefn.getAvailableQueryOperators();
        assertEquals((NEW_OPERATORS.length - 1), queryOperators.length);
        assertEquals(NEW_OPERATORS[0], queryOperators[0]);
    }

}
