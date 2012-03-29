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

import org.jboss.tools.modeshape.jcr.LocalName;
import org.jboss.tools.modeshape.jcr.Utils;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 */
public class LocalNameTest {

    private LocalName localName;

    @Before
    public void beforeEach() {
        this.localName = new LocalName();
    }

    @Test
    public void differentModesDifferentValuesShouldNotBeEqual() {
        final String VALUE = "value"; //$NON-NLS-1$
        this.localName.set(VALUE);
        final LocalName thatLocalName = new LocalName(VALUE + "Changed"); //$NON-NLS-1$
        thatLocalName.setMode(LocalName.Mode.DOUBLE_QUOTED);
        assertFalse(this.localName.equals(thatLocalName));
    }

    @Test
    public void differentModesSameValuesShouldBeEqual() {
        final String VALUE = "value"; //$NON-NLS-1$
        this.localName.set(VALUE);
        final LocalName thatLocalName = new LocalName(VALUE);
        thatLocalName.setMode(LocalName.Mode.DOUBLE_QUOTED);
        assertTrue(this.localName.equals(thatLocalName));
    }

    @Test
    public void initialModeShouldBeUnquoted() {
        assertTrue(this.localName.isUnquoted());
    }

    @Test
    public void initialValueShouldBeCorrectWhenSet() {
        final String INITIAL_VALUE = "initialValue"; //$NON-NLS-1$
        this.localName = new LocalName(INITIAL_VALUE);
        assertEquals(INITIAL_VALUE, this.localName.get());
    }

    @Test
    public void initialValueShouldBeNull() {
        assertNull(this.localName.get());
    }

    @Test
    public void sameModesDifferentValuesShouldNotBeEqual() {
        final String VALUE = "value"; //$NON-NLS-1$
        this.localName.set(VALUE);
        final LocalName thatLocalName = new LocalName(VALUE + "Changed"); //$NON-NLS-1$
        assertFalse(this.localName.equals(thatLocalName));
    }

    @Test
    public void sameModesSameValuesShouldBeEqual() {
        final String VALUE = "value"; //$NON-NLS-1$
        this.localName.set(VALUE);
        final LocalName thatLocalName = new LocalName(VALUE);
        assertTrue(this.localName.equals(thatLocalName));
    }

    @Test
    public void shouldHaveCorrectDoubleQuotedCndNotation() {
        final String VALUE = "value"; //$NON-NLS-1$
        this.localName.set(VALUE);
        this.localName.setMode(LocalName.Mode.DOUBLE_QUOTED);
        assertEquals('"' + VALUE + '"', this.localName.toCndNotation(null));
    }

    @Test
    public void shouldHaveCorrectSingleQuotedCndNotation() {
        final String VALUE = "value"; //$NON-NLS-1$
        this.localName.set(VALUE);
        this.localName.setMode(LocalName.Mode.SINGLE_QUOTED);
        assertEquals('\'' + VALUE + '\'', this.localName.toCndNotation(null));
    }

    @Test
    public void shouldHaveCorrectUnquotedCndNotation() {
        final String VALUE = "value"; //$NON-NLS-1$
        this.localName.set(VALUE);
        assertEquals(VALUE, this.localName.toCndNotation(null));
    }

    @Test
    public void shouldHaveSameHashCodeWhenEqual() {
        final LocalName thatLocalName = new LocalName();
        assertEquals(this.localName.hashCode(), thatLocalName.hashCode());

        final String VALUE = "value"; //$NON-NLS-1$
        this.localName.set(VALUE);
        thatLocalName.set(VALUE);
        assertEquals(this.localName.hashCode(), thatLocalName.hashCode());
    }

    @Test
    public void shouldNotSetModeToSameValue() {
        assertFalse(this.localName.setMode(LocalName.Mode.UNQOUTED));
    }

    @Test
    public void shouldQuoteIfContainsSpacesAndModeIsUnquoted() {
        final String NEW_VALUE = "new value"; //$NON-NLS-1$
        this.localName.set(NEW_VALUE);
        assertTrue(this.localName.isUnquoted());
        assertEquals('\'' + NEW_VALUE + '\'', this.localName.toCndNotation(null));
    }

    @Test
    public void shouldSetModeToDoubleQuoted() {
        assertTrue(this.localName.setMode(LocalName.Mode.DOUBLE_QUOTED));
        assertTrue(this.localName.isDoubleQuoted());
    }

    @Test
    public void shouldSetModeToSingleQuoted() {
        assertTrue(this.localName.setMode(LocalName.Mode.SINGLE_QUOTED));
        assertTrue(this.localName.isSingleQuoted());
    }

    @Test
    public void shouldSetModeToUnquoted() {
        this.localName.setMode(LocalName.Mode.SINGLE_QUOTED);
        assertTrue(this.localName.setMode(LocalName.Mode.UNQOUTED));
        assertTrue(this.localName.isUnquoted());
    }

    @Test
    public void shouldSetValue() {
        final String NEW_VALUE = "newValue"; //$NON-NLS-1$
        this.localName.set(NEW_VALUE);
        assertEquals(NEW_VALUE, this.localName.get());
    }

    @Test
    public void shouldSetValueToEmpty() {
        this.localName.set(Utils.EMPTY_STRING);
        assertEquals(Utils.EMPTY_STRING, this.localName.get());
    }

    @Test
    public void shouldSetValueToNull() {
        final String NULL_VALUE = null;
        this.localName.set(NULL_VALUE);
        assertNull(this.localName.get());
    }

}
