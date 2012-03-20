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
import org.jboss.tools.modeshape.jcr.cnd.Constants;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 */
public class NodeAttributesTest {

    private NodeAttributes attributes;

    @Before
    public void beforeEach() {
        this.attributes = new NodeAttributes();
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
    public void shouldNotBeSameNameSiblingsAfterConstruction() {
        SameNameSiblings attribute = this.attributes.getSameNameSiblings();
        assertTrue(attribute.isNot());
        assertFalse(attribute.is());
        assertFalse(attribute.isVariant());
    }

    @Test
    public void defaultAttributesShouldHaveEmptyCompressedAndCompactCndNotation() {
        assertTrue(Utils.isEmpty(this.attributes.toCndNotation(NotationType.COMPRESSED)));
        assertTrue(Utils.isEmpty(this.attributes.toCndNotation(NotationType.COMPACT)));
    }

    @Test
    public void copiesShouldBeEqual() {
        NodeAttributes copy = NodeAttributes.copy(this.attributes);
        assertEquals(this.attributes, copy);

        Constants.Helper.changeValue(this.attributes.getAutocreated());
        copy.setAutocreated(this.attributes.getAutocreated().get());
        assertEquals(this.attributes, copy);

        Constants.Helper.changeValue(this.attributes.getMandatory());
        copy.setMandatory(this.attributes.getMandatory().get());
        assertEquals(this.attributes, copy);

        Constants.Helper.changeValue(this.attributes.getProtected());
        copy.setProtected(this.attributes.getProtected().get());
        assertEquals(this.attributes, copy);

        Constants.Helper.changeValue(this.attributes.getSameNameSiblings());
        copy.setSameNameSibling(this.attributes.getSameNameSiblings().get());
        assertEquals(this.attributes, copy);

        this.attributes.setOnParentVersion(OnParentVersion.ABORT);
        copy.setOnParentVersion(this.attributes.getOnParentVersion());
        assertEquals(this.attributes, copy);
    }

    @Test
    public void equalInstancesShouldHaveSameHashCode() {
        NodeAttributes second = new NodeAttributes();
        assertEquals(this.attributes.hashCode(), second.hashCode());
    }
}
