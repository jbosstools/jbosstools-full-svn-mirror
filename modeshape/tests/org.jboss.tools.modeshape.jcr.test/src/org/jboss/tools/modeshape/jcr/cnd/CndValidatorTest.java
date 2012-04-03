/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.jboss.tools.modeshape.jcr.cnd;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.tools.modeshape.jcr.ChildNodeDefinition;
import org.jboss.tools.modeshape.jcr.ItemDefinition;
import org.jboss.tools.modeshape.jcr.NamespaceMapping;
import org.jboss.tools.modeshape.jcr.NodeTypeDefinition;
import org.jboss.tools.modeshape.jcr.PropertyDefinition;
import org.jboss.tools.modeshape.jcr.QualifiedName;
import org.jboss.tools.modeshape.jcr.Utils;
import org.jboss.tools.modeshape.jcr.attributes.PropertyType;
import org.jboss.tools.modeshape.jcr.attributes.QueryOperators.QueryOperator;
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
        this.childNodeDefinition.setName(Constants.QUALIFIED_NAME1.get());

        this.cnd = new CompactNodeTypeDefinition();
        this.namespaceMapping = new NamespaceMapping();

        this.nodeTypeDefinition = new NodeTypeDefinition();
        this.nodeTypeDefinition.setName(Constants.QUALIFIED_NAME1.get());

        this.propertyDefinition = new PropertyDefinition();
        this.propertyDefinition.setName(Constants.QUALIFIED_NAME1.get());
    }

    @Test
    public void childNodeDefinitionWithEmptyNameShouldBeAnError() {
        this.childNodeDefinition.setName(null);
        assertTrue(CndValidator.validateChildNodeDefinition(this.childNodeDefinition, null, null).isError());

        this.childNodeDefinition.setName(Utils.EMPTY_STRING);
        assertTrue(CndValidator.validateChildNodeDefinition(this.childNodeDefinition, null, null).isError());
    }

    @Test
    public void childNodeDefinitionWithInvalidDefaultTypeNameShouldBeAnError() {
        this.childNodeDefinition.setName("name"); //$NON-NLS-1$
        this.childNodeDefinition.setDefaultPrimaryTypeName("missingName:"); //$NON-NLS-1$
        assertTrue(CndValidator.validateChildNodeDefinition(this.childNodeDefinition, null, null).isError());
    }

    @Test
    public void childNodeDefinitionWithInvalidNameShouldBeAnError() {
        this.childNodeDefinition.setName("invalid/name"); //$NON-NLS-1$
        assertTrue(CndValidator.validateChildNodeDefinition(this.childNodeDefinition, null, null).isError());
    }

    @Test
    public void childNodeDefinitionWithInvalidRequiredTypeNameShouldBeAnError() {
        this.childNodeDefinition.setName("name"); //$NON-NLS-1$
        this.childNodeDefinition.addRequiredType("missingName:"); //$NON-NLS-1$
        assertTrue(CndValidator.validateChildNodeDefinition(this.childNodeDefinition, null, null).isError());
    }

    @Test
    public void childNodeNameWithNonMatchingQualifierShouldBeAnError() {
        this.childNodeDefinition.setName(Constants.NAME_WITH_NON_DEFAULT_QUALIFIER.get());
        assertTrue(CndValidator.validateName(this.childNodeDefinition, Constants.Helper.getDefaultNamespacePrefixes(), null)
                               .isError());
    }

    @Test
    public void cndWithoutNamespaceMappingsAndNodeTypeDefintionsShouldBeAWarning() {
        assertTrue(CndValidator.validateCnd(this.cnd).isWarning());
    }

    @Test
    public void defaultTypeNameWithNonMatchingQualifierShouldBeAnError() {
        this.childNodeDefinition.setName(Constants.QUALIFIED_NAME1.get());
        this.childNodeDefinition.setDefaultPrimaryTypeName(Constants.NAME_WITH_NON_DEFAULT_QUALIFIER.get());
        assertTrue(CndValidator.validateDefaultType(this.childNodeDefinition, Constants.Helper.getDefaultNamespacePrefixes())
                               .isError());
    }

    @Test
    public void defaultValueWithInvalidQualifierShouldBeAnError() {
        assertTrue(this.propertyDefinition.setType(PropertyType.NAME));
        assertTrue(this.propertyDefinition.addDefaultValue("bogus:value")); //$NON-NLS-1$
        assertTrue(CndValidator.validateDefaultValues(this.propertyDefinition, Constants.Helper.getDefaultNamespacePrefixes())
                               .isError());
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

    @Test
    public void emptyQualifiedNameQualifierShouldBeValid() {
        assertTrue(CndValidator.validateQualifiedName(Constants.NAME_WITH_EMPTY_QUALIFIER, "propertyName", //$NON-NLS-1$
                                                      Constants.Helper.getDefaultQualifiers(), null).isOk());
    }

    @Test
    public void emptyQueryOperatorShouldBeAnError() {
        assertTrue(CndValidator.validateQueryOperator(Utils.EMPTY_STRING, "propName").isError()); //$NON-NLS-1$
    }

    @Test
    public void invalidQualifiedNameQualifierShouldBeAnError() {
        final QualifiedName qname = new QualifiedName(Constants.QUALIFIER1 + "Changed", Constants.UNQUALIFIED_NAME1); //$NON-NLS-1$
        assertTrue(CndValidator.validateQualifiedName(qname, "propertyName", Constants.Helper.getDefaultQualifiers(), null).isError()); //$NON-NLS-1$
    }

    @Test
    public void invalidQueryOperatorShouldBeAnError() {
        assertTrue(CndValidator.validateQueryOperator("a", "propName").isError()); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Test
    public void localNameEqualToParentShouldBeAnError() {
        this.childNodeDefinition.setName(".."); //$NON-NLS-1$
        assertTrue(CndValidator.validateLocalName(this.childNodeDefinition.getName(), "name").isError()); //$NON-NLS-1$
    }

    @Test
    public void localNameEqualToSelfShouldBeAnError() {
        this.childNodeDefinition.setName("."); //$NON-NLS-1$
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
    public void nodeTypeDefinitionWithDuplicateChildNodeNamesShouldBeAnError() {
        this.nodeTypeDefinition.setName("nodeTypeName"); //$NON-NLS-1$

        final String NAME = "name"; //$NON-NLS-1$
        this.childNodeDefinition.setName(NAME);
        final ChildNodeDefinition child2 = new ChildNodeDefinition();
        child2.setName(NAME);

        this.nodeTypeDefinition.addChildNodeDefinition(this.childNodeDefinition);
        this.nodeTypeDefinition.addChildNodeDefinition(child2);

        assertTrue(CndValidator.validateNodeTypeDefinition(this.nodeTypeDefinition, null, null, true).isError());
    }

    @Test
    public void nodeTypeDefinitionWithDuplicatePropertyNamesShouldBeAnError() {
        this.nodeTypeDefinition.setName("nodeTypeName"); //$NON-NLS-1$

        final String NAME = "name"; //$NON-NLS-1$
        this.propertyDefinition.setName(NAME);
        final PropertyDefinition prop2 = new PropertyDefinition();
        prop2.setName(NAME);

        this.nodeTypeDefinition.addPropertyDefinition(this.propertyDefinition);
        this.nodeTypeDefinition.addPropertyDefinition(prop2);

        assertTrue(CndValidator.validateNodeTypeDefinition(this.nodeTypeDefinition, null, null, true).isError());
    }

    @Test
    public void nodeTypeDefinitionWithEmptyNameShouldAnError() {
        this.nodeTypeDefinition.setName(null);
        assertTrue(CndValidator.validateNodeTypeDefinition(this.nodeTypeDefinition, null, null, false).isError());

        this.nodeTypeDefinition.setName(Utils.EMPTY_STRING);
        assertTrue(CndValidator.validateNodeTypeDefinition(this.nodeTypeDefinition, null, null, false).isError());
    }

    @Test
    public void nodeTypeDefinitionWithInvalidNameShouldBeAnError() {
        this.nodeTypeDefinition.setName("invalid/name"); //$NON-NLS-1$
        assertTrue(CndValidator.validateNodeTypeDefinition(this.nodeTypeDefinition, null, null, false).isError());
    }

    @Test
    public void nodeTypeDefinitionWithInvalidPrimaryItemNameShouldBeAnError() {
        this.nodeTypeDefinition.setName("nodeTypeName"); //$NON-NLS-1$
        this.nodeTypeDefinition.setPrimaryItemName("invalid/name"); //$NON-NLS-1$

        assertTrue(CndValidator.validateNodeTypeDefinition(this.nodeTypeDefinition, null, null, false).isError());
    }

    @Test
    public void nodeTypeDefinitionWithInvalidSuperTypeNameShouldBeAnError() {
        this.nodeTypeDefinition.setName("nodeTypeName"); //$NON-NLS-1$
        this.nodeTypeDefinition.addSuperType("invalid/name"); //$NON-NLS-1$

        assertTrue(CndValidator.validateNodeTypeDefinition(this.nodeTypeDefinition, null, null, false).isError());
    }

    @Test
    public void nodeTypeDefinitionWithoutPropertiesAndChildNodesShouldBeAWarning() {
        this.nodeTypeDefinition.setName("name"); //$NON-NLS-1$
        assertTrue(CndValidator.validateNodeTypeDefinition(this.nodeTypeDefinition, null, null, false).isWarning());
    }

    @Test
    public void nodeTypeNameWithNonMatchingQualifierShouldBeAnError() {
        this.nodeTypeDefinition.setName(Constants.NAME_WITH_NON_DEFAULT_QUALIFIER.get());
        assertTrue(CndValidator.validateName(this.nodeTypeDefinition, Constants.Helper.getDefaultNamespacePrefixes(), null)
                               .isError());
    }

    @Test
    public void nullQueryOperatorShouldBeAnError() {
        assertTrue(CndValidator.validateQueryOperator(null, "propName").isError()); //$NON-NLS-1$
    }

    @Test
    public void primaryItemNameWithNonMatchingQualifierShouldBeAnError() {
        this.nodeTypeDefinition.setName(Constants.QUALIFIED_NAME1.get());
        this.nodeTypeDefinition.setPrimaryItemName(Constants.NAME_WITH_NON_DEFAULT_QUALIFIER.get());
        assertTrue(CndValidator.validateNodeTypeDefinition(this.nodeTypeDefinition, Constants.Helper.getDefaultNamespacePrefixes(),
                                                           null, false).isError());
    }

    @Test
    public void propertyDefinitionWithEmptyNameShouldNotBeValid() {
        this.propertyDefinition.setName(null);
        assertTrue(CndValidator.validatePropertyDefinition(this.propertyDefinition, null, null).isError());

        this.propertyDefinition.setName(Utils.EMPTY_STRING);
        assertTrue(CndValidator.validatePropertyDefinition(this.propertyDefinition, null, null).isError());
    }

    @Test
    public void propertyDefinitionWithInvalidDefaultValueShouldBeAnError() {
        this.propertyDefinition.setName("name"); //$NON-NLS-1$
        this.propertyDefinition.setType(PropertyType.LONG);
        this.propertyDefinition.addDefaultValue("notALongValue"); //$NON-NLS-1$

        assertTrue(CndValidator.validatePropertyDefinition(this.propertyDefinition, null, null).isError());
    }

    @Test
    public void propertyDefinitionWithInvalidNameShouldBeAnError() {
        this.propertyDefinition.setName("invalid/name"); //$NON-NLS-1$
        assertTrue(CndValidator.validatePropertyDefinition(this.propertyDefinition, null, null).isError());
    }

    @Test
    public void propertyDefinitionWithMultipleDefaultValuesButSingleValuedShouldBeAnError() {
        this.propertyDefinition.setName("name"); //$NON-NLS-1$
        this.propertyDefinition.addDefaultValue("defaultValue1"); //$NON-NLS-1$
        this.propertyDefinition.addDefaultValue("defaultValue2"); //$NON-NLS-1$

        assertTrue(CndValidator.validatePropertyDefinition(this.propertyDefinition, null, null).isError());
    }

    @Test
    public void propertyNameWithNonMatchingQualifierShouldBeAnError() {
        this.propertyDefinition.setName(Constants.NAME_WITH_NON_DEFAULT_QUALIFIER.get());
        assertTrue(CndValidator.validateName(this.propertyDefinition, Constants.Helper.getDefaultNamespacePrefixes(), null)
                               .isError());
    }

    @Test
    public void requiredTypeNameWithNonMatchingQualifierShouldBeAnError() {
        this.childNodeDefinition.setName(Constants.QUALIFIED_NAME1.get());
        this.childNodeDefinition.addRequiredType(Constants.NAME_WITH_NON_DEFAULT_QUALIFIER.get());
        assertTrue(CndValidator.validateRequiredTypes(this.childNodeDefinition, Constants.Helper.getDefaultNamespacePrefixes())
                               .isError());
    }

    @Test
    public void shouldAllowChildNodeDefinitionsWithResidualNames() {
        this.childNodeDefinition.setName(ItemDefinition.RESIDUAL_NAME);
        assertTrue(CndValidator.validateName(this.childNodeDefinition, null, null).isOk());
    }

    @Test
    public void shouldAllowMultipleChildNodeDefinitionsWithResidualNames() {
        this.nodeTypeDefinition.setName("nodeName"); //$NON-NLS-1$
        this.childNodeDefinition.setName(ItemDefinition.RESIDUAL_NAME);
        final ChildNodeDefinition childNode2 = new ChildNodeDefinition();
        childNode2.setName(ItemDefinition.RESIDUAL_NAME);
        this.nodeTypeDefinition.addChildNodeDefinition(this.childNodeDefinition);
        this.nodeTypeDefinition.addChildNodeDefinition(childNode2);
        assertTrue(CndValidator.validateChildNodeDefinitions(this.nodeTypeDefinition.getName(), null,
                                                             this.nodeTypeDefinition.getChildNodeDefinitions()).isOk());
    }

    @Test
    public void shouldAllowMultiplePropertyDefinitionsWithResidualNames() {
        this.nodeTypeDefinition.setName("nodeName"); //$NON-NLS-1$
        this.propertyDefinition.setName(ItemDefinition.RESIDUAL_NAME);
        final PropertyDefinition propDefn2 = new PropertyDefinition();
        propDefn2.setName(ItemDefinition.RESIDUAL_NAME);
        this.nodeTypeDefinition.addPropertyDefinition(this.propertyDefinition);
        this.nodeTypeDefinition.addPropertyDefinition(propDefn2);
        assertTrue(CndValidator.validatePropertyDefinitions(this.nodeTypeDefinition.getName(), null,
                                                            this.nodeTypeDefinition.getPropertyDefinitions()).isOk());
    }

    @Test
    public void shouldAllowPropertyDefinitionsWithResidualNames() {
        this.propertyDefinition.setName(ItemDefinition.RESIDUAL_NAME);
        assertTrue(CndValidator.validateName(this.propertyDefinition, null, null).isOk());
    }

    @Test
    public void shouldNotAllowChildNodeDefinitionsWithSameName() {
        this.nodeTypeDefinition.setName("nodeName"); //$NON-NLS-1$
        this.childNodeDefinition.setName("name"); //$NON-NLS-1$
        final ChildNodeDefinition childNode2 = new ChildNodeDefinition();
        childNode2.setName(this.childNodeDefinition.getName());
        this.nodeTypeDefinition.addChildNodeDefinition(this.childNodeDefinition);
        this.nodeTypeDefinition.addChildNodeDefinition(childNode2);
        assertTrue(CndValidator.validateChildNodeDefinitions(this.nodeTypeDefinition.getName(), null,
                                                             this.nodeTypeDefinition.getChildNodeDefinitions()).isError());
    }

    @Test
    public void shouldNotAllowDuplicateNamespacePrefixes() {
        // create a namespace mapping with a prefix that already exists and a URI that doesn't exist in the default namespaces
        final NamespaceMapping namespaceMapping = new NamespaceMapping(Constants.NAMESPACE_PREFIX1, "xyz"); //$NON-NLS-1$
        assertTrue(CndValidator.validateNamespaceMapping(namespaceMapping, Constants.Helper.getDefaultNamespaces()).isError());
    }

    @Test
    public void shouldNotAllowDuplicateNamespaceUris() {
        // create a namespace mapping with a URI that already exists and a prefix that doesn't exist in the default namespaces
        final NamespaceMapping namespaceMapping = new NamespaceMapping("xyz", Constants.NAMESPACE_URI1); //$NON-NLS-1$
        assertTrue(CndValidator.validateNamespaceMapping(namespaceMapping, Constants.Helper.getDefaultNamespaces()).isError());
    }

    @Test
    public void shouldNotAllowDuplicateQualifiedNames() {
        assertTrue(CndValidator.validateQualifiedName(Constants.QUALIFIED_NAME1,
                                                      "propertyName", Constants.Helper.getDefaultQualifiers(), Constants.Helper.getDefaultQualifiedNames()).isError()); //$NON-NLS-1$
    }

    @Test
    public void shouldNotAllowPropertyDefinitionsWithSameName() {
        this.nodeTypeDefinition.setName("nodeName"); //$NON-NLS-1$
        this.propertyDefinition.setName("name"); //$NON-NLS-1$
        final PropertyDefinition propDefn2 = new PropertyDefinition();
        propDefn2.setName(this.propertyDefinition.getName());
        this.nodeTypeDefinition.addPropertyDefinition(this.propertyDefinition);
        this.nodeTypeDefinition.addPropertyDefinition(propDefn2);
        assertTrue(CndValidator.validatePropertyDefinitions(this.nodeTypeDefinition.getName(), null,
                                                            this.nodeTypeDefinition.getPropertyDefinitions()).isError());
    }

    @Test
    public void shouldValidateAllQueryOperators() {
        for (final QueryOperator operator : QueryOperator.values()) {
            assertTrue(CndValidator.validateQueryOperator(operator.toString(), "propName").isOk()); //$NON-NLS-1$
        }
    }

    @Test
    public void superTypeNameWithNonMatchingQualifierShouldBeAnError() {
        this.nodeTypeDefinition.setName(Constants.QUALIFIED_NAME1.get());
        this.nodeTypeDefinition.addSuperType(Constants.NAME_WITH_NON_DEFAULT_QUALIFIER.get());
        assertTrue(CndValidator.validateSuperTypes(this.nodeTypeDefinition.getName(),
                                                   Constants.Helper.getDefaultNamespacePrefixes(),
                                                   this.nodeTypeDefinition.getState(NodeTypeDefinition.PropertyName.SUPERTYPES),
                                                   this.nodeTypeDefinition.getSupertypes()).isError());
    }

    @Test
    public void shouldValidatePathsWithNoErrors() {
        final String PROP_NAME = "testProperty"; //$NON-NLS-1$
        assertFalse(CndValidator.validatePath("A", PROP_NAME).isError()); //$NON-NLS-1$
        assertFalse(CndValidator.validatePath("A/B/C[1]/D[2]", PROP_NAME).isError()); //$NON-NLS-1$
        assertFalse(CndValidator.validatePath("A/B/C[1]/../../D[2]", PROP_NAME).isError()); //$NON-NLS-1$
        assertFalse(CndValidator.validatePath("A/B/C[1]/././D[2]", PROP_NAME).isError()); //$NON-NLS-1$
        assertFalse(CndValidator.validatePath("A/B/C[1]/../D[2]/./E/..", PROP_NAME).isError()); //$NON-NLS-1$
        assertFalse(CndValidator.validatePath("A/../../../B", PROP_NAME).isError()); //$NON-NLS-1$
        assertFalse(CndValidator.validatePath("../../B", PROP_NAME).isError()); //$NON-NLS-1$
        assertFalse(CndValidator.validatePath("A/./../../B/.", PROP_NAME).isError()); //$NON-NLS-1$
        assertFalse(CndValidator.validatePath("/.", PROP_NAME).isError()); //$NON-NLS-1$
        assertFalse(CndValidator.validatePath(".", PROP_NAME).isError()); //$NON-NLS-1$
        assertFalse(CndValidator.validatePath("/A", PROP_NAME).isError()); //$NON-NLS-1$
        assertFalse(CndValidator.validatePath("/A/B/C[1]/D[2]", PROP_NAME).isError()); //$NON-NLS-1$
        assertFalse(CndValidator.validatePath("/A/B/C[1]/../../D[2]", PROP_NAME).isError()); //$NON-NLS-1$
        assertFalse(CndValidator.validatePath("/A/B/C[1]/././D[2]", PROP_NAME).isError()); //$NON-NLS-1$
        assertFalse(CndValidator.validatePath("/A/B/C[1]/../D[2]/./E/..", PROP_NAME).isError()); //$NON-NLS-1$
    }
}
