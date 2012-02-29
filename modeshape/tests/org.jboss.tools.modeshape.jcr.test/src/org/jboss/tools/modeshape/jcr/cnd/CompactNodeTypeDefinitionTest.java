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
import org.jboss.tools.modeshape.jcr.cnd.CompactNodeTypeDefinition.PropertyName;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 */
public class CompactNodeTypeDefinitionTest {

    private CompactNodeTypeDefinition cnd;
    private NamespaceMapping namespaceMapping;
    private NodeTypeDefinition nodeTypeDefinition;

    @Before
    public void beforeEach() {
        this.cnd = new CompactNodeTypeDefinition();
        this.namespaceMapping = new NamespaceMapping();
        this.nodeTypeDefinition = new NodeTypeDefinition();
    }

    @Test
    public void emptyCndShouldProduceWarning() {
        assertTrue(CndValidator.validateCnd(this.cnd).isWarning());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowNullNamespaceToBeAdded() {
        this.cnd.addNamespaceMapping(null);
    }

    @Test
    public void shouldAddNamespace() {
        assertTrue(this.cnd.addNamespaceMapping(this.namespaceMapping));
        assertEquals(this.namespaceMapping, this.cnd.getNamespaceMappings().iterator().next());
    }

    @Test
    public void shouldReceiveEventAfterAddingNamespace() {
        Listener l = new Listener();
        assertTrue(this.cnd.addListener(l));

        assertTrue(this.cnd.addNamespaceMapping(this.namespaceMapping));

        assertEquals(1, l.getCount());
        assertEquals(PropertyName.NAMESPACE_MAPPINGS.toString(), l.getPropertyName());
        assertEquals(this.namespaceMapping, l.getNewValue());
        assertNull(l.getOldValue());
    }

    @Test
    public void shouldNotReceiveEventAfterUnregistering() {
        Listener l = new Listener();
        assertTrue(this.cnd.addListener(l));
        assertTrue(this.cnd.removeListener(l));

        assertTrue(this.cnd.addNamespaceMapping(this.namespaceMapping));
        assertEquals(0, l.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowNullNodeTypeDefinitionToBeAdded() {
        this.cnd.addNodeTypeDefinition(null);
    }

    @Test
    public void shouldAddNodeTypeDefinition() {
        assertTrue(this.cnd.addNodeTypeDefinition(this.nodeTypeDefinition));
        assertEquals(this.nodeTypeDefinition, this.cnd.getNodeTypeDefinitions().iterator().next());
    }

    @Test
    public void shouldReceiveEventAfterAddingNodeTypeDefinition() {
        Listener l = new Listener();
        assertTrue(this.cnd.addListener(l));

        assertTrue(this.cnd.addNodeTypeDefinition(this.nodeTypeDefinition));

        assertEquals(1, l.getCount());
        assertEquals(PropertyName.NODE_TYPE_DEFINITIONS.toString(), l.getPropertyName());
        assertEquals(this.nodeTypeDefinition, l.getNewValue());
        assertNull(l.getOldValue());
    }

    @Test
    public void shouldClearNamespaceMappings() {
        assertTrue(this.cnd.addNamespaceMapping(this.namespaceMapping));
        assertTrue(this.cnd.clearNamespaceMappings());
    }

    @Test
    public void shouldNotClearNamespaceMappingsWhenEmpty() {
        assertFalse(this.cnd.clearNamespaceMappings());
    }

    @Test
    public void shouldReceiveEventAfterClearingNamespaceMappings() {
        assertTrue(this.cnd.addNamespaceMapping(this.namespaceMapping));
        Collection<NamespaceMapping> oldValue = Collections.singletonList(this.namespaceMapping);

        Listener l = new Listener();
        assertTrue(this.cnd.addListener(l));

        assertTrue(this.cnd.clearNamespaceMappings());
        assertEquals(1, l.getCount());
        assertEquals(PropertyName.NAMESPACE_MAPPINGS.toString(), l.getPropertyName());
        assertNull(l.getNewValue());
        assertTrue(Utils.equivalent(oldValue, (Collection<NamespaceMapping>)l.getOldValue()));
    }

    @Test
    public void shouldClearNodeTypeDefinitions() {
        assertTrue(this.cnd.addNodeTypeDefinition(this.nodeTypeDefinition));
        assertTrue(this.cnd.clearNodeTypeDefinitions());
    }

    @Test
    public void shouldNotClearNodeTypeDefinitionsWhenEmpty() {
        assertFalse(this.cnd.clearNodeTypeDefinitions());
    }

    @Test
    public void shouldReceiveEventAfterClearingNodeTypeDefinitions() {
        assertTrue(this.cnd.addNodeTypeDefinition(this.nodeTypeDefinition));
        Collection<NodeTypeDefinition> oldValue = Collections.singletonList(this.nodeTypeDefinition);

        Listener l = new Listener();
        assertTrue(this.cnd.addListener(l));

        assertTrue(this.cnd.clearNodeTypeDefinitions());
        assertEquals(1, l.getCount());
        assertEquals(PropertyName.NODE_TYPE_DEFINITIONS.toString(), l.getPropertyName());
        assertNull(l.getNewValue());
        assertTrue(Utils.equivalent(oldValue, (Collection<NodeTypeDefinition>)l.getOldValue()));
    }

    @Test
    public void shouldReceiveEventAfterRemoveNamespaceMapping() {
        assertTrue(this.cnd.addNamespaceMapping(this.namespaceMapping));

        Listener l = new Listener();
        assertTrue(this.cnd.addListener(l));

        assertTrue(this.cnd.removeNamespaceMapping(this.namespaceMapping));
        assertEquals(1, l.getCount());
        assertEquals(PropertyName.NAMESPACE_MAPPINGS.toString(), l.getPropertyName());
        assertEquals(this.namespaceMapping, l.getOldValue());
        assertNull(l.getNewValue());
    }

    @Test
    public void shouldRemoveNamespaceMapping() {
        assertTrue(this.cnd.addNamespaceMapping(this.namespaceMapping));
        assertTrue(this.cnd.removeNamespaceMapping(this.namespaceMapping));
        assertEquals(0, this.cnd.getNamespaceMappings().size());
    }

    @Test
    public void shouldNotRemoveNamespaceMappingThatDoesNotExist() {
        assertFalse(this.cnd.removeNamespaceMapping(this.namespaceMapping));
    }

    @Test
    public void shouldReceiveEventAfterRemoveNodeTypeDefinition() {
        assertTrue(this.cnd.addNodeTypeDefinition(this.nodeTypeDefinition));

        Listener l = new Listener();
        assertTrue(this.cnd.addListener(l));

        assertTrue(this.cnd.removeNodeTypeDefinition(this.nodeTypeDefinition));
        assertEquals(1, l.getCount());
        assertEquals(PropertyName.NODE_TYPE_DEFINITIONS.toString(), l.getPropertyName());
        assertEquals(this.nodeTypeDefinition, l.getOldValue());
        assertNull(l.getNewValue());
    }

    @Test
    public void shouldRemoveNodeTypeDefinition() {
        assertTrue(this.cnd.addNodeTypeDefinition(this.nodeTypeDefinition));
        assertTrue(this.cnd.removeNodeTypeDefinition(this.nodeTypeDefinition));
        assertEquals(0, this.cnd.getNodeTypeDefinitions().size());
    }

    @Test
    public void shouldNotRemoveNodeTypeDefinitionThatDoesNotExist() {
        assertFalse(this.cnd.removeNodeTypeDefinition(this.nodeTypeDefinition));
    }
}
