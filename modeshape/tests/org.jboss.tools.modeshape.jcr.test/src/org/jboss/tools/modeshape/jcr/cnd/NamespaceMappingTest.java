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
import static org.junit.Assert.assertTrue;

import org.jboss.tools.modeshape.jcr.Listener;
import org.jboss.tools.modeshape.jcr.Utils;
import org.jboss.tools.modeshape.jcr.cnd.CndElement.NotationType;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 */
public class NamespaceMappingTest {

    private NamespaceMapping namespaceMapping;

    @Before
    public void beforeEach() {
        this.namespaceMapping = new NamespaceMapping();
    }

    @Test
    public void compactCndNotationShouldBeCorrect() {
        final String PREFIX = "prefix"; //$NON-NLS-1$
        final String URI = "uri"; //$NON-NLS-1$
        this.namespaceMapping = new NamespaceMapping(PREFIX, URI);
        assertEquals(NamespaceMapping.NOTATION_PREFIX + PREFIX + NamespaceMapping.NOTATION_DELIMITER + URI
                + NamespaceMapping.NOTATION_SUFFIX, this.namespaceMapping.toCndNotation(NotationType.COMPACT));
    }

    @Test
    public void compressedCndNotationShouldBeCorrect() {
        final String PREFIX = "prefix"; //$NON-NLS-1$
        final String URI = "uri"; //$NON-NLS-1$
        this.namespaceMapping = new NamespaceMapping(PREFIX, URI);
        assertEquals(NamespaceMapping.NOTATION_PREFIX + PREFIX + NamespaceMapping.NOTATION_DELIMITER + URI
                + NamespaceMapping.NOTATION_SUFFIX, this.namespaceMapping.toCndNotation(NotationType.COMPRESSED));
    }

    @Test
    public void longCndNotationShouldBeCorrect() {
        final String PREFIX = "prefix"; //$NON-NLS-1$
        final String URI = "uri"; //$NON-NLS-1$
        this.namespaceMapping = new NamespaceMapping(PREFIX, URI);
        assertEquals(NamespaceMapping.NOTATION_PREFIX + PREFIX + NamespaceMapping.NOTATION_DELIMITER + URI
                + NamespaceMapping.NOTATION_SUFFIX, this.namespaceMapping.toCndNotation(NotationType.LONG));
    }

    @Test
    public void shouldHaveEmptyPrefixAfterConstruction() {
        assertTrue(Utils.isEmpty(this.namespaceMapping.getPrefix()));
    }

    @Test
    public void shouldHaveEmptyUriAfterConstruction() {
        assertTrue(Utils.isEmpty(this.namespaceMapping.getUri()));
    }

    @Test
    public void shouldNotReceivePropertyChangeEventsWhenUnregistered() {
        Listener l = new Listener();
        this.namespaceMapping.addListener(l);
        this.namespaceMapping.removeListener(l);

        assertTrue(this.namespaceMapping.setPrefix("newPrefix")); //$NON-NLS-1$
        assertEquals(0, l.getCount());
        assertTrue(this.namespaceMapping.setUri("newUri")); //$NON-NLS-1$
        assertEquals(0, l.getCount());
    }

    @Test
    public void shouldNotSetPrefixToSameValue() {
        final String PREFIX = "prefix"; //$NON-NLS-1$
        assertTrue(this.namespaceMapping.setPrefix(PREFIX));
        assertFalse(this.namespaceMapping.setPrefix(PREFIX));
    }

    @Test
    public void shouldNotSetUriToSameValue() {
        final String URI = "uri"; //$NON-NLS-1$
        assertTrue(this.namespaceMapping.setUri(URI));
        assertFalse(this.namespaceMapping.setUri(URI));
    }

    @Test
    public void shouldReceivePropertyChangeEventWhenPrefixIsChanged() {
        Listener l = new Listener();
        this.namespaceMapping.addListener(l);

        final String OLD_VALUE = this.namespaceMapping.getPrefix();
        final String NEW_VALUE = "prefix"; //$NON-NLS-1$
        assertTrue(this.namespaceMapping.setPrefix(NEW_VALUE));
        assertEquals(1, l.getCount());
        assertEquals(NamespaceMapping.PropertyName.PREFIX.toString(), l.getPropertyName());
        assertEquals(OLD_VALUE, l.getOldValue());
        assertEquals(NEW_VALUE, l.getNewValue());
    }

