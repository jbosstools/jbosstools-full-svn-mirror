/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.jboss.tools.modeshape.jcr.cnd;

import static org.junit.Assert.assertTrue;

import org.jboss.tools.modeshape.jcr.Utils;
import org.jboss.tools.modeshape.jcr.cnd.attributes.PropertyType;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 */
public class CndValidatorTest {

    private ChildNodeDefinition childNodeDefinition;
    private CompactNodeTypeDefinition cnd;
    private NamespaceMapping namespaceMapping;
    private NodeTypeDefinition nodeTypeDefinition;
    private PropertyDefinition propertyDefinition;
    
    @Before
    public void beforeEach() {
        this.childNodeDefinition = new ChildNodeDefinition();
        this.cnd = new CompactNodeTypeDefinition();
        this.namespaceMapping = new NamespaceMapping();
        this.nodeTypeDefinition = new NodeTypeDefinition();
        this.propertyDefinition = new PropertyDefinition();
    }

    @Test
    public void childNodeDefinitionWithEmptyNameShouldBeAnError() {
        this.childNodeDefinition.setName(null);
        assertTrue(CndValidator.validateChildNodeDefinition(this.childNodeDefinition).isError());

        this.childNodeDefinition.setName(Utils.EMPTY_STRING);
        assertTrue(CndValidator.validateChildNodeDefinition(this.childNodeDefinition).isError());
    }

    @Test
    public void childNodeDefinitionWithInvalidNameShouldBeAnError() {
        this.childNodeDefinition.setName("invalid/name"); //$NON-NLS-1$
        assertTrue(CndValidator.validateChildNodeDefinition(this.childNodeDefinition).isError());
    }

    @Test
    public void childNodeDefinitionWithInvalidRequiredTypeNameShouldBeAnError() {
        this.childNodeDefinition.setName("name"); //$NON-NLS-1$
        this.childNodeDefinition.addRequiredType("invalid:name"); //$NON-NLS-1$
        assertTrue(CndValidator.validateChildNodeDefinition(this.childNodeDefinition).isError());
    }

    @Test
    public void childNodeDefinitionWithInvalidDefaultTypeNameShouldBeAnError() {
        this.childNodeDefinition.setName("name"); //$NON-NLS-1$
        this.childNodeDefinition.setDefaultPrimaryTypeName("invalid:name"); //$NON-NLS-1$
        assertTrue(CndValidator.validateChildNodeDefinition(this.childNodeDefinition).isError());
    }

    @Test
    public void propertyDefinitionWithEmptyNameShouldNotBeValid() {
        this.propertyDefinition.setName(null);
        assertTrue(CndValidator.validatePropertyDefinition(this.propertyDefinition).isError());

        this.propertyDefinition.setName(Utils.EMPTY_STRING);
        assertTrue(CndValidator.validatePropertyDefinition(this.propertyDefinition).isError());
    }

    @Test
    public void propertyDefinitionWithInvalidNameShouldBeAnError() {
        this.propertyDefinition.setName("invalid/name"); //$NON-NLS-1$
        assertTrue(CndValidator.validatePropertyDefinition(this.propertyDefinition).isError());
    }

    @Test
    public void propertyDefinitionWithMultipleDefaultValuesButSingleValuedShouldBeAnError() {
        this.propertyDefinition.setName("name"); //$NON-NLS-1$
        this.propertyDefinition.addDefaultValue("defaultValue1"); //$NON-NLS-1$
        this.propertyDefinition.addDefaultValue("defaultValue2"); //$NON-NLS-1$

        assertTrue(CndValidator.validatePropertyDefinition(this.propertyDefinition).isError());
    }

    @Test
    public void propertyDefinitionWithInvalidDefaultValueShouldBeAnError() {
        this.propertyDefinition.setName("name"); //$NON-NLS-1$
        this.propertyDefinition.setType(PropertyType.LONG);
        this.propertyDefinition.addDefaultValue("notALongValue"); //$NON-NLS-1$

        assertTrue(CndValidator.validatePropertyDefinition(this.propertyDefinition).isError());
    }

    @Test
    public void nodeTypeDefinitionWithEmptyNameShouldAnError() {
        this.nodeTypeDefinition.setName(null);
        assertTrue(CndValidator.validateNodeTypeDefinition(this.nodeTypeDefinition).isError());

        this.nodeTypeDefinition.setName(Utils.EMPTY_STRING);
        assertTrue(CndValidator.validateNodeTypeDefinition(this.nodeTypeDefinition).isError());
    }

    @Test
    public void nodeTypeDefinitionWithInvalidNameShouldBeAnError() {
        this.nodeTypeDefinition.setName("invalid/name"); //$NON-NLS-1$
        assertTrue(CndValidator.validateNodeTypeDefinition(this.nodeTypeDefinition).isError());
    }

    @Test
    public void nodeTypeDefinitionWithoutPropertiesAndChildNodesShouldBeAWarning() {
        this.nodeTypeDefinition.setName("name"); //$NON-NLS-1$
        assertTrue(CndValidator.validateNodeTypeDefinition(this.nodeTypeDefinition).isWarning());
    }

