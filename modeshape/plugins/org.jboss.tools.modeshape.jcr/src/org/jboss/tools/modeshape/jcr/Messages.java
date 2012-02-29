/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.jboss.tools.modeshape.jcr;

import org.eclipse.osgi.util.NLS;

/**
 * 
 */
public final class Messages extends NLS {

    public static String childDefinitionName;
    public static String cndHasNoNamespacesOrNodeTypeDefinitions;
    public static String defaultTypeExistsButMarkedAsVariant;
    public static String defaultTypeName;
    public static String defaultValue;
    public static String defaultValuesExistButMarkedAsVariant;
    public static String duplicateChildNodeDefinitionName;
    public static String duplicateDefaultValue;
    public static String duplicateNamespacePrefix;
    public static String duplicateNamespaceUri;
    public static String duplicateNodeTypeDefinitionName;
    public static String duplicatePropertyDefinitionName;
    public static String duplicateQueryOperator;
    public static String duplicateRequiredType;
    public static String duplicateSuperType;
    public static String duplicateValueConstraint;
    public static String emptyDefaultValues;
    public static String emptyLocalName;
    public static String emptyQueryOperator;
    public static String emptyQueryOperators;
    public static String emptyRequiredTypes;
    public static String emptySuperTypes;
    public static String emptyValue;
    public static String emptyValueConstraints;
    public static String errorImportingCndContent;
    public static String errorValidatingPropertyValueForType;
    public static String expectedNamespaceOrNodeDefinition;
    public static String expectedValidQueryOperator;
    public static String invalidFindRequest;
    public static String invalidFindUsingJcrValueRequest;
    public static String invalidGetStateRequest;
    public static String invalidQueryOperator;
    public static String invalidPropertyValueForType;
    public static String invalidUri;
    public static String localNameEqualToSelfOrParent;
    public static String localNameHasInvalidCharacters;
    public static String missingName;
    public static String multipleDefaultValuesForSingleValuedProperty;
    public static String multipleKeywordNotValidInJcr2CndFormat;
    public static String namespacePrefix;
    public static String namespaceUri;
    public static String nodeTypeDefinitionHasNoPropertyDefinitionsOrChildNodeDefinitions;
    public static String nodeTypeDefinitionName;
    public static String okValidationMsg;
    public static String objectIsNull;
    public static String primaryItemExistsButMarkedAsVariant;
    public static String primaryItemName;
    public static String primaryKeywordNotValidInJcr2CndFormat;
    public static String propertyDefinitionName;
    public static String queryOperatorsExistButMarkedAsVariant;
    public static String requiredTypeName;
    public static String requiredTypesExistButMarkedAsVariant;
    public static String stringIsEmpty;
    public static String superTypeName;
    public static String superTypesExistButMarkedAsVariant;
    public static String valueConstraintsExistButMarkedAsVariant;
    public static String vendorBlockWasNotClosed;

    static {
        NLS.initializeMessages("org.jboss.tools.modeshape.jcr.messages", Messages.class); //$NON-NLS-1$
    }

    /**
     * Don't allow construction;
     */
    private Messages() {
        // nothing to do
    }
}
