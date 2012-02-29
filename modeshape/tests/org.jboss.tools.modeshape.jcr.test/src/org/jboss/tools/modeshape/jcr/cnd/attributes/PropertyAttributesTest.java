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

import org.jboss.tools.modeshape.jcr.Utils;
import org.jboss.tools.modeshape.jcr.cnd.CndElement.NotationType;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 */
public class PropertyAttributesTest {

    private PropertyAttributes attributes;

    @Before
    public void beforeEach() {
        this.attributes = new PropertyAttributes();
    }

    @Test
    public void shouldNotBeAutocreatedAfterConstruction() {
        Autocreated attribute = this.attributes.getAutocreated();
        assertTrue(attribute.isNot());
        assertFalse(attribute.is());
        assertFalse(attribute.isVariant());
    }

    @Test
    public void shouldNotBeMandatoryAfterConstruction() {
        Mandatory attribute = this.attributes.getMandatory();
        assertTrue(attribute.isNot());
        assertFalse(attribute.is());
        assertFalse(attribute.isVariant());
    }

    @Test
    public void shouldNotBeProtectedAfterConstruction() {
        Protected attribute = this.attributes.getProtected();
        assertTrue(attribute.isNot());
        assertFalse(attribute.is());
        assertFalse(attribute.isVariant());
    }

    @Test
    public void opvShouldBeDefaultAfterConstruction() {
        assertEquals(OnParentVersion.DEFAULT_VALUE, this.attributes.getOnParentVersion());
    }

    @Test
    public void shouldNotBeMultipleAfterConstruction() {
        Multiple attribute = this.attributes.getMultiple();
        assertTrue(attribute.isNot());
        assertFalse(attribute.is());
        assertFalse(attribute.isVariant());
    }

    @Test
    public void shouldNotHaveQueryOpsAfterConstruction() {
        QueryOperators attribute = this.attributes.getQueryOps();
        assertTrue(attribute.isNot());
        assertFalse(attribute.is());
        assertFalse(attribute.isVariant());
    }

    @Test
    public void shouldNotHaveNoFullTextAfterConstruction() {
        NoFullText attribute = this.attributes.getNoFullText();
        assertTrue(attribute.isNot());
        assertFalse(attribute.is());
        assertFalse(attribute.isVariant());
    }

    @Test
    public void shouldNotHaveNoQueryOrderAfterConstruction() {
        NoQueryOrder attribute = this.attributes.getNoQueryOrder();
        assertTrue(attribute.isNot());
        assertFalse(attribute.is());
        assertFalse(attribute.isVariant());
    }

    @Test
    public void defaultAttributesShouldHaveEmptyCompressedAndCompactCndNotation() {
        assertTrue(Utils.isEmpty(this.attributes.toCndNotation(NotationType.COMPRESSED)));
        assertTrue(Utils.isEmpty(this.attributes.toCndNotation(NotationType.COMPACT)));
    }

}