    @Test
    public void nodeTypeDefinitionWithDuplicatePropertyNamesShouldBeAnError() {
        this.nodeTypeDefinition.setName("nodeTypeName"); //$NON-NLS-1$

        final String NAME = "name"; //$NON-NLS-1$
        this.propertyDefinition.setName(NAME);
        PropertyDefinition prop2 = new PropertyDefinition();
        prop2.setName(NAME);

        this.nodeTypeDefinition.addPropertyDefinition(this.propertyDefinition);
        this.nodeTypeDefinition.addPropertyDefinition(prop2);

        assertTrue(CndValidator.validateNodeTypeDefinition(this.nodeTypeDefinition).isError());
    }

    @Test
    public void nodeTypeDefinitionWithDuplicateChildNodeNamesShouldBeAnError() {
        this.nodeTypeDefinition.setName("nodeTypeName"); //$NON-NLS-1$

        final String NAME = "name"; //$NON-NLS-1$
        this.childNodeDefinition.setName(NAME);
        ChildNodeDefinition child2 = new ChildNodeDefinition();
        child2.setName(NAME);

        this.nodeTypeDefinition.addChildNodeDefinition(this.childNodeDefinition);
        this.nodeTypeDefinition.addChildNodeDefinition(child2);

        assertTrue(CndValidator.validateNodeTypeDefinition(this.nodeTypeDefinition).isError());
    }

    @Test
    public void nodeTypeDefinitionWithInvalidPrimaryItemNameShouldBeAnError() {
        this.nodeTypeDefinition.setName("nodeTypeName"); //$NON-NLS-1$
        this.nodeTypeDefinition.setPrimaryItemName("invalid/name"); //$NON-NLS-1$

        assertTrue(CndValidator.validateNodeTypeDefinition(this.nodeTypeDefinition).isError());
    }

    @Test
    public void nodeTypeDefinitionWithInvalidSuperTypeNameShouldBeAnError() {
        this.nodeTypeDefinition.setName("nodeTypeName"); //$NON-NLS-1$
        this.nodeTypeDefinition.addSuperType("invalid/name"); //$NON-NLS-1$

        assertTrue(CndValidator.validateNodeTypeDefinition(this.nodeTypeDefinition).isError());
    }

    @Test
    public void cndWithoutNamespaceMappingsAndNodeTypeDefintionsShouldBeAWarning() {
        assertTrue(CndValidator.validateCnd(this.cnd).isWarning());
    }

    @Test
    public void localNameEqualToSelfShouldBeAnError() {
        this.childNodeDefinition.setName("."); //$NON-NLS-1$
        assertTrue(CndValidator.validateLocalName(this.childNodeDefinition.getName(), "name").isError()); //$NON-NLS-1$
    }

    @Test
    public void localNameEqualToParentShouldBeAnError() {
        this.childNodeDefinition.setName(".."); //$NON-NLS-1$
        assertTrue(CndValidator.validateLocalName(this.childNodeDefinition.getName(), "name").isError()); //$NON-NLS-1$
    }

    @Test
    public void localNameWithInvalidCharactersShouldBeAnError() {
        this.childNodeDefinition.setName("name/"); //$NON-NLS-1$
        assertTrue(CndValidator.validateLocalName(this.childNodeDefinition.getName(), "name").isError()); //$NON-NLS-1$

        this.childNodeDefinition.setName("name:"); //$NON-NLS-1$
        assertTrue(CndValidator.validateLocalName(this.childNodeDefinition.getName(), "name").isError()); //$NON-NLS-1$

        this.childNodeDefinition.setName("name["); //$NON-NLS-1$
        assertTrue(CndValidator.validateLocalName(this.childNodeDefinition.getName(), "name").isError()); //$NON-NLS-1$

        this.childNodeDefinition.setName("name]"); //$NON-NLS-1$
        assertTrue(CndValidator.validateLocalName(this.childNodeDefinition.getName(), "name").isError()); //$NON-NLS-1$

        this.childNodeDefinition.setName("name|"); //$NON-NLS-1$
        assertTrue(CndValidator.validateLocalName(this.childNodeDefinition.getName(), "name").isError()); //$NON-NLS-1$

        this.childNodeDefinition.setName("name*"); //$NON-NLS-1$
        assertTrue(CndValidator.validateLocalName(this.childNodeDefinition.getName(), "name").isError()); //$NON-NLS-1$
    }

    @Test
    public void emptyNamespaceMappingPrefixShouldBeAnError() {
        assertTrue(CndValidator.validateNamespaceMapping(this.namespaceMapping).isError());
    }

    @Test
    public void emptyNamespaceMappingUriShouldBeAnError() {
        this.namespaceMapping.setUri("uri"); //$NON-NLS-1$
        assertTrue(CndValidator.validateNamespaceMapping(this.namespaceMapping).isError());
    }
}