    @Test
    public void shouldReceivePropertyChangeEventWhenUriIsChanged() {
        Listener l = new Listener();
        this.namespaceMapping.addListener(l);

        final String OLD_VALUE = this.namespaceMapping.getUri();
        final String NEW_VALUE = "uri"; //$NON-NLS-1$
        assertTrue(this.namespaceMapping.setUri(NEW_VALUE));
        assertEquals(1, l.getCount());
        assertEquals(NamespaceMapping.PropertyName.URI.toString(), l.getPropertyName());
        assertEquals(OLD_VALUE, l.getOldValue());
        assertEquals(NEW_VALUE, l.getNewValue());
    }

    @Test
    public void shouldSetPrefix() {
        final String PREFIX = "prefix"; //$NON-NLS-1$
        assertTrue(this.namespaceMapping.setPrefix(PREFIX));
        assertEquals(PREFIX, this.namespaceMapping.getPrefix());
    }

    @Test
    public void shouldSetPrefixAtConstruction() {
        final String PREFIX = "prefix"; //$NON-NLS-1$
        this.namespaceMapping = new NamespaceMapping(PREFIX, null);
        assertEquals(PREFIX, this.namespaceMapping.getPrefix());
    }

    @Test
    public void shouldSetUri() {
        final String URI = "uri"; //$NON-NLS-1$
        assertTrue(this.namespaceMapping.setUri(URI));
        assertEquals(URI, this.namespaceMapping.getUri());
    }

    @Test
    public void shouldSetUriAtConstruction() {
        final String URI = "uri"; //$NON-NLS-1$
        this.namespaceMapping = new NamespaceMapping(null, URI);
        assertEquals(URI, this.namespaceMapping.getUri());
    }

    @Test
    public void twoNamespaceMappingsWithDifferentPrefixesAndSameUriShouldHaveDifferentHashCodes() {
        NamespaceMapping namespace1 = new NamespaceMapping("prefix", "uri"); //$NON-NLS-1$ //$NON-NLS-2$
        NamespaceMapping namespace2 = new NamespaceMapping(namespace1.getPrefix() + "different", namespace1.getUri()); //$NON-NLS-1$

        assertFalse(namespace1.hashCode() == namespace2.hashCode());
    }

    @Test
    public void twoNamespaceMappingsWithDifferentPrefixesAndSameUriShouldNotBeEqual() {
        NamespaceMapping namespace1 = new NamespaceMapping("prefix", "uri"); //$NON-NLS-1$ //$NON-NLS-2$
        NamespaceMapping namespace2 = new NamespaceMapping(namespace1.getPrefix() + "different", namespace1.getUri()); //$NON-NLS-1$

        assertFalse(namespace1.equals(namespace2));
    }

    @Test
    public void twoNamespaceMappingsWithSamePrefixAndDifferentUrisShouldNotBeEqual() {
        NamespaceMapping namespace1 = new NamespaceMapping("prefix", "uri"); //$NON-NLS-1$ //$NON-NLS-2$
        NamespaceMapping namespace2 = new NamespaceMapping(namespace1.getPrefix(), namespace1.getUri() + "different"); //$NON-NLS-1$

        assertFalse(namespace1.equals(namespace2));
    }

    @Test
    public void twoNamespaceMappingsWithSamePrefixAndDifferentUrisShouldNotHaveSameHashCode() {
        NamespaceMapping namespace1 = new NamespaceMapping("prefix", "uri"); //$NON-NLS-1$ //$NON-NLS-2$
        NamespaceMapping namespace2 = new NamespaceMapping(namespace1.getPrefix(), namespace1.getUri() + "different"); //$NON-NLS-1$

        assertFalse(namespace1.hashCode() == namespace2.hashCode());
    }

    @Test
    public void twoNamespaceMappingsWithSamePrefixAndSameUriShouldBeEqual() {
        NamespaceMapping namespace1 = new NamespaceMapping("prefix", "uri"); //$NON-NLS-1$ //$NON-NLS-2$
        NamespaceMapping namespace2 = new NamespaceMapping(namespace1.getPrefix(), namespace1.getUri());

        assertTrue(namespace1.equals(namespace2));
    }

    @Test
    public void twoNamespaceMappingsWithSamePrefixAndSameUriShouldHaveSameHashCode() {
        NamespaceMapping namespace1 = new NamespaceMapping("prefix", "uri"); //$NON-NLS-1$ //$NON-NLS-2$
        NamespaceMapping namespace2 = new NamespaceMapping(namespace1.getPrefix(), namespace1.getUri());

        assertEquals(namespace1.hashCode(), namespace2.hashCode());
    }

}
