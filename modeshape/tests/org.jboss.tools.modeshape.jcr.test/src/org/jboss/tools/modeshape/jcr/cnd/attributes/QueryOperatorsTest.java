/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.jboss.tools.modeshape.jcr.cnd.attributes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.tools.modeshape.jcr.Utils;
import org.jboss.tools.modeshape.jcr.attributes.AttributeState;
import org.jboss.tools.modeshape.jcr.attributes.QueryOperators;
import org.jboss.tools.modeshape.jcr.attributes.QueryOperators.QueryOperator;
import org.jboss.tools.modeshape.jcr.cnd.CndElement;
import org.jboss.tools.modeshape.jcr.cnd.Constants;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 */
public class QueryOperatorsTest implements Constants {

    private QueryOperators attribute;

    private void add( final QueryOperator operator ) {
        if (!this.attribute.add(operator)) {
            fail();
        }
    }

    @Before
    public void beforeEach() {
        this.attribute = new QueryOperators();
    }

    @Test
    public void initialCndNotationShouldBeEmpty() {
        assertTrue(Utils.isEmpty(this.attribute.toCndNotation(CndElement.NotationType.LONG)));
        assertTrue(Utils.isEmpty(this.attribute.toCndNotation(CndElement.NotationType.COMPRESSED)));
        assertTrue(Utils.isEmpty(this.attribute.toCndNotation(CndElement.NotationType.COMPACT)));
    }

    private void remove( final QueryOperator operator ) {
        if (!this.attribute.remove(operator)) {
            fail();
        }
    }

    @Test
    public void verifyAddedItem() {
        // setup
        add(OPERATOR_ONE);

        // tests
        assertEquals(1, this.attribute.getSupportedItems().size());
        assertTrue(this.attribute.getSupportedItems().contains(OPERATOR_ONE));
    }

    @Test
    public void verifyInitiallyNoSupportedItems() {
        assertEquals(0, this.attribute.getSupportedItems().size());
    }

    @Test
    public void verifyInitialStateShouldBeIsNot() {
        assertEquals(AttributeState.Value.IS_NOT, this.attribute.get());
    }

    @Test
    public void verifyMultipleElementsCndNotation() {
        // setup
        add(OPERATOR_ONE);
        add(OPERATOR_TWO);
        add(OPERATOR_THREE);

        // tests
        assertEquals(QUERY_OPS_THREE_OPERATOR_COMPACT_FORM, this.attribute.toCndNotation(CndElement.NotationType.COMPACT));
        assertEquals(QUERY_OPS_THREE_OPERATOR_COMPRESSED_FORM, this.attribute.toCndNotation(CndElement.NotationType.COMPRESSED));
        assertEquals(QUERY_OPS_THREE_OPERATOR_LONG_FORM, this.attribute.toCndNotation(CndElement.NotationType.LONG));
    }

    @Test
    public void verifyOneElementCndNotation() {
        // setup
        add(OPERATOR_ONE);

        // tests
        assertEquals(QUERY_OPS_ONE_OPERATOR_COMPACT_FORM, this.attribute.toCndNotation(CndElement.NotationType.COMPACT));
        assertEquals(QUERY_OPS_ONE_OPERATOR_COMPRESSED_FORM, this.attribute.toCndNotation(CndElement.NotationType.COMPRESSED));
        assertEquals(QUERY_OPS_ONE_OPERATOR_LONG_FORM, this.attribute.toCndNotation(CndElement.NotationType.LONG));
    }

    @Test
    public void verifyRemoveItem() {
        // setup
        add(OPERATOR_ONE);
        add(OPERATOR_TWO);
        remove(OPERATOR_ONE);

        // tests
        assertFalse(this.attribute.getSupportedItems().contains(OPERATOR_ONE));
    }

    @Test
    public void verifySameElementIsNotAdded() {
        // setup
        add(OPERATOR_ONE);

        // tests
        if (this.attribute.add(OPERATOR_ONE)) {
            fail();
        }

        assertEquals(1, this.attribute.getSupportedItems().size());
    }

    @Test
    public void verifyStateShouldBeIsAfterAdd() {
        // setup
        add(QueryOperator.EQUALS);

        // tests
        assertEquals(AttributeState.Value.IS, this.attribute.get());
    }

    @Test
    public void verifyStateShouldBeIsNotWhenEmpty() {
        // setup
        add(OPERATOR_ONE);
        remove(OPERATOR_ONE);

        // tests
        assertEquals(0, this.attribute.getSupportedItems().size());
        assertEquals(AttributeState.Value.IS_NOT, this.attribute.get());
    }

    @Test
    public void verifyVariantCndNotation() {
        // setup
        this.attribute.set(AttributeState.Value.VARIANT);

        // tests
        assertEquals(QUERY_OPS_VARIANT_COMPACT_FORM, this.attribute.toCndNotation(CndElement.NotationType.COMPACT));
        assertEquals(QUERY_OPS_VARIANT_COMPRESSED_FORM, this.attribute.toCndNotation(CndElement.NotationType.COMPRESSED));
        assertEquals(QUERY_OPS_VARIANT_LONG_FORM, this.attribute.toCndNotation(CndElement.NotationType.LONG));
    }

}
